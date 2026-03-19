package rayman.rtfs.world.worldgen.structure.rule;

import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.RandomState;
import rayman.rtfs.world.worldgen.GeneratorContext;
import rayman.rtfs.world.worldgen.RTFRandomState;
import rayman.rtfs.world.worldgen.cell.Cell;
import rayman.rtfs.world.worldgen.cell.heightmap.WorldLookup;
import rayman.rtfs.world.worldgen.cell.terrain.Terrain;
import rayman.rtfs.world.worldgen.cell.terrain.TerrainType;

record CellTest(float cutoff, Set<Terrain> terrainTypeBlacklist) implements StructureRule {
	public static final Codec<CellTest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.FLOAT.fieldOf("cutoff").forGetter(CellTest::cutoff),
		Codec.STRING.xmap(TerrainType::get, Terrain::getName).listOf().fieldOf("terrain_type_blacklist").forGetter((set) -> set.terrainTypeBlacklist().stream().toList())
	).apply(instance, CellTest::new));

	public CellTest(float cutoff, List<Terrain> terrainTypeBlacklist) {
		this(cutoff, ImmutableSet.copyOf(terrainTypeBlacklist));
	}

	@Override
	public boolean test(RandomState randomState, BlockPos pos) {
		if((Object) randomState instanceof RTFRandomState rtfRandomState) {
			@Nullable
			GeneratorContext generatorContext = rtfRandomState.generatorContext();
			if(generatorContext != null) {
				WorldLookup worldLookup = generatorContext.lookup;
				Cell cell = new Cell();
				worldLookup.applyCell(cell.reset(), pos.getX(), pos.getZ(), false);
				if(cell.riverMask < this.cutoff || this.terrainTypeBlacklist.contains(cell.terrain)) {
					return false;
				}
			}
			return true;
		}
		// Non-RTF world: allow the structure by default
		return true;
	}

	@Override
	public Codec<CellTest> codec() {
		return CODEC;
	}
}
