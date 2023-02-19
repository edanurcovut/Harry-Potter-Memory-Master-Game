package com.harrypottergame.memorymaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    FirebaseUser user;
    EditText kullaniciMail, kullaniciEskiSifre, kullaniciYeniSifre;
    Button sifreDegistir;
    AlertDialog.Builder builder;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        user = FirebaseAuth.getInstance().getCurrentUser();
        kullaniciMail = findViewById(R.id.kullanici_mail);
        firebaseAuth = FirebaseAuth.getInstance();
        kullaniciEskiSifre = findViewById(R.id.txtEskiSifre);
        kullaniciYeniSifre = findViewById(R.id.yeniSifre);
        sifreDegistir = findViewById(R.id.buttonSifreDegis);
        builder = new AlertDialog.Builder(this);

        sifreDegistir.setOnClickListener(view -> {
            String email = kullaniciMail.getText().toString();
            String kullaniciEskiParola = kullaniciEskiSifre.getText().toString();
            if (!email.equals("") && email != null && !kullaniciEskiSifre.equals("") && kullaniciEskiSifre != null) {
                firebaseAuth.signInWithEmailAndPassword(kullaniciMail.getText().toString(),
                                kullaniciEskiSifre.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    // once oturum açıp sonra şifre değiştiriyoruz
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(kullaniciMail.getText().toString(), kullaniciEskiSifre.getText().toString());

                                    // Prompt the user to re-provide their sign-in credentials
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updatePassword(kullaniciYeniSifre.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    builder.setTitle("Bilgi");
                                                                    builder.setMessage("Şifreniz değiştirilmiştir.");
                                                                    builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            Intent intent = new Intent(ChangePassword.this, AcilisEkrani.class);
                                                                            startActivity(intent);
                                                                            finish();

                                                                        }
                                                                    });
                                                                    builder.show();


                                                                } else {
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }

        });
    }
}