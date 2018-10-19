package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.Graphic;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Locations;
import com.ruseps.model.Position;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.Projectile;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatHitTask;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

public class ArmadylAbyzou implements CombatStrategy {
	
	/** @author Lord Lewis **/

	private static final Animation attackanim = new Animation(64);
	private static final Graphic HeavyAttackGFX = new Graphic(287);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC abyzou = (NPC)entity;
		if(abyzou.isChargingAttack() || abyzou.getConstitution() <= 0) {
			return true;
		}
		
		if(abyzou.getConstitution() <= 1500 && !abyzou.hasHealed()) {
			victim.performGraphic(HeavyAttackGFX);
			abyzou.forceChat("Armadyl Banish Thee!");
			victim.dealDamage(new Hit(400, Hitmask.RED, CombatIcon.NONE));
			abyzou.setHealed(true);
		}
		
		Player target = (Player)victim;
		boolean crucio = false;
		for (Player t : Misc.getCombinedPlayerList(target)) {
			
			if (Locations.goodDistance(t.getPosition(), abyzou.getPosition(), 1)) {
				crucio = true;
				abyzou.getCombatBuilder().setVictim(t);
				new CombatHitTask(abyzou.getCombatBuilder(), new CombatContainer(abyzou, t, 1, CombatType.MAGIC, true)).handleAttack();
			}
		}
		if (crucio) {
			abyzou.performAnimation(attackanim);
			//abyzou.performGraphic(attack_graphic);
		}

		int attackStyle = Misc.getRandom(3);
	if (attackStyle == 0) { // Hand slash
			abyzou.performAnimation(attackanim);
			abyzou.getCombatBuilder().setContainer(new CombatContainer(abyzou, target, 1, 2, CombatType.MELEE, true));
	} else if (attackStyle == 1) { // Hand slash
				abyzou.performAnimation(attackanim);
				abyzou.getCombatBuilder().setContainer(new CombatContainer(abyzou, target, 1, 2, CombatType.MELEE, true));

		} else if (attackStyle == 2) { // Single Poison Blast
			
			abyzou.performAnimation(attackanim);
			abyzou.getCombatBuilder().setContainer(new CombatContainer(abyzou, target, 1, 2, CombatType.MELEE, true));
		} else if (attackStyle == 3) { // Skill Drain Attack
			abyzou.performAnimation(attackanim);
				abyzou.getCombatBuilder().setContainer(new CombatContainer(abyzou, target, 2, 2, CombatType.MELEE, true));
			TaskManager.submit(new Task(1, target, false) {
				@Override
				public void execute() {
					int skill = (6);
					Skill skillT = Skill.forId(skill);
					Player player = (Player) target;
					int lvl = player.getSkillManager().getCurrentLevel(skillT);
					lvl -= 4 + Misc.getRandom(3);
					 target.performGraphic(new Graphic(287));
					player.getSkillManager().setCurrentLevel(skillT, player.getSkillManager().getCurrentLevel(skillT) - lvl <= 0 ?  1 : lvl);
					target.getPacketSender().sendMessage("@red@Abyzou has drained your Magic!");
					stop();
				}
			});
		}
		return true;
	}
	

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 20;
	}
	
	

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}