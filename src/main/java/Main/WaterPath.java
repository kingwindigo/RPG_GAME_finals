package Main;

import java.util.ArrayList;
import java.util.List;

public class WaterPath extends Character {

    public WaterPath(String name) {
        super(name, "water", 130, 28, 18);
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " shoots a water blast at " + target.getName() + "!");
        int damage = getAttackPower() + 8;
        // FIXED: Use receiveDamage so shields/walls can intercept this attack!
        target.receiveDamage(damage);
        increaseMastery(3);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        String skillName = gu.getName().toLowerCase();
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");
        boolean rainActive = battleManager.isRainActive();

        switch (skillName) {
            case "water shield":
                System.out.println("💧 " + target.getName() + " is protected by a Water Shield!");
                target.addStatusEffect(StatusEffect.waterShield(rainActive));
                increaseMastery(9);
                break;

            case "water ball":
                int damage = (int) (getAttackPower() * 1.6);
                if (rainActive) {
                    damage = (int)(damage * 1.3);   // 30% stronger in rain
                    System.out.println("🌧️ (Rain empowered!)");
                }
                target.receiveDamage(damage);
                System.out.println("Deals " + damage + " water damage!");
                increaseMastery(10);
                break;

            case "rain":
                System.out.println("🌧️ Rain falls heavy across the battlefield!");
                battleManager.activateRain(3);
                increaseMastery(12);
                break;

            default:
                System.out.println("Unknown water skill used.");
        }
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        List<GuItem> skills = new ArrayList<>();
        // Note: Rain is set to "AOE". It will default to an offensive target track unless you add "AOE" to your buff targeting system, or just let them pick any enemy to cast the sky-wide weather event!
        skills.add(new GuItem("Water Shield", "Protects an ally", "Water", 35, 1, "Shield"));
        skills.add(new GuItem("Water Ball", "Water explosion attack", "Water", 40, 1, "Damage"));
        skills.add(new GuItem("Rain", "Creates rain on the battlefield", "Water", 25, 1, "AOE"));
        return skills;
    }
}