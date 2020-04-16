package com.mem.arkdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    TextView textViewToLoginActivity;
    Button buttonRegistration;
    EditText editTextLogin;
    EditText editTextPassword;
    EditText editTextAcceptPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textViewToLoginActivity = (TextView) findViewById(R.id.textViewToLoginActivity);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextAcceptPassword = (EditText) findViewById(R.id.editTextAcceptPassword);
        buttonRegistration = (Button) findViewById((R.id.buttonRegister));

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextLogin.getText().toString().isEmpty() && !editTextAcceptPassword.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty() && editTextPassword.getText().toString().equals(editTextAcceptPassword.getText().toString())) {
                    registerUser("https://arkhangelsdelivery.000webhostapp.com/register.php");
                } else {
                    Toast.makeText(RegistrationActivity.this, "Введите все поля\nЛибо пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void registerUser(String URL_reg){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_reg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("login", editTextLogin.getText().toString());
                        editor.putString("password", editTextPassword.getText().toString());
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        Toast.makeText(RegistrationActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(RegistrationActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrationActivity.this, "При регистрации произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login", editTextLogin.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }
}
