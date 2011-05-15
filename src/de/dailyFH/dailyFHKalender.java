package de.dailyFH;

import android.view.*;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import java.io.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class dailyFHKalender extends Activity {

	// am start -n de.dailyFH/.dailyFHKalender

	// Menueelemente
	private final int TERMINEINFUEGEN = 0;
	private final int TERMINLOESCHEN = 1;
	
	// Debug-Ausgaben de-/aktivieren
	private final boolean DEBUG_OUTPUT = true;
	
	// Konstanten fuer String-Array um Datentyp zu unterscheiden
	private final int DATEN_SEMESTERZEITPLAN = 2;
	private final int DATEN_STUNDENPLAN = 4;
	private final int DATEN_PRUEFUNGEN = 8;
	private final int DATEN_MANTERMINE = 16;

	// Maximale Anzahl der Eintraege im Semesterzeitplan
	private final int EINTRAEGE_SEMESTERZEITPLAN = 15;

	private String[][][] szpEintraege = new String[4][EINTRAEGE_SEMESTERZEITPLAN][2];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		// Das Array enthaelt in der ersten Dimension die unterschiedlichen Datenarten: 
		// Stundenplan, Semesterzeitplan, Pruefungen und manuelle Termine
		szpEintraege[DATEN_SEMESTERZEITPLAN] = getKalenderSemesterzeitplanDaten();

		CheckBox cbSemesterzeitplan = (CheckBox)findViewById(R.id.CBSemesterzeitplan);
		cbSemesterzeitplan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					displayKalenderdaten(DATEN_SEMESTERZEITPLAN);
				} else {
					entferneKalenderdaten(DATEN_SEMESTERZEITPLAN);
				}
			}
		});
	} // onCreate

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, TERMINEINFUEGEN, 0, "Termin eintragen");
		menu.add(0, TERMINLOESCHEN, 0, "Termin löschen");
		return super.onCreateOptionsMenu(menu);
	} // onCreateOptionsMenu

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO - Menuepunkte mit Funktion belegen
		return super.onOptionsItemSelected(item);
	}
	
	public String[][] getKalenderSemesterzeitplanDaten() {
		String[][] szpEintraege = new String[EINTRAEGE_SEMESTERZEITPLAN][2];
		int i = 0;
		boolean item = false;
		boolean name = false;
		boolean datum = false;

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			// Darauf achten, dass die Datei nicht UTF-8 (+ BOM) kodiert ist.
			// Sonst gibt es eine Exception aufgrund eines ungueltigen Zeichens in der ersten Zeile 
			InputStreamReader is = new InputStreamReader(getAssets().open("semesterzeitplan.xml"), "ISO-8859-1");
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
								szpEintraege[i][0] = xpp.getText();
							}
							name = false;

						} else if (datum) {
							if (i < EINTRAEGE_SEMESTERZEITPLAN) {
								// "1" -> Wert fuer das <datum>-Tag
								szpEintraege[i][1] = xpp.getText();
							}
							datum = false;
							i++;
						}

						// Ende des Tags eingelesen
					} else if (eventType == XmlPullParser.END_TAG) {
						// Ende eines Tags erreicht. Solange kein neuer Tag eingelesen
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
			Log.e("dailyFHKalender", "Exception in getKalenderDaten() - " + e.getMessage());
			e.printStackTrace();
		}

		if(DEBUG_OUTPUT) {
			i = 0;
			while(szpEintraege[i][0] != null) {
				Log.i("dailyFHKalender", "szpEintraege[i][0] - Name: " + szpEintraege[i][0]);
				Log.i("dailyFHKalender", "szpEintraege[i][1] - Datum: " + szpEintraege[i][1]);
				i++;
			}		
		} // getKalenderDaten()
		return szpEintraege;
	}
	
	public void displayKalenderdaten(int typ) {
		
		if(typ == DATEN_SEMESTERZEITPLAN) {
			CharSequence tvTextString;
			TableLayout tl = (TableLayout)findViewById(R.id.kalenderTable);
			tl.removeAllViews();	// Alles vorhandene im TableLayout entfernen
			int i = 0;
		
			while(szpEintraege[DATEN_SEMESTERZEITPLAN][i][0] != null)
			{
				// Neues Reihe im TableLayout
				TableRow trRowName = new TableRow(this);
				TableRow trRowDatum = new TableRow(this);
				trRowName.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));
				trRowDatum.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));
				trRowName.setPadding(0, 4, 0, 0);
				
				// Wird innerhalb der Reihe dargestellt
				TextView tvTextName = new TextView(this);
				TextView tvTextDatum = new TextView(this);
				tvTextName.setWidth(300);
				tvTextDatum.setWidth(300);
				tvTextName.setTextColor(Color.BLACK);
				tvTextDatum.setTextColor(Color.BLACK);
				tvTextName.setTextSize(9);
				tvTextDatum.setTextSize(9);
			
				trRowName.addView(tvTextName);
				trRowDatum.addView(tvTextDatum);
				tl.addView(trRowName);
				tl.addView(trRowDatum);

				tvTextString = Html.fromHtml("<b>" + szpEintraege[DATEN_SEMESTERZEITPLAN][i][0] + "</b>");
				tvTextName.setText(tvTextString);
				tvTextString = Html.fromHtml(szpEintraege[DATEN_SEMESTERZEITPLAN][i][1]);
				tvTextDatum.setText(tvTextString);
			
				i++;
			}
		} // if(typ == ...)
	} // displayKalenderdaten()
	
	public void entferneKalenderdaten(int typ) {
		TableLayout tl = (TableLayout)findViewById(R.id.kalenderTable);
		
		if(typ == DATEN_SEMESTERZEITPLAN) {
			tl.removeAllViews();
		} else if(typ == DATEN_PRUEFUNGEN) {
			
		} else if(typ == DATEN_STUNDENPLAN) {
			
		} else if(typ == DATEN_MANTERMINE) {
			
		}
	}
}