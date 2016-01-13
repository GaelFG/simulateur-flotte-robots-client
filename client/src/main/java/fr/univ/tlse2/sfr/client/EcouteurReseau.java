package fr.univ.tlse2.sfr.client;

import java.util.List;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.communication.EtatSimulation;
import fr.univ.tlse2.sfr.communication.MessageTexte;

/**
 * L'écouteur réseau du serveur. Définit la réaction du serveur aux messages envoyés.
 */
public class EcouteurReseau extends Listener{
	
	private List<EtatSimulation> buffer_etats_simulation;

	public EcouteurReseau(List<EtatSimulation> buffer_etats_simulation) {
		super();
		this.buffer_etats_simulation = buffer_etats_simulation;
	}
	
	/** Réagit à la reception d'un objet sérialisé. */
	 public void received (Connection connection, Object object) {

		 if (object instanceof EtatSimulation) {
			 EtatSimulation etat_simulation = (EtatSimulation)object;
			 buffer_etats_simulation.add(etat_simulation);
         }
		 
		 if (object instanceof MessageTexte) {
       	  MessageTexte message = (MessageTexte)object;
            System.out.println("Message serveur : " + message.get_contenu());
         }
		 
		 
      }
}
