package Main;

import java.util.ArrayList;
import java.util.List;

public class WildPath extends Character {

    private int guUsesThisTurn = 0;   // Track limit

    public WildPath(String name) {
        super(name, "wild", 135, 30, 20);   // Balanced stats
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " strikes with primal force at " + target.getName() + "!");
        int damage = getAttackPower() + 12;
        target.receiveDamage(damage);
        increaseMastery(4);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        if (guUsesThisTurn >= 2) {
            System.out.println(getName() + " cannot use more than 2 Gu items this turn!");
            return;
        }

        System.out.println(getName() + " (Wild) channels " + gu.getName() + "!");

        // Wild Path uses the GuItem's own activate method
        gu.activate(this, target);

        guUsesThisTurn++;
        increaseMastery(7);
    }

    // Call this at the start of every new turn
    public void resetGuUses() {
        guUsesThisTurn = 0;
    }

    public int getGuUsesThisTurn() {
        return guUsesThisTurn;
    }
    @Override
    public List<GuItem> getAvailableSkills() {
        List<GuItem> skills = new ArrayList<>();
        // Wild can access skills from multiple paths
        skills.add(new GuItem("Fire Ball", "Launches a ball of fire", "Fire", 45, 1, "Damage"));
        skills.add(new GuItem("Water Shield", "Protects an ally", "Water", 35, 1, "Shield"));
        skills.add(new GuItem("Earth Buff", "Greatly increases defense", "Earth", 30, 1, "Buff"));
        skills.add(new GuItem("Dark Penetration", "Ignores most defense", "Dark", 42, 1, "Damage"));
        return skills;
    }
}