package rayman.rtfs.world.worldgen.biome;

import rayman.rtfs.world.worldgen.noise.module.Noise;
import rayman.rtfs.world.worldgen.noise.module.Noises;

public interface ClimateParameter {
	float min();

	float max();

	default float mid() {
		return (this.min() + this.max()) / 2.0F;
	}

	default Noise source() {
		return Noises.constant(this.mid());
	}
}
