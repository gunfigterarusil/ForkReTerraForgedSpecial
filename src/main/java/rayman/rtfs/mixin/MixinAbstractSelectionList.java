package rayman.rtfs.mixin;

import java.util.Collection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.components.AbstractSelectionList;

@Mixin(AbstractSelectionList.class)
public interface MixinAbstractSelectionList<E extends AbstractSelectionList.Entry<E>> {
	@Invoker("replaceEntries")
	void invokeReplaceEntries(Collection<E> entries);
}
