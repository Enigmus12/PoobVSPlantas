package presentation;

import javax.swing.*;

import domain.Board;
import domain.PoobVSZombiesExeption;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
/**
 * Clase `OnePlayer` que representa la interfaz gráfica para configurar el modo de juego de un jugador.
 * Incluye un campo de texto para ingresar el nombre del jugador y botones para iniciar el juego
 * o regresar al menú principal.
 */
public class OnePlayer extends JFrame {

    private JTextField userName;          // Campo de texto para ingresar el nombre del jugador
    private JPanel backgroundPanel;       // Panel que contiene la interfaz gráfica
    private JButton play;                 // Botón para iniciar el juego
    private JButton back;                 // Botón para regresar al menú principal
    private MainApp mainApp;              // Referencia al controlador principal de la aplicación
    private Board board;                  // Referencia al tablero de juego (singleton)

    /**
     * Constructor de la clase `OnePlayer`.
     * Inicializa los componentes gráficos y define las acciones asociadas a los botones.
     *
     * @param app Referencia al controlador principal de la aplicación.
     */
    public OnePlayer(MainApp app) {
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
     * Establece el fondo, los botones y el campo de texto.
     */
    public void prepareElements() {
        setTitle("One Player");           // Título de la ventana
        setSize(800, 600);                // Tamaño inicial de la ventana
        setLocationRelativeTo(null);      // Centrar la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear un panel personalizado para el fondo
        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("images/onePlayer.png"); // Cargar imagen de fondo
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this); // Dibujar imagen de fondo
            }
        };

        backgroundPanel.setLayout(null); // Usar layout absoluto
        setContentPane(backgroundPanel); // Establecer el panel como contenido principal

        // Crear el campo de texto y los botones
        userName = new JTextField("Enter your name here"); // Campo de texto con texto predeterminado
        back = new JButton(); // Botón para regresar al menú principal
        play = new JButton(); // Botón para iniciar el juego

        // Configurar estilo del campo de texto
        userName.setOpaque(false);
        userName.setBorder(null);
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Arial", Font.BOLD, 16));

        // Agregar los componentes al panel
        backgroundPanel.add(play);
        backgroundPanel.add(back);
        backgroundPanel.add(userName);

        // Hacer botones transparentes
        makeButtonTransparent(play);
        makeButtonTransparent(back);

        // Agregar listener para reposicionar dinámicamente los componentes al cambiar el tamaño de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionComponents(); // Recalcular posiciones al redimensionar la ventana
            }
        });

        repositionComponents(); // Posicionar inicialmente los componentes
    }

    /**
     * Reposiciona dinámicamente los componentes gráficos en función del tamaño actual de la ventana.
     */
    private void repositionComponents() {
        int panelWidth = backgroundPanel.getWidth();  // Ancho del panel
        int panelHeight = backgroundPanel.getHeight(); // Alto del panel

        // Calcular posiciones relativas de los elementos
        int userNameX = (int) (panelWidth * 0.7); // 70% del ancho
        int userNameY = (int) (panelHeight * 0.3);
        int backX = (int) (panelWidth * 0.015);
        int backY = (int) (panelHeight * 0.8);
        int playX = (int) (panelWidth * 0.7);
        int playY = (int) (panelHeight * 0.7);

        // Establecer posiciones y tamaños de los componentes
        userName.setBounds(userNameX, userNameY, 180, 40);
        back.setBounds(backX, backY, 75, 30);
        play.setBounds(playX, playY, 200, 30);
    }

    /**
     * Configura las acciones asociadas a los botones "Play" y "Back".
     */
    public void prepareActions() {
        // Acción para regresar al menú principal
        back.addActionListener(e -> mainApp.showMainMenu());

        // Acción para iniciar el juego
        play.addActionListener(e -> {
            String name = userName.getText().trim(); // Obtener el nombre ingresado, eliminando espacios en blanco

            try {
                // Validar el nombre del jugador usando la lógica del tablero
                board.validateNameOnePlayer(name);
                mainApp.showScreenGame("OnePlayer"); // Mostrar la pantalla del juego

            } catch (PoobVSZombiesExeption exception) {
                // Mostrar un mensaje de error si ocurre una excepción de validación
                JOptionPane.showMessageDialog(this, exception.getMessage(),
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}
