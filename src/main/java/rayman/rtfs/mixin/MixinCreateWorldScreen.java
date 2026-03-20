package rayman.rtfs.mixin;

import java.nio.file.Path;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.WorldDataConfiguration;

@Mixin(CreateWorldScreen.class)
public interface MixinCreateWorldScreen {
    @Invoker("getDataPackSelectionSettings")
    Pair<Path, PackRepository> invokeGetDataPackSelectionSettings(WorldDataConfiguration config);

    @Invoker("tryApplyNewDataPacks")
    void invokeTryApplyNewDataPacks(PackRepository repository, boolean confirm, Consumer<WorldDataConfiguration> consumer);

    @Accessor("uiState")
    WorldCreationUiState getUiState();
}
