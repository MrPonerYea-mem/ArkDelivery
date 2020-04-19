package com.mem.arkdelivery.ui.dashboard;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mem.arkdelivery.MainActivity;
import com.mem.arkdelivery.R;
import com.mem.arkdelivery.map.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Spinner spinnerCity1;
    private Spinner spinnerCity2;
    private Button btnMap, btnMap2, buttonOrder;
    String[] city = {"Москва пункт 1", "Санкт-Петербург пункт 1", "Архангельск пункт 1", "Москва пункт 2", "Санкт-Петербург пунтк 2", "Архангельск пункт 2"};
    Map<String, Integer[]> position = new HashMap<>();
    private static String TAG = "order";

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView textView = root.findViewById(R.id.text_dashboard);
        spinnerCity1 = (Spinner) root.findViewById(R.id.spinner);
        spinnerCity2 = (Spinner) root.findViewById(R.id.spinner2);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity1.setAdapter(adapter);
        spinnerCity2.setAdapter(adapter);

        btnMap = (Button) root.findViewById(R.id.buttonOpenMap);
        btnMap2 = (Button) root.findViewById(R.id.buttonMapOpen);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("country", spinnerCity1.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        btnMap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("country", spinnerCity2.getSelectedItem().toString());
                startActivity(intent);
            }
        });

        buttonOrder = (Button) root.findViewById(R.id.buttonOrder);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerCity1.getSelectedItem() != spinnerCity2.getSelectedItem()) {
                    addOrder("https://arkhangelsdelivery.000webhostapp.com/addOrder.php");
                    Log.d(TAG, "0");
                } else {
                    Toast.makeText(inflater.getContext(), "Выберите пожалуйста разные пункты", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;

//        Handler handler = new Handler(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
//            }
//        });
        //SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //boolean session = sharedPreferences.getBoolean("session", false);
    }


    private void addOrder(String URL_add) {
        Log.d(TAG, "0.5");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                Log.d(TAG, "1");
                try {
                    obj = new JSONObject(response);
                    Log.d(TAG, "2");
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "3");
                    } else {
                        Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("adressFrom", spinnerCity1.getSelectedItem().toString());
                params.put("adressTo", spinnerCity2.getSelectedItem().toString());
                //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                params.put("login", preferences.getString("login", ""));
                Log.d(TAG, "login"+preferences.getString("login", ""));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);
    }

}
