import java.awt.*;

public abstract class Bullet {
    public static final int NUM_OF_WEAPON_TYPES = 6;
    private double x;
    private double y;
    private double angle;
    private Color color;

    public Bullet(Color color, double x, double y, double angle) {
        this.color = color;
        this.x = x;
        this.y = y;
        if (angle == 0) angle += .000000000001;
        this.angle = angle;
    }

    public static String nameByID(int id) {
        switch (id) {//TODO edit this
            case -1: //fist
                return fistBullet.NAME;
            case 0: //dualie
                return pistolBullet.NAME;
            case 1: //sniper
                return sniperBullet.NAME;
            case 2: //laser
                return laserBullet.NAME;
            case 3: //shotty
                return shotgunBullet.NAME;
            case 4: //minigun
                return minigunBullet.NAME;
        }
        return "";
    }//TODO: need to edit when adding new weapon

    public static int rofByID(int id) {
        switch (id) {//TODO edit this
            case -1: //fist
                return fistBullet.ROF;
            case 0: //dualie
                return pistolBullet.ROF;
            case 1: //sniper
                return sniperBullet.ROF;
            case 2: //laser
                return laserBullet.ROF;
            case 3: //shotty
                return shotgunBullet.ROF;
            case 4: //minigun
                return minigunBullet.ROF;
        }
        return 9999; //backup case
    }//TODO: need to edit when adding new weapon

    abstract int damage();

    abstract void act();

    abstract void draw(Graphics2D g2);

    abstract double getRX();

    abstract double getRY();

    public double getX() {
        return x;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void addX(double dX) {
        x += dX;
    }

    public void addY(double dY) {
        y += dY;
    }

    abstract int getType();

    public double getY() {
        return y;
    }

    public void setY(double newY) {
        y = newY;
    }

    public Color getColor() {
        return color;
    }

    public double getAngle() {
        return angle;
    }
}
