package jetpack;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler ;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;
import java.util.concurrent.ThreadLocalRandom;


public class JetPack extends Application {
    
    // Setter Screen Size
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;  
    
    // Oppretter player og obstacle objekt
    Player player = new Player();
    Obstacle[] obstacles;

    
    // Oppretter GraphicsContext og tidsvariabel
    private GraphicsContext gc;
    private double time;
    private int gameTimer;
    
    // Bakgrunnsbilde
    String imgpath = "image/background.jpg";
    Image img = new Image(imgpath);
    BackgroundImage bg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
    
    private Parent initSpace() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        root.setBackground(new Background(bg));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();


        root.getChildren().addAll(canvas);
        
        createObstacles();
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.05;

                if (time >= 0.35) {
                    // Tegner ny frame - player
                    drawPlayer(gc);
                    
                    for (Obstacle o : obstacles) {
                        // Tegner ny frame - obstacles
                        o.setX(o.getX()-10);
                        drawObstacle(o, gc);
                        checkCollision(o);
                        if (o.getX()+o.getSize() < 0) {
                            o.setX(1700);
                        }
                    }
                    
                    if (!player.getUpDirection()) { // Bevegelse - player
                        if (player.getY() < player.getMaxY()) {
                            // Svever ned mot bakken - så lenge man fortsatt er over bakken
                            player.setY(player.getY()+10);
                        }
                    }
                    else { // Bevegelse - player
                        // Fortsatt på vei opp etter hoppet
                        player.setY(player.getY()-25);
                        player.setUpDirection(false);
                    }
                    time = 0;
                }
            }
        };
        timer.start();          
        // gameTimer();
        return root;
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Start Metode - Setter opp Scene
        primaryStage.setTitle("JetPack");
        // oppretter rootpane, canvas og scene        
        Scene scene = new Scene(initSpace());

        primaryStage.setScene(scene);
        primaryStage.show();

        // Key event handler for scene -> pressed SPACE
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    jump(gc);
                }
            }
        });        
    }
    
    /* public void gameTimer() {
        for (;;) {
            try { 
                Thread.sleep(1000); 
                gameTimer++;
                System.out.println(gameTimer);
                if (gameTimer == 5) {
                    createObstacle();
                }
            }
            catch (InterruptedException e) { 
                e.printStackTrace(); 
            } 
        }
    }*/

    public static void main(String[] args) {
        launch(args);
    }
    
    public void drawPlayer(GraphicsContext gc) {
        // Metode som tegner Player
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.clearRect(player.getX()-1, player.getOldY()-1, player.getSize()+3, player.getSize()+3);
        gc.strokeRect(player.getX(), player.getY(), player.getSize(), player.getSize());
        player.setOldY(player.getY());
    }
    
    public void jump(GraphicsContext gc) {
        // HOPP!
        player.setUpDirection(true);
        player.setY(player.getY() - 25);
        drawPlayer(gc);
    }    
    
    public void createObstacles() {
        // Genererer array med obstacles - Kan gjøres random
        obstacles = new Obstacle[10];
        
        int min = 1900;
        
        for (int i = 0; i < obstacles.length; i++) {
            if (i == 0) {
                obstacles[i] = new Obstacle(800, 550, 170);
            }
            if (i == 1) {
                obstacles[i] = new Obstacle(1300, 450, 250);                
            }
            if (i == 2) {
                obstacles[i] = new Obstacle(1700, 0, 300);
            }
            if (i == 3) {
                obstacles[i] = new Obstacle(1700, 475, 275);
            }
            if (i > 3) {
                int x = ThreadLocalRandom.current().nextInt(min, min+300 + 1);
                int y = ThreadLocalRandom.current().nextInt(1, 700);
                int size = ThreadLocalRandom.current().nextInt(1, 350);
                obstacles[i] = new Obstacle(x, y, size);
                min = x;
            }
        }
    }
    
    public void drawObstacle(Obstacle o, GraphicsContext gc) {
        // Tegner obstacle
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.clearRect(o.getOldX()-1, o.getY()-1, o.getSize()+3, o.getSize()+3);
        gc.strokeRect(o.getX(), o.getY(), o.getSize(), o.getSize());
        o.setOldX(o.getX());
    }
    
    public void checkCollision(Obstacle o) {
        // Sjekker Kollisjon mellom spiller og obstacle
        if (o.getX() < player.getX()+player.getSize() && o.getY() < player.getY()+player.getSize()) {
            if (player.getX() < o.getX()+o.getSize() && player.getY() < o.getY()+o.getSize()) {
                System.out.println("kollision");
            }
        }
    }
    
}
