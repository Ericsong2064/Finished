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
    private boolean won;
    private boolean lost;

    private boolean isFreezed;
    private int hp;private int hp1;
    private Boss ez;private Boss ez1;
    private String name;
    private double note;
    private int num = 0;
    private Player player;
    private int click;
    private boolean[] pressedKeys;
    private BufferedImage healthpot;private BufferedImage zmove;private BufferedImage stone; private BufferedImage slash; private BufferedImage background;private BufferedImage ball;private BufferedImage xmove;
    private Timer timer;
    private int time;
    private int j;
    private int p;
    private boolean collected;
    private JButton stop;

    private boolean reset;

    public GraphicsPanel(String name) {
        collected = false;
        reset = false;
        this.name = name;
        click = 0;
        ez1 = new Boss("images/Boss2.png");
        ez = new Boss("images/Boss.png");
        ez1.setHealth(6000);
        hp = ez.gethp();hp1 = ez1.gethp();
        won = false;
        lost = false;
        player = new Player("images/man1.png","images/man.png", name);
        try{
            slash = ImageIO.read(new File("images/slash.png"));
            ball = ImageIO.read(new File("images/ball.png"));
            stone = ImageIO.read(new File("images/Stone.png"));
            healthpot = ImageIO.read(new File("images/healthpot.png"));
            zmove = ImageIO.read(new File("images/zmove.png"));
            xmove = ImageIO.read(new File("images/xmove.png"));
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
        if (ez.gethp() > 0) {
            try{
                background = ImageIO.read(new File("images/background1.png"));
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            g.drawImage(background,0,0,null);
            g.drawImage(ez.getbossImage(), 700, 200, null);// the order that things get "painted" matter; we put background down first
            // this loop does two things:  it draws each Coin that gets placed with mouse clicks,
            // and it also checks if the player has "intersected" (collided with) the Coin, and if so,
            // the score goes up and the Coin is removed from the arraylist
            if (player.playerRect().intersects(ez.BossRect())) {
                player.minus(100);
            }
            if (hp > ez.gethp()) {
                g.drawImage(slash, 500, 150, null);
                hp = ez.gethp();
            }
            if (player.alive() == false && !lost) {
                g.setColor(Color.PINK);
                g.setFont(new Font("Courier New", Font.BOLD, 100));
                g.drawString("YOU LOSE!", 250, 300);
                player.no();
                won = true;
            }
            if(time%5 ==0){
                collected = false;
                p=5;
            }
            if(p==5&& !collected){
                int num = (int)(Math.random()*960);
                int num1 = (int)(Math.random()*560);
                g.drawImage(healthpot,num,num1,null);
                long t = 4;
                if(player.playerRect().intersects(num,num1,healthpot.getWidth(),healthpot.getHeight())){
                    player.heal(10);
                    p=0;
                    collected = true;
                }
            }
            // draw score
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Clicks: " + click,20,100);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            g.drawString("Boss health: " + ez.gethp(),600,40);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString(player.getName() + "'s Score: " + player.getScore(), 20, 40);
            g.drawString("Time: " + time, 20, 70);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString("Health: " +player.getHealth(),20,130);
            if (time % 5 == 0) {
                int x = (int) (Math.random() * 960);
                int y = (int) (Math.random() * 580);
                g.drawImage(ball, x, y, null);
                if (player.playerRect().intersects(x, y, ball.getWidth(), ball.getHeight())) {
                    player.minus(10);
                }
            }

        }else if(ez1.gethp()>0){
            won = false;
            lost = false;
            if(!reset) {
                player.MaxHealth();
                reset = true;
            }
            try{
                background = ImageIO.read(new File("images/background2.png"));
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            if(player.getScore() ==0) {
                player.setScore(1);
            }
            g.drawImage(background,0,0,null);
            g.drawImage(ez1.getbossImage(), 600, 200, null);// the order that things get "painted" matter; we put background down first
            if (player.playerRect().intersects(ez1.BossRect())) {
                player.minus(100);
            }
            if (hp1 > ez1.gethp()) {
                g.drawImage(slash, 500, 150, null);
                hp1 = ez1.gethp();
            }
            if (player.alive() == false && !lost) {
                g.setColor(Color.PINK);
                g.setFont(new Font("Courier New", Font.BOLD, 100));
                g.drawString("YOU LOSE!", 250, 300);
                won = true;
            }
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Clicks: " + click,20,100);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Boss health: " + ez1.gethp(),600,40);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString(player.getName() + "'s Score: " + player.getScore(), 20, 40);
            g.drawString("Time: " + time, 20, 70);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString("Health: " +player.getHealth(),20,130);
            num = (int)(Math.random()*540)+1;
            if(time%5==0 ){
                note = 950;
                while (note > 0) {
                    note -= .1;
                    g.drawImage(stone, (int) note, num, null);
                    if (player.playerRect().intersects(note, num, stone.getWidth(), stone.getHeight())) {
                        player.minus(.01);
                    }
                }
                time++;
            }
        }else{
            g.setFont(new Font("Courier New", Font.BOLD, 24));
           g.drawString("You Win!",250,300);
        }

        if (isFreezed) {
            g.setFont(new Font("Courier New", Font.BOLD, 100));
            g.drawString("Paused", 300, 300);
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
        if(pressedKeys[90] && j%5 ==0){
            int x= player.getxCoord();
            int y = player.getyCoord();
            while(x<=960){
              g.drawImage(zmove,x,y,null);
              x+=1;
              if(ez.BossRect().intersects(x,y,zmove.getWidth(),zmove.getHeight())){
                  ez.minusHp(5);
              }
                if(ez1.BossRect().intersects(x,y,zmove.getWidth(),zmove.getHeight())){
                    ez1.minusHp(5);
                }
            }
        }
        if(pressedKeys[88]){
            g.drawImage(xmove,600,200,null);
            if(ez.gethp()>0){
                ez.minusHp(5);
            }else if(ez1.gethp()>0){
                ez1.minusHp(5);
            }
        }
        if(time>j){
            j++;
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
        click++;
        if(ez.gethp()>0) {
            ez.minus();
        }else if(ez1.gethp()>0){
            ez1.minus();
        }
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