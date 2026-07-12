/**
 * Author : Belal Khamees
 */

import java.util.Scanner;


public class Main {
    private static double balance = 2500.75;
    private static int successfulDeposit = 0;
    private static int successfulWithdraw = 0;

    static void printMenu(){
         System.out.println("========= ATM =========\n" +
                            "1. Check Balance\n" +
                            "2. Deposit\n" +
                            "3. Withdraw\n" +
                            "4. Show Account Status\n" +
                            "5. Exit\n" +
                            "=======================");
     }

    static void deposit(double amount){
        if(amount > 0){
          balance += amount;
          successfulDeposit++;
          System.out.println("Your Current Balance Is: " + balance);
        }
        else{
            System.out.println("Invalid amount.");
        }
    }

    static void withdraw(double amount){
        if(amount > 0 && amount <= balance){
            balance -= amount;
            successfulWithdraw++;
            System.out.println("Your Current Balance Is: " + balance);
        }
        else if(amount > balance){
            System.out.println("Insufficient Balance.");
        }
        else if(amount == 0){
            System.out.println("Transaction cancelled.");
        }
        else{
            System.out.println("Invalid Amount.");
        }

        if(balance == 0){
            System.out.println("Warning: Your account is empty.");
        }
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String correctPIN = "1234";

        byte attempts = 3;

        while (attempts > 0) {

        System.out.println("Enter The PIN Number: ");
        String pin = sc.next();

        if (correctPIN.equals(pin)) break;

        attempts--;

        if(attempts == 0){
            System.out.println("Your Account Has Been Locked.");
            return;
        }

      }

        byte choice;
        double amount;

        do{
            printMenu();
             choice = sc.nextByte();

            switch(choice){
                case 1:
                    System.out.println("Current Balance Is: " + balance);
                    break;

                case 2:
                    System.out.println("Enter The Deposit Amount: ");
                    amount = sc.nextDouble();
                    deposit(amount);
                    break;

                case 3:
                    System.out.println("Enter The Withdraw Amount: ");
                    amount = sc.nextDouble();
                    withdraw(amount);
                    break;

                case 4:
                    System.out.print("Account Status Is: ");
                    if(balance >= 5000){
                        System.out.println("VIP Customer");
                    }
                    else if(balance >= 1000){
                        System.out.println("Regular Customer");
                    }
                    else if(balance < 1000){
                        System.out.println("Low Balance");
                    }
                    break;

                case 5:
                    System.out.println("Thank You For Using Our ATM.");
                    System.out.println("Your Successful Deposit Is: " + successfulDeposit );
                    System.out.println("Your Successful Withdrawal Is: " + successfulWithdraw );

                    break;

                default:
                    System.out.println("Invalid Option.");
            }

            if(choice == 5) break;

        } while(true);




    }

}



