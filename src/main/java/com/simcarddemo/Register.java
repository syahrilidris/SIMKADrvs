package com.simcarddemo;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements Observer {

    public static final String page1 = "/storage/emulated/0/page1.txt";
    public static final String page2 = "/storage/emulated/0/page2.txt";

    final static String suppdoc = "/storage/emulated/0/suppdoc.txt";

    public static String SuppdocURL = "http://103.21.34.216:81/simcardsupp/suppdoc.php";
    protected final String TAG = getClass().getSimpleName();
    String bypersonalinfo = "http://103.21.34.216:81/simcarddemo/insert.php";
    String bysiminfo = "http://103.21.34.216:81/simcarddemo/SimInfo.php";

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
    private EditText Name;
    private EditText PassportNo;
    private EditText ID;
    private EditText DOB;
    private EditText Nationality;
    private EditText Gender;

//SimInfo
    private Spinner spinner2;
    private EditText PUK1;
    private EditText PUK2;
    private EditText PhoneNo;
    private EditText SerialNo;

    private Spinner spinner2sim2;
    private EditText PUK1sim2;
    private EditText PUK2sim2;
    private EditText PhoneNosim2;
    private EditText SerialNosim2;

    private Spinner spinner2sim3;
    private EditText PUK1sim3;
    private EditText PUK2sim3;
    private EditText PhoneNosim3;
    private EditText SerialNosim3;

    private Spinner spinner2sim4;
    private EditText PUK1sim4;
    private EditText PUK2sim4;
    private EditText PhoneNosim4;
    private EditText SerialNosim4;

    private Spinner spinner2sim5;
    private EditText PUK1sim5;
    private EditText PUK2sim5;
    private EditText PhoneNosim5;
    private EditText SerialNosim5;

    private EditText Address;
    private EditText Address2;
    private EditText Address3;
    private EditText Postcode;
    private EditText City;
    private EditText State;
    private Button Register;
    private Button Movers;
    private Button Save;
    private Bitmap mphoto;
    private Bitmap mphoto2;
    private Spinner spinner; //citizen
    private Spinner spinner3; //non-citizen

    private boolean imageLoaded = false;
    String AccountNo ;
    String DocNo;
    String ActivDt;
    private String PersonID;
    String repl;
    private ProgressDialog progress;
    private TextView ShowID;
    public String camera;
    String picLive;

    String myString;
    private String connectstate;
    String connection;
    String id;

    String page1_Value;
    String page2_Value;


    //DOB
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    //Detail mykad
    String name;
    String name1;
    String ICName;
    String ICID;
    String ICGender;
    String ICDOB;
    String ICAdd;
    String ICAdd1;
    String ICAdd2;
    String ICPcode;
    String ICCity;
    String ICState;
    String FPresult;
    String ICimage;
    ImageView ICCard;
    int count = 0;
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

    String p2supp;

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
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {

                synchronized (this) {

                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {

                        if (device != null) {

                            // Open reader
                            //System.out.println("Opening reader: " + device.getDeviceName() + "...");
                            try {
                                //new OpenTask().execute(device);
                                mReader.open(device);
                               // Toast.makeText(getApplicationContext(), "Ready to Read MyKad", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception ex)
                            {
                                Toast.makeText(getApplicationContext(), "No Reader Detected", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No Reader Detected", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        System.out.println("Permission denied for device " + device.getDeviceName());
                        Toast.makeText(getApplicationContext(), "Reader Denied", Toast.LENGTH_LONG).show();
                    }
                }

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                synchronized (this) {

                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (device != null && device.equals(mReader.getDevice())) {

                        // Close reader
                        // logMsg("Closing reader...");
                        mReader.close();
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //unregisterReceiver(mReceiver);
        //mReader.close();
    }

    private class PowerParams {
        public int slotNum;
        public int action;
    }

    private class PowerResult {
        public byte[] atr;
        public Exception e;
    }

    private class PowerTask extends AsyncTask<PowerParams, Void, PowerResult> {

        @Override
        protected PowerResult doInBackground(PowerParams... params) {

            PowerResult result = new PowerResult();

            try {

                result.atr = mReader.power(params[0].slotNum, params[0].action);

            } catch (Exception e) {

                result.e = e;
            }

            return result;
        }

        @Override
        protected void onPostExecute(PowerResult result) {

            System.out.println("Result : " + result.e);

            if (result.e != null) {
                String test = result.e.toString();
                if(test.contains("RemovedCardException"))
                {
                    Toast.makeText(getApplicationContext(), "Card Not Inserted", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                if(test.contains("UnresponsiveCardException"))
                {
                    Toast.makeText(getApplicationContext(), "Please Insert Card Correctly", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                if(test.contains("reader is not opened"))
                {
                   // Toast.makeText(getApplicationContext(), "Reader Not Detected", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                }
                //System.out.println(result.e.toString());
            }
            else {

                // Show ATR
                if (result.atr != null) {

                    System.out.println("ATR:");
                    //logBuffer(result.atr, result.atr.length);
                    Toast.makeText(getApplicationContext(), "Ready to Read MyKad", Toast.LENGTH_LONG).show();
                    //VerifyMykad();
                    Protocol();

                } else {
                    System.out.println("ATR: None");
                }
            }
        }
    }

    private class SetProtocolResult {

        public int activeProtocol;
        public Exception e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        checkServer();


        try {
            //new File(path  ).mkdir();
            File file = new File(mrzpath);
            if (file.exists()) {
                file.delete();
            }
        }

        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }

        try {
           // new File(path  ).mkdir();
            File file2 = new File(fppath);
            if (file2.exists()) {
                file2.delete();
            }
        }

        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }

        Register = (Button)findViewById(R.id.btnSend);
        Name = (EditText)findViewById(R.id.Name);
        ID = (EditText)findViewById(R.id.ID);
        DOB = (EditText)findViewById(R.id.DOB);
        DOB.setInputType(InputType.TYPE_NULL);
        Nationality = (EditText)findViewById(R.id.Nationality);
        Gender = (EditText)findViewById(R.id.Gender);
        spinner = (Spinner)findViewById(R.id.spinner);
        PUK1 = (EditText)findViewById(R.id.PUK1);
        PUK2 = (EditText)findViewById(R.id.PUK2);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        PhoneNo = (EditText)findViewById(R.id.PhoneNo);
        SerialNo = (EditText)findViewById(R.id.SerialNo);
        PUK1sim2 = (EditText)findViewById(R.id.PUK1sim2);
        PUK2sim2 = (EditText)findViewById(R.id.PUK2sim2);
        spinner2sim2 = (Spinner)findViewById(R.id.spinner2sim2);
        PhoneNosim2 = (EditText)findViewById(R.id.PhoneNosim2);
        SerialNosim2 = (EditText)findViewById(R.id.SerialNosim2);
        PUK1sim3 = (EditText)findViewById(R.id.PUK1sim3);
        PUK2sim3 = (EditText)findViewById(R.id.PUK2sim3);
        spinner2sim3 = (Spinner)findViewById(R.id.spinner2sim3);
        PhoneNosim3 = (EditText)findViewById(R.id.PhoneNosim3);
        SerialNosim3 = (EditText)findViewById(R.id.SerialNosim3);
        PUK1sim4 = (EditText)findViewById(R.id.PUK1sim4);
        PUK2sim4 = (EditText)findViewById(R.id.PUK2sim4);
        spinner2sim4 = (Spinner)findViewById(R.id.spinner2sim4);
        PhoneNosim4 = (EditText)findViewById(R.id.PhoneNosim4);
        SerialNosim4 = (EditText)findViewById(R.id.SerialNosim4);

        PUK1sim5 = (EditText)findViewById(R.id.PUK1sim5);
        PUK2sim5 = (EditText)findViewById(R.id.PUK2sim5);
        spinner2sim5 = (Spinner)findViewById(R.id.spinner2sim5);
        PhoneNosim5 = (EditText)findViewById(R.id.PhoneNosim5);
        SerialNosim5 = (EditText)findViewById(R.id.SerialNosim5);

        Address = (EditText)findViewById(R.id.Address1);
        Address2 = (EditText)findViewById(R.id.Address2);
        Address3 = (EditText)findViewById(R.id.Address3);
        Postcode = (EditText)findViewById(R.id.Postcode);
        City = (EditText)findViewById(R.id.City);
        State = (EditText)findViewById(R.id.State);
        LiveCapture  = (ImageView)findViewById(R.id.LiveCapture);
        AttachDoc = (ImageView) findViewById(R.id.AttachDoc);
        mykadImage = (ImageView)findViewById(R.id.myKadPhoto);
        lblMykad= (TextView)findViewById(R.id.textView38);

        ICCard = (ImageView)findViewById(R.id.imageButton);
        categorytitle = (TextView)findViewById(R.id.registertitle);

        Movers = (Button)findViewById(R.id.movers);

        try {
            categorytype = getIntent().getExtras().getString("type");
            categorytitle.setText(categorytype + (" " + categorytitle.getText()));

            count = Integer.parseInt(getIntent().getExtras().getString("count"));

            ViewFile();
            ViewFile2();

            LinearLayout layoutsim1= (LinearLayout)findViewById(R.id.layoutsim1);
            LinearLayout layoutsim2= (LinearLayout)findViewById(R.id.layoutsim2);
            LinearLayout layoutsim3= (LinearLayout)findViewById(R.id.layoutsim3);
            LinearLayout layoutsim4= (LinearLayout)findViewById(R.id.layoutsim4);
            LinearLayout layoutsim5= (LinearLayout)findViewById(R.id.layoutsim5);

            if (count ==1)
            {
                layoutsim2.setVisibility(View.VISIBLE);
            }
            if (count ==2)
            {
                layoutsim2.setVisibility(View.VISIBLE);
                layoutsim3.setVisibility(View.VISIBLE);
            }
            if (count ==3)
            {
                layoutsim2.setVisibility(View.VISIBLE);
                layoutsim3.setVisibility(View.VISIBLE);
                layoutsim4.setVisibility(View.VISIBLE);
            }
            if (count ==4)
            {
                layoutsim2.setVisibility(View.VISIBLE);
                layoutsim3.setVisibility(View.VISIBLE);
                layoutsim4.setVisibility(View.VISIBLE);
                layoutsim5.setVisibility(View.VISIBLE);
            }


            TextView supp = (TextView)findViewById(R.id.textView23);
            p2supp = getIntent().getExtras().getString("supp");
            supp.setText(p2supp);

            ImageView tick5 = (ImageView)findViewById(R.id.tick5);
            tick5.setVisibility(View.VISIBLE);

        }
        catch (Exception ex)
        {

        }

        if (categorytype.contains("CITIZEN"))
        {
            spinner3 = (Spinner)findViewById(R.id.spinner3);
            spinner3.setVisibility(View.GONE);
            LinearLayout linearmypassport = (LinearLayout) findViewById(R.id.linearPassport);
            linearmypassport.setVisibility(View.GONE);


        }

        if (categorytype.contains("FOREIGNER"))
        {
            spinner3 = (Spinner)findViewById(R.id.spinner3);
            spinner.setVisibility(View.GONE);
            spinner=spinner3;
            LinearLayout linearmykad = (LinearLayout) findViewById(R.id.linearMykad);
            linearmykad.setVisibility(View.GONE);
            Movers.setVisibility(View.GONE);
        }

        if (categorytype.contains("BUSINESS"))
        {
            spinner3 = (Spinner)findViewById(R.id.spinner3);
            spinner3.setVisibility(View.GONE);
        }

        Bundle b = getIntent().getExtras();
        if(b!=null){
            ImageView iv_photo=(ImageView)findViewById(R.id.LiveCapture);
            Bundle extras = getIntent().getExtras();
            Bitmap bmp = (Bitmap) extras.getParcelable("Bitmap");
            iv_photo.setImageBitmap(bmp);
            LinearLayout imageCapture = (LinearLayout)findViewById(R.id.imageCapture);
           // imageCapture.setVisibility(View.VISIBLE);
        }
        else {

        }


        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMddmmss");
        Date todayDate = new Date();
        AccountNo = currentDate.format(todayDate);

        SimpleDateFormat current = new SimpleDateFormat("yyyy-MM-dd");
        Date activationdate = new Date();
        ActivDt = current.format(activationdate);

        //DOB
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setDateTimeField();

        Button readBarcode = (Button) findViewById(R.id.btnBarcode);
        readBarcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //  ReadMRZ();
                ReadBarcode2();

            }
        });


        CheckFile();
    }






    public void CheckFile()
    {
        File imgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg");

        if (imgFile.exists()) {
            mphoto2 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            LiveCapture.setImageBitmap(mphoto2);
            LinearLayout imageCapture = (LinearLayout) findViewById(R.id.imageCapture);
            imageCapture.setVisibility(View.VISIBLE);

            ImageView imageCapture2 = (ImageView) findViewById(R.id.LiveCapture);
            imageCapture2.setVisibility(View.VISIBLE);
            ImageView tick1 = (ImageView) findViewById(R.id.tick1);
            tick1.setVisibility(View.VISIBLE);

            picLive = "capture";
        }

        try {

            File file = new File(page1);
            if (file.exists()) {
                file.delete();
            }

        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void CheckFile2()
    {
        try {

            File file = new File(page1);
            if (file.exists()) {
                file.delete();
            }

            File file2 = new File(page2);
            if (file2.exists()) {
                file2.delete();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }


    public void ViewFile()
    {

        try {
            File file = new File(page1);
            if (file.exists()) {
                //Toast.makeText(Register.this,"Start reading", Toast.LENGTH_LONG).show();
                String[] separated = (FileHelper.ReadFile(Register.this)).split("\\;");

                Name.setText(separated[1]);
                Gender.setText(separated[2]);
                DOB.setText(separated[3]);
                ID.setText(separated[4]);

                String test = separated[5];
                if(test.contains("Mykad"))
                {
                    spinner.setSelection(0);
                }
                if(test.contains("MyTentera"))
                {
                    spinner.setSelection(1);
                }
                if(test.contains("MyPolis"))
                {
                    spinner.setSelection(2);
                }


                //spinner
                Nationality.setText(separated[6]);
                Address.setText(separated[7]);
                Address2.setText(separated[8]);
                Address3.setText(separated[9]);
                City.setText(separated[10]);
                Postcode.setText(separated[11]);
                State.setText(separated[12]);




            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

    public void ViewFile2()
    {
        try {
            File file = new File(page1);
            if (file.exists()) {

                String[] separated = (FileHelper.ReadFile2(Register.this)).split("\\;");



                if (String.valueOf(count).contains("0")) {

                    //Operator= separated[0];

                    String[] baths = getResources().getStringArray(R.array.operator_arrays);
                    spinner.setSelection(Arrays.asList(baths).indexOf(separated[0]));

                    PhoneNo.setText(separated[1]);
                    SerialNo.setText(separated[2]);
                    PUK1.setText(separated[3]);
                    PUK2.setText(separated[4]);



                }

                if (String.valueOf(count).contains("1")) {
                   // Operator= separated[0];

                    String[] baths = getResources().getStringArray(R.array.operator_arrays);
                    spinner2.setSelection(Arrays.asList(baths).indexOf(separated[0]));
                    PhoneNo.setText(separated[1]);
                    SerialNo.setText(separated[2]);
                    PUK1.setText(separated[3]);
                    PUK2.setText(separated[4]);

                   // Operatorsim2= separated[6];

                    String[] baths2 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim2.setSelection(Arrays.asList(baths2).indexOf(separated[6]));
                    PhoneNosim2.setText(separated[1]);
                    SerialNosim2.setText(separated[2]);
                    PUK1sim2.setText(separated[3]);
                    PUK2sim2.setText(separated[4]);




                }
                if (String.valueOf(count).contains("2")) {
                    //Operator= separated[0];
                    String[] baths = getResources().getStringArray(R.array.operator_arrays);
                    spinner2.setSelection(Arrays.asList(baths).indexOf(separated[0]));
                    PhoneNo.setText(separated[1]);
                    SerialNo.setText(separated[2]);
                    PUK1.setText(separated[3]);
                    PUK2.setText(separated[4]);


                    //Operatorsim2= separated[6];
                    String[] baths2 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim2.setSelection(Arrays.asList(baths).indexOf(separated[6]));
                    PhoneNosim2.setText(separated[7]);
                    SerialNosim2.setText(separated[8]);
                    PUK1sim2.setText(separated[9]);
                    PUK2sim2.setText(separated[10]);


                    //Operatorsim3= separated[12];
                    String[] baths3 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim3.setSelection(Arrays.asList(baths).indexOf(separated[12]));
                    PhoneNosim3.setText(separated[13]);
                    SerialNosim3.setText(separated[14]);
                    PUK1sim3.setText(separated[15]);
                    PUK2sim3.setText(separated[16]);



                }
                if (String.valueOf(count).contains("3")) {
                    //Operator= separated[0];
                    String[] baths = getResources().getStringArray(R.array.operator_arrays);
                    spinner2.setSelection(Arrays.asList(baths).indexOf(separated[0]));
                    PhoneNo.setText(separated[1]);
                    SerialNo.setText(separated[2]);
                    PUK1.setText(separated[3]);
                    PUK2.setText(separated[4]);


                    //Operatorsim2= separated[6];
                    String[] baths2 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim2.setSelection(Arrays.asList(baths).indexOf(separated[6]));
                    PhoneNosim2.setText(separated[7]);
                    SerialNosim2.setText(separated[8]);
                    PUK1sim2.setText(separated[9]);
                    PUK2sim2.setText(separated[10]);


                    //Operatorsim3= separated[12];
                    String[] baths3 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim3.setSelection(Arrays.asList(baths).indexOf(separated[12]));
                    PhoneNosim3.setText(separated[13]);
                    SerialNosim3.setText(separated[14]);
                    PUK1sim3.setText(separated[15]);
                    PUK2sim3.setText(separated[16]);


                    //Operatorsim3= separated[18];
                    String[] baths4 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim4.setSelection(Arrays.asList(baths).indexOf(separated[18]));
                    PhoneNosim4.setText(separated[19]);
                    SerialNosim4.setText(separated[20]);
                    PUK1sim4.setText(separated[21]);
                    PUK2sim4.setText(separated[22]);


                }

                if (String.valueOf(count).contains("4")) {

                    String[] baths = getResources().getStringArray(R.array.operator_arrays);
                    spinner2.setSelection(Arrays.asList(baths).indexOf(separated[0]));
                    PhoneNo.setText(separated[1]);
                    SerialNo.setText(separated[2]);
                    PUK1.setText(separated[3]);
                    PUK2.setText(separated[4]);


                    //Operatorsim2= separated[6];
                    String[] baths2 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim2.setSelection(Arrays.asList(baths).indexOf(separated[6]));
                    PhoneNosim2.setText(separated[7]);
                    SerialNosim2.setText(separated[8]);
                    PUK1sim2.setText(separated[9]);
                    PUK2sim2.setText(separated[10]);


                    //Operatorsim3= separated[12];
                    String[] baths3 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim3.setSelection(Arrays.asList(baths).indexOf(separated[12]));
                    PhoneNosim3.setText(separated[13]);
                    SerialNosim3.setText(separated[14]);
                    PUK1sim3.setText(separated[15]);
                    PUK2sim3.setText(separated[16]);


                    //Operatorsim3= separated[18];
                    String[] baths4 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim4.setSelection(Arrays.asList(baths).indexOf(separated[18]));
                    PhoneNosim4.setText(separated[19]);
                    SerialNosim4.setText(separated[20]);
                    PUK1sim4.setText(separated[21]);
                    PUK2sim4.setText(separated[22]);

                    //Operatorsim3= separated[18];
                    String[] baths5 = getResources().getStringArray(R.array.operator_arrays);
                    spinner2sim5.setSelection(Arrays.asList(baths).indexOf(separated[24]));
                    PhoneNosim5.setText(separated[25]);
                    SerialNosim5.setText(separated[26]);
                    PUK1sim5.setText(separated[27]);
                    PUK2sim5.setText(separated[28]);



                }



                //simcardview.setText ((FileHelper.ReadFile2(SubmitPreview.this)).replaceAll("\\;",System.getProperty("line.separator")));

            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }



    public void checkServer()
    {
            FPServer = isPortOpen(http_url, 8081, 10000);
            FaceServer = isPortOpen(http_url, 83, 10000);
            StaticServer = isPortOpen(http_url, 81, 10000);

            if(StaticServer.toString() == "false")
            {
                Toast.makeText(Register.this, "Server Down", Toast.LENGTH_LONG).show();
            }
            if(FPServer.toString() == "false")
            {
                Toast.makeText(Register.this, "Fingerprint Server Down", Toast.LENGTH_LONG).show();
            }
            if(FaceServer.toString() == "false")
            {
                Toast.makeText(Register.this, "Face Server Down", Toast.LENGTH_LONG).show();
            }
    }

    public void createSuppDoc()
    {
        String filename = Environment.getExternalStorageDirectory() + "/SuppDoc/"  + ID.getText().toString();
        //Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_LONG).show();
        File folder = new File(filename);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
        } else {
            // Do something else on failure
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void init()
    {
        // Get USB manager
        mManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        // Initialize reader
        mReader = new Reader(mManager);
        mReader.setOnStateChangeListener(new Reader.OnStateChangeListener() {

            @Override
            public void onStateChange(int slotNum, int prevState, int currState) {

                if (prevState < Reader.CARD_UNKNOWN
                        || prevState > Reader.CARD_SPECIFIC) {
                    prevState = Reader.CARD_UNKNOWN;
                }

                if (currState < Reader.CARD_UNKNOWN
                        || currState > Reader.CARD_SPECIFIC) {
                    currState = Reader.CARD_UNKNOWN;
                }

                // Create output string
                final String outputString = "Slot " + slotNum + ": "
                        + stateStrings[prevState] + " -> "
                        + stateStrings[currState];

                // Show output
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {//logMsg(outputString);
                    }
                });
            }
        });

        // Register receiver for USB permission
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mReceiver, filter);

        // Initialize reader spinner
        for (UsbDevice device : mManager.getDeviceList().values()) {
            if (mReader.isSupported(device)) {
                dev = device.getDeviceName();
            }
        }

        if (dev != null) {

            // For each device
            for (UsbDevice device : mManager.getDeviceList().values()) {

                // If device name is found
                if (dev.equals(device.getDeviceName())) {

                    // Request permission
                    mManager.requestPermission(device,
                            mPermissionIntent);

                    //requested = true;
                    break;
                }
            }
        }
    }

    public void powered()
    {
        try {

            // Set parameters
            PowerParams params = new PowerParams();
            params.slotNum = 0;
            params.action = 2;

            // Perform power action
            System.out.println("Slot " + 0 + ": " + powerActionStrings[2] + "...");
            new PowerTask().execute(params);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
        }
    }

    public void Protocol() {

        SetProtocolResult result = new SetProtocolResult();

        try {

            result.activeProtocol = mReader.setProtocol(0, Reader.PROTOCOL_T0);

        } catch (Exception e) {

            progress.dismiss();
            result.e = e;
        }

        SendAPDU1();
    }

    public void ReadMRZ(View v)
    {
        if (categorytype.contains("CITIZEN")){
            Toast.makeText(Register.this, "Passport reading is only for Foreigner", Toast.LENGTH_LONG).show();
        }
        else
        {
        Toast.makeText(Register.this, "Reading MRZ", Toast.LENGTH_LONG).show();

       /* Intent mrz = new Intent(Intent.ACTION_MAIN);
        mrz.setComponent(new ComponentName("com.regula.documentreader", "com.regula.documentreader.Splash"));
      //mrz.putExtra("Print", "Admin;"+ID.getText().toString()+";"+PhoneNo.getText().toString()+";"+formattedDate);
        startActivity(mrz);*/
        Intent intent = new Intent();
        intent.setClassName("com.regula.documentreader", "com.regula.documentreader.CaptureActivity");
        startActivityForResult(intent, 5);
        //Toast.makeText(Register.this,"Start reading", Toast.LENGTH_LONG).show();
    }
    }


    public void VerifyMykad()
    {
        //Toast.makeText(Register.this, "Verify Fingerprint on MyKad", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClassName("com.morpho.Verify", "com.morpho.Verify.ConnectionActivity");
        startActivityForResult(intent, 1);
        //startActivity(intent);
    }



    @Override
    public void update(Observable observable, Object data) {
    }

    private void setDateTimeField() {
        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DOB.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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
            progress = ProgressDialog.show(Register.this, "Waiting", "Sending", true);
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
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    public void OnMovers(View v) {
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://103.21.34.216:81/moversdemo/mykadquery.php"));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ekyc.inetgo.net/fr-web/check"));
        startActivity(browserIntent);
       // SearchingTask task = new SearchingTask();
        // task.execute();
    }

    public void onSimadd(View v)
    {
        count = count+1;
        LinearLayout layoutsim1= (LinearLayout)findViewById(R.id.layoutsim1);
        LinearLayout layoutsim2= (LinearLayout)findViewById(R.id.layoutsim2);
        LinearLayout layoutsim3= (LinearLayout)findViewById(R.id.layoutsim3);
        LinearLayout layoutsim4= (LinearLayout)findViewById(R.id.layoutsim4);
        LinearLayout layoutsim5= (LinearLayout)findViewById(R.id.layoutsim5);

        if (count ==1)
        {
            layoutsim2.setVisibility(View.VISIBLE);
        }
        if (count ==2)
        {
            layoutsim3.setVisibility(View.VISIBLE);
        }
        if (count ==3)
        {
            layoutsim4.setVisibility(View.VISIBLE);
        }
        if (count ==4)
        {
            layoutsim5.setVisibility(View.VISIBLE);

            Toast.makeText(Register.this, "You have reach limit of added SIM Card Number", Toast.LENGTH_LONG).show();
        }


    }

    public void onUndoSimadd(View v)
    {
        LinearLayout layoutsim1= (LinearLayout)findViewById(R.id.layoutsim1);
        LinearLayout layoutsim2= (LinearLayout)findViewById(R.id.layoutsim2);
        LinearLayout layoutsim3= (LinearLayout)findViewById(R.id.layoutsim3);
        LinearLayout layoutsim4= (LinearLayout)findViewById(R.id.layoutsim4);
        LinearLayout layoutsim5= (LinearLayout)findViewById(R.id.layoutsim5);

        if (count ==1)
        {
            count = count-1;
            layoutsim2.setVisibility(View.GONE);
        }
        if (count ==2)
        {     count = count-1;
            layoutsim3.setVisibility(View.GONE);
        }
        if (count ==3)
        {     count = count-1;
            layoutsim4.setVisibility(View.GONE);
        }
        if (count ==4)
        {     count = count-1;
            layoutsim5.setVisibility(View.GONE);
        }


    }

    public void OnRegister(View v)
    {
        CheckFile2();
        isConnectedToInternet();
        checkServer();
        boolean fieldsOK=validate(new EditText[]{Name, ID, DOB, Nationality, Gender, PUK1, PUK2, PhoneNo, SerialNo, Address, Address2, Address3, Postcode, City, State});

        if(LiveCapture.getDrawable() == null)
        {
            Toast.makeText(Register.this, "Image not insert", Toast.LENGTH_LONG).show();
        }
        else
        {
            ImageView tick1 = (ImageView)findViewById(R.id.tick1);
            ImageView tick2 = (ImageView)findViewById(R.id.tick2);


           // if(tick1.getVisibility() == View.VISIBLE && tick2.getVisibility() == View.VISIBLE) {

                if(tick1.getVisibility() == View.VISIBLE ) {
                if (fieldsOK == false) {
                    Toast.makeText(Register.this, "Please Fill the Form", Toast.LENGTH_LONG).show();
                } else {


                    //personal info
                    page1_Value = repl + ";" + Name.getText().toString() + ";" + Gender.getText().toString() + ";" + DOB.getText().toString() + ";" +
                            ID.getText().toString() + ";" + spinner.getSelectedItem().toString() + ";" + Nationality.getText().toString()
                            + ";" + Address.getText().toString() + ";" + Address2.getText().toString() + ";" + Address3.getText().toString()
                            + ";" + City.getText().toString() + ";" + Postcode.getText().toString() + ";" + State.getText().toString();

                    FileHelper.saveToFile(page1_Value);


                    //sim info

                    if (count == 0) {
                        page2_Value = spinner2.getSelectedItem().toString() + ";" + PhoneNo.getText().toString() + ";" + SerialNo.getText().toString() + ";" + PUK1.getText().toString() + ";" +
                                PUK2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);
                    }
                    if (count == 1) {
                        page2_Value = spinner2.getSelectedItem().toString() + ";" + PhoneNo.getText().toString() + ";" + SerialNo.getText().toString() + ";" + PUK1.getText().toString() + ";" +
                                PUK2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim2.getSelectedItem().toString() + ";" + PhoneNosim2.getText().toString() + ";" + SerialNosim2.getText().toString() + ";" + PUK1sim2.getText().toString() + ";" +
                                PUK2sim2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);
                    }

                    if (count == 2) {
                        page2_Value = spinner2.getSelectedItem().toString() + ";" + PhoneNo.getText().toString() + ";" + SerialNo.getText().toString() + ";" + PUK1.getText().toString() + ";" +
                                PUK2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim2.getSelectedItem().toString() + ";" + PhoneNosim2.getText().toString() + ";" + SerialNosim2.getText().toString() + ";" + PUK1sim2.getText().toString() + ";" +
                                PUK2sim2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim3.getSelectedItem().toString() + ";" + PhoneNosim3.getText().toString() + ";" + SerialNosim3.getText().toString() + ";" + PUK1sim3.getText().toString() + ";" +
                                PUK2sim3.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);
                    }

                    if (count == 3) {
                        page2_Value = spinner2.getSelectedItem().toString() + ";" + PhoneNo.getText().toString() + ";" + SerialNo.getText().toString() + ";" + PUK1.getText().toString() + ";" +
                                PUK2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim2.getSelectedItem().toString() + ";" + PhoneNosim2.getText().toString() + ";" + SerialNosim2.getText().toString() + ";" + PUK1sim2.getText().toString() + ";" +
                                PUK2sim2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim3.getSelectedItem().toString() + ";" + PhoneNosim3.getText().toString() + ";" + SerialNosim3.getText().toString() + ";" + PUK1sim3.getText().toString() + ";" +
                                PUK2sim3.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim4.getSelectedItem().toString() + ";" + PhoneNosim4.getText().toString() + ";" + SerialNosim4.getText().toString() + ";" + PUK1sim4.getText().toString() + ";" +
                                PUK2sim4.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);
                    }

                    if (count == 4) {
                        page2_Value = spinner2.getSelectedItem().toString() + ";" + PhoneNo.getText().toString() + ";" + SerialNo.getText().toString() + ";" + PUK1.getText().toString() + ";" +
                                PUK2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim2.getSelectedItem().toString() + ";" + PhoneNosim2.getText().toString() + ";" + SerialNosim2.getText().toString() + ";" + PUK1sim2.getText().toString() + ";" +
                                PUK2sim2.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim3.getSelectedItem().toString() + ";" + PhoneNosim3.getText().toString() + ";" + SerialNosim3.getText().toString() + ";" + PUK1sim3.getText().toString() + ";" +
                                PUK2sim3.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim4.getSelectedItem().toString() + ";" + PhoneNosim4.getText().toString() + ";" + SerialNosim4.getText().toString() + ";" + PUK1sim4.getText().toString() + ";" +
                                PUK2sim4.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);

                        page2_Value = ";" + spinner2sim5.getSelectedItem().toString() + ";" + PhoneNosim5.getText().toString() + ";" + SerialNosim5.getText().toString() + ";" + PUK1sim5.getText().toString() + ";" +
                                PUK2sim5.getText().toString() + ";";
                        FileHelper.saveToFile2(page2_Value);
                    }

                    Intent i = new Intent(this, SubmitPreview.class);
                    i.putExtra("count", String.valueOf(count));
                    i.putExtra("type",categorytype);
                    TextView supp = (TextView)findViewById(R.id.textView23);
                    i.putExtra("supp",supp.getText());
                    startActivity(i);
                    this.finish();

                }
                /*if(FPServer.toString() == "true" && StaticServer.toString() == "true" && FaceServer.toString() == "true") {
                     FPID = ID.getText().toString();
                    SendFP();
                    //SearchingTask task = new SearchingTask();
                    //task.execute();
                    //addperson1();
                }
                else
                {
                    Toast.makeText(Register.this, "Check Server", Toast.LENGTH_LONG).show();
                }*/
            }
            else{
                Toast.makeText(Register.this, "Please capture biometric details", Toast.LENGTH_LONG).show();
            }
        }
    }



    public void OnRegister2(View v)
    {

        if (categorytype.contains("CITIZEN"))
        {
            //personal info
            page1_Value = repl+ ";" +Name.getText().toString()+ ";" +Gender.getText().toString()+ ";" +DOB.getText().toString()+ ";" +
                  ID.getText().toString()+ ";" +spinner.getSelectedItem().toString()+ ";" +Nationality.getText().toString()
                  + ";" +Address.getText().toString()+ ";" +Address2.getText().toString()+ ";" +Address3.getText().toString()
                  + ";" +City.getText().toString()+ ";" +Postcode.getText().toString()+ ";" +State.getText().toString();

            FileHelper.saveToFile(page1_Value);


            //sim info
            page2_Value = spinner2.getSelectedItem().toString()+ ";" +PhoneNo.getText().toString()+ ";" +SerialNo.getText().toString()+ ";" +PUK1.getText().toString()+ ";" +
                    PUK2.getText().toString();

            FileHelper.saveToFile2(page2_Value);

            Intent i = new Intent(this, SubmitPreview.class);
            startActivity(i);
            this.finish();
        }

        if (categorytype.contains("FOREIGNER"))
        {
            page1_Value = repl+ ";" +Name.getText().toString()+ ";" +Gender.getText().toString()+ ";" +DOB.getText().toString()+ ";" +
                    ID.getText().toString()+ ";" +spinner.getSelectedItem().toString()+ ";" +Nationality.getText().toString()
                    + ";" +Address.getText().toString()+ ";" +Address2.getText().toString()+ ";" +Address3.getText().toString()
                    + ";" +City.getText().toString()+ ";" +Postcode.getText().toString()+ ";" +State.getText().toString();




            FileHelper.saveToFile(page1_Value);
            Intent i = new Intent(this, SubmitPreview.class);
            //  i.putExtra("name", picLive);
            //  i.putExtra("type",categorytype);
            startActivity(i);
            this.finish();
        }

        if (categorytype.contains("BUSINESS"))
        {
            page1_Value = repl+ ";" +Name.getText().toString()+ ";" +Gender.getText().toString()+ ";" +DOB.getText().toString()+ ";" +
                    ID.getText().toString()+ ";" +spinner.getSelectedItem().toString()+ ";" +Nationality.getText().toString()
                    + ";" +Address.getText().toString()+ ";" +Address2.getText().toString()+ ";" +Address3.getText().toString()
                    + ";" +City.getText().toString()+ ";" +Postcode.getText().toString()+ ";" +State.getText().toString();




            FileHelper.saveToFile(page1_Value);
            Intent i = new Intent(this, SubmitPreview.class);
            //  i.putExtra("name", picLive);
            //  i.putExtra("type",categorytype);
            startActivity(i);
            this.finish();
        }

/*

        isConnectedToInternet();
        checkServer();
        boolean fieldsOK=validate(new EditText[]{Name, ID, DOB, Nationality, Gender, PUK1, PUK2, PhoneNo, SerialNo, Address, Address2, Address3, Postcode, City, State});

        if(LiveCapture.getDrawable() == null)
        {
            Toast.makeText(Register.this, "Image not insert", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(fieldsOK == false)
            {
                Toast.makeText(Register.this, "Please Fill the Form", Toast.LENGTH_LONG).show();
            }
            else
            {

                if (categorytype.contains("CITIZEN"))
                {
                    page1_Value = Name + ";" + ID + ";" + DOB + ";" + Nationality + ";" + Name + ";" + ID + ";" + DOB
                            + ";" + Nationality + ";" + Gender + ";" + PUK1 + ";" + PUK2 + ";" + PhoneNo + ";" + SerialNo
                            + ";" + Address + ";" + Address2 + ";" + Address3 + ";" +  Postcode + ";" + City + ";" + State;
                    CheckFile();
                    FileHelper.saveToFile(page1_Value);
                    Intent i = new Intent(this, SubmitPreview.class);
                  //  i.putExtra("name", picLive);
                  //  i.putExtra("type",categorytype);
                    startActivity(i);
                    this.finish();
                }








            }
        }*/
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
                    Toast.makeText(Register.this, "Face Image not clear : Check failed", Toast.LENGTH_LONG).show();
                    //DeleteFP();
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
                    Toast.makeText(Register.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                }
                else {
                    progress.dismiss();
                    Toast.makeText(Register.this, "Face Image not clear : Check failed", Toast.LENGTH_LONG).show();
                    //DeleteFP();
                    Register.setEnabled(true);
                }
            }

        }
    }

    private byte[] JPNDF3(byte[] data)
    {
        int t = 7;

        byte[] response = new byte[512];
        int responseLength = 0;
        try {
            System.out.println("***COMMAND APDU***");
            System.out.println("IFD - " + getHexString(data, data.length));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (iSlotNum >= 0) {
            try {
                responseLength = mReader.transmit(iSlotNum, data, data.length, response, response.length);
                byte[] GetResponse = {(byte) 0x00, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x05};
                responseLength = mReader.transmit(iSlotNum, GetResponse, GetResponse.length, response, response.length);
                for(int i = 0; i <= 2; i ++)
                {
                    convbyte(t);
                    // t = t+255;
                    System.out.println("value t : " + t);


                    if(i == 2)
                    {
                        //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF3_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_1, JPNDF3_1.length, response, response.length);
                        byte[] JPNDF3_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x05, (byte) 0x02, (byte) 0x02, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_2, JPNDF3_2.length, response, response.length);
                        byte[] JPNDF3_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x02};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_3, JPNDF3_3.length, response, response.length);


                       /* //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF3_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x9F, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_1, JPNDF3_1.length, response, response.length);
                        byte[] JPNDF3_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0D, (byte) 0x04, (byte) 0x9F, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_2, JPNDF3_2.length, response, response.length);
                        byte[] JPNDF3_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x9F};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_3, JPNDF3_3.length, response, response.length);

                    }
                    else if(i == 1)
                    {
                        byte[] JPNDF3_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_1, JPNDF3_1.length, response, response.length);
                        byte[] JPNDF3_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0E, (byte) 0x03, (byte) 0xFF, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_2, JPNDF3_2.length, response, response.length);
                        byte[] JPNDF3_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_3, JPNDF3_3.length, response, response.length);
                        t = t+159;*/
                    }
                    else if(i == 1)
                    {
                        //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF3_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_1, JPNDF3_1.length, response, response.length);
                        byte[] JPNDF3_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x01, (byte) 0xFF, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_2, JPNDF3_2.length, response, response.length);
                        byte[] JPNDF3_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_3, JPNDF3_3.length, response, response.length);
                        t = t+2;
                    }


                    else {

                        //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF3_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_1, JPNDF3_1.length, response, response.length);
                        byte[] JPNDF3_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) v2, (byte) v1, (byte) 0xFF, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_2, JPNDF3_2.length, response, response.length);
                        byte[] JPNDF3_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
                        responseLength = mReader.transmit(iSlotNum, JPNDF3_3, JPNDF3_3.length, response, response.length);
                        t = t+255;
                    }

                    MykadDF3 = MykadDF3 + getHexString(response, responseLength).replaceAll("90 00", "");
                    //A1.setText(MykadDF2);
                    System.out.println("ICC2 - " + MykadDF3);
                }
                //FileHelper.saveToFile2(MykadDF3);
                //getImage();
                //getFP();
                byte[] fp = toByteArray(MykadDF3.replaceAll(" ", ""));
                File f1 = new File("/storage/emulated/0/os2.pkmn");
                saveBytesToFile2(fp, f1);
                VerifyMykad();

               /* byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                byte[] JPNDF2_3 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xFF};
                responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);*/

            } catch (Exception e) {
                System.out.println("****************************************");
                System.out.println("       ERROR transmit: Review APDU  ");
                System.out.println("****************************************");
                responseLength = 0;
                byte[] ra = Arrays.copyOf(response, responseLength);
                response = null;
                return (ra);
            }
           /* try {
                System.out.println("");
                MykadDF2 = MykadDF2 + " " +  getHexString(response, responseLength);
                //DF2();
                //System.out.println("ICC2 - " + getHexString(response, responseLength));
                System.out.println("ICC2 - " + MykadDF2);
                byte[] ra2 = Arrays.copyOf(response, responseLength);
                respJPNDF2_1 = ra2;

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        byte[] ra = Arrays.copyOf(response, responseLength);
        response = null;
        return (ra);
    }


    public static void saveBytesToFile2(byte[] bytes, File outputFile) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(outputFile);
        out.flush();
        out.write(bytes);
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

    public static byte[] toBytesFromHex(String hex) {
        byte rc[] = new byte[hex.length() / 2];
        for (int i = 0; i < rc.length; i++) {
            String h = hex.substring(i * 2, i * 2 + 2);
            int x = Integer.parseInt(h, 16);
            rc[i] = (byte) x;
        }
        return rc;
    }

    public void getImage()
    {
        String result = MykadDF2;
        ICimage = result.replaceAll(" ", "");
        //String data = result.replaceAll(" ", "");
        //ShowImage(toBytesFromHex(data));
    }

    public void ShowImage(byte[] data)
    {
//mykad image
        progress.dismiss();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        mykadImage.setImageBitmap(bitmap);
        mykadImage.setVisibility(View.VISIBLE);
        lblMykad.setVisibility(View.VISIBLE);
       // Toast.makeText(Register.this, "Please Verify your MyKad Fingerprint", Toast.LENGTH_LONG).show();
        //VerifyMykad();
    }


    public void registerUser()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, bypersonalinfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG, "response: " + response);

                        if(response.toString().contains("Success"))
                        {

                            //progress.dismiss();
                            Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            onAttachDocSend();

                            Intent intent = new Intent(Register.this, Payment.class);
                            intent.putExtra("Print", "Shop User ID : Admin \n   Customer ID: "+ID.getText().toString()+"\n   Telco: "+ spinner2.getSelectedItem().toString() +"\n   Phone No: "+PhoneNo.getText().toString()+";;;Date: "+formattedDate);
                            startActivity(intent);
                            finish();

                        }
                        else
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
                params.put("Name",Name.getText().toString());
                params.put("Father_Name","");
                params.put("Mother_Name","");
                params.put("Grandfather_Name","");
                params.put("Spouse_Name","");
                params.put("Gender",Gender.getText().toString());
                params.put("Birth_Date",DOB.getText().toString());
                params.put("Doc_No",ID.getText().toString());
                params.put("Doc_Ty",spinner.getSelectedItem().toString());
                params.put("Nationality",Nationality.getText().toString());
                params.put("Occupation","");
                params.put("Email","");
                params.put("Per_Addr1",Address.getText().toString());
                params.put("Per_Addr2",Address2.getText().toString());
                params.put("Per_Addr3",Address3.getText().toString());
                params.put("Per_Town",City.getText().toString());
                params.put("Per_POBox",Postcode.getText().toString());
                params.put("Per_State",State.getText().toString());

                //siminfo
                params.put("operator",spinner2.getSelectedItem().toString());
                params.put("phoneNo",PhoneNo.getText().toString());
                params.put("serialno", SerialNo.getText().toString());
                params.put("puk1",PUK1.getText().toString());
                params.put("puk2", PUK2.getText().toString());

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
                        Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_LONG).show();

                       /* Intent intent3 = new Intent(Intent.ACTION_MAIN);
                        intent3.setComponent(new ComponentName("com.taztag.testthermalprinter", "com.taztag.testthermalprinter.MainActivity"));
                        intent3.putExtra("Print", "Shop User ID : Admin \n   Customer ID: "+ID.getText().toString()+"\n   Telco: "+ spinner2.getSelectedItem().toString() +"\n   Phone No: "+PhoneNo.getText().toString()+";;;Date: "+formattedDate);
                        startActivity(intent3);
                        onBackPressed();*/
                        Intent intent = new Intent(Register.this, Payment.class);
                        intent.putExtra("Print", "Shop User ID : Admin \n   Customer ID: "+ID.getText().toString()+"\n   Telco: "+ spinner2.getSelectedItem().toString() +"\n   Phone No: "+PhoneNo.getText().toString()+";;;Date: "+formattedDate);
                        startActivity(intent);

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
                        //Log.d(TAG, error.getMessage());
                        //System.out.println(error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){

                //Converting Bitmap to String

                //String image = getStringImage(mphoto);

                Map<String,String> params = new HashMap<String, String>();

                params.put("docty", spinner.getSelectedItem().toString());
                params.put("docno", ID.getText().toString());
                params.put("operator",spinner2.getSelectedItem().toString());
                params.put("phoneNo",PhoneNo.getText().toString());
                params.put("serialno", SerialNo.getText().toString());
                params.put("puk1",PUK1.getText().toString());
                params.put("puk2", PUK2.getText().toString());


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

        params.put("idCardNo", ID.getText().toString());
        params.put("photoFile", toBase64Image(actualimage));

        try {
            byte[] resp = doPost("/faceRecognition", params);

            System.out.println("Responses :" + new String(resp, "UTF-8"));
            PersonID = new String(resp,"UTF-8");

            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(new String(resp,"UTF-8"));

            if (m.find(1)) {
                repl = m.group();
            }

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
            //Log.i(TAG, new String(resp, "UTF-8"));
            System.out.println("Responses :" + new String(resp, "UTF-8"));
            //PersonID = new String(resp,"UTF-8");
            //String repl = PersonID.replaceAll("[^0-9]", "");
            // System.out.println(repl);
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

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    static public String getServerUrlBase() {
       // return "http://" + ServerAddress + "/arges-service/api";
       // return "http://" + ServerAddress + "/service/api";
        return "https://ekyc.inetgo.net/fr-web/api";
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    public void imageClick(View view) { //photo
        camera = "1";
        startCamera();
    }

    public void imageClick2(View view) { //supp doc

        ImageView tick5 = (ImageView)findViewById(R.id.tick5);

        Toast.makeText(getApplicationContext(), "Add multiple supporting Documents", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClassName("com.junjunguo.imageadaptertolistview", "com.junjunguo.imageadaptertolistview.MainActivity");
        startActivityForResult(intent, 6);

    }

    private void startCamera()
    {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, CAMERA_REQUEST);

        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File("/storage/emulated/0/test.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 1888);
    }

    private void startCamera2()
    {
        File file;
        Uri fileUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File("/storage/emulated/0/SuppDoc/" +ID.getText().toString()+ "/1.jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        this.startActivityForResult(intent, 13);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:
                try {
                    System.out.println(fppath);

                    File file = new File(fppath);
                    if (file.exists()) {
                        //Toast.makeText(Register.this,"Start reading", Toast.LENGTH_LONG).show();
                        String[] separated = (FileHelper.ReadFP(getApplicationContext())).split("\\;");
                        //Name.setText(separated[0]);

                        FPresult = separated[0];
                        if(FPresult.contains("Fingerprint Match"))
                        {
                            Toast.makeText(getApplicationContext(), "Fingerprint Match", Toast.LENGTH_LONG).show();
                            SendAPDU();
                            ICCard.setClickable(false);
                            ImageView tick4 = (ImageView)findViewById(R.id.tick4);
                            tick4.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), "Fingerprint Does Not Match", Toast.LENGTH_LONG).show();
                            ICCard.setClickable(true);
                        }
                    }
                }
                catch (Exception ex)
                {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "MyKad read Error", Toast.LENGTH_LONG).show();
                }

                break;
            case 5:
                try {
                    File file = new File(mrzpath);
                    if (file.exists()) {
                        //Toast.makeText(Register.this,"Start reading", Toast.LENGTH_LONG).show();
                        String[] separated = (FileHelper.ReadFilemrz(Register.this)).split("\\;");
                        spinner.setSelection(2);
                        Name.setText(separated[0]);
                        Gender.setText(separated[1]);
                        ID.setText(separated[2]);
                        Nationality.setText(separated[3]);
                        String date = separated[4];
                        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                        Date d1 = sdf.parse(date);
                        sdf.applyPattern("yyyy-MM-dd");
                        DOB.setText(sdf.format(d1));

                        ImageView tick3 = (ImageView)findViewById(R.id.tick3);
                        tick3.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
                break;

            case 6:
            {
                File file = new File(suppdoc);
                if (file.exists()) {
                    String[] separated = (FileHelper.ReadFileSupp(Register.this)).split("\\;");
                    System.out.println("Supp doc length: " + separated.length);
                    TextView supp = (TextView)findViewById(R.id.textView23);
                    if(separated[0].isEmpty()) {
                        {
                            Toast.makeText(Register.this, "No Attach Document", Toast.LENGTH_LONG).show();
                            supp.setText("No attach documents");
                         //   ImageView tick4 = (ImageView)findViewById(R.id.tick4);
                        //    tick4.setVisibility(View.GONE);
                        }
                    }
                    else{
                        //LinearLayout imageCapture = (LinearLayout) findViewById(R.id.imageCapture);
                        //imageCapture.setVisibility(View.VISIBLE);

                        ImageView tick5 = (ImageView)findViewById(R.id.tick5);
                        tick5.setVisibility(View.VISIBLE);
                        supp.setText("Attach documents : +  " + (separated.length - 1) + " documents");
                    }
                }
                break;
            }

            case 13:
                if (requestCode == 13 && resultCode == RESULT_OK) {

                    if (mphoto2 != null) {
                        mphoto2.recycle();
                    }

                    File imgFile = new  File("/storage/emulated/0/SuppDoc/"+ID.getText().toString()+"/1.jpg");

                    if(imgFile.exists()){

                        RelativeLayout imageCapture3 = (RelativeLayout) findViewById(R.id.relativeLayout2);
                        imageCapture3.setVisibility(View.VISIBLE);
                        ImageView tick5 = (ImageView)findViewById(R.id.tick5);
                        tick5.setVisibility(View.VISIBLE);

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        AttachDoc.setImageBitmap(mark(myBitmap, "For Demo Purposes"));

                    }
                    break;



                }



            case 20:
                CheckFP();
                break;

            case 1888:
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

                    if(imgFile.exists()){

                        LinearLayout imageCapture = (LinearLayout) findViewById(R.id.imageCapture);
                        imageCapture.setVisibility(View.VISIBLE);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
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

                        actualimage = Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, false);

                        LiveCapture.setImageBitmap(actualimage);
                        LiveCapture.setVisibility(View.VISIBLE);

                        ImageView tick1 = (ImageView)findViewById(R.id.tick1);
                        tick1.setVisibility(View.VISIBLE);

                    }
                    break;
                }

            default:
            {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (scanningResult != null) {
                    String scanContent = scanningResult.getContents();
                    String scanFormat = scanningResult.getFormatName();
                    //Toast.makeText(Register.this, "CONTENT: " + scanContent + " FORMAT: " + scanFormat, Toast.LENGTH_LONG).show();
                    SerialNo.setText(scanContent);
                } else {
                   // Toast toast = Toast.makeText(getApplicationContext(),
                   //         "No scan data received!", Toast.LENGTH_SHORT);
                  //  toast.show();
                }
            }
        }


    }




    public void CheckFP()
    {
        try {

            File file = new File(path);
            if (file.exists()) {
                ImageView tick2 = (ImageView)findViewById(R.id.tick2);
                tick2.setVisibility(View.VISIBLE);
            }
            else
            {    ImageView tick2 = (ImageView)findViewById(R.id.tick2);
                tick2.setVisibility(View.GONE);
            }
        }
        catch (Exception ex)
        {
        }

    }



    // Get Mykad Details
    public void DF1()
    {
         name = Mykad.substring(0,240).replaceAll(" ", "");
         name1 = convertHexToString(name);
         ICName = name1.replaceAll("\\s+", " ");
         ICID = Mykad.substring(360,399).replaceAll(" ", "");
         ICGender = Mykad.substring(399,405).replaceAll(" ", "");
         ICDOB = Mykad.substring(426,438).replaceAll(" ", "");

        Name.setText(ICName);
        ID.setText(convertHexToString(ICID));

        String gend = convertHexToString(ICGender);

        if(gend.contains("L"))
        {
            Gender.setText("M");
        }
        else
        {
            Gender.setText("F");
        }
        String dob = ICDOB.substring(0,4) + "-" + ICDOB.substring(4, 6)  + "-" + ICDOB.substring(6, 8);
        DOB.setText(dob);
        //Nationality.setText(convertHexToString(ICCitizen));
    }

    public void DF2()
    {
         ICAdd = MykadDF2.substring(0,90).replaceAll(" ", "");
         ICAdd1 = MykadDF2.substring(90, 180).replaceAll(" ", "");
         ICAdd2 = MykadDF2.substring(180,270).replaceAll(" ", "");
         ICPcode = MykadDF2.substring(270, 278).replaceAll(" ","");
         ICCity = MykadDF2.substring(276,354).replaceAll(" ", "");
         ICState = MykadDF2.substring(354,432).replaceAll(" ", "");

        Address.setText(convertHexToString(ICAdd));
        Address2.setText(convertHexToString(ICAdd1));
        Address3.setText(convertHexToString(ICAdd2));
        Postcode.setText(ICPcode);
        City.setText(convertHexToString(ICCity));
        State.setText(convertHexToString(ICState));

        //FileHelper.saveToFile(";" + convertHexToString(ICAdd) + ";" + convertHexToString(ICAdd1) + ";" + convertHexToString(ICAdd2) + ";" + ICPcode + ";" + convertHexToString(ICCity) + ";" + convertHexToString(ICState));
    }

    public String convertHexToString(String hex){

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for( int i=0; i<hex.length()-1; i+=2 ){

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char)decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

    public void ClearText()
    {
        Name.setText("");
        ID.setText((""));
        Gender.setText((""));
        DOB.setText("");
        Nationality.setText(("MYS"));
        Address.setText("");
        Address2.setText("");
        Address3.setText("");

        mykadImage.setImageDrawable(null);
        MykadDF2="";
    }

    public void SendAPDU1() {

        byteJPNDF3=null;
        respJPNDF3=null;
        byteJPNDF3_1=null;
        respJPNDF3_1=null;
        //convbyte();

        try
        {
            byteJPNDF3 = atohex("00 A4 04 00 0A A0 00 00 00 74 4A 50 4E 00 10");
            respJPNDF3 = JPNDF3(byteJPNDF3);

            respAPDU = JPNDF2(byteJPNDF3);
        }
        catch (Exception ex)
        {

        }
    }

    public void SendAPDU() {
        byteAPDU = null;
        byteAPDU2=null;
        respAPDU=null;
        respAPDU2=null;

        byteJPNDF4=null;
        respJPNDF4=null;
        byteJPNDF4_1=null;
        respJPNDF4_1=null;

        try
        {
            ShowImage(toBytesFromHex(ICimage));

            byteAPDU = atohex("00 A4 04 00 0A A0 00 00 00 74 4A 50 4E 00 10");
            respAPDU = transceives(byteAPDU);

            byteJPNDF4 = atohex("00 A4 04 00 0A A0 00 00 00 74 4A 50 4E 00 10");
            respJPNDF4 = JPNDF4(byteAPDU);
        }
        catch (Exception ex)
        {

        }
    }

    private static byte[] atohex(String data)
    {
        String hexchars = "0123456789abcdef";

        data = data.replaceAll(" ","").toLowerCase();
        if (data == null)
        {
            return null;
        }
        byte[] hex = new byte[data.length() / 2];

        for (int ii = 0; ii < data.length(); ii += 2)
        {
            int i1 = hexchars.indexOf(data.charAt(ii));
            int i2 = hexchars.indexOf(data.charAt(ii + 1));
            hex[ii/2] = (byte)((i1 << 4) | i2);
        }
        return hex;
    }

    private static byte hexStringToByte(String data) {
        return (byte) ((Character.digit(data.charAt(0), 16) << 4)
                + Character.digit(data.charAt(1), 16));
    }


    public void convbyte(int t)
    {
        int value = t;
        String test = Integer.toString(value, 16);
        String result;

        if(test.length() == 4)
        {
            result = test;
            String a1 = result.toString().substring(0,2);
            String a2 = result.toString().substring(2,4);
            offset = a1;
            offset2 = a2;
            System.out.println("Offsets - " + offset + ":" + offset2);

            v1 = hexStringToByte(offset);
            v2 = hexStringToByte(offset2);
            System.out.println("Results4 - " + v2 + ":" + v1);
        }
        else if (test.length() == 3)
        {
            result = ("0" + test);
            String a1 = result.toString().substring(0,2);
            String a2 = result.toString().substring(2,4);
            offset = a1;
            offset2 = a2;
            System.out.println("Offsets - " + offset + ":" + offset2);

            v1 = hexStringToByte(offset);
            v2 = hexStringToByte(offset2);
            System.out.println("Results3 - " + v2 + ":" + v1);
        }
        else if(test.length() == 2)
        {
            result = ("00" + test);
            String a1 = result.toString().substring(0,2);
            String a2 = result.toString().substring(2,4);
            offset = a1;
            offset2 = a2;

            v1 = hexStringToByte(offset);
            v2 = hexStringToByte(offset2);
            System.out.println("Results2 - " + v2 + ":" + v1);
        }
        else if(test.length() == 1)
        {
            result = ("000" + test);
            String a1 = result.toString().substring(0,2);
            String a2 = result.toString().substring(2,4);
            offset = a1;
            offset2 = a2;
            // v1 = Byte.valueOf(offset);
            // v2 = Byte.valueOf(offset2);

            /*int i = Integer.parseInt(offset,16);
            v1 = (byte) i;
            int j = Integer.parseInt(offset2,16);
            v2 = (byte) j;*/

            System.out.println("Results1 - " + v2 + ":" + v1);
            System.out.println("Offsets - " + offset + ":" + offset2);

            v1 = hexStringToByte(offset);
            v2 = hexStringToByte(offset2);
        }
    }

    private byte[] JPNDF2(byte[] data)
    {
        int t = 3;

        byte[] response = new byte[4000];
        int responseLength = 0;
        try {
            System.out.println("***COMMAND APDU***");
            System.out.println("IFD - " + getHexString(data, data.length));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (iSlotNum >= 0) {
            try {
                responseLength = mReader.transmit(iSlotNum, data, data.length, response, response.length);
                byte[] GetResponse = {(byte) 0x00, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x05};
                responseLength = mReader.transmit(iSlotNum, GetResponse, GetResponse.length, response, response.length);
                for(int i = 0; i <= 15; i ++)
                {
                    convbyte(t);
                    //t = t+255;
                    System.out.println("value t : " + t);
                    if(i == 15)
                    {
                        //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF2_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_1, JPNDF2_1.length, response, response.length);
                        byte[] JPNDF2_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) v2, (byte) v1, (byte) 0x99, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                        byte[] JPNDF2_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x99};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);
                        //t=t+175;
                    }

                    else  if(i == 16)
                    {
                        byte[] JPNDF2_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_1, JPNDF2_1.length, response, response.length);
                        byte[] JPNDF2_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x8D, (byte) 0x0F, (byte) 0x10, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                        byte[] JPNDF2_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x10};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);
                    }

                    else {

                        //System.out.println("Results - " + v2 + ":" + v1);
                        byte[] JPNDF2_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_1, JPNDF2_1.length, response, response.length);
                        byte[] JPNDF2_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) v2, (byte) v1, (byte) 0xFF, (byte) 0x00};
                        //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                        byte[] JPNDF2_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
                        responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);
                        t=t+255;
                    }
                    /*byte[] JPNDF2_1 = {(byte) 0xC8, (byte) 0x32, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x78, (byte) 0x00};
                    responseLength = mReader.transmit(iSlotNum, JPNDF2_1, JPNDF2_1.length, response, response.length);
                    byte[] JPNDF2_2 = {(byte) 0xCC, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xF4, (byte) 0x0E, (byte) 0x78, (byte) 0x00};
                    //byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x03,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                    responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                    byte[] JPNDF2_3 = {(byte) 0xCC, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x78};
                    responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);*/


                    MykadDF2 = MykadDF2 + getHexString(response, responseLength).replaceAll("90 00", "");
                    //A1.setText(MykadDF2);
                    // System.out.println("ICC2 - " + MykadDF2);
                }
                //FileHelper.saveToFile(MykadDF2);
                getImage();
                //test1();

               /* byte[] JPNDF2_2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)v2,(byte)v1,(byte)0xFF,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF2_2, JPNDF2_2.length, response, response.length);
                byte[] JPNDF2_3 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xFF};
                responseLength = mReader.transmit(iSlotNum, JPNDF2_3, JPNDF2_3.length, response, response.length);*/

            } catch (Exception e) {
                System.out.println("****************************************");
                System.out.println("       ERROR transmit: Review APDU  ");
                System.out.println("****************************************");
                responseLength = 0;
                byte[] ra = Arrays.copyOf(response, responseLength);
                response = null;
                return (ra);
            }
           /* try {
                System.out.println("");
                MykadDF2 = MykadDF2 + " " +  getHexString(response, responseLength);
                //DF2();
                //System.out.println("ICC2 - " + getHexString(response, responseLength));
                System.out.println("ICC2 - " + MykadDF2);
                byte[] ra2 = Arrays.copyOf(response, responseLength);
                respJPNDF2_1 = ra2;

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        byte[] ra = Arrays.copyOf(response, responseLength);
        response = null;
        return (ra);
    }


    private byte[] transceives (byte[] data) {
        byte[] response = new byte[512];
        int responseLength = 0;
        try {
            System.out.println("***COMMAND APDU***");
            System.out.println("IFD - " + getHexString(data, data.length));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (iSlotNum >= 0) {
            try {
                responseLength = mReader.transmit(iSlotNum, data, data.length, response, response.length);
                byte[] GetResponse = {(byte)0x00,(byte)0xC0,(byte)0x00,(byte)0x00,(byte)0x05};
                responseLength = mReader.transmit(iSlotNum, GetResponse, GetResponse.length, response, response.length);
                byte[] JPNDF1 = {(byte)0xC8,(byte)0x32,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0xFF,(byte)0x00};
                //byte[] JPNDF1 = {(byte)0xC8,(byte)0x32,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0xFFF,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF1, JPNDF1.length, response, response.length);
                byte[] JPNDF2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x01,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x99,(byte)0x00,(byte)0xFF,(byte)0x00};
                //byte[] JPNDF2 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x03,(byte)0x00,(byte)0xFFF,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF2, JPNDF2.length, response, response.length);
                byte[] JPNDF3 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xFF};
                //byte[] JPNDF3 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xFFF};
                responseLength = mReader.transmit(iSlotNum, JPNDF3, JPNDF3.length, response, response.length);
               /* byte[] JPNDF4 = {(byte)0xC8,(byte)0x32,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0xFA0,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF4, JPNDF4.length, response, response.length);
                byte[] JPNDF5 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x02,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x03,(byte)0x00,(byte)0xFA0,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF5, JPNDF5.length, response, response.length);
                byte[] JPNDF6 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0xFA0};
                responseLength = mReader.transmit(iSlotNum, JPNDF6, JPNDF6.length, response, response.length);*/

            } catch (Exception e) {
                System.out.println("****************************************");
                System.out.println("       ERROR transmit: Review APDU  ");
                System.out.println("****************************************");
                responseLength = 0;
                byte[] ra = Arrays.copyOf(response, responseLength);
                response = null;
                return (ra);
            }
            try {
                System.out.println("");
                Mykad =  getHexString(response, responseLength);
                DF1();
                System.out.println("ICC - " + getHexString(response, responseLength));
                byte[] ra2 = Arrays.copyOf(response, responseLength);
                System.out.println("Response : " + response[0] +" : " + responseLength);
                respAPDU2 = ra2;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        byte[] ra = Arrays.copyOf(response, responseLength);
        response = null;
        return (ra);
    }

    private static String getHexString(byte[] data,int slen) throws Exception
    {
        String szDataStr = "";
        for (int ii=0; ii < slen; ii++)
        {
            szDataStr += String.format("%02X ", data[ii] & 0xFF);
        }
        return szDataStr;
    }

    private byte[] JPNDF4(byte[] data)
    {
        byte[] response = new byte[512];
        int responseLength = 0;
        try {
            System.out.println("***COMMAND APDU***");
            System.out.println("IFD - " + getHexString(data, data.length));
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (iSlotNum >= 0) {
            try {
                responseLength = mReader.transmit(iSlotNum, data, data.length, response, response.length);
                byte[] GetResponse = {(byte) 0x00, (byte) 0xC0, (byte) 0x00, (byte) 0x00, (byte) 0x05};
                responseLength = mReader.transmit(iSlotNum, GetResponse, GetResponse.length, response, response.length);
                byte[] JPNDF4 = {(byte)0xC8,(byte)0x32,(byte)0x00,(byte)0x00,(byte)0x05,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x90,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF4, JPNDF4.length, response, response.length);
                byte[] JPNDF5 = {(byte)0xCC,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x04,(byte)0x00,(byte)0x01,(byte)0x00,(byte)0x03,(byte)0x00,(byte)0x90,(byte)0x00};
                responseLength = mReader.transmit(iSlotNum, JPNDF5, JPNDF5.length, response, response.length);
                byte[] JPNDF6 = {(byte)0xCC,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x90};
                responseLength = mReader.transmit(iSlotNum, JPNDF6, JPNDF6.length, response, response.length);

            } catch (Exception e) {
                System.out.println("****************************************");
                System.out.println("       ERROR transmit: Review APDU  ");
                System.out.println("****************************************");
                responseLength = 0;
                byte[] ra = Arrays.copyOf(response, responseLength);
                response = null;
                return (ra);
            }
            try {
                System.out.println("");
                MykadDF2 =  getHexString(response, responseLength);
                DF2();
                System.out.println("ICC2 - " + getHexString(response, responseLength));
                byte[] ra2 = Arrays.copyOf(response, responseLength);
                respJPNDF4_1 = ra2;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] ra = Arrays.copyOf(response, responseLength);
        response = null;
        return (ra);
    }



    public void btn_Read(View v)
    {
        if (categorytype.contains("FOREIGNER")){
            Toast.makeText(Register.this, "MyKad reading is only for Citizen", Toast.LENGTH_LONG).show();
        }
        else {
            Testtask test = new Testtask();
            test.execute();
            //VerifyMykad();
        }
    }



    public void ReadBarcode2()
    {
        //IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //scanIntegrator.initiateScan();
        //  BARCODE_REQUEST

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    private class  Testtask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            init();
            progress = ProgressDialog.show(Register.this, "Waiting", "Reading MyKad Details. Please Wait.", true);
            ClearText();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(500);
                powered();
                // VerifyMykad();
            } catch (Exception ex) {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(Register.this, "Reading MyKad Complete.", Toast.LENGTH_LONG).show();

        }
    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
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
            String[] separated = (FileHelper.ReadFileSupp(Register.this)).split("\\;");

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
                params.put("dir", ID.getText().toString());
                params.put("ImageName", ID.getText().toString()+"/" +ID.getText().toString()+"_"+ No +".jpg");

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }








}



class Utils
{
    public static void justSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e){}

    }
}