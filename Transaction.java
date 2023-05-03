package OOP;
public class Transaction {
    private double amount;
    private String name;
    private String id;
    private String memo;
    private String nowDate;
    public Transaction(String name,String id,double amount,String memo,String nowDate){
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.memo = memo;
        this.nowDate = nowDate;
    }
    public double getAmount(){
        return amount;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getMemo(){
        return memo;
    }
    public String getNowDate(){
        return nowDate;
    }


}
