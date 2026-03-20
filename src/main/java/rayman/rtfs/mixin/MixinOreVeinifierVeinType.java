package rayman.rtfs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.levelgen.OreVeinifier;

@Mixin(OreVeinifier.VeinType.class)
public interface MixinOreVeinifierVeinType {
    @Accessor("minY")
    int getMinY();

    @Accessor("maxY")
    int getMaxY();
}
