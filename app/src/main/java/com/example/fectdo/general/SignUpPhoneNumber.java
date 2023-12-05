package com.example.fectdo.general;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.fectdo.R;
import com.hbb20.CountryCodePicker;

public class SignUpPhoneNumber extends AppCompatActivity {
    
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_phone_number);

        countryCodePicker = findViewById(R.id.countryCode);
        phoneInput = (EditText) findViewById(R.id.inputPhone);
        signUpButton = findViewById(R.id.signupBtn);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        signUpButton.setOnClickListener((view)->{
            if(!countryCodePicker.isValidFullNumber()){
                phoneInput.setError("Phone Number is Not Valid.");
                return;
            }
            Intent intent = new Intent(SignUpPhoneNumber.this, SignUpOTP.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}