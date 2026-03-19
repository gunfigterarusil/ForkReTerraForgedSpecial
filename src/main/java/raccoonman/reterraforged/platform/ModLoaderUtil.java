package raccoonman.reterraforged.platform;

import net.minecraftforge.fml.loading.FMLLoader;

public class ModLoaderUtil {

	public static boolean isLoaded(String modId) {
		return FMLLoader.getLoadingModList().getModFileById(modId) != null;
	}
}
