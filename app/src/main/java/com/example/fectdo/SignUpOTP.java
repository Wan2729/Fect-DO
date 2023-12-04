package com.example.fectdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fectdo.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SignUpOTP extends AppCompatActivity {

    String phoneNumber;
    EditText otpInput;
    Button otpBtn;
    TextView resendOtpTextview;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    long timeout = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_otp);

        otpInput = findViewById(R.id.otp);
        otpBtn = findViewById(R.id.otpBtn);
        resendOtpTextview = findViewById(R.id.resendOtp);
        resendOtpTextview.setText("Hello");

        phoneNumber = getIntent().getExtras().getString("phone");
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();

        sendOtp(phoneNumber,false);

        otpBtn.setOnClickListener(v->{
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,enteredOtp);
            signIn(credential);
        });

        resendOtpTextview.setOnClickListener(v ->{
            sendOtp(phoneNumber,true);
        });
    }

    void sendOtp(String phoneNumber, boolean isResend){
        startResendTimer();
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeout, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(),"OTP sent successfully");
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){
        //login and go to next activity
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification success");
                    Intent intent = new Intent(SignUpOTP.this,SignUpUserrname.class);
                    startActivity(intent);
                }else {
                    AndroidUtil.showToast(getApplicationContext(),"OTP verification failed");
                }
            }
        });
    }

    void startResendTimer(){
        resendOtpTextview.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeout--;
                String text = "Resend OTP in "+timeout+" seconds";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resendOtpTextview.setText(text);
                        if(timeout<=0){
                            timeout=60L;
                            timer.cancel();
                            resendOtpTextview.setEnabled(true);
                        }
                    }
                });
            }
        },0,1000);
    }
}