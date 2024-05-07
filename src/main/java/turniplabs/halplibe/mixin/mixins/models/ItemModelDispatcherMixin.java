package turniplabs.halplibe.mixin.mixins.models;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.ItemHelper;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
@Mixin(value = ItemModelDispatcher.class, remap = false)
public abstract class ItemModelDispatcherMixin {
    @Shadow public abstract void addDispatch(ItemModel dispatchable);

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        try {
            Set<Map.Entry<Item, Function<Item, ItemModel>>> entries = ((Map<Item, Function<Item, ItemModel>>) ItemHelper.class.getField("queuedItemModels").get(null)).entrySet();
            for (Map.Entry<Item, Function<Item, ItemModel>> entry : entries){
                addDispatch(entry.getValue().apply(entry.getKey()));
            }
            ItemHelper.class.getField("itemDispatcherInitialized").set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}