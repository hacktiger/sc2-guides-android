package com.sc2guide.sc2_guides_android.view.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Intent intent;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // Firebase stuff
        mAuth = FirebaseAuth.getInstance();

        editTxtEmail = findViewById(R.id.log_in_email);
        editTxtPassword = findViewById(R.id.log_in_password);
        logInBtn = findViewById(R.id.log_in_button);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogIn(v);
            }
        });
    }

    private void handleLogIn(View v) {
        // TODO: add Spinner while loading in here + sign up + other places
        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
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
}
