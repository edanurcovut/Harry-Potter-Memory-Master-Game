package com.harrypottergame.memorymaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AcilisEkrani extends AppCompatActivity {

   //Splash açılış ekranı
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    ProgressBar progressBar;
    CountDownTimer cdt;
    ImageView imageView;
    FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    //
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_acilis_ekrani);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setMax(100);
        imageView=findViewById(R.id.splashscreen);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.harrypotter_acilis_bg));
        int oneMin= 1 * 60 * 50;



          //Firebase kullanıcı giriş olup olmadığı kontrol edilir
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser(); // authenticated user

              cdt = new CountDownTimer(oneMin, 1) {
            public void onTick(long millisUntilFinished) {
                long finishedSeconds = oneMin - millisUntilFinished;
                int total = (int) ((finishedSeconds/ 30)+1 );
                progressBar.setProgress(total);
            }

            public void onFinish() {
                //Kullanıcı oturumu açıksa Oyun seçimi sayfasına yönlenldirilir.

                if(firebaseUser != null){
                    String user=firebaseUser.getEmail();
                    Toast.makeText(getApplicationContext(),user +" \n Hoşgeldin",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AcilisEkrani.this,SingleOrMultiPlayer.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    //Kullanıcı oturumu açık değilse Login sayfasına yönlendirilir

                    Intent i = new Intent(AcilisEkrani.this,LoginActivity.class);
                    startActivity(i);
                    finish();

                }



            }
        }.start();
    }

}