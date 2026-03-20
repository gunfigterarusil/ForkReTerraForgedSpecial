package rayman.rtfs.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.base.Suppliers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction.NoiseHolder;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import rayman.rtfs.RTFCommon;
import rayman.rtfs.concurrent.ThreadPools;
import rayman.rtfs.config.PerformanceConfig;
import rayman.rtfs.data.worldgen.preset.Preset;
import rayman.rtfs.registries.RTFRegistries;
import rayman.rtfs.tags.RTFDensityFunctionTags;
import rayman.rtfs.world.worldgen.GeneratorContext;
import rayman.rtfs.world.worldgen.RTFRandomState;
import rayman.rtfs.world.worldgen.densityfunction.CellSampler;
import rayman.rtfs.world.worldgen.densityfunction.NoiseFunction;
import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.noise.module.Noises;
import rayman.rtfs.world.worldgen.terrablender.TBClimateSampler;
import rayman.rtfs.world.worldgen.terrablender.TBCompat;

@Mixin(RandomState.class)
@Implements(@Interface(iface = RTFRandomState.class, prefix = "rtfs$RTFRandomState$"))
class MixinRandomState {
	private DensityFunction.Visitor densityFunctionWrapper;
	@Shadow
	@Final
	private Climate.Sampler sampler;
	@Shadow
	@Final
    private SurfaceSystem surfaceSystem;

	@Deprecated
	private volatile boolean hasContext;
	@Nullable
	private GeneratorContext generatorContext;
	@Nullable
	private Preset preset;

	private long seed;

	@Redirect(
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseRouter;mapAll(Lnet/minecraft/world/level/levelgen/DensityFunction$Visitor;)Lnet/minecraft/world/level/levelgen/NoiseRouter;"
		),
		method = "<init>",
		require = 1
	)
	private NoiseRouter RandomState(NoiseRouter router, DensityFunction.Visitor visitor, NoiseGeneratorSettings noiseGeneratorSettings, HolderGetter<NormalNoise.NoiseParameters> params, final long seed) {
		this.seed = seed;
		this.densityFunctionWrapper = new DensityFunction.Visitor() {

			@Override
			public DensityFunction apply(DensityFunction function) {
				if(function instanceof NoiseFunction.Marker marker) {
					return new NoiseFunction(marker.noise(), (int) seed);
				}
				if(function instanceof CellSampler.Marker marker) {
					MixinRandomState.this.hasContext |= true;
					return new CellSampler(Suppliers.memoize(() -> MixinRandomState.this.generatorContext.lookup), marker.field());
				}
				return visitor.apply(function);
			}

			@Override
			public NoiseHolder visitNoise(NoiseHolder noiseHolder) {
	            return visitor.visitNoise(noiseHolder);
	        }
		};
		return router.mapAll(this.densityFunctionWrapper);
	}

	public void rtfs$RTFRandomState$initialize(RegistryAccess registries) {
		RegistryLookup<Preset> presets = registries.lookupOrThrow(RTFRegistries.PRESET);
		RegistryLookup<Noise> noises = registries.lookupOrThrow(RTFRegistries.NOISE);
		RegistryLookup<DensityFunction> functions = registries.lookupOrThrow(Registries.DENSITY_FUNCTION);

		functions.get(RTFDensityFunctionTags.ADDITIONAL_NOISE_ROUTER_FUNCTIONS).ifPresent((set) -> {
			set.forEach((function) -> function.value().mapAll(this.densityFunctionWrapper));
		});

		if((Object) this.sampler instanceof TBClimateSampler tbClimateSampler && TBCompat.isEnabled()) {
			functions.get(TBCompat.uniquenessKey()).ifPresent((uniqueness) -> {
				tbClimateSampler.setUniqueness(uniqueness.value().mapAll(this.densityFunctionWrapper));
			});
		}

		presets.get(Preset.KEY).ifPresentOrElse((presetHolder) -> {
			this.preset = presetHolder.value();

			if(this.hasContext) {
				PerformanceConfig config = PerformanceConfig.read(PerformanceConfig.DEFAULT_FILE_PATH)
					.resultOrPartial(RTFCommon.LOGGER::error)
					.orElseGet(PerformanceConfig::makeDefault);
				this.generatorContext = GeneratorContext.makeCached(this.preset, noises, (int) this.seed, config.tileSize(), config.batchCount(), ThreadPools.availableProcessors() > 4);
			}
		}, () -> {
			if(this.hasContext) {
//				throw new IllegalStateException("Missing preset!");
			}
		});
	}

	@Nullable
	public Preset rtfs$RTFRandomState$preset() {
		return this.preset;
	}

	@Nullable
	public GeneratorContext rtfs$RTFRandomState$generatorContext() {
		return this.generatorContext;
	}

	@Nullable
	public DensityFunction rtfs$RTFRandomState$wrap(DensityFunction function) {
		return function.mapAll(this.densityFunctionWrapper);
	}

	public Noise rtfs$RTFRandomState$seed(Noise noise) {
		return Noises.shiftSeed(noise, (int) this.seed);
	}
}
