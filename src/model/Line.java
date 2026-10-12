package model;

public class Line {
    
    private final Point point1,point2;
    private final int colour;
    
    public Line(Point point1, Point point2, int colour) {
        this.point1 = point1;
        this.point2 = point2;
        this.colour = colour;
    }
    
    // Getters
    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public int getColour() {
        return colour;
    }

}
