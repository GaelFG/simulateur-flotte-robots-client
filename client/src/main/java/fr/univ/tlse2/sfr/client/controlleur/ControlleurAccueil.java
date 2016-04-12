package fr.univ.tlse2.sfr.client.controlleur;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.esotericsoftware.kryonet.Client;
import fr.univ.tlse2.sfr.client.App;
import fr.univ.tlse2.sfr.client.EcouteurReseau;
import fr.univ.tlse2.sfr.client.EcouteurReseauAffichageSimulation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import fr.univ.tlse2.sfr.communication.ArreterSimulation;
import fr.univ.tlse2.sfr.communication.DemarrerSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import fr.univ.tlse2.sfr.communication.EtatCarte;
import fr.univ.tlse2.sfr.communication.EtatRobot;
import fr.univ.tlse2.sfr.communication.EtatSimulation;


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
	private Label label_nb_robots;
	@FXML
	private Label label_nb_obstacles;
	
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
	 * Le fichier de config choisit par l'utilisateur
	 */
	private File selectedFile = null;
	
	public ControlleurAccueil() {
		
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		conf.setOnAction((event) -> {
			parcourir.setDisable(false);
			nom_fichier.setDisable(false);
			label_nb_obstacles.setDisable(true);
			label_nb_robots.setDisable(true);
			input_nb_obstacle.setDisable(true);
			input_nb_robot.setDisable(true);
		});
		manuel.setOnAction((event) -> {
			parcourir.setDisable(true);
			nom_fichier.setDisable(true);
			label_nb_obstacles.setDisable(true);
			label_nb_robots.setDisable(true);
			input_nb_obstacle.setDisable(true);
			input_nb_robot.setDisable(true);
		});
		auto.setOnAction((event) -> {
			parcourir.setDisable(true);
			nom_fichier.setDisable(true);
			label_nb_obstacles.setDisable(false);
			label_nb_robots.setDisable(false);
			input_nb_obstacle.setDisable(false);
			input_nb_robot.setDisable(false);
		});
		definir_action_bouton_parcourir();
		definir_action_bouton_lancer_simulation();
	}
	
	private void definir_action_bouton_lancer_simulation() {
		valider.setOnAction((event) -> {
			
			if (manuel.isSelected()) {
				this.init_root_layout();
			}
			
			if (auto.isSelected()) {
				try{
					int value_robot = Integer.parseInt(input_nb_robot.getText());
					int value_obstacle = Integer.parseInt(input_nb_obstacle.getText());
					if(value_robot <= 0 && value_obstacle <= 0){
						this.afficher_fenetre_modale_d_erreur("Saisir un nombre positif de robots et d'obstacles !");
					}else if(value_obstacle <= 0){
						this.afficher_fenetre_modale_d_erreur("Saisir un nombre positif d'obstacles !");
					}else if(value_robot <= 0){
						this.afficher_fenetre_modale_d_erreur("Saisir un nombre positif de robots !");
					}else{
						afficher_fenetre_modale_d_erreur("La fonctionnalit� 'Param�trer le nombre de robots et d'obstacles sur la simulation' n'est pas impl�ment�e.");
					}
				}catch(Exception e){
					this.afficher_fenetre_modale_d_erreur("Saisir un nombre valide de robots et d'obstacles !");
				}
			}
			
			if(conf.isSelected()){
				if(this.selectedFile == null){
					this.afficher_fenetre_modale_d_erreur("Aucun fichier configuration valide s�lectionn� !");
				}else{
					afficher_fenetre_modale_d_erreur("La fonctionnalit� 'Charger des param�tres de simulation depuis un fichier externe' n'est pas impl�ment�e.");
				}
			}
		});
	}
	
	private void definir_action_bouton_parcourir() {
		parcourir.setOnAction((event) -> {
			this.selectedFile = null;
			FileChooser chooser = new FileChooser();
			FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Fichiers conf (*.conf)", "*.conf");
			chooser.getExtensionFilters().add(extensionFilter);
			this.selectedFile = chooser.showOpenDialog(null);
			if(this.selectedFile != null){
				nom_fichier.setText("Fichier s�lectionn� : " + selectedFile.getName());
				// todo : envoyer le fichier a la fenetre de simulation => surement mettre en variable de classe
				// le fichier
			}else{
				
			}
		});
	}
	
	private void afficher_fenetre_modale_d_erreur(String message){
		Alert fenetre_modale = new Alert(Alert.AlertType.ERROR);
		fenetre_modale.setHeaderText(message);
		fenetre_modale.setResizable(true);
		fenetre_modale.getDialogPane().setPrefSize(300, 100);
		fenetre_modale.showAndWait();
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
					connecteur_kryo.sendTCP(new ArreterSimulation());
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

}
