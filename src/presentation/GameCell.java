package presentation;

import domain.Board;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameCell extends JButton {
    private GameCell previous;
    private GameCell next;
    private int row;
    private int column;
    private Board board;
    private String currentPlantType;
    private String currentZombieType;
    private ImageIcon backgroundImage;
    private int bgX, bgY;
    private Timer moveTimer;
    private boolean occuped;
    private boolean haveZombie;
    private Timer peaTimer;
    private List<Pea> peas; // Lista para almacenar las "peas"

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
        this.peas = new ArrayList<>();
        setContentAreaFilled(false);
        haveZombie = false;
        this.board=Board.getBoard();
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

    public boolean hasZombie() {
        return haveZombie;
    }

    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint();
    }

    public void setPrevious(GameCell previous) {
        this.previous = previous;
    }

    public void setNext(GameCell next) {
        this.next = next;
    }

    public void addPlant(String plantType) {
        // Si ya hay un zombie, no permitir agregar planta
        if (haveZombie) {
            System.out.println("Cannot place plant. Zombie present in cell.");
            return;
        }
        
        this.currentPlantType = plantType;
        if (plantType.equals("PeasShooter")) {
            startPeaTimer();
        }
        updateBackgroundImage(PLANT_IMAGES.get(plantType));
        occuped = true;
        repaint();
    }

    public void removePlant() {
        if (currentPlantType != null) {
            if (peaTimer != null) {
                peaTimer.stop();
            }
            this.currentPlantType = null;
            this.backgroundImage = null;
            repaint();
        }
    }

    public void addZombie(String zombieType) {
        this.currentZombieType = zombieType;
        updateBackgroundImage(ZOMBIE_IMAGES.getOrDefault(zombieType, ZOMBIE_IMAGES.get("ZombieBasic")));
        bgX = getWidth(); // Comenzar desde el borde derecho
        initializeZombieMovement();
        occuped = true;
        haveZombie = true;
        repaint(); // Forzar repintado inicial
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
        peaTimer = new Timer(5000, e -> addPea());
        peaTimer.start();
    }

    private void addPea() {
        Pea newPea = new Pea(0, getHeight() / 2 - 10);
        peas.add(newPea);
        newPea.startMovement();
    }

    private void initializeZombieMovement() {
        moveTimer = new Timer(100, e -> {
            bgX -= 1; // velocidad de movimiento
            
            // Si hay una planta en la celda anterior y estamos lo suficientemente cerca
            if (previous != null && previous.currentPlantType != null && bgX <= getWidth() / 8) {

                ZombieAttack();
                
                // Detener el movimiento cuando está muy cerca de la planta
                ((Timer) e.getSource()).stop();
                return;
            }
            
            repaint(); // Repintar en cada iteración para mostrar el movimiento
    
            if (bgX < -getWidth()) {
                ((Timer) e.getSource()).stop();
                send("Zombie", currentZombieType);
                System.out.println(currentZombieType);
                removeBackground();
                occuped = false;
                haveZombie = false;
                currentZombieType = null;
            }
        });
        moveTimer.start();
    }

    public void receive(String type, String typeCharacter) {
        if ("Zombie".equals(type)) {

            addZombie(typeCharacter);
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
        if (next != null) {
            next.receive("Pea", "Pea");
            next.addPea();
        }
    }

    private void sendZombie() {
        if (previous != null) {
            if (!previous.isOccupied()) {
                previous.receive("Zombie", currentZombieType);
                board.moveZombie(row, column);
                
                // Limpiar completamente el estado actual
                this.haveZombie = false;
                this.currentZombieType = null;
                this.backgroundImage = null;
            }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar zombie o background en movimiento
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), bgX, bgY, getWidth(), getHeight(), this);
        }
        
        // Si hay planta, dibujarla
        if (currentPlantType != null) {
            ImageIcon plantIcon = new ImageIcon(PLANT_IMAGES.get(currentPlantType));
            g2d.drawImage(plantIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        
        // Dibujar "peas"
        for (Pea pea : peas) {
            g2d.drawImage(new ImageIcon("images/Pea.png").getImage(), pea.x, 0, getWidth(), getHeight(), this);
        }
    }

    private class Pea {
        int x, y;
        private Timer movePeaTimer;

        Pea(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void startMovement() {
            movePeaTimer = new Timer(50, e -> {
                x += 2;
                if (x > getWidth()) {
                    send("Pea", "Pea");
                    peas.remove(this);
                    movePeaTimer.stop();
                } else if (hasZombie() && x >= bgX) {
                    // Detener "pea" y continuar con el zombi
                    peas.remove(this);
                    movePeaTimer.stop();
                    repaint();
                    if(!board.damageZombie(row, column, "Pea")){
                        backgroundImage = null;
                        board.removeZombie(row, column);
                    }
                }
                repaint();
            });
            movePeaTimer.start();
        }
    }

    private void ZombieAttack() {
        if (previous.currentPlantType != null && currentZombieType != null) {
            Timer attackTimer = new Timer(1000, e -> {
                // Aplicar daño a la planta
                if (!board.ZombieAttack(row, column)) { // La planta ha muerto
                    previous.removePlant();
                    board.removePlant(row,column-1);// Elimina la planta
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador de ataque
                    initializeZombieMovement(); // Reanuda el movimiento del zombie
                    return;
                }

                repaint();
            });
            attackTimer.setRepeats(true);
            attackTimer.start();
        }
    }


}
