package presentation;

import domain.Board;
import domain.PoobVSZombiesExeption;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * Clase ScreenGame
 * Representa la ventana principal del juego "Poob vs Zombies". Maneja la interfaz gráfica y la interacción con el jugador.
 * Se encarga de crear el tablero del juego, manejar eventos del usuario, actualizar el temporizador, y otras funcionalidades principales.
 */
public class ScreenGame extends JFrame {
    private Board board;           // Singleton que maneja la lógica del juego
    private String selectedPlant;  // Almacena el tipo de planta seleccionada actualmente
    private MainApp app;             // Referencia a la aplicación principal
    private JLabel sunsLabel;      // Etiqueta para mostrar la cantidad de soles
    private static final int ROWS = 5;  //Numero de filas predefinido
    private static final int COLS = 10;  //Numero de columnas predefinido
    private GameCell[][] cells;      // Matriz para las celdas del tablero
    private int remainingTime; // Tiempo restante en segundos
    private JLabel timerLabel; // Para mostrar el tiempo restante
    private boolean shovelMode;    // Indica si el jugador está en modo pala (para eliminar plantas)
    private PauseMenu pauseMenu;   // Menú de pausa
    private boolean isPaused = false; // Indica si el juego está en pausa
    private Timer gameTimer;       // Temporizador para actualizar el tiempo restante y los soles
    private Timer zombieSpawnTimer; // Temporizador para generar zombis periódicamente

    /**
     * Constructor de la clase ScreenGame.
     * @param app Referencia a la clase principal MainApp.
     * @param modeGame Modo de juego seleccionado.
     */

    public ScreenGame(MainApp app,String modeGame ) {
        this.app = app; // Recibir referencia de MainApp
        this.board = Board.getBoard(); // Obtener instancia del tablero
        this.cells = new GameCell[ROWS][COLS]; // Inicializar matriz de celdas
        this.shovelMode = false;
        prepareElements();
        prepareActions();
        game();
    }


    /**
     * Configura los elementos gráficos del juego, incluyendo el tablero, los paneles, y los temporizadores.
     */
    private void prepareElements() {
        setTitle("Poob vs Zombies"); // Título de la ventana
        setSize(1000, 600);          // Dimensiones de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra el programa al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setLayout(new BorderLayout()); // Usa un BorderLayout para la disposición de componentes
        remainingTime = 10 * 60;    // Tiempo total del juego (10 minutos en segundos)

        // Crear paneles principales
        JPanel centerPanel = createGameBoard(); // Tablero central del juego
        JPanel header = createHeader();         // Encabezado con información del juego
        JPanel leftPanel = createLeftGridPanel(); // Panel izquierdo para herramientas y selecciones
        JPanel rightPanel = createRightPanel(); // Panel derecho para estadísticas y controles

        // Agregar los paneles a la ventana
        add(header, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // Configura el temporizador principal del juego
        gameTimer = new Timer(1000, e -> {
            if (!isPaused) { // Solo se actualiza si el juego no está en pausa
                updateSunsCounter(); // Actualiza el contador de soles
                updateTimer();       // Actualiza el temporizador restante
            }
        });
        gameTimer.start(); // Inicia el temporizador
    }

    /**
     * Configura las acciones de las celdas del tablero, permitiendo interacción con el jugador.
     */
    public void prepareActions() {
        // Configura que cada celda del tablero responda al clic del jugador
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                GameCell cell = cells[row][col]; // Obtiene la celda actual
                cell.addActionListener(e -> handleCellClick(cell)); // Asocia el evento de clic con el manejo correspondiente
            }
        }
    }


    // acciones que deben realizarse cuando el usuario hace clic en una celda
    private void handleCellClick(GameCell cell) {
        if (shovelMode) {
            try {
                board.shovel(cell.getRow(), cell.getColumn());
                cell.removePlant();
                shovelMode = false; // Desactivar modo pala después de usar
                setCursor(Cursor.getDefaultCursor()); // Restaurar cursor
            } catch (PoobVSZombiesExeption exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        } else if (selectedPlant != null) {
            try {
                board.addPlant(selectedPlant, cell.getRow(), cell.getColumn());
                cell.addPlant(selectedPlant);
                cell.repaint();
                updateSunsCounter();
                
                // Restablecer el cursor al cursor predeterminado
                setCursor(Cursor.getDefaultCursor());
                
                // Limpiar la selección de planta
                selectedPlant = null;
            } catch (PoobVSZombiesExeption exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Seleccione una planta primero o use la pala.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE);
        }
    }

/**
     * Crea y configura el encabezado de la ventana principal del juego.
     * Este encabezado incluye tres componentes principales:
     * - Panel de soles (a la izquierda).
     * - Fondo decorativo (en el centro).
     * - Botón de pausa (a la derecha).
     * @return JPanel configurado como encabezado.
     */
    private JPanel createHeader() {
        // Crear un panel con un diseño BorderLayout
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(10, 120)); // Altura fija
        header.setBackground(Color.GRAY); // Fondo gris

        // Agregar componentes al header
        header.add(createSunPanel(), BorderLayout.WEST); // Panel de soles a la izquierda
        header.add(createBackgroundPanel(), BorderLayout.CENTER); // Fondo decorativo al centro
        header.add(createPauseButton(), BorderLayout.EAST); // Botón de pausa a la derecha

        return header;
    }

    /**
     * Crea un botón de pausa para detener el juego.
     * Al hacer clic en el botón, se muestra un menú de pausa, y el juego se detiene hasta que el jugador decide continuar.
     * @return JButton configurado como botón de pausa.
     */
    private JButton createPauseButton() {
        JButton pauseButton = new JButton(); // Crear botón vacío
        pauseButton.setFocusPainted(false); // Deshabilitar enfoque visual
        pauseButton.setBorderPainted(false); // Deshabilitar bordes
        pauseButton.setContentAreaFilled(false); // Sin fondo visible
    
        // Configurar ícono del botón
        ImageIcon icon = new ImageIcon("images/PauseButton.png"); // Cargar la imagen del botón
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Escalar la imagen
        pauseButton.setIcon(new ImageIcon(scaledImage)); // Establecer el ícono escalado
        pauseButton.setPreferredSize(new Dimension(90, 110)); // Tamaño preferido del botón
    
        // Agregar acción al botón
        pauseButton.addActionListener(e -> {
            isPaused = true; // Pausar el juego
            PauseMenu pauseMenu = new PauseMenu(this); // Crear menú de pausa
            pauseMenu.setVisible(true); // Mostrar menú de pausa
            
            // Reanudar el juego si el jugador elige continuar
            if (pauseMenu.wasGameContinued()) {
                isPaused = false; 
            }
        });
    
        return pauseButton;
    }    

    /**
     * Crea un panel decorativo para mostrar el contador de soles.
     * Incluye un fondo visual personalizado y un JLabel con la cantidad de soles disponibles.
     * @return JPanel configurado como panel de soles.
     */
    private JPanel createSunPanel() {
        // Crear un panel con un fondo personalizado
        JPanel sunPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("images/TableroSol.png"); // Imagen de fondo
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this); // Dibujar fondo
            }
        };

        sunPanel.setPreferredSize(new Dimension(100, 50)); // Tamaño preferido
        sunPanel.setOpaque(false); // Fondo transparente

        // Crear un JLabel para mostrar los soles
        sunsLabel = new JLabel("Soles: " + board.getSuns()); // Texto inicial con los soles del tablero
        sunsLabel.setBounds(10, 300, 100, 50); // Posición y tamaño
        sunsLabel.setForeground(Color.YELLOW); // Color del texto
        sunsLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente y estilo
        sunPanel.add(sunsLabel); // Agregar etiqueta al panel

        return sunPanel;
    }
/**
     * Crea un panel para mostrar el temporizador del juego.
     * El temporizador indica el tiempo restante en el juego y se actualiza dinámicamente.
     * @return JPanel configurado como panel del temporizador.
     */
    private JPanel createTimerPanel() {
        JPanel timerPanel = new JPanel();
        timerPanel.setOpaque(false); // Fondo transparente
    
        // Crear etiqueta del temporizador con formato inicial
        timerLabel = new JLabel(formatTime(remainingTime));
        timerLabel.setForeground(Color.WHITE); // Color de texto blanco
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente personalizada
        timerPanel.add(timerLabel); // Agregar etiqueta al panel
    
        return timerPanel;
    }

    /**
     * Crea un panel decorativo de fondo para los botones de selección de plantas y herramientas.
     * Incluye botones para cada tipo de planta y un botón de pala para eliminar plantas.
     * @return JPanel configurado como panel de fondo.
     */
    private JPanel createBackgroundPanel() {
        // Crear panel con un fondo personalizado
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("images/TableroPlantas.png"); // Imagen de fondo
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this); // Dibujar fondo
            }
        };

        backgroundPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Diseño con alineación a la izquierda
        backgroundPanel.setOpaque(false); // Fondo transparente

        // Configurar datos de cada planta
        String[][] plantData = {
            {"SunFlower", "images/cartaSunFlower.jpg", "images/SunFlowerCursor.png"},
            {"PeasShooter", "images/cartaPeasShooter.jpg", "images/PeasShooterCursor.png"},
            {"WallNut", "images/cartaWallNut.jpg", "images/WallNutCursor.png"},
            {"PotatoMine", "images/cartaPotatoMine.jpg", "images/PotatoMineCursor.png"},
            {"EciPlant", "images/cartaEciPlant.jpg", "images/EciPlantCursor.png"}
        };

        // Crear y agregar botones de plantas al panel
        for (String[] data : plantData) {
            JButton plantButton = createPlantButton(data[0], data[1], data[2]);
            backgroundPanel.add(plantButton);
        }

        // Crear y agregar el botón de la pala
        JButton shovelButton = createShovelButton();
        shovelButton.setPreferredSize(new Dimension(80, 100)); // Tamaño del botón
        backgroundPanel.add(shovelButton);

        return backgroundPanel;
    }

    /**
     * Crea un botón de pala para activar el modo de eliminación de plantas.
     * Al hacer clic en el botón, el cursor del mouse se cambia por una pala y se habilita el modo de eliminación.
     * @return JButton configurado como botón de pala.
     */
    private JButton createShovelButton() {
        JButton shovelButton = new JButton();
        shovelButton.setFocusPainted(false); // Sin enfoque visual
        shovelButton.setBorderPainted(false); // Sin bordes
        shovelButton.setContentAreaFilled(false); // Sin fondo visible

        // Configurar el ícono del botón de pala
        ImageIcon icon = new ImageIcon("images/Shovel.png");
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Escalar imagen
        shovelButton.setIcon(new ImageIcon(scaledImage)); // Establecer ícono

        shovelButton.addActionListener(e -> {
            shovelMode = true; // Activar modo de pala
            selectedPlant = null; // Desactivar selección de planta

            // Cambiar el cursor del mouse por una pala
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image shovelImage = toolkit.getImage("images/ShovelCursor.png");
            Cursor customCursor = toolkit.createCustomCursor(
                shovelImage,
                new Point(0, 0), // Punto activo del cursor
                "ShovelCursor"
            );
            setCursor(customCursor); // Establecer el cursor personalizado
        });

        return shovelButton;
    }

    /**
     * Actualiza el temporizador del juego.
     * Decrementa el tiempo restante y actualiza la etiqueta correspondiente. Si el tiempo llega a 0, finaliza el juego.
     */
    private void updateTimer() {
        if (remainingTime > 0) {
            remainingTime--; // Decrementar el tiempo
            timerLabel.setText(formatTime(remainingTime)); // Actualizar etiqueta del temporizador
        } else {
            // Mostrar mensaje al finalizar el tiempo
            JOptionPane.showMessageDialog(this, "¡El tiempo se acabó!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Salir del juego o manejar el fin del juego
        }
    }

    /**
     * Formatea el tiempo restante en minutos y segundos (MM:SS).
     * @param seconds Tiempo restante en segundos.
     * @return Cadena con el tiempo formateado.
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60; // Calcular minutos
        int secs = seconds % 60;    // Calcular segundos
        return String.format("%02d:%02d", minutes, secs); // Formato MM:SS
    }
/**
     * Crea el panel derecho del juego, que contiene la imagen de la carretera y el temporizador.
     * Este panel representa el lugar desde donde se genera el ataque de los zombies.
     * @return JPanel configurado como panel derecho.
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 0)); // Tamaño del panel
        rightPanel.setBackground(Color.DARK_GRAY); // Fondo oscuro

        // Imagen de la carretera
        ImageIcon roadIcon = new ImageIcon("images/Carretera.png");
        Image scaledImage = roadIcon.getImage().getScaledInstance(200, 600, Image.SCALE_DEFAULT);
        JLabel roadLabel = new JLabel(new ImageIcon(scaledImage));

        // Panel del temporizador debajo de la carretera
        JPanel timerPanel = createTimerPanel();
        timerPanel.setPreferredSize(new Dimension(200, 50));

        // Agregar componentes al panel
        rightPanel.add(roadLabel, BorderLayout.CENTER);
        rightPanel.add(timerPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    /**
     * Crea el panel izquierdo del juego, que contiene la imagen de la casa de Dave.
     * Este panel representa el lugar desde donde el jugador planta las defensas.
     * @return JPanel configurado como panel izquierdo.
     */
    private JPanel createLeftGridPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0)); // Tamaño del panel
        leftPanel.setBackground(Color.LIGHT_GRAY); // Fondo claro

        // Imagen de la casa de Dave
        ImageIcon davesHouseIcon = new ImageIcon("images/Dave'sHouse.png");
        Image scaledImage = davesHouseIcon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH);
        JLabel davesHouseLabel = new JLabel(new ImageIcon(scaledImage));

        // Agregar imagen al panel
        leftPanel.add(davesHouseLabel, BorderLayout.CENTER);

        return leftPanel;
    }

    /**
     * Crea un botón para representar cada planta en el panel de selección de plantas.
     * Permite seleccionar una planta y cambia el cursor a una imagen personalizada al seleccionarla.
     * @param plantType Tipo de planta (nombre de la planta).
     * @param imagePath Ruta de la imagen para el botón.
     * @param cursorImagePath Ruta de la imagen para el cursor personalizado.
     * @return JButton configurado como botón de planta.
     */
    private JButton createPlantButton(String plantType, String imagePath, String cursorImagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false); // Sin borde de enfoque
        button.setBorderPainted(false); // Sin bordes visibles
        button.setContentAreaFilled(false); // Sin fondo visible

        // Cargar y configurar la imagen del botón
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setPreferredSize(new Dimension(90, 110)); // Tamaño del botón

        // Configurar acción al hacer clic
        button.addActionListener(e -> {
            shovelMode = false; // Desactiva el modo de pala
            selectedPlant = plantType; // Selecciona la planta

            // Cambiar el cursor a una imagen personalizada
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image cursorImage = toolkit.getImage(cursorImagePath);
            Cursor customCursor = toolkit.createCustomCursor(
                cursorImage,
                new Point(0, 0), // Punto activo del cursor
                plantType + "Cursor" // Nombre del cursor
            );
            setCursor(customCursor); // Establece el cursor personalizado
        });

        return button;
    }

    /**
     * Crea el tablero principal del juego como un panel de celdas (GameCells) organizadas en una cuadrícula.
     * Las celdas se conectan entre sí horizontalmente y, si están en la primera columna, se les asigna una podadora.
     * @return JPanel configurado como tablero principal.
     */
    private JPanel createGameBoard() {
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 2, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/Tablero.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // Fondo del tablero
            }
        };

        // Crear y conectar las celdas del tablero
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GameCell cell = new GameCell(i, j, this);
                cells[i][j] = cell;
                panel.add(cell);

                // Conectar celdas horizontalmente
                if (j > 0) {
                    cells[i][j - 1].setNext(cell); // Conectar a la derecha
                    cell.setPrevious(cells[i][j - 1]); // Conectar a la izquierda
                }

                // Asignar podadora a las celdas en la primera columna
                if (j == 0) {
                    cell.addLawnMower(i);
                }
            }
        }

        return panel;
    }

    /**
     * Actualiza el contador de soles en el juego.
     * Muestra la cantidad actual de soles en la etiqueta correspondiente.
     */
    private void updateSunsCounter() {
        sunsLabel.setText("Soles: " + board.getSuns());
    }

    /**
     * Configura y maneja el inicio del juego según el modo seleccionado.
     * Actualmente soporta un modo de juego individual.
     */
    private void game() {
        if (app.getGameMode().equals("OnePlayer")) {
            gameOnePlayer();
        }
    }
    /**
     * Modo de juego para un solo jugador. Controla la generación periódica de zombies en el tablero.
     * - Los zombies se generan en intervalos de tiempo iniciales de 10 segundos, luego aleatoriamente entre 10 y 15 segundos.
     * - El zombie es colocado en una posición aleatoria válida del tablero.
     */
    private void gameOnePlayer() {
        // Timer para generar zombies periódicamente
        Timer zombieSpawnTimer = new Timer(10 * 1000, e -> { // Intervalo inicial de 10 segundos
            if (!isPaused) {
                boolean zombieAgregado = false; // Bandera para verificar si el zombie fue agregado

                while (!zombieAgregado) { // Intentar agregar un zombie hasta lograrlo
                    try {
                        // Obtener información del zombie generado por la lógica del tablero
                        HashMap<String, int[]> coordenadaZombie = board.gameOnePlayer();

                        if (coordenadaZombie != null && !coordenadaZombie.isEmpty()) {
                            // Extraer tipo de zombie y su posición
                            Map.Entry<String, int[]> entry = coordenadaZombie.entrySet().iterator().next();
                            String zombieType = entry.getKey(); // Tipo de zombie
                            int[] position = entry.getValue();  // Coordenadas

                            // Validar si las coordenadas están dentro de los límites del tablero
                            if (position[0] >= 0 && position[0] < ROWS && position[1] >= 0 && position[1] < COLS) {
                                GameCell cell = cells[position[0]][position[1]];

                                // Intentar agregar el zombie; manejar excepciones si la celda está ocupada
                                cell.addZombie(zombieType);
                                cell.repaint(); // Refrescar la celda para mostrar cambios
                                zombieAgregado = true; // Zombie agregado correctamente
                            }
                        }
                    } catch (PoobVSZombiesExeption ex) {
                        // Capturar excepción si ocurre un error al agregar el zombie
                    }
                }
            }

            // Cambiar el intervalo del Timer a un valor aleatorio entre 10 y 15 segundos
            ((Timer) e.getSource()).setDelay((10 + new Random().nextInt(6)) * 1000); // 10-15 segundos
        });

        // Configuración del Timer
        zombieSpawnTimer.setInitialDelay(10 * 1000); // Primer zombie a los 10 segundos
        zombieSpawnTimer.setRepeats(true);          // Repetir indefinidamente
        zombieSpawnTimer.start();                   // Iniciar el Timer
    }

    /**
     * Pausa el juego. Detiene los temporizadores del juego y muestra el menú de pausa.
     */
    public void pauseGame() {
        if (!isPaused) {
            isPaused = true; // Establece el estado en pausa
            pauseMenu.setVisible(true); // Muestra el menú de pausa

            // Detener temporizadores
            if (gameTimer != null) gameTimer.stop();
            if (zombieSpawnTimer != null) zombieSpawnTimer.stop();
        }
    }

    /**
     * Reanuda el juego desde el estado de pausa. Reactiva los temporizadores del juego.
     */
    public void resumeGame() {
        if (isPaused) {
            isPaused = false; // Sale del estado de pausa
            pauseMenu.setVisible(false); // Oculta el menú de pausa

            // Reanudar temporizadores
            if (gameTimer != null) gameTimer.start();
            if (zombieSpawnTimer != null) zombieSpawnTimer.start();
        }
    }

    /**
     * Guarda la partida actual. Muestra un mensaje de confirmación al usuario.
     */
    public void saveGame() {
        JOptionPane.showMessageDialog(this, "Partida guardada.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Cierra la ventana actual, indicando el fin del juego o la salida de la partida.
     */
    public void exitGame() {
        dispose(); // Cierra la ventana
    }

    /**
     * Devuelve el estado actual del juego (pausado o no).
     * @return `true` si el juego está pausado, `false` en caso contrario.
     */
    public boolean getPauseGame() {
        return isPaused;
    }

    /**
     * Devuelve la referencia a la aplicación principal (MainApp).
     * @return Objeto `MainApp` asociado.
     */
    public MainApp getMainApp() {
        return app;
    }
    
}