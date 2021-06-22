package com.onlie.voting.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Pverify_page extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentuser;
    private DatabaseReference myRef;
    FirebaseDatabase database;
    private TextView txt_number;
    private EditText editText_kod;
    private Button btn_onaylayın;
    private ProgressBar progressBar;
    private String verificationid;
    private String phonenumber;
    private TextView txt_error_verify;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pverify_page);

        mAuth = FirebaseAuth.getInstance();
        mCurrentuser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        txt_number = (TextView)findViewById(R.id.txt_number);
        editText_kod = (EditText)findViewById(R.id.editText_kod);
        btn_onaylayın = (Button)findViewById(R.id.btn_onaylayın);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        phonenumber = getIntent().getStringExtra("phoneNumber");
        txt_number.setVisibility(View.VISIBLE);
        String t = phonenumber.substring(0,3);
        String l = phonenumber.substring(3,6);
        String e = phonenumber.substring(6,9);
        String f = phonenumber.substring(9,11);
        String o = phonenumber.substring(11,13);
        txt_number.setText(t+" "+l+" "+e+" "+f+" "+o);
        sendVerificationCode(phonenumber);

        btn_onaylayın.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kod = editText_kod.getText().toString();
                if (kod.isEmpty() || kod.length()<6)
                {
                    Toast.makeText(com.onlie.voting.onlinevotingsystem.Pverify_page.this,"You entered missing code!!", Toast.LENGTH_LONG).show();
                    editText_kod.setHint("Wrong Code");
                    editText_kod.requestFocus();
                    return;
                }
                else if(!kod.isEmpty() && kod.length()==6 )
                {
                    verifyCode(kod);
                    btn_onaylayın.setEnabled(true);
                }
            }
        });

    }
    private void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD, // Activity (for callback binding)
                mCallbacks
        );
    }
    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        singInWithCredential(credential);
    }
    private void singInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {   String uıd = mAuth.getUid();
                          //  myRef=database.getReference("Kullanicilar").child("Bireysel").child(uıd);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        Intent register = new Intent(com.onlie.voting.onlinevotingsystem.Pverify_page.this, MainActivity.class);
                                        startActivity(register);
                                        finish();
                                    }else
                                    {
                                        Toast.makeText(com.onlie.voting.onlinevotingsystem.Pverify_page.this,"Code Approved\n", Toast.LENGTH_SHORT).show();
                                        Intent register = new Intent(com.onlie.voting.onlinevotingsystem.Pverify_page.this, welcomeActivity.class);
                                        register.setFlags(register.FLAG_ACTIVITY_NEW_TASK | register.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(register);
                                        finish();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(com.onlie.voting.onlinevotingsystem.Pverify_page.this,"You entered the wrong verification code!\n", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String smscode = phoneAuthCredential.getSmsCode();
            if (smscode != null)
            {
                editText_kod.setText(smscode);
                verifyCode(smscode);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(com.onlie.voting.onlinevotingsystem.Pverify_page.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
