package com.sc2guide.sc2_guides_android.view.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;
import com.sc2guide.sc2_guides_android.controller.FirebaseController;

public class LogInActivity extends AppCompatActivity {
    private FirebaseController mFirebaseController;

    private Intent intent;
    private ActionBar ab;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private Button logInBtn;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Firebase controller
        mFirebaseController = new FirebaseController();
        // Action Bar
        setUpActionBar();
        setUpVarMap ();
        setUpHideKeyBoard();
        //
        logInBtn.setOnClickListener(v -> handleLogIn(v));
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
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mFirebaseController.currentUser() != null) {
            intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpVarMap () {
        editTxtEmail = findViewById(R.id.log_in_email);
        editTxtPassword = findViewById(R.id.log_in_password);
        logInBtn = findViewById(R.id.log_in_button);
        spinner = findViewById(R.id.log_in_progress);
    }

    private void setUpActionBar () {
        ab = getSupportActionBar();
        assert ab != null; //
        ab.setTitle("Log In");
        ab.setSubtitle("Login to start exploring sc2 guides");
    }

    public void updateUI() {
        logInBtn.setBackgroundColor(Color.GRAY);
        spinner.setVisibility(View.VISIBLE);
    }

    public void handleLogIn(View v) {
        // Actual log in
        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();
        if ( email.isEmpty() || password.isEmpty() ) {
            Toast.makeText(this, "Dont leave the fields empty", Toast.LENGTH_SHORT).show();
            return;
        }
        updateUI();
        // sign in with firebase
        mFirebaseController.signInWithEmailAndPassword(email, password,
                task -> {
                    if (task.isSuccessful()) {
                        intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        logInBtn.setBackgroundColor(Color.GREEN); // change to holo purple later
                        spinner.setVisibility(View.INVISIBLE);
                        Toast.makeText(LogInActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onLinkSignUp(View v) {
        intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
