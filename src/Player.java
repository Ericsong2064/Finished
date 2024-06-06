import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private double health;
    private boolean alive;
    private double MOVE_AMT = 20;
    private BufferedImage right;
    private BufferedImage left;
    private boolean facingRight;
    private double xCoord;
    private double yCoord;
    private int score;
    private String name;

    public Player(String leftImg,String rightImg, String name) {
        this.name = name;
        facingRight = true;
        xCoord = 50; // starting position is (50, 435), right on top of ground
        yCoord = 435;
        score = 0;
        health = 100;
        alive = true;
        try {
            right = ImageIO.read(new File(rightImg));
            left = ImageIO.read(new File(leftImg));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void setHealth(int hp){
        health = hp;
    }
    public void minus(double hp){
        health-=hp;
        if(health<=0){
            alive = false;
        }
    }
    public int getHealth(){
        return (int)health;
    }
    public int getxCoord() {
        return (int) xCoord;
    }

    public int getyCoord() {
        return (int) yCoord;
    }
    public void no(){
        MOVE_AMT = 0.0;
    }
    public void yes(){
        MOVE_AMT = 20;
    }
    public void setSpeed(int speed){
        MOVE_AMT = speed;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score){
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public void moveRight() {
        if (xCoord + MOVE_AMT <= 920) {
            xCoord += MOVE_AMT;
        }
    }
    public boolean alive(){
        if(health>0){
            return true;
        }else{
            return false;
        }
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= 0) {
            xCoord -= MOVE_AMT;
        }
    }

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            yCoord -= MOVE_AMT;
        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += MOVE_AMT;
        }
    }

    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }


    public BufferedImage getPlayerImage() {
        if (facingRight) {
            return right;
        } else {
            return left;
        }
    }
    public void heal(int hp){
        health+=hp;
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle((int) xCoord, (int) yCoord, imageWidth, imageHeight);
        return rect;
    }
}