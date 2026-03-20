package rayman.rtfs.data.worldgen;

import java.util.stream.Stream;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.OreVeinifier;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import rayman.rtfs.RTFCommon;
import rayman.rtfs.data.worldgen.preset.CaveSettings;
import rayman.rtfs.data.worldgen.preset.Preset;
import rayman.rtfs.data.worldgen.preset.WorldSettings;
import rayman.rtfs.registries.RTFRegistries;
import rayman.rtfs.world.worldgen.densityfunction.CellSampler;
import rayman.rtfs.world.worldgen.densityfunction.RTFDensityFunctions;
import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.terrablender.TBCompat;

public class RTFNoiseRouterData {
	public static final ResourceKey<DensityFunction> HEIGHT = createKey("height");
	public static final ResourceKey<DensityFunction> GRADIENT = createKey("gradient");
	public static final ResourceKey<DensityFunction> EROSION = createKey("erosion");
	public static final ResourceKey<DensityFunction> SEDIMENT = createKey("sediment");

	private static final float SCALER = 128.0F;
	private static final float UNIT = 1.0F / SCALER;

    public static void bootstrap(Preset preset, BootstrapContext<DensityFunction> ctx) {
        HolderGetter<Noise> noises = ctx.lookup(RTFRegistries.NOISE);
        HolderGetter<DensityFunction> densityFunctions = ctx.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParams = ctx.lookup(Registries.NOISE);

	WorldSettings worldSettings = preset.world();
	WorldSettings.Properties properties = worldSettings.properties;

	CaveSettings caveSettings = preset.caves();

        int worldHeight = properties.worldHeight;
        int worldDepth = properties.worldDepth;

        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getCONTINENTS(), RTFDensityFunctions.cell(CellSampler.Field.CONTINENT));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getEROSION(), RTFDensityFunctions.cell(CellSampler.Field.EROSION));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getRIDGES(), RTFDensityFunctions.cell(CellSampler.Field.WEIRDNESS));

        DensityFunction height = rayman.rtfs.mixin.MixinNoiseRouterData.invokeRegisterAndWrap(ctx, HEIGHT, RTFDensityFunctions.cell(CellSampler.Field.HEIGHT));
        DensityFunction offset = rayman.rtfs.mixin.MixinNoiseRouterData.invokeRegisterAndWrap(ctx, rayman.rtfs.mixin.MixinNoiseRouterData.getOFFSET(), DensityFunctions.add(DensityFunctions.constant(NoiseRouterData.GLOBAL_OFFSET), DensityFunctions.mul(DensityFunctions.add(DensityFunctions.mul(DensityFunctions.constant(-1.0D), RTFDensityFunctions.noise(noises.getOrThrow(TerrainTypeNoise.GROUND))), RTFDensityFunctions.clampToNearestUnit(RTFDensityFunctions.conditionalArrayCache(height), properties.terrainScaler())), DensityFunctions.constant(2.0D))));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getDEPTH(), DensityFunctions.add(DensityFunctions.yClampedGradient(-worldDepth, worldHeight, yGradientRange(-worldDepth), yGradientRange(worldHeight)), offset));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getBASE_3D_NOISE_OVERWORLD(), DensityFunctions.zero());
        ctx.register(NoiseRouterData.JAGGEDNESS, jaggednessPerformanceHack());

        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getNOODLE(), noodle(-worldDepth, worldHeight, 1.0F - caveSettings.noodleCaveProbability, densityFunctions, noiseParams));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getENTRANCES(), probabilityDensity(caveSettings.entranceCaveProbability, rayman.rtfs.mixin.MixinNoiseRouterData.invokeEntrances(densityFunctions, noiseParams)));
        ctx.register(rayman.rtfs.mixin.MixinNoiseRouterData.getSPAGHETTI_2D(), probabilityDensity(caveSettings.spaghettiCaveProbability, spaghetti2D(-worldDepth, worldHeight, densityFunctions, noiseParams)));

        ctx.register(GRADIENT, RTFDensityFunctions.cell(CellSampler.Field.GRADIENT));
        ctx.register(EROSION, RTFDensityFunctions.cell(CellSampler.Field.HEIGHT_EROSION));
        ctx.register(SEDIMENT, RTFDensityFunctions.cell(CellSampler.Field.SEDIMENT));
        ctx.register(TBCompat.uniquenessKey(), RTFDensityFunctions.cell(CellSampler.Field.BIOME_REGION));
    }

    protected static NoiseRouter overworld(Preset preset, HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams, HolderGetter<Noise> noises) {
	WorldSettings worldSettings = preset.world();
	WorldSettings.Properties properties = worldSettings.properties;
	int worldDepth = properties.worldDepth;

	CaveSettings caves = preset.caves();
	float cheeseCaveDepthOffset = caves.cheeseCaveDepthOffset;

	DensityFunction aquiferBarrier = DensityFunctions.noise(noiseParams.getOrThrow(Noises.AQUIFER_BARRIER), 0.5);
        DensityFunction aquiferFluidLevelFloodedness = DensityFunctions.noise(noiseParams.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS), 0.67);
        DensityFunction aquiferFluidLevelSpread = DensityFunctions.noise(noiseParams.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD), 0.7142857142857143);
        DensityFunction aquiferLava = DensityFunctions.noise(noiseParams.getOrThrow(Noises.AQUIFER_LAVA));
        DensityFunction temperature = RTFDensityFunctions.cell(CellSampler.Field.TEMPERATURE);
        DensityFunction vegetation = RTFDensityFunctions.cell(CellSampler.Field.MOISTURE);
        DensityFunction factor = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getFACTOR());
        DensityFunction depth = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getDEPTH());
        DensityFunction initialDensity = rayman.rtfs.mixin.MixinNoiseRouterData.invokeNoiseGradientDensity(DensityFunctions.cache2d(factor), depth);
        DensityFunction slopedCheese = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getSLOPED_CHEESE());
        DensityFunction entrances = caves.entranceCaveProbability > 0.0F ? DensityFunctions.min(slopedCheese, DensityFunctions.mul(DensityFunctions.constant(5.0D), DensityFunctions.interpolated(rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getENTRANCES())))) : slopedCheese;

        DensityFunction slopedCheeseRange = DensityFunctions.mul(DensityFunctions.rangeChoice(slopedCheese, -1000000.0D, cheeseCaveDepthOffset, entrances, DensityFunctions.interpolated(slideOverworld(underground(caves.cheeseCaveProbability, densityFunctions, noiseParams, slopedCheese), -worldDepth))), DensityFunctions.constant(0.64)).squeeze();
        DensityFunction finalDensity = DensityFunctions.min(slopedCheeseRange, rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getNOODLE()));

        DensityFunction y = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getY());
        int minY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> ((rayman.rtfs.mixin.MixinOreVeinifierVeinType) (Object) veinType).getMinY()).min().orElse(-DimensionType.MIN_Y * 2);
        int maxY = Stream.of(OreVeinifier.VeinType.values()).mapToInt(veinType -> ((rayman.rtfs.mixin.MixinOreVeinifierVeinType) (Object) veinType).getMaxY()).max().orElse(-DimensionType.MIN_Y * 2);
        DensityFunction oreVeininess = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.ORE_VEININESS), 1.5, 1.5), minY, maxY, 0);
        DensityFunction oreVeinA = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.ORE_VEIN_A), 4.0, 4.0), minY, maxY, 0).abs();
        DensityFunction oreVeinB = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.ORE_VEIN_B), 4.0, 4.0), minY, maxY, 0).abs();
        DensityFunction oreVein = DensityFunctions.add(DensityFunctions.constant(-0.08F), DensityFunctions.max(oreVeinA, oreVeinB));
        DensityFunction oreGap = DensityFunctions.noise(noiseParams.getOrThrow(Noises.ORE_GAP));
        return new NoiseRouter(aquiferBarrier, aquiferFluidLevelFloodedness, aquiferFluidLevelSpread, aquiferLava, temperature, vegetation, rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getCONTINENTS()), rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getEROSION()), depth, rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getRIDGES()), slideOverworld(DensityFunctions.add(initialDensity, DensityFunctions.constant(UNIT * -90)).clamp(-64.0, 64.0), -worldDepth), finalDensity, oreVeininess, oreVein, oreGap);
	}

    private static DensityFunction underground(float cheeseCaveProbability, HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams, DensityFunction slopedCheese) {
        DensityFunction spaghetti2d = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getSPAGHETTI_2D());
        DensityFunction spaghettiRoughnessFunction = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getSPAGHETTI_ROUGHNESS_FUNCTION());
        DensityFunction caveLayerNoise = DensityFunctions.noise(noiseParams.getOrThrow(Noises.CAVE_LAYER), 8.0);
        DensityFunction caveLayer = DensityFunctions.mul(DensityFunctions.constant(4.0), caveLayerNoise.square());
        DensityFunction caveCheese = probabilityDensity(cheeseCaveProbability, DensityFunctions.noise(noiseParams.getOrThrow(Noises.CAVE_CHEESE), 0.6666666666666666));
        DensityFunction slopedCaves = DensityFunctions.add(DensityFunctions.add(DensityFunctions.constant(0.27), caveCheese).clamp(-1.0, 1.0), DensityFunctions.add(DensityFunctions.constant(1.5), DensityFunctions.mul(DensityFunctions.constant(-0.64), slopedCheese)).clamp(0.0, 0.5));
        DensityFunction slopedCaveLayered = DensityFunctions.add(caveLayer, slopedCaves);
        DensityFunction underground = DensityFunctions.min(DensityFunctions.min(slopedCaveLayered, rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getENTRANCES())), DensityFunctions.add(spaghetti2d, spaghettiRoughnessFunction));
        DensityFunction pillars = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getPILLARS());
        DensityFunction pillarRange = DensityFunctions.rangeChoice(pillars, -1000000.0, 0.03, DensityFunctions.constant(-1000000.0), pillars);
        return DensityFunctions.max(underground, pillarRange);
    }

    private static DensityFunction spaghetti2D(int minY, int maxY, HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams) {
        DensityFunction modulator = DensityFunctions.noise(noiseParams.getOrThrow(Noises.SPAGHETTI_2D_MODULATOR), 2.0, 1.0);
        DensityFunction sampler = DensityFunctions.weirdScaledSampler(modulator, noiseParams.getOrThrow(Noises.SPAGHETTI_2D), DensityFunctions.WeirdScaledSampler.RarityValueMapper.TYPE2);
        DensityFunction elevation = DensityFunctions.mappedNoise(noiseParams.getOrThrow(Noises.SPAGHETTI_2D_ELEVATION), 0.0, Math.floorDiv(minY, 8), 8.0);
        DensityFunction thicknessModulator = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getSPAGHETTI_2D()_THICKNESS_MODULATOR);
        DensityFunction elevationGradient = DensityFunctions.add(elevation, DensityFunctions.yClampedGradient(minY, maxY, minY / -8.0D, maxY / -8.0D)).abs();
        DensityFunction normal = DensityFunctions.add(elevationGradient, thicknessModulator).cube();
        DensityFunction weird = DensityFunctions.add(sampler, DensityFunctions.mul(DensityFunctions.constant(0.083D), thicknessModulator));
        return DensityFunctions.max(weird, normal).clamp(-1.0D, 1.0D);
    }

    private static DensityFunction noodle(int minY, int maxY, float threshold, HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams) {
	int baseY = minY + 4;

	DensityFunction y = rayman.rtfs.mixin.MixinNoiseRouterData.invokeGetFunction(densityFunctions, rayman.rtfs.mixin.MixinNoiseRouterData.getY());
        DensityFunction selector = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.NOODLE), 1.0, 1.0), baseY, maxY, -1);
        DensityFunction thickness = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.mappedNoise(noiseParams.getOrThrow(Noises.NOODLE_THICKNESS), 1.0, 1.0, -0.05, -0.1), baseY, maxY, 0);
        DensityFunction ridgeA = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.NOODLE_RIDGE_A), 2.6666666666666665, 2.6666666666666665), baseY, maxY, 0);
        DensityFunction ridgeB = rayman.rtfs.mixin.MixinNoiseRouterData.invokeYLimitedInterpolatable(y, DensityFunctions.noise(noiseParams.getOrThrow(Noises.NOODLE_RIDGE_B), 2.6666666666666665, 2.6666666666666665), baseY, maxY, 0);
        DensityFunction ridge = DensityFunctions.mul(DensityFunctions.constant(1.5), DensityFunctions.max(ridgeA.abs(), ridgeB.abs()));
        return DensityFunctions.rangeChoice(selector, -1000000.0, threshold, DensityFunctions.constant(64.0), DensityFunctions.add(thickness, ridge));
    }

    private static DensityFunction slideOverworld(DensityFunction function, int minY) {
        return slide(function, minY, 0, 24, UNIT * 15);
    }

    private static DensityFunction slide(DensityFunction function, int minY, int bottomGradientStart, int bottomGradientEnd, double bottomGradientTarget) {
        DensityFunction bottomGradient = DensityFunctions.yClampedGradient(minY + bottomGradientStart, minY + bottomGradientEnd, 0.0, 1.0);
        return DensityFunctions.lerp(bottomGradient, bottomGradientTarget, function);
    }

    /*
     * the multiply function doesnt sample the second input
     * if the first input is zero, however this optimization doesn't get
     * applied if either input is a Constant, so if we use
     * use DensityFunctions.zero() Noises.JAGGED will still get sampled
     */
    private static DensityFunction jaggednessPerformanceHack() {
	return DensityFunctions.add(DensityFunctions.zero(), DensityFunctions.zero());
    }

    // do this a different way, since this affects the size of the cave as well
    @Deprecated
    private static DensityFunction probabilityDensity(float probability, DensityFunction function) {
	if(probability == 0.0F) {
		return DensityFunctions.constant(1.0F);
	}
	return DensityFunctions.add(DensityFunctions.constant(1.0F - probability), function);
    }

    private static float yGradientRange(float range) {
	return 1.0F + (-range / SCALER);
    }

    private static ResourceKey<DensityFunction> createKey(String string) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, RTFCommon.location(string));
    }
}