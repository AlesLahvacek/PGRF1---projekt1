package graphics.rasterizer;

import graphics.Raster;
import model.Line;

public class TrivialLineRasterizer implements LineRasterizer {

    private final Raster<Integer> raster;

    public TrivialLineRasterizer(Raster<Integer> raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        rasterize(line.getPoint1().getX(), line.getPoint2().getX(), line.getPoint1().getY(), line.getPoint2().getY(), line.getColour());
    }

    private void rasterize(int x1, int x2, int y1, int y2, int colour){
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (Math.abs(dx) >= Math.abs(dy)) {
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }

            if (x1 == x2) {
                raster.setPixel(x1, y1, colour);
                return;
            }

            for (int x = x1; x <= x2; x++) {
                float y = y1 + (y2 - y1) * (x - x1) / (float) (x2 - x1);
                raster.setPixel(x, Math.round(y), colour);
            }
        } else {
            if (y1 > y2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
                temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++) {
                float x = x1 + (x2 - x1) * (y - y1) / (float) (y2 - y1);
                raster.setPixel(Math.round(x), y, colour);
            }
        }
    }

}
