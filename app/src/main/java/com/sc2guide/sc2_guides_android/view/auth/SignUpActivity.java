package com.sc2guide.sc2_guides_android.view.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.MainActivity;
import com.sc2guide.sc2_guides_android.R;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuthService mAuth;
    private Intent intent;
    private ActionBar ab;

    private EditText editTxtEmail;
    private EditText editTxtPassword;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //
        setUpActionBar();
        //
        setUpVarMap();
        // set action of clicking sign up button -> register account
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp(v);
            }
        });
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
        editTxtPassword = findViewById(R.id.sign_up_password);
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
        String email = editTxtEmail.getText().toString();
        String password = editTxtPassword.getText().toString();
        mAuth.getFirebase().createUserWithEmailAndPassword(email, password)
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
