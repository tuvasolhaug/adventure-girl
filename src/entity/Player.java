package entity;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity{

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    private Font customFont;

    // For displaying messages
    boolean showMessage = false;
    String message = "";
    int messageTimer = 0;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH = keyH;

        // Load the custom font
        try {

            InputStream is = getClass().getResourceAsStream("/fonts/QuinqueFive.otf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);

        } catch (FontFormatException | IOException e) {

            e.printStackTrace();

            customFont = new Font("Arial", Font.PLAIN, 20); // Fallback font

        }

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 3;
        direction = "down";

    }

    public void getPlayerImage(){

        up1 = setup("/player/2D-game-girl-up");
        up2 = setup("/player/2D-game-girl-up2");
        down1 = setup("/player/2D-game-girl-down");
        down2 = setup("/player/2D-game-girl-down2");
        left1 = setup("/player/2D-game-girl-left");
        left2 = setup("/player/2D-game-girl-left2");
        right1 = setup("/player/2D-game-girl-right");
        right2 = setup("/player/2D-game-girl-right2");

    }

    public void update() {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            // Set direction based on key input
            if(keyH.upPressed){ direction = "up"; }
            else if(keyH.downPressed){ direction = "down"; }
            else if(keyH.leftPressed){ direction = "left"; }
            else if(keyH.rightPressed){ direction = "right"; }

            // Check for tile collision
            collisionOn = false;
            gp.cCheck.checkTile(this);

            //Check object collision
            int objIndex = gp.cCheck.checkObject(this, true);
            pickUpObject(objIndex);

            //sjekk npc-kollisjon
            int npcIndex = gp.cCheck.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Move player if no collision
            if (!collisionOn) {

                switch(direction) {

                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;

                }
            }

            // Sprite animation handling
            spriteCounter++;
            if(spriteCounter > 12) {

                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;

            }
        }
        // Update message timer
        if (messageTimer > 0) {

            messageTimer--;

            if (messageTimer == 0) {

                showMessage = false;

            }
        }


    }

    public void pickUpObject(int i){

        if(i != 999){
        }
    }

    public void interactNPC(int i){
        if(i != 999){

        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch (direction) {

            case "up":
                if(spriteNum == 1){

                    image= up1;

                }

                else if(spriteNum == 2){

                    image= up2;

                }
                break;

            case "down":
                if(spriteNum == 1){

                    image= down1;

                }

                else if (spriteNum == 2){

                    image= down2;

                }
                break;

            case "left":
                if(spriteNum == 1){

                    image= left1;

                }

                else if(spriteNum == 2){

                    image= left2;

                }
                break;

            case "right":
                if(spriteNum == 1){

                    image= right1;

                }

                else if(spriteNum == 2){

                    image= right2;

                }
                break;

        };

        g2.drawImage(image, screenX, screenY, null);

        // MESSAGE DISPLAY (burde egt hatt dette i UI klassen men men)
        if (showMessage) {

            int boxWidth = 600;
            int boxHeight = 100;
            int boxX = (gp.screenWidth - boxWidth) / 2;
            int boxY = gp.screenHeight - 120;

            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRect(boxX + 10, boxY + 10, boxWidth, boxHeight);

            g2.setColor(new Color(255,255,255));
            g2.fillRect(boxX, boxY, boxWidth, boxHeight);

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(8));
            g2.drawRect(boxX, boxY, boxWidth, boxHeight);


            g2.setFont(customFont.deriveFont(Font.BOLD, 10F));
            g2.setColor(Color.black);

            String[] words = message.split(" ");
            StringBuilder line = new StringBuilder();
            int lineHeight = g2.getFontMetrics().getHeight();
            int y = boxY + lineHeight + 10;

            for (String word : words) {

                if (g2.getFontMetrics().stringWidth(line + word) < boxWidth - 40) {

                    line.append(word).append(" ");

                } else {

                    g2.drawString(line.toString(), boxX + 20, y);
                    line = new StringBuilder(word + " ");
                    y += lineHeight;

                }
            }

            g2.drawString(line.toString(), boxX + 20, y);

        }
    }
}
