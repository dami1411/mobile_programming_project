package com.example.shopf;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Prods implements Parcelable {
    @Exclude
    String key;
    String srcImg;
    String name;
    String description;
    Double price;
    Float rating;

    public Prods() {
    }

    public Prods(String srcImg, String name, String description, Double price, Float rating) {
        this.srcImg = srcImg;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Float getRating() {
        if (rating != null)
            return rating;
        else return 3.0f;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }


    protected Prods(Parcel in) {
        key = in.readString();
        srcImg = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readFloat();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(srcImg);
        dest.writeString(name);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(rating);
        }
    }

    public static final Creator<Prods> CREATOR = new Creator<Prods>() {
        @Override
        public Prods createFromParcel(Parcel in) {
            return new Prods(in);
        }

        @Override
        public Prods[] newArray(int size) {
            return new Prods[size];
        }
    };


}
