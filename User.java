package OOP;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User extends Account{
    private String name;
    private String phNum;
    private String hashPassword;
    ArrayList<Transaction> transaction = new ArrayList<>();

    public User(String name,String phNum){
        this.name = name;
        this.phNum = phNum;
        this.hashPassword = encryptedPassword(forPin());

    }
    public String encryptedPassword(String pin){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(pin.getBytes());

            BigInteger hashPassword = new BigInteger(1,messageDigest);

            return hashPassword.toString(16);

        }catch (NoSuchAlgorithmException e){

            e.printStackTrace();
            return "";
        }
    }

    public void changePass(String pass){
        this.hashPassword = encryptedPassword(pass);
    }

    public String forPin(){
        String pin = "";
        for(int i = phNum.length()-4; i < phNum.length(); i++){
            pin += phNum.charAt(i);
        }
        return pin;
    }
    public boolean validatePassword(String pin){
        String password = encryptedPassword(pin);
        if(this.hashPassword.equals(password)){
            return true;
        }else{
            return false;
        }
    }
    public boolean userLogin(String id,String pin){
        if(this.userId.compareTo(id) == 0 && validatePassword(pin)){
            return true;
        }
        return false;
    }
    public String getUserName(){
        return name;
    }
    public void addTransaction(String name,String id,double amount,String memo,String dateNow){
        Transaction newTransaction = new Transaction(name,id,amount,memo,dateNow);
        transaction.add(newTransaction);

    }
    public void showTransaction(String name,String id,double amount,String memo,String dateNow){
        if(amount>0){
            System.out.printf("\n%s \nDeposit to %s [%s] : ฿%.02f\nNote : %s\n",dateNow,name,id,amount,memo);
        }else if(name.equals("")){
            System.out.printf("\n%s \nTop Up to phone number [%s] : ฿(%.02f) \nNote : %s\n",dateNow,id,amount,memo);
        } else if(amount < 0){
            System.out.printf("\n%s \nTransfer from %s [%s]\n \tto %s [%s] : ฿(%.02f)\n \tNote : %s\n",dateNow,getUserName(),getAccId(),name,id,amount,memo);
        }
        System.out.println();
    }
    public double getAcctBalance(){
        double balance = 0.0;
        for(Transaction t : transaction){
            balance += t.getAmount();
        }
        return balance;
    }
    public void showTransactionHistory(){
        if(transaction.size() > 0){
            for(Transaction i : transaction){
                if(i.getAmount()>0){
                    System.out.printf("\n%s \nDeposit to %s [%s] : ฿%.02f\nNote : %s\n",i.getNowDate(),i.getName(),i.getId(),i.getAmount(),i.getMemo());
                }else if(i.getName().equals("")){
                    System.out.printf("\n%s \nTop Up to phone number [%s] : ฿(%.02f) \nNote : %s\n",i.getNowDate(),i.getId(),i.getAmount(),i.getMemo());
                } else if(i.getAmount()< 0){
                    System.out.printf("\n%s \nTransfer from %s [%s]\n \tto %s [%s] : ฿(%.02f)\n \tNote : %s\n",i.getNowDate(),getUserName(),getAccId(),i.getName(),i.getId(),i.getAmount(),i.getMemo());
                }
                System.out.println();
            }
        }
        else{
            System.out.println("\n\nNo transaction history!!!\n\n");
        }
    }
    public String getPhNum(){
        return phNum;
    }
    public void check(String name,String id,double amount,String memo){
        if(name.equals("")){
            System.out.println("\nTop Up phone number : "+id);
            System.out.println("Top Up amount       : "+amount);
            System.out.println("Top Up Note         : "+memo);
        }else if(amount < 0){
            System.out.println("\nTransfer account ID : "+id);
            System.out.println("Transfer Name       : "+name);
            System.out.println("Transfer amount     : "+amount);
            System.out.println("Transfer memo       : "+memo);
        }else if(amount > 0){
            System.out.println("\nDeposit Amount : "+amount);
            System.out.println("Deposit memo   :  "+memo);
        }

    }
    AccNames names = new AccNames();
    public String getTransferName(){
        return names.getRandomName();
    }
    public String getHashPassword(){
        return this.hashPassword;
    }
    public void setHashPassword(String hashPassword){
        this.hashPassword = hashPassword;
    }
}
