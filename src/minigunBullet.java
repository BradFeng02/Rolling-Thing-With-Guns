import java.awt.*;
import java.awt.geom.AffineTransform;

public class minigunBullet extends Bullet {
    public static final int ROF = 6;
    public static final String NAME = "minigun";
    private double speed;
    private int age = 0;

    public minigunBullet(Color color, double x, double y, double angle,
            double speed) {
        super(color, x, y, angle);
        this.speed = speed;
    }

    public static void fire(java.util.List bullets, Color col, double x,
            double y, double angle, boolean WeaponSide, Guy guy) {
        if (WeaponSide) {
            bullets.add(new minigunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle),
                                          angle + (Math.random() * 2 - 1) / 8,
                                          Math.random() * 1.4 + 6));
            guy.addVx(.56 * Math.cos(angle));
            guy.addVy(.56 * Math.sin(angle));
        } else {
            bullets.add(new minigunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI +
                                                  (Math.random() * 2 - 1) / 8,
                                          Math.random() * 1.4 + 6));
            guy.addVx(-.56 * Math.cos(angle));
            guy.addVy(-.56 * Math.sin(angle));
        }
    }

    public int damage() {
        double prob = Math.random() - age * .0008;
        if (prob > .66) {
            return 2;
        }
        if (prob > .33) {
            return 1;
        }
        return 0;

    }

    public void act() {
        addX(-Math.cos(getAngle()) * speed);
        addY(-Math.sin(getAngle()) * speed);
        age++;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(getColor());
        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX(), getY());
        g2.fillOval((int) getX(), (int) getY() - 2, 5, 5);
        g2.setTransform(old);

//       g2.setStroke(new BasicStroke(1));
//        g2.drawLine((int) getRX(), 0, (int) getRX(), 690);
//       g2.drawLine(0, (int) getRY(), 1000, (int) getRY());
    }

    public double getRX() {
        return getX();
    }

    public double getRY() {
        return getY();
    }

    public int getType() {
        return 4;
    }
}
