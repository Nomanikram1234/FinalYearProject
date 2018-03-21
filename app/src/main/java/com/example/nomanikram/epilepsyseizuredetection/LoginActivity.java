package com.example.nomanikram.epilepsyseizuredetection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private View view;

    ProgressDialog progressDialog;

    Snackbar snackbar_loginfailure;

    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(intent);
//        finish();


        username = (AppCompatEditText) findViewById(R.id.txt_username);
        password = (AppCompatEditText) findViewById(R.id.txt_password);

        usernameLayout = (TextInputLayout) findViewById(R.id.username_textInputLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_textInputLayout);

        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);

        relative= (RelativeLayout) findViewById(R.id.relativeLayout);

        view= (View) findViewById(R.id.relativeLayout);

        setupAuthStateListener();






        // Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_usernameFields_empty = username.getText().toString().isEmpty() ;
                boolean is_passwordFields_empty = password.getText().toString().isEmpty();
                boolean email_wrong_pattern = !username.getText().toString().contains("@") || !username.getText().toString().contains(".com");

                if(is_usernameFields_empty)
                {
                    usernameLayout.setErrorEnabled(true);
                    usernameLayout.setError("Email field is empty");
                }
                else
                {
                    usernameLayout.setErrorEnabled(false);

                    if(email_wrong_pattern)
                    {
                        usernameLayout.setErrorEnabled(true);
                        usernameLayout.setError("Email pattern is wrong");
                    }
                    else
                        usernameLayout.setErrorEnabled(false);

                }
                if(is_passwordFields_empty)
                {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Password field is empty");
                }
                else
                    passwordLayout.setErrorEnabled(false);



                if( !is_usernameFields_empty && !is_passwordFields_empty && !email_wrong_pattern)
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
              snackbar_loginfailure =  Snackbar.make(view,"Login Failure",Snackbar.LENGTH_SHORT);

              View v = snackbar_loginfailure.getView();
              v.setBackgroundColor(getResources().getColor(R.color.colorSnackbarBackgroundFailure));

              TextView txt = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
              txt.setTextColor(getResources().getColor(R.color.colorSnackbarText));

              hideSoftKeyboard();

              snackbar_loginfailure.show();
              progressDialog.hide();
              Log.w("Tag","Login Failure!");


            }
        });

    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        //to hide keyboard
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        /* to show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
         */
    }

}
