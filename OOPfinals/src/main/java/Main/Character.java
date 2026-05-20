package Main;

import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    private String name;
    private int hp;
    private int maxHp;
    private int attackPower;
    private int defensePower;
    private int mastery;        // 15 to 100
    private int mana;
    private int maxMana;
    private String elementPath; // "fire", "water", "earth", etc.

    // Updated constructor to require the element path string up front
    public Character(String name, String elementPath, int hp, int attackPower, int defensePower) {
        this.name = name;
        this.elementPath = elementPath.toLowerCase();
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.mastery = 15;
        this.mana = 80;
        this.maxMana = 80;
    }

    public String getElementPath() {
        return this.elementPath;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public int getDefensePower() { return defensePower; }
    public int getMastery() { return mastery; }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
    }

    public void increaseMastery(int amount) {
        this.mastery = Math.min(100, this.mastery + amount);
    }
    
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }

    public void useMana(int amount) {
        this.mana = Math.max(0, this.mana - amount);
    }

    // Abstract methods for Polymorphism
    public abstract void basicAttack(Character target);
    public abstract void useSkill(GuItem gu, Character target, BattleManager battleManager);
    public abstract List<GuItem> getAvailableSkills();

    public void defend() {
        // Default defend logic
    }
    
    private List<StatusEffect> activeEffects = new ArrayList<>();

    public void addStatusEffect(StatusEffect effect) {
        activeEffects.add(effect);
        System.out.println(getName() + " gained: " + effect.getName());
    }

    public List<StatusEffect> getActiveEffects() {
        return activeEffects;
    }

    public void receiveDamage(int incomingDamage) {
        int damage = incomingDamage;

        for (StatusEffect eff : activeEffects) {
            if (eff.getType().equals("EarthWall") && eff.getWallHp() > 0) {
                int absorbed = Math.min(damage, eff.getWallHp());
                eff.damageWall(absorbed);
                System.out.println("Earth Wall absorbed " + absorbed + " damage!");
                if (eff.getWallHp() <= 0) {
                    System.out.println("Earth Wall has been destroyed!");
                }
                return;
            }
        }

        for (StatusEffect eff : activeEffects) {
            if (eff.getDamageReduction() > 0) {
                damage -= eff.getDamageReduction();
            }
            if (eff.getDefenseBonus() > 0) {
                damage = (int)(damage * 0.75);
            }
        }

        damage = Math.max(0, damage);
        setHp(getHp() - damage);
        System.out.println(getName() + " took " + damage + " damage!");
    }

    public void processEndOfTurnEffects() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            StatusEffect eff = activeEffects.get(i);

            if (eff.getHealPerTurn() > 0) {
                setHp(getHp() + eff.getHealPerTurn());
                System.out.println(getName() + " healed " + eff.getHealPerTurn() + " HP from " + eff.getName());
            }

            eff.decreaseDuration();

            if (eff.getDuration() <= 0 && !eff.getType().equals("EarthWall")) {
                System.out.println(eff.getName() + " has expired on " + getName());
                activeEffects.remove(i);
            }
        }
    }

    public int calculateFinalDamage(int baseDamage) {
        int finalDamage = baseDamage;
        for (StatusEffect eff : activeEffects) {
            if (eff.getType().equals("FireImbue")) {
                finalDamage += eff.getBonusDamage();
            }
        }
        return finalDamage;
    }
}