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
         int dx = x2-x1;
         int dy = y2-y1;

         int steps = Math.max(Math.abs(dx), Math.abs(dy));

         if (steps==0){
            drawPixel(x1, y1, color);
            return;
         }

         float xIncrement = (float) dx /steps;
         float yIncrement = (float) dy / steps;

         float currentX = x1;
         float currentY = y1;

         for(int i =0;i<=steps;i++){
            drawPixel(Math.round(currentX), Math.round(currentY), color);
            currentX += xIncrement;
            currentY += yIncrement;
         }
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2, Color color){
        int dx = Math.abs(x2-x1);
        int dy = Math.abs(y2-y1);

        int sx = (x1<x2)? 1 : -1;
        int sy = (y1<y2)?1:-1;

        int err = dx-dy;
        int e2;

        while(true){
            drawPixel(x1,y1,color);

            if(x1==x2&&y1==y1){
                break;
            }
            e2=2*err;

            if(e2>-dy){
                err -=dy;
                x1+=sx;
            }
            if(e2<dx){
                err+=dx;
                y1+=sy;
            }
        }
    }
    public void floodFill(int x, int y, Color targetColor){

    }

    public BufferedImage getImage(){
        return image;
    }
}