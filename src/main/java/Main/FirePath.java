/*
 * FirePath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;

public class FirePath extends Character {

        public FirePath(String name) {
            // Hardcode "fire" as the second argument so the parent class saves it
            super(name, "fire", 120, 35, 12); // name, elementPath, hp, attack, defense
        }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " slashes with fire at " + target.getName() + "!");
        int damage = getAttackPower() + 10;
        damage = calculateFinalDamage(damage);   // Apply Fire Imbue
        target.receiveDamage(damage);
        increaseMastery(3);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (gu.getName().toLowerCase()) {
            case "fireball":
                int damage = (int) (getAttackPower() * 1.9);
                target.receiveDamage(damage);
                System.out.println("It deals " + damage + " fire damage!");
                increaseMastery(10);
                break;

            case "fireimbue":
                System.out.println(getName() + "'s attacks are now imbued with fire!");
                addStatusEffect(StatusEffect.fireImbue());
                increaseMastery(8);
                break;

            case "fireslash":
                damage = getAttackPower() + 30;
                damage = calculateFinalDamage(damage);   // Apply Fire Imbue
                target.receiveDamage(damage);
                System.out.println("Powerful close-range slash deals " + damage + " damage!");
                increaseMastery(12);
                break;

            default:
                System.out.println("Unknown skill! Using basic effect.");
                target.setHp(target.getHp() - 25);
        }
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        List<GuItem> skills = new ArrayList<>();
        skills.add(new GuItem("FireBall", "Launches a ball of fire", "Fire", 45, 1, "Damage"));
        skills.add(new GuItem("FireImbue", "Imbues attacks with fire", "Fire", 30, 1, "Buff"));
        skills.add(new GuItem("FireSlash", "Powerful close range fire slash", "Fire", 50, 1, "Damage"));
        return skills;
    }
}