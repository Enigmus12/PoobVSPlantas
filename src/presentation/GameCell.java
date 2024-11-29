package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GameCell extends JButton {
    private GameCell previous; // Referencia al nodo anterior
    private GameCell next;     // Referencia al nodo siguiente
    private int row;
    private int column;
    private ArrayList<String> ocupants;  // Objetos en la celda
    private List<ImageIcon> overlayImages; // Lista de imágenes superpuestas
    private ImageIcon backgroundImage; // Imagen de fondo (planta o decoración)
    private String currentPlantType; // tipo de planta actual

    public GameCell(int row, int column) {
        super();
        previous = null;
        next = null;
        this.row = row;
        this.column = column;
        currentPlantType = null;
        setContentAreaFilled(false); // Hacer el botón transparente para gestionar el dibujo personalizado
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    // Métodos para gestionar la lista enlazada
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
        this.currentPlantType = plantType; // Save the current plant type
        // Map plant types to their respective image paths
        Map<String, String> plantImages = Map.of(
                "SunFlower", "images/SunFlower.png",
                "PeasShooter", "images/PeasShooter.png",
                "WallNut", "images/WallNut.png",
                "PotatoMine", "images/PotatoMine.png",
                "EciPlant", "images/EciPlant.png"
        );

        // Set the background image if the plant type exists, otherwise null
        setBackgroundImage(plantImages.getOrDefault(plantType, null));
    }

    public void addLawnMower(int row ){
        setBackgroundImage("images/Mower.png");
    }

    // Método para eliminar la planta
    public void removePlant() {
        if (currentPlantType != null) {
            this.currentPlantType = null; // Limpiar el tipo de planta actual
            this.backgroundImage = null; // Eliminar la imagen de fondo
            repaint(); // Redibujar la celda; // Limpiar la lista de plantas
        }

    }

    // Establecer la imagen de fondo
    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            System.out.println("error en imagen");
        }
        repaint(); // Redibujar el botón
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dibujar la imagen de fondo si existe
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void addZombie(String zombieType) {
        switch (zombieType) {
            case "ZombieBasic":
                setBackgroundImage("images/ZombieBasic.png");
                break;
            case "ConeheadZombie":
                setBackgroundImage("images/ConeheadZombie.png");
                break;
            case "BucketheadZombie":
                setBackgroundImage("images/BucketheadZombie.png");
                break;
            case "FlagZombie":
                setBackgroundImage("images/FlagZombie.png");
                break;
            default:
                setBackgroundImage(null);
        }
        repaint(); // Redibujar la celda para mostrar el zombie
    }


}