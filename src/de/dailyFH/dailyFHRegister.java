package de.dailyFH;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class dailyFHRegister extends Activity {
	private Button submit;
	private EditText matrikelnummer;
	private EditText pin;
	private Intent login;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// App Titelleiste ausblenden
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Elemente holen
		submit = (Button) findViewById(R.id.buttonRegisterSubmit);
		matrikelnummer = (EditText) findViewById(R.id.editMatr);
		pin = (EditText) findViewById(R.id.editPin);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPrefs(pin.getText().toString(), matrikelnummer.getText()
						.toString());
			}
		});

	}

	public void setPrefs(String pin, String matrikelnummer) {

		// Prefsname erzeugen
		String file = matrikelnummer + "_prefs";

		// Legt pref files an wenn es sie nicht gibt, sonst wird die vorhandene
		// geladen
		SharedPreferences settings = getSharedPreferences(file, 0);

		// pr�fen, ob die Matirkelnummer schon gibt

		if (!settings.contains("Matrikelnummer")) {
			// Wurde noch nicht angelegt

			// Editor erstellen
			SharedPreferences.Editor editor = settings.edit();
			// Daten schreiben
			editor.putString("Matrikelnummer", matrikelnummer);
			editor.putString("Pin", md5(pin));
			editor.commit();
			Log.i("Gestezer Pin", md5(pin));

			this.login = new Intent(this, dailyFHLogin.class);
			startActivity(login);

		} else {
			// Wurde bereits angelegt
			Log.i("Fehler", "Existiert bereits");
		}
	}

	private String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
