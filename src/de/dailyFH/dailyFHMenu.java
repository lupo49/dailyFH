package de.dailyFH;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class dailyFHMenu extends Activity {



	// Buttons des Menues
	private Button buttonAktuelles;
	private Button buttonMensa;
	private Button buttonKalender;
	private Button buttonEinstellungen;
	private Intent intentAktuelles;
	private Intent intentMensa;
	private Intent intentKalender;
	private Intent intentEinstellungen;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menue);

		// Intent fuer die Module initialisieren
		intentAktuelles = new Intent(this, dailyFHLogin.class);
		intentEinstellungen = new Intent(this, dailyFHLogin.class);
		intentKalender = new Intent(this, dailyFHLogin.class);
		intentMensa = new Intent(this, dailyFHLogin.class);

		// Buttons initalisieren und Onclick legen
		buttonAktuelles = (Button) findViewById(R.id.buttonAktuelles);
		buttonAktuelles.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intentAktuelles);
			}
		});

		buttonMensa = (Button) findViewById(R.id.buttonMensaplan);
		buttonMensa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intentMensa);
			}
		});

		buttonKalender = (Button) findViewById(R.id.buttonStundenplan);
		buttonKalender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intentKalender);
			}
		});

		buttonEinstellungen = (Button) findViewById(R.id.buttonEinstellungen);
		buttonEinstellungen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intentEinstellungen);
			}
		});
	}

}
