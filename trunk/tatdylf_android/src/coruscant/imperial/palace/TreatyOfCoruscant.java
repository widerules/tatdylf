package coruscant.imperial.palace;


import coruscant.jedi.temple.initialization.Init;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TreatyOfCoruscant extends Activity {
	SharedPreferences prefs;
	static boolean isInit = false;
		
    @Override
	protected void onResume() {
		super.onResume();
        String password = prefs.getString(getString(R.string.password_key), null);
        if (password == null) {
        	setContentView(R.layout.create_password);
        } else {
        	setContentView(R.layout.main);
        }
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String password = prefs.getString(getString(R.string.password_key), null);
        if (password == null) {
        	setContentView(R.layout.create_password);
        } else {
        	setContentView(R.layout.main);
        }
        Log.d("TreatyOfCoruscant", "Started activity!!!");
        LocateSatellites ls = new LocateSatellites(getApplicationContext(), !isInit, this);
        ls.run();
        if(!isInit){
        	isInit = true;
        }
    }
    
    public void showPreferences(View v) {
       	setContentView(R.layout.authenticate);
    }
    
    public void showPasswordHint(View v) {
    	Log.d("LowerDeflectorShield", "showing password hint");
    	TextView hint = (TextView)findViewById(R.id.show_hint);
    	hint.setText(prefs.getString(getString(R.string.hint_key), null)); 
    	hint.setVisibility(View.VISIBLE);
    }
    
    public void submitPassword(View v) {
    	Log.d("LowerDeflectorShield", "submitting password");
    	String savedPass = prefs.getString(getString(R.string.password_key), "");
    	Log.d("LowerDeflectorShield", "got saved pass: " + savedPass);
    	EditText passField = (EditText)findViewById(R.id.authenticate_password);
    	Log.d("LowerDeflectorShield", "got pass field");
    	String enteredPass = passField.getText().toString();
    	Log.d("LowerDeflectorShield", "got submitted pass" + enteredPass);
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
    
    public void createPassword(View v) {
    	EditText passField = (EditText)findViewById(R.id.new_password);
    	EditText confirmField = (EditText)findViewById(R.id.new_password_confirm);
    	EditText hintField = (EditText)findViewById(R.id.new_password_hint);
    	String pass = passField.getText().toString();
    	String confirm = confirmField.getText().toString();
    	String hint = hintField.getText().toString();
    	if(pass.equals(confirm)) {
    		Editor e = prefs.edit();
    		e.putString(getString(R.string.password_key), pass);
    		Log.d("TreatyOfCoruscant", "adding password to prefs: " + pass);
    		e.putString(getString(R.string.hint_key), hint);
    		Log.d("TreatyOfCoruscant", "adding hint to prefs: " + hint);
    		e.commit();
        	setContentView(R.layout.main);
    	} else {
    		TextView error = (TextView)findViewById(R.id.mismatching_passwords_error);
    		error.setVisibility(View.VISIBLE);
    	}
    }
    
    public void cancelAuthentication(View v) {
        String password = prefs.getString(getString(R.string.password_key), null);
        if (password == null) {
        	setContentView(R.layout.create_password);
        } else {
        	setContentView(R.layout.main);
        }    	
    }
}