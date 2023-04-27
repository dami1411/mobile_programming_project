package com.example.shopf;

import com.google.firebase.database.Exclude;

public class WishList {
    @Exclude
    String key;
    String keyProd;
    String emailWsl;

    public WishList(String keyProd, String emailWsl) {
        this.keyProd = keyProd;
        this.emailWsl = emailWsl;
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

    public String getEmailWsl() {
        return emailWsl;
    }

    public void setEmail(String emailWsl) {
        this.emailWsl = emailWsl;
    }
}
