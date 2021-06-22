package com.onlie.voting.onlinevotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;


public class Phone_page extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentuser;

    private CountryCodePicker cpp ;
    private EditText txt_phone;
    private Button save_phone_number;
    private TextView mesaj;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_page);
        mAuth = FirebaseAuth.getInstance();
        mCurrentuser = mAuth.getCurrentUser();

        save_phone_number = (Button)findViewById(R.id.btn_next_phone);
        cpp = (CountryCodePicker) (findViewById(R.id.cpp_codepicker));
        txt_phone =(EditText)findViewById(R.id.edtxt_phone_number);


        save_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = cpp.getSelectedCountryCodeWithPlus();
                String number =  txt_phone.getText().toString();
                phoneNumber = code +""+number;

                if (txt_phone.getText().toString().startsWith("0"))
                {

                    Toast.makeText(com.onlie.voting.onlinevotingsystem.Phone_page.this,"Do not put 0 in front of the number!\n", Toast.LENGTH_LONG).show();
                    txt_phone.getText().clear();

                }
                else if (txt_phone.length() <10)
                {

                    Toast.makeText(com.onlie.voting.onlinevotingsystem.Phone_page.this,"You must enter a number of valid length!", Toast.LENGTH_LONG).show();
                    txt_phone.getText().clear();

                }
                if(!txt_phone.toString().isEmpty() && txt_phone.length() == 10 ) {
                    save_phone_number.setEnabled(true);
                    Intent intent = new Intent(com.onlie.voting.onlinevotingsystem.Phone_page.this, com.onlie.voting.onlinevotingsystem.Pverify_page.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                }
            }
        });

    }
    protected void onStart()
    {
        super.onStart();
        if (mCurrentuser != null)
        {
            Intent intent = new Intent(com.onlie.voting.onlinevotingsystem.Phone_page.this,MainActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}



