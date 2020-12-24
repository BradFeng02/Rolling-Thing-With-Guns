import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class Level extends JPanel {
    private static final double GRAV = .33;//.33
    private static final double BOUNCE_RETAIN = .55;//.55
    private static final double FRICTION = .03;//.03
    private static final int TICK_TIME = 10; //10
    private static final int[][] OBS =//format: {x,y,length}
            {{455, 110, 90}, {0, 680, 1000}, {375, 375, 250}, {110, 550, 100},
                    {790, 550, 100}, {130, 220, 160}, {710, 220, 160}};
    double lastx1 = 60;
    double lastx2 = 940;
    double lasty1 = 0;
    double lasty2 = 0;
    private double stopHeight1 = 0;
    private double stopHeight2 = 0;
    private List<Bullet> bullets = Collections.synchronizedList(
            new ArrayList<>());
    private Guy P1 = new Guy("empty", 1, 60, 0, 0, 0);
    private Guy P2 = new Guy("empty", 2, 940, 0, 0, 0);
    private int score1 = 0;
    private int score2 = 0;
    private boolean jumping1 = false;
    private boolean jumping2 = false;
    private int jumps1 = 2;
    private int jumps2 = 2;
    private double hudx1 = 60;
    private double hudx2 = 940;
    private double hudy1 = 0;
    private double hudy2 = 0;
    int time=0;

    public Level() {
        initLevel();
    }

    private void initLevel() {
        setPreferredSize(new Dimension(1000, 690));
        setBackground(Color.white);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Logic(), 0, TICK_TIME);
        addKeyListener(new Input());
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObs(g);
        Graphics2D g2 = (Graphics2D) g;
        drawGuy(g2, P1, Color.red);
        drawGuy(g2, P2, Color.blue);
        drawBullets(g2);
        drawBorder(g2);
        drawScore(g2);
        drawUp(g2);
        drawHUD(g2);
    }

    private void drawObs(Graphics g) {
        for (int[] OB : OBS) {

            g.fillRect(OB[0], OB[1], OB[2], 10);

        }
    }

    private void drawGuy(Graphics2D g2, Guy guy, Color sightColor) {
        int y = (int) guy.getY();
        int x = (int) guy.getX();
        AffineTransform old = g2.getTransform();
        guy.addAngle(guy.getVr() / 45);
        double angle = guy.getAngle();
        g2.rotate(angle, guy.getX(), guy.getY());
        g2.drawImage(guy.getSprite(), (int) guy.getX() - 45,
                     (int) guy.getY() - 45, 90, 90, null);
        g2.setColor(sightColor);
        g2.setStroke(
                new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                0, new float[]{2, 5}, 0));
        int far = 45;
        int r = sightColor.getRed();
        int g = sightColor.getGreen();
        int b = sightColor.getBlue();
        if (guy == P1 && guy.getWeaponID() != 0 || guy.getWeaponID() == -1) {
        } else {
            for (int i = 100; i >= 0; i--) {
                if (far < 0) break;
                g2.setColor(new Color(r, g, b, i));
                g2.drawLine(x - far, y, x - far - 5, y);
                far += 5;
            }
        }
        if (guy == P2 && guy.getWeaponID() != 0 || guy.getWeaponID() == -1) {
        } else {
            far = 45;
            for (int i = 100; i >= 0; i--) {
                if (far > 1000) break;
                g2.setColor(new Color(r, g, b, i));
                g2.drawLine(x + far, y, x + far + 5, y);
                far += 5;
            }
        }

        g2.setTransform(old);

    }

    private void drawBullets(Graphics2D g2) {
        synchronized (bullets) {
            for (Bullet bul : bullets) {
                bul.draw(g2);
            }
        }

    }

    private void drawBorder(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(10));
        g2.drawRect(5, -50, 990, 735);
    } //only 5 thick

    private void drawScore(Graphics2D g2) {
        Map<TextAttribute, Object> attributes = new HashMap<>();
        Font currentFont = g2.getFont();
        attributes.put(TextAttribute.FAMILY, currentFont.getFamily());
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        attributes.put(TextAttribute.SIZE, currentFont.getSize() * 3);
        Font myFont = Font.getFont(attributes);
        g2.setFont(myFont);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String s = "" + score2;
        g2.setColor(Color.decode("#000099"));
        g2.drawString(s, 965 - fontMetrics.stringWidth(s), 60);
        s = "" + score1;
        g2.setColor(Color.decode("#990000"));
        g2.drawString(s, 35, 60);
    }

    private void drawUp(Graphics2D g2) {
        if (P1.getY() < -4000) {
            score2 += 9999;
        } else if (P2.getY() < -4000) {
            score1 += 9999;
        }

        if (P1.getY() < -55) {
            g2.setColor(Color.red);
            double sized = (1500 + P1.getY()) / 88;
            if (sized <= 0) {
                sized = 0;
            }
            sized = 10 + Math.abs(sized);
            int size = (int) sized;
            int[] xs = {(int) P1.getX(), (int) P1.getX() + size,
                    (int) P1.getX() - size};
            int[] ys = {5, 5 + size, 5 + size};
            g2.fillPolygon(xs, ys, 3);

            AffineTransform old = g2.getTransform();
            double angle = P1.getAngle();
            int mini = size;
            g2.rotate(angle, P1.getX(), 10 + 2 * mini);
            g2.drawImage(P1.getSprite(), (int) P1.getX() - mini, 10 + mini,
                         2 * mini, 2 * mini, null);
            g2.setTransform(old);
            g2.setColor(Color.white);
            Map<TextAttribute, Object> attributes = new HashMap<>();
            Font currentFont = g2.getFont();
            attributes.put(TextAttribute.FAMILY, currentFont.getFamily());
            attributes.put(TextAttribute.WEIGHT, 1f);
            attributes.put(TextAttribute.SIZE, sized - 7);
            Font myFont = Font.getFont(attributes);
            g2.setFont(myFont);
            FontMetrics fontMetrics = g2.getFontMetrics();
            String s = "" + (((int) (Math.abs((P1.getY())) - 55) / 30));
            g2.drawString(s, (int) (P1.getX() - fontMetrics.stringWidth(s) / 2 -
                    1), 4 + size + 18 - 20);
        }

        if (P2.getY() < -55) {
            g2.setColor(Color.blue);
            double sized = (1500 + P2.getY()) / 88;
            if (sized <= 0) {
                sized = 0;
            }
            sized = 10 + Math.abs(sized);
            int size = (int) sized;
            int[] xs = {(int) P2.getX(), (int) P2.getX() + size,
                    (int) P2.getX() - size};
            int[] ys = {5, 5 + size, 5 + size};
            g2.fillPolygon(xs, ys, 3);

            AffineTransform old = g2.getTransform();
            double angle = P2.getAngle();
            int mini = size;
            g2.rotate(angle, P2.getX(), 10 + 2 * mini);
            g2.drawImage(P2.getSprite(), (int) P2.getX() - mini, 10 + mini,
                         2 * mini, 2 * mini, null);
            g2.setTransform(old);
            g2.setColor(Color.white);
            Map<TextAttribute, Object> attributes = new HashMap<>();
            Font currentFont = g2.getFont();
            attributes.put(TextAttribute.FAMILY, currentFont.getFamily());
            attributes.put(TextAttribute.WEIGHT, 1f);
            attributes.put(TextAttribute.SIZE, sized - 7);
            Font myFont = Font.getFont(attributes);
            g2.setFont(myFont);
            FontMetrics fontMetrics = g2.getFontMetrics();
            String s = "" + (((int) (Math.abs((P2.getY())) - 55) / 30));
            g2.drawString(s, (int) (P2.getX() - fontMetrics.stringWidth(s) / 2 -
                    1), 4 + size + 18 - 20);
        }
    }

    private void drawHUD(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        double jiggle = 20.0;
        time++;
        if (time>=10){//10
            time=0;
            lastx1=(lastx1+P1.getX())/2;
            lastx2=(lastx2+P2.getX())/2;
            lasty1=(lasty1+P1.getY())/2;
            lasty2=(lasty2+P2.getY())/2;
        }

        hudx1 = ((jiggle - 1) * hudx1 + lastx1) / (jiggle);
        hudx2 = ((jiggle - 1) * hudx2 + lastx2) / (jiggle);
        hudy1 = ((jiggle - 1) * hudy1 + lasty1) / (jiggle);
        hudy2 = ((jiggle - 1) * hudy2 + lasty2) / (jiggle);

        //double hudA1 = (P1.getX() - hudx1) / 1000;
        double hudA1=0;
        int x = (int) hudx1;
        int y = (int) hudy1;

        g2.rotate(-hudA1, P1.getX(), P1.getY());
        double reload = ((double) P1.getCool()) / ((double) P1.getROF());
        if (reload > 1) reload = 1;
        if (reload < 0) reload = 0;
        g2.setColor(Color.darkGray);
        g2.fillRect(x - 40, y - 60, 70, 5);
        g2.setColor(Color.green);
        g2.fillRect(x - 40, y - 60, (int) (70 - reload * 70), 5);
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke());
        g2.drawRect(x - 40, y - 60, 70, 5);

        if (jumps1 >= 2) g2.setColor(Color.yellow);
        int[] xs = {x + 35 - 2 + 5, x + 40 - 1 + 5, x + 45 + 5, x + 45 + 5,
                x + 40 - 1 + 5, x + 35 - 2 + 5};
        int[] ys = {y - 47 - 1, y - 50 - 1, y - 47 - 1, y - 42 - 1 + 1,
                y - 45 - 1 + 1, y - 42 - 1 + 1};
        g2.fillPolygon(new Polygon(xs, ys, 6));
        g2.setColor(Color.darkGray);
        g2.drawPolygon(new Polygon(xs, ys, 6));
        if (jumps1 >= 1) g2.setColor(Color.yellow);
        for (int i = 0; i < 6; i++) {
            ys[i] -= 12;
        }
        g2.fillPolygon(new Polygon(xs, ys, 6));
        g2.setColor(Color.darkGray);
        g2.drawPolygon(new Polygon(xs, ys, 6));
        g2.setTransform(old);


        //double hudA2 = (P2.getX() - hudx2) / 500;
        double hudA2=0;
        x = (int) hudx2;
        y = (int) hudy2;
        g2.rotate(-hudA2, P2.getX(), P2.getY());
        reload = ((double) P2.getCool()) / ((double) P2.getROF());
        if (reload > 1) reload = 1;
        if (reload < 0) reload = 0;
        g2.fillRect(x - 40, y - 60, 70, 5);
        g2.setColor(Color.green);
        g2.fillRect(x - 40, y - 60, (int) (70 - reload * 70), 5);
        g2.setColor(Color.darkGray);
        g2.setStroke(new BasicStroke());
        g2.drawRect(x - 40, y - 60, 70, 5);
        if (jumps2 >= 2) g2.setColor(Color.yellow);
        int[] xs2 = {x + 35 - 2 + 5, x + 40 - 1 + 5, x + 45 + 5, x + 45 + 5,
                x + 40 - 1 + 5, x + 35 - 2 + 5};
        int[] ys2 = {y - 47 - 1, y - 50 - 1, y - 47 - 1, y - 42 - 1 + 1,
                y - 45 - 1 + 1, y - 42 - 1 + 1};
        g2.fillPolygon(new Polygon(xs2, ys2, 6));
        g2.setColor(Color.darkGray);
        g2.drawPolygon(new Polygon(xs2, ys2, 6));
        if (jumps2 >= 1) g2.setColor(Color.yellow);
        for (int i = 0; i < 6; i++) {
            ys2[i] -= 12;
        }
        g2.fillPolygon(new Polygon(xs2, ys2, 6));
        g2.setColor(Color.darkGray);
        g2.drawPolygon(new Polygon(xs2, ys2, 6));
        g2.setTransform(old);
    }

    //logic below

    private boolean platUnderOrCeil(Guy guy,
            int which) { //which is stopHeight+which
        double Y = guy.getY() + guy.getVy();
        double X = guy.getX() + guy.getVx();
        for (int[] OB : OBS) {

            if (X > OB[0] - 21 && X < OB[0] + 21 + OB[2] &&
                    guy.getY() + 44 < OB[1] && Y + 46 > OB[1]) {
                //Y >= OB[1] - 46 && Y <= OB[1] - 24) {
                if (which == 1) stopHeight1 = OB[1] - 45;
                else stopHeight2 = OB[1] - 45;
                if (which == 1) return !guy.isDroppingThru ||
                        stopHeight1 == 680 - 45;//dont drop thru ground
                else return !guy.isDroppingThru || stopHeight2 == 680 - 45;
            }
        }
        return false;
    }

    private void applyV() {
        P1.addX(P1.getVx());
        P2.addX(P2.getVx());
        P1.addY(P1.getVy());
        P2.addY(P2.getVy());
    }

    private void gravity() {
        P1.addVy(GRAV);
        P2.addVy(GRAV);
    }

    private void bounceVert(boolean one, boolean two, double stoph1,
            double stoph2) {
        if (one) {
            P1.setVy(P1.getVy() * -BOUNCE_RETAIN);
            P1.setY(stoph1);
        }
        if (two) {
            P2.setVy(P2.getVy() * -BOUNCE_RETAIN);
            P2.setY(stoph2);
        }
    }

    private void bounceHoriz(Guy guy) {
        double knew = guy.getX() + guy.getVx();
        if (knew >= 946) {
            guy.setVx(guy.getVx() * -BOUNCE_RETAIN);
            guy.setX(945);
            guy.syncVrWithVy();
        } else if (knew <= 54) {
            guy.setVx(guy.getVx() * -BOUNCE_RETAIN);
            guy.setX(55);
            guy.syncVrWithVy();
        }
    }

    private void friction(boolean one, boolean two) {
        if (one) {
            if (P1.getVx() >= 0) {
                P1.addVx(-FRICTION);
                if (P1.getVx() <= 0) P1.setVx(0);
            } else {
                P1.addVx(FRICTION);
                if (P1.getVx() >= 0) P1.setVx(0);
            }
        }
        if (two) {
            if (P2.getVx() >= 0) {
                P2.addVx(-FRICTION);
                if (P2.getVx() <= 0) P2.setVx(0);
            } else {
                P2.addVx(FRICTION);
                if (P2.getVx() >= 0) P2.setVx(0);
            }
        }
    }

    private void move() {
        if (P1.isMovingLeft) {
            P1.addVx(-.05);
            P1.setVr(P1.getVr() - .1);
        }
        if (P1.isMovingRight) {
            P1.addVx(.05);
            P1.setVr(P1.getVr() + .1);
        }
        if (P2.isMovingLeft) {
            P2.addVx(-.05);
            P2.setVr(P2.getVr() - .1);
        }
        if (P2.isMovingRight) {
            P2.addVx(.05);
            P2.setVr(P2.getVr() + .1);
        }
    }

    private void updateBul() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bul = bullets.get(i);
            double y = bul.getRY();
            double x = bul.getRX();
            double x1 = x;
            double x2 = x + (500) / Math.tan(bul.getAngle());
            double y1 = y;
            double y2 = y + 500;
            if (x <= -50 || x >= 1050 || y >= 740 || y < -4000) {
                bullets.remove(i);
            } else if (bul.getColor() == Color.magenta && (Math.sqrt(
                    Math.pow(y - P2.getY(), 2) + Math.pow(x - P2.getX(), 2)) <=
                    45 || (bul.getType() == 2 && x != 1025 && Math.abs(
                    (P2.getX() - x1) * (y2 - y1) -
                            (P2.getY() - y1) * (x2 - x1)) / Math.sqrt(
                    Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) <= 45 &&
                    ((Math.cos(bul.getAngle()) <= 0 && P2.getX() >= x) ||
                            (Math.cos(bul.getAngle()) >= 0 &&
                                    P2.getX() <= x))))) {
                score1 += bul.damage();
                if (bul.getType() != -1 && bul.getType() != 2) {
                    bullets.remove(i);
                } else {
                    bul.act();
                }
            } else if (bul.getColor() == Color.cyan && (Math.sqrt(
                    Math.pow(y - P1.getY(), 2) + Math.pow(x - P1.getX(), 2)) <=
                    45 || (bul.getType() == 2 && x != 1025 && Math.abs(
                    (P1.getX() - x1) * (y2 - y1) -
                            (P1.getY() - y1) * (x2 - x1)) / Math.sqrt(
                    Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)) <= 45 &&
                    ((Math.cos(bul.getAngle()) <= 0 && P1.getX() >= x) ||
                            (Math.cos(bul.getAngle()) >= 0 &&
                                    P1.getX() <= x))))) {
                score2 += bul.damage();
                if (bul.getType() != -1 && bul.getType() != 2) {
                    bullets.remove(i);
                } else {
                    bul.act();
                }
            } else {
                bul.act();
            }
        }
    }

    private class Logic extends TimerTask {
        public void run() {
            P1.redCool();
            P2.redCool();
            if (P1.isShooting) P1.shoot(bullets);
            if (P2.isShooting) P2.shoot(bullets);
            boolean air1 = platUnderOrCeil(P1, 1);
            boolean air2 = platUnderOrCeil(P2, 2);
            if (air1) {
                jumps1 = 2;
            }
            if (air2) {
                jumps2 = 2;
            }
            applyV();
            gravity();
            bounceVert(air1, air2, stopHeight1, stopHeight2);
            if (air1) P1.syncVrWithVx();
            if (air2) P2.syncVrWithVx();
            bounceHoriz(P1);
            bounceHoriz(P2);
            friction(air1, air2);
            move();
            //limit speed below
            if (air1 && Math.abs(P1.getVx()) > 4) P1.addVx(
                    -Math.signum(P1.getVx()) * (Math.abs(P1.getVx()) - 4) / 8);
            if (air2 && Math.abs(P2.getVx()) > 4) P2.addVx(
                    -Math.signum(P2.getVx()) * (Math.abs(P2.getVx()) - 4) / 8);
            repaint();
            updateBul();
        }
    }

    //input below
    private class Input extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_UP:
                    if (jumps2 > 0 && !jumping2) {
//                        if (P2.getVy()>0) P2.setVy(-10);
//                        else P2.addVy(-10);
                        P2.setVy(-10);
                    }
                    jumps2--;
                    jumping2 = true;
                    break;
                case KeyEvent.VK_LEFT:
                    P2.isMovingLeft = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    P2.isMovingRight = true;
                    break;
                case KeyEvent.VK_DOWN:
                    P2.isDroppingThru = true;
                    break;
                case KeyEvent.VK_COMMA:
                    P2.isShooting = true;
                    break;
                case KeyEvent.VK_W:
                    if (jumps1 > 0 && !jumping1) {
//                    if (P1.getVy()>0) P1.setVy(-10);
//                    else P1.addVy(-10);
                        P1.setVy(-10);
                    }
                    jumps1--;
                    jumping1 = true;
                    break;
                case KeyEvent.VK_A:
                    P1.isMovingLeft = true;
                    break;
                case KeyEvent.VK_D:
                    P1.isMovingRight = true;
                    break;
                case KeyEvent.VK_S:
                    P1.isDroppingThru = true;
                    break;
                case KeyEvent.VK_G:
                    P1.isShooting = true;
                    break;
                case KeyEvent.VK_Q:
                    P1.cycleWeapons();
                    break;
                case KeyEvent.VK_SHIFT:
                    if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_RIGHT)
                        P2.cycleWeapons();
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    P2.isMovingLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    P2.isMovingRight = false;
                    break;
                case KeyEvent.VK_DOWN:
                    P2.isDroppingThru = false;
                    break;
                case KeyEvent.VK_COMMA:
                    P2.isShooting = false;
                    break;
                case KeyEvent.VK_A:
                    P1.isMovingLeft = false;
                    break;
                case KeyEvent.VK_D:
                    P1.isMovingRight = false;
                    break;
                case KeyEvent.VK_G:
                    P1.isShooting = false;
                    break;
                case KeyEvent.VK_S:
                    P1.isDroppingThru = false;
                    break;
                case KeyEvent.VK_W:
                    jumping1 = false;
                    break;
                case KeyEvent.VK_UP:
                    jumping2 = false;
                    break;
            }
        }
    }
}
