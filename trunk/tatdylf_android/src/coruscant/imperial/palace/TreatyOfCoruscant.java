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

public class TreatyOfCoruscant extends Activity {
	SharedPreferences prefs;
	static boolean isInit = false;
	
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
        if(!isInit){
        	try {
//				Init.init(getApplicationContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
        	isInit = true;
        }
       	Intent intent = new Intent(TreatyOfCoruscant.this, TheSenate.class);
       	intent.setAction("coruscant.imperial.palace.THE_SENATE");
       	startService(intent);
    }
    
    public void showPreferences(View v) {
       	Intent intent = new Intent(TreatyOfCoruscant.this, LowerDeflectorShield.class);
       	intent.setAction("coruscant.imperial.palace.LOWER_DEFLECTOR_SHIELD");
       	startActivity(intent);
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