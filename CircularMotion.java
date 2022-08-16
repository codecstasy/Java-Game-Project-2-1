package circular.motion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_SPACE;
import java.awt.event.KeyListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CircularMotion extends JPanel implements KeyListener, ActionListener {

    static int playgroundHeight = 800;
    static int playgroundWidth = 400;

    boolean isShiftPressed = false;
    double cx = playgroundWidth / 2;
    double cy = playgroundHeight / 2;
    double dropletX = playgroundWidth / 2, dropletY = -35;
    static Color dropletColor1 = Color.RED;
    static Color dropletColor2 = Color.BLUE;
    static Color ballColor1 = Color.RED;
    static Color ballColor2 = Color.BLUE;
    static int throwingTimeInterval = 1000;
    static int movementInterval = 2;
    double dropletX2 = playgroundWidth / 2, dropletY2 = playgroundHeight + 35;
    double rad = 50;
    double panelRadius = 15;
    int angle1 = 90;
    int angle2 = 270;
    static boolean spacePressed = false;
    double posx1 = cx + rad * Math.cos(angle1 * ((Math.PI) / 180));
    double posy1 = cy + rad * Math.sin(angle1 * ((Math.PI) / 180));
    double posx2 = cx + rad * Math.cos(angle2 * ((Math.PI) / 180));
    double posy2 = cy + rad * Math.sin(angle2 * ((Math.PI) / 180));
    boolean isGameOver = false;
    boolean hideBall1 = false;
    boolean hideBall2 = false;

    int score = 0;

    Thread recoverToMinimalPosition = new Thread();
    Thread currentPlayerThread = new Thread();

    CircularMotion() {

    }

    private void moveBall1(boolean isShiftPressed, int increment) {
        posx1 = cx + rad * Math.cos(angle1 * ((Math.PI) / 180));
        posy1 = cy + rad * Math.sin(angle1 * ((Math.PI) / 180));
        if (isShiftPressed) {
            angle1 -= increment;
        } else {
            angle1 += increment;
        }
    }

    private void moveDroplet() {
        dropletY += 1;
    }

    static boolean isMoving = false;

    private void moveBall2(boolean isShiftPressed, int increment) {
        isMoving = true;
        posx2 = cx + rad * Math.cos(angle2 * ((Math.PI) / 180));
        posy2 = cy + rad * Math.sin(angle2 * ((Math.PI) / 180));

        if (isShiftPressed) {
            angle2 -= increment;
        } else {
            angle2 += increment;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isGameOver) {
            g2d.setColor(Color.lightGray);
            g2d.fillOval((int) cx - (int) rad, (int) cy - (int) rad, (int) rad * 2, (int) rad * 2);

            g2d.setColor(Color.RED);
            g2d.fillOval((int) posx1 - (int) panelRadius, (int) posy1 - (int) panelRadius, 30, 30);//Circle 1

            g2d.setColor(Color.BLUE);
            g2d.fillOval((int) posx2 - (int) panelRadius, (int) posy2 - (int) panelRadius, 30, 30);

            if (!hideBall1) {
                g2d.setColor(dropletColor1);
                g2d.fillOval((int) dropletX - (int) panelRadius, (int) dropletY - (int) panelRadius, 30, 30);

            }

            if (!hideBall2) {
                g2d.setColor(dropletColor2);
                g2d.fillOval((int) dropletX2 - (int) panelRadius, (int) dropletY2 - (int) panelRadius, 30, 30);

            }

        }

//        if(isGameOver) {
//            play = false;
//            ballXdir = 0;
//            ballYdir = 0;
//            g.setColor(Color.RED);
//            g.setFont(new Font("serif", Font.BOLD, 30));
//            g.drawString("You Won!, Score: " + score, 190, 300);
//            
//            g.setFont(new Font("serif", Font.BOLD, 20));
//            g.drawString("Press Enter to Restart", 190, 400);
//        }
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 50, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 50, 400);
        }
    }
    static CircularMotion game;

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Collision Mania");
        game = new CircularMotion();
        frame.add(game);
        frame.setSize(playgroundWidth, playgroundHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(game);
        frame.setVisible(true);
        game.initGame();
        // drop ball at every one sec
        new Thread(new Runnable() {
            public void run() {

                while (true) {

                    try {
                        throwingTimeInterval -= 50;
//                        movementInterval -= 0.1;
                        System.out.println("triggered!");
                        dropletColor1 = Math.random() > 0.5 ? Color.BLUE : Color.RED;
                        boolean direction = Math.random() > 0.5 ? true : false;
                        if(direction)
                        {
                            game.control2();
                        }
                        else
                        {
                            game.control3();
                        }
                        Thread.sleep(throwingTimeInterval);
                    } catch (Exception ex) {
                        Logger.getLogger(CircularMotion.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }).start();

        // drop 2nd ball at every two sec
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    dropletColor2 = Math.random() < 0.5 ? Color.BLUE : Color.RED;
//                    Thread.sleep(2000);
//                    while (true) {
//
//                        game.control3();
//                        Thread.sleep(2000);
//
//                    }
//                } catch (Exception ex) {
//                    Logger.getLogger(CircularMotion.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        }).start();
//        game.control();
    }

    public void control() throws InterruptedException {
        System.out.println("Pressed");
        int cnt = 0;

        while (cnt < 60) {
            moveBall1(isShiftPressed, 3);
            moveBall2(isShiftPressed, 3);
            repaint();
            Thread.sleep(5);
            cnt++;
        }

        isMoving = false;
    }

    public void abort() {
        isGameOver = true;
        hideBall2 = true;
        hideBall1 = true;
    }

    public void control2() throws InterruptedException {
        int count = playgroundHeight + 70;
        hideBall1 = false;
        dropletY = 0;
        
        while (count != 0 && !isGameOver) {
            dropletY += 1;
//            System.out.println(count + " = count");
            Thread.sleep(movementInterval);
            count--;
            if (hideBall1) {
                repaint();
                continue;
            }
            
            if (doesCollide2(1)) {
                abort();
            } else if (doesCollide(1)) {
                System.out.println("doesColide");
                hideBall1 = true;
                score++;
                dropletY = 0;
                repaint();
                break;
//                continue;
            }

            repaint();

        }
    }

    public void control3() throws InterruptedException {
        int count = playgroundHeight + 70;
        dropletY2 = playgroundHeight;
        hideBall2 = false;
        while (count != 0 && !isGameOver) {
            dropletY2--;
//            System.out.println("~~~ " + hideBall1 + " ~~~ " + hideBall2);
            Thread.sleep(movementInterval);
            count--;
            if (hideBall2) {
                repaint();
                continue;
            }
            
            if (doesCollide2(2)) {
                abort();
            } else if (doesCollide(2)) {
                hideBall2 = true;
                score++;
                dropletY2 = playgroundHeight;
                repaint();
                break;
//                continue;
            }

            repaint();
        }
    }

    private boolean circleCircleCollision(double x1, double y1, double r1, double x2, double y2, double r2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy <= (r1 + r2) * (r1 + r2);
    }

    public boolean doesCollide(int ballNumber) throws InterruptedException {
        if (ballNumber == 1) {
            return circleCircleCollision(posx1, posy1, panelRadius, dropletX, dropletY, panelRadius)
                    || circleCircleCollision(posx2, posy2, panelRadius, dropletX, dropletY, panelRadius);
        }
        return circleCircleCollision(posx1, posy1, panelRadius, dropletX2, dropletY2, panelRadius)
                || circleCircleCollision(posx2, posy2, panelRadius, dropletX2, dropletY2, panelRadius);
    }

    private boolean circleCircleCollision2(double x1, double y1, double r1, double x2, double y2, double r2, Color c1, Color c2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy <= (r1 + r2) * (r1 + r2) && c1 != c2;
    }

    public boolean doesCollide2(int ballNumber) throws InterruptedException {
        if (ballNumber == 1) {
            return circleCircleCollision2(posx1, posy1, panelRadius, dropletX, dropletY, panelRadius, ballColor1, dropletColor1)
                    || circleCircleCollision2(posx2, posy2, panelRadius, dropletX, dropletY, panelRadius, ballColor2, dropletColor1);
        }
        return circleCircleCollision2(posx1, posy1, panelRadius, dropletX2, dropletY2, panelRadius, ballColor1, dropletColor2)
                || circleCircleCollision2(posx2, posy2, panelRadius, dropletX2, dropletY2, panelRadius, ballColor2, dropletColor2);
    }

    public void initGame() {
        System.out.println("INIT");
        hideBall1 = false;
        hideBall2 = false;
        dropletY = -35;
        dropletY2 = playgroundHeight;
        score = 0;
        movementInterval = 2;
        throwingTimeInterval = 1000;
        isShiftPressed = false;
        cx = playgroundWidth / 2;
        cy = playgroundHeight / 2;
        dropletX = playgroundWidth / 2;
        dropletY = -35;
        dropletColor1 = Color.RED;
        dropletColor2 = Color.BLUE;
        ballColor1 = Color.RED;
        ballColor2 = Color.BLUE;

        dropletX2 = playgroundWidth / 2;
        dropletY2 = playgroundHeight + 35;
        rad = 50;
        panelRadius = 15;
        angle1 = 90;
        angle2 = 270;
        spacePressed = false;
        posx1 = cx + rad * Math.cos(angle1 * ((Math.PI) / 180));
        posy1 = cy + rad * Math.sin(angle1 * ((Math.PI) / 180));
        posx2 = cx + rad * Math.cos(angle2 * ((Math.PI) / 180));
        posy2 = cy + rad * Math.sin(angle2 * ((Math.PI) / 180));
        isGameOver = false;
        hideBall1 = false;
        hideBall2 = false;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) && isGameOver) {
            initGame();
            isGameOver = false;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isMoving) {
                return;
            }

            isShiftPressed = e.isShiftDown();
            recoverToMinimalPosition.stop();
            currentPlayerThread.stop();
            currentPlayerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        control();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CircularMotion.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            currentPlayerThread.start();
        }

//        if (e.getKeyCode() == KeyEvent.VK_W) {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        control2();
//                    } catch (Exception ex) {
//                        Logger.getLogger(CircularMotion.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }).start();
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
