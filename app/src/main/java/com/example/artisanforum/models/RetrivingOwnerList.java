package com.example.artisanforum.models;

public class RetrivingOwnerList {
    private String name;
    private String phone;
    private String type;
    private String proimg;

    public RetrivingOwnerList() {
    }

    public RetrivingOwnerList(String name, String phone, String type, String proimg) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.proimg = proimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProimg() {
        return proimg;
    }

    public void setProimg(String proimg) {
        this.proimg = proimg;
    }
}
