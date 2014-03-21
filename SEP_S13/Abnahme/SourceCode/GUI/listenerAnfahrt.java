package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import GUI.mainWindow;
import Kartendarstellung.SEPSlippyMap;

public class listenerAnfahrt implements ActionListener{

	final JPopupMenu anfahrtspunkte = new JPopupMenu();
	

	public void actionPerformed(ActionEvent e) {
		
        String gewaehlt = e.getActionCommand();
        double x = GUI.mainWindow.x;
        double y = GUI.mainWindow.y;
        
     // TODO Hier muss durch Jonathan an den entsprechenden
	// Stellen die jeweilige Methode aufgerufen werden...

        if (gewaehlt.equals("Fahrzeugdepot")) {
			Anfahrtspunkte.punkt_eintragen(x, y , gewaehlt);
    		GUI.mainWindow.kartenAnzeige.aktualisiereDarstellung();
		} else if (gewaehlt.equals("Muellentleerungsort (Plastik)")) {
			Anfahrtspunkte.punkt_eintragen(x, y, gewaehlt);
			GUI.mainWindow.kartenAnzeige.aktualisiereDarstellung();
		} else if (gewaehlt.equals("Muellentleerungsort (Papier)")) {
			Anfahrtspunkte.punkt_eintragen(x, y, gewaehlt);
			GUI.mainWindow.kartenAnzeige.aktualisiereDarstellung();
		} else if (gewaehlt.equals("Muellentleerungsort (Restmuell)")) {
			Anfahrtspunkte.punkt_eintragen(x, y, gewaehlt);
			GUI.mainWindow.kartenAnzeige.aktualisiereDarstellung();
		} else if (gewaehlt.equals("Muellentleerungsort (Biomuell)")) {
			Anfahrtspunkte.punkt_eintragen(x, y, gewaehlt);
			GUI.mainWindow.kartenAnzeige.aktualisiereDarstellung();
		}

		
	}

}
