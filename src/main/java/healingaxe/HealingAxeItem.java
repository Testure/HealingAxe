package healingaxe;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HealingAxeItem extends ItemAxe {
    private final int chance;
    private final int chanceGoal;
    private final int food;
    private final float saturation;
    private final EnumRarity rarity;

    public HealingAxeItem(float damage, float speed) {
        super(ToolMaterial.DIAMOND, damage, speed);
        Integer miningLevel = HealingAxe.axeMiningLevel.get();
        Integer chance = HealingAxe.chance.get();
        Integer food = HealingAxe.foodAmount.get();
        Float saturation = HealingAxe.saturationAmount.get();
        EnumRarity axeRarity = HealingAxe.axeRarity.getReal();
        setRegistryName(new ResourceLocation(HealingAxe.MODID, "healing_axe"));
        setTranslationKey("healing_axe");
        setHarvestLevel("axe", miningLevel != null ? miningLevel : 3);
        setCreativeTab(CreativeTabs.TOOLS);
        this.saturation = saturation != null ? saturation : 0.5F;
        this.chance = chance != null ? chance : 50;
        this.food = food != null ? food : 2;
        this.chanceGoal = this.chance - 3;
        this.rarity = axeRarity;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return rarity;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity holder, int itemSlot, boolean isSelected) {
        if (!world.isRemote) {
            if (holder instanceof EntityPlayer && isSelected && world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                EntityPlayer player = (EntityPlayer) holder;
                if (!player.isCreative() && !player.isSpectator()) {
                    FoodStats stats = player.getFoodStats();
                    if (stats.needFood() || stats.getSaturationLevel() < 20.0F) {
                        if (world.rand.nextInt(chance) > chanceGoal) {
                            stats.addStats(food, saturation);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {

    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
