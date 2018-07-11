package com.simcarddemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Login extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    private ProgressDialog pDialog;
    String Login = "http://aril.16mb.com/Image/getDocno.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText)findViewById(R.id.input_email);
        inputPassword = (EditText)findViewById(R.id.input_password);
        btnLogin = (Button)findViewById(R.id.btn_login);

        //Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //Check for empty data in the form
                if(!email.isEmpty() && !password.isEmpty())
                {
                    //login user
                    checkLogin(email, password);
                }
                else
                {
                    //Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkLogin(final String email, final String password)
    {
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        pDialog.show();

        if(email.equals("admin") && password.equals("123"))
        {
            pDialog.dismiss();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());

            Intent intent = new Intent(Login.this, MainMenu.class);
            intent.putExtra("Username", "admin");
            intent.putExtra("LoginTime", strDate);
            startActivity(intent);
            finish();
        }
        else
        {
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Incorrect UserName or Password", Toast.LENGTH_LONG).show();
        }

    }
}
