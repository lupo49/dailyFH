package de.dailyFH;

import de.dailyFH.FK.NewsFK;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.view.ViewGroup.LayoutParams;

/* 
 * INFO:
 * 	- 	Um eine Internetverbindung im Android-Emulator aufbauen zu koennen,
 * 		muss dem Device die Eigenschaft "GSM Modem support" eingetragen werden.
 * 
 * Nächste Schritte:
 * 	-   Die Fehlermeldung bei "No Connection" wird immer 
 * 		unter die bestehende gehaengt anstatt zu ersetzen
 */

public class dailyFHNews extends Activity {

	// Button zum Aktualisieren der News und die gesamte News-Tabelle
	private Button buttonAktualisieren;
	private TableLayout table;

	// String-Array für die 15. aktuellsten News (Title,Description,Date)
	String[][] news = new String[3][15];

	private de.dailyFH.FK.NewsFK newsFK;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		newsFK = new NewsFK();

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		// Tabelle initalisieren
		table = (TableLayout) findViewById(R.id.table);

		// Buttons initalisieren und Onclick legen
		buttonAktualisieren = (Button) findViewById(R.id.buttonRefresh);
		buttonAktualisieren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Bei Klick auf aktualisieren rufe die neuesten News ab
				updateNews();
			}
		});

		for (int i = 0; i < news.length; i++) {
			for (int j = 0; j < news[i].length; j++) {
				news[i][j] = "";
			}
		}

		try {
			news = newsFK.parseFile("news.xml");
			designNews();
		} catch (Exception e) {

			CharSequence styledText;
			TableRow row_empty = new TableRow(this);
			row_empty.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			TextView text_empty = new TextView(this);
			text_empty.setWidth(300);

			text_empty.setTextColor(Color.BLACK);
			text_empty.setTextSize(9);
			row_empty.setPadding(0, 4, 0, 0);
			styledText = Html.fromHtml("<b>"
					+ "Keine News auf ihrem Smartphone gefunden. Bitte "
					+ "drücken sie den Aktualisieren-Button." + "</b>");
			text_empty.setText(styledText);
			// der Zeile wird der TextView hinzugefügt
			row_empty.addView(text_empty);
			// der Tabelle wird die Zeile hinzugefügt
			table.addView(row_empty);
		}

	}

	public void updateNews() {
		// String zum Entgegennehmen der News in der Form [?][3]
		// ---> drei, da es Immer Überschrift, Datum und Inhalt gibt
		try {
			if(newsFK.dateiLaden()) {
				newsFK.parseFile("news.xml");
				designNews();
			}
		} catch (Exception e) {
			// FIXME - Die Fehlermeldungen werden alle untereinander aufgereiht
			this.deleteFile("news.xml");
			Log.v("Fehler",
					"No Connection (GSM Modem support im Emulator aktiviert?)");
			CharSequence styledText;
			TableRow row_empty = new TableRow(this);
			row_empty.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			TextView text_empty = new TextView(this);
			text_empty.setWidth(300);

			text_empty.setTextColor(Color.BLACK);
			text_empty.setTextSize(9);
			row_empty.setPadding(0, 4, 0, 0);
			styledText = Html.fromHtml("<b>"
					+ "Daten können nicht aus dem Internet abgerufen werden."
					+ " Keine Internetverbindung?" + "</b>");
			text_empty.setText(styledText);
			// der Zeile wird der TextView hinzugefügt
			row_empty.addView(text_empty);
			// der Tabelle wird die Zeile hinzugefügt
			table.addView(row_empty, 1);
		}
	}

	public void designNews() {
		// Alte News entfernen
		table.removeViews(1, table.getChildCount() - 1);
		// Zum Fortmatieren der Texte Mittels HTML
		CharSequence styledText;

		// Durchlaufen der News
		for (int j = 0; j < news[0].length; j++) {
			// Erstellen einer neuen Zeile für die Tabelle
			TableRow row_title = new TableRow(this);
			TableRow row_desc = new TableRow(this);
			TableRow row_date = new TableRow(this);

			row_title.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			row_desc.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));
			row_date.setLayoutParams(new LayoutParams(300,
					LayoutParams.WRAP_CONTENT));

			// Erstellen eines neuen TextViews für die Zeile
			TextView text_title = new TextView(this);
			TextView text_date = new TextView(this);
			TextView text_desc = new TextView(this);

			text_title.setWidth(300);
			text_date.setWidth(300);
			text_desc.setWidth(300);

			text_title.setTextColor(Color.BLACK);
			text_desc.setTextColor(Color.BLACK);
			text_date.setTextColor(Color.BLACK);

			text_title.setTextSize(9);
			row_title.setPadding(0, 4, 0, 0);
			styledText = Html.fromHtml("<b>" + news[0][j] + "</b>");
			text_title.setText(styledText);
			// der Zeile wird der TextView hinzugefügt
			row_title.addView(text_title);
			// der Tabelle wird die Zeile hinzugefügt
			table.addView(row_title);

			text_date.setTextSize(7);
			styledText = Html.fromHtml("<i>" + news[2][j] + "</i>");
			text_date.setText(styledText);
			// der Zeile wird der TextView hinzugefügt
			row_date.addView(text_date);
			// der Tabelle wird die Zeile hinzugefügt
			table.addView(row_date);

			text_desc.setTextSize(9);
			styledText = Html.fromHtml(news[1][j]);
			text_desc.setText(styledText);
			// der Zeile wird der TextView hinzugefügt
			row_desc.addView(text_desc);
			// der Tabelle wird die Zeile hinzugefügt
			table.addView(row_desc);
		}
	}
}
