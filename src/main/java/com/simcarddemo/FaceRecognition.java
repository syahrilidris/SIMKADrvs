package com.simcarddemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.google.android.gms.common.api.GoogleApiClient;

public class FaceRecognition extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    //Query
    String byidURL = "http://103.21.34.216:81/simcarddemo/query.php";
    String bySim = "http://103.21.34.216:81/simcarddemo/Totalsim.php";
    String byoperator = "http://103.21.34.216:81/simcarddemo/operator.php";
    String byinfo = "http://103.21.34.216:81/simcard/SimInfo.php";
    String testURL = "http://103.21.34.216:83/service/api/person/query?app-id=test&app-key=test&person-id=";

    private static final String ServerAddress = "103.21.34.216:83";
    private static final int CAMERA_REQUEST = 1888;
    private static final Long GroupID = 1L;
    private static final float Threshold = 0.5f;

    private String connectstatus;

    private ImageView custimage;
    private ImageView imageSource;
    private ImageButton Capturing;
    private ImageButton Searching;
    private RelativeLayout Info;
    //private TextView tvDoc;
    private TextView tvName;
    private TextView tvPass;
    private TextView tvID;
    private TextView tvDOB;
    private TextView tvNationality;
    private TextView tvgender;
    private TextView tvAdd1;
    private TextView tvAdd2;
    private TextView tvAdd3;
    private TextView tvPostcode;
    private TextView tvCity;
    private TextView tvState;
    private Spinner spinner2;
    private TextView PhoneNo;
    private TextView SerialNo;
    private TextView PUK1;
    private TextView PUK2;


    WebView webviewacc;
    LinearLayout maxis;
    LinearLayout digi;
    LinearLayout celcom;
    LinearLayout umobile;
    Bitmap actualimage;

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

    //image
    String FaceID;
    String Info1;
    String Info2;

    //simcard
    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    private TextView tvOperator;
    private TextView tvPhone;
    private ListView list;

    //private GoogleApiClient client;
    private Bitmap mphoto;
    private String Person;
    private boolean imageLoaded = false;
    private ProgressDialog progress;

    private String total;
    private String docID;
    String value1;
    TextView title;
    //File myFilesDir;
    private String connectstate;
    Boolean allowamaxnumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        webviewacc = (WebView)findViewById(R.id.webviewacc);
        Capturing = (ImageButton) findViewById(R.id.Camera);
        Searching = (ImageButton) findViewById(R.id.Searching);
        imageSource = (ImageView) findViewById(R.id.imageView3);
        Info = (RelativeLayout)findViewById(R.id.Info);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPass = (TextView) findViewById(R.id.tvPass);
        tvID = (TextView) findViewById(R.id.tvID);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
        tvNationality = (TextView) findViewById(R.id.tvNationality);
        tvgender = (TextView) findViewById(R.id.tvGender);
        tvAdd1 = (TextView) findViewById(R.id.tvAddr1);
        tvAdd2 = (TextView) findViewById(R.id.tvAddr2);
        tvAdd3 = (TextView) findViewById(R.id.tvAddr3);
        tvPostcode = (TextView) findViewById(R.id.tvPostcode);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvState = (TextView) findViewById(R.id.tvState);
        custimage = (ImageView)findViewById(R.id.Custimage);

        maxis1 = (TextView)findViewById(R.id.tvmaxis1);
        maxis2 = (TextView)findViewById(R.id.tvmaxis2);
        digi1 = (TextView)findViewById(R.id.tvdigi1);
        digi2 = (TextView)findViewById(R.id.tvdigi2);
        celcom1 = (TextView)findViewById(R.id.tvcelcom1);
        celcom2 = (TextView)findViewById(R.id.tvcelcom2);
        umobile1 = (TextView)findViewById(R.id.tvumobile1);
        umobile2 = (TextView)findViewById(R.id.tvumobile2);

        maxis = (LinearLayout)findViewById(R.id.maxis);
        digi = (LinearLayout)findViewById(R.id.digi);
        celcom = (LinearLayout)findViewById(R.id.celcom);
        umobile = (LinearLayout)findViewById(R.id.umobile);

        //simcard
        tvOperator = (TextView)findViewById(R.id.operator);
        tvPhone = (TextView)findViewById(R.id.PhoneNo);
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();


        startCamera();

        Button btnAddSim = (Button)findViewById(R.id.btnAddSim);
        btnAddSim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              SimInfo();
            }
        });

        Button theButton2 = (Button)findViewById(R.id.btnCancel);
        theButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final Button theButton = (Button)findViewById(R.id.btnProceed);
        theButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              if (theButton.getText().toString()=="Add New Number")
               {
                   if(allowamaxnumber==true) {
                       Toast.makeText(FaceRecognition.this, "Reach maximum registered Phone Number", Toast.LENGTH_LONG).show();
                       // theButton.setEnabled(false);

                   }
                   else {

                       //if add new number
                       LinearLayout linearLayout = (LinearLayout) findViewById(R.id.addSim);
                       linearLayout.setVisibility(View.VISIBLE);

                    /*   LinearLayout Telco = (LinearLayout) findViewById(R.id.telco);
                       Telco.setVisibility(View.GONE);

                       RelativeLayout Relative = (RelativeLayout) findViewById(R.id.relativeLayout);
                       Relative.setVisibility(View.GONE);

                       RelativeLayout linearLayout2 = (RelativeLayout) findViewById(R.id.L2);
                       linearLayout2.setVisibility(View.GONE);
                       LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linear1);
                       linearLayout3.setVisibility(View.GONE);
                       ImageView customer = (ImageView) findViewById(R.id.Custimage);
                       customer.setVisibility(View.GONE);
                       TextView info = (TextView) findViewById(R.id.textView31);
                       info.setVisibility(View.GONE);
                       list.setVisibility(View.GONE);

                       LinearLayout webviewdisplay = (LinearLayout) findViewById(R.id.webviewdisplay);
                       webviewdisplay.setVisibility (View.GONE);*/
                   }
              }


                else
                {
                    final Dialog dialog = new Dialog(FaceRecognition.this);
                    dialog.setContentView(R.layout.custom_dialog_box);
                    dialog.setTitle("Please Select Type: ");
                    Button btnExit = (Button) dialog.findViewById(R.id.btnNormal);
                    btnExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(FaceRecognition.this, Register.class);
                            intent.putExtra("type","CITIZEN");
                            startActivity(intent);
                            dialog.dismiss();
                            //Clearfile();
                            //CheckFP();
                        }
                    });

                    dialog.findViewById(R.id.btnMinor).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FaceRecognition.this, Register.class);
                            intent.putExtra("type","FOREIGNER");
                            startActivity(intent);

                            dialog.dismiss();
                            //Clearfile();
                            //CheckFP();
                        }
                    });

                    dialog.findViewById(R.id.btnCorporate).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FaceRecognition.this, Register.class);
                            intent.putExtra("type","BUSINESS");
                            startActivity(intent);

                            dialog.dismiss();
                            //Clearfile();
                            //CheckFP();
                        }
                    });
                    // show dialog on screen
                    dialog.show();

                }


             /*   else
              {
                //  onBackPressed();
               //   startActivity(new Intent(FaceRecognition.this, Register.class));

                  Bitmap bitmap = ((BitmapDrawable)imageSource.getDrawable()).getBitmap();

                  Bundle extras = new Bundle();
                  Intent intent=new Intent(FaceRecognition.this, Register.class);
                  extras.putParcelable("Bitmap", bitmap);
                  intent.putExtras(extras);
                  startActivity(intent);

              }*/
            }
        });

    }

    private void startCamera()
    {
        File file;
        Uri fileUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1888);
    }
    public void Recapture(View view)
    {
       startCamera();
    }

    public void Search ()
    {
        if (imageSource.getDrawable() == null) {
            Toast.makeText(FaceRecognition.this, "No Image captured. Please try again", Toast.LENGTH_LONG).show();
        } else {

            SearchingTask task = new SearchingTask();
            task.execute(actualimage);
        }
    }


    public void checkStat()
    {
        if (title.getText().toString()== "Fingerprint") {
            //check fingerprint ID with
            findViewById(R.id.L2).setVisibility(View.INVISIBLE);
            //   docID = "xx11";
            //  Query();
        }
        else
        {
            findViewById(R.id.L2).setVisibility(View.VISIBLE);
        }
    }
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    public void SimInfo()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, byinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(FaceRecognition.this, "Successfully Registered", Toast.LENGTH_LONG).show();



                        Intent intent = new Intent(FaceRecognition.this, Payment.class);
                        intent.putExtra("Print", "Shop User ID : Admin \n   Customer ID: "+Info2+"\n   Telco: "+ spinner2.getSelectedItem().toString() +"\n   Phone No: "+PhoneNo.getText().toString()+";;;Date: "+formattedDate);
                        startActivity(intent);

                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
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
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                spinner2 = (Spinner) findViewById(R.id.spinner2);
                PhoneNo = (TextView) findViewById(R.id.PhoneNo);
                SerialNo = (TextView) findViewById(R.id.SerialNo);
                PUK1 = (TextView) findViewById(R.id.PUK1);
                PUK2 = (TextView) findViewById(R.id.PUK2);

                params.put("docty", Info1); //
                params.put("docno", Info2);
                params.put("operator",spinner2.getSelectedItem().toString());
                params.put("phoneNo",PhoneNo.getText().toString());
                params.put("serialno", SerialNo.getText().toString());
                params.put("puk1",PUK1.getText().toString());
                params.put("puk2", PUK2.getText().toString());

                System.out.println (Info1);
                System.out.println (Info2);
                System.out.println (spinner2.getSelectedItem().toString());
                System.out.println (PhoneNo.getText().toString());
                System.out.println (PUK1.getText().toString());
                System.out.println (PUK2.getText().toString());


                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public void CheckImage(View v)
    {
        if(imageSource.getDrawable() == null)
        {
            Toast.makeText(FaceRecognition.this, "Image not insert", Toast.LENGTH_LONG).show();
        }
        else {
            SearchingTask task = new SearchingTask();
            task.execute(((BitmapDrawable) imageSource.getDrawable()).getBitmap());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 1888 && resultCode == RESULT_OK) {

            if (mphoto != null) {
                mphoto.recycle();
            }
            mphoto = (Bitmap) data.getExtras().get("data");
            imageSource.setImageBitmap(mphoto);
            deleteFiles("storage/emulated/0/DCIM/Camera/");
            callBroadCast();

            Search();
        }*/

        if (requestCode == 1888 && resultCode == RESULT_OK) {

            if (mphoto != null) {
                mphoto.recycle();
            }

            File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");
            Matrix matrix = new Matrix();
            boolean swapWidthHeight = false;

            try {

                ExifInterface exif = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,1);

                switch (orientation)
                {
                    case 3: //180
                        matrix.postRotate(180);
                        break;

                    case 6://90
                        matrix.postRotate(90);
                        swapWidthHeight = true;
                        break;

                    case 8://270
                        matrix.postRotate(270);
                        swapWidthHeight = true;
                        break;
                }
            }
            catch (Exception ex)
            {
            }

            mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap rotatedBitmap = Bitmap.createBitmap(mphoto,0,0,mphoto.getWidth(),mphoto.getHeight(),matrix,true);

            int newWidth=0, newHeight=0;
            if (mphoto.getWidth() >= mphoto.getHeight()) {
                newHeight = 500;
                newWidth = mphoto.getWidth() * newHeight / mphoto.getHeight();
            } else {
                newWidth = 500;
                newHeight = mphoto.getHeight() * newWidth / mphoto.getWidth();
            }
            if (swapWidthHeight) {
                int temp = newWidth;
                newWidth = newHeight;
                newHeight = temp;
            }

          //actualimage = Bitmap.createScaledBitmap(mphoto, 900, 540, false);
            actualimage = Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, false);
            imageSource.setImageBitmap(mphoto);
            Search();
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Delete File-------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/


    public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Refresh Gallery---------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{"storage/emulated/0/DCIM/Camera/"}, new String[]{"image/jpeg"}, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File("/storage/emulated/0/DCIM/Camera/");
            Uri contentUri = Uri.fromFile(f);
            Log.e("ExternalStorage", "-> uri=" + contentUri);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);


        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
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

    public class SearchingTask extends AsyncTask<Bitmap, Void, String> {
        @Override


        protected void onPreExecute() {
            isConnectedToInternet();
            Clear();
            progress = ProgressDialog.show(FaceRecognition.this, "Waiting", "Searching", true);
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            return search(GroupID, params[0], 3, Threshold);
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(FaceRecognition.this, Person, Toast.LENGTH_LONG).show();
            progress.dismiss();

                if (result != null) {
                        if (result.isEmpty()) {
                            Toast.makeText(FaceRecognition.this, "No match was found", Toast.LENGTH_LONG).show();
                        } else {
                            //Info.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Subscriber Found. Populating Information", Toast.LENGTH_LONG).show();

                            Info.setVisibility(View.VISIBLE);
                            //resultAdapter.update(result);
                        }
                } else {
                    //more.setVisibility(View.VISIBLE);
                    if (connectstate.contains("false")) {
                        progress.dismiss();
                        Toast.makeText(FaceRecognition.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(FaceRecognition.this, "No match was found", Toast.LENGTH_LONG).show();
                        Button theButton = (Button) findViewById(R.id.btnProceed);
                        theButton.setVisibility(View.VISIBLE);
                        theButton.setText("Register");
                        Button theButton2 = (Button) findViewById(R.id.btnCancel);
                        theButton2.setVisibility(View.VISIBLE);
                    }
                }
        }
    }

    protected String search(Long groupId, Bitmap photo, int tops, float threshold) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app-id", "test");
        params.put("app-key", "test");
        params.put("group-id", groupId.toString());
        params.put("image-data", toBase64Image(photo));
        params.put("tops", Integer.toString(tops));
        params.put("threshold", Float.toString(threshold));
        try {
            byte[] resp = doPost("/search", params);
            Log.i(TAG, new String(resp, "UTF-8"));
            String test = (new String(resp, "UTF-8"));

            if(test.length() == 2 )
            {
                return null ;
            }
            else {
                Pattern p = Pattern.compile("-?\\d+");
                Matcher m = p.matcher(new String(resp, "UTF-8"));
                if (m.find()) {
                    //System.out.println("Group 0: " + m.group(0));
                    Person = m.group(0).toString();
                    System.out.println("Person: "+Person);
                    //Toast.makeText(FaceRecognition.this, "ID : " + Person, Toast.LENGTH_LONG).show();
                }
                //ObjectMapper mapper = new ObjectMapper();
                //String result = mapper.readValue(resp, new TypeReference<String>() {
                //});
                Totalsim();
                //Query();
                return test;
            }

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            //Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    protected String toBase64Image(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] byteArray = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    static public String getServerUrlBase() {
        return "http://" + ServerAddress + "/service/api";
    }

    protected byte[] doPost(String path, Map<String, String> parameters) throws Exception {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String serverBase = getServerUrlBase();
        if (serverBase == null) {
            throw new Exception("Server's URL base is not set!");
        }

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(serverBase + path);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                urlConnection.setRequestProperty("Connection", "close");
            }
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String postData = createQueryString(parameters);
            urlConnection.setFixedLengthStreamingMode(postData.getBytes().length);
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postData);
            out.close();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                if (statusCode == 500) {
                    try {
                        byte[] data = getResponseData(urlConnection);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Register.ServerException exp = objectMapper.readValue(data, Register.ServerException.class);
                        Log.w(TAG, "Fail to search: " + exp.getMessage());
                    } catch (IOException e) {
                        Log.w(TAG, "Fail to search: " + statusCode);
                    }
                }
                throw new Exception("Access failure");
            }
            return getResponseData(urlConnection);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL [" + serverBase + path + "]: " + e.getMessage(), e);
            throw new Exception("Bad URL: " + serverBase + path);
        } catch (IOException e) {
            connectstatus = e.getMessage();
            Log.e(TAG, "Access remote service error: " + e.getMessage(), e);
            throw new Exception("Access remote service error: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private byte[] getResponseData(HttpURLConnection conn) throws IOException {
        InputStream inputStream = new BufferedInputStream(conn.getInputStream());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        int n;
        while ((n = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, n);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    private String createQueryString(Map<String, String> parameters) {
        StringBuilder queryString = new StringBuilder();
        if (parameters != null) {
            boolean first = true;
            for (String name : parameters.keySet()) {
                if (!first) {
                    queryString.append(PARAMETER_DELIMITER);
                }
                try {
                    queryString.append(name).append(PARAMETER_EQUALS_CHAR).append(URLEncoder.encode(parameters.get(name), "UTF-8"));
                    if (first) {
                        first = false;
                    }

                } catch (UnsupportedEncodingException e) {
                    Log.w(TAG, "Encode http request param [" + name + "] error: " + e.getMessage());
                }
            }
        }
        return queryString.toString();
    }

    public void Query()
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, byidURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Totalsim();
                            System.out.println(response);

                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test" + jsonArray.length());

                            if(jsonArray.length()==0)
                            {
                                Toast.makeText(FaceRecognition.this, "No Data Exist", Toast.LENGTH_SHORT).show();
                                Button theButton = (Button)findViewById(R.id.btnProceed);
                                theButton.setVisibility(View.VISIBLE);
                                Button theButton2 = (Button)findViewById(R.id.btnCancel);
                                theButton2.setVisibility(View.VISIBLE);
                            }
                            else {

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

                                //Info.setVisibility(View.VISIBLE);
                                //imageurl = customer.getString("Image");

                                if (name == "null") {
                                    name = "-";
                                } //Name
                                tvName.setText(name);


                                if (pass == "null") {
                                    pass = "-";
                                } // DocType
                                tvPass.setText(pass);
                                Info1 = pass;
                                if (id == "null") {
                                    id = "-";
                                } //Account No
                                tvID.setText(id);
                                Info2 = id;
                                if (dob == "null") {
                                    dob = "-";
                                } //DocCountry
                                tvDOB.setText(dob);

                                if (nationality == "null") {
                                    nationality = "-";
                                } //DocCountry
                                tvNationality.setText(nationality);

                                if (gender == "null") {
                                    gender = "-";
                                } //DocCountry
                                tvgender.setText(gender);

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

                                webviewacc.loadUrl("http://103.21.34.216:81/portal1/viewsimsupp.php?id="+Info2);


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
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG);
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        System.out.println(error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("AccNo", docID);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Totalsim() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, bySim,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Totalsim();
                            System.out.println(response);

                            //Creating JsonObject from response String
                            JSONObject jsonObject = new JSONObject(response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test" + jsonArray.length());

                            if(jsonArray.length()==0)
                            {
                                Toast.makeText(FaceRecognition.this, "Information Does not Exist", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject customer = jsonArray.getJSONObject(i);

                                    docID = customer.getString("Doc_No");
                                    System.out.println();
                                }
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
                parameters.put("AccNo", Person);
                return parameters;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getImage() {

        //String imgUrl = "http://152.3.214.12:8080/arges-service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        String imgUrl = "http://103.21.34.216:83/service/api/person/face-image/" + FaceID + "?app-id=test&app-key=test";
        System.out.println("imgURL 1: " + imgUrl);
        Picasso.with(this).load(imgUrl).into(custimage);
        progress.dismiss();
        Button theButton = (Button)findViewById(R.id.btnProceed);
        theButton.setVisibility(View.VISIBLE);
        theButton.setText("Add New Number");
        Button theButton2 = (Button)findViewById(R.id.btnCancel);
        theButton2.setVisibility(View.VISIBLE);

        // bio.disconnect();
        //aq.id(R.id.imageView).progress(R.id.imageView).image(imgUrl, true, true, 0, R.drawable.user);

    }

    private void getFaceID()
    {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = testURL + Person;
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

                            System.out.println("SIM query" + response.toString());

                            //extracting json array from response string
                            JSONArray jsonArray = jsonObject.getJSONArray("customer");
                            System.out.println("test" + jsonArray.length());

                            if(jsonArray.length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "No SIM Card Information", Toast.LENGTH_SHORT).show();
                                TextView simInfodisplay = (TextView) findViewById(R.id.TVsimdet);
                                simInfodisplay.setText("CUSTOMER HAS NO SIM CARD INFORMATION");
                            }

                            TextView simInfo = (TextView) findViewById(R.id.textView31);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject customer = jsonArray.getJSONObject(i);

                                String docno = customer.getString("DocumentNo");
                                String operator = customer.getString("Operator");
                                String phoneNo = customer.getString("PhoneNo");
                                String simserialno = customer.getString("SimSerialNo");


                                if (i ==4)
                                {
                                    allowamaxnumber = true;

                                }



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
                        System.out.println(error);
                    }
                }){

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

    public void Clear()
    {
      //  tvDoc.setText("");
        tvName.setText("");
        tvPass.setText("");
        tvID.setText("");
        tvDOB.setText("");
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

}
