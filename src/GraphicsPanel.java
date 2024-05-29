import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private BufferedImage beam;
    private BufferedImage ball;
    private boolean isFreezed;
    private BufferedImage slash;
    private int hp;
    private Boss ez;
    private String name;
    private Player player;
    private boolean[] pressedKeys;
    private Timer timer;
    private int time;

    private JButton stop;

    public GraphicsPanel(String name) {
        this.name = name;
        ez = new Boss("images/Boss.png");
        hp = ez.gethp();
        player = new Player("images/man1.png","images/man.png", name);
        try{
            slash = ImageIO.read(new File("images/slash.png"));
            beam = ImageIO.read(new File("images/beam.png"));
            ball = ImageIO.read(new File("images/ball.png"));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        isFreezed = false;

        pressedKeys = new boolean[128];
        time = 0;
        timer = new Timer(1000, this); // this Timer will call the actionPerformed interface method every 1000ms = 1 second
        timer.start();
        stop = new JButton("PAUSE");
        add(stop);
        stop.setFocusable(false);
        stop.addActionListener(this);
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow(); // see comment above

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
        g.drawImage(ez.getbossImage(), 700,200,null);// the order that things get "painted" matter; we put background down first
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
        // this loop does two things:  it draws each Coin that gets placed with mouse clicks,
        // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
        // the score goes up and the Coin is removed from the arraylist
       if(player.playerRect().intersects(700,200,ez.getbossImage().getWidth(),ez.getbossImage().getHeight())){
           player.minus(100);
       }
        if(ez.gethp()<=0){
            g.setColor(Color.PINK);
            g.setFont(new Font("Courier New", Font.BOLD, 100));
            g.drawString("YOU WIN!",250,300);
        }
        if(hp>ez.gethp()){
            g.drawImage(slash,500,150,null);
            hp = ez.gethp();
        }
        if(player.alive() == false){
            g.setColor(Color.PINK);
            g.setFont(new Font("Courier New", Font.BOLD, 100));
            g.drawString("YOU LOSE!",250,300);

        }
        // draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.BOLD, 24));
        g.drawString(player.getName() + "'s Score: " + player.getScore(), 20, 40);
        g.drawString("Time: " + time, 20, 70);
        if(time%5 ==0){
                int x = (int)(Math.random()*960);
                int y = (int)(Math.random()*580);
                g.drawImage(ball,x,y,null);
                if(player.playerRect().intersects(x,y,ball.getWidth(),ball.getHeight())){
                    player.minus(.5);
            }

        }
        if(isFreezed) {
            g.setFont(new Font("Courier New", Font.BOLD, 100));
            g.drawString("Paused",300,300);
        }
        // player moves left (A)
        if (pressedKeys[65]) {
            player.faceLeft();
            player.moveLeft();
        }

        // player moves right (D)
        if (pressedKeys[68]) {
            player.faceRight();
            player.moveRight();
        }
        // player moves up (W)
        if (pressedKeys[87]) {
            player.moveUp();
        }

        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }
    }

    // ----- KeyListener interface methods -----
    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    // ----- MouseListener interface methods -----
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {

        ez.minus();
        if(isFreezed == false) {
            Point mouseClickLocation = e.getPoint();
            if (player.playerRect().contains(mouseClickLocation)) {
                player.turn();
            }
        }
    }

    public void mouseEntered(MouseEvent e) { } // unimplemented

    public void mouseExited(MouseEvent e) { } // unimplemented

    // ACTIONLISTENER INTERFACE METHODS: used for buttons AND timers!
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            time++;
        }
        if(e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            if(button == stop && isFreezed == false){
                timer.stop();
                isFreezed = true;
                player.no();
            }else if(button == stop && isFreezed){
                timer.start();
                isFreezed = false;
                player.yes();
            }
        }
    }
}
