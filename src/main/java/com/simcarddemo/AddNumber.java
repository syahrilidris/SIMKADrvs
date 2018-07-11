package com.simcarddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNumber extends AppCompatActivity implements View.OnClickListener{

    String byidURL = "http://aril.16mb.com/Image/SimInfo.php";
    String byName = "http://aril.16mb.com/Image/searchName.php";
    protected final String TAG = getClass().getSimpleName();
    private Button Add;
    private TextView PUK1;
    private TextView PUK2;
    private TextView SerialNo;
    private TextView PhoneNo;
    private Spinner Aoperator;
    private String DocNo;
    private TextView IDName;

    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ID = bundle.getString("ID");

        Add = (Button)findViewById(R.id.button);
        PUK1 = (TextView)findViewById(R.id.AtvPUK1);
        PUK2 = (TextView)findViewById(R.id.AtvPUK2);
        SerialNo = (TextView)findViewById(R.id.AtvSerialNo);
        PhoneNo = (TextView)findViewById(R.id.AtvPhone);
        Aoperator = (Spinner)findViewById(R.id.Aoperator);
        IDName = (TextView)findViewById(R.id.textView13);

        searchName();

        Add.setOnClickListener(this);
    }

    public void searchName()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);

                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test" + jsonArray.length());

                            if(jsonArray.length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "Subscriber Does Not Exist", Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                DocNo = customer.getString("DocNo");
                                String name = customer.getString("Name");
                                IDName.setText("Hello, " + name + " for Subscriber ID : " + DocNo);
                            }
                            //Verify();
                        }
                        catch (JSONException ex)
                        {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("AccNo", ID);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onClick(View v)
    {
        if(v==Add)
        {
            registerUser();
            Add.setEnabled(false);
            Toast.makeText(AddNumber.this, "Successfully Registered", Toast.LENGTH_LONG).show();
        }
    }

    public void registerUser()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, byidURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("register: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docno", DocNo);
                params.put("operator", Aoperator.getSelectedItem().toString());
                params.put("phoneNo", PhoneNo.getText().toString());
                params.put("puk1",PUK1.getText().toString());
                params.put("puk2", PUK2.getText().toString());
                params.put("serialno", SerialNo.getText().toString());
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        System.out.println("Regrequest" + stringRequest);
    }
}
