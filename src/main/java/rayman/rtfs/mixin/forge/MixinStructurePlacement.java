package rayman.rtfs.mixin.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

@Mixin(StructurePlacement.class)
public interface MixinStructurePlacement {
	@Accessor
	int getSalt();
}
