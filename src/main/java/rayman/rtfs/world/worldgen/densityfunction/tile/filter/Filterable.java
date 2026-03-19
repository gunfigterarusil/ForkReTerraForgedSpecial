package rayman.rtfs.world.worldgen.densityfunction.tile.filter;

import rayman.rtfs.world.worldgen.cell.Cell;
import rayman.rtfs.world.worldgen.densityfunction.tile.Size;

public interface Filterable {
    int getBlockX();

    int getBlockZ();

    Size getBlockSize();

    Cell[] getBacking();

    Cell getCellRaw(int x, int z);
}
