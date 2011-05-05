package de.dailyFH;

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
	private String eingabe;
	private String pin = "1234";
	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Buttons initialisieren
		submitButton = (Button) this.findViewById(R.id.buttonPinOK);
		pinEingabe = (EditText) this.findViewById(R.id.inputPinEingabe);

		// Fokus auf TextView setzen damit nicht automatisch das Keyboard
		// eingeblendet ist
		TextView labelRegister = (TextView) this.findViewById(R.id.labelRegisterAccount);
		labelRegister.setFocusableInTouchMode(true);
		labelRegister.requestFocus();

		// Intent fuer das Menue initialisieren
		intent = new Intent(this, dailyFHMenu.class);

		// Listener beim Button anmelden
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Wert des Inputfeldes holen
				eingabe = pinEingabe.getText().toString();

				// Pruefe Eingabe mit der eingestellten Pin ab
				if (eingabe.equals(pin)) {
					// Pin ist gueltig
					Log.i("1", "PIN ist gleich");

					// neue Activity starten
					startActivity(intent);

					// Beendet den Login-Bildschirm damit das Hauptmenue das
					// erste ist
					finish();
				} else {
					// Pin ist nicht gueltig
					Log.i("1", "PIN ist nicht gleich");

					// TODO - Fehlermeldung ausgeben
				}
			}
		}); // Ende OnClickListener
	} // Ende onCreate
}