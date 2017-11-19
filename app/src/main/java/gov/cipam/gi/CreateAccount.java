package gov.cipam.gi;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gov.cipam.gi.Common.SharedPref;
import gov.cipam.gi.Model.Users;

public class CreateAccount extends AppCompatActivity {
    private EditText mEmailField,mPassField,mNameField;
    private Button mSignupButton;
    private FirebaseAuth mAuth;
    private static String email,password,name;
    private static String TAG="Create Account";
    private DatabaseReference mUsersDatabase,mUserExists;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mEmailField =(EditText)findViewById(R.id.emailField);
        mPassField =(EditText)findViewById(R.id.passField);
        mNameField =(EditText)findViewById(R.id.nameField);

        mSignupButton =(Button)findViewById(R.id.signupButton);

        mProgress = new ProgressDialog(this);

        //--------------------------------------------------------------------------------------------------

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmailField.getText().toString().trim();
                password = mPassField.getText().toString().trim();
                name = mNameField.getText().toString().trim();


                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(name)){

                    if (TextUtils.isEmpty(email)){ mEmailField.setError("Enter Email");}
                    if (TextUtils.isEmpty(password)){ mPassField.setError("Enter Password");}
                    if (TextUtils.isEmpty(name)){ mNameField.setError("Enter Name");}

                }
                else {
                    startSignUp();
                }    }
        });
    }

    private void startSignUp() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateAccount.this, "Successfully Registered.",
                                    Toast.LENGTH_SHORT).show();
                            final String uid = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUser = mUsersDatabase.child(uid);
                            currentUser.child("email").setValue(email);
                            currentUser.child("name").setValue(name);
                            userEmailVerification();
                            Users user = new Users();
                            user.setEmail(email);
                            user.setName(name);
                            SharedPref.saveObjectToSharedPreference(CreateAccount.this,"userinfo","userdata",user);



                            startActivity(new Intent(CreateAccount.this,HomePage.class));




                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccount.this, "Signup failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    public void userEmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateAccount.this, "Verification Email sent",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

}
