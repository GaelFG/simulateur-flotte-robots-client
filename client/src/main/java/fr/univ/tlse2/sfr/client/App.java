package fr.univ.tlse2.sfr.client;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.client.vue.GridPane;
import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatRobot;
import fr.univ.tlse2.sfr.communication.EtatSimulation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

@SuppressWarnings("restriction")
public class App extends Application {
	
	private Stage primary_stage;
	private BorderPane root_layout;
	
	private ObservableList<EtatRobot> etat_robot_data = FXCollections.observableArrayList();
	private Client connecteur_kryo;
	private List<EtatSimulation> buffer_etats_simulation;
	private EtatCarte carte;
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primary_stage = primaryStage;
        this.primary_stage.setTitle("IHM Simulation Flotte Robots");
        //this.init_root_layout();
        Scene scene = new Scene(this.show_simulation(), Color.WHITE);
        this.primary_stage.setScene(scene);
        this.primary_stage.show();
	}
	
	/*private void init_root_layout(){
		try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("vue/RootLayout.fxml"));
            root_layout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(root_layout);
            primary_stage.setScene(scene);
            primary_stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}*/

	 public GridPane show_simulation() {
		 
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
		return new GridPane(carte, buffer_etats_simulation);
        /*try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("vue/test.fxml"));
            AnchorPane simulation = (AnchorPane) loader.load();
            
            
            simulation.getChildren().add(grid);
            
            root_layout.setCenter(simulation);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
	
	public static void main(String[] args) {
		launch(args);
	}
}