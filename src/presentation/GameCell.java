package presentation;

import domain.Board;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.*;

public class GameCell extends JButton {
    private GameCell previous; // Reference to the previous node
    private GameCell next;     // Reference to the next node
    private ImageIcon overlayImage;  // Imagen de sobreposición
    private int row;
    private int column;
    private Board board;
    private String currentPlantType; // Current plant type
    private String currentZombieType; // Current zombie type
    private ImageIcon backgroundImage; // Background image (plant or zombie)
    private int bgX, bgY; // Background position within the cell
    private Timer moveTimer; // Timer to move the zombie image
    private Boolean occuped;
    private ImageIcon bulletImage; // Imagen de la bala private int bulletX;

    private boolean showBullet; // Indicador para mostrar la bala// Posición X de la bala
    private int bulletX;
    private int overlayX;
    private int overlayY;

    public GameCell(int row, int column) {
        super();
        this.row = row;
        this.occuped=false;
        this.column = column;
        this.currentPlantType = null;
        this.currentZombieType = null;
        bgX = 0; // Initial background position
        bgY = 0; // Initial background position
        setContentAreaFilled(false); // Make the button transparent for custom drawing
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    // Methods to manage the linked list
    public void setPrevious(GameCell previous) {
        this.previous = previous;
    }

    public GameCell getPrevious() {
        return previous;
    }

    public void setNext(GameCell next) {
        this.next = next;
    }

    public GameCell getNext() {
        return next;
    }

    // Method to add a plant
    public void addPlant(String plantType) {
        this.currentPlantType = plantType; // Save the current plant type
        if(plantType.equals("PeasShooter")){
            System.out.println("disparo");
            iniciarD();
        }
        // Map plant types to their respective image paths
        Map<String, String> plantImages = Map.of(
                "SunFlower", "images/SunFlower.png",
                "PeasShooter", "images/PeasShooter.png",
                "WallNut", "images/WallNut.png",
                "PotatoMine", "images/PotatoMine.png",
                "EciPlant", "images/EciPlant.png"
        );

        // Set the background image of the plant
        backgroundImage = new ImageIcon(plantImages.getOrDefault(plantType, null));
        repaint(); // Redraw the cell with the new background image (plant)
        occuped=true;
    }

    // Method to add a zombie and start its movement
    public void addZombie(String zombieType) {
        this.currentZombieType = zombieType;

        // Map zombie types to their respective image paths
        Map<String, String> zombieImages = Map.of(
                "ZombieBasic", "images/ZombieBasic.png",
                "ConeheadZombie", "images/ConeheadZombie.png",
                "BucketheadZombie", "images/BucketheadZombie.png",
                "FlagZombie", "images/FlagZombie.png"
        );

        // Set the background image of the zombie
        backgroundImage = new ImageIcon(zombieImages.getOrDefault(zombieType, null));
        bgX = getWidth(); // Start the zombie at the right edge
        // Start the zombie movement timer
        initMoveTimerZombie();
        repaint(); // Redraw the cell with the new background image (zombie)
    }

    // Method to initialize the timer that moves the zombie image
    private void initMoveTimerZombie() {
        moveTimer = new Timer(100, e -> {
            if (backgroundImage != null) {
                bgX -= 2; // Move the background 2 pixels to the left (for the zombie)
                if (bgX < -getWidth()) {
                    if (previous != null) {
                        previous.receive(currentZombieType);

                    }
                    moveTimer.stop(); // Stop the timer when the zombie moves out of the cell
                }
            }
            repaint(); // Redraw the cell with the new background position (zombie)
        });
        moveTimer.start(); // Start the zombie timer
    }
    public boolean isOccuped(){
        return occuped;
    }
    // Method to receive a zombie or plant
    public void receive(String type) {
        if (isOccuped()){

            System.out.println("colision");
        }else{
            addZombie(type);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar la imagen de fondo si existe
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), bgX, bgY, getWidth(), getHeight(), this);
        }

        // Dibujar la imagen de sobreposición si existe
        if (overlayImage != null) {

            g2d.drawImage(overlayImage.getImage(), overlayX, overlayY, getWidth(), getHeight(), this);
        }
    }


    // Method to remove the plant or zombie (clear everything)
    public void removeBackground() {
        this.backgroundImage = null; // Remove the background image of the plant or zombie
        repaint(); // Redraw the cell
    }

    // Method to add the lawnmower
    public void addLawnMower(int row) {
        setBackgroundImage("images/Mower.png");
        occuped=true;
    }

    // Set the background image (e.g., for the lawnmower or decorations)
    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint(); // Redraw the button with the new image
    }

    // Method to remove the plant
    public void removePlant() {
        if (currentPlantType != null) {
            this.currentPlantType = null; // Clear the current plant type
            this.backgroundImage = null; // Remove the background image
            repaint(); // Redraw the cell
        }

    }
    private void iniciarD(){
        setOverlayImage("images/Pea.png");
    }
    public void setOverlayImage(String imagePath) {
        if (imagePath != null) {
            this.overlayImage = new ImageIcon(imagePath);
        } else {
            this.overlayImage = null;
        }
        repaint(); // Redibujar la celda
    }

}


