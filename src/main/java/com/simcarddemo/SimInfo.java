package com.simcarddemo;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SimInfo extends AppCompatActivity {

    //php script
    String byinfo = "http://103.21.34.216:81/simcard/SimInfo.php";


    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siminfo);





        //DOB
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);



    }



    public void onNext(View v)
    {


    }

    @Override
    public void onBackPressed() {


    }



}
