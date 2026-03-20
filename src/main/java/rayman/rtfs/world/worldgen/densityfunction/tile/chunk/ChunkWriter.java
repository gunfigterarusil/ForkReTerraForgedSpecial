package rayman.rtfs.world.worldgen.densityfunction.tile.chunk;

import rayman.rtfs.world.worldgen.cell.Cell;

public interface ChunkWriter extends ChunkHolder {
    Cell genCell(int x, int z);
}
