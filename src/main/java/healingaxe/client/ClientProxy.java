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
    private int color0 = -1;
    private int color1 = -1;

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
            if (i == 0)
                return getColor0();
            if (i == 1)
                return getColor1();
            return -1;
        }, HealingAxe.ITEM);
    }

    private int getColor0() {
        if (color0 == -1) {
            String col = HealingAxe.axeHandleColor.get();
            if (col != null && !col.isEmpty()) {
                try {
                    color0 = Integer.parseUnsignedInt(col.substring(2), 16);
                } catch (Exception e) {
                    HealingAxe.LOGGER.error("Error getting axe layer0 color!", e);
                    color0 = 0;
                }
            }
        }
        return color0;
    }

    private int getColor1() {
        if (color1 == -1) {
            String col = HealingAxe.axeColor.get();
            if (col != null && !col.isEmpty()) {
                try {
                    color1 = Integer.parseUnsignedInt(col.substring(2), 16);
                } catch (Exception e) {
                    HealingAxe.LOGGER.error("Error getting axe layer1 color!", e);
                    color1 = 0;
                }
            }
        }
        return color1;
    }

    @SubscribeEvent
    public void modelLoad(ModelRegistryEvent event) {
        ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(HealingAxe.axeModel.get()));
        ModelLoader.setCustomMeshDefinition(HealingAxe.ITEM, stack -> loc);
        ModelLoader.setCustomModelResourceLocation(HealingAxe.ITEM, 0, loc);
        ModelBakery.registerItemVariants(HealingAxe.ITEM, loc);
    }
}
