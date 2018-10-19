package com.ruseps.world.content.skill.impl.summoning;

import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class BossPets {

	public enum BossPet {
		
		PET_CHAOS_ELEMENTAL(3200, 3033, 11995),
		PET_KING_BLACK_DRAGON(50, 3030, 11996),
		PET_GENERAL_GRAARDOR(6260, 3031, 11997),
		PET_TZTOK_JAD(2745, 3032, 11978),
		PET_CORPOREAL_BEAST(8133, 3034, 12001),
		PET_KREE_ARRA(6222, 3035, 12002),
		PET_KRIL_TSUTSAROTH(6203, 3036, 12003),
		PET_COMMANDER_ZILYANA(6247, 3037, 12004),
		PET_DAGANNOTH_SUPREME(2881, 3038, 12005),
		PET_DAGANNOTH_PRIME(2882, 3039, 12006),
		PET_DAGANNOTH_REX(2883, 3040, 11990),
		PET_FROST_DRAGON(51, 3047, 11991),
		PET_TORMENTED_DEMON(8349, 3048, 11992),
		PET_KALPHITE_QUEEN(1158, 3050, 11993),
		PET_SLASH_BASH(2060, 3051, 11994),
		PET_PHOENIX(8549, 3052, 11989),
		PET_BANDOS_AVATAR(4540, 3053, 11988),
		PET_NEX(13447, 3054, 11987),
		PET_JUNGLE_STRYKEWYRM(9467, 3055, 11986),
		PET_DESERT_STRYKEWYRM(9465, 3056, 11985),
		PET_ICE_STRYKEWYRM(9463, 3057, 11984),
		PET_GREEN_DRAGON(941, 3058, 11983),
		PET_BABY_BLUE_DRAGON(52, 3059, 11982),
		PET_BLUE_DRAGON(55, 3060, 11981),
		PET_BLACK_DRAGON(54, 3061, 11979),
		PET_SCORPIA(2001, 3063, 11975),
		PET_SKOTIZO(7286, 3064, 11967),
		PET_WILDYWRYM(3334, 3066, 11970),
		PET_BORK(7134, 3067, 11971),
		PET_BARRELCHEST(5666, 3068, 11972),
		PET_SIRE(5886, 3070, 11973),
		PET_ROCK(1265, 3069, 11974),
		PET_LIZADMAN(6766, 3065, 11969),
		ROCK_GOLEM(-1, 6723, 13321),
		HERON(-1, 6724, 13320),
		BEAVER(-1, 6726, 13322),
		TANGLEROOT(-1, 6727, 13323),
		ROCKY(-1, 6728, 13324),
		SQUIRREL(-1, 6729, 13325),
		RIFT_GUARDIAN(-1, 6730, 13326),
		HELLPUPY(963, 963, 13247),
		KRAKEN(6640, 6640, 12655),
		BABY_MOLE(5781, 5781, 12646),
		OLMLET(-1, 6731, 13327),
		PET_VENENATIS(2000, 3062, 11976);
		
		
		private final int npcId; 
		private final int spawnNpcId; 
		private final int itemId;
		
		BossPet(int npcId, int spawnNpcId, int itemId) {
			this.npcId = npcId;
			this.spawnNpcId = spawnNpcId;
			this.itemId = itemId;
		}
		
		public static BossPet forId(int itemId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getItemId() == itemId) {
					return p;
				}
			}
			return null;
		}
		
		public static BossPet forSpawnId(int spawnNpcId) {
			for(BossPet p : BossPet.values()) {
				if(p != null && p.getSpawnNpcId() == spawnNpcId) {
					return p;
				}
			}
			return null;
		}

		public int getSpawnNpcId() {
			return spawnNpcId;
		}

		public int getItemId() {
			return itemId;
		}
	}
	
	public static boolean pickup(Player player, NPC npc) {
		BossPet pet = BossPet.forSpawnId(npc.getId());
		if(pet != null) {
			if(player.getSummoning().getFamiliar() != null && player.getSummoning().getFamiliar().getSummonNpc() != null && player.getSummoning().getFamiliar().getSummonNpc().isRegistered()) {
				if(player.getSummoning().getFamiliar().getSummonNpc().getId() == pet.getSpawnNpcId() && player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
					player.getSummoning().unsummon(true, true);
					player.getPacketSender().sendMessage("You pick up your pet.");
					return true;
				} else {
					player.getPacketSender().sendMessage("This is not your pet to pick up.");
				}
			} else {
				player.getPacketSender().sendMessage("This is not your pet to pick up.");
			}
		}
		return false;
	}
	
}
