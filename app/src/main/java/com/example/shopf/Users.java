package com.example.shopf;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Users implements Parcelable {

    String name, surname, pwd;
    @Exclude
    String email;

    public Users(String name, String surname, String pwd) {
        this.name = name;
        this.surname = surname;

        this.pwd = pwd;
    }

    protected Users(Parcel in) {
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        pwd = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void escapeSpecialChars(String toFormat) {
        int specialPos, dotPos;
        specialPos = dotPos = 0;
        specialPos = toFormat.indexOf('@');
        if (specialPos != -1)
            toFormat = toFormat.substring(0, specialPos) + "tokchio" + toFormat.substring(specialPos + 1);
        Log.d("SPECIAL", toFormat);
        dotPos = toFormat.indexOf('.');
        if (dotPos != -1)
            while (dotPos != -1) {
                toFormat = toFormat.substring(0, dotPos) + "tok" + toFormat.substring(dotPos + 1);
                dotPos = toFormat.indexOf('.', dotPos + 1);
            }

        Log.d("email formatted", toFormat);
    }

    public String getOriginalEmailFrom(String formatted) {
        String originalEmail = "";
        String[] email = formatted.split("tokchio");
        if (email != null) {
            email[1] = "@" + email[1];
            Log.d("@", String.valueOf(email));
            int pointPos = 0;
            String[] dots = email[0].split("tok");
            String[] domain = email[1].split("tok");
            pointPos = dots.length - 1;
            for (int i = 0; i < pointPos; i++) {
                originalEmail += dots[i] + '.';
            }
            originalEmail += dots[dots.length - 1];
            originalEmail += domain[0] + '.' + domain[1];
        }

        //Log.d("DOTS", String.valueOf(emailElements));
        Log.d("ORIGINAL", String.valueOf(originalEmail));
        return originalEmail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeString(pwd);
    }
}
