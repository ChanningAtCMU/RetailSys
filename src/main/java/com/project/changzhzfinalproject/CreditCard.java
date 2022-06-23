package com.project.changzhzfinalproject;

public class CreditCard {

    private String cardType;

    /**
     * Credit card constructor
     * @param cardType
     */
    public CreditCard (String cardType){
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}
