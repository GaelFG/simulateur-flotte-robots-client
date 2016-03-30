package fr.univ.tlse2.sfr.client.controlleur;

import fr.univ.tlse2.sfr.communication.EtatObstacle;
import java.util.List;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatRobot;

import fr.univ.tlse2.sfr.communication.EtatSimulation;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Controlleur de la frame simulation
 */
public class ControlleurSimulation {
	
	private final static int FACTEUR_GROSSISSEMENT = 1;
	
	// Bouton play
	@FXML
	private Button play;
	// Bouton pause
	@FXML
	private Button pause;
	// Bouton >>
	@FXML
	private Button acc;
	// Bouton <<
	@FXML
	private Button slow;
	@FXML
	private Canvas canvas_simulation;
	@FXML
	private AnchorPane simulation;
	
	private Image sprite_blatte_a;
	private Client connecteur_kryo;
	
	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ControlleurSimulation() {
		sprite_blatte_a = new Image("/blatte.png", true);
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Handle Button event.
		play.setOnAction((event) -> {
			System.out.println("Button Action");
			DemarrerSimulation lancement_simu = new DemarrerSimulation("simulation");
			connecteur_kryo.sendTCP(lancement_simu);
		});
		
		
	}
	
	public void set_connecteur_kryo(Client connecteur_kryo)
	{
		this.connecteur_kryo = connecteur_kryo;
	}

	// Dessine l'etatSimulation courant
	public void dessiner(EtatSimulation etat_simulation) {
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        
        //Efface la frame précédente
        gc.clearRect(0, 0, canvas_simulation.getWidth(), canvas_simulation.getHeight());
        
        dessiner_carte(etat_simulation.carte);
        dessiner_robots(etat_simulation.liste_robots);
        dessiner_obstacles(etat_simulation.liste_obstacles);
	}

	private void dessiner_carte(EtatCarte etat_carte) {
		GraphicsContext gc = canvas_simulation.getGraphicsContext2D();
        gc.setStroke(Color.DARKGREY);
        double width = etat_carte.largeur;
		double height = etat_carte.hauteur;
        
        for(int x = 0; x <= width; x = x + 16){
        	gc.strokeLine(x, 0, x, height);
        }
        for(int y = 0; y <= height; y = y+ 16){ 		
    		gc.strokeLine(0, y, width, y);
    	}

	}
	
	private void dessiner_obstacles(List<EtatObstacle> obstacles){
		GraphicsContext gc = canvas_simulation.getGraphicsContext2D();
		//dessiner les obstacles        
        gc.setFill(Color.DIMGREY);
        for(EtatObstacle obstacle : obstacles){
        	gc.fillRect((obstacle.position_obstacle.x - obstacle.taille), (obstacle.position_obstacle.y - obstacle.taille), obstacle.taille*2, obstacle.taille*2);
        }
	}
	
	 /**
     * Sets the transform for the GraphicsContext to rotate around a pivot point.
     *
     * @param gc the graphics context the transform to applied to.
     * @param angle the angle of rotation.
     * @param px the x pivot co-ordinate for the rotation (in canvas co-ordinates).
     * @param py the y pivot co-ordinate for the rotation (in canvas co-ordinates).
     */
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
	/**
     * Draws an image on a graphics context.
     *
     * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the point:
     *   (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
     *
     * @param gc the graphics context the image is to be drawn on.
     * @param angle the angle of rotation.
     * @param tlpx the top left x co-ordinate where the image will be plotted (in canvas co-ordinates).
     * @param tlpy the top left y co-ordinate where the image will be plotted (in canvas co-ordinates).
     */
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }
	
	private void dessiner_robots(List<EtatRobot> robots){
		GraphicsContext gc = canvas_simulation.getGraphicsContext2D();
		gc.setFill(Color.YELLOW);
		for(EtatRobot etat : robots){
			drawRotatedImage(gc, sprite_blatte_a, etat.orientation_robot+90, etat.pos_robot.x, etat.pos_robot.y);
        }
	}
}
