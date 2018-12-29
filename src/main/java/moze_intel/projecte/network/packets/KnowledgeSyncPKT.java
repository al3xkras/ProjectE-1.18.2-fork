package moze_intel.projecte.network.packets;

import moze_intel.projecte.PECore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class KnowledgeSyncPKT {
	private final NBTTagCompound nbt;

	public KnowledgeSyncPKT(NBTTagCompound nbt)
	{
		this.nbt = nbt;
	}

	public static void encode(KnowledgeSyncPKT msg, PacketBuffer buf)
	{
		buf.writeCompoundTag(msg.nbt);
	}

	public static KnowledgeSyncPKT decode(PacketBuffer buf)
	{
		return new KnowledgeSyncPKT(buf.readCompoundTag());
	}

	public static class Handler
	{
		public static void handle(final KnowledgeSyncPKT message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() -> {
				PECore.proxy.getClientTransmutationProps().deserializeNBT(message.nbt);
				PECore.debugLog("** RECEIVED TRANSMUTATION DATA CLIENTSIDE **");
			});
		}
	}
}
