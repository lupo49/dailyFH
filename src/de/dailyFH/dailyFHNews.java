package de.dailyFH;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.view.ViewGroup.LayoutParams;

/* Nächste Schritte:
 * Löschen der alten News bei erneuten Klick auf aktualisieren
 * Verfeinerung der Positionierung
 * Suche nach automatischen Umbruch in TextViews
 */

public class dailyFHNews extends Activity {

	// Button zum Aktualisieren der News und die Gesamte News-Tabelle
	private Button buttonAktualisieren;
	private TableLayout table;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		// Tabelle initalisieren
		table = (TableLayout)findViewById(R.id.table);
		
		// Buttons initalisieren und Onclick legen
		buttonAktualisieren = (Button) findViewById(R.id.buttonRefresh);
		buttonAktualisieren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        //Bei Klick auf aktualisieren rufe die neuesten News ab
				createNews();
			}
		});
	}
	
	public void createNews()
	{
		//String zum entgegennehmen der News in der Form [?][3] 
		//---> drei, da es Immer Überschrift,Datum und Inhalt gibt
		String[][] news= getNews();
		//Zum Fortmatieren der Texte Mittels HTML
		CharSequence styledText;
		
		//Durchlaufen der News
		for(int i=0; i<news.length; i++)
		{
			for(int j=0; j<news[i].length; j++)
			{
				//Erstellen einer neuen Zeile für die Tabelle
		        TableRow row = new TableRow(this);
		        row.setLayoutParams(new LayoutParams(
		                       LayoutParams.MATCH_PARENT,
		                       LayoutParams.WRAP_CONTENT));
		       
		       //Erstellen eines neuen TextViews für die Zeile
		        TextView text= new TextView(this);
		        
		        //Abfrage, ob es sich um Überschrift, Datum oder Inhalt handelt
		        //demensprechende Formation
		        if(j==0)
		        {
		        	text.setTextSize(9);
		        	row.setPadding(0,4,0,0);
		        	styledText = Html.fromHtml("<b>"+news[i][j]+"</b>");
		        }else if(j==1)
		        {
		        
		        	text.setTextSize(7);
		        	styledText = Html.fromHtml("<i>"+news[i][j]+"</i>");
		        }else{
		       
		        	text.setTextSize(9);
		        	styledText = Html.fromHtml(news[i][j]);
		        }
		        
		        //Inhalt des Textviews auf unseren "HTML-Text" setzen
		        text.setText(styledText);
		        text.setTextColor(Color.BLACK);
		
		        // der Zeile wird der TextView hinzugefügt 
		        row.addView(text);
		 
		        //der Tabelle wird die Zeile hinzugefügt
		        table.addView(row);
			}
		}
	}
	
	public String[][] getNews()
	{
		//Diese Funktion ist nur zu Demozwecken
		//Später sollen hier die News des FH-Server ausgewertet/aufbereitet werden
		String[][] news=new String[5][3];
		
		news[0][0]="Projekt- und Bachelorarbeit (Thema: Cloud Computing mit <br/>OpenNebula)";
		news[0][1]="Freitag, 6. Mai 2011 07:21";
		news[0][2]="Vergebe eine Projekt- und Bachelorarbeit im Bereich Open Source Cloud <br/> Computing auf Basis von... ";
		news[1][0]="Lehrveranstaltungen von Dr. Ehleben";
		news[1][1]="Donnerstag, 5. Mai 2011 07:13";
		news[1][2]="Die Lehrveranstaltungen von Herrn Dr. Ehleben entfallen am 05.05.11 <br/> aufgrund einer Erkrankung.";
		news[2][0]="Statistik Tutorium";
		news[2][1]="Donnerstag, 5. Mai 2011 00:49";
		news[2][2]="Aufgrund einer Erkrankung muss leider am Donnerstag, den 5.5.11 das <br/>Statistik Tutorium bei Frau Pantea Kock ausfallen.";
		news[3][0]="Tutorium Analysis 2";
		news[3][1]="Mittwoch, 4. Mai 2011 08:57";
		news[3][2]="Das Tutorium zum Kurs Analysis 2 findet zukünftig <br/> im Raum A.1.02 statt.";
		news[4][0]="Vortrag von Hr. Kollmeier, GF der apsolut GmbH  zum Thema SAP SRM";
		news[4][1]="Montag, 2. Mai 2011 22:26";
		news[4][2]="Am Mittwoch, den 4.5. wird um 12 Uhr in A.E.01 Hr. Kollmeier,<br/> Geschäftsführer der apsolut GmbH...";
		
		return news;
	}

}
