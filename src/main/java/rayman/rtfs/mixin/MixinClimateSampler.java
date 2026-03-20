package rayman.rtfs.mixin;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Climate;
import rayman.rtfs.world.worldgen.biome.RTFClimateSampler;

@Mixin(Climate.Sampler.class)
@Implements(@Interface(iface = RTFClimateSampler.class, prefix = "rtfs$RTFClimateSampler$"))
class MixinClimateSampler {
	private BlockPos spawnSearchCenter = BlockPos.ZERO;

	public void rtfs$RTFClimateSampler$setSpawnSearchCenter(BlockPos spawnSearchCenter) {
		this.spawnSearchCenter = spawnSearchCenter;
	}

	public BlockPos rtfs$RTFClimateSampler$getSpawnSearchCenter() {
		return this.spawnSearchCenter;
	}
}
