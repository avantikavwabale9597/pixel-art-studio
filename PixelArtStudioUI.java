import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PixelArtStudioUI extends JFrame {
    private CanvasLogic logic;
    private String currentTool = "PENCIL";
    private Color currentColor = Color.BLACK;
    private DrawingPanel drawPanel;
    private int startX, startY;

    public PixelArtStudioUI(){
        logic = new CanvasLogic();

        setTitle("Retro Pixel Art Studio");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel toolbar = new JPanel();
        JButton btnPencil = new JButton("Pencil (DDA)");
        JButton btnLine = new JButton ("Line (Bresenham)");
        JButton btnFill = new JButton("Bucket (Flood Fill)");
        JButton btnClear = new JButton("Clear Canvas");

        btnPencil.addActionListener(e -> currentTool = "PENCIL");
        btnLine.addActionListener(e -> currentTool = "LINE");
        btnFill.addActionListener(e -> currentTool = "FILL");
        btnClear.addActionListener(e -> {
            logic.clearCanvas();
            drawPanel.repaint();
        });

        toolbar.add(btnPencil);
        toolbar.add(btnLine);
        toolbar.add(btnFill);
        toolbar.add(btnClear);
        add(toolbar, BorderLayout.NORTH);

        drawPanel = new DrawingPanel();
        add(drawPanel, BorderLayout.CENTER);

        setupMouseListeners();
    }

    private void setupMouseListeners(){
        drawPanel.addMouseListener(new MouseAdapter(){
            @Override 
            public void mousePressed(MouseEvent e){
                startX = e.getX();
                startY = e.getY();

                if(currentTool.equals("FILL")){
                    logic.floodFill(startX, startY, currentColor);
                    drawPanel.repaint();
                }
            }

            @Override 
            public void mouseReleased(MouseEvent e){
                if(currentTool.equals("LINE")){
                    logic.drawLineBresenham(startX, startY, e.getX(),e.getY(), currentColor);
                    drawPanel.repaint();
                }
            }
        });

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override 
            public void mouseDragged(MouseEvent e){
                if(currentTool.equals("PENCIL")){
                    logic.drawLineDDA(startX, startY, e.getX(),e.getY(),currentColor);
                    startX=e.getX();
                    startY=e.getY();
                    drawPanel.repaint();
                }
            }
        });
    }

    class DrawingPanel extends JPanel {
        @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(logic.getImage(),0,0,null);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            new PixelArtStudioUI().setVisible(true);
        });
    }
}