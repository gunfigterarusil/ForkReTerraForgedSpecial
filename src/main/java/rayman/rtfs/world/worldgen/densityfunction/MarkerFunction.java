package rayman.rtfs.world.worldgen.densityfunction;

import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public interface MarkerFunction extends DensityFunction.SimpleFunction {

	// This method is intentionally unreachable at runtime.
	// MarkerFunction instances are replaced by their mapped equivalents
	// during NoiseRouter.mapAll() in MixinRandomState before any compute() call.
	@Override
	default double compute(FunctionContext ctx) {
		throw new UnsupportedOperationException("MarkerFunction.compute() should never be called - it must be replaced via mapAll() first");
	}

    DensityFunction mapAll(Visitor visitor);

	@Override
	default double minValue() {
		return Float.NEGATIVE_INFINITY;
	}

	@Override
	default double maxValue() {
		return Float.POSITIVE_INFINITY;
	}

	public interface Mapped extends DensityFunction.SimpleFunction {

		// codec() is not needed - Mapped instances are ephemeral and never serialized.
		@Override
		default KeyDispatchDataCodec<NoiseFunction> codec() {
			throw new UnsupportedOperationException("MarkerFunction.Mapped is not serializable");
		}
	}
}
