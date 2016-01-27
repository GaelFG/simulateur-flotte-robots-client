package fr.univ.tlse2.sfr.client.controlleur;

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
	
	
	
	
	// les valeurs données pour la carte par le serveur
	// représente le nombre de "gros carrés" soit 25px et un robot rempli 
	// une "petite case" soit 5px par 5px et représente une unité de 0,2
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
		double width = etat_simulation.carte.largeur * 20;
		double height = etat_simulation.carte.hauteur * 20;
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        
        //Efface la frame pr�c�dente
        gc.clearRect(0, 0, width, height);
        
		// Dessiner la grille
        gc.setStroke(Color.BLACK);
        for(int x = 0; x < width - 1; x = x+ 25){
        	gc.strokeLine(x, 0, x, height);
        }
        for(int y = 0; y < height - 1; y = y + 25){ 		
    		gc.strokeLine(0, y, width, y);
    	}
        
		// Dessiner les robots
        gc.setFill(Color.YELLOW);
		for(EtatRobot etat : etat_simulation.liste_robots){
            gc.fillRect(etat.pos_robot.x*10, etat.pos_robot.y*10, 10, 10);
        }
		
	}
}
