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
        this.lines = new ArrayList<>();
        this.polygon = new Polygon(lines);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        // Obsluha vstupu z myši
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    finishPolygon();
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    changePoint(e);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    startLine(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && previewLine != null) {
                    lines.add(new Line(
                            previewLine.getPoint1(),
                            previewLine.getPoint2(),
                            LINE_COLOUR
                    ));
                    previewLine = null;
                    render();
                }
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (previewLine != null) {
                    previewLine = new Line(
                            previewLine.getPoint1(),
                            getPoint(e),
                            PREVIEW_COLOUR
                    );
                    render();
                }
            }
        });

        // Obsluha vstupu z klávesnice
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    lines.clear();
                    previewLine = null;
                    render();
                }

                if (e.getKeyCode() == KeyEvent.VK_E) {

                }
            }
        });
    }

    public void render(){
        canvas.clear();
        polygonRasterizer.rasterize(polygon);
        if (previewLine != null) {
            rasterizer.rasterize(previewLine);
        }
        canvas.repaint();
    }

    private void startLine(MouseEvent e){
        Point currentPoint = getPoint(e);

        if (lines.isEmpty()) {
            rasterizer.rasterize(new Line(currentPoint, currentPoint, LINE_COLOUR));
            lines.add(new Line(currentPoint, currentPoint, LINE_COLOUR));
            canvas.repaint();
            return;
        }

        Point startPoint = lines.get(lines.size() - 1).getPoint2();
        previewLine = new Line(startPoint, currentPoint, PREVIEW_COLOUR);
        render();
    }

    private void finishPolygon(){
        previewLine = null;
        render();
    }

    private void changePoint(MouseEvent e){
        if (lines.isEmpty()) {
            return;
        }

        Point newPoint = getPoint(e);
        Point nearestPoint = null;
        long nearestDistance = Long.MAX_VALUE;

        for (Line line : lines) {
            Point[] points = {line.getPoint1(), line.getPoint2()};
            for (Point point : points) {
                long dx = point.getX() - newPoint.getX();
                long dy = point.getY() - newPoint.getY();
                long distance = dx * dx + dy * dy;

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPoint = point;
                }
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            Point point1 = line.getPoint1();
            Point point2 = line.getPoint2();

            if (point1 == nearestPoint) {
                point1 = newPoint;
            }

            if (point2 == nearestPoint) {
                point2 = newPoint;
            }

            lines.set(i, new Line(point1, point2, line.getColour()));
        }

        render();
    }

    private Point getPoint(MouseEvent e){
        return new Point(e.getX(), e.getY());
    }

}