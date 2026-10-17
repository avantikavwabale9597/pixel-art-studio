import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CanvasLogic{
    private BufferedImage image;
    private final int width = 800;
    private final int height = 600;

    public CanvasLogic(){
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearCanvas();
    }

    public void drawPixel(int x, int y, Color color){
        if(x>=0 && x<width&&y>=0&&y<height){
            image.setRGB(x, y,color.getRGB());
        }
    }

    public void clearCanvas(){
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,width,height);
        g.dispose();
    }

    public void drawLineDDA(int x1, int y1, int x2, int y2, Color color){

        drawPixel(x2,y2,color);
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2, Color color){

    }
    public void floodFill(int x, int y, Color targetColor){

    }

    public BufferedImage getImage(){
        return image;
    }
}