package com.nadaesimposible;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class laylogin extends Activity {
	private EditText edit_nombre;
	private EditText edit_mail;
	private EditText edit_tel;
	private Button button_login;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		SharedPreferences mSharedPreferences;
		mSharedPreferences = getApplicationContext().getSharedPreferences("prefs", 0);

		if (!mSharedPreferences.getString("idUsuario", "S/D").equals("S/D")) {
			finish();
			startActivity(new Intent(laylogin.this, enviarhabilidadesActivity.class));
		}

		findViews();
		setEvents();
	}

	private void setEvents() {
		button_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nombre = edit_nombre.getText().toString();
				String mail = edit_mail.getText().toString();
				String telefono = edit_tel.getText().toString();

				String validacion = validarDatos();
				if (validacion.length() == 0) {
					String url2 = "http://gonzalobenoffi.com.ar/eventos/login.php?nombre=" + nombre + "&mail=" + mail
							+ "&telefono=" + telefono;

					showpDialog();

					JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url2, null,
							new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// Log.d(TAG, response.toString());
							try {
								/*
								 * 
								 * {"usuario":{"idUsuario":"21","nombre":
								 * "gonzalo","mail":"benoffi11@gmail.com",
								 * "telefono":"2235161108"}}
								 * 
								 */

								JSONObject usuario = response.getJSONObject("usuario");
								String idUsuario = usuario.getString("idUsuario");
								// Toast.makeText(getApplicationContext(),
								// idUsuario.trim(), Toast.LENGTH_SHORT).show();
								SharedPreferences mSharedPreferences;
								mSharedPreferences = getApplicationContext().getSharedPreferences("prefs", 0);
								SharedPreferences.Editor editor = mSharedPreferences.edit();
								editor.putString("idUsuario", idUsuario);
								editor.commit();
								finish();
								startActivity(new Intent(laylogin.this, enviarhabilidadesActivity.class));

							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG)
										.show();
							}
							hidepDialog();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
							// hide the progress dialog
							hidepDialog();
						}
					});

					RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
					mRequestQueue.add(jsonObjReq);
				} else {
					Toast.makeText(getApplicationContext(), validacion, Toast.LENGTH_LONG).show();
				}

			}

		});

	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	private void findViews() {
		edit_nombre = (EditText) findViewById(R.id.editText_nombre);
		edit_mail = (EditText) findViewById(R.id.editText_email);
		edit_tel = (EditText) findViewById(R.id.editText_telefono);
		button_login = (Button) findViewById(R.id.button_ingresar);

		pDialog = new ProgressDialog(laylogin.this);
		pDialog.setMessage("Cargando");

	}

	// Valido los datos ingresados
	public String validarDatos() {
		if (edit_nombre.getText().toString().length() == 0) {

			return "Falta nombre";
		}

		if (edit_mail.getText().toString().length() == 0) {

			return "Falta mail";
		}

		if (edit_tel.getText().toString().length() == 0) {

			return "Falta teléfono";
		}

		return "";
	}
}
