package org.jkweb;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Random;

/**
 * JavaFX App
 */
public class App extends Application {

    private int perRun = 10000;
    private int points;
    private int dotsOverall;
    private int dotsIn;
    private int mainSize = 800;
    private int mainSpacer = 5;

    private void drawBackground(GraphicsContext gc) {
        gc.setStroke(Color.GREEN);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
        gc.fillRect(0, 0, mainSize + (mainSpacer * 2), mainSize + (mainSpacer * 2));
        gc.strokeRect(mainSpacer, mainSpacer, mainSize, mainSize);
        gc.strokeLine(mainSize + (mainSpacer * 2), (mainSize) / 2 + (mainSpacer * 2), 0, (mainSize) / 2 + (mainSpacer * 2));
        gc.strokeLine((mainSize) / 2 + (mainSpacer * 2), 0, (mainSize) / 2 + (mainSpacer * 2), mainSize + (mainSpacer * 2));
        gc.strokeOval(mainSpacer, mainSpacer, mainSize, mainSize);
    }

    public void animationTimer(GraphicsContext gc, GraphicsContext gcResults) {
        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                gc.translate(gc.getCanvas().getHeight()/2, gc.getCanvas().getHeight()/2);
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                Random ran = new Random();
                for (int i = 0; i < perRun; i++) {
                    int max = mainSize/2;
                    int min = -(mainSize/2);
                    double x = ran.nextInt((max - min) + 1) + min;
                    double y = ran.nextInt((max - min) + 1) + min;
                    double s = Math.sqrt((x*x) + (y*y));

                    if (s < mainSize/2) {
                        dotsIn++;
                        gc.setStroke(Color.YELLOWGREEN);
                    } else {
                        gc.setStroke(Color.BLUE);
                    }
                    gc.strokeLine(x, y, x, y);
                }
                dotsOverall = dotsOverall + perRun;
                points = points + perRun;
                if (points > perRun * 5000) {
                    gc.clearRect(0, 0, mainSize + (mainSpacer * 2), mainSize + (mainSpacer * 2));
                    drawBackground(gc);
                    points = 0;
                }

                gcResults.clearRect(0, 0, 300, 250);
                gcResults.strokeText("Time in Sec: " + t, 10, 20);
                gcResults.strokeText("Dots Overall: " + dotsOverall, 10, 40);
                gcResults.strokeText("Dots In: " + dotsIn, 10, 60);
                gcResults.strokeText("Estimite: " + (double) dotsIn / (double) dotsOverall, 10, 80);
            }
        }.start();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Pi | Monte-Carlo-Simulation");
        Group root = new Group();
        Canvas canvas = new Canvas(mainSize + (mainSpacer * 2), mainSize + (mainSpacer * 2));
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        drawBackground(gc);

        //stage.initStyle(StageStyle.UNDECORATED);
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));

        stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    System.exit(0);
                }
            }
        });

        // Window with results
        Stage resultStage = new Stage(StageStyle.DECORATED);
        resultStage.setTitle("Pi | Monte-Carlo-Simulation > Results");
        Group rootResultStage = new Group();
        Canvas canvasResultStage = new Canvas(300, 250);
        final GraphicsContext gcResults = canvasResultStage.getGraphicsContext2D();

        rootResultStage.getChildren().add(canvasResultStage);
        resultStage.setScene(new Scene(rootResultStage));
        resultStage.setResizable(false);

        animationTimer(gc, gcResults);
        stage.setResizable(false);
        stage.show();
        resultStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}