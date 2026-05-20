/*
 * DarkPath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;

public class DarkPath extends Character {

    public DarkPath(String name) {
        // GLASS CANNON ADJUSTMENT: Lower HP (95), Massive Base Attack (46)
        super(name, "dark", 70, 46, 8); 
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " slashes with dark energy at " + target.getName() + "!");
        
        // Use calculateFinalDamage so that if Dark Imbue is active, it adds the bonus!
        int baseDamage = getAttackPower() + 10;
        int finalDamage = calculateFinalDamage(baseDamage);
        
        target.receiveDamage(finalDamage);
        increaseMastery(2);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        String skillName = gu.getName().toLowerCase();
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (skillName) {
            case "dark penetration":
                // Devastating damage multiplier
                int basePenDamage = (int) (getAttackPower() * 2.1);
                int finalPenDamage = calculateFinalDamage(basePenDamage);
                
                target.receiveDamage(finalPenDamage);
                System.out.println("🔮 Deep shadows pierce the target! Deals " + finalPenDamage + " dark damage!");
                increaseMastery(10);
                break;

            case "dark imbue":
                System.out.println("👁️ " + getName() + "'s weapon is shrouded in malicious darkness! Attacks are empowered!");
                // Buffs YOURSELF to enhance subsequent basic attacks and skills
                this.addStatusEffect(StatusEffect.darkImbue());
                increaseMastery(8);
                break;

                case "dark aoe":
                    System.out.println("💥 " + getName() + " unleashes a reckless explosion of forbidden dark matter across the battlefield!");

                    // 1. Recoil Cost: Dark user loses 15 HP to unleash this forbidden power
                    this.setHp(this.getHp() - 15);
                    System.out.println("⚠️ Recoil! " + getName() + " sacrificed 15 HP to fuel the spell! (Current HP: " + this.getHp() + ")");

                    // 2. Base damage calculation
                    int aoeDamage = (int) (getAttackPower() * 1.5); // Adjusted slightly lower since it hits everyone!

                    // 3. TRUE AOE LOOP: Cycle through all 3 possible enemy slots in the match
                    for (int i = 0; i < 3; i++) {
                        Character enemy = battleManager.getEnemyInSlot(i);

                        // Ensure the enemy slot isn't empty and the monster is actually alive
                        if (enemy != null && enemy.getHp() > 0) {
                            enemy.receiveDamage(aoeDamage);
                            System.out.println("🔮 Darkness engulfs " + enemy.getName() + " for " + aoeDamage + " damage!");
                        }
                    }

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
        skills.add(new GuItem("Dark Penetration", "Devastating raw power strike", "Dark", 42, 1, "Damage"));
        skills.add(new GuItem("Dark AOE", "High risk nuke: costs user HP, deals massive damage", "Dark", 25, 1, "Damage"));
        return skills;
    }
}