package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Clase `PauseMenu` que representa el menú de pausa del juego.
 * Este menú permite al jugador realizar acciones como reanudar el juego,
 * guardar el progreso (funcionalidad futura) o salir al menú principal.
 */
public class PauseMenu extends JDialog {

    private ScreenGame parentGame; // Referencia a la ventana principal del juego
    private boolean gameContinued = false; // Bandera para saber si el juego se reanudó

    /**
     * Constructor de la clase `PauseMenu`.
     * Configura el diálogo con las opciones del menú de pausa.
     *
     * @param parent Referencia al objeto `ScreenGame` que invoca el menú.
     */
    public PauseMenu(ScreenGame parent) {
        super(parent, "Menú de Pausa", true); // Crear un JDialog modal
        this.parentGame = parent; // Almacenar la referencia al juego principal

        // Configurar el diálogo
        setSize(400, 300); // Dimensiones del menú
        setLocationRelativeTo(parent); // Centrar respecto al juego
        setLayout(new BorderLayout()); // Establecer layout principal

        // Panel principal para los botones del menú
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // GridLayout para botones
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Espaciado interno

        // Botón para reanudar el juego
        JButton resumeButton = createStyledButton("Reanudar Juego");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameContinued = true; // Indicar que el juego continúa
                dispose(); // Cerrar el menú de pausa
            }
        });
        buttonPanel.add(resumeButton);

        // Botón para guardar el juego (funcionalidad futura)
        JButton saveButton = createStyledButton("Guardar Juego");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mensaje informando que la funcionalidad no está implementada
                JOptionPane.showMessageDialog(
                        PauseMenu.this,
                        "La funcionalidad de guardar el juego aún no está implementada.",
                        "Guardar Juego",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        buttonPanel.add(saveButton);

        // Botón para salir al menú principal
        JButton exitButton = createStyledButton("Salir al Menú Principal");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Confirmación antes de salir al menú principal
                int confirm = JOptionPane.showConfirmDialog(
                        PauseMenu.this,
                        "¿Estás seguro de que deseas salir al menú principal?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    parentGame.dispose(); // Cerrar la ventana principal del juego
                    dispose(); // Cerrar el menú de pausa

                    // Volver al Menú Principal
                    SwingUtilities.invokeLater(() -> {
                        MainMenu mainMenu = new MainMenu(parentGame.getMainApp()); // Crear nueva instancia del menú principal
                        mainMenu.setVisible(true); // Mostrar el menú principal
                    });
                }
            }
        });
        buttonPanel.add(exitButton);

        // Crear un panel de fondo para estilizar el menú
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150)); // Fondo semitransparente
                g.fillRect(0, 0, getWidth(), getHeight()); // Rellenar todo el panel
            }
        };
        backgroundPanel.setLayout(new BorderLayout()); // Layout para centrar los botones
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER); // Añadir el panel de botones

        // Añadir el panel de fondo al diálogo
        add(backgroundPanel);
    }

    /**
     * Crea un botón estilizado para el menú de pausa.
     *
     * @param text Texto a mostrar en el botón.
     * @return Botón estilizado con las propiedades definidas.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente personalizada
        button.setForeground(Color.WHITE); // Color del texto
        button.setBackground(new Color(70, 130, 180)); // Color de fondo
        button.setFocusPainted(false); // Eliminar borde de foco
        button.setPreferredSize(new Dimension(200, 50)); // Tamaño preferido
        return button;
    }

    /**
     * Verifica si el juego fue continuado desde el menú de pausa.
     *
     * @return `true` si el juego fue reanudado, `false` en caso contrario.
     */
    public boolean wasGameContinued() {
        return gameContinued;
    }
}
