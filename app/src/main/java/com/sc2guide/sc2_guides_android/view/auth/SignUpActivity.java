package com.sc2guide.sc2_guides_android.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;
import com.sc2guide.sc2_guides_android.data.model.User;
import com.sc2guide.sc2_guides_android.view.MainActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseController mFirebaseController;
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
                updateUI(R.color.btn_inactive, true);
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
        mFirebaseController = new FirebaseController();
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
        // Validation for the password
        String checkPasswordResult = validatePassword(uPassword, uConfirmPassword );
        if(!checkPasswordResult.equals("OK")) {
            Toast.makeText(this, checkPasswordResult, Toast.LENGTH_SHORT).show();
            updateUI(R.color.zergPurple, false);
            return;
        }
        // if password is validated then sign up the user
        signUpUser(uName, uEmail, uPassword);
    }

    /**
     * @effects: change confirm button color and progress bar visibility
     * @param btnColor : int Resources
     * @param isVisible true: visible/ false: not
     */
    private void updateUI(int btnColor, boolean isVisible){
        signUpBtn.setBackgroundColor(getResources().getColor(btnColor));
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @effects: sign up user using firebase authentication API
     * @param uName
     * @param uEmail
     * @param uPassword
     */
    private void signUpUser(String uName, String uEmail, String uPassword) {
        mFirebaseController.createUserWithEmailAndPassword(uEmail, uPassword,
                task -> {
                    if (task.isSuccessful()) {
                        insertUserToDB(uName, uEmail); // Log the user to the real time database
                        // Sign in success, start main activity with the signed-in user's information
                        intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        String errMess = task.getException().getMessage();
                        Toast.makeText(SignUpActivity.this, "Error : " + errMess, Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    Toast.makeText(SignUpActivity.this, "Something went wrong : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * @effects: insert user to firebase realtime database
     * @param uName
     * @param uEmail
     */
    private void insertUserToDB(String uName, String uEmail) {
        try {
            User user = new User(uName, uEmail);
            mFirebaseController.insertUser(user, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                } else {
                    String errMess = task.getException().getMessage();
                    Toast.makeText(SignUpActivity.this, "Error : " + errMess, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, " " +  e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String validatePassword(String uPassword, String uConfirmPassword) {
        if (!uPassword.equals(uConfirmPassword)){
            return "Password must match";
        }
        if (uPassword.length() < 6){
            return "Password must contain more than 5 characters";
        }
        return "OK";
    }
}
