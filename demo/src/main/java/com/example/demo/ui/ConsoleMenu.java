package com.example.demo.ui;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Scanner;
@Component
public class ConsoleMenu {
    private final AccountService service;
    private final Scanner sc = new Scanner(System.in);

    public ConsoleMenu(AccountService service){
        this.service = service;
    }

    public void start(){

        System.out.println("====================================");
        System.out.println("            BANKING SYSTEM          ");
        System.out.println("====================================\n\n");
        boolean running = true;

        do {
            displayMenu();
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;

                case 2:
                    viewAllAccounts();
                    break;

                case 3:
                    searchAccount();
                    break;

                case 4:
                    depositMoney();
                    break;

                case 5:
                    withdrawMoney();
                    break;

                case 6:
                    transferMoney();
                    break;

                case 7:
                    checkBalance();
                    break;

                case 8:
                    deleteAccount();
                    break;

                case 9:
                    System.out.println("Thank you for using Banking System.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }while(running);
    }

    private void displayMenu(){
        System.out.println("1. Create Account");
        System.out.println("2. View All Accounts");
        System.out.println("3. Search Account");
        System.out.println("4. Deposit Money");
        System.out.println("5. Withdraw Money");
        System.out.println("6. Transfer Money");
        System.out.println("7. Check Balance");
        System.out.println("8. Delete Account");
        System.out.println("9. Exit");

        System.out.println("\n\nEnter your choice : ");
    }

    private void createAccount() {
        System.out.println("Enter Account no : ");
        int accountNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Holder Name : ");
        String holderName = sc.nextLine();
        System.out.println("Enter Account Type : ");
        String accountType = sc.next();
        System.out.println("Enter balance : ");
        double balance = sc.nextDouble();
        Account account = new Account(accountNo,holderName,accountType,balance);
        if(!service.createAccount(account)){
            System.out.println("Account already exists.");
        }else{
            System.out.println("Account created successfully.");
        }
    }

    private void viewAllAccounts() {
        Collection<Account> accounts = service.getAllAccounts();
        if(accounts.isEmpty()){
            System.out.println("No accounts found.");
        }else{
            for(Account account : accounts){
                System.out.println(account);
            }
        }
    }

    private void searchAccount() {
        System.out.println("Enter account no : ");
        int accountNo = sc.nextInt();
        Account account = service.findAccountByAccountNo(accountNo);
        if(account == null){
            System.out.println("No accounts found.");
        }else{
            System.out.println(account);
        }
    }

    private void depositMoney() {
        System.out.println("Enter account no : ");
        int accountNo = sc.nextInt();
        System.out.println("Enter amount : ");
        double amount = sc.nextDouble();

        if(!service.deposit(accountNo,amount)){
            System.out.println("Deposit failed. Invalid account number or amount.");
        }else{
            System.out.println("Deposit successful");
        }
    }

    private void withdrawMoney() {
        System.out.println("Enter account no : ");
        int accountNo = sc.nextInt();
        System.out.println("Enter amount : ");
        double amount = sc.nextDouble();

        if(!service.withdraw(accountNo,amount)){
            System.out.println("Withdraw failed. Invalid account number or amount.");
        }else{
            System.out.println("Withdraw successful");
        }
    }

    private void transferMoney() {
        System.out.println("Enter sender account no : ");
        int senderAccountNo = sc.nextInt();
        System.out.println("Enter receiver account no : ");
        int receiverAccountNo = sc.nextInt();
        System.out.println("Enter amount : ");
        double amount = sc.nextDouble();

        if(!service.transfer(senderAccountNo,receiverAccountNo,amount)){
            System.out.println("Transfer failed. Invalid account numbers or amount.");
        }else{
            System.out.println("Transfer successful");
        }
    }

    private void checkBalance() {
        System.out.println("Enter account no : ");
        int accountNo = sc.nextInt();
        Account account = service.findAccountByAccountNo(accountNo);
        if(account == null){
            System.out.println("No account found.");
        }else{
            System.out.println("Balance : " + account.getBalance());
        }
    }

    private void deleteAccount() {
        System.out.println("Enter account no : ");
        int accountNo = sc.nextInt();

        if(!service.deleteAccountByAccountNo(accountNo)){
            System.out.println("No account found.");
        }else{
            System.out.println("Account deleted successfully");
        }
    }
}
