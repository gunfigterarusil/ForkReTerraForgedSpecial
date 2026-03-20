package rayman.rtfs.world.worldgen.noise.function;

import com.mojang.serialization.Codec;

import rayman.rtfs.platform.RegistryUtil;
import rayman.rtfs.registries.RTFBuiltInRegistries;

public class CurveFunctions {

	public static void bootstrap() {
		register("interpolation", Interpolation.CODEC);
		register("scurve", SCurveFunction.CODEC);
	}

	public static CurveFunction scurve(float lower, float upper) {
		return new SCurveFunction(lower, upper);
	}

	private static void register(String name, Codec<? extends CurveFunction> value) {
		RegistryUtil.register(RTFBuiltInRegistries.CURVE_FUNCTION_TYPE, name, value);
	}
}
