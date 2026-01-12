package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tiles fordi moderne monitor har bedre res
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 px
    public final int screenHeight = maxScreenRow * tileSize; // 576px

    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //INITIER KEYHANDLER
    KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();

    //SYSTEM
    public CollisionCheck cCheck = new CollisionCheck(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public Entity npc[] = new Entity[10];
    TileManager tileM = new TileManager(this);
    public SuperObject obj[] = new SuperObject[10];
    public UI ui = new UI(this);
    Thread gameThread;

    //FPS
    int FPS = 60;

    //Game state
    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;

    //KONSTRUKTØR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setNPC();
        aSetter.setObject();
        playMusic(0);
        gameState = playState;
    }

    //METODE
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = (double) 1000000000 /FPS; // 0.01666 s
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            // denne funksjonen gjer at spelet oppdaterer karakterens posisjon

            update();

            repaint();



            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000000.0;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void update(){

        if(gameState == playState) {
            //PLAYER
            player.update();

            //NPC
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }
        }

        if(gameState == pauseState) {
            //nothing
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        //Debug
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }

        //Tile
        tileM.draw(g2);

        //Object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        //NPC wizard
        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null) {
                npc[i].draw(g2);
            }
        }

        //Player
        player.draw(g2);

        //UI
        ui.draw(g2);

        //Debug
        if(keyH.checkDrawTime == true){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.WHITE);
            g2.drawString("Drawtime: " + passed, 100, 40);
            System.out.println("Drawtime: " + passed);
        }


        g2.dispose();
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
