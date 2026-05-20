package Main;

public class StatusEffect {
    private String name;
    private int duration;
    private String type;

    // Earth Wall
    private int wallHp;

    // Fire Imbue
    private int bonusDamage;

    // Water Shield
    private int damageReduction;
    private int healPerTurn;

    // Earth Buff
    private int defenseBonus;

    public StatusEffect(String name, int duration, String type) {
        this.name = name;
        this.duration = duration;
        this.type = type;
    }

    // Static Factory Methods
    public static StatusEffect fireImbue() {
        StatusEffect eff = new StatusEffect("Fire Imbue", 2, "FireImbue");
        eff.bonusDamage = 15;
        return eff;
    }

    public static StatusEffect waterShield(boolean isRainActive) {
        StatusEffect eff = new StatusEffect("Water Shield", 2, "WaterShield");
        if(isRainActive == true){
            System.out.println("Water shield is Empowered by the rain!");
            eff.damageReduction = 20;
            eff.healPerTurn = 13;
        }
        else{
            eff.damageReduction = 12;
            eff.healPerTurn = 8;
        }

        return eff;
    }

    public static StatusEffect earthWall(int hp) {
        StatusEffect eff = new StatusEffect("Earth Wall", 99, "EarthWall"); // long duration
        eff.wallHp = hp;
        return eff;
    }

    public static StatusEffect earthBuff() {
        StatusEffect eff = new StatusEffect("Earth Buff", 2, "EarthBuff");
        eff.defenseBonus = 30;
        return eff;
    }

    // Getters
    public String getName() { return name; }
    public int getDuration() { return duration; }
    public String getType() { return type; }
    public int getWallHp() { return wallHp; }
    public int getBonusDamage() { return bonusDamage; }
    public int getDamageReduction() { return damageReduction; }
    public int getHealPerTurn() { return healPerTurn; }
    public int getDefenseBonus() { return defenseBonus; }

    public void decreaseDuration() { if (duration > 0) duration--; }
    public void damageWall(int dmg) { wallHp -= dmg; }
}