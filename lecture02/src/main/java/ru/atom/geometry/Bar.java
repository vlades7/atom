package ru.atom.geometry;

public class Bar implements Collider {
    private final Point point1;
    private final Point point2;

    private boolean checkPoint(Point p, Bar b) {
        return p.getX() >= b.point1.getX()
                && p.getY() >= b.point1.getY()
                && p.getX() <= b.point2.getX()
                && p.getY() <= b.point2.getY();
    }

    public Bar(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        if (this.point1.getX() > this.point2.getX()) {
            int tmp = this.point1.getX();
            this.point1.setX(this.point2.getX());
            this.point2.setX(tmp);
        }
        if (this.point1.getY() > this.point2.getY()) {
            int tmp = this.point1.getY();
            this.point1.setY(this.point2.getY());
            this.point2.setY(tmp);
        }
    }

    @Override
    public boolean isColliding(Collider other) {
        return this.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Bar) {
            Bar otherBar = (Bar) o;

            if (checkPoint(otherBar.point1, this)
                    || checkPoint(otherBar.point2, this)
                    || checkPoint(new Point(otherBar.point2.getX(), otherBar.point1.getY()), this)
                    || checkPoint(new Point(otherBar.point1.getX(), otherBar.point2.getY()), this))
                return true;
            else return otherBar.point2.getX() <= this.point2.getX()
                    && otherBar.point1.getX() >= this.point1.getX()
                    && otherBar.point2.getY() >= this.point2.getY()
                    && otherBar.point1.getY() <= this.point1.getY()
                    || otherBar.point2.getY() <= this.point2.getY()
                    && otherBar.point1.getY() >= this.point1.getY()
                    && otherBar.point2.getX() >= this.point2.getX()
                    && otherBar.point1.getX() <= this.point1.getX();
        } else if (o instanceof Point) {
            Point otherPoint = (Point) o;
            return checkPoint(otherPoint, this);
        }
        return false;
    }
}