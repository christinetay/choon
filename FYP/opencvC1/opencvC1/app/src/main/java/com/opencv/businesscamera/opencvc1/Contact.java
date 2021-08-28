package com.opencv.businesscamera.opencvc1;

/**
 * Created by Tay on 23/5/2018.
 */

public class Contact {
    private String name;
    private String telNo;
    private String email;
    private String address;


    public Contact(String name,String telNo, String email, String address){
        this.name=name;
        this.telNo=telNo;
        this.email=email;
        this.address=address;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getTelNo(){
        return telNo;
    }

    public void setTelNo(String telNo){
        this.telNo=telNo;
    }
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }
    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address=address;
    }


}
