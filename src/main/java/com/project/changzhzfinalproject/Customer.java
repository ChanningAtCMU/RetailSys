package com.project.changzhzfinalproject;

import java.util.Date;

public class Customer {
    private int age;
    private String id, firstName, lastName, email, gender,cardType, cardNum, cardHolder,expireDate, cvv;

    public Customer(String id, String firstName, String lastName, String email, String gender, int age, String cardType, String cardNum, String cardHolder, String expireDate, String cvv) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.cardType = cardType;
        this.cardNum = cardNum;
        this.cardHolder = cardHolder;
        this.expireDate = expireDate;
        this.cvv = cvv;
    }

    public Customer(String id, String gender, int age, String cardType, String cardNum, String cardHolder, String expireDate, String cvv){
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.cardType = cardType;
        this.cardNum = cardNum;
        this.cardHolder = cardHolder;
        this.expireDate = expireDate;
        this.cvv = cvv;
    }

    public Customer(String gender){
        this.gender = gender;
    }

    public Customer(String id, String lastName){
        this.id = id;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpireDate() {
        return expireDate;
    }
}
