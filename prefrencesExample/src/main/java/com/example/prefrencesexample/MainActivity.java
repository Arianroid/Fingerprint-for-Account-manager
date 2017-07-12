package com.example.prefrencesexample;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Read and show Data
		
		pref=getPreferences(MODE_APPEND);
		
		final TextView txtResult=(TextView)findViewById(R.id.tvResult);
		txtResult.setText(pref.getString("name", "No Value"));
		
		
		
		Button btn=(Button)findViewById(R.id.btnSave);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et=(EditText)findViewById(R.id.etName);
				
				String name=et.getText().toString();
				
				SharedPreferences.Editor editor=pref.edit();
				editor.putString("name", name);
				editor.commit();
				
				txtResult.setText(pref.getString("name", "No Value"));
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
