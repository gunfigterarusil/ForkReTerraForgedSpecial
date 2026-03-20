package rayman.rtfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import rayman.rtfs.data.worldgen.preset.Preset;
import rayman.rtfs.platform.RegistryUtil;
import rayman.rtfs.registries.RTFBuiltInRegistries;
import rayman.rtfs.registries.RTFRegistries;
import rayman.rtfs.world.worldgen.biome.modifier.BiomeModifiers;
import rayman.rtfs.world.worldgen.densityfunction.RTFDensityFunctions;
import rayman.rtfs.world.worldgen.feature.RTFFeatures;
import rayman.rtfs.world.worldgen.feature.chance.RTFChanceModifiers;
import rayman.rtfs.world.worldgen.feature.placement.RTFPlacementModifiers;
import rayman.rtfs.world.worldgen.feature.template.decorator.TemplateDecorators;
import rayman.rtfs.world.worldgen.feature.template.placement.TemplatePlacements;
import rayman.rtfs.world.worldgen.floatproviders.RTFFloatProviderTypes;
import rayman.rtfs.world.worldgen.heightproviders.RTFHeightProviderTypes;
import rayman.rtfs.world.worldgen.noise.domain.Domains;
import rayman.rtfs.world.worldgen.noise.function.CurveFunctions;
import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.noise.module.Noises;
import rayman.rtfs.world.worldgen.structure.rule.StructureRule;
import rayman.rtfs.world.worldgen.structure.rule.StructureRules;
import rayman.rtfs.world.worldgen.surface.rule.RTFSurfaceRules;

public class RTFCommon {
	public static final String MOD_ID = "rtfs";
	public static final String LEGACY_MOD_ID = "terraforged";
	public static final Logger LOGGER = LogManager.getLogger("rtfs");

	public static void bootstrap() {
		RTFBuiltInRegistries.bootstrap();
		TemplatePlacements.bootstrap();
		TemplateDecorators.bootstrap();
		RTFChanceModifiers.bootstrap();
		RTFPlacementModifiers.bootstrap();
		RTFDensityFunctions.bootstrap();
		Noises.bootstrap();
		Domains.bootstrap();
		CurveFunctions.bootstrap();
		RTFFeatures.bootstrap();
		RTFHeightProviderTypes.bootstrap();
		RTFFloatProviderTypes.bootstrap();
		BiomeModifiers.bootstrap();
		RTFSurfaceRules.bootstrap();
		StructureRules.bootstrap();

		RegistryUtil.createDataRegistry(RTFRegistries.NOISE, Noise.DIRECT_CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.PRESET, Preset.DIRECT_CODEC);
		RegistryUtil.createDataRegistry(RTFRegistries.STRUCTURE_RULE, StructureRule.DIRECT_CODEC);
	}

	public static ResourceLocation location(String name) {
		if (name.contains(":")) return new ResourceLocation(name);
		return new ResourceLocation(RTFCommon.MOD_ID, name);
	}
}
