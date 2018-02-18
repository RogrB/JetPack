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
import javafx.scene.text.Font;


public class JetPack extends Application {
    
    // Setter Screen Size
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;  
    
    // Oppretter player og obstacle objekt
    Player player = new Player();
    Obstacle[] obstacles;
    private int upCount = 0;
    private int score = 0;
    private int collisions = 0;
    
    // Oppretter GraphicsContext og tidsvariabel
    private GraphicsContext gc;
    private double time;
    private int gameTimer;
    private Text scoreText;
    private Text lifeText;
    
    // Bakgrunnsbilde
    String imgpath = "image/background.jpg";
    Image img = new Image(imgpath);
    BackgroundImage bg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
    
    private Parent initSpace() {
        // Initialize Game
        Pane root = new Pane();
        String scoreT = "Score: " + Integer.toString(score);
        String lifeT = "Collisions: " + Integer.toString(collisions);
        scoreText = new Text(20, 20, scoreT); // Viser score øverst i venstre hjørne
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font ("Verdana", 20));
        lifeText = new Text(1000, 20, lifeT); // Viser collisions øverst i høyre hjørne
        lifeText.setFill(Color.WHITE);
        lifeText.setFont(Font.font("Verdana", 20));
        root.setPrefSize(WIDTH, HEIGHT);
        root.setBackground(new Background(bg));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas, scoreText, lifeText);
        
        createObstacles();
        // Animationtimer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.05;
                drawPlayer(gc);
                if (player.getUpDirection()) { // Funksjonalitet for Hopp
                    if (upCount < 10) {
                        player.setY(player.getY()-5);
                        drawPlayer(gc);
                        upCount++;
                    }
                    if (upCount >= 10 && upCount <= 20) { // "hangtime" mot slutten av hoppet
                        player.setY(player.getY()-1);
                        drawPlayer(gc);
                        upCount++;
                    }
                    if (upCount > 20) {
                        player.setUpDirection(false); // Hopp ferdig
                        upCount = 0;
                    }
                }
                else {
                    if (player.getY() < player.getMaxY()) { // På vei ned igjen
                        player.setY(player.getY()+3);
                        drawPlayer(gc);
                    }
                }

                if (time >= 0.35) { // Flytter obstacles til venstre på skjermen
                    for (Obstacle o : obstacles) {
                        // Tegner ny frame - obstacles
                        o.setX(o.getX()-10);
                        drawObstacle(o, gc);
                        checkCollision(o);
                        checkScore(o);
                        if (o.getX()+o.getSize() < 0) { // Når obstacles går ut av skjermen - resettet X verdi
                            o.setX(1700);
                            o.setPassed(false);
                        }
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
    }    
    
    public void createObstacles() {
        // Genererer array med obstacles
        obstacles = new Obstacle[7];
        obstacles[0] = new Obstacle(800, 550, 170);
        obstacles[1] = new Obstacle(1300, 450, 250);                
        obstacles[2] = new Obstacle(1700, 0, 300);
        obstacles[3] = new Obstacle(1700, 475, 275);
        obstacles[4] = new Obstacle(2100, 500, 375);
        obstacles[5] = new Obstacle(2475, 10, 200);
        obstacles[6] = new Obstacle(2475, 400, 200);
        //obstacles[7] = new Obstacle(2250, 550, 165);
        //obstacles[8] = new Obstacle(2450, 300, 400);
        //obstacles[9] = new Obstacle(2450, 550, 100);
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
                collisions++;
                String lText = "Collisions: " + Integer.toString(collisions);
                lifeText.setText(lText); // Skriver collisions til screen
            }
        }
        
    }
     
    public void checkScore(Obstacle o) {
        // Sjekker om objekt har passert player - legger til score
        if (o.getX()+o.getSize() < player.getX()) {
            if (!o.getPassed()) {
                o.setPassed(true);
                score++;
                String sText = "Score: " + Integer.toString(score);
                scoreText.setText(sText); // Skriver score til screen
            }
        }
    }
    
}
