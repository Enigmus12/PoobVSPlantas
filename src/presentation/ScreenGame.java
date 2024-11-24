package presentation;

import domain.Board;

import javax.swing.*;
import java.awt.*;

public class ScreenGame extends JFrame {
    private Board board;
    private String selectedPlant;
    private MainApp app;
    private JPanel backgroundPanel;

    public ScreenGame(MainApp app) {
        this.app = app; // Recibir referencia de MainApp
        this.board = Board.getBoard(); // Obtener instancia del tablero
        prepareElements();
        prepareActions();
    }

    public void prepareElements() {
        setTitle("Game Layout");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar layout principal
        setLayout(new BorderLayout());

        // Crear paneles para cada región
        JPanel header = createPanelWithBackground(Color.GRAY, "Encabezado");
        JPanel footer = createPanelWithBackground(Color.LIGHT_GRAY, "Pie de Página");
        JPanel leftPanel = createLeftGridPanel();
        JPanel rightPanel = createPanelWithBackground(Color.DARK_GRAY, "Panel Derecho");
        JPanel centerPanel = createGridPanelWithBackground(5, 10); // Tablero 5x10 con fondo

        // Agregar paneles al JFrame
        add(header, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void prepareActions() {
        // Aquí puedes agregar los listeners relacionados con eventos generales de la pantalla
        // Por ejemplo, interacción con los botones del panel izquierdo
    }

    private JPanel createPanelWithBackground(Color color, String text) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrar el texto
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Texto blanco
        panel.add(label);
        return panel;
    }

    private JPanel createLeftGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 filas, 1 columna, espaciado de 5px
        gridPanel.setPreferredSize(new Dimension(150, 0)); // Ancho fijo para el panel izquierdo

        // Rutas de las imágenes
        String[] imagePaths = {
                "images/cartaSunFlower.jpg", // Primer botón (Sunflower)
                "images/image2.jpg",
                "images/image3.jpg",
                "images/image4.jpg",
                "images/image5.jpg"
        };

        // Agregar botones con imágenes redimensionadas al GridLayout
        for (int i = 0; i < imagePaths.length; i++) {
            JButton button = createButtonWithScaledImage(imagePaths[i]);

            // Listener para el primer botón (Sunflower)
            if (i == 0) {
                button.addActionListener(e -> {selectedPlant = "Sunflower";});
            }

            gridPanel.add(button); // Agregar botón al panel
        }

        return gridPanel;
    }

    private JButton createButtonWithScaledImage(String imagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false); // Eliminar borde de selección
        button.setBorderPainted(false); // Eliminar bordes visibles
        button.setContentAreaFilled(false); // Hacer transparente el fondo

        // Cargar la imagen y redimensionarla
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        button.setIcon(scaledIcon); // Establecer la imagen en el botón

        // Asegurar que la imagen se redimensiona dinámicamente al cambiar el tamaño del botón
        button.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = button.getWidth();
                int height = button.getHeight();
                Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(resizedImage));
            }
        });

        return button;
    }

    private JPanel createGridPanelWithBackground(int rows, int cols) {
        JPanel panel = new JPanel(new GridLayout(rows, cols, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar imagen de fondo
                ImageIcon backgroundImage = new ImageIcon("images/tablero.jpg");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Agregar botones a la cuadrícula
        for (int i = 0; i < rows * cols; i++) {
            JButton button = new JButton("Cell " + (i + 1));
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(true);
            panel.add(button);
        }

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenGame screenGame = new ScreenGame(null); // null porque no hay MainApp en este ejemplo
            screenGame.setVisible(true);
        });
    }
}
