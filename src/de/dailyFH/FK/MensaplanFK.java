package de.dailyFH.FK;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.dailyFH.Webservices.MensaplanWS;

import android.util.Log;
import android.widget.TableLayout;

public class MensaplanFK {

	String[] menueS1 = new String[30];
	String[] menueS2 = new String[30];
	String[][] menueS1S2 = new String[2][30];
	String datum5;
	private de.dailyFH.Webservices.MensaplanWS mensaplanWS;

	public String[][] updateMensaMenueNord(String menue_Auswahl, TableLayout table) {

		mensaplanWS = new MensaplanWS();
		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {
			menueS1S2 = mensaplanWS.parseFileNord(menue_Auswahl);
		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}
		return menueS1S2;
	}

	public String[][] updateMensaMenueSonne(String menue_Auswahl, TableLayout table) {
		if(mensaplanWS == null) mensaplanWS = new MensaplanWS();
		
		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {
			menueS1S2 = mensaplanWS.parseFileSonne(menue_Auswahl);
		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}
		
		return menueS1S2;
	}

	public String[][] updateMensaMenueKost(String menue_Auswahl, TableLayout table) {
		if(mensaplanWS == null) mensaplanWS = new MensaplanWS();
		
		int i = 0;
		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {
			menueS1S2 = mensaplanWS.parseFileKost(menue_Auswahl);
		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}

		return menueS1S2;
	}
}
