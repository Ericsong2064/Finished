import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Boss {
    private boolean alive;
    private int health;
    private BufferedImage boss;
    public Boss( String image){
        health = 5000;
        alive = true;
        try{
            boss = ImageIO.read(new File(image));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void minus(){
        health -= 5000;
    }
    public int gethp(){
        return health;
    }
    public void minusHp(int hp){
        health-=hp;
    }
    public void setHealth(int hp){
        health = hp;
    }
    public BufferedImage getbossImage(){
        return boss;
    }
    public Rectangle BossRect() {
        int imageHeight = getbossImage().getHeight();
        int imageWidth = getbossImage().getWidth();
        Rectangle rect = new Rectangle(600, 200, imageWidth, imageHeight);
        return rect;
    }
}