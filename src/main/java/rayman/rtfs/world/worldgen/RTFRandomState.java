package rayman.rtfs.world.worldgen;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import rayman.rtfs.data.worldgen.preset.Preset;
import rayman.rtfs.world.worldgen.noise.module.Noise;

public interface RTFRandomState {
	void initialize(RegistryAccess registries);

	@Nullable
	Preset preset();

	@Nullable
	GeneratorContext generatorContext();

	DensityFunction wrap(DensityFunction function);

	Noise seed(Noise noise);
}
