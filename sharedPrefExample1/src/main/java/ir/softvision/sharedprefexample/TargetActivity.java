package ir.softvision.sharedprefexample;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.TextView;

public class TargetActivity extends Activity {

	
	String prefName="Ali";
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_target);
		
		pref=getSharedPreferences(prefName, MODE_APPEND);
		
		TextView txtResult=(TextView)findViewById(R.id.txtResult);
		
		txtResult.setText(pref.getString("name", "No Name"));
		txtResult.setTextSize(pref.getInt("font", 5));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_target, menu);
		return true;
	}

}
