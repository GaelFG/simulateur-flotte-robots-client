package fr.univ.tlse2.sfr.client.controlleur;

import fr.univ.tlse2.sfr.communication.EtatObstacle;
import fr.univ.tlse2.sfr.communication.EtatRobot;

import fr.univ.tlse2.sfr.communication.EtatSimulation;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Controlleur de la frame simulation
 */
public class ControlleurSimulation {
	@FXML
	private Button play;
	@FXML
	private Canvas canvas_simulation;
	@FXML
	private AnchorPane simulation;
	
	// les valeurs donnÃ©es pour la carte par le serveur
	// reprÃ©sente le nombre de "gros carrÃ©s" soit 25px et un robot rempli 
	// une "petite case" soit 5px par 5px et reprÃ©sente une unitÃ© de 0,2
	// exemple une carte 2x2 fera 50px par 50px
	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ControlleurSimulation() {

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
		});
	}

	// Dessine l'etatSimulation courant
	public void dessiner(EtatSimulation etat_simulation) {
		//def de variables
		double width = etat_simulation.carte.largeur * 10;
		double height = etat_simulation.carte.hauteur * 10;
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        
        //Efface la frame précédente
        gc.clearRect(0, 0, canvas_simulation.getWidth(), canvas_simulation.getHeight());
        
		// Dessiner la grille
        gc.setStroke(Color.BLACK);
        for(int x = 0; x < width - 1; x = x+ 25){
        	gc.strokeLine(x, 0, x, height);
        }
        for(int y = 0; y < height - 1; y = y + 25){ 		
    		gc.strokeLine(0, y, width, y);
    	}
        
        //dessiner les obstacles
        //System.out.println(etat_simulation.liste_obstacles);
        gc.setFill(Color.RED);
        for(EtatObstacle obstacle : etat_simulation.liste_obstacles){
        	System.out.println(obstacle.taille);
        	gc.fillRect(obstacle.position_obstacle.x*10, obstacle.position_obstacle.y*10, obstacle.taille*20, obstacle.taille*20);
        }
        
		// Dessiner les robots
        gc.setFill(Color.YELLOW);
		for(EtatRobot etat : etat_simulation.liste_robots){
            gc.fillRect(etat.pos_robot.x*10, etat.pos_robot.y*10, 10, 10);
        }
		
	}
}
