package moze_intel.projecte.gameObjs.items.armor;

import java.util.function.Consumer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public abstract class PEArmor extends ArmorItem {

	protected PEArmor(ArmorMaterial material, EquipmentSlot armorPiece, Properties props) {
		super(material, armorPiece, props);
	}

	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return true;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 50;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		// Check if the enchantment category is valid for the armor piece's equipment slot
		if (enchantment.category == EnchantmentCategory.BREAKABLE || enchantment.category==EnchantmentCategory.VANISHABLE)
			return true;
		if (this.getSlot() == EquipmentSlot.HEAD) {
			return enchantment.category == EnchantmentCategory.ARMOR_HEAD || enchantment.category == EnchantmentCategory.ARMOR;
		} else if (this.getSlot() == EquipmentSlot.CHEST) {
			return enchantment.category == EnchantmentCategory.ARMOR_CHEST || enchantment.category == EnchantmentCategory.ARMOR;
		} else if (this.getSlot() == EquipmentSlot.LEGS) {
			return enchantment.category == EnchantmentCategory.ARMOR_LEGS || enchantment.category == EnchantmentCategory.ARMOR;
		} else if (this.getSlot() == EquipmentSlot.FEET) {
			return enchantment.category == EnchantmentCategory.ARMOR_FEET || enchantment.category == EnchantmentCategory.ARMOR;
		}
		return false;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return 0;
	}

	/**
	 * Minimum percent damage will be reduced to if the full set is worn
	 */
	public abstract float getFullSetBaseReduction();

	/**
	 * Gets the max damage that a piece of this armor in a given slot can absorb of a specific type.
	 *
	 * @apiNote A value of zero means that there is no special bonus blocking powers for that damage type, and the piece's base reduction will be get used instead by the
	 * damage calculation event.
	 */
	public abstract float getMaxDamageAbsorb(EquipmentSlot slot, DamageSource source);

	/**
	 * Gets the overall effectiveness of a given slots piece.
	 */
	public float getPieceEffectiveness(EquipmentSlot slot) {
		if (slot == EquipmentSlot.FEET || slot == EquipmentSlot.HEAD) {
			return 0.2F;
		} else if (slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS) {
			return 0.3F;
		}
		return 0;
	}
}