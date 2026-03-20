package rayman.rtfs.world.worldgen.feature.chance;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import rayman.rtfs.world.worldgen.GeneratorContext;
import rayman.rtfs.world.worldgen.RTFRandomState;
import rayman.rtfs.world.worldgen.densityfunction.tile.Tile;

class BiomeEdgeChanceModifier extends RangeChanceModifier {
	public static final Codec<BiomeEdgeChanceModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("from").forGetter((o) -> o.from),
		Codec.FLOAT.fieldOf("to").forGetter((o) -> o.to),
		Codec.BOOL.fieldOf("exclusive").forGetter((o) -> o.exclusive)
	).apply(instance, BiomeEdgeChanceModifier::new));

	public BiomeEdgeChanceModifier(float from, float to, boolean exclusive) {
		super(from, to, exclusive);
	}

	@Override
	public Codec<BiomeEdgeChanceModifier> codec() {
		return CODEC;
	}

	@Override
	protected float getValue(ChanceContext chanceCtx, FeaturePlaceContext<?> placeCtx) {
		BlockPos pos = placeCtx.origin();
		@Nullable
		GeneratorContext generatorContext;
		if((Object) placeCtx.level().getLevel().getChunkSource().randomState() instanceof RTFRandomState rtfRandomState
				&& (generatorContext = rtfRandomState.generatorContext()) != null
				&& generatorContext.cache != null) {
			int x = pos.getX();
			int z = pos.getZ();
			int chunkX = SectionPos.blockToSectionCoord(x);
			int chunkZ = SectionPos.blockToSectionCoord(z);
			Tile providerTile = generatorContext.cache.provideAtChunk(chunkX, chunkZ);
			if(providerTile == null) return 0.5f;
			Tile.Chunk chunk = providerTile.getChunkReader(chunkX, chunkZ);
			return chunk.getCell(x, z).biomeRegionEdge;
		}
		// Non-RTF world: return neutral mid-range value
		return 0.5f;
	}
}
