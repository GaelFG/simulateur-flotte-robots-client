package fr.univ.tlse2.sfr.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import fr.univ.tlse2.sfr.communication.MessageTexte;

/**
 * L'éscouteur réseau du serveur. Définit la réaction du client aux messages envoyés.
 */
public class EcouteurReseau extends Listener{
	
	/** Réagit à la reception d'un objet sérialisé. */
	 public void received (Connection connection, Object object) {
		 if (object instanceof MessageTexte) {
       	  MessageTexte message = (MessageTexte)object;
            System.out.println("Message serveur : " + message.get_contenu());
         }
		 
		 
      }
}
