package gov.cipam.gi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gov.cipam.gi.Common.SharedPref;
import gov.cipam.gi.Model.Users;

public class SignIn extends AppCompatActivity {
    private TextView mCreateAccount,mForgotPass;
    private EditText mEmailField,mPassField;
    private Button mSignInButton;
    private FirebaseAuth mAuth;
    private String email,password;
    private static String TAG="SignIn";
    private DatabaseReference mDatabaseUser;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        mPrefs = getPreferences(MODE_PRIVATE);


        mCreateAccount =(TextView)findViewById(R.id.createAccText);
        mForgotPass =(TextView)findViewById(R.id.forgotPass);

        mEmailField =(EditText)findViewById(R.id.emailField);
        mPassField =(EditText)findViewById(R.id.passField);
        mSignInButton =(Button)findViewById(R.id.signinButton);

        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ForgotPassword();
            }
        });
        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,CreateAccount.class));
            }
        });


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmailField.getText().toString().trim();
                password = mPassField.getText().toString().trim();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    if (TextUtils.isEmpty(email)){
                        mEmailField.setError("Enter Email");
                    }
                    if(TextUtils.isEmpty(password)){
                        mPassField.setError("Enter Password");
                    }

                }
                else{startSignIn();}

            }
        });


    }

    private void ForgotPassword() {
        email = mEmailField.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            mEmailField.setError("Enter Email");
            Toast.makeText(SignIn.this, "Enter Email",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            mEmailField.setError("Enter Email");
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignIn.this, "password reset email sent ",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }
    }

    private void startSignIn() {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignIn.this, "Success",
                                    Toast.LENGTH_SHORT).show();

                            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference current_user = mDatabaseUser.child(user_id);
                            current_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Users user = dataSnapshot.getValue(Users.class);
                                    SharedPref.saveObjectToSharedPreference(SignIn.this,"userinfo","userdata",user);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            startActivity(new Intent(SignIn.this,HomePage.class));


                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            startActivity(new Intent(SignIn.this,HomePage.class));
        }
        super.onStart();
    }
}
