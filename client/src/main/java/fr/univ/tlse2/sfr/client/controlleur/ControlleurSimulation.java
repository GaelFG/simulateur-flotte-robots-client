package fr.univ.tlse2.sfr.client.controlleur;

import fr.univ.tlse2.sfr.client.model.EtatRobot;
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
	
	@FXML
	private Button play;
	
	@FXML
	private Canvas canvas_simulation;
	
	@FXML
	private AnchorPane simulation;
	
	
	private ObservableList<EtatRobot> etat_robot_data = FXCollections.observableArrayList();
	
	/**
	 * The constructor (is called before the initialize()-method).
	 */
	public ControlleurSimulation() {
		// initialize map
		
		// get etatRobot list
		
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Handle Button event.
		play.setOnAction((event) -> {
			System.out.println("Button Action");
		});			
		
		this.create_canvas();
	}
	

	
	private void create_canvas(){
		double width = canvas_simulation.getWidth();
		double height = canvas_simulation.getHeight();
		System.out.println("Width : " + width);
		System.out.println("Height : " + height);
        GraphicsContext gc = canvas_simulation.getGraphicsContext2D() ;
        gc.clearRect(0, 0, width, height);
        System.out.println(gc.getFont());
        
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
        		System.out.println("y : " + y);
            	gc.setLineWidth(2.5);  
            }else {
            	 gc.setLineWidth(1);
			}
        	
        	gc.strokeLine(0, y, width - (width%15), y);
        }
	 }
}
