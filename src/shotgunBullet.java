import java.awt.*;
import java.awt.geom.AffineTransform;

public class shotgunBullet extends Bullet {
    public static final int ROF = 81;
    public static final String NAME = "shotty";
    private int range;

    public shotgunBullet(Color color, double x, double y, double angle,
            int range) {
        super(color, x, y, angle);
        this.range = range;
    }

    public static void fire(java.util.List bullets, Color col, double x,
            double y, double angle, boolean WeaponSide, Guy guy) {
        if (WeaponSide) {
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle + .08,
                                          17));
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle - .08,
                                          17));
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle + .2,
                                          17));
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle - .2,
                                          17));
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle + .12,
                                          16));
            bullets.add(new shotgunBullet(col, (x - 45 * Math.cos(angle)),
                                          y - 45 * Math.sin(angle), angle - .12,
                                          16));
            guy.addVx(6 * Math.cos(angle));
            guy.addVy(6 * Math.sin(angle));
        } else {
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI + .08, 17));
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI - .08, 17));
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI + .2, 17));
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI - .2, 17));
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI + .12, 16));
            bullets.add(new shotgunBullet(col, (x + 45 * Math.cos(angle)),
                                          y + 45 * Math.sin(angle),
                                          angle + Math.PI - .12, 16));
            guy.addVx(-6 * Math.cos(angle));
            guy.addVy(-6 * Math.sin(angle));
        }
    }

    public int damage() {
        if (range >= 14) return 4;
        else if (range >= 10) return 3;
        else if (range >= 5) return 2;
        else return 1;
    }

    public void act() {
        addX(-Math.cos(getAngle()) * 20.2);//19
        addY(-Math.sin(getAngle()) * 20.2);
        range--;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(getColor());

        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX(), getY());
        g2.fillRect((int) (getX() - 4), (int) (getY() - 2.5), 8, 5);
        g2.setTransform(old);

//        g2.setStroke(new BasicStroke(1));
//        g2.drawLine((int) getRX(), 0, (int) getRX(), 690);
//        g2.drawLine(0, (int) getRY(), 1000, (int) getRY());
    }

    public double getRX() {
        if (range < 0) return 9999;
        return getX();
    }

    public double getRY() {
        return getY();
    }

    public int getType() {
        return 3;
    }

}
