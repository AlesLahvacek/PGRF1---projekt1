package graphics;
import java.awt.*;

public interface Raster<T> {
    
    void setPixel(int x, int y, T value);
    void clear(T value);
    void present(Graphics graphics);

    int getWidth();
    int getHeight();
}
