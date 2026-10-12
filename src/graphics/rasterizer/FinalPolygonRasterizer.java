package graphics.rasterizer;

import java.util.List;

import graphics.Raster;
import model.Line;
import model.Polygon;

public class FinalPolygonRasterizer implements PolygonRasterizer {

    private final LineRasterizer lineRasterizer;



    public FinalPolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }



    @Override
    public void rasterize(Polygon polygon) {
        List<Line> lines = polygon.getLines();
        rasterizeLines(lines);

        if (lines.size() > 1) {
            Line last = lines.get(lines.size()-1);
            Line first = lines.get(0);
            lineRasterizer.rasterize(new Line(first.getPoint1(), last.getPoint2(), last.getColour()));
        }
    }

    private void rasterizeLines(List<Line> lines){
        for (Line line : lines) {
            lineRasterizer.rasterize(line);
        }
    }
    
}
