package com.nadaesimposible;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class inicioActivity extends Activity{

	Button button_siguiente;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		
		button_siguiente = (Button) findViewById(R.id.button_siguiente);
		
		button_siguiente.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intento = new Intent(getApplicationContext(), laylogin.class);
				startActivity(intento);
			}
		});
	}

}
