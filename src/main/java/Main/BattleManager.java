package Main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BattleManager {

    private boolean rainActive = false;
    private int rainDuration = 0;
    private boolean treeOfLifeActive = false;
    private int treeOfLifeDuration = 0;
    private int currentWave = 1;
    private final int MAX_WAVES = 6;

    public int getCurrentWave() { return currentWave; }
    public boolean isGameWon() { return currentWave > MAX_WAVES; }
    private List<Character> playerParty = new ArrayList<>();
    private List<Character> enemyParty = new ArrayList<>();
    
    public BattleManager() {
        initializeSkillDictionary();
        enemyParty.add(new Enemy("Monster A", "earth", 100, 20, 10));
        enemyParty.add(new Enemy("Monster B", "water", 100, 20, 10));
        enemyParty.add(new Enemy("Monster C", "fire", 100, 20, 10));
    }
    
    public void setupParty(List<Character> party) {
        this.playerParty = party;
    }

    public Character getPlayerInSlot(int slot) {
        if (slot >= 0 && slot < playerParty.size()) {
            return playerParty.get(slot);
        }
        return null;
    }
    
    public Character getEnemyInSlot(int slot) {
        if (slot >= 0 && slot < enemyParty.size()) {
            return enemyParty.get(slot);
        }
        return null; // Returns null safely if the slot index is empty or invalid
    }

    public String queueElementAndEvaluate(String element) {
        currentTurnElements.add(element.toLowerCase());

        // Assuming a combo fires when all 3 players have locked in their actions
        if (currentTurnElements.size() == 3) {
            // Sort alphabetically so order of selection doesn't break recipes
            Collections.sort(currentTurnElements);

            String finalResult = "NORMAL";

            // Example check for Scalding Water: 2 fire, 1 water
            if (currentTurnElements.get(0).equals("fire") && 
                currentTurnElements.get(1).equals("fire") && 
                currentTurnElements.get(2).equals("water")) {
                finalResult = "Scalding Water";
            }
            // Example check for Meteor: 3 fire (if you change to a 3-element basic or 6-element ult)
            else if (currentTurnElements.get(0).equals("fireball") && 
                     currentTurnElements.get(1).equals("fireball") && 
                     currentTurnElements.get(2).equals("fireball")) {
                finalResult = "Meteor Strike";
            }

            // Clear the queue inside the backend manager so it's ready for the next round
            currentTurnElements.clear();
            return finalResult;
        }

        return "QUEUING"; 
    }
    
    
    public List<Character> getLivingPlayers() {
        List<Character> living = new ArrayList<>();
        for (Character p : playerParty) {
            if (p != null && p.getHp() > 0) {
                living.add(p);
            }
        }
        return living;
}
public boolean checkAndSpawnNextWave() {
    // Check if any enemy is still kicking
    for (Character e : enemyParty) {
        if (e != null && e.getHp() > 0) {
            return false; // Someone is still alive, don't spawn yet
        }
    }

    // If we get here, all enemies in the current slot array are dead!
    currentWave++;
    
    if (currentWave > MAX_WAVES) {
        System.out.println("🎉 All 6 waves cleared! Victory!");
        return true; 
    }

    System.out.println("⚔️ Wave " + currentWave + " Incoming!");
    enemyParty.clear();

    // 2. SCALING FORMULA: Base stats grow larger with each wave multiplier
    int hpScale = 100 + (currentWave * 25);     // Wave 2: 150HP, Wave 6: 250HP
    int atkScale = 20 + (currentWave * 4);      // Wave 2: 28ATK, Wave 6: 44ATK
    int defScale = 10 + (currentWave * 2);      // Wave 2: 14DEF, Wave 6: 22DEF

    // Re-populate the enemy slot configuration
    enemyParty.add(new Enemy("Omega Monster A", "earth", hpScale, atkScale, defScale));
    enemyParty.add(new Enemy("Omega Monster B", "water", hpScale, atkScale, defScale));
    enemyParty.add(new Enemy("Omega Monster C", "fire", hpScale, atkScale, defScale));

    return true;
}
public String executeEnemyAiTurn(Character enemy) {
        if (enemy == null || enemy.getHp() <= 0) return "";

        List<Character> targets = getLivingPlayers();
        if (targets.isEmpty()) {
            System.out.println("No targets available! Players are wiped out.");
            return "";
        }


        // 1. Pick a random living player target
        java.util.Random rand = new java.util.Random();
        Character randomTargetPlayer = targets.get(rand.nextInt(targets.size()));

        // 2. Pick a random available skill from the enemy profile
        List<GuItem> enemySkills = enemy.getAvailableSkills();

        if (!enemySkills.isEmpty() && rand.nextBoolean()) { 
            // 50% chance to cast an elemental skill if available
            GuItem chosenSkill = enemySkills.get(rand.nextInt(enemySkills.size()));

            // Execute the skill block
            enemy.useSkill(chosenSkill, randomTargetPlayer, this);
            return chosenSkill.getName(); // Returns element name e.g., "fire", "water"
        } else {
            // Fallback to basic attack
            enemy.basicAttack(randomTargetPlayer);
            return "basic";
        }
}
    // --- NEW COMBO FIELDS ---
    // Stores the elements current players have queued up for this specific turn execution
    private final List<String> currentTurnElements = new ArrayList<>();
    
    // Keep track of the active 3 heroes on the field
    private List<Character> activeParty = new ArrayList<>();




    
        private final Map<String, SkillVisualData> skillDictionary = new HashMap<>();



        private void initializeSkillDictionary() {
            // --- 1. Register Special Combo Spells ---
            skillDictionary.put("Scalding Water", new SkillVisualData("scalding_water", 2000));
            skillDictionary.put("Meteor Strike", new SkillVisualData("meteor_strike", 2500));
            skillDictionary.put("Mud Slide", new SkillVisualData("mud_slide", 1800));

            // --- 2. Register Your Base Element Fallbacks ---
            // If no combo triggers, these map the raw path string to its default attack asset
            skillDictionary.put("fireBall", new SkillVisualData("fireBall", 1700));
            skillDictionary.put("water", new SkillVisualData("water_skill", 1700));
            skillDictionary.put("earth", new SkillVisualData("earth_skill", 1700));
            skillDictionary.put("wind", new SkillVisualData("wind_skill", 1500));
            skillDictionary.put("nature", new SkillVisualData("nature_skill", 1900));
            skillDictionary.put("light", new SkillVisualData("light_skill", 1600));
            skillDictionary.put("dark", new SkillVisualData("dark_skill", 2200));
        }

    /**
     * Looks up a skill's visual data by its name or element state.
     */
    public SkillVisualData getVisualData(String key) {
        return skillDictionary.get(key);
    }
    /**
     * Call this when a player chooses an elemental skill.
     * @param element The path element ("fire", "water", "earth", etc.)
     * @return The name of a combo spell if triggered, "NORMAL" if it's a standard single spell, 
     * or "QUEUING" if we are still waiting for more player inputs.
     */


    public boolean isRainActive() { return rainActive; }
    public boolean isTreeOfLifeActive() { return treeOfLifeActive; }
    public int getRainDuration() { return rainDuration; }
    public int getTreeOfLifeDuration() { return treeOfLifeDuration; }

    public void activateRain(int duration) {
        this.rainActive = true;
        this.rainDuration = duration;
        System.out.println(" Heavy Rain covers the battlefield for " + duration + " turns!");
    }

    public void activateTreeOfLife(int duration) {
        this.treeOfLifeActive = true;
        this.treeOfLifeDuration = duration;
        System.out.println(" Tree of Life has been summoned for " + duration + " turns!");
    }

public void processGlobalEffects() { // Removed parameters so it's self-contained!
        // --- 1. WEATHER CONDITIONS TICK ---
        if (rainActive && rainDuration > 0) {
            System.out.println("🌧️ Rain continues to fall...");
            rainDuration--;
            if (rainDuration <= 0) {
                rainActive = false;
                System.out.println("The rain has cleared.");
            }
        }

        // --- 2. TREE OF LIFE FIELD TICK (FIXED: Uses playerParty directly) ---
        if (treeOfLifeActive && treeOfLifeDuration > 0) {
            System.out.println("🌳 Tree of Life is healing all allies...");

            // Read your class-level playerParty variable explicitly
            for (Character ally : this.playerParty) {
                if (ally != null && ally.getHp() > 0) {
                    ally.setHp(ally.getHp() + 18);
                    System.out.println("✨ " + ally.getName() + " was healed 18 HP by Tree of Life. (Current HP: " + ally.getHp() + ")");
                }
            }

            treeOfLifeDuration--;
            if (treeOfLifeDuration <= 0) {
                treeOfLifeActive = false;
                System.out.println("🥀 Tree of Life has withered away.");
            }
        }

    }
}