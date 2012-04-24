package coruscant.imperial.palace;


import coruscant.jedi.temple.initialization.Init;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LowerDeflectorShield extends Activity {
	SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       	setContentView(R.layout.authenticate);
    }

    public void showPasswordHint(View v) {
    	Log.d("LowerDeflectorShield", "showing password hint");
    	TextView hint = (TextView)findViewById(R.id.show_hint);
    	hint.setText("HINT TEST"); //prefs.getString(getString(R.string.hint_key, null))
    	hint.setVisibility(View.VISIBLE);
    }
    
    public void submitPassword(View v) {
    	Log.d("LowerDeflectorShield", "submitting password");
    	String savedPass = prefs.getString(getString(R.string.password_key), null);
    	EditText passField = (EditText)findViewById(R.id.authenticate_password);
    	String enteredPass = passField.getText().toString();
    	if(savedPass.equals(enteredPass)) {
    		Log.d("LowerDeflectorShield", "password is correct!");
    		Intent showPrefs = new Intent(this, Preferences.class);
    		showPrefs.setAction("coruscant.imperial.palace.PREFERENCES");
    		startActivity(showPrefs);
    	} else {
    		Log.d("LowerDeflectorShield", "password is NOT correct!");
    		TextView error = (TextView)findViewById(R.id.authentication_error);
    		error.setVisibility(View.VISIBLE);
    	}
    }
}