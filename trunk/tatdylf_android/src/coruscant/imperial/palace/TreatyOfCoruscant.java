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
        
        Intent intent = new Intent(this, TheForce.class);
        intent.setAction("coruscant.imperial.palace.THE_FORCE");
        startService(intent);
    }
}