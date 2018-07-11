package com.simcarddemo;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acs.smartcard.Features;
import com.acs.smartcard.Reader;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubmitPreview extends AppCompatActivity implements Observer {

    public static final String page1 = "/storage/emulated/0/page1.txt";
    public static final String page2 = "/storage/emulated/0/page2.txt";

    final static String suppdoc = "/storage/emulated/0/suppdoc.txt";
    public static String SuppdocURL = "http://103.21.34.216:81/simcardsupp/suppdoc.php";

    protected final String TAG = getClass().getSimpleName();
    String bypersonalinfo = "http://103.21.34.216:81/simcarddemo/insert.php";
    String bypersonalinfo2 = "http://103.21.34.216:81/simcarddemo/insert2.php";
    String bysiminfo = "http://103.21.34.216:81/simcarddemo/SimInfo.php";
    String bybusinessinfo = "http://103.21.34.216:81/simcarddemo/SimInfo.php";
    private static final String ServerAddress = "103.21.34.216:83";

    private static final int CAMERA_REQUEST = 1888;
    private static final int BARCODE_REQUEST = 12;
    private static final int MY_REQUEST_CODE =5;
    private static final int FP_CODE =1;

    Bitmap actualimage;
    String categorytype;
    TextView categorytitle;

    private ImageView AttachDoc;
    private ImageView LiveCapture ;
    private ImageView mykadImage;
    private TextView lblMykad;

    String count;
    //Personal Info
    String Name;
    String ID;
    String DOB;
    String Nationality;
    String Gender;
    String Address;
    String Address2;
    String Address3;
    String Postcode;
    String City;
    String State;

    //Sim Info
    String Operator;
    String PUK1;
    String PUK2;
    String PhoneNo;
    String SerialNo;

    //Sim Info
    String Operatorsim2;
    String PUK1sim2;
    String PUK2sim2;
    String PhoneNosim2;
    String SerialNosim2;

    //Sim Info
    String Operatorsim3;
    String PUK1sim3;
    String PUK2sim3;
    String PhoneNosim3;
    String SerialNosim3;

    //Sim Info
    String Operatorsim4;
    String PUK1sim4;
    String PUK2sim4;
    String PhoneNosim4;
    String SerialNosim4;

    //Sim Info
    String Operatorsim5;
    String PUK1sim5;
    String PUK2sim5;
    String PhoneNosim5;
    String SerialNosim5;

    private Button Register;
    private Button Movers;
    private Button Save;
    private Bitmap mphoto;
    private Bitmap mphoto2;
    private Spinner spinner; //citizen
    private Spinner spinner3; //non-citizen
    private Spinner spinner2;
    private boolean imageLoaded = false;

    private ArrayList<MyImage> images;
    private ImageAdapter imageAdapter;
    private ListView listView;
    MyImage image;

    String AccountNo ;
    String DocNo;
    String DocTy;


    String ActivDt;
    private String PersonID;
    String repl;
    private ProgressDialog progress;
    private TextView ShowID;
    public String camera;

    String myString;
    private String connectstate;
    String connection;
    String id;

    //DOB
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    TextView persotitle;
    TextView persoview;

    TextView simcardtitle;
    TextView simcardview;

    //Check server
    String http_url = "103.21.34.216";
    Boolean FPServer;
    Boolean FaceServer;
    Boolean StaticServer;

    //FP
        String FPID;


    String checkuser;
    String adduser;

    //AttachDoc

    int No;
    String ba1;
    ImageView iv;

    public static final String sURL = "http://103.21.34.216:8081/soap/IIBiometric";

    //private final String NAMESPACE = "urn:uBiometricIntf-IIBiometric";
    private final String SOAP_ACTION_Add = "urn:uBiometricIntf-IIBiometric#Add";
    private final String METHOD_NAME_Add = "Add";


    public static final String NAMESPACE = "urn:uBiometricIntf-IIBiometric";
    //AddUserWSQ
    public static final String SOAP_ACTION_AddUserWSQ = "urn:uBiometricIntf-IIBiometric#AddUserWSQ";
    public static final String METHOD_NAME_AddUserWSQ = "AddUserWSQ";


    //DeleteUserWSQ
    public static final String SOAP_ACTION_DeleteByUserNo= "urn:uBiometricIntf-IIBiometric#DeleteByUserNo";
    public static final String METHOD_NAME_DeleteByUserNo = "DeleteByUserNo";

    public static final String path = "/storage/emulated/0/Image.wsq";
    public static final String mrzpath = "/storage/emulated/0/mrz.txt";
    public static final String fppath = "/storage/emulated/0/fp.txt";

    //MYKAD
    static byte[] byteAPDU=null;
    static byte[] respAPDU=null;
    static byte[] byteAPDU2=null;
    static byte[] respAPDU2=null;

    static byte[] byteJPNDF2=null;
    static byte[] respJPNDF2=null;
    static byte[] byteJPNDF2_1=null;
    static byte[] respJPNDF2_1=null;

    static byte[] byteJPNDF3=null;
    static byte[] respJPNDF3=null;
    static byte[] byteJPNDF3_1=null;
    static byte[] respJPNDF3_1=null;

    static byte[] byteJPNDF4=null;
    static byte[] respJPNDF4=null;
    static byte[] byteJPNDF4_1=null;
    static byte[] respJPNDF4_1=null;

    private int iSlotNum = 0;
    String activeProtocolString = "Transmission Protocol ";
    String offset, offset2;
    byte v1, v2;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private static final String[] powerActionStrings = { "Power Down",
            "Cold Reset", "Warm Reset" };

    private static final String[] stateStrings = { "Unknown", "Absent",
            "Present", "Swallowed", "Powered", "Negotiable", "Specific" };

    private static final String[] featureStrings = { "FEATURE_UNKNOWN",
            "FEATURE_VERIFY_PIN_START", "FEATURE_VERIFY_PIN_FINISH",
            "FEATURE_MODIFY_PIN_START", "FEATURE_MODIFY_PIN_FINISH",
            "FEATURE_GET_KEY_PRESSED", "FEATURE_VERIFY_PIN_DIRECT",
            "FEATURE_MODIFY_PIN_DIRECT", "FEATURE_MCT_READER_DIRECT",
            "FEATURE_MCT_UNIVERSAL", "FEATURE_IFD_PIN_PROPERTIES",
            "FEATURE_ABORT", "FEATURE_SET_SPE_MESSAGE",
            "FEATURE_VERIFY_PIN_DIRECT_APP_ID",
            "FEATURE_MODIFY_PIN_DIRECT_APP_ID", "FEATURE_WRITE_DISPLAY",
            "FEATURE_GET_KEY", "FEATURE_IFD_DISPLAY_PROPERTIES",
            "FEATURE_GET_TLV_PROPERTIES", "FEATURE_CCID_ESC_COMMAND" };

    private static final String[] propertyStrings = { "Unknown", "wLcdLayout",
            "bEntryValidationCondition", "bTimeOut2", "wLcdMaxCharacters",
            "wLcdMaxLines", "bMinPINSize", "bMaxPINSize", "sFirmwareID",
            "bPPDUSupport", "dwMaxAPDUDataSize", "wIdVendor", "wIdProduct" };

    private static final int DIALOG_VERIFY_PIN_ID = 0;
    private static final int DIALOG_MODIFY_PIN_ID = 1;
    private static final int DIALOG_READ_KEY_ID = 2;
    private static final int DIALOG_DISPLAY_LCD_MESSAGE_ID = 3;
    static final int READ_BLOCK_SIZE = 100;
    private UsbManager mManager;
    private Reader mReader;
    private PendingIntent mPermissionIntent;

    private Features mFeatures = new Features();
    String dev; // device name
    String Mykad;
    String MykadDF2 = "";
    String MykadDF3 = "";
    String MykadDF4;

    String type;
    String supp;

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private class PowerResult {
        public byte[] atr;
        public Exception e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitpreview);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Register = (Button)findViewById(R.id.btnSend);
        persotitle = (TextView) findViewById(R.id.persotitle);
        persoview = (TextView) findViewById(R.id.persoview);
        images = new ArrayList();
        image = new MyImage();
        // Create the adapter to convert the array to views
        imageAdapter = new ImageAdapter(this, images);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.AttDoc);
        listView.setAdapter(imageAdapter);
        simcardtitle=(TextView) findViewById(R.id.simcardtitle);
        simcardview = (TextView) findViewById(R.id.simcardview);




        try
        {
            count = getIntent().getExtras().getString("count");
            type = getIntent().getExtras().getString("type");
            supp = getIntent().getExtras().getString("supp");
        }
        catch (Exception ex) {

        }


        getSuppImage();
        checkServer();
        getActualImage();
        ViewFile();
        ViewFile2();
    }


    @Override
    public void onBackPressed() {
        /*if (0 == getSupportFragmentManager().getBackStackEntryCount()) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }*/
        Intent i = new Intent(this, Register.class);
        i.putExtra("type", type);
        i.putExtra("supp",supp);
        i.putExtra("count",count);
        startActivity(i);
        finish();

    }

    public void checkServer()
    {
            FPServer = isPortOpen(http_url, 8081, 10000);
            FaceServer = isPortOpen(http_url, 83, 10000);
            StaticServer = isPortOpen(http_url, 81, 10000);

            if(StaticServer.toString() == "false")
            {
                Toast.makeText(SubmitPreview.this, "Server Down", Toast.LENGTH_LONG).show();
            }
            if(FPServer.toString() == "false")
            {
                Toast.makeText(SubmitPreview.this, "Fingerprint Server Down", Toast.LENGTH_LONG).show();
            }
            if(FaceServer.toString() == "false")
            {
                Toast.makeText(SubmitPreview.this, "Face Server Down", Toast.LENGTH_LONG).show();
            }
    }


    public void getActualImage()
    {
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

        if(imgFile.exists()){
            Bitmap mphoto = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
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

            actualimage = Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, false);

            Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()), 360, 216, true);
            ImageView Dis_Photo = (ImageView) findViewById(R.id.Dis_Photo);
            Dis_Photo.setImageBitmap(bm);

        }
    }

    public void ViewFile()
    {
        try {
            File file = new File(page1);
            if (file.exists()) {

                String[] separated = (FileHelper.ReadFile(SubmitPreview.this)).split("\\;");

                Name = separated[1];
                Gender=separated[2];
                DOB=separated[3];
                DocNo= separated[4];
                DocTy= separated[5];
                Nationality= separated[6];
                Address= separated[7];
                Address2= separated[8];
                Address3= separated[9];
                City= separated[10];
                Postcode=separated[11];
                State=separated[12];

                persotitle.setText("Name:\n" +
                        "Gender:\n" +
                        "Date Of Birth:\n" +
                        "Document No:\n" +
                        "Document Type:\n" +
                        "Nationality:\n" +
                        "Address Line 1:\n" +
                        "Address Line 2:\n" +
                        "Address Line 3:\n" +
                        "City:\n" +
                        "Postcode:\n" +
                        "State:\n");

                persoview.setText (Name +"\n"+
                        Gender+"\n"+
                DOB+"\n"+
                DocNo+"\n"+
                DocTy+"\n"+
                Nationality+"\n"+
                Address+"\n"+
                Address2+"\n"+
                Address3+"\n"+
                City+"\n"+
                Postcode+"\n"+
                State);



               // persoview.setText ((FileHelper.ReadFile(SubmitPreview.this)).replaceAll("\\;",System.getProperty("line.separator")));
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void ViewFile2()
    {
        try {
            File file = new File(page1);
            if (file.exists()) {

                String[] separated = (FileHelper.ReadFile2(SubmitPreview.this)).split("\\;");

               /* Operator= separated[0];
                PhoneNo=separated[1];
                SerialNo=separated[2];
                PUK1= separated[3];
                PUK2= separated[4];*/


                if (count.contains("0")) {

                    Operator= separated[0];
                    PhoneNo=separated[1];
                    SerialNo=separated[2];
                    PUK1= separated[3];
                    PUK2= separated[4];

                    simcardtitle.setText("Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n");

                    simcardview.setText(Operator +"\n"+PhoneNo +"\n"+SerialNo+"\n"+PUK1+"\n"+PUK2);

                }

                if (count.contains("1")) {
                    Operator= separated[0];
                    PhoneNo=separated[1];
                    SerialNo=separated[2];
                    PUK1= separated[3];
                    PUK2= separated[4];

                    Operatorsim2= separated[6];
                    PhoneNosim2=separated[7];
                    SerialNosim2=separated[8];
                    PUK1sim2= separated[9];
                    PUK2sim2= separated[10];


                    simcardview.setText(Operator +"\n"+PhoneNo +"\n"+SerialNo+"\n"+PUK1+"\n"+PUK2
                                        +"\n\n"+
                                        Operatorsim2 +"\n"+PhoneNosim2  +"\n"+SerialNosim2 +"\n"+PUK1sim2 +"\n"+PUK2sim2
                    );

                    simcardtitle.setText("Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n");
                }
                if (count.contains("2")) {
                    Operator= separated[0];
                    PhoneNo=separated[1];
                    SerialNo=separated[2];
                    PUK1= separated[3];
                    PUK2= separated[4];

                    Operatorsim2= separated[6];
                    PhoneNosim2=separated[7];
                    SerialNosim2=separated[8];
                    PUK1sim2= separated[9];
                    PUK2sim2= separated[10];

                    Operatorsim3= separated[12];
                    PhoneNosim3=separated[13];
                    SerialNosim3=separated[14];
                    PUK1sim3= separated[15];
                    PUK2sim3= separated[16];

                    simcardview.setText(Operator +"\n"+PhoneNo +"\n"+SerialNo+"\n"+PUK1+"\n"+PUK2
                            +"\n\n"+
                            Operatorsim2 +"\n"+PhoneNosim2  +"\n"+SerialNosim2 +"\n"+PUK1sim2 +"\n"+PUK2sim2
                            +"\n\n"+
                            Operatorsim3 +"\n"+PhoneNosim3  +"\n"+SerialNosim3 +"\n"+PUK1sim3 +"\n"+PUK2sim3
                    );


                    simcardtitle.setText("Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n");
                }
                if (count.contains("3")) {
                    Operator= separated[0];
                    PhoneNo=separated[1];
                    SerialNo=separated[2];
                    PUK1= separated[3];
                    PUK2= separated[4];

                    Operatorsim2= separated[6];
                    PhoneNosim2=separated[7];
                    SerialNosim2=separated[8];
                    PUK1sim2= separated[9];
                    PUK2sim2= separated[10];

                    Operatorsim3= separated[12];
                    PhoneNosim3=separated[13];
                    SerialNosim3=separated[14];
                    PUK1sim3= separated[15];
                    PUK2sim3= separated[16];

                    Operatorsim4= separated[18];
                    PhoneNosim4=separated[19];
                    SerialNosim4=separated[20];
                    PUK1sim4= separated[21];
                    PUK2sim4= separated[22];

                    simcardview.setText(Operator +"\n"+PhoneNo +"\n"+SerialNo+"\n"+PUK1+"\n"+PUK2
                            +"\n\n"+
                            Operatorsim2 +"\n"+PhoneNosim2  +"\n"+SerialNosim2 +"\n"+PUK1sim2 +"\n"+PUK2sim2
                            +"\n\n"+
                            Operatorsim3 +"\n"+PhoneNosim3  +"\n"+SerialNosim3 +"\n"+PUK1sim3 +"\n"+PUK2sim3
                            +"\n\n"+
                            Operatorsim4 +"\n"+PhoneNosim4  +"\n"+SerialNosim4 +"\n"+PUK1sim4 +"\n"+PUK2sim4
                    );


                    simcardtitle.setText("Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n");
                }

                if (count.contains("4")) {

                    Operator= separated[0];
                    PhoneNo=separated[1];
                    SerialNo=separated[2];
                    PUK1= separated[3];
                    PUK2= separated[4];

                    Operatorsim2= separated[6];
                    PhoneNosim2=separated[7];
                    SerialNosim2=separated[8];
                    PUK1sim2= separated[9];
                    PUK2sim2= separated[10];

                    Operatorsim3= separated[12];
                    PhoneNosim3=separated[13];
                    SerialNosim3=separated[14];
                    PUK1sim3= separated[15];
                    PUK2sim3= separated[16];

                    Operatorsim4= separated[18];
                    PhoneNosim4=separated[19];
                    SerialNosim4=separated[20];
                    PUK1sim4= separated[21];
                    PUK2sim4= separated[22];

                    Operatorsim5= separated[24];
                    PhoneNosim5=separated[25];
                    SerialNosim5=separated[26];
                    PUK1sim5= separated[27];
                    PUK2sim5= separated[28];

                    simcardview.setText(Operator +"\n"+PhoneNo +"\n"+SerialNo+"\n"+PUK1+"\n"+PUK2
                            +"\n\n"+
                            Operatorsim2 +"\n"+PhoneNosim2  +"\n"+SerialNosim2 +"\n"+PUK1sim2 +"\n"+PUK2sim2
                            +"\n\n"+
                            Operatorsim3 +"\n"+PhoneNosim3  +"\n"+SerialNosim3 +"\n"+PUK1sim3 +"\n"+PUK2sim3
                            +"\n\n"+
                            Operatorsim4 +"\n"+PhoneNosim4  +"\n"+SerialNosim4 +"\n"+PUK1sim4 +"\n"+PUK2sim4
                            +"\n\n"+
                            Operatorsim5 +"\n"+PhoneNosim5  +"\n"+SerialNosim5 +"\n"+PUK1sim5 +"\n"+PUK2sim5
                    );

                    simcardtitle.setText("Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n"
                            +
                            "Telco:\n" +
                            "Phone Number:\n" +
                            "Serial No:\n" +
                            "PUK1:\n" +
                            "PUK2:\n\n");
                }



                //simcardview.setText ((FileHelper.ReadFile2(SubmitPreview.this)).replaceAll("\\;",System.getProperty("line.separator")));

            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }








    public void getSuppImage()
    {
        try {
            LinearLayout layout = (LinearLayout)findViewById(R.id.linearDoc);
            String[] separated = (FileHelper.ReadFileSupp(SubmitPreview.this)).split("\\;");
            // Toast.makeText(getApplicationContext(), "separated.length =" + separated.length, Toast.LENGTH_LONG).show();

            for (int i = 0; i <= separated.length; i++) {
                //images
                // /(separated[i]);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(360,216);
                layoutParams.gravity= Gravity.CENTER;
                layoutParams.setMargins(0,20,0,20);
                iv = new ImageView(this);
                iv.setLayoutParams(layoutParams);
                iv.setMaxHeight(216);
                iv.setMaxWidth(360);

                iv.setId(i + 28000);

                Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeFile((separated[i])), 360, 216, true);

                iv.setImageBitmap(bm);

                //  Bitmap bmImg = BitmapFactory.decodeFile((separated[i]));
                //iv.setImageBitmap(bmImg);

                image = new MyImage();
                image.setDatetime(System.currentTimeMillis());
                image.setPath((separated[i]));
                images.add(image);
                //listView.requestLayout();

                layout.addView(iv);


            }
        }
        catch (Exception ex)
        {
            // Toast.makeText(getApplicationContext(), "Error" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }








    @Override
    public void update(Observable observable, Object data) {
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
         if(connectstate.contains("false"))
         {
             //Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
             connection = "No Connection";
         }
         else
         {
             //Toast.makeText(getApplicationContext(), "Good Connection", Toast.LENGTH_LONG).show();
             connection = "Good Connection";
         }
     }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Checking Server Connection---------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/


    public static boolean isPortOpen(final String ip, final int port, final int timeout) {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            return true;
        }

        catch(ConnectException ce){
            ce.printStackTrace();
            return false;
        }

        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void btn_FP(View view)
    {

        Intent intent = new Intent();
        intent.setClassName("com.morpho.morphosample", "com.morpho.morphosample.ConnectionActivity");
        startActivityForResult(intent, 20);

    }

    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------Send Fingerprint To Server---------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------------------------------------------------------------------*/

    public void SendFP()
    {
        if(connectstate.contains("false"))
        {
            Toast.makeText(getApplicationContext(), "No Internet Connect", Toast.LENGTH_LONG).show();
        }
        else
        {
            Register.setEnabled(false);
            AddUserAsyncTask myAsync = new AddUserAsyncTask();
            myAsync.execute();
        }
    }

    public class AddUserAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(SubmitPreview.this, "Waiting", "Sending", true);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            InputStream is=null;
            byte[] array= new byte[1000000];
            //byte[] array= null;

            try{
                is = new FileInputStream(path);
                if (path != null) {
                    try {
                        array=streamToBytes(is);
                    } finally {
                        is.close();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                try {
                    throw new IOException("Unable to open file (cho.wsq)");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            //*/
            try{
                //Create request
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_AddUserWSQ);
                //request.addProperty("firstNo",100);
                //request.addProperty("Second",300);

                PropertyInfo prop = new PropertyInfo();
                prop.setName("UserID");//Server Parameter name 1
                prop.setValue(FPID); //Param 1 Value
                //prop.setType(String.class);
                prop.setType(String.class);
                request.addProperty(prop);

                prop = new PropertyInfo();
                prop.setName("myValue");//Server Parameter name 2
                prop.setValue(array);//Param 2 Value
                prop.setType(byte[].class);
                request.addProperty(prop);

                SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
                new MarshalBase64().register(sse);
                sse.dotNet=true;
                sse.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(sURL);
                htse.call(SOAP_ACTION_AddUserWSQ, sse);
                //Object response = (Object) sse.getResponse();
                //SoapObject response = (SoapObject) sse.getResponse();
                Vector<SoapObject> result = (Vector<SoapObject>) sse.getResponse();

                //Inside your for loop
                SoapObject so = result.get(0);

                Log.d(TAG, so.toString());
                Log.d(TAG, String.valueOf(so.getPropertyCount()));
                Log.d(TAG, so.getProperty(0).toString());
                Log.d(TAG, so.getProperty(1).toString());
                adduser = so.getProperty(1).toString();

                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Log.d(TAG, e.toString());
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(adduser.equals("1"))
            {
                //Toast.makeText(getApplicationContext(), "Successfully Insert", Toast.LENGTH_LONG).show();
                SearchingTask task = new SearchingTask();
                task.execute();
            }
            else if(adduser.equals("-9001") )
            {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid Fingerprint format", Toast.LENGTH_LONG).show();
                Register.setEnabled(true);
            }
            else if(adduser.equals("-9002"))
            {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Registration Failed: Duplicate ID", Toast.LENGTH_LONG).show();
                Register.setEnabled(true);
            }
            else if(adduser.equals("-9003"))
            {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Registration Failed: Duplicate Fingerprint", Toast.LENGTH_LONG).show();
                Register.setEnabled(true);
            }

            //TextView myTv = (TextView) findViewById(R.id.textView2);
            //myTv.setText("giRet: "+Integer.toString(giRet));
            super.onPostExecute(result);
        }
    }

    public byte[] streamToBytes (InputStream is){
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

    public void OnRegister(View v)
    {
       /* registerUser();
        if (count.contains("0"))
        {

        }
        else
        {
           Multiplesimcard();
        }*/

        ImageView Dis_Photo = (ImageView) findViewById(R.id.Dis_Photo);
         isConnectedToInternet();
        checkServer();
        if(Dis_Photo.getDrawable() == null)
        {
            Toast.makeText(SubmitPreview.this, "Image not insert", Toast.LENGTH_LONG).show();
        }
        else
        {
            //test
  if(FPServer.toString() == "true" && StaticServer.toString() == "true" && FaceServer.toString() == "true") {
                FPID = DocNo;
                   SendFP();
           // SearchingTask task = new SearchingTask();
             //task.execute();

                }
                else
                {
                    Toast.makeText(SubmitPreview.this, "Check Server", Toast.LENGTH_LONG).show();
                }
        }
    }

    public void Multiplesimcard()
    {
        int i;

        for (i=0;i<=Integer.parseInt(count);i++)
        {
            if (i==0)
            {
               // Toast.makeText(SubmitPreview.this, "1", Toast.LENGTH_LONG).show();
                SimInfo();
            }
            if (i==1)
            {
                SimInfo2();
            }
            //
            if (i==2)
            {
                SimInfo3();
            }
            if (i==3)
            {
                SimInfo4();
            }
            if (i==4)
            {
                SimInfo5();
            }
        }
    }



    public class SearchingTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //progress = ProgressDialog.show(Register.this, "Waiting", "Sending", true);
        }

        @Override
        protected String doInBackground(String... params) {return addperson();}

        @Override
        protected void onPostExecute(String result) {


            //progress.dismiss();
            if (result != null) {
                if (result.isEmpty())
                {
                    //Toast.makeText(MainActivity.this, "Successfully Register", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    Toast.makeText(SubmitPreview.this, "Face Image not clear : Registration failed", Toast.LENGTH_LONG).show();
                    DeleteFP();
                    Register.setEnabled(true);
                }
                else
                {
                    //Toast.makeText(Register.this, "Successfully Register", Toast.LENGTH_LONG).show();
                    Register.setClickable(false);
                }
            }

            else
            {
                if(connectstate.contains("false"))
                {
                    progress.dismiss();
                    Toast.makeText(SubmitPreview.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                }
                else {
                    progress.dismiss();
                    Toast.makeText(SubmitPreview.this, "Face Image not clear : Registration failed", Toast.LENGTH_LONG).show();
                    DeleteFP();
                    Register.setEnabled(true);
                }
            }

        }
    }


    public static byte[] toByteArray(String data) {
        try {
            int dataLen = data.length();
            if (dataLen == 0) {
                return null;
            }
            byte[] result = new byte[(dataLen / 2)];
            for (int idx = 0; idx < dataLen; idx = (idx + 1) + 1) {
                result[idx / 2] = (byte) Integer.parseInt(data.substring(idx, idx + 2), 16);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    public void registerUser()
    {

        if (count.contains("0"))
        {
            //do nothing
        }
        else
        {
            bypersonalinfo = bypersonalinfo2;
            Multiplesimcard();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, bypersonalinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            onAttachDocSend();

                            Intent intent = new Intent(SubmitPreview.this, Payment.class);
                            intent.putExtra("Print", "Shop User ID : Admin \n   Customer ID: " + DocNo + "\n   Telco: " + Operator + "\n   Phone No: " + PhoneNo + ";;;Date: " + formattedDate);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finishAffinity();
                            finish();

                        }
                        if(response.toString().equals("1062"))
                        {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Duplicate ID", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            DeleteFP();
                            Toast.makeText(getApplicationContext(), "Timeout error, please try again.", Toast.LENGTH_LONG).show();

                            // error.printStackTrace();
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
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("Camvi", repl);
              //  params.put("Camvi", "123");
                params.put("Name",Name);
                params.put("Father_Name","");
                params.put("Mother_Name","");
                params.put("Grandfather_Name","");
                params.put("Spouse_Name","");
                params.put("Gender",Gender);
                params.put("Birth_Date",DOB);
                params.put("Doc_No",DocNo);
                params.put("Doc_Ty",DocTy);
                params.put("Nationality",Nationality);
                params.put("Occupation","");
               // params.put("PhoneNo","");
                params.put("Email","");
                params.put("Per_Addr1",Address);
                params.put("Per_Addr2",Address2);
                params.put("Per_Addr3",Address3);
                params.put("Per_Town",City);
                params.put("Per_POBox",Postcode);
                params.put("Per_State",State);



                params.put("operator",Operator);
                params.put("phoneNo",PhoneNo);
                params.put("serialno", SerialNo);
                params.put("puk1",PUK1);
                params.put("puk2",PUK2);

                return params;
            }

        };

        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    String formattedDate = df.format(c.getTime());



    public void SimInfo()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bysiminfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();


                        }
                        if(response.toString().equals("1062"))
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Number", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {

                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            //SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", DocTy);
                params.put("docno", DocNo);

                params.put("operator",Operator);
                params.put("phoneNo",PhoneNo);
                params.put("serialno", SerialNo);
                params.put("puk1",PUK1);
                params.put("puk2", PUK2);

                return params;
            }

        };

       // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public void SimInfo2()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bysiminfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();


                        }
                        if(response.toString().equals("1062"))
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Number", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {

                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            //SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", DocTy);
                params.put("docno", DocNo);

                params.put("operator",Operatorsim2);
                params.put("phoneNo",PhoneNosim2);
                params.put("serialno", SerialNosim2);
                params.put("puk1",PUK1sim2);
                params.put("puk2", PUK2sim2);

                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public void SimInfo3()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bysiminfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();


                        }
                        if(response.toString().equals("1062"))
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Number", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {

                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            //SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", DocTy);
                params.put("docno", DocNo);

                params.put("operator",Operatorsim3);
                params.put("phoneNo",PhoneNosim3);
                params.put("serialno", SerialNosim3);
                params.put("puk1",PUK1sim3);
                params.put("puk2", PUK2sim3);

                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public void SimInfo4()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bysiminfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();


                        }
                        if(response.toString().equals("1062"))
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Number", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {

                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            //SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", DocTy);
                params.put("docno", DocNo);

                params.put("operator",Operatorsim4);
                params.put("phoneNo",PhoneNosim4);
                params.put("serialno", SerialNosim4);
                params.put("puk1",PUK1sim4);
                params.put("puk2", PUK2sim4);

                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }

    public void SimInfo5()
    {
        //final String username = name.getText().toString();
        //final String email = Email.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bysiminfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success")) {

                            //progress.dismiss();
                            Toast.makeText(SubmitPreview.this, "Successfully Registered", Toast.LENGTH_LONG).show();


                        }
                        if(response.toString().equals("1062"))
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Number", Toast.LENGTH_LONG).show();

                        }
                        if(response.toString().equals("1292"))
                        {

                            Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof TimeoutError)
                        {
                            //Log.d(TAG, error.getMessage());
                            //SimInfo();
                        }
                        if (error instanceof NetworkError) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "Cannot connect to Internet...Please check your connection!");
                        }
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String
                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", DocTy);
                params.put("docno", DocNo);

                params.put("operator",Operatorsim5);
                params.put("phoneNo",PhoneNosim5);
                params.put("serialno", SerialNosim5);
                params.put("puk1",PUK1sim5);
                params.put("puk2", PUK2sim5);

                return params;
            }

        };

        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //bio.disconnect();
        //System.out.println("Sim" + stringRequest);
    }


    protected String addperson() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app-id", "test");
        params.put("app-key", "test");
        params.put("person-name", Name);
        params.put("group-ids", "1");
        params.put("image-data", toBase64Image(actualimage));
        try {
            byte[] resp = doPost("/person/create", params);
            System.out.println("Responses :" + new String(resp, "UTF-8"));
            PersonID = new String(resp,"UTF-8");
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(new String(resp,"UTF-8"));
            if (m.find(1)) {
                repl = m.group();

            }

            registerUser();

            return repl;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    protected String toBase64Image(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] byteArray = bos.toByteArray();
        return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    protected String Refresh() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app-id", "test");
        params.put("app-key", "test");
        params.put("person-id", repl);

        try {
            byte[] resp = doPost("/person/refresh", params);
            System.out.println("Responses :" + new String(resp, "UTF-8"));
            return null;
        } catch (UnsupportedEncodingException e) {
            progress.dismiss();
            Log.e(TAG, e.getMessage());
            return null;
        } catch (Exception e) {
            progress.dismiss();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }



    static public String getServerUrlBase() {
       // return "http://" + ServerAddress + "/arges-service/api";
        return "http://" + ServerAddress + "/service/api";
    }

    public static class ServerException extends Throwable {
        private static final long serialVersionUID = 4017805196493169996L;

        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
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
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
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
                        ServerException exp = objectMapper.readValue(data, ServerException.class);
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



    //watermark for image scanned documents
    public static Bitmap mark(Bitmap src, String watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(18);
        paint.setAntiAlias(true);
        paint.setUnderlineText(true);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        canvas.drawText(watermark, 40, 60, paint);

        return result;
    }




    public void DeleteFP() {
        isConnectedToInternet();
        if (connectstate.contains("false")) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Deleting FP", Toast.LENGTH_LONG).show();


            DeleteByUserNoAsyncTask myAsync = new DeleteByUserNoAsyncTask();
            myAsync.execute();
        }
    }


    public class DeleteByUserNoAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            InputStream is = null;
            byte[] array = new byte[1000000];

            //*/
            try {
                //Create request
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DeleteByUserNo);

                PropertyInfo prop = new PropertyInfo();

                prop.setName("UserID");
                prop.setValue(FPID);
                prop.setType(String.class);
                request.addProperty(prop);

                SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                new MarshalBase64().register(sse);
                sse.dotNet = true;
                sse.setOutputSoapObject(request);
                HttpTransportSE htse = new HttpTransportSE(sURL);
                htse.call(SOAP_ACTION_DeleteByUserNo, sse);
                Vector<SoapObject> result = (Vector<SoapObject>) sse.getResponse();

                //Inside your for loop
                SoapObject so = result.get(0);

                Log.d(TAG, so.toString());
                Log.d(TAG, String.valueOf(so.getPropertyCount()));
                Log.d(TAG, so.getProperty(0).toString().replace(" ", ""));
                Log.d(TAG, so.getProperty(1).toString());

                id = so.getProperty(0).toString().replace(" ", "");
                System.out.println("ID : " + id);
                Toast.makeText(getApplicationContext(), "Deleted id :" + id, Toast.LENGTH_LONG).show();


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
            progress.dismiss();

            super.onPostExecute(result);
        }


    }


    public void onAttachDocSend()
    {
        //  progress = ProgressDialog.show(Display.this, "Waiting", "Sending", true);
        try {
            No = 0;
            String[] separated = (FileHelper.ReadFileSupp(SubmitPreview.this)).split("\\;");

            for (int i = 0; i <= separated.length; i++) {
                //Toast.makeText(getApplicationContext(), "Error" +separated[i], Toast.LENGTH_LONG).show();
                upload(separated[i]);
                No++;
            }
        }
        catch (Exception ex)
        {
             //Toast.makeText(getApplicationContext(), "Error" + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void upload(String path) {
        System.out.println("Path : " + path);
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

        // Upload image to server
        sendAttachDoc();
    }

    public void sendAttachDoc() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SuppdocURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("Image uploaded")) {
                            Toast.makeText(getApplicationContext(), "Successfully uploaded "+No+" attach document", Toast.LENGTH_LONG).show();
                        }
                        //progress.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            System.out.println("Error : " + error.getMessage());
                            // Log.d(TAG, error.getMessage());
                        }
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("base64", ba1);
                params.put("dir", DocNo);
                params.put("ImageName", DocNo+"/" +DocNo+"_"+ No +".jpg");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}



