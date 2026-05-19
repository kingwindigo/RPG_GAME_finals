/*
 * FirePath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;

public class DarkPath extends Character {

    public DarkPath(String name) {
        super(name, "dark", 120, 35, 12); // hp, attack, defense
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " slashes with fire at " + target.getName() + "!");
        int damage = getAttackPower() + 10;
        target.receiveDamage(damage);
        
        // Small mastery gain on basic attack
        increaseMastery(2);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (gu.getName().toLowerCase()) {
            case "dark penetration":
                int damage = (int) (getAttackPower() * 1.9);
                target.receiveDamage(damage);
                System.out.println("It deals " + damage + " dark damage!");
                increaseMastery(10);
                break;

            case "dark imbue":
                System.out.println(getName() + "'s attacks are now imbued with darkness!");
                // TODO: Add a status effect later (e.g. fireImbued = true)
                increaseMastery(8);
                break;

            case "dark AOE":
                System.out.println("All beings now have lower defense!");
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
        skills.add(new GuItem("Dark Penetration", "Ignores most defense", "Dark", 42, 1, "Damage"));
        skills.add(new GuItem("Dark Imbue", "Adds defense penetration", "Dark", 30, 1, "Buff"));
        skills.add(new GuItem("Dark AOE", "Reduces enemy defense", "Dark", 25, 1, "Debuff"));
        return skills;
    }
}