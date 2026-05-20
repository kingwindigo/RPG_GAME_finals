/*
 * WoodPath class
 */
package Main;

import java.util.ArrayList;
import java.util.List;

public class WoodPath extends Character {

    public WoodPath(String name) {
        super(name, "wood", 120, 35, 12); // hp, attack, defense
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " attacks with roots at " + target.getName() + "!");
        int damage = getAttackPower() + 10;
        target.receiveDamage(damage);
        increaseMastery(2);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        String skillName = gu.getName().toLowerCase();
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (skillName) {
            case "wood sustain":
                if (battleManager.isTreeOfLifeActive()) {
                    System.out.println("❌ Cannot use Wood Sustain while Tree of Life is active!");
                } else {
                    System.out.println("🌿 " + target.getName() + " is greatly healed by nature!");
                    // Safeguard healing using the inherited setHp() logic bounds
                    target.setHp(target.getHp() + 55);
                    System.out.println("✨ " + target.getName() + " restored 55 HP!");
                    increaseMastery(10);
                }
                break;

            case "tree of life":
                System.out.println("🌳 Tree of Life has been summoned! Ambient nature elements empower the field.");
                battleManager.activateTreeOfLife(2);
                increaseMastery(8);
                break;

            case "wood buff":
                System.out.println("🛡️ Buffed " + target.getName() + " with wood properties!");
                // FIXED: Now calls the new factory blueprint successfully
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
        skills.add(new GuItem("Wood Sustain", "Strong single target heal", "Wood", 45, 1, "Heal"));
        // Tree of Life is set to "Heal". In your attackButton layout, this will prompt ally targeting selection.
        skills.add(new GuItem("Tree of Life", "Summons healing tree", "Wood", 35, 1, "Heal"));
        skills.add(new GuItem("Wood Buff", "Buffs an ally", "Wood", 30, 1, "Buff"));
        return skills;
    }
}