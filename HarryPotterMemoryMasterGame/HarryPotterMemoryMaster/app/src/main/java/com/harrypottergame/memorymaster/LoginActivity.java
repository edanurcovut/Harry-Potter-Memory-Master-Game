package com.harrypottergame.memorymaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView textKaydol,textSifremiUnuttum;
    EditText kullaniciAdiText,kullaniciSifreText;
    RelativeLayout btnGiris;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kullaniciAdiText=findViewById(R.id.kullanici_adi);
        kullaniciSifreText=findViewById(R.id.sifre);
        textKaydol=findViewById(R.id.txtKayitOl);
        textSifremiUnuttum=findViewById(R.id.txtSifremiUnuttum);
        btnGiris=findViewById(R.id.girisBtn);
        builder = new AlertDialog.Builder(this);


        //FirebaseAuth nesnesi oluştur.Kayıt işlemleri bu nesne üzerinden yapılır.
        mAuth = FirebaseAuth.getInstance();

        textKaydol.setOnClickListener(view -> {
            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);
        });

        textSifremiUnuttum.setOnClickListener(view -> {
            Intent intent=new Intent(this,ChangePassword.class);
            startActivity(intent);
        });

        btnGiris.setOnClickListener(view -> {
            mAuth.signInWithEmailAndPassword(kullaniciAdiText.getText().toString(),
                            kullaniciSifreText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // Oturum açma başarılı ise Oyun modu seçme sayfasına yönlendirme
                                Intent intent=new Intent(LoginActivity.this,SingleOrMultiPlayer.class);
                                startActivity(intent);
                                finish();

                            }

                            else {
                                // Oturum açma başarılı değil ise uyarı ver
                                builder.setTitle("Uyarı");
                                builder.setMessage("Email veya parola hatalı");
                                builder.setPositiveButton("Tamam",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        kullaniciAdiText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_login_bg));
                                        kullaniciSifreText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_login_bg));

                                    }
                                });
                                builder.show();
                            }
                        }
                    });
        });
    }
}