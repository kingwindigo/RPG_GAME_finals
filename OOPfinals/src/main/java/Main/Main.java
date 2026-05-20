package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BattleManager battleManager = new BattleManager();

        System.out.println("=== Elemental Paths RPG - 3v3 Test ===\n");

        // ====================== PLAYER PARTY ======================
        Character p1 = new EarthPath("P1");
        Character p2 = new WaterPath("P2");
        Character p3 = new WoodPath("P3");

        List<Character> party = new ArrayList<>();
        party.add(p1);
        party.add(p2);
        party.add(p3);

        // ====================== ENEMIES ======================
        List<Character> enemies = new ArrayList<>();
        enemies.add(new Enemy("Goblin Brute", 280, 22));
        enemies.add(new Enemy("Shadow Stalker", 200, 30));
        enemies.add(new Enemy("Stone Golem", 350, 18));

        System.out.println("Battle Started!\n");

        while (true) {
            if (isAllDead(party)) {
                System.out.println("\n=== DEFEAT - Your party has been wiped out! ===");
                break;
            }
            if (isAllDead(enemies)) {
                System.out.println("\n=== VICTORY! All enemies defeated! ===");
                break;
            }

            // ==================== PLAYER TURN ====================
            System.out.println("\n=== PLAYER TURN (2 actions per character) ===");
            printStatus(party, enemies);

            for (Character ch : party) {
                if (ch.getHp() <= 0) continue;

                for (int action = 1; action <= 2; action++) {
                    if (ch.getHp() <= 0) break;

                    System.out.println("\n→ " + ch.getName() + " - Action " + action 
                                      + " (" + ch.getHp() + " HP | " 
                                      + ch.getMana() + "/" + ch.getMaxMana() + " Mana)");

                    System.out.println("1. Basic Attack");
                    System.out.println("2. Use Skill");
                    System.out.println("3. Defend");
                    System.out.println("4. Flee");

                    int choice = getValidInput(sc, 1, 4);

                    if (choice == 1) {
                        Character target = chooseTarget(sc, enemies, "Enemy");
                        ch.basicAttack(target);
                    } 
                    else if (choice == 2) {
                        List<GuItem> skills = ch.getAvailableSkills();   // ← Now calls from Path class

                        if (skills.isEmpty()) {
                            System.out.println("No skills available.");
                            continue;
                        }

                        System.out.println("Choose Skill:");
                        for (int i = 0; i < skills.size(); i++) {
                            System.out.println((i + 1) + ". " + skills.get(i).getName());
                        }

                        int skillIdx = getValidInput(sc, 1, skills.size()) - 1;
                        GuItem selectedGu = skills.get(skillIdx);

                        if (ch.getMana() < 20) {
                            System.out.println("Not enough Mana!");
                            continue;
                        }
                        
                        String targetType = getTargetType(selectedGu);
                        Character target = chooseTarget(sc, 
                            targetType.equals("ally") ? party : enemies, 
                            targetType.equals("ally") ? "Ally" : "Enemy");

                        ch.useSkill(selectedGu, target, battleManager);
                        ch.useMana(20);
                    } 
                    else if (choice == 3) {
                        ch.defend();
                        action++;
                    } 
                    else if (choice == 4) {
                        if (attemptFlee(sc, party)) {
                            System.out.println("You successfully fled!");
                            sc.close();
                            return;
                        }
                        break; // End this character's actions if flee fails
                    }
                }
            }

            // ==================== ENEMY TURN ====================
            System.out.println("\n=== ENEMY TURN ===");
            for (Character enemy : enemies) {
                if (enemy.getHp() <= 0) continue;
                
                Character target = getRandomAlive(party);
                if (target != null) {
                    enemy.basicAttack(target);
                }
            }
            System.out.println("\n=== END OF TURN EFFECTS ===");
            for (Character c : party) {
                if (c.getHp() > 0) c.processEndOfTurnEffects();
            }
            for (Character e : enemies) {
                if (e.getHp() > 0) e.processEndOfTurnEffects();
            }
            // ==================== GLOBAL EFFECTS ====================
            System.out.println("\n=== GLOBAL EFFECTS PHASE ===");
            battleManager.processGlobalEffects(party, enemies);

            // Process individual character end-of-turn effects
            for (Character c : party) {
                if (c.getHp() > 0) c.processEndOfTurnEffects();
            }
            for (Character e : enemies) {
                if (e.getHp() > 0) e.processEndOfTurnEffects();
            }
            
            
        }
        sc.close();
    }
    

    // ====================== HELPER METHODS ======================

    private static boolean isAllDead(List<Character> list) {
        return list.stream().allMatch(c -> c.getHp() <= 0);
    }

    private static void printStatus(List<Character> party, List<Character> enemies) {
        System.out.println("\n--- Your Party ---");
        for (Character c : party) {
            System.out.printf("%-12s HP: %3d/%3d  Mana: %3d/%3d\n", 
                c.getName(), c.getHp(), c.getMaxHp(), c.getMana(), c.getMaxMana());
        }

        System.out.println("\n--- Enemies ---");
        for (Character e : enemies) {
            System.out.printf("%-15s HP: %3d\n", e.getName(), e.getHp());
        }
    }

    private static int getValidInput(Scanner sc, int min, int max) {
        while (true) {
            try {
                int input = sc.nextInt();
                if (input >= min && input <= max) return input;
                System.out.print("Please enter a number between " + min + " and " + max + ": ");
            } catch (Exception e) {
                System.out.print("Invalid input. Enter a number: ");
                sc.nextLine();
            }
        }
    }

    private static Character chooseTarget(Scanner sc, List<Character> targets, String type) {
        System.out.println("Choose " + type + " target:");
        int count = 0;
        for (int i = 0; i < targets.size(); i++) {
            Character c = targets.get(i);
            if (c.getHp() > 0) {
                System.out.println((count + 1) + ". " + c.getName() + " (" + c.getHp() + " HP)");
                count++;
            }
        }
        if (count == 0) return null;

        int choice = getValidInput(sc, 1, count);
        return targets.stream()
                      .filter(c -> c.getHp() > 0)
                      .skip(choice - 1)
                      .findFirst()
                      .orElse(null);
    }

    private static String getTargetType(GuItem gu) {
        String name = gu.getName().toLowerCase();
        String effect = gu.getEffectType().toLowerCase();

        if (effect.equals("shield") || effect.equals("buff") || effect.equals("heal")) {
            return "ally";
        }
        if (name.contains("imbue") || name.contains("shield") || name.contains("buff")) {
            return "ally";
        }
        return "enemy";
    }

    private static Character getRandomAlive(List<Character> list) {
        List<Character> alive = list.stream().filter(c -> c.getHp() > 0).toList();
        if (alive.isEmpty()) return null;
        return alive.get((int) (Math.random() * alive.size()));
    }

    private static boolean attemptFlee(Scanner sc, List<Character> party) {
        System.out.println("Attempting to flee... (30% success)");
        if (Math.random() < 0.30) {
            return true;
        } else {
            System.out.println("Flee failed! Backlash damage to the party!");
            for (Character member : party) {
                if (member.getHp() > 0) {
                    member.setHp(member.getHp() - 35);
                    System.out.println(member.getName() + " took 35 backlash damage!");
                }
            }
            return false;
        }
    }
}
*/