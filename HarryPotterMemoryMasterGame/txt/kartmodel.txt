package com.harrypottergame.memorymaster;

public class KartModel {
    public  String kartAd;
    public int puan;
   public String imgData;
  public   int evKatSayisi;
  public   String evAd;


    public KartModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public KartModel(String kartAd, int puan, String imgData, int evKatSayisi, String evAd) {
        this.kartAd = kartAd;
        this.puan = puan;
        this.imgData = imgData;
        this.evKatSayisi = evKatSayisi;
        this.evAd = evAd;
    }

    public String getKartAd() {
        return kartAd;
    }

    public void setKartAd(String kartAd) {
        this.kartAd = kartAd;
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    public String getImgData() {
        return imgData;
    }

    public void setImgData(String imgData) {
        this.imgData = imgData;
    }

    public int getEvKatSayisi() {
        return evKatSayisi;
    }

    public void setEvKatSayisi(int evKatSayisi) {
        this.evKatSayisi = evKatSayisi;
    }

    public String getEvAd() {
        return evAd;
    }

    public void setEvAd(String evAd) {
        this.evAd = evAd;
    }
}
