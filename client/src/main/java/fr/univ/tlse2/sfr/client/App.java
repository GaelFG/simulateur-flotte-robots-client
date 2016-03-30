package fr.univ.tlse2.sfr.client;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

@SuppressWarnings("restriction")
public class App extends Application {
	
	private Stage primary_stage;
	private BorderPane root_layout;
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primary_stage = primaryStage;
        this.primary_stage.setTitle("IHM Simulation Flotte Robots");
        this.init_root_layout();
        this.show_choice();
        this.primary_stage.setHeight(360);
        this.primary_stage.setWidth(400);
        this.primary_stage.setResizable(false);
        this.primary_stage.show();
	}
	
	private void init_root_layout(){
		// Initialize root layout
        root_layout = new BorderPane();

        // Show the scene containing the root layout.
        Scene scene = new Scene(root_layout);
        primary_stage.setScene(scene);
	}

	 public void show_choice() {        
		try {
			final ToggleGroup group = new ToggleGroup();
			
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
	
	public static void main(String[] args) {
		launch(args);
	}
}