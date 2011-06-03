package de.dailyFH;

import de.dailyFH.FK.LoginFK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class dailyFHLogin extends Activity {

	// Gui-Elemente
	private Button submitButton;
	private EditText pinEingabe;
	private Intent intent;
	private Intent intentRegister;
	private EditText MatrikelnummerEingabe;

	private de.dailyFH.FK.LoginFK loginFK;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		loginFK = new LoginFK();
		
		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Buttons initialisieren
		submitButton = (Button) this.findViewById(R.id.buttonPinOK);
		pinEingabe = (EditText) this.findViewById(R.id.inputPinEingabe);
		MatrikelnummerEingabe = (EditText) this
				.findViewById(R.id.inputMatrikelEingabe);

		// Fokus auf TextView setzen damit nicht automatisch das Keyboard
		// eingeblendet ist
		TextView labelRegister = (TextView) this
				.findViewById(R.id.labelRegisterAccount);
		labelRegister.setFocusableInTouchMode(true);
		labelRegister.requestFocus();

		// Intent fuer das Menue initialisieren
		intent = new Intent(this, dailyFHMenu.class);
		intentRegister = new Intent(this, dailyFHRegister.class);

		// Listener beim Button anmelden
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Pruefe Eingabe ab
				if (loginFK.pruefPin(pinEingabe.getText().toString(), MatrikelnummerEingabe.getText().toString())) {
					// Pin ist gueltig
					Log.i("1", "PIN ist korrekt gepr�ft worden");
					// neue Activity starten
					startActivity(intent);
					// Beendet den Login-Bildschirm damit das Hauptmenue das Erste ist
					finish();
				} else {
					// Pin ist nicht gueltig
					Log.i("1", "PIN ist nicht gleich");

					// TODO - Fehlermeldung ausgeben
					// - Fehlerfenster
				}
			}
		}); // Ende OnClickListener

		labelRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Neue Activity starten
				startActivity(intentRegister);
				// Login beenden
				finish();
			}
		});
	} // Ende onCreate
}