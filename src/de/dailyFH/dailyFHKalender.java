package de.dailyFH;

import java.io.InputStream;

import de.dailyFH.libs.XMLParser;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class dailyFHKalender extends Activity {

	private final int TERMINEINFUEGEN = 0;
	private final int TERMINLOESCHEN = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		try {
		InputStream is = getAssets().open("semesterzeitplan.xml");
		XMLParser xmlp = new XMLParser();
		xmlp.init(is);
		xmlp.elementeAusgeben();
		}catch(Exception e ) {
			Log.i("dailyFHKalender", "XMLPARSER - " + e.getMessage());
		}
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
}