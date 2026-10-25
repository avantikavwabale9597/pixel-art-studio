import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.util.Stack;

public class CanvasLogic {
    private BufferedImage image;
    private final int width = 850;
    private final int height = 650;

    public CanvasLogic() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        clearCanvas();
    }

    public void drawPixel(int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            image.setRGB(x, y, color.getRGB());
        }
    }

    public void clearCanvas() {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }


    public void drawLine(int x1, int y1, int x2, int y2, Color color, String algorithm) {
        if (algorithm.equals("DDA")) {
            drawLineDDA(x1, y1, x2, y2, color);
        } else if (algorithm.equals("Bresenham")) {
            drawLineBresenham(x1, y1, x2, y2, color);
        }
    }

    public void drawCircle(int xc, int yc, int r, Color color, String algorithm) {
        if (algorithm.equals("DDA")) {
            drawCircleDDA(xc, yc, r, color);
        } else if (algorithm.equals("Bresenham")) {
            drawCircleBresenham(xc, yc, r, color);
        } else if (algorithm.equals("Midpoint")) {
            drawCircleMidpoint(xc, yc, r, color);
        }
    }

    public void applyFill(int x, int y, Color color, String algorithm) {
        if (algorithm.equals("Flood Fill") || algorithm.equals("Seed Fill")) {
            floodFill(x, y, color); 
        } else if (algorithm.equals("Scan Line Fill")) {
            scanLineFill(x, y, color);
        }
    }


    public void drawLineDDA(int x1, int y1, int x2, int y2, Color color) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        if (steps == 0) { drawPixel(x1, y1, color); return; }
        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;
        float currentX = x1;
        float currentY = y1;
        for (int i = 0; i <= steps; i++) {
            drawPixel(Math.round(currentX), Math.round(currentY), color);
            currentX += xIncrement;
            currentY += yIncrement;
        }
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        int e2;
        while (true) {
            drawPixel(x1, y1, color);
            if (x1 == x2 && y1 == y2) break;
            e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 < dx) { err += dx; y1 += sy; }
        }
    }


    
    public void drawCircleDDA(int xc, int yc, int r, Color color) {
        
        int n = 0;
        while ((1 << n) < r) {
            n++;
        }
        float epsilon = 1.0f / (1 << n);
        float x = r;
        float y = 0;
        float startX = x;
        float startY = y;
       do {
            float x2 = x - epsilon * y;
            float y2 = y + epsilon * x2; 
            
            drawPixel(xc + Math.round(x2), yc + Math.round(y2), color);
            
            x = x2;
            y = y2;

        } while ((Math.abs(x - startX) > epsilon) || (Math.abs(y - startY) > epsilon));
    }
    

    public void drawCircleBresenham(int xc, int yc, int r, Color color) {
       int x = 0;
        int y = r;
        int d = 3 - 2 * r;

        while (x <= y) {
            drawPixel(xc + x, yc + y, color);
            drawPixel(xc - x, yc + y, color);
            drawPixel(xc + x, yc - y, color);
            drawPixel(xc - x, yc - y, color);
            drawPixel(xc + y, yc + x, color);
            drawPixel(xc - y, yc + x, color);
            drawPixel(xc + y, yc - x, color);
            drawPixel(xc - y, yc - x, color);

            if (d < 0) {
                d = d + 4 * x + 6;
            } else {
                y--;
                d = d + 4 * (x - y) + 10;
            }
            x++;
        }
    }

    public void drawCircleMidpoint(int xc, int yc, int r, Color color) {
        int x = r;
        int  y =0;
        int p = 1-r;

        while(x>=y){
            drawPixel(xc + x, yc + y, color);
            drawPixel(xc + y, yc + x, color);
            drawPixel(xc - y, yc + x, color);
            drawPixel(xc - x, yc + y, color);
            drawPixel(xc - x, yc - y, color);
            drawPixel(xc - y, yc - x, color);
            drawPixel(xc + y, yc - x, color);
            drawPixel(xc + x, yc - y, color);

            y++;

            if (p <= 0) {
                p = p + 2 * y + 1;
            } else {
                x--;
                p = p + 2 * y - 2 * x + 1;
            }
        }
        
    }


    public void floodFill(int x, int y, Color targetColor) {
        int targetRGB = targetColor.getRGB();
        int sourceRGB = image.getRGB(x, y);
        if (sourceRGB == targetRGB) return;
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));
        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int px = p.x;
            int py = p.y;
            if (px >= 0 && px < width && py >= 0 && py < height) {
                if (image.getRGB(px, py) == sourceRGB) {
                    image.setRGB(px, py, targetRGB);
                    stack.push(new Point(px + 1, py));
                    stack.push(new Point(px - 1, py));
                    stack.push(new Point(px, py + 1));
                    stack.push(new Point(px, py - 1));
                }
            }
        }
    }

    public void scanLineFill(int x, int y, Color targetColor) {
       
        int targetRGB = targetColor.getRGB();
        int sourceRGB = image.getRGB(x, y);
        
        if (sourceRGB == targetRGB) return;

        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int cx = p.x;
            int cy = p.y;

            while (cx >= 0 && image.getRGB(cx, cy) == sourceRGB) {
                cx--;
            }
            cx++;

            boolean spanAbove = false;
            boolean spanBelow = false;

            while (cx < width && image.getRGB(cx, cy) == sourceRGB) {
                image.setRGB(cx, cy, targetRGB);

                if (!spanAbove && cy > 0 && image.getRGB(cx, cy - 1) == sourceRGB) {
                    stack.push(new Point(cx, cy - 1));
                    spanAbove = true;
                } else if (spanAbove && cy > 0 && image.getRGB(cx, cy - 1) != sourceRGB) {
                    spanAbove = false;
                }

                if (!spanBelow && cy < height - 1 && image.getRGB(cx, cy + 1) == sourceRGB) {
                    stack.push(new Point(cx, cy + 1));
                    spanBelow = true;
                } else if (spanBelow && cy < height - 1 && image.getRGB(cx, cy + 1) != sourceRGB) {
                    spanBelow = false;
                }
                cx++;
            }
        }
    
    }

    public BufferedImage getImage() {
        return image;
    }
}