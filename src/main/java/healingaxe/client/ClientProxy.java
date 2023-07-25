package healingaxe.client;

import healingaxe.CommonProxy;
import healingaxe.HealingAxe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void init() {
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, i) -> {
            if (i == 0) {
                String col = HealingAxe.axeHandleColor.get();
                if (col != null && !col.isEmpty()) {
                    return Integer.parseUnsignedInt(col.substring(2), 16);
                }
            }
            if (i == 1) {
                String col = HealingAxe.axeColor.get();
                if (col != null && !col.isEmpty()) {
                    return Integer.parseUnsignedInt(col.substring(2), 16);
                }
            }
            return -1;
        }, HealingAxe.ITEM);
    }

    @SubscribeEvent
    public void modelLoad(ModelRegistryEvent event) {
        ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(HealingAxe.axeModel.get()));
        ModelLoader.setCustomMeshDefinition(HealingAxe.ITEM, stack -> loc);
        ModelLoader.setCustomModelResourceLocation(HealingAxe.ITEM, 0, loc);
        ModelBakery.registerItemVariants(HealingAxe.ITEM, loc);
    }
}
