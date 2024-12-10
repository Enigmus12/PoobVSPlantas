package presentation;

import domain.Board;
import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class GameCell extends JButton {
    private GameCell previous;
    private GameCell next;
    private ImageIcon overlayImage;
    private int row;
    private int column;
    private Board board;
    private String currentPlantType;
    private String currentZombieType;
    private ImageIcon backgroundImage;
    private int bgX, bgY, overlayX,overlayY;
    private Timer moveTimer;
    private boolean occuped;
    private Timer peaTimer;
    private static final Map<String, String> PLANT_IMAGES = Map.of(
            "SunFlower", "images/SunFlower.png",
            "PeasShooter", "images/PeasShooter.png",
            "WallNut", "images/WallNut.png",
            "PotatoMine", "images/PotatoMine.png",
            "EciPlant", "images/EciPlant.png"
    );
    private static final Map<String, String> ZOMBIE_IMAGES = Map.of(
            "ZombieBasic", "images/ZombieBasic.png"
    );

    public GameCell(int row, int column) {
        super();
        this.row = row;
        this.column = column;
        this.occuped = false;
        this.currentPlantType = null;
        this.currentZombieType = null;
        this.bgX = 0;
        this.bgY = 0;
        setContentAreaFilled(false);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isOccupied() {
        return occuped;
    }
    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint(); // Redraw the button with the new image
    }

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

    public void addPlant(String plantType) {
        this.currentPlantType = plantType;
        if (plantType.equals("PeasShooter")) {
            startPeaTimer();
        }
        updateBackgroundImage(PLANT_IMAGES.get(plantType));
        occuped = true;
    }

    public void addZombie(String zombieType) {
        this.currentZombieType = zombieType;
        updateBackgroundImage(ZOMBIE_IMAGES.getOrDefault(zombieType, ZOMBIE_IMAGES.get("ZombieBasic")));
        initializeZombieMovement();
        occuped = true;
    }

    private void updateBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint();
    }

    private void startPeaTimer() {
        peaTimer = new Timer(10000, e -> addPea());
        peaTimer.start();
    }

    private void addPea() {
        setOverlayImage("images/Pea.png");
        if (bgX == 0) {
            overlayX -= getHeight() / 2;
        } else {
            overlayX = bgX;
            overlayY = bgY;
        }
        movePea();
    }

    private void movePea() {
        Timer movePeaTimer = new Timer(100, e -> {
            overlayX += 5;
            if (overlayX > getWidth()) {
                send("Pea", "Pea");
                ((Timer) e.getSource()).stop();
                setOverlayImage(null);
            }
            repaint();
        });
        movePeaTimer.start();
    }

    private void initializeZombieMovement() {
        moveTimer = new Timer(100, e -> {
            bgX -= 1;
            if (bgX < -getWidth()) {
                ((Timer) e.getSource()).stop();
                send("Zombie", currentZombieType);
                removeBackground();
                occuped = false; // Liberar la celda cuando el zombie sale
                currentZombieType = null;
            }
            repaint();
        });
        moveTimer.start();
    }

    public boolean isZombieExited() {
        return currentZombieType == null;
    }

    public void receive(String type, String typeCharacter) {
        if ("Zombie".equals(type)) {
            addZombie(typeCharacter); // Eliminar la verificación de ocupación
        }

    }
    

    private void handlePea() {
        if (isOccupied()) {
            if (currentZombieType != null) {
                System.out.println("The 'Pea' cannot pass, there is a zombie.");
            } else {
                System.out.println("The 'Pea' passes over the plant.");
            }
        } else {
            System.out.println("The 'Pea' passes through an empty cell.");
        }
    }

    private void send(String type, String typeCharacter) {
        if ("Pea".equals(type)) {
            sendPea();
        } else if ("Zombie".equals(type)) {
            sendZombie();
        }
    }

    private void sendPea() {
        if (next != null && !next.isOccupied()) {
            next.receive("Pea", "Pea");
            next.addPea();
        } else {
            System.out.println("Cannot send pea, next cell is occupied.");
        }
    }

    private void sendZombie() {
        if (previous != null && !previous.isOccupied()) {
            previous.receive("Zombie", currentZombieType);
        } else {
            System.out.println("Cannot send zombie, previous cell is occupied.");
        }
    }

    public void removeBackground() {
        this.backgroundImage = null;
        repaint();
    }

    public void addLawnMower(int row) {
        setBackgroundImage("images/Mower.png");
        occuped = true;
    }

    public void setOverlayImage(String imagePath) {
        if (imagePath != null) {
            this.overlayImage = new ImageIcon(imagePath);
        } else {
            this.overlayImage = null;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), bgX, bgY, getWidth(), getHeight(), this);
        }
        if (overlayImage != null) {
            g2d.drawImage(overlayImage.getImage(), overlayX, overlayY, getWidth(), getHeight(), this);
        }
    }
    public void removePlant() {
        if (currentPlantType != null) {
            this.currentPlantType = null; // Clear the current plant type
            this.backgroundImage = null; // Remove the background image
            repaint(); // Redraw the cell
        }

    }
}
