package rayman.rtfs.mixin.terrablender;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.biome.Climate;
import rayman.rtfs.world.worldgen.terrablender.TBTargetPoint;

@Mixin(Climate.TargetPoint.class)
@Implements(@Interface(iface = TBTargetPoint.class, prefix = "rtfs$TBTargetPoint$"))
class MixinTargetPoint {
	private double uniqueness = Double.NaN;

	public void rtfs$TBTargetPoint$setUniqueness(double uniqueness) {
		this.uniqueness = uniqueness;
	}

	@Nullable
	public double rtfs$TBTargetPoint$getUniqueness() {
		return this.uniqueness;
	}
}
