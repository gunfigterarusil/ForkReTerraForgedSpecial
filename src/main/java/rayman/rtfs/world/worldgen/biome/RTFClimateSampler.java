package rayman.rtfs.world.worldgen.biome;

import net.minecraft.core.BlockPos;

public interface RTFClimateSampler {
	void setSpawnSearchCenter(BlockPos center);

	BlockPos getSpawnSearchCenter();
}
