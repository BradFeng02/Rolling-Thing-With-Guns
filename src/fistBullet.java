import java.awt.*;
import java.awt.geom.AffineTransform;

public class fistBullet extends Bullet {

    public static final int ROF = 20;
    public static final String NAME = "empty";
    private int decay = 10;
    private double x;
    private double y;

    public fistBullet(Color color, double x, double y, double angle) {
        super(color, x, y, angle);
        this.x = x;
        this.y = y;
    }

    public void act() {
        decay--;
    }

    public static void fire(java.util.List bullets, Color col, double x, double y,
            double angle) {
        bullets.add(new fistBullet(col, x - 45, y, angle));
        bullets.add(new fistBullet(col, x - 45, y, angle + Math.PI));
    }

    public int getType() {
        return -1;
    }

    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX() + 45, getY());
        g2.setColor(getColor());
        int h = 3;
        int[] xs = {(int) x, (int) x + h, (int) x + 3 * h, (int) x + 2 * h,
                (int) x + 4 * h, (int) x + 2 * h, (int) x + 3 * h, (int) x + h,
                (int) x, (int) x - h, (int) x - 3 * h, (int) x - 2 * h,
                (int) x - 4 * h, (int) x - 2 * h, (int) x - 3 * h, (int) x - h};
        int[] ys =
                {(int) y + 4 * h, (int) y + 2 * h, (int) y + 3 * h, (int) y + h,
                        (int) y, (int) y - h, (int) y - 3 * h, (int) y - 2 * h,
                        (int) y - 4 * h, (int) y - 2 * h, (int) y - 3 * h,
                        (int) y - h, (int) y, (int) y + h, (int) y + 3 * h,
                        (int) y + 2 * h};
        g2.fillPolygon(new Polygon(xs, ys, 16));

        g2.setTransform(old);
    }

    public double getRX() {
        if (decay <= 0) {
            return 9999;
        }
        if (decay < 10) {
            return 1025;
        }
        double angle = getAngle() - Math.PI;
        return getX() + Math.abs(Math.cos(angle / 2) * 90) - Math.abs(
                Math.sin(angle / 2) * Math.sin(angle) * 29);
    }

    public double getRY() {
        double angle = getAngle() - Math.PI;
        return getY() + Math.sin(angle) * 45;
    }

    public int damage() {
        return 1;
    }
}
