package com.ruseps.net.packet.impl;

import com.ruseps.model.Flag;
import com.ruseps.model.Item;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.container.impl.Equipment;
import com.ruseps.model.container.impl.Inventory;
import com.ruseps.model.definitions.WeaponAnimations;
import com.ruseps.model.definitions.WeaponInterfaces;
import com.ruseps.model.definitions.ItemDefinition.EquipmentType;
import com.ruseps.net.packet.Packet;
import com.ruseps.net.packet.PacketListener;
import com.ruseps.util.Misc;
import com.ruseps.world.content.BonusManager;
import com.ruseps.world.content.Sounds;
import com.ruseps.world.content.Sounds.Sound;
import com.ruseps.world.content.combat.magic.Autocasting;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.minigames.impl.Dueling;
import com.ruseps.world.content.minigames.impl.Dueling.DuelRule;
import com.ruseps.world.entity.impl.player.Player;

/**
 * This packet listener manages the equip action a player
 * executes when wielding or equipping an item.
 * 
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		int id = packet.readShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		if(player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */) {
			player.getPacketSender().sendInterfaceRemoval();
			//return;
		}
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			/*
			 * Making sure slot is valid.
			 */
			if (slot >= 0 && slot <= 28) {
				Item item = player.getInventory().getItems()[slot].copy();
				if(!player.getInventory().contains(item.getId()))
					return;
				/*
				 * Making sure item exists and that id is consistent.
				 */
				if (item != null && id == item.getId()) {
					for (Skill skill : Skill.values()) {
						if(skill == Skill.CONSTRUCTION)
							continue;
						if (item.getDefinition().getRequirement()[skill.ordinal()] > player.getSkillManager().getMaxLevel(skill)) {
							StringBuilder vowel = new StringBuilder();
							if (skill.getName().startsWith("a") || skill.getName().startsWith("e") || skill.getName().startsWith("i") || skill.getName().startsWith("o") || skill.getName().startsWith("u")) {
								vowel.append("an ");
							} else {
								vowel.append("a ");
							}
							player.getPacketSender().sendMessage("You need " + vowel.toString() + Misc.formatText(skill.getName()) + " level of at least " + item.getDefinition().getRequirement()[skill.ordinal()] + " to wear this.");
							return;
						}
					}
					int equipmentSlot = item.getDefinition().getEquipmentSlot();
					if(equipmentSlot == -1) {
						return;
					}
					if((item.getDefinition().getEquipmentType() == EquipmentType.WEAPON && !item.getDefinition().isWeapon()) &&!item.getDefinition().isCustom() || item.getDefinition().isNoted()) {
						return;
					}
					Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
					if(player.getLocation() == Location.DUEL_ARENA) {
						for(int i = 10; i < player.getDueling().selectedDuelRules.length; i++) {
							if(player.getDueling().selectedDuelRules[i]) {
								DuelRule duelRule = DuelRule.forId(i);
								if(equipmentSlot == duelRule.getEquipmentSlot() || duelRule == Dueling.DuelRule.NO_SHIELD && item.getDefinition().isTwoHanded()) {
									player.getPacketSender().sendMessage("The rules that were set do not allow this item to be equipped.");
									return;
								}
							}
						}
						if(player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
							if(equipmentSlot == Equipment.WEAPON_SLOT || item.getDefinition().isTwoHanded()) {
								player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
								return;
							}
						}
					}
					if (player.hasStaffOfLightEffect() && equipItem.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender().sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
					if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
						int amount = equipItem.getAmount() + item.getAmount() <= Integer.MAX_VALUE ? equipItem.getAmount() + item.getAmount() : Integer.MAX_VALUE;
						player.getInventory().delete(item);
						player.getEquipment().getItems()[equipmentSlot].setAmount(amount);
						equipItem.setAmount(amount);
						player.getEquipment().refreshItems();
					} else {
						if (item.getDefinition().isTwoHanded() && item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
							int slotsNeeded = 0;
							if (player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT) && player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
								slotsNeeded++;
							}
							if (player.getInventory().getFreeSlots() >= slotsNeeded) {
								Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
								player.getInventory().add(shield);
								player.getInventory().delete(item);
								player.getEquipment().delete(shield);
								player.getInventory().add(equipItem);
								player.getInventory().add(shield);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().full();
								return;
							}
						} else if (equipmentSlot == Equipment.SHIELD_SLOT && player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition().isTwoHanded()) {
							player.getInventory().setItem(slot, player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
							player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
							player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
							resetWeapon(player);
						} else {
							if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot() && equipItem.getId() != -1) {
								if(player.getInventory().contains(equipItem.getId())) {
									player.getInventory().delete(item);
									player.getInventory().add(equipItem);
								} else
									player.getInventory().setItem(slot, equipItem);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().setItem(slot, new Item(-1, 0));
								player.getEquipment().setItem(item.getDefinition().getEquipmentSlot(), item);
							}
						}
					}
					if (equipmentSlot == Equipment.WEAPON_SLOT) {
						resetWeapon(player);
					} else if(equipmentSlot == Equipment.RING_SLOT && item.getId() == 2570) {
						player.getPacketSender().sendMessage("<img=10> <col=996633>Warning! The Ring of Life special effect does not work in the Wilderness or").sendMessage("<col=996633> Duel Arena.");
					}

					if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 4153) {
						player.getCombatBuilder().cooldown(false);
					}

					player.setCastSpell(null);
					BonusManager.update(player);
					player.getEquipment().refreshItems();
					player.getInventory().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Sounds.sendSound(player, Sound.EQUIP_ITEM);
				}
			}
			break;
		}
	}

	public static void resetWeapon(Player player) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		WeaponInterfaces.assign(player, weapon);
		WeaponAnimations.assign(player, weapon);
		if(player.getAutocastSpell() != null || player.isAutocast()) {
			Autocasting.resetAutocast(player, true);
			player.getPacketSender().sendMessage("Autocast spell cleared.");
		}
		player.setSpecialActivated(false);
                player.getPacketSender().sendSpriteChange(41006, 945);
		CombatSpecial.updateBar(player);
	}

	public static final int OPCODE = 41;
}