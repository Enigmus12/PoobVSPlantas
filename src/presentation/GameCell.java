package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import domain.Character;
import domain.Plant;
import domain.Zombie;

public class GameCell extends JButton {
    private GameCell previous; // Referencia al nodo anterior
    private GameCell next;     // Referencia al nodo siguiente
    private int row;
    private int column;
    private List<Plant> plants;  // Plantas en la celda
    private List<Zombie> zombies; // Zombis en la celda
    private List<Plant> peas;// Guisantes en la celda

    private ImageIcon backgroundImage; // Imagen de fondo (planta o decoración)

    public GameCell(int row, int column) {
        super();
        previous = null;
        next = null;
        this.row = row;
        this.column = column;
        plants = new ArrayList<>();
        zombies = new ArrayList<>();
        peas = new ArrayList<>();
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

    // Métodos para gestionar plantas
    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

    public List<Plant> getPlants() {
        return plants;
    }

    // Métodos para gestionar zombis
    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }

    public void removeZombie(Character zombie) {
        zombies.remove(zombie);
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    // Métodos para gestionar guisantes
    public void addPea(Plant pea) {
        peas.add(pea);
    }

    public void removePea(Plant pea) {
        peas.remove(pea);
    }

    public List<Plant> getPeas() {
        return peas;
    }

    // Método para establecer la imagen de fondo según el tipo de planta
    public void addPlant(String plantType) {
        switch (plantType) {
            case "Sunflower":
                setBackgroundImage("images/Sunflower.png");
                break;
            case "Peashooter":
                setBackgroundImage("images/Peashooter.png");
                break;
            case "WallNut":
                setBackgroundImage("images/WallNut.png");
                break;
            case "PotatoMine":
                setBackgroundImage("images/PotatoMine.png");
                break;
            case "EciPlant":
                setBackgroundImage("images/eciPlant.png");
                break;
            default:
                setBackgroundImage(null);
        }
    }
    public void addLawnMower(int row ){
        setBackgroundImage("images/mower.png");
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
}