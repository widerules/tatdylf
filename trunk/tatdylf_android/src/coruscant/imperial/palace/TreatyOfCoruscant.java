package coruscant.imperial.palace;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TreatyOfCoruscant extends Activity {
	SharedPreferences prefs;
	
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
        
       	Intent intent = new Intent(TreatyOfCoruscant.this, TheSenate.class);
       	intent.setAction("coruscant.imperial.palace.THE_SENATE");
       	startService(intent);
    }
    
    public void showPreferences(View v) {
    	setContentView(R.layout.authenticate);
    }
    
    public void showPasswordHint(View v) {
    	TextView hint = (TextView)findViewById(R.id.show_hint);
    	hint.setText(prefs.getString(getString(R.string.hint_key), null));
    	hint.setVisibility(View.VISIBLE);
    }
    
    public void submitPassword(View v) {
    	String savedPass = prefs.getString(getString(R.string.password_key), null);
    	EditText passField = (EditText)findViewById(R.id.authenticate_password);
    	String enteredPass = passField.getText().toString();
    	if(savedPass.equals(enteredPass)) {
    		Intent showPrefs = new Intent(this, Preferences.class);
    		showPrefs.setAction("coruscant.imperial.palace.PREFERENCES");
    		startActivity(showPrefs);
    	} else {
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
    		prefs.edit().putString(getString(R.string.password_key), pass);
    		prefs.edit().putString(getString(R.string.hint_key), hint);
        	setContentView(R.layout.main);
    	} else {
    		TextView error = (TextView)findViewById(R.id.mismatching_passwords_error);
    		error.setVisibility(View.VISIBLE);
    	}
    }
}