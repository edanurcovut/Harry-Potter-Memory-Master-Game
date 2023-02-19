package com.harrypottergame.memorymaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView textKaydol, textSifremiUnuttum;
    EditText kullaniciAdiText, kullaniciSifreText,kullaniciMail;
    RelativeLayout btnKayit;
    FirebaseAuth getmAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
       builder = new AlertDialog.Builder(this);

        kullaniciAdiText = findViewById(R.id.kullanici_adi);
        kullaniciSifreText = findViewById(R.id.sifre);
        kullaniciMail = findViewById(R.id.kullanici_mail);
        btnKayit = findViewById(R.id.kayitBtn);


        btnKayit.setOnClickListener(view -> {
            String userName=kullaniciAdiText.getText().toString();
            String userPassword=kullaniciSifreText.getText().toString();
            String userMail=kullaniciMail.getText().toString();

            if (userName.isEmpty() || userPassword.isEmpty()|| userMail.isEmpty() || !userMail.contains("@") || !userMail.contains(".") || userPassword.length()<6 || userPassword.length()>12)
            {

                kullaniciAdiText.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_login_error_bg));
                kullaniciSifreText.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_login_error_bg));
                builder.setTitle("Uyarı");
                builder.setMessage("Kullanıcı adı, parola veya mail hatalı");
                builder.setPositiveButton("Tamam",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        kullaniciAdiText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_login_bg));
                        kullaniciSifreText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_login_bg));

                    }
                });
                builder.show();

            } else {
                mAuth.createUserWithEmailAndPassword(userMail,userPassword)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                   String userUid= task.getResult().getUser().getUid();

                                    HashMap<String, String> userMap = new HashMap<String, String>();
                                    userMap.put("user_email", userMail);
                                    userMap.put("user_name", userName);
                                    userMap.put("user_password", userPassword);

                                    myRef.child(userUid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                builder.setTitle("Bilgi");
                                                builder.setMessage("Kullanıcı kaydınız başarıyla gerçekleşmiştir.");
                                                builder.setPositiveButton("Tamam",new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                });
                                                builder.show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });                                }
                                else{
                                    //Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



               // registerFunc();
            }

        });


    }


}
