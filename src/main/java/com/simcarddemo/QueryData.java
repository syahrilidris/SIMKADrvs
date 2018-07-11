package com.simcarddemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryData extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String temp;
    String searchingID = "0";
    //EditText ID;
    String ID;
    Button test1;
    Button test2;
    String byidURL = "http://103.21.34.216:81/Image/CheckUser.php";
    String getid = "http://103.21.34.216:81/simcarddemo/getid.php";
    String getidserial = "http://103.21.34.216:81/simcarddemo/getidserial.php";

    String getDocument = "http://103.21.34.216:81/simcarddemo/getDocno.php";
    String detail = "http://103.21.34.216:81/simcarddemo/query.php";
    String byoperator = "http://103.21.34.216:81/simcarddemo/operator.php";

    String testURL = "http://103.21.34.216:83/service/api/person/query?app-id=test&app-key=test&person-id=";

    /*String byidURL = "http://aril.16mb.com/simcard/query.php";
    String getDocument = "http://aril.16mb.com/simcard/getDocno.php";
    String byfinger = "http://aril.16mb.com/simcard/gambar.php";
    String testURL = "http://152.3.214.12:8080/arges-service/api/person/query?app-id=test&app-key=test&person-id=";*/

    String repl;
    private ProgressDialog progress;
    Spinner searchingtype;
    WebView webviewacc;
    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    private ImageView custimage;
    private TextView tvName;
    private TextView tvPass;
    private TextView tvID;
    private TextView tvdob;
    private TextView tvgender;
    private TextView tvNationality;
    private TextView tvAdd1;
    private TextView tvAdd2;
    private TextView tvAdd3;
    private TextView tvPostcode;
    private TextView tvCity;
    private TextView tvState;

    LinearLayout maxis;
    LinearLayout digi;
    LinearLayout celcom;
    LinearLayout umobile;

    String connectstate;

    TextView maxis1;
    TextView maxis2;
    TextView digi1;
    TextView digi2;
    TextView celcom1;
    TextView celcom2;
    TextView umobile1;
    TextView umobile2;

    private ScrollView scroll;

    private TextView tvOperator;
    private TextView tvPhone;
    private ListView list;

    String tvmaxis1;// = "PHONE NUMBER:";
    String tvmaxis2;// = "SERIAL NUMBER:";
    String tvdigi1;// = "PHONE NUMBER:";
    String tvdigi2;// = "SERIAL NUMBER:";
    String tvcelcom1;// = "PHONE NUMBER:";
    String tvcelcom2;// = "SERIAL NUMBER:";
    String tvumobile1;// = "PHONE NUMBER:";
    String tvumobile2;// = "SERIAL NUMBER:";

    String FaceID;
    String DocNo;
    private SearchView simpleSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_data);

        webviewacc = (WebView)findViewById(R.id.webviewacc);
        searchingtype = (Spinner)findViewById(R.id.spinner);
        searchingtype.setPrompt("Searching By...");
        tvName = (TextView)findViewById(R.id.tvName);
        tvPass = (TextView)findViewById(R.id.tvPassport);
        tvID = (TextView)findViewById(R.id.tvID);
        tvdob = (TextView)findViewById(R.id.tvDOB);
        tvgender = (TextView)findViewById(R.id.tvGender);
        tvNationality = (TextView)findViewById(R.id.tvNationality);
        tvAdd1 = (TextView)findViewById(R.id.tvAdd1);
        tvAdd2 = (TextView)findViewById(R.id.tvAdd2);
        tvAdd3 = (TextView)findViewById(R.id.tvAdd3);
        tvPostcode = (TextView)findViewById(R.id.tvPostcode);
        tvCity = (TextView)findViewById(R.id.tvCity);
        tvState = (TextView)findViewById(R.id.tvState);
        custimage = (ImageView)findViewById(R.id.Custimage);

        maxis1 = (TextView)findViewById(R.id.tvmaxis1);
        maxis2 = (TextView)findViewById(R.id.tvmaxis2);
        digi1 = (TextView)findViewById(R.id.tvdigi1);
        digi2 = (TextView)findViewById(R.id.tvdigi2);
        celcom1 = (TextView)findViewById(R.id.tvcelcom1);
        celcom2 = (TextView)findViewById(R.id.tvcelcom2);
        umobile1 = (TextView)findViewById(R.id.tvumobile1);
        umobile2 = (TextView)findViewById(R.id.tvumobile2);

        scroll = (ScrollView)findViewById(R.id.scroll1);

        tvOperator = (TextView)findViewById(R.id.operator);
        tvPhone = (TextView)findViewById(R.id.PhoneNo);

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();

        simpleSearchView = (SearchView) findViewById(R.id.searchView); // inititate a search view
        simpleSearchView.setOnQueryTextListener(this);


        maxis = (LinearLayout)findViewById(R.id.maxis);
        digi = (LinearLayout)findViewById(R.id.digi);
        celcom = (LinearLayout)findViewById(R.id.celcom);
        umobile = (LinearLayout)findViewById(R.id.umobile);


    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void clear()
    {
        maxis1.setText("");
        maxis2.setText("");
        digi1.setText("");
        digi2.setText("");
        umobile1.setText("");
        umobile2.setText("");
        celcom1.setText("");
        celcom2.setText("");

        tvmaxis1 = "PHONE NUMBER:";
        tvmaxis2 = "SERIAL NUMBER:";
        tvdigi1 = "PHONE NUMBER:";
        tvdigi2 = "SERIAL NUMBER:";
        tvcelcom1 = "PHONE NUMBER:";
        tvcelcom2 = "SERIAL NUMBER:";
        tvumobile1 = "PHONE NUMBER:";
        tvumobile2 = "SERIAL NUMBER:";
    }

    public boolean onQueryTextSubmit(String query) {

        isConnectedToInternet();
        clear();

        if(connectstate.contains("false"))
        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        else
        {
            // do something on text submit
            System.out.println("Result : " + query + " " + "Searching Type : " + searchingtype.getSelectedItem().toString());
            ID = query;
            searchingID = query;
            Choose(searchingtype.getSelectedItem().toString());
        }
        return false;
    }

    public int Choose(String month) {
        progress = ProgressDialog.show(QueryData.this, "Waiting", "Getting Details. Please Wait.", true);
        int Choose = 0;

        switch (month) {
            case "PhoneNo":
                getID();


                break;
            case "SimSerialNo":
                getIDSerial();
                break;
            case "DocumentNo":
                //Query();
                GetDocNo();
                break;
        }

        return Choose;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // do something when text changes
        return false;

    }

    public void getID()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getid,
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
                                scroll.setVisibility(View.INVISIBLE);
                            }
                            else {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject customer = jsonArray.getJSONObject(i);

                                    searchingID = customer.getString("DocumentNo");

                                }
                                GetDocNo();
                                //Verify();
                                //Query();
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
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        System.out.println(error);
                    }

                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                //parameters.put("AccNo", ID.getText().toString());
                //searchingID = ID.getText().toString();
                parameters.put("AccNo", ID);
                searchingID = ID;
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getIDSerial()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getidserial,
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
                                scroll.setVisibility(View.INVISIBLE);
                            }
                            else {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject customer = jsonArray.getJSONObject(i);

                                    searchingID = customer.getString("DocumentNo");

                                }
                                GetDocNo();
                                //Verify();
                                //Query();
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
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        System.out.println(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                //parameters.put("id", ID.getText().toString());
                //searchingID = ID.getText().toString();
                parameters.put("id", ID);
                searchingID = ID;
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ShowDetail()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

                                String name = customer.getString("Name");
                                String pass = customer.getString("Doc_Ty");
                                String id = customer.getString("Doc_No");
                                String dob = customer.getString("Birth_Date");
                                String gender = customer.getString("Gender");
                                String nationality = customer.getString("Nationality");
                                String add1 = customer.getString("Per_Addr1");
                                String add2 = customer.getString("Per_Addr2");
                                String add3 = customer.getString("Per_Addr3");
                                String postcode = customer.getString("Per_POBox");
                                String city = customer.getString("Per_Town");
                                String state = customer.getString("Per_State");


                                if ( name == "null") {
                                    name = "-";
                                } //Account No
                                tvName.setText(name);

                                if (pass == "null") {
                                    pass = "-";
                                } // DocType
                                tvPass.setText(pass);

                                if (id == "null") {
                                    id = "-";
                                } // DocType
                                tvID.setText(id);

                                if (dob == "null") {
                                    dob = "-";
                                } //DocCountry
                                tvdob.setText(dob);

                                if (gender == "null") {
                                    gender = "-";
                                } //DocCountry
                                tvgender.setText(gender);

                                if (nationality == "null") {
                                    nationality = "-";
                                } //DocCountry
                                tvNationality.setText(nationality);

                                if (add1 == "null") {
                                    add1 = "-";
                                } //DocCountry
                                tvAdd1.setText(add1);

                                if (add2 == "null") {
                                    add2 = "-";
                                } //DocCountry
                                tvAdd2.setText(add2);

                                if (add3 == "null") {
                                    add3 = "-";
                                } //Address1
                                tvAdd3.setText(add3);

                                if (postcode == "null") {
                                    postcode = "-";
                                } //DocCountry
                                tvPostcode.setText(postcode);

                                if (city == "null") {
                                    city = "-";
                                } //Address1
                                tvCity.setText(city);

                                if (state == "null") {
                                    state = "-";
                                } //Address1
                                tvState.setText(state);
                                simcardinfo();
                                getFaceID();


                                webviewacc.loadUrl("http://103.21.34.216:81/portal1/viewsimsupp.php?id="+id);



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
                parameters.put("AccNo", searchingID);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getImage() {

        progress.dismiss();


        TextView title = (TextView)findViewById(R.id.titlecustenq);
        title.setVisibility(View.GONE);
        //String imgUrl = "http://152.3.214.12:8080/arges-service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        String imgUrl = "http://103.21.34.216:83/service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        System.out.println("imgURL 1: " + imgUrl);
        Picasso.with(this).load(imgUrl).into(custimage);
        // bio.disconnect();
        //aq.id(R.id.imageView).progress(R.id.imageView).image(imgUrl, true, true, 0, R.drawable.user);

    }

    private void getFaceID()
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = testURL + DocNo;
        System.out.println(url);

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response);
                        System.out.println("Response" + response);
                        Pattern p = Pattern.compile("\\[(.*?)\\]");
                        Matcher m = p.matcher(response);

                        while(m.find()) {
                            System.out.println("Group 1: " + m.group(1));
                            FaceID = m.group(1);
                        }
                        getImage();
                        //System.out.println(FaceID);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
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
                                scroll.setVisibility(View.INVISIBLE);
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                DocNo = customer.getString("Camvi");
                                //finger();
                                scroll.setVisibility(View.VISIBLE);
                                ShowDetail();

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
                parameters.put("id", searchingID);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void simcardinfo()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byoperator,
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
                                Toast.makeText(getApplicationContext(), "No SIM Card Information", Toast.LENGTH_SHORT).show();
                                TextView simInfodisplay = (TextView) findViewById(R.id.textView31);
                                simInfodisplay.setText("CUSTOMER HAS NO SIM CARD INFORMATION");
                            }

                            TextView simInfo = (TextView) findViewById(R.id.textView31);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                String docno = customer.getString("DocumentNo");
                                String operator = customer.getString("Operator");
                                String phoneNo = "";
                                phoneNo = customer.getString("PhoneNo");
                                String simserialno = "";
                                simserialno = customer.getString("SimSerialNo");

                                //imageurl = customer.getString("Image");

                               /* simInfo.setText(simInfo.getText().toString()    + "\n\n Operator : " + operator
                                        + "\n  PhoneNo : " + phoneNo
                                        + "\n  SimSerialNo : " + simserialno);
*/

                                if(operator.contains("MAXIS"))
                                {
                                    maxis.setVisibility(View.VISIBLE);
                                    maxis.setBackgroundColor(Color.parseColor("#FFB6C1"));
                                    tvmaxis1 = tvmaxis1 + "\n"+phoneNo;
                                    tvmaxis2 = tvmaxis2 + "\n"+simserialno;
                                    maxis1.setText(tvmaxis1);
                                    maxis2.setText(tvmaxis2);
                                }
                                if(operator.contains("DIGI"))
                                {
                                    digi.setVisibility(View.VISIBLE);
                                    digi.setBackgroundColor(Color.parseColor("#FFFF99"));
                                    tvdigi1 = tvdigi1 + "\n"+phoneNo;
                                    tvdigi2 = tvdigi2 + "\n"+simserialno;
                                    digi1.setText(tvdigi1);
                                    digi2.setText(tvdigi2);

                                }
                                if(operator.contains("CELCOM"))
                                {
                                    celcom.setVisibility(View.VISIBLE);
                                    celcom.setBackgroundColor(Color.parseColor("#add8e6"));
                                    tvcelcom1 = tvcelcom1 + "\n"+phoneNo;
                                    tvcelcom2 = tvcelcom2 + "\n"+simserialno;
                                    celcom1.setText(tvcelcom1);
                                    celcom2.setText(tvcelcom2);

                                }
                                if(operator.contains("UMOBILE"))
                                {
                                    umobile.setVisibility(View.VISIBLE);
                                    umobile.setBackgroundColor(Color.parseColor("#FFB266"));
                                    tvumobile1 = tvumobile1 + "\n"+phoneNo;
                                    tvumobile2 = tvumobile2 + "\n"+simserialno;
                                    umobile1.setText(tvumobile1);
                                    umobile2.setText(tvumobile2);

                                }
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
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            progress.dismiss();
                            simcardinfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //System.out.println(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("AccNo", searchingID);
                System.out.println("Docno : " + DocNo);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

        /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Checking Internet Connection---------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/


    public void isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        connectstate = String.valueOf(isConnected);
    }

}
