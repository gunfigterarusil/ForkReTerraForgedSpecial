package rayman.rtfs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.components.AbstractWidget;

@Mixin(AbstractWidget.class)
public interface MixinAbstractWidget {
    @Accessor("height")
    int getHeight();

    @Accessor("height")
    void setHeight(int height);
}
