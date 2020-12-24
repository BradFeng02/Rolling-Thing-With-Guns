import java.awt.*;
import java.awt.geom.AffineTransform;

public class laserBullet extends Bullet {
    public static final int ROF = 177;
    public static final String NAME = "laser";
    private int decay = 100;
    private double x;
    private double y;

    public laserBullet(Color color, double x, double y, double angle) {
        super(color, x, y, angle);
        this.x = x;
        this.y = y;
    }

    public static void fire(java.util.List bullets, Color col, double x,
            double y, double angle, boolean WeaponSide) {
        if (WeaponSide) bullets.add(new laserBullet(col, x - 45, y, angle));
        else bullets.add(new laserBullet(col, x - 45, y, angle + Math.PI));
    }

    public int damage() {
        return 10;
    }

    public void act() {

        decay--;
    }

    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.rotate(getAngle(), getX() + 45, getY());
        int trans = (int) 4.5 * decay;
        if (trans > 255) trans = 255;
        Color col = new Color(getColor().getRed(), getColor().getGreen(),
                              getColor().getBlue(), trans);
        g2.setColor(col);
        g2.fillRect((int) x - 4997, (int) (y - 1.5), 5000, 3);

        g2.setTransform(old);

//        double angle = getAngle() - Math.PI;
//        double rr=getX() + Math.abs(Math.cos(angle / 2) * 90) - Math.abs(
//                Math.sin(angle / 2) * Math.sin(angle) * 29);
//        int yy=500;
//        //int xx= (int) (rr +Math.cos(getAngle())*(yy-getRY()));
//        int xx=(int)(rr+(yy-getRY())/Math.tan(getAngle()));
//        g2.drawLine(xx,1000,xx,0);
//        g2.drawLine(0,yy,2000,yy);
    }

    public double getRX() {
        if (decay <= 0) {
            return 9999;
        }
        if (decay < 100) {
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

    public int getType() {
        return 2;
    }
}
