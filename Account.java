package OOP;

import java.util.Random;
public class Account {
    Random rd = new Random();
    protected String accId;
    protected String userId;
    public Account(){

        this.accId = formattedAccID();
        this.userId = randomUserID();
    }
    public String randomAccID(){
        String id = "";
        for(int i = 0; i<10; i++){
            id += ((Integer) rd.nextInt(10)).toString();
        }
        return id;
    }
    public String randomUserID(){
        String id = "";
        for(int i = 0; i < 6; i++){
            id += ((Integer) rd.nextInt(10)).toString();
        }
        return id;
    }
    public String getUserId(){
        return this.userId;
    }
    public String getAccId(){
        return this.accId;
    }
    public String formattedAccID(){
        String formatted = "";
        String unFormatted = randomAccID();
        int count = 0;
        for(int i = 0; i < unFormatted.length(); i++){
            formatted += unFormatted.charAt(i);
            count++;
            if(count == 3 || count == 4){
                formatted += "-";
            }
        }
        return formatted;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public void setAccId(String accId){
        this.accId = accId;
    }
}
