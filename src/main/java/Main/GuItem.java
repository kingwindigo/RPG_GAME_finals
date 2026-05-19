package Main;

public class GuItem {
    
    private String name;
    private String description;
    private String path;        // Fire, Water, Earth, Wood, Dark, Illusion, Wild
    private int power;          // Base power of the Gu
    private int cost;           // Action points cost (usually 1)
    private String effectType;  // "Damage", "Heal", "Buff", "Shield", "Debuff", "AOE"

    // Constructor
    public GuItem(String name, String description, String path, int power, int cost, String effectType) {
        this.name = name;
        this.description = description;
        this.path = path;
        this.power = power;
        this.cost = cost;
        this.effectType = effectType;
    }

    // Getters (Encapsulation - no setters needed for items)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    public String getEffectType() {
        return effectType;
    }

    // Useful method for display
    @Override
    public String toString() {
        return name + " (" + path + ") - " + description;
    }

    // You can add more methods later
    // NEW: This method will handle what the Gu actually does
    public void activate(Character user, Character target) {
        System.out.println(user.getName() + " activates " + name + "!");
        
        // Basic default behavior based on effectType
        switch (effectType.toLowerCase()) {
            case "damage":
                int dmg = (int)(user.getAttackPower() * (power / 30.0));
                target.setHp(target.getHp() - dmg);
                System.out.println("Dealt " + dmg + " damage!");
                break;
                
            case "heal":
                int heal = power;
                target.setHp(target.getHp() + heal);
                System.out.println("Restored " + heal + " HP!");
                break;
                
            case "shield":
                System.out.println(target.getName() + " is now shielded!");
                // TODO: Apply shield status later
                break;
                
            case "buff":
                System.out.println(target.getName() + "'s defense increased!");
                break;
                
            default:
                System.out.println("No specific effect defined.");
        }
    }
}