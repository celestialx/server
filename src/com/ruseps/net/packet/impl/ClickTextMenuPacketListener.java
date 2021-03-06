package com.ruseps.net.packet.impl;

import com.ruseps.model.PlayerRights;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.world.content.clan.ClanChatManager;
import com.ruseps.world.entity.impl.player.Player;

public class ClickTextMenuPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {

		int interfaceId = packet.readShort();
		int menuId = packet.readByte();

		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.MANAGER) {
			player.getPacketSender().sendConsoleMessage("Clicked text menu: "+interfaceId+", menuId: "+menuId);
		}
		
		if(interfaceId >= 29344 && interfaceId <= 29443) { // Clan chat list
			int index = interfaceId - 29344;
			ClanChatManager.handleMemberOption(player, index, menuId);
		}
		
	}

	public static final int OPCODE = 222;
}
