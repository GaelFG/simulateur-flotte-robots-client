package fr.univ.tlse2.sfr.client.controlleur;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.client.EcouteurReseau;
import fr.univ.tlse2.sfr.communication.EtatRobot;
import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatSimulation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;




/**
 * Controlleur de la frame simulation
 */
@SuppressWarnings("restriction")
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
		// get etatRobot list
		
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
		
		this.create_canvas();
	}

	public void dessiner(EtatSimulation etat_simulation) {
		// TODO Auto-generated method stub
		
	}
	

	
	private void create_canvas(){
		double width = this.carte.largeur * 25;
		double height = this.carte.hauteur * 25;
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        gc.clearRect(0, 0, width, height);

        //gc.strokeLine(50, 50, 50, 50);
        
        /*gc.setStroke(Color.BLACK);
        for (int x = 0; x <= width; x+=15) {
            if (x % 25 == 0)  {
            	gc.setLineWidth(2.5);  
            }else {
            	 gc.setLineWidth(1);
			}
            gc.strokeLine(x, 0, x, height - (height%15));
        }
        
        gc.setStroke(Color.BLACK);
        for (int y = 0; y <= height; y+=15) {        	
        	if (y % 25 == 0) {
            	gc.setLineWidth(2.5);  
            }else {
            	 gc.setLineWidth(1);
			}
        	
        	gc.strokeLine(0, y, width - (width%15), y);
        }*/
        for(int x = 0; x < height-1; x++){
        	for(int y = 0; y < width-1; y++){
        		int abs = x * 5;
        		int ord = y * 5;
        		gc.setStroke(Color.BLACK);        		
        		gc.strokeLine(x, y, 5, 5);
        	}
        }
        gc.setFill(Color.YELLOW);
        for(EtatRobot etat : buffer_etats_simulation.get(0).liste_robots){
            int x = (int) (Math.floor(etat.pos_robot.x)+10);
            int y = (int) (Math.floor(etat.pos_robot.y)+10);
            gc.fillRect(x, y, 10, 10);
        }
        // test affichage robots
        
        for(int i = 0; i < buffer_etats_simulation.size(); i++){
        	// get list EtatRobot
        	List<EtatRobot> etatsRobots = buffer_etats_simulation.get(i).liste_robots;
        	for(EtatRobot etat : etatsRobots){
        		System.out.println("#####################");
        		System.out.println("Robot " + etat.id_robot);
        		System.out.println("x : " + etat.pos_robot.x);
        		System.out.println("y : " + etat.pos_robot.y);
        		System.out.println("#####################");
        	}
        }
	 }
}
