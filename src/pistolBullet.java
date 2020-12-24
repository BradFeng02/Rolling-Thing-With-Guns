import java.awt.*;
import java.awt.geom.AffineTransform;

public class pistolBullet extends Bullet {
    public static final int ROF = 15;
    public static final String NAME = "dualies";

    public pistolBullet(Color color, double x, double y, double angle) {
        super(color, x, y, angle);
    }

    public static void fire(java.util.List bullets, Color col, double x,
            double y, double angle, boolean dualiesStartSide, Guy guy) {
        if (dualiesStartSide) {
            bullets.add(new pistolBullet(col, (x - 45 * Math.cos(angle)),
                                         y - 45 * Math.sin(angle), angle));
        } else {
            bullets.add(new pistolBullet(col, (x + 45 * Math.cos(angle)),
                                         y + 45 * Math.sin(angle),
                                         angle + Math.PI));
        }
        guy.toggleDualiesSide();
    }

    public int damage() {
        return 2;
    }

    public void act() {
        addX(-Math.cos(getAngle()) * 5);//5
        addY(-Math.sin(getAngle()) * 5);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(getColor());

        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX(), getY());
        g2.fillOval((int) (getX() - 2.5), (int) (getY() - 2.5), 5, 5);
        g2.fillRect((int) getX(), (int) (getY() - 2.5), 10, 5);


        g2.setTransform(old);

//        g2.setStroke(new BasicStroke(1));
//        g2.drawLine((int) getRX(),0,(int) getRX(),690);
//        g2.drawLine(0,(int) getRY(),1000,(int) getRY());
    }

    public double getRX() {
        return getX();
    }

    public double getRY() {
        return getY();
    }

    public int getType() {
        return 0;
    }
}
