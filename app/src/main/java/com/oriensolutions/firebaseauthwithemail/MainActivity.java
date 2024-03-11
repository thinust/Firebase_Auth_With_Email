package com.oriensolutions.firebaseauthwithemail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        EditText emailEditText = findViewById(R.id.editTextText);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i(TAG, "createUserWithEmailAndPassword:success");

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
//                                    updateUI(user);
                                    Toast.makeText(MainActivity.this, "Please verify Your Email", Toast.LENGTH_LONG).show();
                                    findViewById(R.id.button).setVisibility(View.GONE);


                                } else {

                                    Log.w(TAG, "createUserWithEmailAndPassword:failed");
                                    Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null && !user.isEmailVerified()) {
                    Toast.makeText(MainActivity.this, "Please verify Your Email", Toast.LENGTH_LONG).show();

                } else {

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();


                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        updateUI(firebaseAuth.getCurrentUser());
                                    }
                                }
                            });
                }

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            if(user.isEmailVerified()){
                Toast.makeText(MainActivity.this, "Please verify Your Email", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}