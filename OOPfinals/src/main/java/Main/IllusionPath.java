/*
 * FirePath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;

public class IllusionPath extends Character {

    public IllusionPath(String name) {
        super(name, "illusion", 120, 35, 12); // hp, attack, defense
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
            case "illusion attack":
                int damage = (int) (getAttackPower() * 1.9);
                target.setHp(target.getHp() - damage);
                System.out.println("It deals " + damage + " illusion damage!");
                increaseMastery(10);
                break;

            case "illusion creation":
                System.out.println("illusion has been created");
                // TODO: Add a status effect later (e.g. fireImbued = true)
                increaseMastery(8);
                break;

            case "false stats":
                System.out.println(target.getName() + "are now facing illusions");
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
        skills.add(new GuItem("Illusion Attack", "Debuff enemy with illusions", "Illusion", 35, 1, "Debuff"));
        skills.add(new GuItem("Illusion Creation", "Creates random helpful illusion", "Illusion", 30, 1, "Utility"));
        skills.add(new GuItem("False Stats", "Applies false stats on target", "Illusion", 20, 1, "Debuff"));
        return skills;
    }
}