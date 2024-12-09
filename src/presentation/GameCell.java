package presentation;

import domain.Board;
import java.awt.*;
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
    private int overlayX;
    private int overlayY;
    private Timer peaTimer;


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
    // Método para iniciar el temporizador de la "pea"
    private void iniciarPea() {
        // Temporizador para crear y mover la "pea" cada 5 segundos
        peaTimer = new Timer(10000, e -> {
            // Cada vez que se dispara, se agrega una "pea"
            addPea();
        });
        peaTimer.start();
    }

    // Método para agregar la "pea" como un overlay en el "Peashooter"
    // Método para agregar la "pea" como un overlay en el "Peashooter"
    private void addPea() {
        // Crear la "pea" y mostrarla en la celda del "Peashooter"
        setOverlayImage("images/Pea.png");  // Imagen de la "pea"
        if(bgX==0){
            overlayX-=getHeight()/2;
        }else {
            overlayX = bgX;  // Inicia en la posición X del "Peashooter"
            overlayY = bgY;  // Usa la misma Y que el "Peashooter"
        }
        // Temporizador para mover la "pea" cada 100ms
        Timer movePeaTimer = new Timer(100, e -> {
            // Mueve la "pea" a la derecha
            overlayX += 5;  // Incrementar posición X

            // Verificar si la "pea" ha salido de la celda
            if (overlayX > getWidth()) {
                // Enviar la "pea" a la siguiente celda
                send(true, "Pea", "Pea");
                // Detener el movimiento de la "pea" en la celda actual
                ((Timer)e.getSource()).stop();
                setOverlayImage(null);  // Eliminar la "pea" del overlay en la celda actual
            } else {
                repaint();  // Redibuja la celda con la nueva posición de la "pea"
            }
        });
        movePeaTimer.start();
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
            iniciarPea();
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


    public void addZombie(String zombieType) {
        this.currentZombieType = zombieType;
        
        Map<String, String> zombieImages = Map.of(
            "ZombieBasic", "images/ZombieBasic.png"
        );
    
        // Establecer la imagen de fondo del zombie
        backgroundImage = new ImageIcon(zombieImages.getOrDefault(zombieType, "images/ZombieBasic.png"));
        
        // Inicializar el movimiento del zombie
        initializeZombieMovement();
        
        occuped = true;
        repaint();
    }
    
    private void initializeZombieMovement() {
        // Crear un temporizador para mover el zombie
        moveTimer = new Timer(100, e -> {
            // Mover el zombie hacia la izquierda
            bgX -= 1; // Ajustar la velocidad 
            
            // Verificar si el zombie ha salido completamente de la celda
            if (bgX < -getWidth()) {
                // Detener el temporizador
                ((Timer)e.getSource()).stop();
                
                // Intentar mover el zombie a la celda anterior
                send(false, "Zombie", currentZombieType);
                
                // Limpiar esta celda
                currentZombieType = null;
                backgroundImage = null;
                occuped = false;
                bgX = 0;
            }
            
            repaint();
        });
        
        // Iniciar el temporizador de movimiento
        moveTimer.start();
    }


    public boolean isOccuped(){return occuped;}

    // Método para recibir un zombie o planta
    // Método para recibir un zombie o planta

    public void receive(String type, String typeCharacter) {
        if ("Zombie".equals(type)) {
            if (!isOccuped()) {
                addZombie(typeCharacter);
            } else {
                System.out.println("Cell occupied, cannot receive zombie");
            }
        } else if ("Pea".equals(type)) {
            // Si la celda está ocupada pero no por un zombie, la "pea" pasa
            if (isOccuped()) {
                if (currentZombieType != null) {
                    // Si hay un zombie, la "pea" se detiene
                    System.out.println("La 'pea' no puede pasar, hay un zombie.");
                    return;  // La "pea" no pasa
                } else {
                    // Si hay una planta pero no un zombie, la "pea" pasa
                    System.out.println("La 'pea' pasa por encima de la planta.");
                    // Aquí podemos iniciar el movimiento de la "pea" o cualquier lógica adicional
            }
        } }
    }


    // Método para enviar un zombie o planta
    private void send(Boolean direction, String type, String typeCharacter) {
        // Verifica la dirección (hacia adelante o hacia atrás) y si la celda siguiente está ocupada
        if (direction) {
            // Enviar al siguiente si no está ocupado
            if (next != null && !next.isOccuped()) {
                next.receive(type, typeCharacter);  // Enviar a la siguiente celda
                // Mover la "pea" al siguiente celda (y hacer que continúe moviéndose allí)
                next.addPea();
            } else {
                System.out.println("No se puede enviar a la siguiente celda. Está ocupada.");
            }
        } else {
            // Enviar a la anterior si no está ocupado
            if (previous != null && !previous.isOccuped()) {
                previous.receive(type, typeCharacter);  // Enviar a la celda anterior
            } else {
                System.out.println("No se puede enviar a la celda anterior. Está ocupada.");
            }
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


