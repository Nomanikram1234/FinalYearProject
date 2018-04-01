package com.example.nomanikram.epilepsyseizuredetection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // Declaring variables for layout
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;

    // Declaring variables for input fields (Edit text)
    private AppCompatEditText username;
    private AppCompatEditText password;

    // Declaring variables for button
    private AppCompatButton btn_login;
    private AppCompatButton btn_signup;
    private AppCompatButton btn_help_login;

    // Declaring varible for layout;
    private RelativeLayout relative;

    // Declaring variable for View representing Snackbar
    private View view_S;

    // Declaring variable for progress dialog
    ProgressDialog progressDialog;

    // Declaring snackbar
    Snackbar snackbar;

    // Aleart Dialog
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    // Auth Listener to represent either user is authenticated or not
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//      Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//      startActivity(intent);
//      finish();

        // For Demo Login Purpose
        login("jani@gmail.com","Nomi1234");


        // Initializing the variables
        username = (AppCompatEditText) findViewById(R.id.txt_username);
        password = (AppCompatEditText) findViewById(R.id.txt_password);

        usernameLayout = (TextInputLayout) findViewById(R.id.username_textInputLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_textInputLayout);

        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        btn_help_login = (AppCompatButton) findViewById(R.id.btn_help_Login);

        relative = (RelativeLayout) findViewById(R.id.relativeLayout);

        // View variable initialized for snackbar
        view_S = (View) findViewById(R.id.relativeLayout);


        setupAuthStateListener();

        // Set Listener to login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Function called to hide the keyboard
                hideSoftKeyboard();

                /*  boolean variable initialized to ...
                 *  check if : user name field is empty
                 *  check if : password field is empty
                 *  check if : email pattern is correct i.e either it should contain '@' or '.com'
                 */
                boolean is_usernameFields_empty = username.getText().toString().isEmpty();
                boolean is_passwordFields_empty = password.getText().toString().isEmpty();
                boolean email_wrong_pattern = !username.getText().toString().contains("@") || !username.getText().toString().contains(".com");

                // check if username field is empty
                if (is_usernameFields_empty)
                {
                    usernameLayout.setErrorEnabled(true);
                    usernameLayout.setError("Email field is empty");
                }
                // if username is not empty
                else
                {
                    usernameLayout.setErrorEnabled(false);

                    // check if email pattern is wrong
                    if (email_wrong_pattern)
                    {
                        usernameLayout.setErrorEnabled(true);
                        usernameLayout.setError("Email pattern is wrong");
                    } else
                        usernameLayout.setErrorEnabled(false);

                }
                // checck if password field is empty
                if (is_passwordFields_empty) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("Password field is empty");
                }
                else
                    passwordLayout.setErrorEnabled(false);

                // checks if username & password fields are not empty, email pattern is correct
                if (!is_usernameFields_empty && !is_passwordFields_empty && !email_wrong_pattern) {

                    // displays the progress dialog while the user is signing in the application
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setTitle("Signing in");
                    progressDialog.show();

                    // finally login through provided username and password
                    login(username.getText().toString(), password.getText().toString());

                }

            }
        });

        // It will start Register Activity
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        // It will star Login Activity
        btn_help_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(LoginActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.layout_loginpassword_reset, null);
                final AppCompatEditText txt_reset_email;
                AppCompatButton btn_reset;
                builder.setView(view);



                /* *********************************** */
                txt_reset_email = (AppCompatEditText) view.findViewById(R.id.txt_email_reset);
                btn_reset = (AppCompatButton) view.findViewById(R.id.btn_reset_password);

                btn_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftKeyboard();

                        // check if the email field is empty in alert dialog
                        if (!txt_reset_email.getText().toString().isEmpty()) {

                            // sends reset password request to email
                            FirebaseAuth.getInstance().sendPasswordResetEmail(txt_reset_email.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                // initializing the snackbar for reset password
                                                snackbar = Snackbar.make(view_S, "Reset password link sent", Snackbar.LENGTH_SHORT);
                                                View v2 = snackbar.getView();
                                                v2.setBackgroundColor(getResources().getColor(R.color.colorSnackbarBackgroundSuccess));
                                                TextView txt = (TextView) v2.findViewById(android.support.design.R.id.snackbar_text);
                                                txt.setTextColor(getResources().getColor(R.color.colorSnackbarText));
//                        hideSoftKeyboard();
                                                snackbar.show();

                                                alertDialog.hide();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    // initializing snackbar for operation failed
                                    snackbar = Snackbar.make(view_S, "Operation Failed ", Snackbar.LENGTH_SHORT);
                                    View v2 = snackbar.getView();
                                    v2.setBackgroundColor(getResources().getColor(R.color.colorSnackbarBackgroundFailure));
                                    TextView txt = (TextView) v2.findViewById(android.support.design.R.id.snackbar_text);
                                    txt.setTextColor(getResources().getColor(R.color.colorSnackbarText));
                                    snackbar.show();

                                }
                            });
                        }
                        Log.w("", "Reset Button Pressed");
                    }
                });

                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // to remove cursor from the input fields
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

        if (mAuthListener != null)
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);

    }

    private void setupAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    Log.w("", "Signed in: " + user.getUid());
                else
                    Log.w("", "Signed out!");

            }
        };
    }


    private void login(String email, String password) {

        // Firebase login function
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Initializing the snackbar for failure condition
                snackbar = Snackbar.make(view_S, "Login Failure", Snackbar.LENGTH_SHORT);


                View v = snackbar.getView();
                v.setBackgroundColor(getResources().getColor(R.color.colorSnackbarBackgroundFailure));

                TextView txt = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
                txt.setTextColor(getResources().getColor(R.color.colorSnackbarText));

                hideSoftKeyboard();

                snackbar.show();

                Log.w("Tag", "Login Failure!");


            }
        });

    }


    // function to hide keyboard from the screen
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        //to hide keyboard
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        /* to show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
         */
    }

}
