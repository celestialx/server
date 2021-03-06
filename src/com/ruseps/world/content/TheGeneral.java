package com.ruseps.world.content;

import com.ruseps.model.Animation;
import com.ruseps.model.GameObject;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Position;
import com.ruseps.util.Misc;
import com.ruseps.util.Stopwatch;
import com.ruseps.world.World;
import com.ruseps.world.entity.Entity;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class TheGeneral {
	/**
	 * author live:nrpker7839
	 */
	
	
	public static final int npc1 = 7553;
	private static int TIME = 2400; // 40 minutes? not sure lol
    private static boolean spawned = false;
    
	public static int[] COMMONLOOT = { 6199, 15501, 989, 3140, 4087, 11732, 989 };
	public static int[] RARELOOT = { 20000, 20001, 20002, 15220, 15018, 15020, 15019, 6585, 4151, 11283, 11846, 11848,
			11850, 11852, 11854, 11856 };
	public static int[] SUPERRARELOOT = { 13887, 13893, 13899, 13905, 13884, 13890, 13896, 13902, 13858, 13861, 13864,
			13867, 13870, 13873, 13876 };
	
	private static TheGeneral current;

	public static TheGeneral getCurrent() {
		return current;
	}

	public static void sequence() {
		if(spawned == true) {
			return;
		}
		if(TIME > 0) {
			TIME--;		
		} 
		if (TIME == 1200) {
			World.sendMessage("@or2@ [Boss Event] The boss event will take place in 20 minutes!");
		} 
		if (TIME <= 0) {
			TIME += 2400;
			spawned = true;
			NPC npc = new NPC(7553, new Position(2898, 2793, 0));
			World.register(npc);
			World.sendMessage("<img=10>@red@The General has spawned at the battle arena! ::arena");
			
		}
		
	}
	public static void giveLoot(Player player, NPC npc) {
		// int chance = Misc.getRandom(100);
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];

		// 1 Super rare
		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(superrare),
				new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true, 200));
		String itemName = (new Item(superrare).getDefinition().getName());
		String itemMessage = Misc.anOrA(itemName) + " " + itemName;
		World.sendMessage("<img=10><col=FF0000>The General dropped " + itemMessage + "!");

		// 2 rares
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(RARELOOT[Misc.getRandom(RARELOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		// Commons
		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

		GroundItemManager.spawnGroundItem(player,
				new GroundItem(new Item(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)]),
						new Position(2890 + Misc.getRandom(16), 2783 + Misc.getRandom(21), 0), "", true, 200, true,
						200));

	}

}
