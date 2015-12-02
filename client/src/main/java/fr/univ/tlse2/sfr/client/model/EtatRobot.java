package fr.univ.tlse2.sfr.client.model;

import fr.univ.tlse2.sfr.communication.Position;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * <b>Classe EtatRobot</b>
 * <p>Cette classe représente la classe EtatRobot de fr.univ.tlse2.sfr.communication
 * traduite en javaFX</p>
 * @author arthurgorjux
 *
 */

public class EtatRobot {
	private final IntegerProperty id_robot;
	private final ObjectProperty<Position> pos_robot;
	private final IntegerProperty orientation_robot;
	
	public EtatRobot(){
		this.id_robot = new SimpleIntegerProperty(0);
		this.pos_robot = new SimpleObjectProperty<Position>(new Position());
		this.orientation_robot = new SimpleIntegerProperty(0);
	}
	
	public EtatRobot(int id, Position pos, int orientation){
		this.id_robot = new SimpleIntegerProperty(id);
		this.pos_robot = new SimpleObjectProperty<Position>(new Position(pos));
		this.orientation_robot = new SimpleIntegerProperty(orientation);
	}
	
	public Integer get_id_robot(){
		return this.id_robot.get();
	}
	
	public void set_id_robot(int id){
		this.id_robot.set(id);
	}
	
	public Position get_position_robot(){
		return this.pos_robot.get();
	}
	
	public void set_position_robot(Position pos){
		this.pos_robot.set(pos);
	}
	
	public Integer get_orientation_robot(){
		return this.orientation_robot.get();
	}
	
	public void set_orientation_robot(int orientation){
		this.orientation_robot.set(orientation);
	}
}
