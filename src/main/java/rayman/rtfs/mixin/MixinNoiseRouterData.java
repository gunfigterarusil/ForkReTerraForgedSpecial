package rayman.rtfs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

@Mixin(NoiseRouterData.class)
public interface MixinNoiseRouterData {
    @Invoker("registerAndWrap")
    static DensityFunction invokeRegisterAndWrap(BootstrapContext<DensityFunction> ctx, ResourceKey<DensityFunction> key, DensityFunction function) {
        throw new UnsupportedOperationException();
    }

    @Invoker("getFunction")
    static DensityFunction invokeGetFunction(HolderGetter<DensityFunction> densityFunctions, ResourceKey<DensityFunction> key) {
        throw new UnsupportedOperationException();
    }

    @Invoker("noiseGradientDensity")
    static DensityFunction invokeNoiseGradientDensity(DensityFunction factor, DensityFunction depth) {
        throw new UnsupportedOperationException();
    }

    @Invoker("yLimitedInterpolatable")
    static DensityFunction invokeYLimitedInterpolatable(DensityFunction y, DensityFunction function, int minY, int maxY, int offset) {
        throw new UnsupportedOperationException();
    }

    @Invoker("entrances")
    static DensityFunction invokeEntrances(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams) {
        throw new UnsupportedOperationException();
    }

    @Invoker("postProcess")
    static DensityFunction invokePostProcess(DensityFunction function) {
        throw new UnsupportedOperationException();
    }

    @Invoker("underground")
    static DensityFunction invokeUnderground(HolderGetter<DensityFunction> densityFunctions, HolderGetter<NormalNoise.NoiseParameters> noiseParams, DensityFunction function) {
        throw new UnsupportedOperationException();
    }

    @Accessor("OFFSET")
    static ResourceKey<DensityFunction> getOFFSET() { throw new UnsupportedOperationException(); }
    @Accessor("FACTOR")
    static ResourceKey<DensityFunction> getFACTOR() { throw new UnsupportedOperationException(); }
    @Accessor("DEPTH")
    static ResourceKey<DensityFunction> getDEPTH() { throw new UnsupportedOperationException(); }
    @Accessor("SLOPED_CHEESE")
    static ResourceKey<DensityFunction> getSLOPED_CHEESE() { throw new UnsupportedOperationException(); }
    @Accessor("CONTINENTS")
    static ResourceKey<DensityFunction> getCONTINENTS() { throw new UnsupportedOperationException(); }
    @Accessor("EROSION")
    static ResourceKey<DensityFunction> getEROSION() { throw new UnsupportedOperationException(); }
    @Accessor("RIDGES")
    static ResourceKey<DensityFunction> getRIDGES() { throw new UnsupportedOperationException(); }
    @Accessor("ENTRANCES")
    static ResourceKey<DensityFunction> getENTRANCES() { throw new UnsupportedOperationException(); }
    @Accessor("NOODLE")
    static ResourceKey<DensityFunction> getNOODLE() { throw new UnsupportedOperationException(); }
    @Accessor("SPAGHETTI_2D")
    static ResourceKey<DensityFunction> getSPAGHETTI_2D() { throw new UnsupportedOperationException(); }
    @Accessor("SPAGHETTI_ROUGHNESS_FUNCTION")
    static ResourceKey<DensityFunction> getSPAGHETTI_ROUGHNESS_FUNCTION() { throw new UnsupportedOperationException(); }
    @Accessor("PILLARS")
    static ResourceKey<DensityFunction> getPILLARS() { throw new UnsupportedOperationException(); }
    @Accessor("SPAGHETTI_2D_THICKNESS_MODULATOR")
    static ResourceKey<DensityFunction> getSPAGHETTI_2D_THICKNESS_MODULATOR() { throw new UnsupportedOperationException(); }
    @Accessor("Y")
    static ResourceKey<DensityFunction> getY() { throw new UnsupportedOperationException(); }
    @Accessor("BASE_3D_NOISE_OVERWORLD")
    static ResourceKey<DensityFunction> getBASE_3D_NOISE_OVERWORLD() { throw new UnsupportedOperationException(); }

    @Accessor("GLOBAL_OFFSET")
    static float getGLOBAL_OFFSET() { throw new UnsupportedOperationException(); }
}
