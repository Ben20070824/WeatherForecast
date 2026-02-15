package com.example.weatherforecast;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String nickName;
    private String gender;
    private String account;
    private String age;
    private String birth;

    private String city;

    private String university;
    private String signature;
    private String avatar;

    public UserInfo(String nickName, String gender, String account, String age, String birth, String city, String university, String signature) {
        this.nickName = nickName;
        this.gender = gender;
        this.account = account;
        this.age = age;
        this.birth = birth;
        this.city = city;
        this.university = university;
        this.signature = signature;
    }

    public UserInfo(String nickName, String gender, String account, String age, String birth, String city, String university, String signature, String avatar) {
        this.nickName = nickName;
        this.gender = gender;
        this.account = account;
        this.age = age;
        this.birth = birth;
        this.city = city;
        this.university = university;
        this.signature = signature;
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
