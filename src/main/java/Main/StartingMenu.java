/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Administrator
 */

public class StartingMenu extends javax.swing.JFrame {
    private static final int ICON_WIDTH = 100;
    private static final int ICON_HEIGHT = 100;
    private int p1ReadyAttack = 0;
    private int p2ReadyAttack = 0;
    private int p3ReadyAttack = 0;
    
    private String p1SelectedSkillName = ""; 
    private String p2SelectedSkillName = ""; 
    private String p3SelectedSkillName = ""; 
// Stores the index of the enemy targeted (e.g., 1, 2, or 3)
    private int p1SelectedTargetIndex = -1;
    private int p2SelectedTargetIndex = -1;
    private int p3SelectedTargetIndex = -1;
    private String[] enemyAssignedSkins = {"", "", ""};



    
    
    private static final int ARENAICON_WIDTH = 170;
    private static final int ARENAICON_HEIGHT = 190;
    private BattleManager battleMgr = new BattleManager();
    // Changing this to a String tracker allows us to track screens cleanly
    // without conflicting layout constraints.
    public String currentScreen = "MENU"; 
    
    private final java.util.List<String> chosenPaths = new java.util.ArrayList<>();
    private static final int MAX_CHOICES = 3;

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(StartingMenu.class.getName());

    // IMAGE TRACKERS
    private Image backgroundGif;
    private Image[] settingsLayers = new Image[9];
    private Image newArenaBackground;
    
    //KILLERMOVE
    private int killerMoveCount = 0;
    

    /**
     * Creates new form StartingMenu
     */
    public StartingMenu() {
        // 1. Load your original menu animation asset
        backgroundGif = new ImageIcon(getClass().getResource("/Animation.gif")).getImage();


        // 2. Load all 9 file sheets into memory following your precise naming schema
        for (int i = 0; i < 9; i++) {
            java.net.URL imgURL = getClass().getResource("/" + i + "BG.png");
            if (imgURL != null) {
                settingsLayers[i] = new ImageIcon(imgURL).getImage();
            } else {
                System.out.println("Warning: Missing /" + i + "BG.png");
            }
        }
        java.net.URL newPanelURL = getClass().getResource("/arena.png"); // <-- Change this to your filename
        if (newPanelURL != null) {
            newArenaBackground = new ImageIcon(newPanelURL).getImage();
        } else {
            System.out.println("Warning: Missing /arena.png");
        }

        // 3. Replace content pane BEFORE initComponents using your exact background style
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // STATE 1: Draw Menu GIF Fullscreen
                if (currentScreen.equals("MENU") && backgroundGif != null) {
                    g2d.drawImage(backgroundGif, 0, 0, getWidth(), getHeight(), this);
                }

                // STATE 2: Flatten and Draw all 9 Background Layers Fullscreen
                else if (currentScreen.equals("SETTINGS")) {
                    for (Image layerImage : settingsLayers) {
                        if (layerImage != null) {
                            g2d.drawImage(layerImage, 0, 0, getWidth(), getHeight(), this);
                        }
                    }
                }

                // STATE 3: Draw Arena Background Fullscreen <-- ADD THIS BLOCK
                else if (currentScreen.equals("ARENA") && newArenaBackground != null) {
                    g2d.drawImage(newArenaBackground, 0, 0, getWidth(), getHeight(), this);
                }
            }
        });
        
        initComponents();
        // 4. Set panels up for the home menu view initially
        MenuPanel.setVisible(true);
        SettingsPanel.setVisible(false);
        ArenaPanel.setVisible(false);

        
        // 5. CRUCIAL: Make the panels clear like glass. This lets the main frame's
        // custom fullscreen graphics paint loop show through underneath your buttons!
        MenuPanel.setOpaque(false);
        SettingsPanel.setOpaque(false);
        ArenaPanel.setOpaque(false);
        
        player1Target1.setVisible(false);
        player1Target2.setVisible(false);
        player1Target3.setVisible(false);
        
        player2Target1.setVisible(false);
        player2Target2.setVisible(false);
        player2Target3.setVisible(false);
        
        player3Target1.setVisible(false);
        player3Target2.setVisible(false);
        player3Target3.setVisible(false);
        

    }
    
    
    
    
private void selectPath(String pathName) {
            // 1. Check if the player already picked 3 paths
            if (chosenPaths.size() >= MAX_CHOICES) {
                javax.swing.JOptionPane.showMessageDialog(this, "You can only choose up to 3 paths!");
                return;
            }

            // 2. Add the selection to our tracking list
            chosenPaths.add(pathName);

            // 3. Refresh the displayed image icons on your labels
            updatePathLabels();
        }
        private void updatePathLabels() {
            // Array grouping your labels so we can loop through them dynamically
            javax.swing.JLabel[] statusLabels = {p1Icon, p2Icon, p3Icon};

            for (int i = 0; i < statusLabels.length; i++) {
                if (i < chosenPaths.size()) {
                    // Get the string identifier (e.g., "fire")
                    String pathName = chosenPaths.get(i);

                    // Look for the image file
                    java.net.URL imgURL = getClass().getResource("/"+pathName+"Icon.png");

                    if (imgURL != null) {
                        // 1. Load the original raw image
                        ImageIcon originalIcon = new ImageIcon(imgURL);
                        java.awt.Image rawImage = originalIcon.getImage();

                        // 2. Scale the image to your exact fixed dimensions smoothly
                        java.awt.Image scaledImage = rawImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);

                        // 3. Put the scaled image back into a new ImageIcon and apply it
                        statusLabels[i].setIcon(new ImageIcon(scaledImage));
                    } else {
                        System.out.println("Missing asset file: /resources/" + pathName + ".png");
                    }
                } else {
                    // Clear out any remaining slots if no path is chosen yet
                    statusLabels[i].setIcon(null);
                }
            }
        }

        // The Redo Button Action Method
        private void resetPaths() {
            chosenPaths.clear(); // Empty out the choices tracker array
            updatePathLabels();  // Clears out the image view on all three icons
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        MenuPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        TitleLabel = new javax.swing.JLabel();
        SettingsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        p2Icon = new javax.swing.JLabel();
        p3Icon = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        p1Icon = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        ArenaPanel = new javax.swing.JPanel();
        p1Base = new javax.swing.JLabel();
        p2Base = new javax.swing.JLabel();
        p3Base = new javax.swing.JLabel();
        e3Base = new javax.swing.JLabel();
        e1Base = new javax.swing.JLabel();
        e2Base = new javax.swing.JLabel();
        ProjectilePath = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        player1Target3 = new javax.swing.JButton();
        player3Icon = new javax.swing.JLabel();
        player1Icon = new javax.swing.JLabel();
        player2Icon = new javax.swing.JLabel();
        player1Skill1 = new javax.swing.JButton();
        player1Skill2 = new javax.swing.JButton();
        player1Target1 = new javax.swing.JButton();
        player1Target2 = new javax.swing.JButton();
        player2Target3 = new javax.swing.JButton();
        player2Skill1 = new javax.swing.JButton();
        player2Skill2 = new javax.swing.JButton();
        player2Target1 = new javax.swing.JButton();
        player2Target2 = new javax.swing.JButton();
        player3Target3 = new javax.swing.JButton();
        player3Target1 = new javax.swing.JButton();
        player3Target2 = new javax.swing.JButton();
        player3Skill1 = new javax.swing.JButton();
        AttackButton = new javax.swing.JButton();
        player3Skill2 = new javax.swing.JButton();
        e3HpBar = new javax.swing.JProgressBar();
        p1HpBar = new javax.swing.JProgressBar();
        p2HpBar = new javax.swing.JProgressBar();
        p3HpBar = new javax.swing.JProgressBar();
        e1HpBar = new javax.swing.JProgressBar();
        e2HpBar = new javax.swing.JProgressBar();
        gameStateLabel = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        MenuPanel.setOpaque(false);
        MenuPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        playButton.setBackground(java.awt.Color.black);
        playButton.setForeground(java.awt.Color.white);
        playButton.setText("PLAY");
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playButtonMouseExited(evt);
            }
        });
        playButton.addActionListener(this::playButtonActionPerformed);
        MenuPanel.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 100, 60));

        TitleLabel.setFont(new java.awt.Font("Viner Hand ITC", 2, 48)); // NOI18N
        TitleLabel.setForeground(new java.awt.Color(255, 0, 0));
        TitleLabel.setText("REVEREND INSANITY");
        MenuPanel.add(TitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        getContentPane().add(MenuPanel, new java.awt.GridBagConstraints());

        SettingsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/FirstCharMenu.png"))); // NOI18N
        SettingsPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 290, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/2ndCharMenu.png"))); // NOI18N
        SettingsPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 290, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/3rdCharMenu.png"))); // NOI18N
        SettingsPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 300, -1, -1));

        jLabel1.setFont(new java.awt.Font("Niagara Solid", 0, 60)); // NOI18N
        jLabel1.setText("CHOOSE CHARACTER PATH (Class)");
        SettingsPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, 1225, -1));

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton2.setText("Fire Path");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        SettingsPanel.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton3.setText("Water Path");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        SettingsPanel.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 110, 161, -1));

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton4.setText("Wood Path");
        jButton4.addActionListener(this::jButton4ActionPerformed);
        SettingsPanel.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 110, -1, -1));

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton5.setText("Earth Path");
        jButton5.addActionListener(this::jButton5ActionPerformed);
        SettingsPanel.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, -1, -1));

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton6.setText("Illusion Path");
        jButton6.addActionListener(this::jButton6ActionPerformed);
        SettingsPanel.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 110, -1, -1));

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton7.setText("Dark Path");
        jButton7.addActionListener(this::jButton7ActionPerformed);
        SettingsPanel.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, -1, -1));

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton8.setText("Wild Path");
        jButton8.addActionListener(this::jButton8ActionPerformed);
        SettingsPanel.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 110, -1, -1));

        p2Icon.setMaximumSize(new java.awt.Dimension(100, 100));
        p2Icon.setMinimumSize(new java.awt.Dimension(100, 100));
        p2Icon.setPreferredSize(new java.awt.Dimension(100, 100));
        SettingsPanel.add(p2Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 170, -1, -1));

        p3Icon.setMaximumSize(new java.awt.Dimension(100, 100));
        p3Icon.setMinimumSize(new java.awt.Dimension(100, 100));
        p3Icon.setPreferredSize(new java.awt.Dimension(100, 100));
        SettingsPanel.add(p3Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 170, -1, -1));

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButton9.setText("Redo");
        jButton9.addActionListener(this::jButton9ActionPerformed);
        SettingsPanel.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 127, -1));

        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p1Icon.setMaximumSize(new java.awt.Dimension(100, 100));
        p1Icon.setMinimumSize(new java.awt.Dimension(100, 100));
        p1Icon.setPreferredSize(new java.awt.Dimension(100, 100));
        jLayeredPane1.add(p1Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        SettingsPanel.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 105, -1));

        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton10.setText("Proceed to Fight");
        jButton10.addActionListener(this::jButton10ActionPerformed);
        SettingsPanel.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1450, 640, -1, -1));

        getContentPane().add(SettingsPanel, new java.awt.GridBagConstraints());

        ArenaPanel.setEnabled(false);
        ArenaPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p1Base.setText("p1");
        ArenaPanel.add(p1Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 740, -1, -1));

        p2Base.setText("p2");
        ArenaPanel.add(p2Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 810, -1, -1));

        p3Base.setText("p3");
        ArenaPanel.add(p3Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 680, -1, -1));

        e3Base.setText("e3");
        ArenaPanel.add(e3Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(1710, 750, -1, -1));

        e1Base.setText("e1");
        ArenaPanel.add(e1Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(1360, 770, -1, -1));

        e2Base.setText("e2");
        ArenaPanel.add(e2Base, new org.netbeans.lib.awtextra.AbsoluteConstraints(1580, 710, -1, -1));

        ProjectilePath.setText("ProjectilePath");
        ArenaPanel.add(ProjectilePath, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 850, -1, -1));

        jPanel1.setBackground(new java.awt.Color(76, 76, 76));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        ArenaPanel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 820, 2080, 200));

        player1Target3.setText("target3");
        player1Target3.addActionListener(this::player1Target3ActionPerformed);
        ArenaPanel.add(player1Target3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 90, 30));

        player3Icon.setText("jLabel5");
        ArenaPanel.add(player3Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 140, 110));

        player1Icon.setText("jLabel5");
        ArenaPanel.add(player1Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, 130));

        player2Icon.setText("jLabel5");
        ArenaPanel.add(player2Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 150, 100));

        player1Skill1.setText("skill1");
        player1Skill1.addActionListener(this::player1Skill1ActionPerformed);
        ArenaPanel.add(player1Skill1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 90, 30));

        player1Skill2.setText("skill2");
        player1Skill2.addActionListener(this::player1Skill2ActionPerformed);
        ArenaPanel.add(player1Skill2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 90, 30));

        player1Target1.setText("target1");
        player1Target1.addActionListener(this::player1Target1ActionPerformed);
        ArenaPanel.add(player1Target1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 90, 30));

        player1Target2.setText("target2");
        player1Target2.addActionListener(this::player1Target2ActionPerformed);
        ArenaPanel.add(player1Target2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 90, 30));

        player2Target3.setText("target3");
        player2Target3.addActionListener(this::player2Target3ActionPerformed);
        ArenaPanel.add(player2Target3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, 90, 30));

        player2Skill1.setText("skill1");
        player2Skill1.addActionListener(this::player2Skill1ActionPerformed);
        ArenaPanel.add(player2Skill1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 90, 30));

        player2Skill2.setText("skill2");
        player2Skill2.addActionListener(this::player2Skill2ActionPerformed);
        ArenaPanel.add(player2Skill2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 90, 30));

        player2Target1.setText("target1");
        player2Target1.addActionListener(this::player2Target1ActionPerformed);
        ArenaPanel.add(player2Target1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, 90, 30));

        player2Target2.setText("target2");
        player2Target2.addActionListener(this::player2Target2ActionPerformed);
        ArenaPanel.add(player2Target2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, 90, 30));

        player3Target3.setText("target3");
        player3Target3.addActionListener(this::player3Target3ActionPerformed);
        ArenaPanel.add(player3Target3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 480, 90, 30));

        player3Target1.setText("target1");
        player3Target1.addActionListener(this::player3Target1ActionPerformed);
        ArenaPanel.add(player3Target1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 360, 90, 30));

        player3Target2.setText("target2");
        player3Target2.addActionListener(this::player3Target2ActionPerformed);
        ArenaPanel.add(player3Target2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 420, 90, 30));

        player3Skill1.setText("skill1");
        player3Skill1.addActionListener(this::player3Skill1ActionPerformed);
        ArenaPanel.add(player3Skill1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 390, 90, 30));

        AttackButton.setFont(new java.awt.Font("Segoe UI", 3, 30)); // NOI18N
        AttackButton.setText("Launch Attack");
        AttackButton.addActionListener(this::AttackButtonActionPerformed);
        ArenaPanel.add(AttackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 660, 260, 90));

        player3Skill2.setText("skill2");
        player3Skill2.addActionListener(this::player3Skill2ActionPerformed);
        ArenaPanel.add(player3Skill2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 90, 30));

        e3HpBar.setBackground(new java.awt.Color(255, 0, 0));
        e3HpBar.setStringPainted(true);
        ArenaPanel.add(e3HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1750, 740, -1, 20));

        p1HpBar.setBackground(new java.awt.Color(0, 209, 0));
        p1HpBar.setStringPainted(true);
        ArenaPanel.add(p1HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 720, -1, 20));

        p2HpBar.setBackground(new java.awt.Color(0, 209, 0));
        p2HpBar.setStringPainted(true);
        ArenaPanel.add(p2HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 790, -1, 20));

        p3HpBar.setBackground(new java.awt.Color(0, 209, 0));
        p3HpBar.setStringPainted(true);
        ArenaPanel.add(p3HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 660, -1, 20));

        e1HpBar.setBackground(new java.awt.Color(255, 0, 0));
        e1HpBar.setStringPainted(true);
        ArenaPanel.add(e1HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 750, -1, 20));

        e2HpBar.setBackground(new java.awt.Color(255, 0, 0));
        e2HpBar.setStringPainted(true);
        ArenaPanel.add(e2HpBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1590, 690, -1, 20));

        gameStateLabel.setFont(new java.awt.Font("Segoe UI Emoji", 1, 70)); // NOI18N
        ArenaPanel.add(gameStateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 150, 1320, 120));

        getContentPane().add(ArenaPanel, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
            playButton.setBackground(Color.RED);
            playButton.setForeground(Color.BLACK);
            
            // Layout Shift Control: Update screen tracking variables cleanly
            currentScreen = "SETTINGS"; 
            MenuPanel.setVisible(false);
            SettingsPanel.setVisible(true);
            
            // Instantly repaints everything across the entire window view bounds
            this.repaint();
    }//GEN-LAST:event_playButtonActionPerformed

    private void playButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseEntered
    playButton.setBackground(new Color(255,215,0));

    playButton.setForeground(Color.BLACK);        // TODO add your handling code here:
    }//GEN-LAST:event_playButtonMouseEntered

    private void playButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playButtonMouseExited
        playButton.setBackground(Color.BLACK);

    playButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_playButtonMouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectPath("fire");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        selectPath("water");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        selectPath("wood");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        selectPath("earth");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        selectPath("illusion");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        selectPath("dark");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        selectPath("wild");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        resetPaths();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // 1. Double check they have picked their 3 characters
            if (chosenPaths.size() < 3) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select all 3 paths before starting!");
                return;
            }

            // 2. Turn selection strings into your exact subclass objects dynamically!
            java.util.List<Character> matchParty = new java.util.ArrayList<>();
            matchParty.add(createCharacterFromPath("Hero 1", chosenPaths.get(0)));
            matchParty.add(createCharacterFromPath("Hero 2", chosenPaths.get(1)));
            matchParty.add(createCharacterFromPath("Hero 3", chosenPaths.get(2)));

            // 3. Hand your fully initialized squad over to the Battle Manager backend
            battleMgr.setupParty(matchParty);

            // 4. Update the portrait interfaces using your naming convention
            setStaticCircleFace(player1Icon, "FirstCharMenu", 100, 120); 
            setStaticCircleFace(player2Icon, "2ndCharMenu", 100, 120); 
            setStaticCircleFace(player3Icon, "3rdCharMenu", 100, 120); 

            // 5. Place characters on their starting battle bases
            updateCombatSprite(p1Base, "1stChar", "");
            updateCombatSprite(p2Base, "2ndChar", "");
            updateCombatSprite(p3Base, "3rdChar", "");

            // Set up monster labels
            updateCombatSprite(e1Base, "e1", "");
            updateCombatSprite(e2Base, "e2", "");
            updateCombatSprite(e3Base, "e3", "");
            
            // 6. Flip panels to the combat arena view
            currentScreen = "ARENA";
            MenuPanel.setVisible(false);
            SettingsPanel.setVisible(false);
            ArenaPanel.setVisible(true);

            // Hide target selectors until a skill is explicitly activated
            player1Target1.setVisible(false);
            player1Target2.setVisible(false);
            player1Target3.setVisible(false);

            this.revalidate();
            this.repaint();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void player1Skill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1Skill1ActionPerformed
        
    Character p1Character = battleMgr.getPlayerInSlot(0); 
        if (p1Character == null) return;
 

        
        // 1. Get the actual skill object from your subclass polymorphic list
        // (Assuming Skill 1 corresponds to index 0 in your subclass list)
        java.util.List<GuItem> availableSkills = p1Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {
            GuItem chosenSkill = availableSkills.get(0);

            // 2. Temporarily record the chosen skill name
            p1SelectedSkillName = chosenSkill.getName();
            player1Skill1.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p1SelectedSkillName);
        }

        // 3. Reveal the target selection options to the user
        if (p1ReadyAttack < 2) {
            player1Target1.setVisible(true);
            player1Target2.setVisible(true);
            player1Target3.setVisible(true);
        }


    }//GEN-LAST:event_player1Skill1ActionPerformed

    private void player2Skill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2Skill1ActionPerformed
    Character p2Character = battleMgr.getPlayerInSlot(1); 
        if (p2Character == null) return;

        java.util.List<GuItem> availableSkills = p2Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {
            GuItem chosenSkill = availableSkills.get(0);

            // 2. Temporarily record the chosen skill name
            p2SelectedSkillName = chosenSkill.getName();
            player2Skill1.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p2SelectedSkillName);
        }

        // 3. Reveal the target selection options to the user
        if (p2ReadyAttack < 2) {
            player2Target1.setVisible(true);
            player2Target2.setVisible(true);
            player2Target3.setVisible(true);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_player2Skill1ActionPerformed

    private void player3Skill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3Skill1ActionPerformed
    Character p3Character = battleMgr.getPlayerInSlot(2); 
        if (p3Character == null) return;

        java.util.List<GuItem> availableSkills = p3Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {
            GuItem chosenSkill = availableSkills.get(0);

            // 2. Temporarily record the chosen skill name
            p3SelectedSkillName = chosenSkill.getName();
            player3Skill1.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p3SelectedSkillName);
        }

        // 3. Reveal the target selection options to the user
        if (p3ReadyAttack < 2) {
            player3Target1.setVisible(true);
            player3Target2.setVisible(true);
            player3Target3.setVisible(true);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_player3Skill1ActionPerformed

    private void player1Skill2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1Skill2ActionPerformed
    Character p1Character = battleMgr.getPlayerInSlot(0); 
        if (p1Character == null) return;

        player1Skill2.setBackground(Color.BLACK);
        player1Skill2.setForeground(Color.WHITE);

        
        java.util.List<GuItem> availableSkills = p1Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {

            GuItem chosenSkill = availableSkills.get(1);
            


            p1SelectedSkillName = chosenSkill.getName();
            player1Skill2.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p1SelectedSkillName);
        }

        if (p1ReadyAttack < 2) {
            player1Target1.setVisible(true);
            player1Target2.setVisible(true);
            player1Target3.setVisible(true);
        }

        
        

    }//GEN-LAST:event_player1Skill2ActionPerformed

    private void player1Target1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1Target1ActionPerformed
    if (p1ReadyAttack < 1) {
        killerMoveCount++;
            p1ReadyAttack++;
            System.out.println("Ready Attack count: " + p1ReadyAttack);
            p1SelectedTargetIndex = 1; 
            player1Target1.setBackground(Color.GRAY);
            player1Target1.setForeground(Color.WHITE);

            player1Target1.setVisible(true);
            player1Target2.setVisible(false);
            player1Target3.setVisible(false);
        }
        else if (p1ReadyAttack == 1) {
            killerMoveCount++;
            p1SelectedTargetIndex = 1; 

            player1Target1.setBackground(Color.BLACK);
            player1Target1.setForeground(Color.WHITE);

            player1Target1.setVisible(true);
            player1Target2.setVisible(false);
            player1Target3.setVisible(false);

            this.repaint(); 
        }

    }//GEN-LAST:event_player1Target1ActionPerformed

    private void player1Target2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1Target2ActionPerformed
    if (p1ReadyAttack < 1) {
        killerMoveCount++;
        p1SelectedTargetIndex = 2; 
            p1ReadyAttack++;
            System.out.println("Ready Attack count: " + p1ReadyAttack);

            player1Target2.setBackground(Color.GRAY);
            player1Target2.setForeground(Color.WHITE);

            player1Target1.setVisible(false);
            player1Target2.setVisible(true);
            player1Target3.setVisible(false);
        }
        else if (p1ReadyAttack == 1) {
            killerMoveCount++;
            p1SelectedTargetIndex = 2; 

            player1Target2.setBackground(Color.BLACK);
            player1Target2.setForeground(Color.WHITE);

            player1Target1.setVisible(false);
            player1Target2.setVisible(true);
            player1Target3.setVisible(false);

            this.repaint(); 
        }
    }//GEN-LAST:event_player1Target2ActionPerformed

    private void player1Target3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player1Target3ActionPerformed
    if (p1ReadyAttack < 1) {
        killerMoveCount++;
        p1SelectedTargetIndex = 3; 
            p1ReadyAttack++;
            System.out.println("Ready Attack count: " + p1ReadyAttack);

            player1Target3.setBackground(Color.GRAY);
            player1Target3.setForeground(Color.WHITE);

            player1Target1.setVisible(false);
            player1Target2.setVisible(false);
            player1Target3.setVisible(true);
        }
        else if (p1ReadyAttack == 1) {
            killerMoveCount++;
            p1SelectedTargetIndex = 3; 

            player1Target3.setBackground(Color.BLACK);
            player1Target3.setForeground(Color.WHITE);

            player1Target1.setVisible(false);
            player1Target2.setVisible(false);
            player1Target3.setVisible(true);

            this.repaint(); 
        }
    }//GEN-LAST:event_player1Target3ActionPerformed

    private void player2Target1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2Target1ActionPerformed
    if (p2ReadyAttack < 1) {
        killerMoveCount++;
        p2SelectedTargetIndex = 1; 
            p2ReadyAttack++;
            System.out.println("Ready Attack count: " + p2ReadyAttack);

            player2Target1.setBackground(Color.GRAY);
            player2Target1.setForeground(Color.WHITE);

            player2Target1.setVisible(true);
            player2Target2.setVisible(false);
            player2Target3.setVisible(false);
        }
    else if (p2ReadyAttack == 1) {
            killerMoveCount++;
            p2SelectedTargetIndex = 1; 

            player2Target1.setBackground(Color.BLACK);
            player2Target1.setForeground(Color.WHITE);

            player2Target1.setVisible(true);
            player2Target2.setVisible(false);
            player2Target3.setVisible(false);

            this.repaint(); 
        }
    }//GEN-LAST:event_player2Target1ActionPerformed

    private void player2Skill2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2Skill2ActionPerformed
    Character p2Character = battleMgr.getPlayerInSlot(1); 
        if (p2Character == null) return;

        java.util.List<GuItem> availableSkills = p2Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {
            GuItem chosenSkill = availableSkills.get(1);

            // 2. Temporarily record the chosen skill name
            p2SelectedSkillName = chosenSkill.getName();
            player2Skill2.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p2SelectedSkillName);
        }

        // 3. Reveal the target selection options to the user
        if (p2ReadyAttack < 2) {
            player2Target1.setVisible(true);
            player2Target2.setVisible(true);
            player2Target3.setVisible(true);
        }         // TODO add your handling code here:
    }//GEN-LAST:event_player2Skill2ActionPerformed

    private void player3Skill2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3Skill2ActionPerformed
    Character p3Character = battleMgr.getPlayerInSlot(2); 
        if (p3Character == null) return;


        java.util.List<GuItem> availableSkills = p3Character.getAvailableSkills();
        if (availableSkills != null && !availableSkills.isEmpty()) {
            GuItem chosenSkill = availableSkills.get(1);

            // 2. Temporarily record the chosen skill name
            p3SelectedSkillName = chosenSkill.getName();
            player3Skill2.setText(chosenSkill.getName());
            System.out.println("Recorded Selection: Player 1 prepares " + p3SelectedSkillName);
        }

        // 3. Reveal the target selection options to the user
        if (p3ReadyAttack < 2) {
            player3Target1.setVisible(true);
            player3Target2.setVisible(true);
            player3Target3.setVisible(true);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_player3Skill2ActionPerformed

    private void player2Target2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2Target2ActionPerformed
    if (p2ReadyAttack < 1) {
        killerMoveCount++;
        p2SelectedTargetIndex = 2; 
            p2ReadyAttack++;
            System.out.println("Ready Attack count: " + p2ReadyAttack);

            player2Target2.setBackground(Color.GRAY);
            player2Target2.setForeground(Color.WHITE);

            player2Target1.setVisible(false);
            player2Target2.setVisible(true);
            player2Target3.setVisible(false);
        }
    else if (p2ReadyAttack == 1) {
            killerMoveCount++;
            p2SelectedTargetIndex = 2; 

            player2Target2.setBackground(Color.BLACK);
            player2Target2.setForeground(Color.WHITE);

            player2Target1.setVisible(false);
            player2Target2.setVisible(true);
            player2Target3.setVisible(false);

            this.repaint(); 
        }
    }//GEN-LAST:event_player2Target2ActionPerformed

    private void player2Target3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player2Target3ActionPerformed
    if (p2ReadyAttack < 1) {
        killerMoveCount++;
        p2SelectedTargetIndex = 3; 
            p2ReadyAttack++;
            System.out.println("Ready Attack count: " + p2ReadyAttack);

            player2Target3.setBackground(Color.GRAY);
            player2Target3.setForeground(Color.WHITE);

            player2Target1.setVisible(false);
            player2Target2.setVisible(false);
            player2Target3.setVisible(true);
        }
    else if (p2ReadyAttack == 1) {
            killerMoveCount++;
            p2SelectedTargetIndex = 3; 

            player2Target3.setBackground(Color.BLACK);
            player2Target3.setForeground(Color.WHITE);

            player2Target1.setVisible(false);
            player2Target2.setVisible(false);
            player2Target3.setVisible(true);

            this.repaint(); 
        }        // TODO add your handling code here:
    }//GEN-LAST:event_player2Target3ActionPerformed

    private void player3Target1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3Target1ActionPerformed
    if (p3ReadyAttack < 1) {
        killerMoveCount++;
        p3SelectedTargetIndex = 1; 
            p3ReadyAttack++;
            System.out.println("Ready Attack count: " + p3ReadyAttack);

            player3Target1.setBackground(Color.GRAY);
            player3Target1.setForeground(Color.WHITE);

            player3Target1.setVisible(true);
            player3Target2.setVisible(false);
            player3Target3.setVisible(false);
        }
    else if (p3ReadyAttack == 1) {
            killerMoveCount++;
            p3SelectedTargetIndex = 1; 

            player3Target1.setBackground(Color.BLACK);
            player3Target1.setForeground(Color.WHITE);

            player3Target1.setVisible(true);
            player3Target2.setVisible(false);
            player3Target3.setVisible(false);

            this.repaint(); 
        }        // TODO add your handling code here:
    }//GEN-LAST:event_player3Target1ActionPerformed

    private void player3Target2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3Target2ActionPerformed
    if (p3ReadyAttack < 1) {
        killerMoveCount++;
        p3SelectedTargetIndex = 2; 
            p3ReadyAttack++;
            System.out.println("Ready Attack count: " + p3ReadyAttack);

            player3Target2.setBackground(Color.GRAY);
            player3Target2.setForeground(Color.WHITE);

            player3Target1.setVisible(false);
            player3Target2.setVisible(true);
            player3Target3.setVisible(false);
        }
    else if (p3ReadyAttack == 1) {
            killerMoveCount++;
            p3SelectedTargetIndex = 2; 

            player3Target2.setBackground(Color.BLACK);
            player3Target2.setForeground(Color.WHITE);

            player3Target1.setVisible(false);
            player3Target2.setVisible(true);
            player3Target3.setVisible(false);

            this.repaint(); 
        }         // TODO add your handling code here:
    }//GEN-LAST:event_player3Target2ActionPerformed

    private void player3Target3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_player3Target3ActionPerformed
    if (p3ReadyAttack < 1) {
        killerMoveCount++;
        p3SelectedTargetIndex = 3; 
            p3ReadyAttack++;
            System.out.println("Ready Attack count: " + p3ReadyAttack);

            player3Target3.setBackground(Color.GRAY);
            player3Target3.setForeground(Color.WHITE);

            player3Target1.setVisible(false);
            player3Target2.setVisible(false);
            player3Target3.setVisible(true);
        }
    else if (p3ReadyAttack == 1) {
            killerMoveCount++;
            p3SelectedTargetIndex = 3; 

            player3Target3.setBackground(Color.BLACK);
            player3Target3.setForeground(Color.WHITE);

            player3Target1.setVisible(false);
            player3Target2.setVisible(false);
            player3Target3.setVisible(true);

            this.repaint(); 
        }         // TODO add your handling code here:
    }//GEN-LAST:event_player3Target3ActionPerformed

    private void AttackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AttackButtonActionPerformed
// 1. Validation check arrays
JButton[][] playerTarget = {
    {player1Target1, player1Target2, player1Target3},
    {player2Target1, player2Target2, player2Target3},
    {player3Target1, player3Target2, player3Target3}
};

JButton[][] playerSkill = {
    {player1Skill1, player1Skill2},
    {player2Skill1, player2Skill2},
    {player3Skill1, player3Skill2},
};

String[] selectedSkillName = {
    p1SelectedSkillName,
    p2SelectedSkillName,
    p3SelectedSkillName
};

int[] selectedTargetIndex = {
    p1SelectedTargetIndex,
    p2SelectedTargetIndex,
    p3SelectedTargetIndex
};

JLabel[] playerBases = {
    p1Base,
    p2Base,
    p3Base
};

String[] animNames = {
    "1stChar",
    "2ndChar",
    "3rdChar"
};



System.out.println("--- PLAYER TURN PROCESSING START ---");

boolean actionExecuted = false;

for (Character player : battleMgr.getLivingPlayers()) {
    player.processEndOfTurnEffects(); // Processes shields & wood/water buffs
}

battleMgr.processGlobalEffects();
refreshAllBattleUi();
// === STEP 1: CALCULATE BACKEND DAMAGE & TRIGGERS FOR LIVING PLAYERS ===
for (int p = 0; p < 3; p++) {
    if (selectedSkillName[p].isEmpty()) {
        continue;
    }

    Character attacker = battleMgr.getPlayerInSlot(p);
    
    // GUARD: Skip dead characters entirely
    if (attacker == null || attacker.getHp() <= 0) {
        System.out.println(animNames[p] + " is dead! Skipping action execution.");
        continue; 
    }

    // Find the physical GuItem object to inspect its Effect Type metadata
    GuItem skillToUse = null;
    for (GuItem gu : attacker.getAvailableSkills()) {
        if (gu.getName().equalsIgnoreCase(selectedSkillName[p])) {
            skillToUse = gu;
            break;
        }
    }

    if (skillToUse != null) {
        Character targetCharacter = null;
        String type = skillToUse.getEffectType().toLowerCase();
        int targetSlotIndex = selectedTargetIndex[p] - 1;

        // DATA-DRIVEN TARGET ROUTING
        if (type.equals("buff") || type.equals("heal") || type.equals("shield")) {
            // Supportive spells dynamically map back onto the caster
            targetCharacter = battleMgr.getPlayerInSlot(targetSlotIndex);
        } else {
            // Offensive spells require an enemy index array coordinate mapping
            if (selectedTargetIndex[p] != -1) {
                targetCharacter = battleMgr.getEnemyInSlot(selectedTargetIndex[p] - 1);
            }
        }

        // EXECUTE BACKEND MECHANICS IF TARGET VALIDATED
        if (targetCharacter != null) {
            actionExecuted = true;
            
            // Set attacking frame posture
            updateCombatSprite(playerBases[p], animNames[p], "2");

            // Process backend calculations (damage, mastery points, or buff states)
            attacker.useSkill(skillToUse, targetCharacter, battleMgr);
            
            System.out.println(attacker.getName() + " successfully casted " + skillToUse.getName());

            // Run your animation layer project channel (Unflipped player direction)
            playSkillAnimation(ProjectilePath, selectedSkillName[p], 3000);
        }
    }
}
for (Character player : battleMgr.getLivingPlayers()) {
            if (player instanceof WildPath) {
                ((WildPath) player).resetGuUses(); 
            }
        }

// === STEP 2: RUN SINGLE TIMELINE CLEANUP & ENEMY RETALIATION ===
if (actionExecuted) {
    // Single master timeline tracker loop (Only runs ONCE per team phase click)
    javax.swing.Timer sequenceTimer = new javax.swing.Timer(3000, e -> {
        
        // Return living players to idle postures safely
        for (int p = 0; p < 3; p++) {
            Character player = battleMgr.getPlayerInSlot(p);
            if (player != null && player.getHp() > 0) {
                updateCombatSprite(playerBases[p], animNames[p], "");
            }
        }

        // Wipe input variables clean for the next turn
        p1SelectedSkillName = ""; p2SelectedSkillName = ""; p3SelectedSkillName = "";
        p1SelectedTargetIndex = -1; p2SelectedTargetIndex = -1; p3SelectedTargetIndex = -1;
        p1ReadyAttack = 0; p2ReadyAttack = 0; p3ReadyAttack = 0;

        // Hide target choice buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playerTarget[i][j].setVisible(false);
            }
        }

        // === STEP 3: ENEMY RETALIATION PHASE (NO EXTRA TIMER) ===
        System.out.println("--- ENEMY TURN START ---");
        
        // Loop through all 3 enemy slots instantly
        for (int i = 0; i < 3; i++) {
            Character activeEnemy = battleMgr.getEnemyInSlot(i);

            // GUARD: Only allow living enemies to strike
            if (activeEnemy != null && activeEnemy.getHp() > 0) {
                String skillUsed = battleMgr.executeEnemyAiTurn(activeEnemy);

                if (!skillUsed.equals("") && !skillUsed.equals("basic")) {
                    System.out.println(activeEnemy.getName() + " triggering visual: " + skillUsed);
                    // Pass true so the projectile plays horizontally flipped!
                    playSkillAnimation(ProjectilePath, skillUsed, 2000);
                }
            }
        }

        // Final evaluation checklist pass (Triggers wave updates/win screens)
        refreshAllBattleUi();
        if (getContentPane() != null) {
            getContentPane().repaint();
        }
    });

    // Clear and drop UI button focus configurations
    for (int i = 0; i < 3; i++) {
        Character turnCharacter = battleMgr.getPlayerInSlot(i);
        java.util.List<GuItem> nextSkills = (turnCharacter != null) ? turnCharacter.getAvailableSkills() : null;
        for (int j = 0; j < 3; j++) {
            if(j < 2){
                String freshSkillName = nextSkills.get(j).getName();
                playerSkill[i][j].setText(freshSkillName);
                playerSkill[i][j].setBackground(Color.WHITE);
                playerSkill[i][j].setForeground(Color.BLACK);
            }

            playerTarget[i][j].setVisible(false);
            playerTarget[i][j].setBackground(Color.WHITE);
            playerTarget[i][j].setForeground(Color.BLACK);
        }
    }

    sequenceTimer.setRepeats(false);
    sequenceTimer.start();
}
        
        
        
    }//GEN-LAST:event_AttackButtonActionPerformed
           
public void updateCombatSprite(javax.swing.JLabel label, String position, String state) {
// 1. If dead, make the sprite vanish instantly
    if (state.equals("dead")) {
        label.setIcon(null);
        label.setText("");
        return;
    }

    // 2. ENEMY RANDOMIZATION & TRACKING INTERCEPT
    String finalPosition = position; 
    boolean isEnemy = false; 
    
    if (position.equalsIgnoreCase("e1") || position.equalsIgnoreCase("e2") || position.equalsIgnoreCase("e3")) {
        isEnemy = true; 
        int enemyIdx = position.equalsIgnoreCase("e1") ? 0 : position.equalsIgnoreCase("e2") ? 1 : 2;
        
        if (enemyAssignedSkins[enemyIdx].isEmpty()) {
            String[] availableSkins = { "1stChar", "2ndChar", "3rdChar" };
            java.util.Random rand = new java.util.Random();
            enemyAssignedSkins[enemyIdx] = availableSkins[rand.nextInt(availableSkins.length)];
            System.out.println(position + " randomly rolled skin: " + enemyAssignedSkins[enemyIdx]);
        }
        
        finalPosition = enemyAssignedSkins[enemyIdx];
    }

    // 3. Look for the asset file
    String fullPath = "/" + finalPosition + state + ".png";
    java.net.URL imgURL = getClass().getResource(fullPath);

    if (imgURL != null) {
        // Load the full-sized image instantly into memory
        ImageIcon originalIcon = new ImageIcon(imgURL);
        java.awt.Image rawImage = originalIcon.getImage();
        
        int srcWidth = rawImage.getWidth(label);
        int srcHeight = rawImage.getHeight(label);

        java.awt.image.BufferedImage finalImage = new java.awt.image.BufferedImage(
                ARENAICON_WIDTH, ARENAICON_HEIGHT, java.awt.image.BufferedImage.TYPE_INT_ARGB
        );
        
        java.awt.Graphics2D g2d = finalImage.createGraphics();
        

        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 4. COMBINED SCALE & FLIP ENGINE
        if (isEnemy) {
            g2d.drawImage(rawImage, 
                    0, 0, ARENAICON_WIDTH, ARENAICON_HEIGHT, // Target canvas box
                    srcWidth, 0, 0, srcHeight,               // Read source image backwards
                    label
            );
        } else {
            // Player: Draw normal scaling without flipping
            g2d.drawImage(rawImage, 
                    0, 0, ARENAICON_WIDTH, ARENAICON_HEIGHT, 
                    0, 0, srcWidth, srcHeight, 
                    label
            );
        }
        g2d.dispose(); 
        
        label.setIcon(new ImageIcon(finalImage));
        label.setText(""); 
    } else {
        System.out.println("Missing asset file: " + fullPath);
        label.setText(position + " [" + state + "]"); 
    }
}
        

    /**
     * Convienence wrapper to quickly initialize standard game start stances.
     */
    private void initializeArenaSprites() {
        // Set up player team based on selections made in your chosenPaths tracking array
        if (chosenPaths.size() > 0) updateCombatSprite(p1Base, chosenPaths.get(0), "Idle");
        if (chosenPaths.size() > 1) updateCombatSprite(p2Base, chosenPaths.get(1), "Idle");
        if (chosenPaths.size() > 2) updateCombatSprite(p3Base, chosenPaths.get(2), "Idle");

        // Set up opponent positions using your custom enemy asset names
        updateCombatSprite(e1Base, "Zombie", "Idle");
        updateCombatSprite(e2Base, "Skeleton", "Idle");
        updateCombatSprite(e3Base, "Boss", "Idle");
    }
    


public void playSkillAnimation(javax.swing.JLabel targetLabel, String gifName, int delayMs) {
    // 1. Find the GIF resource file
    String fullPath = "/" + gifName + ".gif";
    java.net.URL imgURL = getClass().getResource(fullPath);

    if (imgURL != null) {
        // Load the GIF directly. 
        // Note: We don't use getScaledInstance() here because scaling an animated GIF 
        // via that method breaks the animation frames in Java. 
        // Ensure your fireball.gif is already sized properly (like 100x100) in your folder!
        javax.swing.ImageIcon skillGif = new javax.swing.ImageIcon(imgURL);
        targetLabel.setIcon(skillGif);
        targetLabel.setText(""); // Clear placeholder text

        // Force the UI to refresh immediately to display the first frame
        targetLabel.revalidate();
        targetLabel.repaint();

        // 2. Set up a one-shot timer to remove the GIF after the delay runs out
        javax.swing.Timer deleteTimer = new javax.swing.Timer(delayMs, e -> {
            // This code runs automatically ONLY when the time runs out
            targetLabel.setIcon(null); 

            System.out.println("Skill animation finished and cleared.");
        });

        deleteTimer.setRepeats(false); // CRUCIAL: Tells the timer to only run ONCE and stop
        deleteTimer.start();           // Start ticking down

    } else {
        System.out.println("Error: Could not find skill asset at " + fullPath);
    }
}
    

public void updateHpBar(javax.swing.JProgressBar progressBar, Character character) {
    if (progressBar == null) return;

    // If the character is missing or dead, drop the bar to 0 cleanly
    if (character == null || character.getHp() <= 0) {
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setString("DEAD");
        progressBar.setForeground(java.awt.Color.GREEN);
        return;
    }

    // 1. Synchronize the progress bar min/max scales to match the character stats
    progressBar.setMinimum(0);
    progressBar.setMaximum(character.getMaxHp());
    progressBar.setValue(character.getHp());

    // 2. Set the custom text display inside the bar
    progressBar.setString(character.getHp() + " / " + character.getMaxHp() + " HP");

}


public void refreshAllBattleUi() {
    // 1. Group your progress bars and visual labels into matching slot arrays
    javax.swing.JProgressBar[] playerBars = { p1HpBar, p2HpBar, p3HpBar };
    javax.swing.JLabel[] playerBases = { p1Base, p2Base, p3Base };
    String[] playerIdentifiers = { "1stChar", "2ndChar", "3rdChar" };

    javax.swing.JProgressBar[] enemyBars  = { e1HpBar, e2HpBar, e3HpBar };
    javax.swing.JLabel[] enemyBases = { e1Base, e2Base, e3Base };
    String[] enemyIdentifiers = { "e1", "e2", "e3" };

    // Defeat condition check
    if (battleMgr.getLivingPlayers().isEmpty()) {
        endGameSequence(false); // Triggers the Lose screen state!
        return;
    }

    // 2. ALWAYS Refresh Player data & check alive states
    for (int i = 0; i < playerBars.length; i++) {
        Character p = battleMgr.getPlayerInSlot(i);
        updateHpBar(playerBars[i], p);
        
        if (p == null || p.getHp() <= 0) {
            updateCombatSprite(playerBases[i], playerIdentifiers[i], "dead");
        }
    }

    // 3. FIXED: ALWAYS Refresh Current Enemy UI status every turn, regardless of wave changes!
    for (int i = 0; i < enemyBars.length; i++) {
        Character e = battleMgr.getEnemyInSlot(i);
        updateHpBar(enemyBars[i], e);

        // If the enemy is dead, wipe their sprite off the field
        if (e == null || e.getHp() <= 0) {
            updateCombatSprite(enemyBases[i], enemyIdentifiers[i], "dead");
        }
    }

    // 4. Run Wave Spawning Evaluation Logic
    boolean waveChanged = battleMgr.checkAndSpawnNextWave();

    if (waveChanged) {
        if (battleMgr.isGameWon()) {
            endGameSequence(true); // Triggers the Win screen state!
        } else {
            System.out.println("New wave generated. Resetting visual skins...");

            // Clear skin history tags for the new spawn tier
            for (int i = 0; i < enemyAssignedSkins.length; i++) {
                enemyAssignedSkins[i] = ""; 
            }

            // Immediately force-initialize the newly spawned enemies' max properties
            for (int i = 0; i < 3; i++) {
                Character newEnemy = battleMgr.getEnemyInSlot(i);
                updateHpBar(enemyBars[i], newEnemy);
                updateCombatSprite(enemyBases[i], enemyIdentifiers[i], ""); 
            }
        }
    }
}

public void setStaticCircleFace(javax.swing.JLabel label, String filename, int circleDiameter, int imageSize) {
    java.net.URL imgURL = getClass().getResource("/" + filename + ".png");

    if (imgURL == null) {
        System.out.println("Warning: Could not find face asset at /" + filename + ".png");
        label.setText("[" + filename + "]"); 
        return;
    }

    try {
        java.awt.image.BufferedImage srcImg = javax.imageio.ImageIO.read(imgURL);

        // Canvas constraints setup
        java.awt.image.BufferedImage roundedCanvas = new java.awt.image.BufferedImage(
                circleDiameter, circleDiameter, java.awt.image.BufferedImage.TYPE_INT_ARGB
        );

        java.awt.Graphics2D g2d = roundedCanvas.createGraphics();
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Render mask boundary
        g2d.setColor(java.awt.Color.WHITE);
        g2d.fillOval(0, 0, circleDiameter, circleDiameter);
        g2d.setComposite(java.awt.AlphaComposite.SrcIn);

        // Center calculation profile
        int offset = (circleDiameter - imageSize) / 2;
        g2d.drawImage(srcImg, offset, offset, imageSize, imageSize, null);
        g2d.dispose();

        // Push assets cleanly directly onto target Swing frame element properties
        label.setText(""); 
        label.setIcon(new javax.swing.ImageIcon(roundedCanvas));
        
        label.setPreferredSize(new java.awt.Dimension(circleDiameter, circleDiameter));
        label.setSize(circleDiameter, circleDiameter);

    } catch (Exception e) {
        System.out.println("Error rendering circle portrait: " + e.getMessage());
    }
}
/**
 * Triggers the end-of-game sequence for wins and losses.
 * Shows the result, waits 10 seconds, and smoothly routes back to settings.
 */
public void endGameSequence(boolean isWin) {
    System.out.println("--- END GAME SEQUENCE TRIGGERED ---");
    
    // 1. Assuming you have a label to display status updates on your arena UI
    // (Replace 'statusLabel' with whatever your actual Jlabel variable name is)
    if (isWin) {
        gameStateLabel.setText("🎉 VICTORY! ALL WAVES CLEARED! 🎉");
        gameStateLabel.setForeground(java.awt.Color.GREEN);
    } else {
        gameStateLabel.setText("💀 GAME OVER! YOUR PARTY WAS WIPED OUT! 💀");
        gameStateLabel.setForeground(java.awt.Color.RED);
    }
    gameStateLabel.setVisible(true);

    // 2. Create a 10-second (10000 ms) delay timer to route back
    javax.swing.Timer returnTimer = new javax.swing.Timer(10000, e -> {
        // Switch your state management string back to SETTINGS
        currentScreen = "SETTINGS";
        
        // Hide your combat arena panels/elements so the settings screen components show up
        MenuPanel.setVisible(false);
        SettingsPanel.setVisible(true);
        ArenaPanel.setVisible(false);
        
        // Reinitialize a fresh BattleManager context so the game resets cleanly
        battleMgr = new BattleManager(); 
        
        // Force Java Swing to redraw the 9 settings layers instantly
        revalidate();
        repaint();
        
        System.out.println("Returned to Settings Panel and reset battle metrics.");
    });
    
    returnTimer.setRepeats(false);
    returnTimer.start();
}


private Character createCharacterFromPath(String heroName, String pathString) {
            switch(pathString.toLowerCase()) {
                case "fire":   return new FirePath(heroName);
                case "water":  return new WaterPath(heroName);
                case "earth":  return new EarthPath(heroName);
                case "wild":   return new WildPath(heroName);
                case "wood": return new WoodPath(heroName);
                case "dark":  return new DarkPath(heroName);
                case "illusion":   return new IllusionPath(heroName);
                default:       return new FirePath(heroName); // Fallback safety
            }
        }
    public static void main(String args[]) {

            try {
                for (javax.swing.UIManager.LookAndFeelInfo info :
                        javax.swing.UIManager.getInstalledLookAndFeels()) {

                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

            } catch (ReflectiveOperationException |
                     javax.swing.UnsupportedLookAndFeelException ex) {

                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }

            java.awt.EventQueue.invokeLater(() ->
                    new StartingMenu().setVisible(true));
        }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ArenaPanel;
    private javax.swing.JButton AttackButton;
    private javax.swing.JPanel MenuPanel;
    private javax.swing.JLabel ProjectilePath;
    private javax.swing.JPanel SettingsPanel;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JLabel e1Base;
    private javax.swing.JProgressBar e1HpBar;
    private javax.swing.JLabel e2Base;
    private javax.swing.JProgressBar e2HpBar;
    private javax.swing.JLabel e3Base;
    private javax.swing.JProgressBar e3HpBar;
    private javax.swing.JLabel gameStateLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel p1Base;
    private javax.swing.JProgressBar p1HpBar;
    private javax.swing.JLabel p1Icon;
    private javax.swing.JLabel p2Base;
    private javax.swing.JProgressBar p2HpBar;
    private javax.swing.JLabel p2Icon;
    private javax.swing.JLabel p3Base;
    private javax.swing.JProgressBar p3HpBar;
    private javax.swing.JLabel p3Icon;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel player1Icon;
    private javax.swing.JButton player1Skill1;
    private javax.swing.JButton player1Skill2;
    private javax.swing.JButton player1Target1;
    private javax.swing.JButton player1Target2;
    private javax.swing.JButton player1Target3;
    private javax.swing.JLabel player2Icon;
    private javax.swing.JButton player2Skill1;
    private javax.swing.JButton player2Skill2;
    private javax.swing.JButton player2Target1;
    private javax.swing.JButton player2Target2;
    private javax.swing.JButton player2Target3;
    private javax.swing.JLabel player3Icon;
    private javax.swing.JButton player3Skill1;
    private javax.swing.JButton player3Skill2;
    private javax.swing.JButton player3Target1;
    private javax.swing.JButton player3Target2;
    private javax.swing.JButton player3Target3;
    // End of variables declaration//GEN-END:variables
// A custom panel that merges and stretches multiple asset layers into a single background
    
class LayeredBackgroundPanel extends javax.swing.JPanel {
    private final java.util.List<java.awt.Image> layers = new java.util.ArrayList<>();

    public LayeredBackgroundPanel(String[] resourcePaths) {
        setOpaque(false); // Allows transparency logic between asset sheets
        
        // Load every individual file layer into our memory queue
        for (String path : resourcePaths) {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                layers.add(new javax.swing.ImageIcon(imgURL).getImage());
                
            } else {
                System.out.println("Warning: Could not find layer asset at " + path);
            }
        }
    }


@Override
protected void paintComponent(Graphics g) {
    // 1. Call super to let Swing normally clear and manage the background layers
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // 2. STATE 1: Draw Menu GIF Fullscreen
    if (currentScreen.equals("MENU") && backgroundGif != null) {
        g2d.drawImage(backgroundGif, 0, 0, getWidth(), getHeight(), this);
    }

    // 3. STATE 2: Settings Background Layers
    else if (currentScreen.equals("SETTINGS")) {
        for (Image layerImage : settingsLayers) {
            if (layerImage != null) {
                g2d.drawImage(layerImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // 4. STATE 3: Draw Arena Background Fullscreen
    else if (currentScreen.equals("ARENA") && newArenaBackground != null) {
        g2d.drawImage(newArenaBackground, 0, 0, getWidth(), getHeight(), this);
    }

    g2d.dispose();
}


}






}
