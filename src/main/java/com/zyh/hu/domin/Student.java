package com.zyh.hu.domin;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Student {
    private int id;
    private String name;
    private String gender;
    private String address;
    private String upDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }
}
