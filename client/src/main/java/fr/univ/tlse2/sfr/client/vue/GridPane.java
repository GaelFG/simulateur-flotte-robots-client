package fr.univ.tlse2.sfr.client.vue;

import java.util.List;

import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatSimulation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GridPane extends Pane {
	
	private static final double SPACING_X = 25;
    private static final double SPACING_Y = 20;
    private static final double RADIUS = 1.5;
    private Canvas canvas = new Canvas();
    private double width = 0;
    private double height = 0;
    private List<EtatSimulation> buffer_etats_simulation;
    
    public GridPane(EtatCarte carte, List<EtatSimulation> buffer_etats_simulation) {
    	this.buffer_etats_simulation = buffer_etats_simulation;
    	this.width = carte.largeur*25;
        this.height = carte.hauteur*25;
        this.getChildren().add(canvas);
    }
    
    private double snap(double y) {
        return ((int) y) + 0.5;
    }
    
    @Override 
    protected void layoutChildren() {
        final int top = (int)snappedTopInset();
        final int right = (int)snappedRightInset();
        final int bottom = (int)snappedBottomInset();
        final int left = (int)snappedLeftInset();
        final int w = (int)width - left - right;
        final int h = (int)height - top - bottom;
        
        canvas.setLayoutX(left);
        canvas.setLayoutY(top);
        
        if (w != canvas.getWidth() || h != canvas.getHeight()) {
            canvas.setWidth(width);
            canvas.setHeight(height);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(1); // change the line width

            gc.clearRect(0, 0, w, h);
            gc.setStroke(Color.GRAY);
            gc.setFill(Color.GRAY);
 
            for (int x = 0; x < w; x += SPACING_X) {
                for (int y = 0; y < h; y += SPACING_Y) {
                    double offsetY = (y%(2*SPACING_Y)) == 0 ? SPACING_X /2 : 0;
                    gc.fillOval(x-RADIUS+offsetY,y-RADIUS,RADIUS+RADIUS,RADIUS+RADIUS);
                }
            }
        }
        
        // draw robots
        this.drawRobots(height, width);
    }
    
    private void drawRobots(double heigth, double width){
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.setLineWidth(5);    	
    }

}
