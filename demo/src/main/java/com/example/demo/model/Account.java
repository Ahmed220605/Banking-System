package com.example.demo.model;

public class Account {
    private int accountNo;
    private String holderName;
    private String accountType;
    private double balance;

    public Account(){}

    public Account(int accountNo,String holderName,String accountType,double balance){
        this.accountNo = accountNo;
        this.holderName = holderName;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getAccountNo(){
        return accountNo;
    }
    public void setAccountNo(int accountNo){
        this.accountNo = accountNo;
    }


    public String getHolderName(){
        return holderName;
    }
    public void setHolderName(String holderName){
        this.holderName = holderName;
    }


    public String getAccountType(){
        return accountType;
    }
    public void setAccountType(String accountType){
        this.accountType = accountType;
    }


    public double getBalance(){
        return balance;
    }
    public void setBalance(double balance){
        this.balance = balance;
    }
    @Override
    public String toString(){
        return "Account{" +
                "accountNo=" + accountNo +
                ", holderName='" + holderName + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }
}
