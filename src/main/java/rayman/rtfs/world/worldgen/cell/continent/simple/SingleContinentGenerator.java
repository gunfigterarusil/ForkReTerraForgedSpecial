package rayman.rtfs.world.worldgen.cell.continent.simple;

import rayman.rtfs.world.worldgen.GeneratorContext;
import rayman.rtfs.world.worldgen.cell.Cell;
import rayman.rtfs.world.worldgen.noise.NoiseUtil.Vec2i;
import rayman.rtfs.world.worldgen.util.PosUtil;
import rayman.rtfs.world.worldgen.util.Seed;

public class SingleContinentGenerator extends ContinentGenerator {
    private Vec2i center;

    public SingleContinentGenerator(Seed seed, GeneratorContext context) {
        super(seed, context);
        long center = this.getNearestCenter(0.0F, 0.0F);
        int cx = PosUtil.unpackLeft(center);
        int cz = PosUtil.unpackRight(center);
        this.center = new Vec2i(cx, cz);
    }

    @Override
    public void apply(Cell cell, float x, float y) {
        super.apply(cell, x, y);
        if (cell.continentX != this.center.x() || cell.continentZ != this.center.y()) {
            cell.continentId = 0.0F;
            cell.continentEdge = 0.0F;
            cell.continentX = 0;
            cell.continentZ = 0;
        }
    }
}
