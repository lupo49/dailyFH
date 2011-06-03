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

import de.dailyFH.FK.KalenderFK;

public class dailyFHKalender extends Activity {

	// am start -n de.dailyFH/.dailyFHKalender

	// Menueelemente
	private final int TERMINEINFUEGEN = 0;
	private final int TERMINLOESCHEN = 1;

	private de.dailyFH.FK.KalenderFK kalenderFK = null;
	private String[][][] szpEintraege;

	// Konstanten fuer String-Array um Datentyp zu unterscheiden
	private final int DATEN_SEMESTERZEITPLAN = 1;
	private final int DATEN_STUNDENPLAN = 2;
	private final int DATEN_PRUEFUNGEN = 3;
	private final int DATEN_MANTERMINE = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);

		kalenderFK = new KalenderFK();
		szpEintraege = new String[4][100][2];

		// Das Array enthaelt in der ersten Dimension die unterschiedlichen
		// Datenarten:
		// Stundenplan, Semesterzeitplan, Pruefungen und manuelle Termine
		szpEintraege[DATEN_SEMESTERZEITPLAN] = kalenderFK.getKalenderSemesterzeitplanDaten();

		CheckBox cbSemesterzeitplan = (CheckBox) findViewById(R.id.CBSemesterzeitplan);
		cbSemesterzeitplan
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							displayKalenderdaten(DATEN_SEMESTERZEITPLAN);
						} else {
							kalenderFK.entferneKalenderdaten(DATEN_SEMESTERZEITPLAN);
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

	public void displayKalenderdaten(int typ) {

		// Daten fuer den Semesterzeitplan anzeigen
		if (typ == DATEN_SEMESTERZEITPLAN) {
			CharSequence tvTextString;
			TableLayout tl = (TableLayout) findViewById(R.id.kalenderTable);
			tl.removeAllViews(); // Alles vorhandene im TableLayout entfernen
			int i = 0;

			while (szpEintraege[DATEN_SEMESTERZEITPLAN][i][0] != null) {
				// Neues Reihe im TableLayout
				TableRow trRowName = new TableRow(this);
				TableRow trRowDatum = new TableRow(this);
				trRowName.setLayoutParams(new LayoutParams(300,
						LayoutParams.WRAP_CONTENT));
				trRowDatum.setLayoutParams(new LayoutParams(300,
						LayoutParams.WRAP_CONTENT));
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

				tvTextString = Html.fromHtml("<b>"
						+ szpEintraege[DATEN_SEMESTERZEITPLAN][i][0] + "</b>");
				tvTextName.setText(tvTextString);
				tvTextString = Html
						.fromHtml(szpEintraege[DATEN_SEMESTERZEITPLAN][i][1]);
				tvTextDatum.setText(tvTextString);

				i++;
			}
		} // if(typ == ...)
	} // displayKalenderdaten()
}