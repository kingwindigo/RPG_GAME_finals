package Main;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {

    private List<GuItem> availableSkills = new ArrayList<>();

    public Enemy(String name, String elementPath, int hp, int attackPower, int defensePower) {
        super(name, elementPath, hp, attackPower, defensePower);
        generateDefaultSkills();
    }

private void generateDefaultSkills() {
    String skillName = "";
    
    // Check the enemy's element path and assign the actual player skill name
    if (this.getElementPath().equalsIgnoreCase("fire")) {
        skillName = "fire"; // Matches your fireBall.gif!
    } else if (this.getElementPath().equalsIgnoreCase("water")) {
        skillName = "waterStream"; // (Or whatever your water skill name is)
    } else {
        skillName = "basicAttack";
    }

    String desc = "An elemental strike of " + skillName;
    int basePower = 30;
    int cost = 1;
    String type = "Damage";

    // Now the enemy's availableSkills list contains "fireBall"!
    availableSkills.add(new GuItem(skillName, desc, this.getElementPath(), basePower, cost, type)); 
}

    @Override
    public void basicAttack(Character target) {
        System.out.println(getName() + " attacks " + target.getName() + "!");
        target.receiveDamage(getAttackPower());
    }

    @Override
    public void useSkill(GuItem gu, Character target, BattleManager battleManager) {
        // Triggers the built-in logic inside your GuItem.java class perfectly!
        gu.activate(this, target);
    }

    @Override
    public List<GuItem> getAvailableSkills() {
        return this.availableSkills;
    }   
}