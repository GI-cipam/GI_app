package gov.cipam.gi;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gov.cipam.gi.Common.SharedPref;
import gov.cipam.gi.Model.Users;

public class AccountInfo extends AppCompatActivity {
    private TextView mchangePass,mUpdatePass;
    private TextInputLayout mChangePassFieldLayout;
    private EditText mNameField,mEmailField,mChangePassField;
    private String name,email;
    private DatabaseReference mDatabaseUser;
    private static String TAG = "AccountInfo";
    private static String user_id;
    private FirebaseAuth mAuth;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        mAuth = FirebaseAuth.getInstance();


        mNameField =(EditText) findViewById(R.id.nameField);
        mEmailField =(EditText) findViewById(R.id.emailField);
        mChangePassFieldLayout =(TextInputLayout)findViewById(R.id.changePassFieldLayout);
        mChangePassField =(EditText) findViewById(R.id.changePassField);
        mchangePass =(TextView)findViewById(R.id.changePass);
        mUpdatePass =(TextView)findViewById(R.id.updatePass);

        mchangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangePassFieldLayout.setVisibility(View.VISIBLE);
                mUpdatePass.setVisibility(View.VISIBLE);
            }
        });

        mUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = mChangePassField.getText().toString().trim();

                if(TextUtils.isEmpty(newPassword)){
                    mChangePassField.setError("Enter new Password");}

                else{
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountInfo.this, "Password Successfully Changed",
                                                Toast.LENGTH_SHORT).show();
                                        mChangePassFieldLayout.setVisibility(View.GONE);
                                        mUpdatePass.setVisibility(View.GONE);
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });

                }}
        });


    }

    @Override
    protected void onStart() {
        Users user = SharedPref.getSavedObjectFromPreference(AccountInfo.this,"userinfo","userdata",Users.class);
        if(user!=null) {
            mEmailField.setText(user.getEmail());
            mNameField.setText(user.getName());

        }
        else {
            Toast.makeText(AccountInfo.this,"please Signin again",Toast.LENGTH_LONG).show();
        }

        super.onStart();
    }
}
