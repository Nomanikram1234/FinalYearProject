package com.example.nomanikram.epilepsyseizuredetection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {

    private  AppCompatEditText email;
    private  AppCompatEditText password;
    private  AppCompatEditText confirm_password;

    private TextInputLayout textInputLayout_email;
    private TextInputLayout textInputLayout_password;
    private TextInputLayout textInputLayout_confirm_password;

    private  Button btn_register;

    private  RelativeLayout relativeLayout;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (AppCompatEditText) findViewById(R.id.txt_email);
        password = (AppCompatEditText) findViewById(R.id.txt_password);
        confirm_password = (AppCompatEditText) findViewById(R.id.txt_confirm_password);

        textInputLayout_email = (TextInputLayout) findViewById(R.id.textInputLayout_email);
        textInputLayout_password = (TextInputLayout) findViewById(R.id.textInputLayout_password);
        textInputLayout_confirm_password = (TextInputLayout) findViewById(R.id.textInputLayout_confirm_password);

        btn_register = (Button) findViewById(R.id.btn_registration);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener(null);

        mAuth = FirebaseAuth.getInstance();
        Task<ProviderQueryResult> check;



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            boolean password_match = password.getText().toString().matches(confirm_password.getText().toString());
            boolean password_length= password.getText().toString().length() >= 8;
            boolean empty_fields = (email.getText().toString().isEmpty()|| password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty());

            // check if the fields are empty
            if(empty_fields)
            {
                // check email field is empty
                if(email.getText().toString().isEmpty())
                {
                    textInputLayout_email.setErrorEnabled(true);
                    textInputLayout_email.setError("email field is empty");
                }
                else
                    textInputLayout_email.setErrorEnabled(false);

                // check if password field is empty
                if(password.getText().toString().isEmpty())
                {
                    textInputLayout_password.setErrorEnabled(true);
                    textInputLayout_password.setError("Password field is empty");
                }
                else
                    textInputLayout_password.setErrorEnabled(false);

                // check if confirm password field is empty
                if(confirm_password.getText().toString().isEmpty())
                {
                    textInputLayout_confirm_password.setErrorEnabled(true);
                    textInputLayout_confirm_password.setError("Confirm password field is empty");
                }
                else
                    textInputLayout_confirm_password.setErrorEnabled(false);
            }
            else
            {
                // disable all error msgs enabled
                textInputLayout_email.setErrorEnabled(false);
                textInputLayout_password.setErrorEnabled(false);
                textInputLayout_confirm_password.setErrorEnabled(false);

                // check if password length is greater or equal to 8
                if (password_length)
                {
                    // check if both passwords are same
                    if (password_match)
                    {
                        progressDialog = new ProgressDialog(RegisterActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setTitle("Registering");
                        progressDialog.show();

                        register_user(email.getText().toString(), password.getText().toString());
                        Log.w("TAG", "Password matched");
                    }
                    else
                    {
                        textInputLayout_confirm_password.setErrorEnabled(true);
                        textInputLayout_confirm_password.setError("Password not matched");
                        Log.w("TAG", "Password not matched");
                    }
                }
                else
                {
                        textInputLayout_password.setErrorEnabled(true);
                        textInputLayout_password.setError("Password length do not match");
                        Log.w("Tag", "Password length do not match");
                }
            }
            }

        });
    }


    private void register_user(String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.w("Tag", "Registered");
                    Intent intent = new Intent(getApplicationContext(),ProfileDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Log.w("Tag", "Not Registered");
            }
        });
    }
}