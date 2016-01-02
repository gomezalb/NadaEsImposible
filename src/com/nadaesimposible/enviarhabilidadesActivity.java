package com.nadaesimposible;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class enviarhabilidadesActivity extends Activity {

	String[] habilidades;
	EditText edittext_movilidad, edittext_nuevo_vap;
	Spinner spinner_habilidades;
	RadioButton radio_vap, radio_nuevo_vap;
	RadioGroup radiogroup_vap;
	Button button_enviar;
	protected static final String TAG = "internet";
		
	String vector[];
	String idUsuario;

	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enviarhabilidades);
		
		SharedPreferences mSharedPreferences;
		mSharedPreferences = getApplicationContext().getSharedPreferences(Configuracion.misprefs, 0);
		
		idUsuario = mSharedPreferences.getString(Configuracion.idUsuario, "S/D");
		
		edittext_movilidad = (EditText) findViewById(R.id.editText_movilidad);
		edittext_nuevo_vap = (EditText) findViewById(R.id.editText_nuevo_vap);
		spinner_habilidades = (Spinner) findViewById(R.id.spinner_habilidades);
		button_enviar = (Button) findViewById(R.id.button_enviar);
		radiogroup_vap = (RadioGroup) findViewById(R.id.radiogroup_vap);
		radio_vap = (RadioButton) findViewById(R.id.radio_vap);
		radio_nuevo_vap = (RadioButton) findViewById(R.id.radio_nuevo_vap);
		
		radio_vap.setChecked(true);
		
		pDialog = new ProgressDialog(enviarhabilidadesActivity.this);
		pDialog.setMessage("Cargando");
	
		requestInternet_vap();
		
		radiogroup_vap.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
					RadioButton rb = (RadioButton) findViewById(checkedId);

					if(checkedId == R.id.radio_vap)
					{
						edittext_nuevo_vap.setEnabled(false);
						spinner_habilidades.setEnabled(true);
						edittext_nuevo_vap.setText("");
					}
					else
					{
						edittext_nuevo_vap.setEnabled(true);
						spinner_habilidades.setEnabled(false);
						edittext_nuevo_vap.setText("");
						
					}
			}
		});
		
		button_enviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String validacion = validarDatos();
				if (validacion.length()==0)
				{
					String vap = null;
					String movilidad = edittext_movilidad.getText().toString();
					
					SharedPreferences prefs = getSharedPreferences(Configuracion.misprefs,Context.MODE_PRIVATE);
					String idUsuario = prefs.getString(Configuracion.idUsuario, ""); 
					
					if(radio_vap.isChecked()){
						int posicion = spinner_habilidades.getSelectedItemPosition();
						vap = vector[posicion];
					}
					else
					{
						vap = edittext_nuevo_vap.getText().toString();
					}
					
					//Toast.makeText(getApplicationContext(), idUsuario+' '+movilidad+' '+vap, Toast.LENGTH_LONG).show();
					
					movilidad = movilidad.replace(" ", "%20");
					vap = vap.replace(" ", "%20");
					
					String url = "http://gonzalobenoffi.com.ar/eventos/vap.php?idUsuario="+idUsuario+"&movilidad="+movilidad+"&vap="+vap;

					sendInternet_vap(url);
					
					edittext_movilidad.setText("");
					edittext_nuevo_vap.setText("");
					
					radio_vap.setChecked(true);
				}
				else
				{
					Toast.makeText(getApplicationContext(), validacion, Toast.LENGTH_LONG).show();
				}
				
			}
		});
	}
	
	
	private void sendInternet_vap(String url)
	{
		
		showpDialog();
		StringRequest request = new StringRequest(url, new Listener<String>() 
		{

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getApplicationContext(), "Datos enviados correctamente", Toast.LENGTH_SHORT).show();
				hidepDialog();
			}
		
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
				// hide the progress dialog
				hidepDialog();
			}
		
		}); 
		
		RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mRequestQueue.add(request);
	}
	
	private void requestInternet_vap()
	{
		showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(app.url_vap, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				//Log.d(TAG, response.toString());

				Log.d(TAG,"---------------ARREGLO----------");
				
				try
				{
					// Parsing json array response
					// loop through each json object
					String jsonResponse = "";
					vector = new String[response.length()];
					for (int i = 0; i < response.length(); i++)
					{

						JSONObject vap = (JSONObject) response.get(i);

						String name = vap.getString("vap");
						
						jsonResponse = "";

						jsonResponse += name + "\n\n";
					
						Log.d(TAG, jsonResponse);
						
						Log.d(TAG,"-------------------------");
						
						vector[i] = jsonResponse;

						
					}
					ArrayAdapter<String> adaptador_habilidades = new ArrayAdapter<String>(enviarhabilidadesActivity.this, android.R.layout.simple_spinner_item, vector);
					spinner_habilidades.setAdapter(adaptador_habilidades);

				}
				catch (JSONException e)
				{
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}

				hidepDialog();
				
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
				hidepDialog();
				
			}
		});
			
		
		RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		mRequestQueue.add(req);
	}

	
	//Valido los datos ingresados
		public String validarDatos()
			{
				
				if(edittext_movilidad.getText().toString().length()==0){
					
					return "Falta movilidad";
				}
				
				
				if(radio_vap.isChecked()){
					if(spinner_habilidades.getSelectedItem().toString() == ""){
						
						return "Falta Vap";
					}	
				}
				
				if(radio_nuevo_vap.isChecked()){
					if(edittext_nuevo_vap.getText().toString().length()== 0){
						
						return "Falta Vap";
					}	
				}
	
				return "";
			}
		
	
	private void showpDialog()
	{
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog()
	{
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
	
	
	
}
