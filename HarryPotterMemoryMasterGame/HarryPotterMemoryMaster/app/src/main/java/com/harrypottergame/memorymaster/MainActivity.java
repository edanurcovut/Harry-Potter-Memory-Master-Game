package com.harrypottergame.memorymaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    List<KartModel> tumKartListesi;
    List<KartModel>rastgeleSecilenKartListesi;

    KartModel oyuncununSectigiIlkKart;
    KartModel oyuncununSectigiIkinciKart;
    TextView txtSayac,txtPuan;
    FirebaseDatabase database;
    RecyclerView recyclerView;
    ConstraintLayout constraintList;
    KartAdapter kartAdapter;
    String secilenKardAdi;
    Random random;
    Button button;
    ArrayList<Integer> randomNumbers;
    ArrayList<Integer> randomNumbers2;
    ImageView secilenKartResim;
    static int eslestirilmisResimSayisi=0;
    int secimSayisiGenel=0;
     double puanmain=0;
    static int zamanSayac;
    static int time=0;
    public static MediaPlayer surekliSes;
    MediaPlayer sureBittiSes,dogruSes,sureBitmedenBulundu;
    int randomPozisyon;
    ImageView birOncekisecilenresim;
    public static  CountDownTimer zamansayici;

    RelativeLayout relativeLayout;

    ArrayList<ImageView> secilmisResimler;



    int sayiciSecim=0;

    ArrayList<ImageView> arrayListImg;

    Animation animFadeIn,animFadeOut,animBlink,animZoomIn,animZoomOut,animRotate
            ,animMove,animSlideUp,animSlideDown,animBounce,animSequential,animTogether,
            animCrossFadeIn,animCrossFadeOut,zoomInOut,animScale,animAlfa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        tumKartListesi = new ArrayList<KartModel>();
        tumKartListesi.clear();

        sureBittiSes=MediaPlayer.create(this,R.raw.sure_bitti);
        dogruSes=MediaPlayer.create(this,R.raw.dogru_kart);
        sureBitmedenBulundu=MediaPlayer.create(this,R.raw.sure_bitmeden_bulundu);

        constraintList=findViewById(R.id.constraintListKendim);
        arrayListImg=new ArrayList<>();
        button=findViewById(R.id.button);
        button.setStateListAnimator(null);

        recyclerView = findViewById(R.id.recylerview);
        secilenKartResim=findViewById(R.id.secilen_resim_image);
        relativeLayout=findViewById(R.id.relativeLayout);


        secilmisResimler=new ArrayList<ImageView>();

        //Animasyon yakınlaştır ve uzaklaştır

        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animCrossFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        animCrossFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.blink);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.rotate);
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.move);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(),   R.anim.bounce);
        animSequential = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.sequential);
        animTogether = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.together);
        zoomInOut = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.zoom_in_out);
        animAlfa = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.blink);
        animScale = AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.scale);

        tumKartListesi =kartlariGetir();
        random=new Random();
        randomNumbers=new ArrayList<Integer>();
        randomNumbers2=new ArrayList<Integer>();
        randomNumbers.add(-1);
        randomNumbers2.add(-1);

        if(getIntent().getExtras()!=null) {
            String gameMode = getIntent().getExtras().getString("GAME_MODE", null);
        }
        recyclerView.setHasFixedSize(true);
        txtSayac = findViewById(R.id.txt_sayac);
        txtPuan=findViewById(R.id.txt_puan);


    }

    //Oyun açılışında tüm kartları getirmek için kullanılan metot
    private List<KartModel> kartlariGetir() {
        DatabaseReference ref = database.getReference("cards");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot cards : dataSnapshot.getChildren()) {
                        KartModel kartModel = cards.getValue(KartModel.class);
                        tumKartListesi.add(kartModel);
                    }

                }
                Log.d("deneme","Çekilen kartların tamamı: "+ tumKartListesi.size());
                onCreateDialog().show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        return tumKartListesi;
    }


    //oyun süresini ayarlamak için kullanılan metot

    private void sayaciBaslat(int i, TextView textView) {
        i=i*1000;
         zamansayici= new  CountDownTimer(i, 1000) {

           public void onTick(long millisUntilFinished) {
                textView.setText(String.valueOf(millisUntilFinished / 1000));
                zamanSayac=((int) (millisUntilFinished / 1000));
            }

            public void onFinish() {
                txtSayac.setText("0");
                zamansayici.cancel();

                    dialogBilgi(R.layout.dialog_genel, "Süre Doldu", new DecimalFormat("##").format(puanmain)).show();

                if(eslestirilmisResimSayisi<randomNumbers.size()/2)
                {
                    sureBittiSes.start();
                    zamansayici.cancel();

                }
            }
        }.start();

    }

    //oyun zorluk seçimini yapmak için kullanılan metot

    public Dialog onCreateDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_choose_game);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button button2X2=dialog.findViewById(R.id.btntwotwo);
        Button button4X4=dialog.findViewById(R.id.btnfourfour);
        Button button6X6=dialog.findViewById(R.id.btnsixsix);

        button2X2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
               oyunaBasla(4);

            }
        });


        button4X4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                oyunaBasla(16);

            }
        });

        button6X6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                oyunaBasla(36);

            }
        });

        return dialog;
    }



//oyun bittiğinde puanı göstermek için kullanılan metot
    public Dialog dialogBilgi(int layout,String title,String puan) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView textTitle=dialog.findViewById(R.id.txtinfo);
        textTitle.setText(title);
        TextView txt=dialog.findViewById(R.id.txtpuandialog);
        txt.setText(puan);

        Button kapat=dialog.findViewById(R.id.kapat);
        kapat.setText("Kapat");


        kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eslestirilmisResimSayisi=0;
                dialog.dismiss();
                zamansayici.cancel();
                Intent intent=new Intent(MainActivity.this,SingleOrMultiPlayer.class);
                startActivity(intent);
                finish();
            }
        });

        // Add action buttons

        return dialog;
    }


    //oyun zorluk seçiminden sonra oyuna başlamak için kullanılan metottur.
    private void oyunaBasla(int i) {
        int layout;
        time=45;
        sayaciBaslat(time,txtSayac);
        rastgeleSecilenKartListesi=new ArrayList<KartModel>();
        ArrayList<Integer> randomSayilar=randomSayiUret(i,44);
        ArrayList<Integer> randomSayilar2=randomSayiUret2(i/2,i/2);

        if(i==4)
        {
            layout=R.layout.kart_item_layout;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else if(i==16)
        {
            layout=R.layout.kart_item_layout_four_four;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        else{
            layout=R.layout.kart_item_layout_six_six;
            recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        }
        for(int m=0;m<randomSayilar.size()/2;m++)
        {
            rastgeleSecilenKartListesi.add(tumKartListesi.get(randomSayilar.get(m)));

        }
        for(int n=0;n<randomSayilar.size()/2;n++)
        {
            rastgeleSecilenKartListesi.add(rastgeleSecilenKartListesi.get(randomSayilar2.get(n)));

        }

       kartAdapter=new KartAdapter(getApplicationContext(), rastgeleSecilenKartListesi, layout, new KartAdapter.OnItemClickListener() {

        //kullanıcı kart seçmek için tıklama fonksiyonu
        @Override
         public void onItemClick(KartModel kartModel, String tag, ImageView imageView,int position) {
         secimSayisiGenel++;
         relativeLayout.setVisibility(View.VISIBLE);
         if(sayiciSecim==0)
         {
             oyuncununSectigiIlkKart=kartModel;
             secilenKartResim.startAnimation(zoomInOut);
             secilenKartResim.setImageBitmap(decodeImageBase64(oyuncununSectigiIlkKart.getImgData()));
             imageView.startAnimation(zoomInOut);
             imageView.setImageBitmap(decodeImageBase64(oyuncununSectigiIlkKart.getImgData()));
             birOncekisecilenresim=imageView;
             imageView.startAnimation(zoomInOut);
             secilenKartResim.startAnimation(zoomInOut);
             secilmisResimler.add(imageView);
             sayiciSecim++;
             imageView.setOnClickListener(null);
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     relativeLayout.setVisibility(View.GONE);

                 }
             }, 300);

         }
         else {
             oyuncununSectigiIkinciKart=kartModel;
             Log.d("denemepuan","gecen süre: "+(time-zamanSayac));
             Log.d("denemepuan","birinci ev puanı "+(oyuncununSectigiIlkKart.getEvKatSayisi()));
             Log.d("denemepuan","birinci kart puanı "+(oyuncununSectigiIlkKart.getPuan()));
             Log.d("denemepuan","birinci kart adı "+(oyuncununSectigiIlkKart.getKartAd()));
             Log.d("denemepuan","ikinci ev puanı "+(oyuncununSectigiIkinciKart.getEvKatSayisi()));
             Log.d("denemepuan","ikinci kart puanı "+(oyuncununSectigiIkinciKart.getPuan()));
             Log.d("denemepuan","ikinci kart adı "+(oyuncununSectigiIkinciKart.getKartAd()));

             imageView.startAnimation(zoomInOut);
             imageView.setImageBitmap(decodeImageBase64(oyuncununSectigiIkinciKart.getImgData()));


             if(oyuncununSectigiIlkKart.getKartAd().equals(oyuncununSectigiIkinciKart.getKartAd()))
             {
                 puanmain=puanmain+((2*oyuncununSectigiIkinciKart.getPuan()*
                         oyuncununSectigiIkinciKart.getEvKatSayisi())*(zamanSayac/10));
                 Log.d("puandeneme","puan "+puanmain);
                 txtPuan.setText(new DecimalFormat("##").format(puanmain));
                 sayiciSecim=0;
                 eslestirilmisResimSayisi++;
                 Log.d("deneme","eşleitirilmş resim sayısı--"+eslestirilmisResimSayisi );
                 Log.d("deneme","random sayilar sayisi--"+randomSayilar.size() );
                dogruSes.start();
                 imageView.startAnimation(zoomInOut);
                 secilmisResimler.add(imageView);
                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         relativeLayout.setVisibility(View.GONE);
                         imageView.setOnClickListener(null);
                         imageView.setImageResource((R.drawable.secilmis));
                         birOncekisecilenresim.setImageResource((R.drawable.secilmis));
                         secilenKartResim.setImageResource(R.drawable.beyaz_on_yuz);
                          for(int i1=0;i1<secilmisResimler.size();i1++)
                         {
                             secilmisResimler.get(i1).setImageResource(R.drawable.eslestirilmis);
                         }


                         Log.d("denemebulunan", "eşleştirilmiş resim sayısı"+eslestirilmisResimSayisi);
                         Log.d("denemebulunan", "eşleştirilmiş resim sayısı"+randomSayilar.size()/2);
                         if(eslestirilmisResimSayisi==randomSayilar.size()/2)
                         {
                             Log.d("denemesayac", String.valueOf(zamanSayac));
                             if(zamanSayac>=0)
                             {   Log.d("denemesayac", "dialog gösterilecek");
                                 sureBitmedenBulundu.start();
                                 dialogBilgi(R.layout.dialog_genel,"Tebrikler", new DecimalFormat("##").format(puanmain)).show();
                             }
                         }
                     }
                 }, 300);
             }
             else {
                 imageView.startAnimation(zoomInOut);
                 if(oyuncununSectigiIlkKart.getEvAd().equals(oyuncununSectigiIkinciKart.getEvAd()))
                 {
                     puanmain=puanmain-(((oyuncununSectigiIlkKart.getPuan()
                            +oyuncununSectigiIkinciKart.getPuan()) /
                            oyuncununSectigiIlkKart.getEvKatSayisi())*
                            ((time-zamanSayac)/10));
                     if(puanmain<0)
                     {
                         puanmain=0;
                     }
                    txtPuan.setText(new DecimalFormat("##").format(puanmain));


                 }
                 else {
                     puanmain=puanmain-((((oyuncununSectigiIlkKart.getPuan()
                             +oyuncununSectigiIkinciKart.getPuan()) /2)*
                             oyuncununSectigiIlkKart.getEvKatSayisi()*
                             oyuncununSectigiIkinciKart.getEvKatSayisi())*
                             ((time-zamanSayac)/10));
                     if(puanmain<0)
                     {
                         puanmain=0;
                     }
                     txtPuan.setText(new DecimalFormat("##").format(puanmain));

                 }

                 new Handler().postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         relativeLayout.setVisibility(View.GONE);
                         imageView.setImageResource((R.drawable.beyaz_on_yuz));
                     }
                 },300);


             }
         }

      }

           @Override
           public void onItemClick2(KartModel kartModel, ImageView imageView,int position) {

           }
       });

        recyclerView.setAdapter(kartAdapter);
        kartAdapter.notifyDataSetChanged();
    }

     //random seçilecek kartlar için random sayi üret fonksiyonu
    private ArrayList<Integer> randomSayiUret(int i,int max) {
        for(int r=0;r<i;r++)
        {
            int random1=random.nextInt(max);
            if(random1 != (randomNumbers.get(r)) && !randomNumbers.contains(random1))
            {
                randomNumbers.add(r,random1);
            }
            else{
                r--;
            }
        }
        randomNumbers.remove(randomNumbers.size()-1);
        return randomNumbers;
    }

    //random seçilecek kartlar içinde kart eşlerinin farklı indexlere yerleştirilmesi için yazılan metot

    private ArrayList<Integer> randomSayiUret2(int i,int max) {
        for(int r=0;r<i;r++)
        {
            int random1=random.nextInt(max);
            if(random1 != (randomNumbers2.get(r)) && !randomNumbers2.contains(random1))
            {
                randomNumbers2.add(r,random1);
            }
            else{
                r--;
            }
        }
        randomNumbers2.remove(randomNumbers2.size()-1);

        return randomNumbers2;
    }



    /*





        writeNewUser("cards","Albus Dumbledore","GRYFFINDOR",2,20,encodeImageBase64(R.drawable.albusdambildor));

        writeNewUser("cards","Rubeus Hagrid","GRYFFINDOR",2,12,encodeImageBase64(R.drawable.ruberushagrid));
        writeNewUser("cards","Minerva McGonagall","GRYFFINDOR",2,13,encodeImageBase64(R.drawable.minevramaggonal));
        writeNewUser("cards","Arthur Weasley","GRYFFINDOR",2,10,encodeImageBase64(R.drawable.arthurweasely));
        writeNewUser("cards","Sirius Black","GRYFFINDOR",2,18,encodeImageBase64(R.drawable.sitiusblack));
        writeNewUser("cards","Lily Potter","GRYFFINDOR",2,12,encodeImageBase64(R.drawable.iliypotter));
        writeNewUser("cards","Remus Lupin","GRYFFINDOR",2,10,encodeImageBase64(R.drawable.remusluppin));
        writeNewUser("cards","Peter Pettigrew","GRYFFINDOR",2,5,encodeImageBase64(R.drawable.peterpetergriv));
        writeNewUser("cards","Harry Potter","GRYFFINDOR",2,10,encodeImageBase64(R.drawable.harrypotter));
        writeNewUser("cards","Ron Weasley ","GRYFFINDOR",2,8,encodeImageBase64(R.drawable.ronwasely));
        writeNewUser("cards","Hermione Granger ","GRYFFINDOR",2,10,encodeImageBase64(R.drawable.hermoniegranger));


        writeNewUser("cards","Tom Riddle","SLYTHERIN",2,20,encodeImageBase64(R.drawable.tomriddddddle));
        writeNewUser("cards","Horace Slughorn","SLYTHERIN",2,12,encodeImageBase64(R.drawable.horace));
        writeNewUser("cards","Bellatrix Lestrange","SLYTHERIN",2,13,encodeImageBase64(R.drawable.bellatrix));
        writeNewUser("cards","Narcissa Malfoy","SLYTHERIN",2,10,encodeImageBase64(R.drawable.narciamalfoy));
        writeNewUser("cards","Andromeda Tonks","SLYTHERIN",2,16,encodeImageBase64(R.drawable.anromedaslytherin));
        writeNewUser("cards","Lucius Malfoy","SLYTHERIN",2,12,encodeImageBase64(R.drawable.luciusmalfot));
        writeNewUser("cards","Evan Rosier","SLYTHERIN",2,10,encodeImageBase64(R.drawable.evanrosier));
        writeNewUser("cards","Draco Malfoy","SLYTHERIN",2,5,encodeImageBase64(R.drawable.dracomalfoy));
        writeNewUser("cards","Dolores Umbridge","SLYTHERIN",2,10,encodeImageBase64(R.drawable.dolores));
        writeNewUser("cards","Severus Snape","SLYTHERIN",2,18,encodeImageBase64(R.drawable.severus));
        writeNewUser("cards","Leta Lestrange","SLYTHERIN",2,10,encodeImageBase64(R.drawable.leta));



        writeNewUser("cards","Rowena Ravenclaw",         "RAVENCLAW",1,20,encodeImageBase64(R.drawable.rovena));
        writeNewUser("cards","Luna Lovegood",    "RAVENCLAW",1,9,encodeImageBase64(R.drawable.lunalove));
        writeNewUser("cards","Gilderoy Lockhart","RAVENCLAW",1,13,encodeImageBase64(R.drawable.gilderoy));
        writeNewUser("cards","Filius Flitwick",    "RAVENCLAW",1,10,encodeImageBase64(R.drawable.fillusflitwik));
        writeNewUser("cards","Cho Chang ",    "RAVENCLAW",1,11,encodeImageBase64(R.drawable.chocho));
        writeNewUser("cards","Sybill Trelawney",      "RAVENCLAW",1,14,encodeImageBase64(R.drawable.sybilltre));
        writeNewUser("cards","Marcus Belby",        "RAVENCLAW",1,10,encodeImageBase64(R.drawable.marcusbelby));
        writeNewUser("cards","Myrtle Warren",       "RAVENCLAW",1,5,encodeImageBase64(R.drawable.mytre));
        writeNewUser("cards","Padma Patil",   "RAVENCLAW",1,10,encodeImageBase64(R.drawable.padmapatil));
        writeNewUser("cards","Quirinus Quirrell",      "RAVENCLAW",1,15,encodeImageBase64(R.drawable.quirunus));
        writeNewUser("cards","Garrick Ollivander ",     "RAVENCLAW",1,15,encodeImageBase64(R.drawable.garrick));

        writeNewUser("cards","Helga Hufflepuff",          "HUFFLEPUFF",1,20,encodeImageBase64(R.drawable.helgahuf));
        writeNewUser("cards","Cedric Diggory ",           "HUFFLEPUFF",1,18,encodeImageBase64(R.drawable.cedricdig));
        writeNewUser("cards","Nymphadora Tonks",          "HUFFLEPUFF",1,14,encodeImageBase64(R.drawable.nyphadora));
        writeNewUser("cards","Pomona Sprout ",            "HUFFLEPUFF",1,10,encodeImageBase64(R.drawable.pomona));
        writeNewUser("cards","Newt Scamander ",           "HUFFLEPUFF",1,18,encodeImageBase64(R.drawable.newtscamender));
        writeNewUser("cards","Fat Friar ",                "HUFFLEPUFF",1,12,encodeImageBase64(R.drawable.fatfirriar));
        writeNewUser("cards","Hannah Abbott ",            "HUFFLEPUFF",1,10,encodeImageBase64(R.drawable.hannagabbord));
        writeNewUser("cards","Ernest Macmillan",          "HUFFLEPUFF",1,5,encodeImageBase64(R.drawable.ernesmacmillan));
        writeNewUser("cards","Leanne",                    "HUFFLEPUFF",1,10,encodeImageBase64(R.drawable.leanne));
        writeNewUser("cards","Silvanus Kettleburn ",      "HUFFLEPUFF",1,12,encodeImageBase64(R.drawable.silvanuskettil));
        writeNewUser("cards","Ted Lupin ",                "HUFFLEPUFF",1,10,encodeImageBase64(R.drawable.tedluppin));



        */


private String encodeImageBase64(int i)
{
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),i);
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    byte[] imageBytes = byteArrayOutputStream.toByteArray();
    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    return  imageString;

}
//burdaki metot base64  stringini resim nesnesi olan bitmap a çevirir
//base64 ile gelen stringi bitmap a dönüştüren metot
    public static  Bitmap decodeImageBase64(String imageString)
    {
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
       return  decodedByte;
    }






    public void writeNewUser(String child, String kartAd, String evAd, int evKatsayisi, int puan,String resim) {

        Map<String, Object> values = new HashMap<>();
        values.put("evAd", evAd);
        values.put("evKatSayisi", evKatsayisi);
        values.put("imgData", resim);
        values.put("kartAd", kartAd);
        values.put("puan", puan);
        database.getReference("cards").child(kartAd).setValue(values);
    }


    @Override
    protected void onPause() {

        super.onPause();
    }


}








