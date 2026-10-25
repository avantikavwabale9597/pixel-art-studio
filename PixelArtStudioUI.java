import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PixelArtStudioUI extends JFrame {
    
    private CanvasLogic logic;
    private DrawingPanel drawPanel;
    
    private JComboBox<String> toolCombo;
    private JComboBox<String> algoCombo;
    private Color currentColor = Color.BLACK;
    
    private int startX, startY;

    public PixelArtStudioUI() {
        logic = new CanvasLogic();

        setTitle("Retro Pixel Art Studio");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel toolbar = new JPanel();
        
        toolbar.add(new JLabel("Tool: "));
        String[] tools = {"Pencil", "Line", "Circle", "Fill"};
        toolCombo = new JComboBox<>(tools);
        
        toolbar.add(new JLabel("Algorithm: "));
        algoCombo = new JComboBox<>();
        updateAlgorithmDropdown();
        
        JButton btnClear = new JButton("Clear Canvas");

        toolCombo.addActionListener(e -> updateAlgorithmDropdown());
        
        btnClear.addActionListener(e -> {
            logic.clearCanvas();
            drawPanel.repaint();
        });

        toolbar.add(toolCombo);
        toolbar.add(algoCombo);
        toolbar.add(btnClear);
        add(toolbar, BorderLayout.NORTH);

        drawPanel = new DrawingPanel();
        add(drawPanel, BorderLayout.CENTER);
        setupMouseListeners();
    }

    private void updateAlgorithmDropdown() {
        algoCombo.removeAllItems();
        String selectedTool = (String) toolCombo.getSelectedItem();
        
        if (selectedTool.equals("Pencil")) {
            algoCombo.addItem("DDA");
        } else if (selectedTool.equals("Line")) {
            algoCombo.addItem("DDA");
            algoCombo.addItem("Bresenham");
        } else if (selectedTool.equals("Circle")) {
            algoCombo.addItem("DDA");
            algoCombo.addItem("Bresenham");
            algoCombo.addItem("Midpoint");
        } else if (selectedTool.equals("Fill")) {
            algoCombo.addItem("Flood Fill");
            algoCombo.addItem("Seed Fill");
            algoCombo.addItem("Scan Line Fill");
        }
    }

    private void setupMouseListeners() {
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                
                String tool = (String) toolCombo.getSelectedItem();
                String algo = (String) algoCombo.getSelectedItem();
                
                if (tool.equals("Fill")) {
                    logic.applyFill(startX, startY, currentColor, algo);
                    drawPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                String tool = (String) toolCombo.getSelectedItem();
                String algo = (String) algoCombo.getSelectedItem();
                
                if (tool.equals("Line")) {
                    logic.drawLine(startX, startY, e.getX(), e.getY(), currentColor, algo);
                    drawPanel.repaint();
                } else if (tool.equals("Circle")) {
                    // Calculate radius using distance formula
                    int radius = (int) Math.sqrt(Math.pow(e.getX() - startX, 2) + Math.pow(e.getY() - startY, 2));
                    logic.drawCircle(startX, startY, radius, currentColor, algo);
                    drawPanel.repaint();
                }
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                String tool = (String) toolCombo.getSelectedItem();
                if (tool.equals("Pencil")) {
                    // Pencil always uses DDA for smooth freehand
                    logic.drawLineDDA(startX, startY, e.getX(), e.getY(), currentColor);
                    startX = e.getX(); 
                    startY = e.getY();
                    drawPanel.repaint();
                }
            }
        });
    }

    class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(logic.getImage(), 0, 0, null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PixelArtStudioUI().setVisible(true);
        });
    }
}