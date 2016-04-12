package fr.univ.tlse2.sfr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.client.controlleur.ControlleurSimulation;
import fr.univ.tlse2.sfr.communication.EtatSimulation;

public class EcouteurReseauAffichageSimulation extends Listener{
	
	private ControlleurSimulation controleur_simulation;

	public EcouteurReseauAffichageSimulation(ControlleurSimulation controleur_simulation) {
		super();
		this.controleur_simulation = controleur_simulation;
	}
	
	/** Réagit à  la reception d'un objet sérialisé. */
	 public void received (Connection connection, Object object) {

		 if (object instanceof EtatSimulation) {
			 EtatSimulation etat_simulation = (EtatSimulation)object;
			 controleur_simulation.dessiner_etat_simulation(etat_simulation);
         }
		 
      }
}
