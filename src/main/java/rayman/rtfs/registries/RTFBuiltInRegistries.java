package rayman.rtfs.registries;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import rayman.rtfs.platform.RegistryUtil;
import rayman.rtfs.world.worldgen.biome.modifier.BiomeModifier;
import rayman.rtfs.world.worldgen.feature.chance.ChanceModifier;
import rayman.rtfs.world.worldgen.feature.template.decorator.TemplateDecorator;
import rayman.rtfs.world.worldgen.feature.template.placement.TemplatePlacement;
import rayman.rtfs.world.worldgen.noise.domain.Domain;
import rayman.rtfs.world.worldgen.noise.function.CurveFunction;
import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.structure.rule.StructureRule;

public class RTFBuiltInRegistries {
	public static final Registry<Codec<? extends Noise>> NOISE_TYPE = RegistryUtil.createRegistry(RTFRegistries.NOISE_TYPE);
	public static final Registry<Codec<? extends Domain>> DOMAIN_TYPE = RegistryUtil.createRegistry(RTFRegistries.DOMAIN_TYPE);
	public static final Registry<Codec<? extends CurveFunction>> CURVE_FUNCTION_TYPE = RegistryUtil.createRegistry(RTFRegistries.CURVE_FUNCTION_TYPE);
	public static final Registry<Codec<? extends ChanceModifier>> CHANCE_MODIFIER_TYPE = RegistryUtil.createRegistry(RTFRegistries.CHANCE_MODIFIER_TYPE);
	public static final Registry<Codec<? extends TemplatePlacement<?>>> TEMPLATE_PLACEMENT_TYPE = RegistryUtil.createRegistry(RTFRegistries.TEMPLATE_PLACEMENT_TYPE);
	public static final Registry<Codec<? extends TemplateDecorator<?>>> TEMPLATE_DECORATOR_TYPE = RegistryUtil.createRegistry(RTFRegistries.TEMPLATE_DECORATOR_TYPE);
	public static final Registry<Codec<? extends BiomeModifier>> BIOME_MODIFIER_TYPE = RegistryUtil.createRegistry(RTFRegistries.BIOME_MODIFIER_TYPE);
	public static final Registry<Codec<? extends StructureRule>> STRUCTURE_RULE_TYPE = RegistryUtil.createRegistry(RTFRegistries.STRUCTURE_RULE_TYPE);

	public static void bootstrap() {
	}
}
