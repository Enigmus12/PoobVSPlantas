package presentation;

import javax.swing.*;
import java.awt.*;

public class ScreenGame extends JFrame {
    
    public ScreenGame() {
        setTitle("Game Layout");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Usar BorderLayout como layout principal
        setLayout(new BorderLayout());

        // Crear paneles para cada región
        JPanel header = createPanelWithBackground(Color.GRAY, "Encabezado");
        JPanel footer = createPanelWithBackground(Color.LIGHT_GRAY, "Pie de Página");
        JPanel leftPanel = createLeftGridPanel();
        JPanel rightPanel = createPanelWithBackground(Color.DARK_GRAY, "Panel Derecho");
        JPanel centerPanel = createGridPanelWithBackground(5, 10); // Tablero 5x5 con fondo en el centro

        // Agregar paneles al BorderLayout
        add(header, BorderLayout.NORTH);
        add(footer, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Crea un panel con un color de fondo y texto centrado.
     */
    private JPanel createPanelWithBackground(Color color, String text) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrar el texto
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Texto blanco
        panel.add(label);
        return panel;
    }

    /**
     * Crea un panel con una imagen de fondo.
     */
    private JPanel createPanelWithImage(String imagePath) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dibujar imagen de fondo
                ImageIcon backgroundImage = new ImageIcon(imagePath);
                g.drawImage(backgroundImage.getImage(), 0, 0, 2000, getHeight(), this);
            }
        };
    }

    /**
     * Crea un panel izquierdo con GridLayout (5x1) que contiene botones con imágenes.
     */
    private JPanel createLeftGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(5, 1, 5, 5)); // 5 filas, 1 columna, espaciado de 5px
        gridPanel.setPreferredSize(new Dimension(150, 0)); // Ancho fijo para el panel izquierdo

        // Rutas de las imágenes
        String[] imagePaths = {
                "images/cartaSunFlower.jpg",
                "images/image2.jpg",
                "images/image3.jpg",
                "images/image4.jpg",
                "images/image5.jpg"
        };

        // Agregar botones con imágenes redimensionadas al GridLayout
        for (String path : imagePaths) {
            JButton button = createButtonWithScaledImage(path);
            gridPanel.add(button); // Agregar botón al panel
        }

        return gridPanel;
    }

    /**
     * Crea un JButton con una imagen escalada dinámicamente.
     *
     * @param imagePath Ruta de la imagen
     * @return JButton con imagen redimensionada
     */
    private JButton createButtonWithScaledImage(String imagePath) {
        JButton button = new JButton();
        button.setFocusPainted(false); // Eliminar borde de selección
        button.setBorderPainted(false); // Eliminar bordes visibles
        button.setContentAreaFilled(false); // Hacer transparente el fondo

        // Cargar la imagen y redimensionarla
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Tamaño inicial
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

    /**
     * Crea un panel con un GridLayout y un fondo personalizado.
     */
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

        // Agregar componentes a la cuadrícula (por ejemplo, botones)
        for (int i = 0; i < rows * cols; i++) {
            JButton button = new JButton("Cell " + (i + 1));
            button.setOpaque(false); // Hacer el botón transparente
            button.setContentAreaFilled(false); // Eliminar relleno
            button.setBorderPainted(true); // Mostrar bordes
            panel.add(button);
        }
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenGame layout = new ScreenGame();
            layout.setVisible(true);
        });
    }
}
