package fr.univ.tlse2.sfr.client.controlleur;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import fr.univ.tlse2.sfr.client.App;
import fr.univ.tlse2.sfr.client.EcouteurReseau;
import fr.univ.tlse2.sfr.client.EcouteurReseauAffichageSimulation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;

import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatRobot;
import fr.univ.tlse2.sfr.communication.EtatSimulation;
import fr.univ.tlse2.sfr.communication.MessageTexte;

public class ControlleurAccueil {
	
	@FXML
	private RadioButton manuel;
	
	@FXML
	private RadioButton auto;
	
	@FXML
	private RadioButton conf;
	
	@FXML
	private Label nom_fichier;
	
	@FXML
	private Button valider;
	
	@FXML
	private TextField input_nb_robot;
	
	@FXML
	private TextField input_nb_obstacle;
	
	@FXML
	private Button parcourir;
	
	private ObservableList<EtatRobot> etat_robot_data = FXCollections.observableArrayList();
	public static Client connecteur_kryo;
	private List<EtatSimulation> buffer_etats_simulation;
	private EtatCarte carte;
	
	private BorderPane root_layout;

	/**
	 * 
	 * Le fichier de config choisit par l'utilisateur
	 */
	private File selectedFile = null;
	
	
	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ControlleurAccueil() {
		
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		valider.setOnAction((event) -> {
			boolean error = false;
			if(auto.isSelected()){
				try{
					int value_robot = Integer.parseInt(input_nb_robot.getText());
					int value_obstacle = Integer.parseInt(input_nb_obstacle.getText());
					if(value_robot <= 0 && value_obstacle <= 0){
						this.show_error_dialog("Saisir un nombre positif de robot et d'obstacle!");
						error = true;
					}else if(value_obstacle <= 0){
						this.show_error_dialog("Saisir un nombre positif d'obstacle !");
						error = true;
					}else if(value_robot <= 0){
						this.show_error_dialog("Saisir un nombre positif de robot !");
						error = true;
					}else{
						this.init_root_layout();
					}
				}catch(Exception e){
					this.show_error_dialog("Saisir un nombre valide de robot et d'obstacle!");
					error = true;
				}
			}
			if(conf.isSelected()){
				if(this.selectedFile == null){
					this.show_error_dialog("Le fichier de configuration n'est pas choisi !");
					error = true;
				}else{
					this.init_root_layout();
				}
			}
			if(!manuel.isSelected() && !auto.isSelected() && !conf.isSelected()){
				// aucun bouton radio selectionne => affichage message erreur
				this.show_error_dialog("Choisir une option !");
				error = true;
			}else{
				if(!error){
					this.init_root_layout();
				}
			}
		});
		
		conf.setOnAction((event) -> {
			if(conf.isSelected()){
				parcourir.setDisable(false);
			}
		});
		
		manuel.setOnAction((event) -> {
			parcourir.setDisable(true);
		});
		
		auto.setOnAction((event) -> {
			parcourir.setDisable(true);
		});
		
		parcourir.setOnAction((event) -> {
			this.selectedFile = null;
			FileChooser chooser = new FileChooser();
			FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Fichiers conf (*.conf)", "*.conf");
			chooser.getExtensionFilters().add(extensionFilter);
			this.selectedFile = chooser.showOpenDialog(null);
			if(this.selectedFile != null){
				nom_fichier.setText("Fichier sélectionné : " + selectedFile.getName());
				// todo : envoyer le fichier a la fenetre de simulation => surement mettre en variable de classe
				// le fichier
			}else{
				
			}
		});
		
	}
	
	private void show_error_dialog(String text){
		Alert dialog = new Alert(Alert.AlertType.ERROR);
		dialog.setHeaderText(text);
		dialog.setResizable(true);
		dialog.getDialogPane().setPrefSize(300, 100);
		dialog.showAndWait();
	}
	private void init_root_layout(){
		// here, launch the main application
        try {
        	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("vue/RootLayout.fxml"));
			root_layout = (BorderPane) loader.load();
			Scene scene = new Scene(root_layout);
			Stage stage = (Stage) manuel.getScene().getWindow();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent event) {
					System.out.println("il faut stop le calcul pour la simulation en cours");
					connecteur_kryo.sendTCP(new MessageTexte("STOP"));
					
				}
			});
			stage.setHeight(1000);
	        stage.setWidth(1200);
	        stage.setResizable(true);
			stage.setScene(scene);
			this.show_simulation();
			stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void show_simulation(){
		// initialize network connection
		buffer_etats_simulation = Collections.synchronizedList(new LinkedList<EtatSimulation>());
		initialiser_connecteur_kryo("127.0.0.1", 8073);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(App.class.getResource("vue/Simulation.fxml"));
	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            AnchorPane simulation = (AnchorPane) fxmlLoader.load();
            root_layout.setCenter(simulation);

    		//ajouter l'ecouteur reseau adapté
            ControlleurSimulation controleur_affichage_simulation = fxmlLoader.getController();
            connecteur_kryo.addListener(new EcouteurReseauAffichageSimulation(controleur_affichage_simulation));
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
	private void show_progress_bar(){
		ProgressBar bar = new ProgressBar(0);
	    bar.setPrefSize(200, 24);

	    Timeline task = new Timeline(
	        new KeyFrame(
	                Duration.ZERO,       
	                new KeyValue(bar.progressProperty(), 0)
	        ),
	        new KeyFrame(
	                Duration.seconds(2), 
	                new KeyValue(bar.progressProperty(), 1)
	        )
	    );
	    //todo calcul du lancement du serveur
	    int i = 0;
	    while (i< 100000){
	    	task.playFromStart();
	    	i++;
	    }
	    this.init_root_layout();
	    Label loading = new Label("Initialisation du serveur");

	    VBox layout = new VBox(10);
	    layout.getChildren().setAll(
	        loading, bar
	    );
	    layout.setPadding(new Insets(10));
	    layout.setAlignment(Pos.CENTER);
	    layout.getStylesheets().add(getClass().getResource("../vue/css/progress_bar.css").toExternalForm());
	    //Stage stage = (Stage) manuel.getScene().getWindow();
	    Stage stage_progress_bar = new Stage();
	    stage_progress_bar.setHeight(100);
	    stage_progress_bar.setWidth(200);
        stage_progress_bar.setScene(new Scene(layout));
        stage_progress_bar.show();
	}
}
