package com.simcarddemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private GridMenuFragment mGridMenuFragment;


    public static final String path = "/storage/emulated/0/Image.wsq";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle bundle = getIntent().getExtras();
        String value1 = bundle.getString("Username");
        String value2 = bundle.getString("LoginTime");
        this.setTitle("SIMCARDrvs                                                              User Login : " + value1 + "     DateTime : " + value2);

        mGridMenuFragment = GridMenuFragment.newInstance(R.drawable.back);

        CheckFP();

        test();
        setupGridMenu();

        mGridMenuFragment.setOnClickMenuListener(new GridMenuFragment.OnClickMenuListener() {
            @Override
            public void onClickMenu(GridMenu gridMenu, int position) {

                getMonthNumber(position);
            }
        });
    }


    public int getMonthNumber(int month) {

        int monthNumber = 0;




        switch (month) {
            case 0:
              startActivity(new Intent(MainMenu.this, FaceRecognition.class));

                //Intent i = new Intent("com.taztag.testthermalprinter.testThermalPrinter.MainActivity");
                //i.putExtra("Print", "test");
               // Intent intent3 = new Intent(Intent.ACTION_MAIN);
                //intent3.setComponent(new ComponentName("com.taztag.testthermalprinter", "com.taztag.testthermalprinter.MainActivity"));
                //intent3.putExtra("Print", "test");
                //startActivity(intent3);

                break;
            case 1:
                CheckFP();
                startActivity(new Intent(MainMenu.this, CustomerInfo.class));

                break;
            case 2:
               // startActivity(intent2);
                //Toast.makeText(getApplicationContext(),"MATCHING",Toast.LENGTH_LONG).show();
               startActivity(new Intent(MainMenu.this, QueryData.class));

               // Intent intent = new Intent(MainMenu.this, CustomerInfo.class);
                //intent.putExtra("Status", "Fingerprint");
               // startActivity(intent);

                break;
            case 3:

                final Dialog dialog = new Dialog(MainMenu.this);
                dialog.setContentView(R.layout.custom_dialog_box);
                dialog.setTitle("Please Select Type: ");
                Button btnExit = (Button) dialog.findViewById(R.id.btnNormal);
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(MainMenu.this, Register.class);
                        intent.putExtra("type","CITIZEN");
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });

                dialog.findViewById(R.id.btnMinor).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, Register.class);
                        intent.putExtra("type","FOREIGNER");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.btnCorporate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainMenu.this, RegisterBusiness.class);
                        intent.putExtra("type","BUSINESS ENTITY");
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                // show dialog on screen
                dialog.show();
                //startActivity(new Intent(MainMenu.this, Register.class));
                break;
        }

        return monthNumber;
    }

    public void CheckFP()
    {
        String supp = "/storage/emulated/0/suppdoc.txt";
        String test4 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/test.jpg";

        try {

            File file2 = new File(supp);
            if (file2.exists()) {
                file2.delete();
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            File file6 = new File(test4);
            if (file6.exists()) {
                file6.delete();
            }

            else
            {
            }
        }
        catch (Exception ex)
        {
        }

    }

    private void test()
    {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main_frame, mGridMenuFragment);
        tx.addToBackStack(null);
        tx.commit();
    }

    private void setupGridMenu() {
        List<GridMenu> menus = new ArrayList<>();
        menus.add(new GridMenu("Face Rec", R.mipmap.face));
        menus.add(new GridMenu("Fingerprint", R.mipmap.fp));
        menus.add(new GridMenu("Enquiry", R.mipmap.profile));
        menus.add(new GridMenu("Register", R.mipmap.user));




        mGridMenuFragment.setupMenu(menus);
    }

    @Override
    public void onBackPressed() {

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            finish();
            startActivity(new Intent(MainMenu.this, Login.class));


    }

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
}
