package rayman.rtfs.world.worldgen.biome.modifier.impl;

import com.mojang.serialization.Codec;

import rayman.rtfs.world.worldgen.biome.modifier.BiomeModifier;

public interface ForgeBiomeModifier extends BiomeModifier, net.minecraftforge.common.world.BiomeModifier {
	Codec<? extends ForgeBiomeModifier> codec();
}
