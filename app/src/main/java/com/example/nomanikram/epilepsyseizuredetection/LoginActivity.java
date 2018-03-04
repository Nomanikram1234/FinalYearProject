package com.example.nomanikram.epilepsyseizuredetection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;

    private AppCompatEditText username;
    private AppCompatEditText password;

    private AppCompatButton btn_login;
    private AppCompatButton btn_signup;

    private RelativeLayout relative;

    ProgressDialog progressDialog;

    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(getApplicationContext(),BluetoothConnectionActivity.class);
        startActivity(intent);
        finish();


        username = (AppCompatEditText) findViewById(R.id.txt_username);
        password = (AppCompatEditText) findViewById(R.id.txt_password);

        usernameLayout = (TextInputLayout) findViewById(R.id.username_textInputLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_textInputLayout);

        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);

        relative= (RelativeLayout) findViewById(R.id.relativeLayout);


        setupAuthStateListener();






        // Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_Fields_empty = username.getText().toString().isEmpty() || password.getText().toString().isEmpty();

                if(is_Fields_empty)
                {
                    // check if username field is empty
                    if(username.getText().toString().isEmpty())
                    {
                        usernameLayout.setErrorEnabled(true);
                        usernameLayout.setError("Email field is empty");
                    }
                    else
                        usernameLayout.setErrorEnabled(false);

                    // check if password field is empty
                    if(password.getText().toString().isEmpty())
                    {
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("Password Field is empty");
                    }
                    else
                        passwordLayout.setErrorEnabled(false);

                    Log.w("Tag","Fields are empty");


                }
                else
                {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Signing in");
                    progressDialog.show();
//                    progressDialog.hide();
                    login(username.getText().toString(), password.getText().toString());


                }
            }
        });


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        relative.setOnClickListener(null);
    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null)
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);

    }

    private void setupAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                    Log.w("","Signed in: "+user.getUid());
                else
                    Log.w("","Signed out!");

            }
        };
    }

    private void login(String email,String password){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                }
              //  finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Tag","Login Failure!");
            }
        });

    }

}
