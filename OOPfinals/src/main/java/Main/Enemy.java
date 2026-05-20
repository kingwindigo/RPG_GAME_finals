package Main;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {

    public Enemy(String name, String elementPath, int hp, int attackPower, int defensePower) {
        super(name, elementPath, hp, attackPower, defensePower);
    }

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " attacks " + target.getName() + "!");
        target.receiveDamage(getAttackPower());
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        // Simple default enemy skill execution behavior
        System.out.println(getName() + " uses " + gu.getName() + "!");
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        // Return an empty list or basic monster moves
        return new ArrayList<>();
    }   
}