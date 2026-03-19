package rayman.rtfs.world.worldgen.cell.rivermap;

import java.util.concurrent.TimeUnit;

import rayman.rtfs.concurrent.cache.Cache;
import rayman.rtfs.concurrent.cache.map.StampedLongMap;
import rayman.rtfs.world.worldgen.util.PosUtil;

public class RiverCache {
    protected RiverGenerator generator;
    protected Cache<Rivermap> cache;

    public RiverCache(RiverGenerator generator) {
        this.cache = new Cache<>(32, 5L, 1L, TimeUnit.MINUTES, StampedLongMap::new);
        this.generator = generator;
    }

    public Rivermap getRivers(int x, int z) {
        return this.cache.computeIfAbsent(PosUtil.pack(x, z), id -> {
		return this.generator.generateRivers(PosUtil.unpackLeft(id), PosUtil.unpackRight(id), id);
        });
    }
}
