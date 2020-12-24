import javax.swing.*;
import java.awt.*;

public class Guy {
    private String currentWeaponName;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double vr = 0;
    private double angle;
    private int team;
    private int cooldown = 0;
    private boolean dualiesStartSide;
    private boolean WeaponSide;
    private int currentWeaponID = -1;
    public boolean isDroppingThru;
    public boolean isMovingLeft;
    public boolean isMovingRight;
    public boolean isShooting;

    public Guy(String startWeapon, int team1or2, double startX, double startY,
            double startVx, double startVy) {
        currentWeaponName = startWeapon;
        x = startX;
        y = startY;
        vx = startVx;
        vy = startVy;
        angle = 0;
        team = team1or2;
        if (team1or2 == 1) {
            dualiesStartSide = false;
            WeaponSide = false;
        } else {
            dualiesStartSide = true;
            WeaponSide = true;
        }
        isDroppingThru=false;
        isMovingLeft=false;
        isMovingRight=false;
        isShooting=false;
    }

    public void shoot(java.util.List bullets) {
        if (cooldown <= 0) {
            Color col;
            if (team == 1) col = Color.magenta;
            else col = Color.cyan;
            switch (currentWeaponID) { //TODO edit this
                case -1: //fists
                    fistBullet.fire(bullets, col, x, y, angle);
                    break;
                case 0: //dualies
                    pistolBullet.fire(bullets, col, x, y, angle,
                                      dualiesStartSide, this);
                    break;
                case 1://sniper
                    sniperBullet.fire(bullets, col, x, y, angle, WeaponSide);
                    break;
                case 2://laser
                    laserBullet.fire(bullets, col, x, y, angle, WeaponSide);
                    break;
                case 3://shotty
                    shotgunBullet.fire(bullets, col, x, y, angle, WeaponSide,
                                       this);
                    break;
                case 4://minigun
                    minigunBullet.fire(bullets, col, x, y, angle, WeaponSide,
                                       this);
                    break;
            }
            cooldown = getROF();
        }
    }//TODO: need to edit when adding new weapon

    public int getROF() {
        return Bullet.rofByID(currentWeaponID);
    }

    public void toggleDualiesSide() {
        dualiesStartSide = !dualiesStartSide;
    }

    public double getVr() {
        return vr;
    }

    public void setVr(double newVr) {
        vr = newVr;
    }

    public void syncVrWithVx() {
        vx = (3 * vx + vr) / 4; //weighted average
        vr = vx;
    }

    public void syncVrWithVy() {
        vy = (vy - Math.abs(vr)) / 2; //weighted average
        vr = Math.signum(vx) * vy;
    }

    public void redCool() {
        cooldown--;
    }

    public void addAngle(double dAngle) {
        angle += dAngle;
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return x;
    }

    public void setX(double newX) {
        x = newX;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double newVx) {
        vx = newVx;
    }

    public double getY() {
        return y;
    }

    public void setY(double newY) {
        y = newY;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double newVy) {
        vy = newVy;
    }

    public void addX(double dX) {
        x += dX;
    }

    public void addY(double dY) {
        y += dY;
    }

    public void addVx(double dVx) {
        vx += dVx;
    }

    public void addVy(double dVy) {
        vy += dVy;
    }

    public int getWeaponID() {
        return currentWeaponID;
    }

    public void cycleWeapons() {
        currentWeaponID++;
        if (currentWeaponID > Bullet.NUM_OF_WEAPON_TYPES - 2)
            currentWeaponID = -1;
        currentWeaponName = Bullet.nameByID(currentWeaponID);
        cooldown = getROF();
    }

    public int getCool() {
        return cooldown;
    }

    public Image getSprite() {
        ImageIcon pic = new ImageIcon(
                "src/guys/" + currentWeaponName + team + ".png");
        return pic.getImage();
    }
}