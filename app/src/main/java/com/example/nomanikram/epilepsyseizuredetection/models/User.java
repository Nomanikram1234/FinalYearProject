package com.example.nomanikram.epilepsyseizuredetection.models;

public class User {
    private String user_id;
    private String user_age;
    private String user_name;
    private String user_gender;
    private Patient patient;
    private String user_contactno;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getUser_contactno() {
        return user_contactno;
    }

    public void setUser_contactno(String user_contactno) {
        this.user_contactno = user_contactno;
    }
}
