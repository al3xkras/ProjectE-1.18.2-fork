package moze_intel.projecte.gameObjs.items;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IAlchBagItem;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.container.AlchBagContainer;
import moze_intel.projecte.utils.AchievementHandler;
import moze_intel.projecte.utils.Constants;
import moze_intel.projecte.utils.ItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AlchemicalBag extends ItemPE
{
	private final String[] colors = new String[] {"white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};

	// MC Lang files have these unlocalized names mapped to raw color names
	private final String[] unlocalizedColors = new String[] {
			"item.fireworksCharge.white", "item.fireworksCharge.orange",
			"item.fireworksCharge.magenta", "item.fireworksCharge.lightBlue",
			"item.fireworksCharge.yellow", "item.fireworksCharge.lime",
			"item.fireworksCharge.pink", "item.fireworksCharge.gray",
			"item.fireworksCharge.silver", "item.fireworksCharge.cyan",
			"item.fireworksCharge.purple", "item.fireworksCharge.blue",
			"item.fireworksCharge.brown", "item.fireworksCharge.green",
			"item.fireworksCharge.red", "item.fireworksCharge.black"};
	
	public AlchemicalBag()
	{
		this.setUnlocalizedName("alchemical_bag");
		this.hasSubtypes = true;
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.ALCH_BAG_GUI, world, hand == EnumHand.MAIN_HAND ? 0 : 1, -1, -1);
		}
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (true || !(entity instanceof EntityPlayer))
		{
			return;
		}
		
		EntityPlayer player = (EntityPlayer) entity;
		ItemStack[] inv = null;// todo 1.9 AlchemicalBags.get(player, (byte) stack.getItemDamage());

		if (player.openContainer instanceof AlchBagContainer)
		{
			ItemStack[] openContainerInv = ((AlchBagContainer) player.openContainer).inventory.getInventory();
			for (int i = 0; i < openContainerInv.length; i++) // Do not use foreach - to avoid desync
			{
				ItemStack current = openContainerInv[i];
				if (current != null && current.getItem() instanceof IAlchBagItem)
				{
					((IAlchBagItem) current.getItem()).updateInAlchBag(openContainerInv, player, current);
				}
			}
			// Do not AlchemicalBags.set/syncPartial here - vanilla handles it because it's the open container
		}
		else
		{
			boolean hasChanged = false;
			for (int i = 0; i < inv.length; i++) // Do not use foreach - to avoid desync
			{
				ItemStack current = inv[i];
				if (current != null && current.getItem() instanceof IAlchBagItem)
				{
					hasChanged = ((IAlchBagItem) current.getItem()).updateInAlchBag(inv, player, current);
				}
			}

			if (!player.worldObj.isRemote && hasChanged)
			{
				//AlchemicalBags.set(player, ((byte) stack.getItemDamage()), inv);
				//AlchemicalBags.syncPartial(player, stack.getItemDamage());
			}
		}
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) 
	{
		return 1; 
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		String name = super.getItemStackDisplayName(stack);
		int i = stack.getItemDamage();

		if (stack.getItemDamage() > 15)
		{
			return name + " (" + I18n.translateToLocal("pe.debug.metainvalid.name") + ")";
		}

		String color = " (" + I18n.translateToLocal(unlocalizedColors[i]) + ")";
		return name + color;
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		super.onCreated(stack, world, player);
		
		if (!world.isRemote)
		{
			player.addStat(AchievementHandler.ALCH_BAG, 1);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs cTab, List list)
	{
		for (int i = 0; i < 16; ++i)
			list.add(new ItemStack(item, 1, i));
	}

	public static ItemStack getFirstBagWithSuctionItem(EntityPlayer player, ItemStack[] inventory)
	{
		for (ItemStack stack : inventory)
		{
			if (stack == null)
			{
				continue;
			}

			if (stack.getItem() == ObjHandler.alchBag)
			{
				/*ItemStack[] inv = AlchemicalBags.get(player, ((byte) stack.getItemDamage()));
				if (ItemHelper.invContainsItem(inv, new ItemStack(ObjHandler.blackHole, 1, 1))
						|| ItemHelper.invContainsItem(inv, new ItemStack(ObjHandler.voidRing, 1, 1)))
				return stack;*/
			}
		}

		return null;
	}
}
