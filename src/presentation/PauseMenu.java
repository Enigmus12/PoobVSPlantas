package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseMenu extends JDialog {
    private ScreenGame parentGame;
    private boolean gameContinued = false;

    public PauseMenu(ScreenGame parent) {
        super(parent, "Menú de Pausa", true);
        this.parentGame = parent;

        // Configurar las propiedades del diálogo
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Crear panel principal con botones
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Botón de reanudar
        JButton resumeButton = createStyledButton("Reanudar Juego");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameContinued = true;
                dispose(); // Cerrar el menú de pausa
            }
        });
        buttonPanel.add(resumeButton);

        // Botón de guardar
        JButton saveButton = createStyledButton("Guardar Juego");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(PauseMenu.this, 
                    "La funcionalidad de guardar el juego aún no está implementada.", 
                    "Guardar Juego", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(saveButton);

        // Botón de salir
        JButton exitButton = createStyledButton("Salir al Menú Principal");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    PauseMenu.this,
                    "¿Estás seguro de que deseas salir al menú principal?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    parentGame.dispose(); // Cierra la ventana del juego
                    dispose(); // Cierra el menú de pausa

                    // Mostrar el Menú Principal
                    SwingUtilities.invokeLater(() -> {
                        MainMenu mainMenu = new MainMenu(parentGame.getMainApp()); // Pasa la referencia a MainApp
                        mainMenu.setVisible(true);
                    });
                }
            }
        });
        buttonPanel.add(exitButton);

        // Añadir fondo y estilo
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        add(backgroundPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    // Método para verificar si el juego fue continuado
    public boolean wasGameContinued() {
        return gameContinued;
    }
}
