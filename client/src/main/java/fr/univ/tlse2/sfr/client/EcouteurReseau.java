package fr.univ.tlse2.sfr.client;

import java.util.List;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.communication.EtatSimulation;
import fr.univ.tlse2.sfr.communication.MessageTexte;

/**
 * L'�scouteur r�seau du serveur. D�finit la r�action du serveur aux messages envoy�s.
 */
public class EcouteurReseau extends Listener{
	
	private List<EtatSimulation> buffer_etats_simulation;

	public EcouteurReseau(List<EtatSimulation> buffer_etats_simulation) {
		super();
		this.buffer_etats_simulation = buffer_etats_simulation;
	}
	
	/** R�agit � la reception d'un objet ss�rialis�. */
	 public void received (Connection connection, Object object) {
		 
		 if (object instanceof MessageTexte) {
       	  MessageTexte message = (MessageTexte)object;
            System.out.println("Message serveur : " + message.get_contenu());
         }
		 
		 
      }
}
