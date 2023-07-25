package healingaxe;

import configurator.Configurator;
import configurator.api.*;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = HealingAxe.MODID, dependencies = "required:configurator@[1.2.5,);")
public class HealingAxe {
    public static final String MODID = "healingaxe";
    public static HealingAxeItem ITEM;

    @SidedProxy(serverSide = "healingaxe.CommonProxy", clientSide = "healingaxe.client.ClientProxy")
    public static CommonProxy proxy;

    public static final StringConfigValue axeColor;
    public static final StringConfigValue axeHandleColor;
    public static final ResourceLocationConfigValue axeModel;
    public static final FloatConfigValue axeDamage;
    public static final FloatConfigValue axeSpeed;
    public static final IntegerConfigValue axeMiningLevel;
    public static final IntegerConfigValue chance;
    public static final IntegerConfigValue foodAmount;
    public static final FloatConfigValue saturationAmount;
    public static final EnumConfigValue<EnumRarity> axeRarity;
    private static final Config COMMON_CONFIG;

    static {
        Config.Builder builderClient = Config.Builder.builder().withName("HealingAxe-Client").ofType(Config.Type.CLIENT);
        Config.Builder builderCommon = Config.Builder.builder().withName("HealingAxe").ofType(Config.Type.COMMON);

        builderClient.push("model_settings");
        axeHandleColor = builderClient.define("layer0_tint", "0x896727");
        axeColor = builderClient.define("layer1_tint", "0xFFFFFF");
        axeModel = builderClient.define("model_location", new ResourceLocation("healingaxe", "healing_axe"));
        builderClient.pop();

        builderCommon.push("axe_stats");
        axeDamage = builderCommon.define("damage", 2.0F);
        axeSpeed = builderCommon.define("speed", -3.0F);
        axeMiningLevel = builderCommon.define("harvest_level", 3);
        foodAmount = builderCommon.define("food_given", 2);
        saturationAmount = builderCommon.define("saturation_per_food", 0.5F);
        chance = builderCommon.define("chance_to_feed", 50);
        axeRarity = builderCommon.defineEnum("item_rarity", EnumRarity.COMMON, EnumRarity::valueOf);
        builderCommon.pop();

        Configurator.registerConfig(builderClient.build());
        COMMON_CONFIG = Configurator.registerConfig(builderCommon.build());
    }

    public HealingAxe() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @SubscribeEvent
    public void itemRegister(RegistryEvent.Register<Item> event) {
        Configurator.loadConfig(COMMON_CONFIG);
        Float damage = axeDamage.get();
        Float speed = axeSpeed.get();
        if (damage == null || speed == null) {
            damage = 2.0F;
            speed = -3.0F;
        }
        ITEM = new HealingAxeItem(damage, speed);
        ForgeRegistries.ITEMS.register(ITEM);
    }
}
