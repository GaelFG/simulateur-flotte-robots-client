package fr.univ.tlse2.sfr.client.controlleur;

import fr.univ.tlse2.sfr.communication.EtatObstacle;

import java.awt.Event;
import java.util.List;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.communication.AjouterObstacle;
import fr.univ.tlse2.sfr.communication.AjouterRobot;
import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatRobot;

import fr.univ.tlse2.sfr.communication.EtatSimulation;
import fr.univ.tlse2.sfr.communication.ParametresSimulation;
import fr.univ.tlse2.sfr.communication.PauseSimulation;
import fr.univ.tlse2.sfr.communication.Position;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Controlleur de la frame simulation
 */
public class ControlleurSimulation {
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
	private GraphicsContext contexte_graphique_du_canvas;
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
		this.set_connecteur_kryo(ControlleurAccueil.connecteur_kryo);
		contexte_graphique_du_canvas = canvas_simulation.getGraphicsContext2D();
		canvas_simulation.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
            	final ContextMenu contextMenu = new ContextMenu();
            	//TODO: gérer le dépop des fenêtres...
                if (t.getClickCount() > 1) { //double click
                    MenuItem ajouter_robot = new MenuItem("Ajouter robot");
                    ajouter_robot.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							System.out.println("on veut ajouter un robot");
							connecteur_kryo.sendTCP(new AjouterRobot(new Position(t.getX(),t.getY())));
						}
                    });
                    
                    MenuItem ajouter_obstacle = new MenuItem("Ajouter obstacle");
                    ajouter_obstacle.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							System.out.println("on veut ajouter un obstacle");
							connecteur_kryo.sendTCP(new AjouterObstacle(new Position(t.getX(),t.getY())));
						}
                    });
                    
                    contextMenu.getItems().addAll(ajouter_robot,ajouter_obstacle);                  
                    contextMenu.show(canvas_simulation, t.getScreenX(), t.getScreenY());
                }  
            }
        });
	}
	
	public void set_connecteur_kryo(Client connecteur_kryo)
	{
		this.connecteur_kryo = connecteur_kryo;
	}

	public void dessiner_etat_simulation(EtatSimulation etat_simulation) {
        //Efface la frame précédente
		contexte_graphique_du_canvas.clearRect(0, 0, canvas_simulation.getWidth(), canvas_simulation.getHeight());
        dessiner_carte(etat_simulation.carte);
        dessiner_robots(etat_simulation.liste_robots);
        dessiner_obstacles(etat_simulation.liste_obstacles);
	}

	private void dessiner_carte(EtatCarte etat_carte) {
		contexte_graphique_du_canvas.setStroke(Color.DARKGREY);
        double width = etat_carte.largeur;
		double height = etat_carte.hauteur;
        
        for(int x = 0; x <= width; x = x + 16){
        	contexte_graphique_du_canvas.strokeLine(x, 0, x, height);
        }
        for(int y = 0; y <= height; y = y+ 16){ 		
        	contexte_graphique_du_canvas.strokeLine(0, y, width, y);
    	}

	}
	
	private void dessiner_obstacles(List<EtatObstacle> obstacles){    
		contexte_graphique_du_canvas.setFill(Color.DIMGREY);
        for(EtatObstacle obstacle : obstacles){
        	contexte_graphique_du_canvas.fillRect((obstacle.position_obstacle.x - obstacle.taille), (obstacle.position_obstacle.y - obstacle.taille), obstacle.taille*2, obstacle.taille*2);
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
		contexte_graphique_du_canvas.setFill(Color.YELLOW);
		for(EtatRobot etat : robots){
			drawRotatedImage(contexte_graphique_du_canvas, sprite_blatte_a, etat.orientation_robot+90, etat.pos_robot.x, etat.pos_robot.y);
        }
	}
	
	public void reagir_action_bouton_demarrer() {
		DemarrerSimulation lancement_simu = new DemarrerSimulation(new ParametresSimulation("simulation", 10, 10));
		this.connecteur_kryo.sendTCP(lancement_simu);
	}
	
	public void reagir_action_bouton_ralentir() {
		afficher_fenetre_modale_d_erreur("La fonctionnalité 'Ralentir la simulation' n'est pas implémentée.");	
	}
	
	public void reagir_action_bouton_accelerer() {
		afficher_fenetre_modale_d_erreur("La fonctionnalité 'Accélerer la simulation' n'est pas implémentée.");	
	}
	
	public void reagir_action_bouton_pause() {
		PauseSimulation pause_simu = new PauseSimulation();
		this.connecteur_kryo.sendTCP(pause_simu);
	}
	
	private void afficher_fenetre_modale_d_erreur(String message){
		Alert fenetre_modale = new Alert(Alert.AlertType.ERROR);
		fenetre_modale.setHeaderText(message);
		fenetre_modale.setResizable(true);
		fenetre_modale.getDialogPane().setPrefSize(640, 240);
		fenetre_modale.showAndWait();
	}
}
