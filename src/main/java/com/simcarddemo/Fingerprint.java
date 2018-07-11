package com.simcarddemo;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by user on 2017-03-09.
 */

public class Fingerprint extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.morpho.morphosample", "com.morpho.morphosample.ConnectionActivity"));
        startActivity(intent);
        //startActivity(new Intent(Passport_Veri.this, Enrollment.class));
    }
}
