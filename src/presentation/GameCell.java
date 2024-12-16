package presentation;

import domain.Board;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Clase `GameCell` que representa una celda individual en el tablero del juego.
 * Cada celda puede contener una planta, un zombie o una podadora y controla la lógica de interacción entre ellos.
 */
public class GameCell extends JButton {
    private GameCell previous; // Referencia a la celda anterior en la fila
    private GameCell next; // Referencia a la siguiente celda en la fila
    private int row; // Fila de la celda en el tablero
    private int column; // Columna de la celda en el tablero
    private Board board; // Instancia del singleton `Board` para la lógica del juego
    private String currentPlantType; // Tipo de planta actual en la celda
    private String currentZombieType; // Tipo de zombie actual en la celda
    private ImageIcon backgroundImage; // Imagen de fondo de la celda
    private int bgX, bgY; // Coordenadas para animaciones
    private Timer moveTimer; // Timer para controlar el movimiento de zombies
    private boolean occuped; // Indica si la celda está ocupada
    private boolean haveZombie; // Indica si hay un zombie en la celda
    private Timer peaTimer; // Timer para generar guisantes (Peas)
    private List<Pea> peas; // Lista de guisantes presentes en la celda
    private boolean lawnmowerActive; // Indica si la podadora está activa en la celda
    private ScreenGame screenGame; // Referencia a la instancia de `ScreenGame`

    // Mapas para almacenar las rutas de las imágenes de plantas y zombies
    private static final Map<String, String> PLANT_IMAGES = Map.of(
        "SunFlower", "images/SunFlower.png",
        "PeasShooter", "images/PeasShooter.png",
        "WallNut", "images/WallNut.png",
        "PotatoMine", "images/PotatoMine.png",
        "EciPlant", "images/EciPlant.png"
    );

    private static final Map<String, String> ZOMBIE_IMAGES = Map.of(
        "ZombieBasic", "images/ZombieBasic.png",
        "ZombieConehead", "images/ZombieConehead.png",
        "ZombieBuckethead", "images/ZombieBuckethead.png"
    );

    /**
     * Constructor para la clase `GameCell`.
     *
     * @param row         Fila de la celda en el tablero.
     * @param column      Columna de la celda en el tablero.
     * @param screenGame  Instancia de `ScreenGame` que contiene la celda.
     */
    public GameCell(int row, int column, ScreenGame screenGame) {
        super();
        this.row = row;
        this.column = column;
        this.occuped = false;
        this.currentPlantType = null;
        this.currentZombieType = null;
        this.bgX = 0;
        this.bgY = 0;
        this.peas = new ArrayList<>();
        setContentAreaFilled(false); // Hacer transparente el fondo
        this.haveZombie = false;
        this.board = Board.getBoard(); // Obtener la instancia del singleton `Board`
        this.lawnmowerActive = true; // Activar la podadora
        this.screenGame = screenGame;
    }

    /**
     * Obtiene la fila de la celda.
     *
     * @return El número de fila.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna de la celda.
     *
     * @return El número de columna.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Verifica si la celda está ocupada.
     *
     * @return `true` si está ocupada, de lo contrario, `false`.
     */
    public boolean isOccupied() {
        return occuped;
    }

    /**
     * Verifica si hay un zombie en la celda.
     *
     * @return `true` si hay un zombie, de lo contrario, `false`.
     */
    public boolean hasZombie() {
        return haveZombie;
    }

    /**
     * Establece la imagen de fondo de la celda.
     *
     * @param imagePath Ruta de la imagen.
     */
    public void setBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint(); // Redibujar la celda
    }

    /**
     * Establece la referencia a la celda anterior en la fila.
     *
     * @param previous La celda anterior.
     */
    public void setPrevious(GameCell previous) {
        this.previous = previous;
    }

    /**
     * Establece la referencia a la siguiente celda en la fila.
     *
     * @param next La celda siguiente.
     */
    public void setNext(GameCell next) {
        this.next = next;
    }

    /**
     * Agrega una planta a la celda.
     * Actualiza la lógica y la representación visual de la celda.
     *
     * @param plantType Tipo de planta que se agregará.
     */
    public void addPlant(String plantType) {
        if (haveZombie) {
            System.out.println("Cannot place plant. Zombie present in cell.");
            return; // No se puede agregar planta si hay un zombie
        }

        this.currentPlantType = plantType;
        if (plantType.equals("PeasShooter")) {
            startPeaTimer(); // Iniciar el temporizador para disparar guisantes
        }
        updateBackgroundImage(PLANT_IMAGES.get(plantType)); // Cambiar imagen de fondo
        occuped = true; // Marcar la celda como ocupada
        repaint();
    }

    /**
     * Elimina la planta de la celda.
     * Detiene cualquier temporizador relacionado y actualiza el estado.
     */
    public void removePlant() {
        if (currentPlantType != null) {
            if (peaTimer != null) {
                peaTimer.stop(); // Detener el temporizador de disparos
            }
            this.currentPlantType = null;
            this.occuped = false; // Marcar la celda como desocupada
            this.backgroundImage = null;
            repaint();
            board.removePlant(row, column); // Actualizar la lógica en el tablero
        }
    }

    /**
     * Agrega un zombie a la celda.
     * Inicia su movimiento y actualiza la lógica y la interfaz.
     *
     * @param zombieType Tipo de zombie que se agregará.
     */
    public void addZombie(String zombieType) {
        this.currentZombieType = zombieType;
        updateBackgroundImage(ZOMBIE_IMAGES.getOrDefault(zombieType, ZOMBIE_IMAGES.get("ZombieBasic"))); // Imagen de zombie
        bgX = getWidth(); // Inicia desde el borde derecho
        initializeZombieMovement(); // Configurar movimiento del zombie
        occuped = true;
        haveZombie = true;
        repaint();
    }

    /**
     * Actualiza la imagen de fondo de la celda.
     *
     * @param imagePath Ruta de la nueva imagen.
     */
    private void updateBackgroundImage(String imagePath) {
        if (imagePath != null) {
            this.backgroundImage = new ImageIcon(imagePath);
        } else {
            this.backgroundImage = null;
        }
        repaint();
    }

    /**
     * Inicia el temporizador para disparar guisantes (Peas).
     * Los guisantes se disparan cada 5 segundos.
     */
    private void startPeaTimer() {
        peaTimer = new Timer(5000, e -> addPea());
        peaTimer.start();
    }

    /**
     * Agrega un guisante (Pea) a la celda y comienza su movimiento.
     */
    private void addPea() {
        Pea newPea = new Pea(0, getHeight() / 2 - 10); // Inicializar en la posición central
        peas.add(newPea);
        newPea.startMovement(); // Iniciar movimiento del guisante
    }

    /**
     * Inicializa el movimiento de los zombies en la celda.
     * El zombie se mueve hacia la izquierda, interactuando con plantas si las encuentra.
     * Si no hay interacciones, sigue avanzando hasta salir de la celda.
     */
    private void initializeZombieMovement() {
        moveTimer = new Timer(100, e -> {
            if (!screenGame.getPauseGame()) { // Solo se mueve si el juego no está pausado
                bgX -= 1; // Reducir X para mover el zombie hacia la izquierda

                // Detectar colisión con planta en la celda anterior
                if (previous != null && previous.currentPlantType != null && bgX <= getWidth() / 8) {
                    ZombieAttack(); // Iniciar ataque a la planta
                    ((Timer) e.getSource()).stop(); // Detener movimiento del zombie
                    return;
                }

                repaint(); // Redibujar celda para mostrar el movimiento

                // Verificar si el zombie sale de la celda
                if (bgX < -getWidth()) {
                    ((Timer) e.getSource()).stop();
                    send("Zombie", currentZombieType); // Enviar zombie a la siguiente celda
                    removeBackground(); // Limpiar la celda
                    occuped = false;
                    haveZombie = false;
                    currentZombieType = null;
                }
            }
        });
        moveTimer.start(); // Iniciar el temporizador
    }

    /**
     * Método para recibir elementos enviados a la celda (zombie, podadora, guisante).
     * @param type Tipo de elemento recibido ("Zombie", "LawnMower", "Pea").
     * @param typeCharacter Detalles del elemento recibido.
     */
    public void receive(String type, String typeCharacter) {
        if ("Zombie".equals(type)) {
            addZombie(typeCharacter); // Agregar un zombie
        } else if ("LawnMower".equals(type)) {
            addLawnMower(row); // Activar la podadora
            chekLawnMower(); // Verificar activación de la podadora
        }
    }


    
    /**
     * Método para enviar elementos desde la celda.
     * @param type Tipo de elemento enviado ("Zombie", "LawnMower", "Pea").
     * @param typeCharacter Detalles del elemento enviado.
     */
    private void send(String type, String typeCharacter) {
        if ("Pea".equals(type)) {
            sendPea(); // Enviar guisante
        } else if ("Zombie".equals(type)) {
            sendZombie(); // Enviar zombie
        } else if ("LawnMower".equals(type)) {
            sendLawnMower(); // Enviar podadora
        }
    }

    /**
     * Método para mover la podadora hacia la derecha.
     * Avanza hasta llegar al final de la fila o ser enviada a la siguiente celda.
     */
    private void sendLawnMower() {
        if (next == null) { // Si es la última celda
            if (moveTimer != null) {
                moveTimer.stop();
            }
            removeBackground(); // Limpiar celda
            haveZombie = false;
            currentZombieType = null;
        } else {
            moveTimer = new Timer(50, e -> {
                if (!screenGame.getPauseGame()) {
                    bgX += 5; // Mover podadora hacia la derecha
                    repaint();

                    // Si la podadora sale de la celda
                    if (bgX > getWidth()) {
                        ((Timer) e.getSource()).stop();
                        next.receive("LawnMower", "LawnMower"); // Enviar podadora a la siguiente celda
                        removeBackground(); // Limpiar celda actual
                    }
                }
            });
            moveTimer.start(); // Iniciar el temporizador
        }
    }

    /**
     * Método para enviar un guisante desde la celda.
     * El guisante se envía hacia la derecha.
     */
    private void sendPea() {
        if (next != null) {
            next.receive("Pea", "Pea");
            next.addPea();
        }
    }

    /**
     * Método para enviar un zombie desde la celda hacia la anterior.
     */
    private void sendZombie() {
        if (previous != null) {
            if (!previous.isOccupied()) {
                if (previous.getColumn() == 0) {
                    previous.chekLawnMower(); // Activar podadora en la primera celda
                } else {
                    previous.receive("Zombie", currentZombieType); // Enviar zombie
                    board.moveZombie(row, column); // Actualizar posición en el tablero

                    // Limpiar celda actual
                    this.haveZombie = false;
                    this.currentZombieType = null;
                    this.backgroundImage = null;
                }
            }
        }
    }


    /**
     * Verifica la activación de la podadora en la celda.
     * Si está activa, la envía hacia adelante; de lo contrario, muestra un mensaje de pérdida.
     */
    private void chekLawnMower() {
        if (lawnmowerActive) {
            send("LawnMower", "LawnMower");
            board.lawnmower(row); // Notificar al tablero la activación
            lawnmowerActive = false; // Desactivar podadora después de usarla
        } else {
            JOptionPane.showMessageDialog(null, "Perdiste! \nSigue intentando", "Juego", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    /**
     * Elimina la imagen de fondo actual de la celda.
     * Se utiliza para limpiar la celda después de eliminar un elemento como un zombie o una planta.
     */
    public void removeBackground() {
        this.backgroundImage = null; // Eliminar referencia a la imagen
        repaint(); // Redibujar la celda
    }

    /**
     * Agrega la podadora al inicio de la fila, representada visualmente como una imagen.
     * @param row Número de fila donde se activa la podadora.
     */
    public void addLawnMower(int row) {
        setBackgroundImage("images/Mower.png"); // Asignar imagen de la podadora
        lawnmowerActive = true; // Activar el estado de la podadora
    }

    /**
     * Sobrescribe el método `paintComponent` para dibujar los elementos de la celda.
     * Se encarga de dibujar zombies, guisantes y otros elementos visuales dinámicamente.
     * 
     * @param g Objeto `Graphics` para manejar el dibujo.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Dibujar imagen de fondo (zombie, podadora, planta)
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage.getImage(), bgX, bgY, getWidth(), getHeight(), this);
        }

        // Dibujar los guisantes en movimiento
        for (Pea pea : peas) {
            g2d.drawImage(new ImageIcon("images/Pea.png").getImage(), pea.x, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Clase interna para manejar los guisantes que disparan las plantas.
     * Controla su movimiento y colisiones con los zombies.
     */
    private class Pea {
        int x, y; // Posición del guisante
        private Timer movePeaTimer; // Timer para mover el guisante

        /**
         * Constructor de la clase Pea.
         * @param x Coordenada inicial en el eje X.
         * @param y Coordenada inicial en el eje Y.
         */
        Pea(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Inicia el movimiento del guisante hacia la derecha.
         * Detecta colisiones con zombies y actualiza el estado del juego.
         */
        void startMovement() {
            movePeaTimer = new Timer(50, e -> {
                if (!screenGame.getPauseGame()) { // Solo mover si el juego no está pausado
                    x += 5; // Incrementar posición X para mover el guisante

                    // Si el guisante sale de la celda actual
                    if (x > getWidth()) {
                        send("Pea", "Pea"); // Enviar guisante a la siguiente celda
                        peas.remove(this); // Eliminar el guisante actual
                        movePeaTimer.stop(); // Detener el temporizador
                    } 
                    // Si colisiona con un zombie en la celda
                    else if (hasZombie() && x >= bgX) {
                        peas.remove(this); // Eliminar el guisante
                        movePeaTimer.stop(); // Detener su movimiento
                        repaint();

                        // Si el zombie recibe daño y es eliminado
                        if (!board.damageZombie(row, column, "Pea")) {
                            backgroundImage = null; // Eliminar zombie visualmente
                            haveZombie = false; // Actualizar estado de la celda
                            currentZombieType = null;

                            if (moveTimer != null) {
                                moveTimer.stop(); // Detener movimiento del zombie
                            }

                            repaint();
                        }
                    }
                    repaint();
                }
            });
            movePeaTimer.start(); // Iniciar el temporizador del guisante
        }
    }

    /**
     * Maneja el ataque del zombie hacia la planta en la celda anterior.
     * Si la planta es destruida, el zombie avanza.
     */
    private void ZombieAttack() {
        if (previous.currentPlantType != null && currentZombieType != null) {
            Timer attackTimer = new Timer(1000, e -> {
                if (!screenGame.getPauseGame()) { // Solo atacar si el juego no está pausado
                    if (!board.ZombieAttack(row, column)) { // Verificar si la planta es destruida
                        previous.removePlant(); // Eliminar la planta visualmente
                        send("Zombie", currentZombieType); // Enviar zombie a la siguiente celda

                        ((Timer) e.getSource()).stop(); // Detener el temporizador de ataque

                        if (moveTimer != null) {
                            moveTimer.stop(); // Detener el movimiento actual del zombie
                        }

                        initializeZombieMovement(); // Reiniciar movimiento del zombie
                        return;
                    }

                    repaint(); // Redibujar la celda después del ataque
                }
            });
            attackTimer.setRepeats(true); // Hacer que el ataque se repita
            attackTimer.start(); // Iniciar el temporizador de ataque
        }
    }
}
