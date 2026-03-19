package rayman.rtfs.world.worldgen.densityfunction.tile.chunk;

import rayman.rtfs.concurrent.Disposable;
import rayman.rtfs.world.worldgen.cell.Cell;

public interface ChunkReader extends ChunkHolder, Disposable {
    Cell getCell(int x, int z);
}
