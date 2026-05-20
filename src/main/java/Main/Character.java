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

    public String getElementPath() { return this.elementPath; }
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

    public java.util.List<StatusEffect> getStatusEffects() {
        return this.activeEffects; 
    }

    public abstract void basicAttack(Character target);
    public abstract void useSkill(GuItem gu, Character target, BattleManager battleManager);
    public abstract List<GuItem> getAvailableSkills();

    public void defend() {}
    
    private List<StatusEffect> activeEffects = new ArrayList<>();

    public void addStatusEffect(StatusEffect effect) {
        activeEffects.add(effect);
        System.out.println(getName() + " gained: " + effect.getName());
    }

    public List<StatusEffect> getActiveEffects() { return activeEffects; }

    public void receiveDamage(int incomingDamage) {
        int remainingDamage = incomingDamage;

        if (this.getStatusEffects() != null) {
            // --- 1. CHECK FOR WATER SHIELD FLAT DAMAGE REDUCTION ---
            for (StatusEffect effect : this.activeEffects) {
                if (effect.getType().equalsIgnoreCase("WaterShield")) {
                    int reduction = effect.getDamageReduction();
                    remainingDamage = Math.max(0, remainingDamage - reduction);
                    System.out.println("💧 Water Shield mitigates -" + reduction + " damage! New damage payload: " + remainingDamage);
                }
                // --- 2. CHECK FOR WOOD BUFF DEFENSE BOOST ---
                else if (effect.getType().equalsIgnoreCase("WoodBuff")) {
                    int reduction = effect.getDefenseBonus();
                    remainingDamage = Math.max(0, remainingDamage - reduction);
                    System.out.println("🌿 Wood Buff bark hardens defenses! Mitigated -" + reduction + " damage.");
                }
            }

            // --- 3. CHECK FOR EARTH WALL ABSORPTION ---
            for (int i = 0; i < this.activeEffects.size(); i++) {
                StatusEffect effect = this.activeEffects.get(i);

                if (effect.getType().equalsIgnoreCase("EarthWall")) {
                    System.out.println("🧱 Earth Wall intercepts the attack! Current Wall HP: " + effect.getWallHp());

                    if (effect.getWallHp() >= remainingDamage) {
                        effect.damageWall(remainingDamage);
                        remainingDamage = 0;
                        System.out.println("🧱 The wall held! Remaining Wall HP: " + effect.getWallHp());
                    } else {
                        remainingDamage -= effect.getWallHp();
                        effect.damageWall(effect.getWallHp()); 
                        System.out.println("💥 The Earth Wall was shattered! " + remainingDamage + " damage bleeds through.");
                    }
                    break; 
                }
            }

            this.activeEffects.removeIf(effect -> effect.getType().equalsIgnoreCase("EarthWall") && effect.getWallHp() <= 0);
        }

        // --- 4. APPLY REMAINING DAMAGE ---
        if (remainingDamage > 0) {
            int finalHp = this.getHp() - remainingDamage;
            this.setHp(Math.max(0, finalHp)); 
            System.out.println(getName() + " takes " + remainingDamage + " actual damage. Current HP: " + this.getHp());
        }
    }

    public void processEndOfTurnEffects() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            StatusEffect eff = activeEffects.get(i);

            if (eff.getHealPerTurn() > 0) {
                setHp(getHp() + eff.getHealPerTurn());
                System.out.println(getName() + " healed " + eff.getHealPerTurn() + " HP from " + eff.getName());
            }

            eff.decreaseDuration();
            

            if (eff.getDuration() <= 0 && !eff.getType().equalsIgnoreCase("EarthWall")) {
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