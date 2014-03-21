package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import GUI.mainWindow;
import Kartendarstellung.SEPSlippyMap;

public class listener implements ActionListener{

	JComboBox fahrzeugetyp;

	public void actionPerformed(ActionEvent e) {
		
		JComboBox fahrzeugetyp = (JComboBox)e.getSource();
        String gewaehlt = (String)fahrzeugetyp.getSelectedItem().toString();
        
     // TODO Hier muss durch Jonathan an den entsprechenden
		// Stellen die jeweilige Methode aufgerufen werden...


						if (gewaehlt.equals("Alle Fahrzeuge")) {
							System.out.println("Alle Fahrzeuge wurden gewählt");
							mainWindow.kartenAnzeige.setAnzeigeFilter_fahrzeugnr(0);
						} else {
							System.out.println(gewaehlt + "wurde ausgewaehlt");
							mainWindow.kartenAnzeige.setAnzeigeFilter_fahrzeugnr(Integer.parseInt((String) gewaehlt));
						}
		
	}

}
