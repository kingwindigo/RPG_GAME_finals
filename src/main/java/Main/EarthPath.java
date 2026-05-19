package Main;

import java.util.ArrayList;
import java.util.List;

public class EarthPath extends Character {

    public EarthPath(String name) {
        super(name, "earth", 180, 25, 30); // High HP and Defense
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " throws a rock at " + target.getName() + "!");
        int damage = getAttackPower() + 5;
        target.receiveDamage(damage);
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        System.out.println(getName() + " uses " + gu.getName() + " on " + target.getName() + "!");

        switch (gu.getName().toLowerCase()) {
            case "earth wall":
                System.out.println(getName() + "Wall of stone now protects" + target.getName());
                target.addStatusEffect(StatusEffect.earthWall(80));
                increaseMastery(10);
                break;

            case "earth throw":
                int damage = (int) (getAttackPower() * 1.9);
                target.receiveDamage(damage);
                System.out.println("dealt" + damage + "to" + target.getName());
                increaseMastery(8);
                break;

            case "earth buff":
                System.out.println(target.getName() + " is covered in strengthening stone (1 turn)!");
                target.addStatusEffect(StatusEffect.earthBuff());
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
        skills.add(new GuItem("Earth Wall", "Creates a protective wall", "Earth", 20, 1, "Shield"));
        skills.add(new GuItem("Earth Throw", "Throws a heavy rock", "Earth", 38, 1, "Damage"));
        skills.add(new GuItem("Earth Buff", "Greatly increases defense", "Earth", 30, 1, "Buff"));
        return skills;
    }
}