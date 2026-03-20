package rayman.rtfs.world.worldgen.cell.heightmap;

import rayman.rtfs.world.worldgen.noise.module.Noise;

public record RegionConfig(int seed, int scale, Noise warpX, Noise warpZ, float warpStrength) {
}
