package presentation;
import javax.swing.*;

import domain.Board;
import domain.PoobVSZombiesExeption;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
/**
 * Clase `TwoPlayer` que representa la interfaz gráfica para el modo de juego de dos jugadores.
 * Contiene campos de texto para ingresar los nombres de los jugadores y botones para iniciar
 * el juego o regresar al menú principal. Actualmente, el modo de dos jugadores está en construcción.
 */
public class TwoPlayer extends JFrame {

    private JTextField userNameOnePlayer; // Campo de texto para el nombre del jugador uno
    private JTextField userNameTwoPlayer; // Campo de texto para el nombre del jugador dos
    private JButton play;                 // Botón para iniciar el juego
    private JButton back;                 // Botón para regresar al menú principal
    private JPanel backgroundPanel;       // Panel que contiene la interfaz gráfica
    private MainApp mainApp;              // Referencia al controlador principal de la aplicación
    private Board board;                  // Referencia al tablero de juego (singleton)

    /**
     * Constructor de la clase `TwoPlayer`.
     * Inicializa los componentes gráficos y define las acciones asociadas a los botones.
     *
     * @param app Referencia al controlador principal de la aplicación.
     */
    public TwoPlayer(MainApp app) {
        board = Board.getBoard(); // Obtener la instancia del tablero (singleton)
        this.mainApp = app;       // Asignar el controlador principal
        prepareElements();        // Configurar elementos gráficos
        prepareActions();         // Configurar acciones para los botones
    }

    /**
     * Configura un botón para hacerlo transparente.
     *
     * @param button Botón que será configurado como transparente.
     */
    private void makeButtonTransparent(JButton button) {
        button.setOpaque(false);             // Elimina el fondo del botón
        button.setContentAreaFilled(false);  // Elimina el área de relleno del botón
        button.setBorderPainted(false);      // Elimina el borde del botón
    }

    /**
     * Configura los elementos gráficos de la ventana.
     * Establece el fondo, botones y campos de texto.
     */
    public void prepareElements() {
        setTitle("Two player");          // Título de la ventana
        setSize(800, 600);               // Tamaño inicial de la ventana
        setLocationRelativeTo(null);     // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un panel personalizado para el fondo
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/twoPlayer.png"); // Cargar imagen de fondo
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // Dibujar imagen de fondo
            }
        };
        backgroundPanel.setLayout(null); // Usar posicionamiento absoluto

        // Crear campos de texto y botones
        userNameOnePlayer = new JTextField("Name player one"); // Campo para el nombre del jugador 1
        userNameTwoPlayer = new JTextField("Name player two"); // Campo para el nombre del jugador 2
        play = new JButton();  // Botón para iniciar el juego
        back = new JButton();  // Botón para regresar al menú principal

        // Configurar estilo de los campos de texto
        userNameOnePlayer.setOpaque(false);
        userNameOnePlayer.setBorder(null);
        userNameOnePlayer.setForeground(Color.WHITE);
        userNameOnePlayer.setFont(new Font("Arial", Font.BOLD, 16));

        userNameTwoPlayer.setOpaque(false);
        userNameTwoPlayer.setBorder(null);
        userNameTwoPlayer.setForeground(Color.WHITE);
        userNameTwoPlayer.setFont(new Font("Arial", Font.BOLD, 16));

        // Hacer botones transparentes
        makeButtonTransparent(back);
        makeButtonTransparent(play);

        // Agregar componentes al panel de fondo
        backgroundPanel.add(userNameOnePlayer);
        backgroundPanel.add(userNameTwoPlayer);
        backgroundPanel.add(play);
        backgroundPanel.add(back);

        // Establecer el panel de fondo como contenido principal de la ventana
        setContentPane(backgroundPanel);

        // Reposicionar los botones al cambiar el tamaño de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons(); // Recalcular posiciones al redimensionar la ventana
            }
        });

        // Posicionar los botones inicialmente
        repositionButtons();
    }

    /**
     * Configura las acciones para los botones "Play" y "Back".
     */
    public void prepareActions() {
        // Acción para regresar al menú principal
        back.addActionListener(e -> mainApp.showMainMenu());

        // Acción para mostrar un mensaje de funcionalidad en construcción al presionar "Play"
        play.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "La funcionalidad de dos jugadores está en construcción.\n¡Próximamente estará disponible!",
                "Funcionalidad en mantenimiento",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Calcula y ajusta la posición y tamaño de los botones y campos de texto
     * en función del tamaño actual de la ventana.
     */
    public void repositionButtons() {
        int panelWidth = backgroundPanel.getWidth();  // Ancho del panel
        int panelHeight = backgroundPanel.getHeight(); // Alto del panel

        // Calcular posiciones relativas de los elementos
        int userName1X = (int) (panelWidth * 0.125);
        int userName1Y = (int) (panelHeight * 0.315);
        int userName2X = (int) (panelWidth * 0.61);
        int userName2Y = (int) (panelHeight * 0.325);
        int backX = (int) (panelWidth * 0.015);
        int backY = (int) (panelHeight * 0.8);
        int playX = (int) (panelWidth * 0.4);
        int playY = (int) (panelHeight * 0.7);

        // Establecer posiciones y tamaños de los componentes
        userNameOnePlayer.setBounds(userName1X, userName1Y, 180, 40);
        userNameTwoPlayer.setBounds(userName2X, userName2Y, 180, 40);
        back.setBounds(backX, backY, 105, 30);
        play.setBounds(playX, playY, 200, 30);
    }
}
