package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import GUI.mainWindow;
import Kartendarstellung.SEPSlippyMap;

public class listenerRoute implements ActionListener{

	final JPopupMenu anfahrtspunkte = new JPopupMenu();
	

	public void actionPerformed(ActionEvent e) {
		
        String gewaehlt = e.getActionCommand();
        
        if (gewaehlt.equals("Setze Fahrt") && GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr() != 0) {
        	GUI.mainWindow.kartenAnzeige.setFahrt(GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr());
		} else if (gewaehlt.equals("Setze Betriebsfahrt") && GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr() != 0) {
			GUI.mainWindow.kartenAnzeige.setBetriebsfahrt(GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr());
		} else if (gewaehlt.equals("Loesche Fahrt") && GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr() != 0) {
			GUI.mainWindow.kartenAnzeige.entfaerbeKante(GUI.mainWindow.kartenAnzeige.getAnzeigeFilter_fahrzeugnr());
		} else { //sonst Fehlermessage ausgeben "Fahrzeug wählen"
			JOptionPane.showMessageDialog(null, "Bitte waehlen Sie ein Fahrzeug aus!", "Eintragung nicht moeglich", JOptionPane.WARNING_MESSAGE);
		}
		
	}

}