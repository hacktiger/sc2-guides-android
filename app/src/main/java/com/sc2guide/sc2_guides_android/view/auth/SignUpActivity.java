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
import com.google.firebase.auth.FirebaseUser;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Intent intent;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //
        mAuth = FirebaseAuth.getInstance();
        editTxtEmail = findViewById(R.id.edit_text_email);
        editTxtPassword = findViewById(R.id.edit_text_password);
        signUpBtn = findViewById(R.id.sign_up_button);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp(v);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSignedClick (View v) {
        intent = new Intent(SignUpActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    private void handleSignUp(View v) {
        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
