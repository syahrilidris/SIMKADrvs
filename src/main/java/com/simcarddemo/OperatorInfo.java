package com.simcarddemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OperatorInfo extends AppCompatActivity {

    String byidURL = "http://aril.16mb.com/simcard/operator.php";
    String getDocument = "http://aril.16mb.com/simcard/getDocno.php";
    String testURL = "http://152.3.214.12:8080/arges-service/api/person/query?app-id=test&app-key=test&person-id=";
    private TextView title;
    private TextView tvOperator;
    private TextView tvPhone;
    private TextView tvdocno;
    private ListView list;
    private String DocNo;
    private String id;

    private ProgressDialog progress;

    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("ID");
        System.out.println("result : " + id);

        //title = (TextView)findViewById(R.id.title);
        tvOperator = (TextView)findViewById(R.id.operator);
        tvPhone = (TextView)findViewById(R.id.PhoneNo);

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();

        //getData();
        //Query();
        progress = ProgressDialog.show(OperatorInfo.this, "Waiting", "Sending", true);
        Query();
        //GetDocNo();
    }

    public void Query()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byidURL,
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
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Subscriber Does Not Exist", Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                String operator = customer.getString("Operator");
                                String phoneNo = customer.getString("PhoneNo");
                                String simserialno = customer.getString("SimSerialNo");

                                //imageurl = customer.getString("Image");

                                HashMap<String,String> persons = new HashMap<String,String>();

                                persons.put("Operator",operator);
                                persons.put("PhoneNo", phoneNo);
                                persons.put("SimSerialNo", simserialno);

                                personList.add(persons);
                            }

                            ListAdapter adapter = new SimpleAdapter(
                                    OperatorInfo.this, personList, R.layout.dblist,
                                    new String[]{"Operator","PhoneNo", "SimSerialNo"},
                                    new int[]{R.id.operator, R.id.PhoneNo, R.id.textView21}
                            );

                            progress.dismiss();
                            list.setAdapter(adapter);

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
                parameters.put("AccNo", id);
                System.out.println("Docno : " + DocNo);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void GetDocNo()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getDocument,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);

                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");


                            if(jsonArray.length() == 0)
                            {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Subscriber Does Not Exist", Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                DocNo = customer.getString("DocNo");
                                Query();
                            }
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
                parameters.put("id", id);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
