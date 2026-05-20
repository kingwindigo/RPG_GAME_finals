/*
 * IllusionPath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IllusionPath extends Character {

    private final Random rand = new Random();

    public IllusionPath(String name) {
        super(name, "illusion", 120, 32, 14); // Balanced utility stats
    }

    @Override
    public void basicAttack(Character target) {
        // FIXED: Typo text updated from fire to illusion
        System.out.println(getName() + " flings a shimmering illusionary blade at " + target.getName() + "!");
        int damage = getAttackPower() + 10;
        target.receiveDamage(damage);
        increaseMastery(2);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        String skillName = gu.getName().toLowerCase();
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (skillName) {
            case "illusion attack":
                System.out.println("🌀 " + target.getName() + " is trapped in a maze of mirrors and loses their sense of direction!");
                
                // --- INSTANT FRIENDLY FIRE MECHANIC ---
                // Find a random enemy ally to take the hit instead of your players
                int randomSlot = rand.nextInt(3);
                Character enemyAlly = battleManager.getEnemyInSlot(randomSlot);
                
                // If the selected slot points back to themselves, or is empty/dead, grab a baseline default fallback strike
                if (enemyAlly == null || enemyAlly == target || enemyAlly.getHp() <= 0) {
                    // Quick check: find ANY random living enemy slot to take the hit
                    enemyAlly = null;
                    for (int i = 0; i < 3; i++) {
                        Character check = battleManager.getEnemyInSlot(i);
                        if (check != null && check != target && check.getHp() > 0) {
                            enemyAlly = check;
                            break;
                        }
                    }
                }

                // Execute the friendly fire execution payload
                int confusionDamage = target.getAttackPower(); // The enemy uses their OWN attack stat to hit their teammate!
                if (enemyAlly != null) {
                    System.out.println("😵 Confused! " + target.getName() + " turns around and blindly strikes their own teammate, " + enemyAlly.getName() + "!");
                    enemyAlly.receiveDamage(confusionDamage);
                } else {
                    // Fallback if it's the last remaining monster on the field
                    System.out.println("🌀 With no teammates left, " + target.getName() + " hits themselves in confusion!");
                    target.receiveDamage(confusionDamage);
                }
                
                increaseMastery(10);
                break;

                case "illusion creation":
                   System.out.println("✨ " + getName() + " weaves raw illusionary threads into a tangible protective construct!");

                   // Roll a 3-sided die (0, 1, or 2)
                   int rngRoll = rand.nextInt(3); 
                   boolean isRain = battleManager.isRainActive();

                   // --- SMART TARGET CORRECTION ---
                   // If the passed-in target is an enemy, ignore it! 
                   // Redirect the buff to yourself, or find a random living player ally.
                   Character friendlyTarget = this; // Default to the illusion caster
                   List<Character> livingPlayers = battleManager.getLivingPlayers();
                   if (!livingPlayers.isEmpty()) {
                       // Pick a random living party member to receive the buff
                       friendlyTarget = livingPlayers.get(rand.nextInt(livingPlayers.size()));
                   }

                   if (rngRoll == 0) {
                       System.out.println("🧱 The illusion solidifies into a phantom Earth Wall!");
                       friendlyTarget.addStatusEffect(StatusEffect.earthwall(60)); 
                   } else if (rngRoll == 1) {
                       System.out.println("💧 The illusion mimics ambient moisture, generating a Water Shield!");
                       friendlyTarget.addStatusEffect(StatusEffect.waterShield(isRain)); 
                   } else {
                       System.out.println("🌳 The illusion projects an entire spiritual ecosystem!");
                       battleManager.activateTreeOfLife(2); 
                   }

                   increaseMastery(8);
                   break;

            case "false stats":
                // Quick, clean backup debuff: strips 15 attack power from an enemy target
                System.out.println("🎭 False Stats! " + target.getName() + " is tricked into believing they are completely weak!");
                int damagePayload = (int) (getAttackPower() * 1.3);
                target.receiveDamage(damagePayload);
                System.out.println("Deals " + damagePayload + " mental illusion damage.");
                increaseMastery(12);
                break;

            default:
                System.out.println("Unknown Gu Item! Using basic effect.");
                target.setHp(target.getHp() - 25);
        }
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        List<GuItem> skills = new ArrayList<>();
        // Note: Illusion Attack targets enemies, Illusion Creation should target your own party allies!
        skills.add(new GuItem("Illusion Attack", "Forces an enemy to strike their own allies", "Illusion", 35, 1, "Debuff"));
        skills.add(new GuItem("Illusion Creation", "Spawns a random Wall, Shield, or Tree of Life", "Illusion", 30, 1, "Utility"));
        skills.add(new GuItem("False Stats", "Tricks the target's parameters", "Illusion", 20, 1, "Debuff"));
        return skills;
    }
}