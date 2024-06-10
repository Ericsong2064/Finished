import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Scanner;

public class GraphicsPanel extends JPanel implements KeyListener, MouseListener, ActionListener {
    private boolean won,saw;
    private boolean lost;

    private boolean isFreezed;
    private int hp;private int hp1;
    private Boss ez,ez1,ez2;
    private String name;
    private double note;
    private int num = 0;
    private Player player;
    private int click;
    private boolean[] pressedKeys;
    private BufferedImage zmove,stone,slash,background,ball,xmove,blast;
    private Timer timer;
    private int time;
    private int k,l,p,x=0,y=0,m=0,z=0,f=0,q=0;
    private JButton stop;

    private boolean reset,reset2;
    Scanner scan = new Scanner(System.in);
    public GraphicsPanel(String name) {
        saw = false;
        k=0;
        l=0;
        p=0;
        reset = false;
        reset2 = false;
        this.name = name;
        click = 0;
        ez1 = new Boss("images/Boss2.png");
        ez = new Boss("images/Boss.png");
        ez2 = new Boss("images/Boss3.png");
        ez2.setHealth(10000);
        ez1.setHealth(8000);
        hp = ez.gethp();hp1 = ez1.gethp();
        won = false;
        lost = false;
        player = new Player("images/man1.png","images/man.png", name);
        try{
            blast = ImageIO.read(new File("images/blast.png"));
            slash = ImageIO.read(new File("images/slash.png"));
            ball = ImageIO.read(new File("images/ball.png"));
            stone = ImageIO.read(new File("images/Stone.png"));
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
            player.yes();
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
                player.setHealth(0);
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
            player.yes();
            won = false;
            lost = false;
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
                player.setHealth(0);
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
        }else if(ez2.gethp()>0){
            player.setScore(2);
            player.setSpeed(10);
            try {
                background = ImageIO.read(new File("images/background3.png"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            g.drawImage(background,0,0,null);
            g.drawImage(ez2.getbossImage(), 520,100,null);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Clicks: " + click,20,100);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.setColor(Color.WHITE);
            g.drawString("Boss health: " + ez2.gethp(),600,40);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString(player.getName() + "'s Score: " + player.getScore(), 20, 40);
            g.drawString("Time: " + time, 20, 70);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 24));
            g.drawString("Health: " +player.getHealth(),20,130);
            if(player.playerRect().intersects(ez2.BossRect())){
                player.setHealth(0);
            }
            if (!player.alive() && !lost) {
                g.setColor(Color.PINK);
                g.setFont(new Font("Courier New", Font.BOLD, 100));
                g.drawString("YOU LOSE!", 250, 300);
                won = true;
                player.setSpeed(0);
            }
            if(time%5==0){
                y = (int)(Math.random()*560);
                x = (int)(Math.random()*500);
                q=(int)(Math.random()*560);
                m=(int)(Math.random()*500);
                f=(int)(Math.random()*560);
                z=(int)(Math.random()*500);
            }
            if(time%5 != 0){
                    g.drawImage(blast, x, y, null);
                    g.drawImage(blast, m, q, null);
                    g.drawImage(blast, z, f, null);
                    if(player.playerRect().intersects(x,y,blast.getWidth()-40,blast.getHeight()-40) ||player.playerRect().intersects(m,q,blast.getWidth()-40,blast.getHeight()-40) || player.playerRect().intersects(z,f,blast.getWidth()-40,blast.getHeight()-40)){
                    player.minus(.2);
                }
            }
        }else{
            try{
                background = ImageIO.read(new File("images/background.png"));
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            timer.stop();
            player.setScore(3);
            g.drawImage(background,0,0,null);
            g.setFont(new Font("Courier New", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawString("You Won!", 50, 500);
            g.drawString("You win this time....", 50, 400);
            g.drawString( "Total Score: " + player.getScore(), 50, 100);
            g.drawString("Time: " + time + " seconds", 50, 200);
            g.drawString("Clicks: " + click,50,300);
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
        if(pressedKeys[67] && time>=p){
            p = time+5;
            if(player.getHealth()<=150) {
                player.heal(50);
            }else{
                player.heal(200-player.getHealth());
            }
        }

        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }
        if(pressedKeys[90] && time>=l){
            l =  time+5;
            int x= player.getxCoord();
            int y = player.getyCoord();
            while(x<=960){
                g.drawImage(zmove,x,y,null);
                x+=1;
                if(ez.BossRect().intersects(x,y,zmove.getWidth(),zmove.getHeight())){
                    ez.minusHp(1);
                }
                if(ez1.BossRect().intersects(x,y,zmove.getWidth(),zmove.getHeight())){
                    ez1.minusHp(1);
                }
                if(ez2.BossRect().intersects(x,y,zmove.getWidth(),zmove.getHeight())){
                    ez2.minusHp(1);
                }
            }
        }
        if(pressedKeys[88] && time>=k){
            k = time+5;
            g.drawImage(xmove,600,200,null);
            if(ez.gethp()>0){
                ez.minusHp(500);
            }else if(ez1.gethp()>0){
                ez1.minusHp(500);
            }else if(ez2.gethp()>0){
                ez2.minusHp((500));
            }
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
        }else if(ez2.gethp()>0){
            ez2.minus();
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