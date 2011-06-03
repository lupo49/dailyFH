package de.dailyFH.FK;

import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import de.dailyFH.R;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class KalenderFK extends Activity {

	// Debug-Ausgaben de-/aktivieren
	private final boolean DEBUG_OUTPUT = true;
	
	// Konstanten fuer String-Array um Datentyp zu unterscheiden
	private final int DATEN_SEMESTERZEITPLAN = 1;
	private final int DATEN_STUNDENPLAN = 2;
	private final int DATEN_PRUEFUNGEN = 3;
	private final int DATEN_MANTERMINE = 4;
	
	// Maximale Anzahl der Eintraege im Semesterzeitplan
	private final int EINTRAEGE_SEMESTERZEITPLAN = 15;
	// 1. Dimension unterscheidet zwischen Semesterzeitplan, Stundenplan usw.
	// 2. Dimension beinhaltet die einzelnen Termine
	// 3. Dimension speichert das Datum und die Beschreibung des Eintrags
	private String[][][] szpEintraege = new String[4][EINTRAEGE_SEMESTERZEITPLAN][2];
	
	
	public String[][] getKalenderSemesterzeitplanDaten() {
		int i = 0;
		boolean item = false;
		boolean name = false;
		boolean datum = false;

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			// Darauf achten, dass die Datei nicht UTF-8 (+ BOM) kodiert ist.
			// Ansonsten gibt es eine Exception aufgrund eines ungueltigen Zeichens
			// in der ersten Zeile
			InputStreamReader is = new InputStreamReader(getAssets().open(
					"semesterzeitplan.xml"), "ISO-8859-1");
			xpp.setInput(is);

			// Gibt das aktuelle Event zurueck: START_TAG, END_TAG ...
			int eventType = xpp.getEventType();

			// Solange Datei durchlaufen bis END_DOCUMENT erreicht
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// Wenn das START_TAG erreicht
				if (eventType == XmlPullParser.START_TAG) {
					// Namen des aktuellen Tags pruefen
					if (xpp.getName().equals("item")) {
						// <item>-Eintrag gefunden. Im naechsten Durchlauf
						// werden
						// die Sub-Eintrage geparsed (xpp.next())
						item = true;
					} else if (xpp.getName().equals("name") && item) {
						name = true;
					} else if (xpp.getName().equals("datum") && item) {
						datum = true;
					}

					// Werte innerhalb eines Tags auslesen
				} else if (eventType == XmlPullParser.TEXT) {
					if (item) {
						if (name) {
							if (i < EINTRAEGE_SEMESTERZEITPLAN) {
								// "0" -> Wert fuer das <name>-Tag
								szpEintraege[DATEN_SEMESTERZEITPLAN][i][0] = xpp.getText();
							}
							name = false;

						} else if (datum) {
							if (i < EINTRAEGE_SEMESTERZEITPLAN) {
								// "1" -> Wert fuer das <datum>-Tag
								szpEintraege[DATEN_SEMESTERZEITPLAN][i][1] = xpp.getText();
							}
							datum = false;
							i++;
						}

						// Ende des Tags eingelesen
					} else if (eventType == XmlPullParser.END_TAG) {
						// Ende eines Tags erreicht. Solange kein neuer Tag
						// eingelesen
						// beide Hilfsvariablen auf false
						if (xpp.getName().equals("item")) {
							item = false;
							name = false;
						}
					}
				} // while-Schleife
					// Naechsten Datensatz einlesen
				eventType = xpp.next();
			} // try
		} catch (Exception e) {
			Log.e("dailyFHKalender",
					"Exception in getKalenderDaten() - " + e.getMessage());
			e.printStackTrace();
		}

		if (DEBUG_OUTPUT) {
			i = 0;
			while (szpEintraege[i][0] != null) {
				Log.i("dailyFHKalender", "szpEintraege[i][0] - Name: "
						+ szpEintraege[i][0]);
				Log.i("dailyFHKalender", "szpEintraege[i][1] - Datum: "
						+ szpEintraege[i][1]);
				i++;
			}
		} // getKalenderDaten()
		
		return szpEintraege[DATEN_SEMESTERZEITPLAN];
	}

	public void entferneKalenderdaten(int typ) {
		TableLayout tl = (TableLayout) findViewById(R.id.kalenderTable);

		if (typ == DATEN_SEMESTERZEITPLAN) {
			tl.removeAllViews();
		} else if (typ == DATEN_PRUEFUNGEN) {

		} else if (typ == DATEN_STUNDENPLAN) {

		} else if (typ == DATEN_MANTERMINE) {

		}
	}
}
