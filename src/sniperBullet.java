import java.awt.*;
import java.awt.geom.AffineTransform;

public class sniperBullet extends Bullet {
    public static final int ROF = 81;
    public static final String NAME = "sniper";

    public sniperBullet(Color color, double x, double y, double angle) {
        super(color, x, y, angle);
    }

    public static void fire(java.util.List bullets, Color col, double x,
            double y, double angle, boolean WeaponSide) {
        if (WeaponSide) {
            bullets.add(new sniperBullet(col, (x - 45 * Math.cos(angle)),
                                         y - 45 * Math.sin(angle), angle));
        } else {
            bullets.add(new sniperBullet(col, (x + 45 * Math.cos(angle)),
                                         y + 45 * Math.sin(angle),
                                         angle + Math.PI));
        }
    }

    public int damage() {
        return 6;
    }

    public void act() {
        addX(-Math.cos(getAngle()) * 19);//19
        addY(-Math.sin(getAngle()) * 19);//
    }

    public void draw(Graphics2D g2) {
        g2.setColor(getColor());

        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX(), getY());
        g2.fillOval((int) (getX() - 20), (int) (getY() - 3), 40, 6);
        g2.fillRect((int) getX(), (int) getY() - 3, 30, 6);
        g2.setTransform(old);

//        g2.setStroke(new BasicStroke(1));
//        g2.drawLine((int) getRX(),0,(int) getRX(),690);
//        g2.drawLine(0,(int) getRY(),1000,(int) getRY());
    }

    public double getRX() {
        return getX() - 20 * Math.cos(getAngle());
    }

    public double getRY() {
        return getY() - 20 * Math.sin(getAngle());
    }

    public int getType() {
        return 1;
    }
}
