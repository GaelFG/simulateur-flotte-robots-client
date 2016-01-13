package fr.univ.tlse2.sfr.client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import fr.univ.tlse2.sfr.communication.MessageTexte;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
	
	private Client connecteur_kryo;
	private Stage primary_stage;
	private BorderPane root_layout;
	
	
	@Override
	public void start(Stage primaryStage) {
		initialiser_connecteur_kryo("127.0.0.1", 8073);
		connecteur_kryo.sendTCP(new DemarrerSimulation("mon test réseau !"));
		this.primary_stage = primaryStage;
        this.primary_stage.setTitle("IHM Simulation Flotte Robots");
        this.init_root_layout();
        this.show_simulation();
	}
	
	private void init_root_layout(){
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
	}

	 public void show_simulation() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("vue/Simulation.fxml"));
            AnchorPane simulation = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            root_layout.setCenter(simulation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		launch(args);
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
		connecteur_kryo.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof MessageTexte) {
		        	  MessageTexte message = (MessageTexte)object;
		             System.out.println("message serveur : " + message.get_contenu());
		          }
		       }
		    });
	}
}