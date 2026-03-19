package rayman.rtfs.world.worldgen.biome.modifier;

import java.util.Map;
import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import rayman.rtfs.platform.RegistryUtil;
import rayman.rtfs.registries.RTFBuiltInRegistries;
import rayman.rtfs.world.worldgen.biome.modifier.impl.AddModifier;
import rayman.rtfs.world.worldgen.biome.modifier.impl.ReplaceModifier;

public class BiomeModifiers {

	public static void bootstrap() {
		register("add", AddModifier.CODEC);
		register("replace", ReplaceModifier.CODEC);
	}

	@SafeVarargs
	public static BiomeModifier add(Order order, GenerationStep.Decoration step, Holder<PlacedFeature>... features) {
		return add(order, step, HolderSet.direct(features));
	}

	public static BiomeModifier add(Order order, GenerationStep.Decoration step, HolderSet<PlacedFeature> features) {
		return add(order, step, Optional.empty(), features);
	}

	public static BiomeModifier add(Order order, GenerationStep.Decoration step, HolderSet<Biome> biomes, HolderSet<PlacedFeature> features) {
		return add(order, step, Optional.of(Pair.of(Filter.Behavior.INCLUDE, biomes)), features);
	}

	@SafeVarargs
	public static BiomeModifier add(Order order, GenerationStep.Decoration step, HolderSet<Biome> biomes, Holder<PlacedFeature>... features) {
		return add(order, step, biomes, HolderSet.direct(features));
	}

	@SafeVarargs
	public static BiomeModifier add(Order order, GenerationStep.Decoration step, Filter.Behavior filterBehavior, HolderSet<Biome> biomes, Holder<PlacedFeature>... features) {
		return add(order, step, filterBehavior, biomes, HolderSet.direct(features));
	}

	public static BiomeModifier add(Order order, GenerationStep.Decoration step, Filter.Behavior filterBehavior, HolderSet<Biome> biomes, HolderSet<PlacedFeature> features) {
		return add(order, step, Optional.of(Pair.of(filterBehavior, biomes)), features);
	}

	public static BiomeModifier add(Order order, GenerationStep.Decoration step, Optional<Pair<Filter.Behavior, HolderSet<Biome>>> biomes, HolderSet<PlacedFeature> features) {
		return new AddModifier(order, step, biomes.map((p) -> new Filter(p.getSecond(), p.getFirst())), features);
	}

	public static BiomeModifier replace(GenerationStep.Decoration step, Map<ResourceKey<PlacedFeature>, Holder<PlacedFeature>> replacements) {
		return replace(step, Optional.empty(), replacements);
	}

	public static BiomeModifier replace(GenerationStep.Decoration step, HolderSet<Biome> biomes, Map<ResourceKey<PlacedFeature>, Holder<PlacedFeature>> replacements) {
		return replace(step, Optional.of(biomes), replacements);
	}

	public static BiomeModifier replace(GenerationStep.Decoration step, Optional<HolderSet<Biome>> biomes, Map<ResourceKey<PlacedFeature>, Holder<PlacedFeature>> replacements) {
		return new ReplaceModifier(step, biomes, replacements);
	}

	public static void register(String name, Codec<? extends BiomeModifier> value) {
		RegistryUtil.register(RTFBuiltInRegistries.BIOME_MODIFIER_TYPE, name, value);
	}
}
