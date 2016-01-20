package fr.univ.tlse2.sfr.client.controlleur;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.client.EcouteurReseau;
import fr.univ.tlse2.sfr.client.model.EtatRobot;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;




/**
 * Controlleur de la frame simulation
 */
@SuppressWarnings("restriction")
public class ControlleurSimulation {
	
	private Client connecteur_kryo;
	private List<EtatSimulation> buffer_etats_simulation;
	
	private EtatCarte carte;
	
	@FXML
	private Button play;
	
	@FXML
	private Canvas canvas_simulation;
	
	@FXML
	private AnchorPane simulation;
	
	
	private ObservableList<EtatRobot> etat_robot_data = FXCollections.observableArrayList();
	
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
	
	private void initialiser_connecteur_kryo(String url_serveur, int port_tcp) {
		connecteur_kryo = new Client();
		connecteur_kryo.start();
		EnregistreurKryo.enregistrerLesClassesDeCommunication(connecteur_kryo.getKryo());
		definir_ecouteur_kryo();
		try {
			connecteur_kryo.connect(5000, url_serveur, port_tcp);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void definir_ecouteur_kryo() {
		connecteur_kryo.addListener(new EcouteurReseau(buffer_etats_simulation));
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
		// initialize network connection
		buffer_etats_simulation = Collections.synchronizedList(new LinkedList<EtatSimulation>());
		initialiser_connecteur_kryo("127.0.0.1", 8073);
		connecteur_kryo.sendTCP(new DemarrerSimulation("mon test réseau !"));
		while(buffer_etats_simulation.isEmpty()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		// initialize map
		this.carte = buffer_etats_simulation.get(0).carte;
		
		
		
		
		// Handle Button event.
		play.setOnAction((event) -> {
			System.out.println("Button Action");
		});			
		
		this.create_canvas();
	}
	

	
	private void create_canvas(){
		/*double width = canvas_simulation.getWidth();
		double height = canvas_simulation.getHeight();*/
		double width = this.carte.largeur * 25;
		double height = this.carte.hauteur * 25;
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        gc.clearRect(0, 0, width, height);
        
        gc.setStroke(Color.BLACK);
        for (int x = 0; x <= width; x+=15) {
            if (x % 75 == 0)  {
            	gc.setLineWidth(2.5);  
            }else {
            	 gc.setLineWidth(1);
			}
            gc.strokeLine(x, 0, x, height - (height%15));
        }
        
        gc.setStroke(Color.BLACK);
        for (int y = 0; y <= height; y+=15) {        	
        	if (y % 75 == 0) {
            	gc.setLineWidth(2.5);  
            }else {
            	 gc.setLineWidth(1);
			}
        	
        	gc.strokeLine(0, y, width - (width%15), y);
        }
	 }
}
