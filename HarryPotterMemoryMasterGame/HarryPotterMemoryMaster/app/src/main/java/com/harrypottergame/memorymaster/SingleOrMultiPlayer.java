package com.harrypottergame.memorymaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SingleOrMultiPlayer extends AppCompatActivity {
    Button onlyPlayer,multiPlayer,oturumuSonlandir;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_or_multi_player);
        onlyPlayer=findViewById(R.id.tekoyuncu);
        multiPlayer=findViewById(R.id.multioyuncu);
        oturumuSonlandir=findViewById(R.id.oturumuSonlandir);

        MainActivity.surekliSes= MediaPlayer.create(this, R.raw.surekli);

        MainActivity.surekliSes.start();

        firebaseAuth=FirebaseAuth.getInstance();
        oturumuSonlandir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent multiplayerIntent=new Intent(SingleOrMultiPlayer.this, AcilisEkrani.class);

                multiplayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(multiplayerIntent);
            }
        });


        //Tek oyunuculu modu seçmek için
        onlyPlayer.setOnClickListener(view -> {
            if(MainActivity.zamansayici!=null){
                MainActivity.zamansayici.cancel();
            }
            Intent onlyplayerIntent=new Intent(SingleOrMultiPlayer.this,MainActivity.class);
            onlyplayerIntent.putExtra("GAME_MODE","ONLY_PLAYER");

            onlyplayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(onlyplayerIntent);
        });


        //Çok oyunculu modu seçmek için kullanılır
        multiPlayer.setOnClickListener(view -> {
            if(MultiPlayerActivity.zamansayici!=null){
                MultiPlayerActivity.zamansayici.cancel();
            }
            Intent multiplayerIntent=new Intent(SingleOrMultiPlayer.this,MultiPlayerActivity.class);
            multiplayerIntent.putExtra("GAME_MODE","MULTI_PLAYER");
            multiplayerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(multiplayerIntent);
        });
    }
}