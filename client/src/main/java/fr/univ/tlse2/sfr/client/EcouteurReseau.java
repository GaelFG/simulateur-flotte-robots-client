package fr.univ.tlse2.sfr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.communication.MessageTexte;

/**
 * L'�scouteur r�seau du serveur. D�finit la r�action du client aux messages envoy�s.
 */
public class EcouteurReseau extends Listener{
	
	/** R�agit � la reception d'un objet s�rialis�. */
	 public void received (Connection connection, Object object) {
		 if (object instanceof MessageTexte) {
       	  MessageTexte message = (MessageTexte)object;
            System.out.println("Message serveur : " + message.get_contenu());
         }
		 
		 
      }
}
