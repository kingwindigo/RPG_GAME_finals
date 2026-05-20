/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

/**
 *
 * @author Administrator
 */
public class SkillVisualData {

        private final String gifName;
        private final int durationMs;

        public SkillVisualData(String gifName, int durationMs) {
            this.gifName = gifName;
            this.durationMs = durationMs;
        }

        public String getGifName() { return gifName; }
        public int getDurationMs() { return durationMs; }
    
}
