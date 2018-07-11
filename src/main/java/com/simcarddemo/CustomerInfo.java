package com.simcarddemo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerInfo extends AppCompatActivity implements Observer {


    //fp
//FP
    String FPID;
    String checkuser;
    int giRet = -100;
    public static final String TAG = "TestFileActivity";
    //http://103.21.34.216:8081/soap/IIBiometric
   // public static final String sURL = "http://gerbang2017.ddns.net:8081/soap/IIBiometric";
    public static final String sURL = "http://103.21.34.216:8081/soap/IIBiometric";
    public static final String SOAP_ACTION_MatchFinger_one2n_WSQ = "urn:uBiometricIntf-IIBiometric#MatchFinger_one2n_WSQ";
    public static final String METHOD_NAME_MatchFinger_one2n_WSQ = "MatchFinger_one2n_WSQ";
    public static final String path = "/storage/emulated/0/Image.wsq";
    public static final String NAMESPACE = "urn:uBiometricIntf-IIBiometric";
    private ProgressDialog progress;
    String byoperator = "http://103.21.34.216:81/simcarddemo/operator.php";
    String byinfo = "http://103.21.34.216:81/simcard/SimInfo.php";
    String byidURL = "http://103.21.34.216:81/simcarddemo/query.php";
    String getDocument = "http://103.21.34.216:81/simcarddemo/getDocno.php";
    String byfinger = "http://103.21.34.216:81/simcard/gambar.php";
    String testURL = "http://103.21.34.216:83/service/api/person/query?app-id=test&app-key=test&person-id=";

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
    String Info;
    String Info2;
    private Spinner spinner2;
    private TextView PhoneNo;
    private TextView SerialNo;
    private TextView PUK1;
    private TextView PUK2;
    String connectstate;
    WebView webviewacc;

    //simcard
    LinearLayout maxis;
    LinearLayout digi;
    LinearLayout celcom;
    LinearLayout umobile;
    TextView maxis1;
    TextView maxis2;
    TextView digi1;
    TextView digi2;
    TextView celcom1;
    TextView celcom2;
    TextView umobile1;
    TextView umobile2;

    String tvmaxis1;// = "PHONE NUMBER:";
    String tvmaxis2;// = "SERIAL NUMBER:";
    String tvdigi1;// = "PHONE NUMBER:";
    String tvdigi2;// = "SERIAL NUMBER:";
    String tvcelcom1;// = "PHONE NUMBER:";
    String tvcelcom2;// = "SERIAL NUMBER:";
    String tvumobile1;// = "PHONE NUMBER:";
    String tvumobile2;// = "SERIAL NUMBER:";


    String DocNo;
    String id;
    String FaceID;
    private boolean imageLoaded = false;
    String imageurl;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Boolean allowamaxnumber = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Intent intent = getIntent();
        //Bundle bundle = intent.getExtras();
        //id = bundle.getString("ID");
        //System.out.println("Customer:" + DocNo);

        webviewacc = (WebView)findViewById(R.id.webviewacc);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPass = (TextView) findViewById(R.id.tvPassport);
        tvID = (TextView) findViewById(R.id.tvID);
        tvdob = (TextView) findViewById(R.id.tvDOB);
        tvgender = (TextView) findViewById(R.id.tvGender);
        tvNationality = (TextView) findViewById(R.id.tvNationality);
        tvAdd1 = (TextView) findViewById(R.id.tvAdd1);
        tvAdd2 = (TextView) findViewById(R.id.tvAdd2);
        tvAdd3 = (TextView) findViewById(R.id.tvAdd3);
        tvPostcode = (TextView) findViewById(R.id.tvPostcode);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvState = (TextView) findViewById(R.id.tvState);
        custimage = (ImageView) findViewById(R.id.Custimage);


        //simcard
        maxis1 = (TextView) findViewById(R.id.tvmaxis1);
        maxis2 = (TextView) findViewById(R.id.tvmaxis2);
        digi1 = (TextView) findViewById(R.id.tvdigi1);
        digi2 = (TextView) findViewById(R.id.tvdigi2);
        celcom1 = (TextView) findViewById(R.id.tvcelcom1);
        celcom2 = (TextView) findViewById(R.id.tvcelcom2);
        umobile1 = (TextView) findViewById(R.id.tvumobile1);
        umobile2 = (TextView) findViewById(R.id.tvumobile2);
        maxis = (LinearLayout) findViewById(R.id.maxis);
        digi = (LinearLayout) findViewById(R.id.digi);
        celcom = (LinearLayout) findViewById(R.id.celcom);
        umobile = (LinearLayout) findViewById(R.id.umobile);



       /* Intent intent2 = new Intent(Intent.ACTION_MAIN);
        intent2.setComponent(new ComponentName("com.morpho.morphosample", "com.morpho.morphosample.ConnectionActivity"));
        startActivity(intent2);
*/try {

            Digent();
        }
        catch (Exception ex)
        {

        }


        Button theButton2 = (Button) findViewById(R.id.btnCancel);
        theButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button theButton = (Button) findViewById(R.id.btnProceed);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (theButton.getText().toString() == "Add New Number") {
                    //if add new number

                    if(allowamaxnumber==true) {
                        Toast.makeText(CustomerInfo.this, "Reach maximum registered Phone Number", Toast.LENGTH_LONG).show();
                        // theButton.setEnabled(false);
                    }
                    else {

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addSim);
                        linearLayout.setVisibility(View.VISIBLE);
                    }


                } else {
                    onBackPressed();
                    startActivity(new Intent(CustomerInfo.this, Register.class));
                }
            }
        });


        // GetDocNo();
        // Query();

        Button btnAddSim = (Button) findViewById(R.id.btnAddSim);
        btnAddSim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimInfo();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }





    public void Digent()
    {
       /* Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.morpho.morphosample", "com.morpho.morphosample.ConnectionActivity"));
        startActivity(intent,12);*/

        Intent intent = new Intent();
        intent.setClassName("com.morpho.morphosample", "com.morpho.morphosample.ConnectionActivity");

        startActivityForResult(intent, 12);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 12:
                try {
                    File file = new File(path);
                    if (file.exists()) {
                        GetMatch();

                        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");
                        String date = df.format(Calendar.getInstance().getTime());

                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Fingerprint captured.", Toast.LENGTH_LONG).show();


                        final Button theButton = (Button)findViewById(R.id.btnProceed);
                        theButton.setText("Register");
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
        }
    }

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    public void SimInfo() {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, byinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CustomerInfo.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                      /*  Intent intent3 = new Intent(Intent.ACTION_MAIN);
                        intent3.setComponent(new ComponentName("com.taztag.testthermalprinter", "com.taztag.testthermalprinter.MainActivity"));
                        // intent3.putExtra("Print", "Admin;"+Info2+";"+PhoneNo.getText().toString()+";"+formattedDate);
                        intent3.putExtra("Print", "Shop User ID : Admin \n   Customer ID: " + Info2 + "\n   Telco: " + spinner2.getSelectedItem().toString() + "\n   Phone No: " + PhoneNo.getText().toString() + ";;;Date: " + formattedDate);


                        startActivity(intent3);
                        onBackPressed();
*/

                        Intent intent = new Intent(CustomerInfo.this, Payment.class);
                        intent.putExtra("Print", "Shop User ID : Admin \n   Customer ID: " + Info2 + "\n   Telco: " + spinner2.getSelectedItem().toString() + "\n   Phone No: " + PhoneNo.getText().toString() + ";;;Date: " + formattedDate);
                        startActivity(intent);

                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            //Log.d(TAG, error.getMessage());
                            SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
//                        Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String, String> params = new HashMap<String, String>();

                spinner2 = (Spinner) findViewById(R.id.spinner2);
                PhoneNo = (TextView) findViewById(R.id.PhoneNo);
                SerialNo = (TextView) findViewById(R.id.SerialNo);
                PUK1 = (TextView) findViewById(R.id.PUK1);
                PUK2 = (TextView) findViewById(R.id.PUK2);

                params.put("docty", Info); //
                params.put("docno", Info2);
                params.put("operator", spinner2.getSelectedItem().toString());
                params.put("phoneNo", PhoneNo.getText().toString());
                params.put("serialno", SerialNo.getText().toString());
                params.put("puk1", PUK1.getText().toString());
                params.put("puk2", PUK2.getText().toString());

                System.out.println(Info);
                System.out.println(Info2);
                System.out.println(spinner2.getSelectedItem().toString());
                System.out.println(PhoneNo.getText().toString());
                System.out.println(PUK1.getText().toString());
                System.out.println(PUK2.getText().toString());


                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
        }
        return os.toByteArray();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CustomerInfo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.simcarddemo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CustomerInfo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.simcarddemo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void Clear()
    {
        //  tvDoc.setText("");
        tvName.setText("");
        tvPass.setText("");
        tvID.setText("");
        tvdob.setText("");
        tvNationality.setText("");
        tvgender.setText("");
        tvAdd1.setText("");
        tvAdd2.setText("");
        tvAdd3.setText("");
        tvPostcode.setText("");
        tvCity.setText("");
        tvState.setText("");

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


    public class MatchFinger_one2n_WSQAsyncTask extends AsyncTask<Integer, Integer, Integer> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            InputStream is = null;
            byte[] array = new byte[1000000];

            try {
                is = new FileInputStream(path);
                if (path != null) {
                    try {
                        array = streamToBytes(is);
                    } finally {
                        is.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    throw new IOException("Unable to open file (cho.wsq)");
                } catch (IOException e1) {

                    e1.printStackTrace();
                }
            }
            //*/
            try {
                //Create request
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MatchFinger_one2n_WSQ);
                //request.addProperty("firstNo",100);
                //request.addProperty("Second",300);
                PropertyInfo prop = new PropertyInfo();
//                prop.setName("UserID");//인자명이 틀리면 안된다.
//                prop.setValue("ChoFile");
//                prop.setType(String.class);
//                request.addProperty(prop);
//
//                prop = new PropertyInfo();
                prop.setName("myValue");
                prop.setValue(array);
                prop.setType(byte[].class);
                request.addProperty(prop);

                SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                new MarshalBase64().register(sse);
                sse.dotNet = true;
                sse.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(sURL);
                htse.call(SOAP_ACTION_MatchFinger_one2n_WSQ, sse);
                //Object response = (Object) sse.getResponse();
                //SoapObject response = (SoapObject) sse.getResponse();
                Vector<SoapObject> result = (Vector<SoapObject>) sse.getResponse();

                //Inside your for loop
                SoapObject so = result.get(0);

                Log.d(TAG, so.toString());
                Log.d(TAG, String.valueOf(so.getPropertyCount()));
                Log.d(TAG, so.getProperty(0).toString().replace(" ", ""));
                Log.d(TAG, so.getProperty(1).toString());
                //  Toast.makeText(getApplicationContext(), so.getProperty(0).toString().replace(" ",""), Toast.LENGTH_LONG).show();
                id = so.getProperty(0).toString().replace(" ", "");
                //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                System.out.println("ID : " + id);
                GetDocNo();
                //Query();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {

        }

        @Override
        protected void onPostExecute(Integer result) {
           // progress.dismiss();
            //    TextView myTv = (TextView) findViewById(R.id.textView2);
//            myTv.setText("giRet: "+Integer.toString(giRet));

            super.onPostExecute(result);
        }


    }

    Context mContext;

    public void GetMatch() {
        isConnectedToInternet();
        Clear();
        if (connectstate.contains("false")) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {

            progress = ProgressDialog.show(CustomerInfo.this, "Waiting", "Processing Fingerprint for Matching", true);
            ImageButton lookup = (ImageButton) findViewById(R.id.imageButton7);
            lookup.setVisibility(View.GONE);

            MatchFinger_one2n_WSQAsyncTask myAsync = new MatchFinger_one2n_WSQAsyncTask();

            myAsync.execute();
        }


    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void GetDocNo() {
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


                            if (jsonArray.length() == 0) {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Subscriber Does Not Exist", Toast.LENGTH_LONG).show();
                                custimage.setImageResource(R.drawable.norecord);
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                DocNo = customer.getString("Camvi");
                                System.out.println("Camvi : " + DocNo);
                                //finger();
                                Query();

                            }
                        } catch (JSONException ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {

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
    public void update(Observable observable, Object data) {

    }

    public void Query() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byidURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            progress.dismiss();
                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test : " + jsonArray.length());

                            if (jsonArray.length() == 0) {

                                Button theButton = (Button) findViewById(R.id.btnProceed);
                                theButton.setVisibility(View.VISIBLE);
                                theButton.setText("Register");
                                Button theButton2 = (Button) findViewById(R.id.btnCancel);
                                theButton2.setVisibility(View.VISIBLE);
                            } else {
                               // progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Subscriber Found. Populating Information", Toast.LENGTH_LONG).show();
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


                                if (name == "null") {
                                    name = "-";
                                } //Account No
                                tvName.setText(name);

                                if (tvName.getText() == "") {
                                    //Toast.makeText(getApplicationContext(), "Subscriber Does Not Exist", Toast.LENGTH_SHORT).show();

                                }


                                if (pass == "null") {
                                    pass = "-";
                                } // DocType
                                tvPass.setText(pass);
                                Info = pass;

                                if (id == "null") {
                                    id = "-";
                                } // DocType
                                tvID.setText(id);
                                Info2 = id;

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
                                getFaceID();
                                simcardinfo();

                                webviewacc.loadUrl("http://103.21.34.216:81/portal1/viewsimsupp.php?id="+Info2);

                            }
                        } catch (JSONException ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("AccNo", id);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*private void getImage() {

        String imgUrl = imageurl;
        System.out.println("imgURL 1: " + imgUrl);
        Picasso.with(this).load(imgUrl).into(imageSource);
        //aq.id(R.id.imageView).progress(R.id.imageView).image(imgUrl, true, true, 0, R.drawable.user);

    }*/

    private void getImage() {

        //String imgUrl = "http://152.3.214.12:8080/arges-service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        String imgUrl = "http://103.21.34.216:83/service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        System.out.println("imgURL 1: " + imgUrl);
        Picasso.with(this).load(imgUrl).into(custimage);
        progress.dismiss();
        Button theButton = (Button) findViewById(R.id.btnProceed);
        theButton.setVisibility(View.VISIBLE);
        theButton.setText("Add New Number");
        Button theButton2 = (Button) findViewById(R.id.btnCancel);
        theButton2.setVisibility(View.VISIBLE);
        // bio.disconnect();
        //aq.id(R.id.imageView).progress(R.id.imageView).image(imgUrl, true, true, 0, R.drawable.user);

    }

    public void simcardinfo() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byoperator,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);

                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            System.out.println("SIM query" + response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test" + jsonArray.length());

                            if (jsonArray.length() == 0) {
                                Toast.makeText(getApplicationContext(), "No SIM Card Information", Toast.LENGTH_SHORT).show();
                                TextView simInfodisplay = (TextView) findViewById(R.id.TVsimdet);
                                simInfodisplay.setText("CUSTOMER HAS NO SIM CARD INFORMATION");
                            }


                            //TextView simInfodisplay = (TextView) findViewById(R.id.TVsimdet);
                            //System.out.println("SIM LENGTH: "+jsonArray.length());
                            //System.out.println("On display: "+simInfodisplay.getText().toString());
                            //simInfodisplay.setText("\n\nSIM CARD INFORMATION");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);
                                //Toast.makeText(getApplicationContext(), "Array"+jsonArray.length(), Toast.LENGTH_SHORT).show();


                                String docno = customer.getString("DocumentNo");
                                String operator = customer.getString("Operator");
                                String phoneNo = customer.getString("PhoneNo");
                                String simserialno = customer.getString("SimSerialNo");

                                if (i ==4)
                                {
                                    allowamaxnumber = true;

                                }


                               /* simInfodisplay.setText(simInfodisplay.getText().toString()+ "\n\n Operator : " + operator
                                        + "\n  PhoneNo : " + phoneNo
                                        + "\n  SimSerialNo : " + simserialno);
*/

                                if (operator.contains("MAXIS")) {
                                    maxis.setVisibility(View.VISIBLE);
                                    maxis.setBackgroundColor(Color.parseColor("#FFB6C1"));
                                    tvmaxis1 = tvmaxis1 + "\n" + phoneNo;
                                    tvmaxis2 = tvmaxis2 + "\n" + simserialno;
                                    maxis1.setText(tvmaxis1);
                                    maxis2.setText(tvmaxis2);

                                }
                                if (operator.contains("DIGI")) {
                                    digi.setVisibility(View.VISIBLE);
                                    digi.setBackgroundColor(Color.parseColor("#FFFF99"));
                                    tvdigi1 = tvdigi1 + "\n" + phoneNo;
                                    tvdigi2 = tvdigi2 + "\n" + simserialno;
                                    digi1.setText(tvdigi1);
                                    digi2.setText(tvdigi2);

                                }
                                if (operator.contains("CELCOM")) {
                                    celcom.setVisibility(View.VISIBLE);
                                    celcom.setBackgroundColor(Color.parseColor("#add8e6"));
                                    tvcelcom1 = tvcelcom1 + "\n" + phoneNo;
                                    tvcelcom2 = tvcelcom2 + "\n" + simserialno;
                                    celcom1.setText(tvcelcom1);
                                    celcom2.setText(tvcelcom2);

                                }
                                if (operator.contains("UMOBILE")) {
                                    umobile.setVisibility(View.VISIBLE);
                                    umobile.setBackgroundColor(Color.parseColor("#FFB266"));
                                    tvumobile1 = tvumobile1 + "\n" + phoneNo;
                                    tvumobile2 = tvumobile2 + "\n" + simserialno;
                                    umobile1.setText(tvumobile1);
                                    umobile2.setText(tvumobile2);

                                }


                             /*   HashMap<String,String> persons = new HashMap<String,String>();
                                persons.put("DocumentNo",docno);
                                persons.put("Operator",operator);
                                persons.put("PhoneNo", phoneNo);
                                persons.put("SimSerialNo", simserialno);*/
                                //   personList.add(persons);
                            }

                        } catch (JSONException ex) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("AccNo", Info2);
                // System.out.println("Docno : " + DocNo);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getFaceID() {
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

                        while (m.find()) {
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

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Checking Internet Connection---------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/


    public void isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        connectstate = String.valueOf(isConnected);
    }
}
