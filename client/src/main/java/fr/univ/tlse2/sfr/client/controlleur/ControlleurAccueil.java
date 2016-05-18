package fr.univ.tlse2.sfr.client.controlleur;

import java.io.File;
import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import fr.univ.tlse2.sfr.client.App;
import fr.univ.tlse2.sfr.client.EcouteurReseau;
import fr.univ.tlse2.sfr.client.EcouteurReseauAffichageSimulation;
import fr.univ.tlse2.sfr.communication.ArreterSimulation;
import fr.univ.tlse2.sfr.communication.EnregistreurKryo;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import fr.univ.tlse2.sfr.communication.ArreterSimulation;
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
	private Label label_nb_robots;
	@FXML
	private Label label_nb_obstacles;
	
	@FXML
	private TextField input_nb_robot;
	@FXML
	private TextField input_nb_obstacle;
	@FXML
	private Button parcourir;

	public static Client connecteur_kryo;
	private Parent conteneur_racine;

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
				this.afficher_vue_voir_simulation();
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
		fenetre_modale.getDialogPane().setPrefSize(640, 240);
		fenetre_modale.showAndWait();
	}
	private void afficher_vue_voir_simulation() {
			initialiser_connecteur_kryo("127.0.0.1", 8073);
        	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(App.class.getResource("vue/Simulation.fxml"));
			try {
				conteneur_racine = loader.load();
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText("Erreur lors de la lecture du fichier");
				alert.setContentText("Impossible de lire le fichier vue/Simulation.fxml");
				alert.showAndWait();
				e.printStackTrace();
			}
			Scene scene = new Scene(conteneur_racine);
			Stage stage = (Stage) manuel.getScene().getWindow();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					System.out.println("il faut arr�ter le calcul pour la simulation en cours");
					connecteur_kryo.sendTCP(new ArreterSimulation());
				}
			});
			stage.setScene(scene);
			//ajouter l'ecouteur reseau adapt�
            ControlleurSimulation controleur_affichage_simulation = loader.getController();
            connecteur_kryo.addListener(new EcouteurReseauAffichageSimulation(controleur_affichage_simulation));
			stage.show();
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
		connecteur_kryo.addListener(new EcouteurReseau());
	}

}
