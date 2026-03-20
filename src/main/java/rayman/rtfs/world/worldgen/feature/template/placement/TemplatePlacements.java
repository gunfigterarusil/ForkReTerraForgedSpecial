package rayman.rtfs.world.worldgen.feature.template.placement;

import com.mojang.serialization.Codec;

import rayman.rtfs.platform.RegistryUtil;
import rayman.rtfs.registries.RTFBuiltInRegistries;

public class TemplatePlacements {

	public static void bootstrap() {
		register("any", AnyPlacement.CODEC);
		register("tree", TreePlacement.CODEC);
	}

	public static AnyPlacement any() {
		return new AnyPlacement();
	}

	public static TreePlacement tree() {
		return new TreePlacement();
	}

	private static void register(String name, Codec<? extends TemplatePlacement<?>> placement) {
		RegistryUtil.register(RTFBuiltInRegistries.TEMPLATE_PLACEMENT_TYPE, name, placement);
	}
}
