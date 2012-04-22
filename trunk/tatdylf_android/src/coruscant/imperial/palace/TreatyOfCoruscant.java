package coruscant.imperial.palace;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class TreatyOfCoruscant extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("TreatyOfCoruscant", "Started activity!!!");
        
//        if (!TheSenate.isServiceRunning()) {
        	Intent intent = new Intent(TreatyOfCoruscant.this, TheSenate.class);
        	intent.setAction("coruscant.imperial.palace.THE_SENATE");
        	startService(intent);
//        }
    }
}