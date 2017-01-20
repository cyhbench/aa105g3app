package com.member;

import java.io.Serializable;


public class MemberVO implements Serializable {
    private String mem_no;
    private String mem_name;
    private String mem_ac;
    private String mem_pw;
    private byte[] mem_image;
    private String mem_sex;
    private String mem_phone;
    private String mem_email;
    private String mem_adrs;
    private String mem_own;
    private String mem_history;
    private String mem_online;

    public MemberVO(){
        super();
    }

    public MemberVO(String mem_no, String mem_name, byte[] mem_image){
        super();
        this.mem_no = mem_no;
        this.mem_name = mem_name;
        this.mem_image = mem_image;
    }
    public MemberVO(String mem_no, String mem_name, String mem_ac,
                    String mem_pw, byte[] mem_image, String mem_sex,
                    String mem_phone, String mem_email, String mem_adrs,
                    String mem_own, String mem_history, String mem_online){
        super();
        this.mem_no = mem_no;
        this.mem_name = mem_name;
        this.mem_ac = mem_ac;
        this.mem_pw = mem_pw;
        this.mem_image = mem_image;
        this.mem_sex = mem_sex;
        this.mem_phone = mem_phone;
        this.mem_email = mem_email;
        this.mem_adrs = mem_adrs;
        this.mem_own = mem_own;
        this.mem_history = mem_history;
        this.mem_online = mem_online;
    }

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public String getMem_name() {
        return mem_name;
    }

    public void setMem_name(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getMem_ac() {
        return mem_ac;
    }

    public void setMem_ac(String mem_ac) {
        this.mem_ac = mem_ac;
    }

    public String getMem_pw() {
        return mem_pw;
    }

    public void setMem_pw(String mem_pw) {
        this.mem_pw = mem_pw;
    }

    public byte[] getMem_image() {
        return mem_image;
    }

    public void setMem_image(byte[] mem_image) {
        this.mem_image = mem_image;
    }

    public String getMem_sex() {
        return mem_sex;
    }

    public void setMem_sex(String mem_sex) {
        this.mem_sex = mem_sex;
    }

    public String getMem_phone() {
        return mem_phone;
    }

    public void setMem_phone(String mem_phone) {
        this.mem_phone = mem_phone;
    }

    public String getMem_email() {
        return mem_email;
    }

    public void setMem_email(String mem_email) {
        this.mem_email = mem_email;
    }

    public String getMem_adrs() {
        return mem_adrs;
    }

    public void setMem_adrs(String mem_adrs) {
        this.mem_adrs = mem_adrs;
    }

    public String getMem_own() {
        return mem_own;
    }

    public void setMem_own(String mem_own) {
        this.mem_own = mem_own;
    }

    public String getMem_history() {
        return mem_history;
    }

    public void setMem_history(String mem_history) {
        this.mem_history = mem_history;
    }

    public String getMem_online() {
        return mem_online;
    }

    public void setMem_online(String mem_online) {
        this.mem_online = mem_online;
    }
}

