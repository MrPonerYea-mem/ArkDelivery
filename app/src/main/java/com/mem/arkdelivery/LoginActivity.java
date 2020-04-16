package com.mem.arkdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUserLogin;
    EditText editTextUserPassword;
    Button btnLogin;
    String login, password;
    TextView textViewRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUserLogin = (EditText) findViewById(R.id.edtUsuario);
        editTextUserPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewRegistration = (TextView) findViewById(R.id.textViewRegister);

        getLastUser();

        textViewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = editTextUserLogin.getText().toString().trim();
                password = editTextUserPassword.getText().toString().trim();
                if (!login.isEmpty() && !password.isEmpty()) {
                    validateUser("https://arkhangelsdelivery.000webhostapp.com/user_validate.php");
                    //btnLogin.setClickable(false);
                } else{
                    Toast.makeText(LoginActivity.this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                    //btnLogin.setClickable(true);
                }
            }
        });
    }

    private void validateUser(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(LoginActivity.this, login + " " + password, Toast.LENGTH_SHORT).show();
                    saveLastUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Логин или пароль неверный", Toast.LENGTH_LONG).show();
                }
                //btnLogin.setClickable(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                //btnLogin.setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("login", login);
                parameters.put("password", password);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
        //VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void saveLastUser(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("login", login);
        editor.putString("password", password);
        editor.putBoolean("session", true);
        editor.commit();
    }

    private void getLastUser(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editTextUserLogin.setText(sharedPreferences.getString("login", ""));
        editTextUserPassword.setText(sharedPreferences.getString("password", ""));
    }
}
//<!--        android:usesCleartextTraffic="true"-->