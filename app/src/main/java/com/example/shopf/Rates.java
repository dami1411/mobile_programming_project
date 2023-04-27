package com.example.shopf;

import com.google.firebase.database.Exclude;

public class Rates {
    @Exclude
    String key;
    Float avgRate;
    Float tempRate;
    String keyProd;
    String emailRate;

    public Rates(Float avgRate, String keyProd, Float tempRate, String emailRate) {
        this.avgRate = avgRate;
        this.keyProd = keyProd;
        this.tempRate = tempRate;
        this.emailRate = emailRate;
    }

    public Float getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(Float avgRate) {

        this.avgRate = avgRate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyProd() {
        return keyProd;
    }

    public void setKeyProd(String keyProd) {
        this.keyProd = keyProd;
    }

    public Float getTempRate() {
        return tempRate;
    }

    public void setTempRate(Float tempRate) {
        this.tempRate = tempRate;
    }

    public String getEmailRate() {
        return emailRate;
    }

    public void setEmailRate(String emailRate) {
        this.emailRate = emailRate;
    }
}

