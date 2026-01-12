package main;

import entity.Player;
import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;



public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font customFont_20, customFont_36, customFont_60;
    public boolean gameFinished = false;

    public UI(GamePanel gp) {
        this.gp = gp;

        // Load the custom font
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/QuinqueFive.ttf");
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            customFont_20 = customFont.deriveFont(Font.BOLD, 20f);
            customFont_36 = customFont.deriveFont(Font.BOLD, 36f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont_20 = new Font("Arial", Font.BOLD, 20); // Fallback font
            customFont_36 = new Font("Arial", Font.BOLD, 36); // Fallback font

        }
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(customFont_36);
        g2.setColor(Color.WHITE);

        if(gp.gameState == gp.playState) {
            // PLaystate ting
        }

        if(gp.gameState == gp.pauseState) {
            drawPauseScreeen();
        }
    }

    public void drawPauseScreeen(){
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        x = gp.screenWidth/2 - length/2;
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXforCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

}
