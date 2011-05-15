package de.dailyFH;

import java.io.*;
import java.net.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

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
			parseFile();
			designNews();
		} catch (Exception e) {

			CharSequence styledText;
			TableRow row_empty = new TableRow(this);
			row_empty.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));
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
			getFile();
			parseFile();
			designNews();
		} catch (Exception e) {
			// FIXME - Die Fehlermeldungen werden alle untereinander aufgereiht
			this.deleteFile("news.xml");
			Log.v("Fehler", "No Connection (GSM Modem support im Emulator aktiviert?)");
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

	public void parseFile() throws XmlPullParserException, IOException,	FileNotFoundException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		boolean item = false;
		boolean date = false;
		boolean desc = false;
		boolean title = false;
		int i = 0;

		FileInputStream inFile = openFileInput("news.xml");
		InputStreamReader isr = new InputStreamReader(inFile, "iso-8859-15");
		BufferedReader in = new BufferedReader(isr);

		xpp.setInput(in);

		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equals("item")) {
					item = true;
				} else if (xpp.getName().equals("title") && item) {
					title = true;
				} else if (xpp.getName().equals("description") && item) {
					desc = true;
				} else if (xpp.getName().equals("pubDate") && item) {
					date = true;
				}
			} else if (eventType == XmlPullParser.TEXT) {
				if (item) {
					if (title) {
						if (i < 15) {
							news[0][i] = xpp.getText();
						}
						title = false;
					} else if (desc) {
						if (i < 15) {
							news[1][i] = xpp.getText();
							news[1][i] = news[1][i].replaceAll("<br/>", "");
							news[1][i] = news[1][i].replaceAll("<br>", "");
							news[1][i] = news[1][i].replaceAll("<br />", "");
						}
						desc = false;
					} else if (date) {
						if (i < 15) {
							news[2][i] = xpp.getText();
							news[2][i] = news[2][i].replaceAll("\\+0200", "");
						}
						date = false;
						i++;
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (xpp.getName().equals("item")) {
					item = false;
					title = false;
					desc = false;
					date = false;
				}
			}
			eventType = xpp.next();
		}
	}

	public void getFile() throws XmlPullParserException, IOException, IllegalStateException, MalformedURLException, 
								ProtocolException, IOException, FileNotFoundException {

		FileOutputStream os;
		final String url_str = "http://www.inf.fh-dortmund.de/rss.php";

		os = openFileOutput("news.xml", Context.MODE_PRIVATE);
		URL url = new URL(url_str.replace(" ", "%20"));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			byte tmp_buffer[] = new byte[4096];
			InputStream is = conn.getInputStream();
			int n;
			while ((n = is.read(tmp_buffer)) > 0) {
				os.write(tmp_buffer, 0, n);
				os.flush();
			}
		} else {
			throw new IllegalStateException("HTTP response: " + responseCode);
		}
		os.close();
	}
}
