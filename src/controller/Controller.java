package controller;

import view.Canvas;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import graphics.rasterizer.FinalPolygonRasterizer;
import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;

/**
 * Handles user input and controls the application flow related to the {@link Canvas}.
 * The controller coordinates input events, canvas operations, and rendering updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {

    private Line previewLine;

    private final Canvas canvas;
    private final LineRasterizer rasterizer;
    private final PolygonRasterizer polygonRasterizer;
    private final Polygon polygon;
    public List<Line> lines;

    private static final int LINE_COLOUR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOUR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygonRasterizer = new FinalPolygonRasterizer(rasterizer);
        this.polygon = new Polygon(new ArrayList<>());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        // rasterizer.rasterize(new Line(new Point(50, 50), new Point(300, 500), Color.WHITE.getRGB()));
        polygonRasterizer.rasterize(new Polygon(lines));
        // Obsluha vstupu z myši
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    finishPolygon();
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    startLine(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO: zobrazit preview čáru
            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO: stisknutí klávesy C -> smazání canvasu
            }
        });
    }

    public void render(){
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        canvas.repaint();
    }

    private void startLine(MouseEvent e){
        // vzít poslední bod polygonu
        // vzít getPoint(e) - aktuální bod
        // previewLine = new Line(startPoint, lastPoint, PREVIEW_COLOUR)
    }

    private void finishPolygon(){
        previewLine = null;
        render();
    }

    private Point getPoint(MouseEvent e){
        return new Point(e.getX(), e.getY());
    }

}