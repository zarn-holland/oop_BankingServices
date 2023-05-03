package OOP;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Main {
    LocalDateTime dateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy- hh:mm");
    public static void main(String[] args) {

        Main mobileBanking = new Main();
        Scanner keyboard = new Scanner(System.in);

        int choice = 0;
        do{
            System.out.println("1> Login Account\n2> Sign Up Account");
            choice = keyboard.nextInt();
            keyboard.nextLine();
            switch (choice){
                case 1:
                    loginAccount(mobileBanking,keyboard);
                    break;
                case 2:
                    signUpAccount(mobileBanking,keyboard);
                    break;
                default:
                    System.out.println("Enter only 1 and 2,");
                    break;
            }
        }while(choice < 1 || choice > 2);
    }
    public static void loginAccount(Main mobileBanking,Scanner keyboard){
        ArrayList<UserInfo> userInformation = new ArrayList<>();

        String path = "C:\\Users\\zarnn\\java\\projects\\OOP\\src\\user_info";
        String line = "";
        try {
            BufferedReader bR = new BufferedReader(new FileReader(path));

            while((line = bR.readLine()) != null){
                String[] userInfo = line.split(",");
                String userId = userInfo[0];
                String hashPin = userInfo[1];
                String userName = userInfo[2];
                String phNum = userInfo[3];
                String accId = userInfo[4];

                UserInfo userInfo1 = new UserInfo(userId,hashPin,userName,phNum,accId);

                userInformation.add(userInfo1);

                for(int i = 5; i< userInfo.length; i++){
                    String trans = userInfo[i];

                    String updateTrans= trans.replace("|",",");

                    String[] transactions = updateTrans.split(",");
                    //id, name , amount, memo,nowDate
                    String otherUserId = transactions[0];
                    String otherUserName = transactions[1];
                    Double transferAmount = Double.valueOf(transactions[2]);
                    String memo = transactions[3];
                    String nowDate = transactions[4];


                    userInfo1.addTransaction(otherUserName,otherUserId,transferAmount,memo,nowDate);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String accId;
        String pin;
        boolean isEnd = false;
        do{
            System.out.print("Enter account ID : ");
            accId = keyboard.nextLine();
            System.out.print("Enter pin : ");
            pin = keyboard.nextLine();

            for(int i = userInformation.size() - 1; i >= 0; i--){
                if(userInformation.get(i).getUserId().equals(accId) && userInformation.get(i).getHashPin().equals(encryptedPassword(pin))){
                    isEnd = true;
                    User user = new User(userInformation.get(i).getUserName(),userInformation.get(i).getPhNum());
                    user.setHashPassword(encryptedPassword(pin));
                    user.setUserId(userInformation.get(i).getUserId());
                    user.setAccId(userInformation.get(i).getAccId());
                    for(Transaction t : userInformation.get(i).getTransactions()){
                        user.addTransaction(t.getName(),t.getId(),t.getAmount(),t.getMemo(),t.getNowDate());
                    }
                    //balance htae ya omm ml
                    mobileBanking.printUserMenu(user,keyboard);
                    break;

                }
            }

            if(! isEnd){
                System.out.println("Wrong user id and password. Try again!!!");
            }

        }while(isEnd == false);


    }
    public static String encryptedPassword(String pin){
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

    public static void signUpAccount(Main mobileBanking,Scanner keyboard){
        System.out.print("Enter name : ");
        String name = keyboard.nextLine();

        System.out.print("Enter phone number : ");
        String phNum = keyboard.nextLine();

        User user = new User(name,phNum);

        System.out.printf("New user %s with ID %s is created.\n",user.getUserName(),user.getUserId());
        System.out.println("Your password is the last four digits of your phone number.\n");

        mobileBanking.mainMenuPrompt(user,keyboard);
        mobileBanking.printUserMenu(user,keyboard);

    }

    /**
     * To accept the user login
     * @param user      the user who try to log in
     * @param keyboard  Scanner object
     */
    public void mainMenuPrompt(User user,Scanner keyboard){
        String accId;
        String pin;
        do{
            System.out.print("Enter account ID : ");
            accId = keyboard.nextLine();
            System.out.print("Enter pin : ");
            pin = keyboard.nextLine();

            if(user.userLogin(accId,pin)){
                System.out.println("Login successful.");
            }else{
                System.out.println("Wrong ID and password. Try Again!!");
            }
        }while(!(user.userLogin(accId,pin)));
    }

    /**
     * To show the user what kind of features can he choose
     * @param user          the owner of account
     * @param keyboard      scanner object
     */
    public void printUserMenu(User user,Scanner keyboard){
        System.out.println("Welcome to ....\n");
        int choice = 0;
        do{
            System.out.println("1. Deposit\n2. Transfer\n3. Top Up \n4. Transaction history\n5. Setting \n6. Exit");
            System.out.print("Enter your choice : ");
            choice = keyboard.nextInt();
            switch (choice){
                case 1:
                    deposit(user,keyboard);
                    break;
                case 2:
                    transfer(user,keyboard);
                    break;
                case 3:
                    topUp(user,keyboard);
                    break;
                case 4:
                    transactionHistory(user);
                    break;
                case 5:
                    setting(user,keyboard);
                    break;
                case 6:
                    System.out.println("Have a nice day. Bye!");
                    break;
                default:
                    System.out.println("Please choose between 1 - 6.");
                    break;
            }
        }while(choice != 6);

        String path = "C:\\Users\\zarnn\\java\\projects\\OOP\\src\\user_info";
        try {
            BufferedWriter bW = new BufferedWriter(new FileWriter(path,true));
            String input = user.getUserId()+","+user.getHashPassword()+","+user.getUserName()+","+user.getPhNum()+","+user.getAccId();
            bW.write(input);
            for(Transaction t : user.transaction){
                String inputTran = ","+t.getId()+"|"+t.getName()+"|"+t.getAmount()+"|"+t.getMemo()+"|"+t.getNowDate();
                bW.write(inputTran);
            }
            bW.newLine();
            bW.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To deposit money to the account
     * @param user          the account owner
     * @param keyboard      scanner object
     */
    public void deposit(User user,Scanner keyboard){
        double amount;
        String memo;
        String dateNow;
        do{
            System.out.print("\nEnter deposit Amount : ");
            amount = keyboard.nextDouble();

            if(amount < 0){
                System.out.println("Deposit amount must be greater than 0. Try Again! ");
            }
        }while(amount < 0);
        keyboard.nextLine();
        System.out.print("Enter note : ");
        memo = keyboard.nextLine();

        //To get the current date time
        dateNow = dateTime.format(formatter);

        // show user to confirm the information he put to deposit
        user.check(user.getUserName(),user.getAccId(),amount,memo);
        //uer can choose cancel and confirm
        int choice;
        do{
            System.out.print("1> Confirm\n2> Cancel\n");
            choice = keyboard.nextInt();
            switch (choice){
                case 1:
                    user.addTransaction(user.getUserName(),user.getAccId(),amount,memo,dateNow);
                    user.showTransaction(user.getUserName(),user.getAccId(),amount,memo,dateNow);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Please choose between 1 and 2.");
                    break;
            }
        }while(choice < 1 || choice >2);


    }

    /**
     * transfer money from this account to the other account
     * @param user          the account owner
     * @param keyboard      scanner object
     */
    public void transfer(User user,Scanner keyboard){
        double amount;
        String memo;
        long accId = 0;
        String dateNow;

        double acctBalance = user.getAcctBalance();
        System.out.println(acctBalance);
        if(acctBalance == 0){
            System.out.println("\nYour balance is "+ acctBalance + "\nPlease deposit first!!\n");
            return;
        }

        keyboard.nextLine();

        //to catch the error if user didn't enter number
        try{
            System.out.print("Account ID to transfer : ");
            accId = keyboard.nextLong();
        }catch (NumberFormatException e){
            e.getMessage();
        }
        String accountID = String.valueOf(accId);
        
        keyboard.nextLine();

        do{
            System.out.print("Enter amount to transfer : ");
            amount = keyboard.nextDouble();

            if(amount < 0){
                System.out.println("Transfer amount must be greater than 0.");
            }else if(amount > acctBalance){
                System.out.println("Transfer amount can't be greater than "+acctBalance+" .");
            }
        }while(amount < 0 || amount > acctBalance);
        keyboard.nextLine();

        System.out.print("Enter note : ");
        memo = keyboard.nextLine();

        //to get the current date time
        dateNow = dateTime.format(formatter);

        String accName = user.getTransferName();

        do{
            System.out.println("Enter your password : ");
            String password = keyboard.nextLine();
            if (user.validatePassword(password)) {
                // show user to confirm the information he put to transfer
                user.check(accName,accountID,-amount,memo);
                int choice;
                do{
                    System.out.print("1> Confirm\n2> Cancel\n");
                    choice = keyboard.nextInt();
                    switch (choice){
                        case 1:
                            user.addTransaction(accName,accountID,-amount,memo,dateNow);
                            user.showTransaction(accName,accountID,-amount,memo,dateNow);
                            break;
                        case 2:
                            return;
                        default:
                            System.out.println("Please choose between 1 and 2.");
                            break;
                    }
                }while(choice < 1 || choice >2);
                break;

            }
            else{
                System.out.println("Wrong password!! Try Again!");
            }
        }while(true);
        


    }

    /**
     * To top Up the phone bill
     * @param user          account owner
     * @param keyboard      scnner object
     */
    public void topUp(User user,Scanner keyboard){
        long phNum = 0;
        double amount;
        String memo;
        String dateNow;

        double acctBalance = user.getAcctBalance();
        System.out.println(acctBalance);

        if(acctBalance == 0){
            System.out.println("\nYour balance is "+ acctBalance + "\nPlease deposit first!!\n");
            return;
        }

        //to catch the error if user didn't enter number
        try{
            System.out.print("Enter phone number : ");
            phNum = keyboard.nextLong();
        }catch(NumberFormatException e){
            e.getMessage();
        }
        String topUpPhNum = String.valueOf(phNum);

        keyboard.nextLine();
        do{
            System.out.print("Enter amount to top up : ");
            amount = keyboard.nextDouble();
            if(amount < 0){
                System.out.println("Top up amount must be greater than 0.");
            }else if(amount > acctBalance){
                System.out.println("Top up amount can't be greater than "+ acctBalance +" .");
            }
        }while(amount <0 || amount > acctBalance);
        keyboard.nextLine();



        System.out.print("Enter memo : ");
        memo = keyboard.nextLine();

        dateNow = dateTime.format(formatter);

        do{
            System.out.println("Enter your password : ");
            String password = keyboard.nextLine();
            if (user.validatePassword(password)) {
                // show user to confirm the information he put to top up
                user.check("",topUpPhNum,-amount,memo);
                int choice;
                do{
                    System.out.print("1> Confirm\n2> Cancel\n");
                    choice = keyboard.nextInt();
                    switch (choice){
                        case 1:
                            user.addTransaction("",topUpPhNum,-amount,memo,dateNow);
                            user.showTransaction("",topUpPhNum,-amount,memo,dateNow);
                            break;
                        case 2:
                            return;
                        default:
                            System.out.println("Please choose between 1 and 2.");
                            break;
                    }
                }while(choice < 1 || choice >2);
                break;
            }else{
                System.out.println("Wrong password!! Try Again!");
            }
        }while(true);

    }

    /**
     * To get the whole transaction history of the account
     * @param user      account owner
     */
    public void transactionHistory(User user){
        user.showTransactionHistory();
    }


    public void setting(User user, Scanner keyboard){
        int choice = 0;
        do{
            System.out.println("1> Show profile \n2> Change pin");
            System.out.print("Enter : ");
            choice = keyboard.nextInt();
            switch(choice){
                case 1:
                    showProfile(user);
                    break;
                case 2:
                    changePassword(user,keyboard);
                    break;
                default:
                    System.out.println("Please choose 1 and 2!!");

            }

        }while(choice < 1 || choice >2);
    }
    /**
     * To show the account info for user
     * @param user  account owner
     */
    public void showProfile(User user){
        System.out.println("\nName         : "+ user.getUserName());
        System.out.println("Account ID   : "+ user.getAccId());
        System.out.println("Phone Number : "+user.getPhNum());
        System.out.println("Pin          : ****");
        System.out.println("Balance      : "+user.getAcctBalance());
        System.out.println();
    }

    public void changePassword(User user,Scanner keyboard){
        System.out.println("Enter your new password : ");
        keyboard.nextLine();
        String newPass = keyboard.nextLine();
        System.out.println("Confirm your new password : ");
        String confirmPass = keyboard.nextLine();
        if(newPass.equals(confirmPass)){
            System.out.println("Your new password has already saved!");
            user.changePass(confirmPass);
        }else{
            System.out.println("Please enter the same password!!!");
            changePassword(user,keyboard);
        }
    }

}
