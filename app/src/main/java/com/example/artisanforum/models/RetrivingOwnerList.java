package com.example.artisanforum.models;

public class RetrivingOwnerList {
    private String name;
    private String phone;
    private String type;

    public RetrivingOwnerList() {
    }

    public RetrivingOwnerList(String name, String phone, String type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
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
}
