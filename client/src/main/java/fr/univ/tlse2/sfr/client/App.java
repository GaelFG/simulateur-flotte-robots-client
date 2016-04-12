package fr.univ.tlse2.sfr.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

public class App extends Application {
	private Stage fenetre_principale;
	private BorderPane root_layout;
	
	@Override
	/** Paramètre la fenêtre, initialise la connection au serveur puis affiche l'écran d'acceuil.*/
	public void start(Stage primaryStage) {
		this.fenetre_principale = primaryStage;
		parametrer_fenetre_principale();
        this.init_root_layout();
        this.fenetre_principale.show();
        this.afficher_vue_accueil_application();
	}
	
	private void parametrer_fenetre_principale(){
		this.fenetre_principale.setTitle("IHM Simulation Flotte Robots");
        this.fenetre_principale.setHeight(360);
        this.fenetre_principale.setWidth(400);
        this.fenetre_principale.setResizable(true);
	}
	
	private void init_root_layout(){
        root_layout = new BorderPane();
        Scene scene = new Scene(root_layout);
        fenetre_principale.setScene(scene);
	}
	
	 public void afficher_vue_accueil_application() {        
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(App.class.getResource("vue/accueil.fxml"));
	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
	        AnchorPane accueil;
			accueil = (AnchorPane) fxmlLoader.load();
			root_layout.getChildren().add(accueil);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	 /** Point d'entrée de l'application cliente. */
	public static void main(String[] args) {
		launch(args);
	}
}