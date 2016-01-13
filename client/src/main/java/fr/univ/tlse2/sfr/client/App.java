package fr.univ.tlse2.sfr.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
	
	private Stage primary_stage;
	private BorderPane root_layout;
	
	
	@Override
	public void start(Stage primaryStage) {
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
            loader.setLocation(App.class.getResource("vue/test.fxml"));
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
}