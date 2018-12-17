package com.sc2guide.sc2_guides_android.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sc2guide.sc2_guides_android.data.model.User;
import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuthService mAuth;
    private Intent intent;
    private ActionBar ab;
    private ProgressBar progressBar;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private EditText editTxtName;
    private EditText editTxtConfirmPassword;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //
        setUpActionBar();
        setUpVarMap();
        setUpHideKeyBoard();
        // set action of clicking sign up button -> register account
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtn.setBackgroundColor(0xFF888888); // GRAY
                progressBar.setVisibility(View.VISIBLE);
                handleSignUp(v);
            }
        });
    }

    /**
     * @effects: hide key board when click outside of the edit text
     */
    private void setUpHideKeyBoard() {
        editTxtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        editTxtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    /**
     * @effects : hide soft keyboard
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e){
            Log.d("SUAct.hideKB", "mess : " + e.getMessage());
        }
    }

    /**
     * @effects: Navigate to LoginActivity
     * @param v
     */
    public void onLinkLogIn (View v) {
        intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    /**
     * @effects: set up the mapping of variables in layout
     */
    private void setUpVarMap () {
        mAuth = new FirebaseAuthService();
        editTxtEmail = findViewById(R.id.sign_up_email);
        editTxtName = findViewById(R.id.sign_up_name);
        editTxtPassword = findViewById(R.id.sign_up_password);
        editTxtConfirmPassword = findViewById(R.id.sign_up_confirm_password);
        progressBar = findViewById(R.id.sign_up_progress);
        signUpBtn = findViewById(R.id.sign_up_button);
    }

    /**
     * @effects: set up the action bar settings;
     */
    private void setUpActionBar () {
        ab = getSupportActionBar();
        ab.setTitle("Sign up");
        ab.setSubtitle("Join the sc2 guides community");
    }

    /**
     * @effects:
     *      on click sign up button
     *      => call to firebase serivce
     *          success => navigate to MainActivity
     *          fail => Toast.make notice
     * @param v
     */
    private void handleSignUp(View v) {
        String uEmail = editTxtEmail.getText().toString();
        String uName = editTxtName.getText().toString();
        String uPassword = editTxtPassword.getText().toString();
        String uConfirmPassword = editTxtConfirmPassword.getText().toString();
        // TODO: !important MAKE A CONTROLLER FOR THIS OR SOMETHING PLEASE
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = firebaseDatabase.getReference("users");

        boolean checkPassword = validatePassword(uPassword, uConfirmPassword );
        if(!checkPassword) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.zergPurple)); // GRAY
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        try {
            User user = new User(uName, uEmail);
            mDatabase.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    } else {
                        String errMess = task.getException().getMessage();
                        Toast.makeText(SignUpActivity.this, "Error : " + errMess, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, " " +  e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        mAuth.getFirebase().createUserWithEmailAndPassword(uEmail, uPassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            String errMess = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this, "Error : " + errMess, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Something went wrong : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        signUpBtn.setBackgroundColor(getResources().getColor(R.color.zergPurple)); // GRAY
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean validatePassword(String uPassword, String uConfirmPassword) {
        return (uPassword.equals(uConfirmPassword));
    }
}
