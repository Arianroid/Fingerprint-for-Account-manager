package ir.softvision.sharedprefexample;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{

	String prefName="Ali";
	SharedPreferences pref;
	
	SeekBar skFont;
	Button btn;
	EditText etName;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // configuration
        
       Configuration config=new Configuration();
       config.locale=new Locale("fa");
       // update config
       Resources res = this.getResources();
       // Change locale settings in the app.
       DisplayMetrics dm = res.getDisplayMetrics();
       android.content.res.Configuration conf = res.getConfiguration();
       conf.locale = new Locale("fa");
       res.updateConfiguration(conf, dm);
       
        // pref
        pref=getSharedPreferences(prefName, MODE_APPEND);
        
        skFont=(SeekBar)findViewById(R.id.skFont);
        
        btn=(Button)findViewById(R.id.btnClick);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
				SharedPreferences.Editor editor=pref.edit();
				editor.putString("name", etName.getText().toString());
				editor.commit();
				
				startActivity(new Intent(MainActivity.this,TargetActivity.class));
				
			}
		});
        
        etName=(EditText)findViewById(R.id.etNAme);
        
        
        skFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			
				
				etName.setTextSize(progress);
				SharedPreferences.Editor editor=pref.edit();
				editor.putInt("font", progress);
				editor.commit();
				
				
				
			}
		});
        
       
        
        
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
    
}
