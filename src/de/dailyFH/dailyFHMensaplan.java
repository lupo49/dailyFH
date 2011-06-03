package de.dailyFH;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.dailyFH.FK.MensaplanFK;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class dailyFHMensaplan extends Activity {

	private Spinner spinnerAuswahl;
	private Spinner spinnerMensa;
	private TableLayout table;

	String[] menueS1 = new String[30];
	String[] menueS2 = new String[30];
	String datum5;
	String doc;

	private de.dailyFH.FK.MensaplanFK mensaplanFK;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		mensaplanFK = new MensaplanFK();

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mensaplan);

		table = (TableLayout) findViewById(R.id.tableInhalt);

		spinnerMensa = (Spinner) findViewById(R.id.spinnerMensa);
		final int posM = spinnerMensa.getSelectedItemPosition();
		final String[] whatEntriesM = getResources().getStringArray(
				R.array.sp_what_mensa_entries);
		final String conferredM = whatEntriesM[posM];

		ArrayAdapter<CharSequence> adapterM = ArrayAdapter.createFromResource(
				this, R.array.sp_what_mensa_entries,
				android.R.layout.simple_spinner_item);
		adapterM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerMensa.setAdapter(adapterM);

		AdapterView.OnItemSelectedListener oislM = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String text = (String) spinnerMensa.getAdapter().getItem(
						position);
				updateMensaAuswahl(text);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};
		spinnerMensa.setOnItemSelectedListener(oislM);
	}

	public void updateMensaAuswahl(String mensa_Auswahl) {
		spinnerAuswahl = (Spinner) findViewById(R.id.spinnerMenue);
		final int posA = spinnerAuswahl.getSelectedItemPosition();
		final String[] whatEntriesA = getResources().getStringArray(
				R.array.sp_what_nord);
		final String conferredA = whatEntriesA[posA];

		if (mensa_Auswahl.equals("Mensa Campus Nord")) {
			ArrayAdapter<CharSequence> adapterA = ArrayAdapter
					.createFromResource(this, R.array.sp_what_nord,
							android.R.layout.simple_spinner_item);
			adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerAuswahl.setAdapter(adapterA);
			AdapterView.OnItemSelectedListener oislA = new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					String text = (String) spinnerAuswahl.getAdapter().getItem(
							position);
					// FIXME - Es muessen Rueckgabewerte definiert werden.
					mensaplanFK.updateMensaMenueNord(text, table);
					designPlanNord(text);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			};
			spinnerAuswahl.setOnItemSelectedListener(oislA);
		}
		if (mensa_Auswahl.equals("Mensa Sonnenstraße")) {
			ArrayAdapter<CharSequence> adapterB = ArrayAdapter
					.createFromResource(this, R.array.sp_what_sonnen,
							android.R.layout.simple_spinner_item);
			adapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerAuswahl.setAdapter(adapterB);
			AdapterView.OnItemSelectedListener oislA = new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					String text = (String) spinnerAuswahl.getAdapter().getItem(
							position);
					// FIXME - Es muessen Rueckgabewerte definiert werden.
					mensaplanFK.updateMensaMenueSonne(text, table);
					designPlanSonne(text);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			};
			spinnerAuswahl.setOnItemSelectedListener(oislA);
		}
		if (mensa_Auswahl.equals("KostBar")) {
			ArrayAdapter<CharSequence> adapterC = ArrayAdapter
					.createFromResource(this, R.array.sp_what_kostbar,
							android.R.layout.simple_spinner_item);
			adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerAuswahl.setAdapter(adapterC);
			AdapterView.OnItemSelectedListener oislA = new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					String text = (String) spinnerAuswahl.getAdapter().getItem(
							position);
					// FIXME - Es muessen Rueckgabewerte definiert werden.
					int i = mensaplanFK.updateMensaMenueKost(text, table);
					designPlanKost(i, text);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			};
			spinnerAuswahl.setOnItemSelectedListener(oislA);
		}
	}

	// Funktion um die Mensadaten grafisch darzustellen
	public void designPlanNord(String text) {

		// Variable für die Anzahl der Menüs in den unterschiedlichen
		// Menüpunkten
		int anzahl = 0;

		if (text.equals("Aktuelles Tagesmenü")) {
			anzahl = 4;
			// Tabellenzeile mit Datumsinhalt einf�gen
			TableRow row_date = new TableRow(this);
			row_date.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			TextView text_date = new TextView(this);
			text_date.setWidth(100);
			text_date.setTextColor(Color.BLACK);
			text_date.setTextSize(14);
			text_date.setText(datum5);
			row_date.setPadding(0, 8, 0, 0);
			row_date.addView(text_date);
			table.addView(row_date);
		}
		if (text.equals("Spezielle Menüs der Woche")) {
			anzahl = 9;
		}

		for (int j = 0; j < anzahl; j++) {
			// Erstellen einer neuen Zeile f�r die Tabelle
			TableRow row_menue = new TableRow(this);
			row_menue.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));

			// Erstellen eines neuen TextViews f�r die Zeile
			TextView text_title = new TextView(this);
			TextView text_beschreibung = new TextView(this);
			// Attribute der Text_Views setzen
			text_title.setWidth(100);
			text_beschreibung.setWidth(200);
			text_title.setTextColor(Color.BLACK);
			text_beschreibung.setTextColor(Color.BLACK);
			text_title.setTextSize(9);
			text_beschreibung.setTextSize(9);

			// Text in die Text Views schreiben
			text_title.setText(menueS1[j]);
			text_beschreibung.setText(menueS2[j]);
			// Text-Views der Tabellenzeile zuordnen
			row_menue.setPadding(0, 4, 0, 0);
			row_menue.addView(text_title);
			row_menue.addView(text_beschreibung);
			// Tabellenzeile der vorhandenen Inhaltstabelle zuordnen
			table.addView(row_menue);
		}

	}

	public void designPlanSonne(String text) {

		// Variable f�r die Anzahl der Men�s in den unterschiedlichen
		// Men�punkten
		int anzahl = 0;

		if (text.equals("Aktuelles Tagesmen�")) {
			anzahl = 3;
			// Tabellenzeile mit Datumsinhalt einf�gen
			TableRow row_date = new TableRow(this);
			row_date.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			TextView text_date = new TextView(this);
			text_date.setWidth(100);
			text_date.setTextColor(Color.BLACK);
			text_date.setTextSize(14);
			text_date.setText(datum5);
			row_date.setPadding(0, 8, 0, 0);
			row_date.addView(text_date);
			table.addView(row_date);
		}

		for (int j = 0; j < anzahl; j++) {
			// Erstellen einer neuen Zeile f�r die Tabelle
			TableRow row_menue = new TableRow(this);
			row_menue.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));

			// Erstellen eines neuen TextViews f�r die Zeile
			TextView text_title = new TextView(this);
			TextView text_beschreibung = new TextView(this);
			// Attribute der Text_Views setzen
			text_title.setWidth(100);
			text_beschreibung.setWidth(200);
			text_title.setTextColor(Color.BLACK);
			text_beschreibung.setTextColor(Color.BLACK);
			text_title.setTextSize(9);
			text_beschreibung.setTextSize(9);

			// Text in die Text Views schreiben
			text_title.setText(menueS1[j]);
			text_beschreibung.setText(menueS2[j]);
			// Text-Views der Tabellenzeile zuordnen
			row_menue.setPadding(0, 4, 0, 0);
			row_menue.addView(text_title);
			row_menue.addView(text_beschreibung);
			// Tabellenzeile der vorhandenen Inhaltstabelle zuordnen
			table.addView(row_menue);
		}

	}

	public void designPlanKost(int i, String text) {

		// Variable f�r die Anzahl der Men�s in den unterschiedlichen
		// Men�punkten
		int anzahl = 0;

		if (text.equals("Aktuelles Tagesmenü")) {
			anzahl = i;
			// Tabellenzeile mit Datumsinhalt einf�gen
			TableRow row_date = new TableRow(this);
			row_date.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			TextView text_date = new TextView(this);
			text_date.setWidth(100);
			text_date.setTextColor(Color.BLACK);
			text_date.setTextSize(14);
			text_date.setText(datum5);
			row_date.setPadding(0, 8, 0, 0);
			row_date.addView(text_date);
			table.addView(row_date);
		}

		for (int j = 0; j < anzahl; j++) {
			// Erstellen einer neuen Zeile f�r die Tabelle
			TableRow row_menue = new TableRow(this);
			row_menue.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));

			// Erstellen eines neuen TextViews f�r die Zeile
			TextView text_title = new TextView(this);
			TextView text_beschreibung = new TextView(this);
			// Attribute der Text_Views setzen
			text_title.setWidth(100);
			text_beschreibung.setWidth(200);
			text_title.setTextColor(Color.BLACK);
			text_beschreibung.setTextColor(Color.BLACK);
			text_title.setTextSize(9);
			text_beschreibung.setTextSize(9);

			// Text in die Text Views schreiben
			text_title.setText(menueS1[j]);
			text_beschreibung.setText(menueS2[j]);
			// Text-Views der Tabellenzeile zuordnen
			row_menue.setPadding(0, 4, 0, 0);
			row_menue.addView(text_title);
			row_menue.addView(text_beschreibung);
			// Tabellenzeile der vorhandenen Inhaltstabelle zuordnen
			table.addView(row_menue);
		}

	}

}
