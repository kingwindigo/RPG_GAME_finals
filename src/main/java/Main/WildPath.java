package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WildPath extends Character {

    private int guUsesThisTurn = 0;   // Track limit
    private List<GuItem> currentTurnSkills = new ArrayList<>();
    private final Random rand = new Random();

    public WildPath(String name) {
        super(name, "wild", 135, 30, 20);   // Balanced stats
        // Initialize with a baseline pool so it's never empty on Turn 1
        generateRandomSkills();
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " strikes with primal, untamed force at " + target.getName() + "!");
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

        System.out.println("🌀 " + getName() + " (Wild) mimics and channels " + gu.getName() + " with wild instability!");

        // --- THE 20% EFFECTIVENESS PENALTY ---
        // Instead of hardcoding math for every single skill, we intercept the target's HP directly
        int hpBefore = target.getHp();
        
        // Execute the native skill execution
        gu.activate(this, target);
        
        // Calculate what changed (Damage dealt or Healing received)
        int hpDifference = Math.abs(target.getHp() - hpBefore);
        
        if (hpDifference > 0) {
            // Revert 20% of the effect to simulate the penalty
            int penaltyReduction = (int) (hpDifference * 0.20);
            
            if (target.getHp() < hpBefore) {
                // It was a damage skill -> Give them back 20% of the damage taken
                target.setHp(target.getHp() + penaltyReduction);
                System.out.println("⚠️ Wild instability reduces damage effectiveness by 20% (Mitigated: " + penaltyReduction + " HP)");
            } else {
                // It was a healing skill -> Strip 20% of the healed amount
                target.setHp(target.getHp() - penaltyReduction);
                System.out.println("⚠️ Wild instability reduces healing effectiveness by 20% (Reduced: " + penaltyReduction + " HP)");
            }
        }

        guUsesThisTurn++;
        increaseMastery(7);
    }

    /**
     * Scrambles and builds a completely randomized set of 3 skills from global paths
     */
    public void generateRandomSkills() {
        currentTurnSkills.clear();
        
        // A master list of all implemented skills across your codebase
        List<GuItem> globalSkillPool = new ArrayList<>();
        globalSkillPool.add(new GuItem("Fire Ball", "Launches a ball of fire", "Fire", 45, 1, "Damage"));
        globalSkillPool.add(new GuItem("Water Shield", "Protects an ally", "Water", 35, 1, "Shield"));
        globalSkillPool.add(new GuItem("Wood Sustain", "Strong single target heal", "Wood", 45, 1, "Heal"));
        globalSkillPool.add(new GuItem("Tree of Life", "Summons healing tree", "Wood", 35, 1, "Heal"));
        globalSkillPool.add(new GuItem("Wood Buff", "Buffs an ally with defense properties", "Wood", 30, 1, "Buff"));
        globalSkillPool.add(new GuItem("Dark Penetration", "Devastating raw power strike", "Dark", 42, 1, "Damage"));
        globalSkillPool.add(new GuItem("Dark AOE", "High risk enemy team blast", "Dark", 25, 1, "Damage"));
        globalSkillPool.add(new GuItem("Illusion Attack", "Forces enemy friendly fire", "Illusion", 35, 1, "Debuff"));
        globalSkillPool.add(new GuItem("Illusion Creation", "Spawns a random protection construct", "Illusion", 30, 1, "Utility"));
        globalSkillPool.add(new GuItem("False Stats", "Tricks the target's parameters", "Illusion", 20, 1, "Debuff"));

        System.out.println("🎲 " + getName() + "'s primal energy shifts! A new set of skills manifests...");
        
        // Pick 3 completely random, unique skills out of the master list
        while (currentTurnSkills.size() < 3 && !globalSkillPool.isEmpty()) {
            int index = rand.nextInt(globalSkillPool.size());
            GuItem chosen = globalSkillPool.remove(index); // Removes to prevent rolling duplicates in the same turn
            currentTurnSkills.add(chosen);
            System.out.println("   ✨ Manifested: [" + chosen.getName() + "]");
        }
    }

    // Call this at the start/end of every new turn round loop
    public void resetGuUses() {
        this.guUsesThisTurn = 0;
        generateRandomSkills(); // Instantly reshuffles the pool for the upcoming turn!
    }

    public int getGuUsesThisTurn() {
        return guUsesThisTurn;
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        // Returns the dynamic pool currently rolled for this turn
        return currentTurnSkills;
    }
}