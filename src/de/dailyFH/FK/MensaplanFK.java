package de.dailyFH.FK;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;
import android.widget.TableLayout;

public class MensaplanFK {

	String[] menueS1 = new String[30];
	String[] menueS2 = new String[30];
	String datum5;

	public void updateMensaMenueNord(String menue_Auswahl, TableLayout table) {

		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {

			parseFileNord(menue_Auswahl);

		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}
	}

	public void updateMensaMenueSonne(String menue_Auswahl, TableLayout table) {

		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {

			parseFileSonne(menue_Auswahl);

		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}
	}

	public int updateMensaMenueKost(String menue_Auswahl, TableLayout table) {
		int i = 0;
		// Alten Mensaplan entfernen
		if (table.getChildCount() > 0) {
			table.removeViews(0, table.getChildCount());
		}
		try {

			i = parseFileKost(menue_Auswahl);

		} catch (Exception e) {
			Log.v("Fehler", "Fehler");
		}

		return i;
	}

	// HTML-File laden, parsen und gewünschte Werte in die vorhandenen Variablen
	// schreiben
	public void parseFileNord(String text) throws IOException,
			FileNotFoundException {

		Document docu = Jsoup.connect(
				"http://www.stwdo.de/Wochenplan.127.0.html").get();

		if (text.equals("Spezielle Menüs der Woche"))
		{
			Elements spalte1 = docu.select("td.Tabellen-spalte-1");
			for (int i = 0; i < 9; i++) {
				Element menuename = spalte1.get(i);
				menueS1[i] = menuename.text();
				Log.v("Titel", menueS1[i]);
			}
			Elements spalte2 = docu.select("td.Tabellen-spalte-2");
			for (int i = 0; i < 9; i++) {
				Element menuebeschreibung = spalte2.get(i);
				menueS2[i] = menuebeschreibung.text();
				Log.v("Beschreibung", menueS2[i]);
			}
		}

		if (text.equals("Aktuelles Tagesmenü")) {
			Elements datums = docu.select("caption");
			Element datum = datums.get(1);
			datum5 = datum.text();
			Log.v("Datum", datum.text());

			Elements spalte1 = docu.select("td.Tabellen-spalte-1");
			int i = 0;
			while (!(spalte1.get(i + 8).equals(spalte1.get(12)))) {
				Element menuename = spalte1.get(i + 9);
				menueS1[i] = menuename.text();
				Log.v("Titel", menueS1[i]);
				i++;
			}
			Elements spalte2 = docu.select("td.Tabellen-spalte-2");
			i = 0;
			while (!(spalte2.get(i + 8).equals(spalte2.get(12)))) {
				Element menuebeschreibung = spalte2.get(i + 9);
				menueS2[i] = menuebeschreibung.text();
				Log.v("Beschreibung", menueS2[i]);
				i++;
			}
		}
	}

	public void parseFileSonne(String text) throws IOException,
			FileNotFoundException {

		Document docu = Jsoup.connect(
				"http://www.stwdo.de/Wochenplan.132.0.html").get();

		if (text.equals("Aktuelles Tagesmen�")) {
			Elements datums = docu.select("caption");
			Element datum = datums.get(1);
			datum5 = datum.text();
			Log.v("Datum", datum.text());

			Elements spalte1 = docu.select("td.Tabellen-spalte-1");
			int i = 0;
			while (!(spalte1.get(i).equals(spalte1.get(3)))) {
				Element menuename = spalte1.get(i);
				menueS1[i] = menuename.text();
				Log.v("Titel", menueS1[i]);
				i++;
			}
			Elements spalte2 = docu.select("td.Tabellen-spalte-2");
			i = 0;
			while (!(spalte2.get(i).equals(spalte2.get(3)))) {
				Element menuebeschreibung = spalte2.get(i);
				menueS2[i] = menuebeschreibung.text();
				Log.v("Beschreibung", menueS2[i]);
				i++;
			}
		}
	}

	public int parseFileKost(String text) throws IOException,
			FileNotFoundException {

		Document docu = Jsoup.connect(
				"http://www.stwdo.de/Wochenplan.248.0.html").get();
		int i = 0;

		if (text.equals("Aktuelles Tagesmenü")) {
			Elements datums = docu.select("caption");
			Element datum = datums.get(1);
			datum5 = datum.text();
			Log.v("Datum", datum.text());

			Elements spalte2 = docu.select("td.Tabellen-spalte-2");
			i = 0;
			while (!(spalte2.get(i).text().equals("-"))) {
				Element menuebeschreibung = spalte2.get(i);
				menueS2[i] = menuebeschreibung.text();
				menueS1[i] = "Menü " + (i + 1);
				i++;
			}

		}
		return i;
	}
}
