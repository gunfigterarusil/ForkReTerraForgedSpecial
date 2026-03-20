package rayman.rtfs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.chunk.ChunkAccess;

@Mixin(SurfaceRules.Context.class)
public interface MixinSurfaceRulesContext {
    @Accessor("system")
    SurfaceSystem getSystem();

    @Accessor("randomState")
    RandomState getRandomState();

    @Accessor("lastUpdateXZ")
    long getLastUpdateXZ();

    @Accessor("blockX")
    int getBlockX();

    @Accessor("blockZ")
    int getBlockZ();

    @Accessor("chunk")
    ChunkAccess getChunk();
}
