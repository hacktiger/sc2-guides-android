package com.sc2guide.sc2_guides_android.view.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuthService mAuth;

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
        // Firebase stuff
        mAuth = new FirebaseAuthService();
        // Action Bar
        // TODO: Right way to get action bar title
        setUpActionBar();
        //
        setUpVarMap ();
        //
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
                handleLogIn(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.currentUser() != null) {
            intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed () {
        // do nothing
    }

    private void setUpVarMap () {
        editTxtEmail = findViewById(R.id.log_in_email);
        editTxtPassword = findViewById(R.id.log_in_password);
        logInBtn = findViewById(R.id.log_in_button);
        spinner = findViewById(R.id.log_in_spinner);
    }

    private void setUpActionBar () {
        ab = getSupportActionBar();
        ab.setTitle("Log In");
        ab.setSubtitle("Login to start exploring sc2 guides");
    }

    public void updateUI() {
        logInBtn.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.VISIBLE);
    }

    public void handleLogIn(View v) {
        // TODO: add Spinner while loading in here + sign up + other places

        // Actual log in
        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();
        // sign in with firebase
        mAuth.getFirebase().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onLinkSignUp(View v) {
        intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
