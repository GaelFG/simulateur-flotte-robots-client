package fr.univ.tlse2.sfr.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

public class App extends Application {
	private Stage fenetre_principale;
	private Parent conteneur_racine;
	
	@Override
	/** Paramètre la fenêtre, initialise la connection au serveur puis affiche l'écran d'acceuil.*/
	public void start(Stage primaryStage) throws IOException {
		this.fenetre_principale = primaryStage;
		parametrer_fenetre_principale();
        this.fenetre_principale.show();
        this.afficher_vue_accueil_application();
	}
	
	private void parametrer_fenetre_principale(){
		this.fenetre_principale.setTitle("IHM Simulation Flotte Robots");
        this.fenetre_principale.setResizable(true);
	}
	
	 public void afficher_vue_accueil_application() throws IOException {
			conteneur_racine = FXMLLoader.load(getClass().getResource("vue/accueil.fxml"));
			Scene scene = new Scene(conteneur_racine, 640, 480);
	        fenetre_principale.setScene(scene);
    }
	 
	public void afficher_vue_voir_simulation() throws IOException {
			//TODO
}

	 /** Point d'entrée de l'application cliente. */
	public static void main(String[] args) {
		launch(args);
	}
}