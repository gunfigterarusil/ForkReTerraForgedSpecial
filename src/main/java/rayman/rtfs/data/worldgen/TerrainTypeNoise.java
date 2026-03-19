package rayman.rtfs.data.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import rayman.rtfs.data.worldgen.preset.Preset;
import rayman.rtfs.data.worldgen.preset.WorldSettings;
import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.noise.module.Noises;

public class TerrainTypeNoise {
	public static final ResourceKey<Noise> GROUND = TerrainNoise.createKey("ground");

	public static void bootstrap(Preset preset, BootstapContext<Noise> ctx) {
		WorldSettings worldSettings = preset.world();
		WorldSettings.Properties properties = worldSettings.properties;
		float seaLevel = properties.seaLevel;
		int terrainScaler = properties.terrainScaler();

		ctx.register(GROUND, Noises.constant(seaLevel / (float)terrainScaler));
	}
}
