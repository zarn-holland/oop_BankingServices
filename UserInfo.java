package OOP;

import java.util.ArrayList;

public class UserInfo {
    private String userId;
    private String hashPin;
    private String userName;
    private String phNum;
    private String accId;


    private ArrayList<Transaction> transactions;
    public UserInfo(String userId,String hashPin,String userName,String phNum,String accId){
        this.userId = userId;
        this.hashPin = hashPin;
        this.userName = userName;
        this.phNum = phNum;
        this.accId = accId;

        transactions = new ArrayList<>();
    }
    public void addTransaction(String name,String id,double amount,String memo,String dateNow){
        Transaction newTransaction = new Transaction(name,id,amount,memo,dateNow);
        transactions.add(newTransaction);
    }
    public ArrayList<Transaction> getTransactions(){
        return this.transactions;
    }
    public String getUserId(){
        return this.userId;
    }
    public String getHashPin(){
        return this.hashPin;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPhNum(){
        return this.phNum;
    }
    public String getAccId(){
        return this.accId;
    }

}

