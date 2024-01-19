import java.sql.*;
import java.util.*;
import java.time.LocalDate; 




public class Methods {
     static Connection con=connection.getConnection();
     static Scanner sc=new Scanner(System.in);
     static int user;
     static long passcode;
     static LocalDate date=java.time.LocalDate.now();
     static String query;
    public static void main(String [] args){
        
    }
    //tostart method
    //Asking for account opening

        static void toStart(){
         while(true){
            System.out.println("---------WELCOME TO BANKING SYSTEM---------");
            System.out.println("PLEASE SELECT AN OPTION");
            System.out.println("TO CREATE ACCOUNT --->PRESS 1");
            System.out.println("TO LOGIN TO YOUR ACCOUNT --->PRESS 2");
            int sinput=sc.nextInt();
            if(sinput==1){
                CreateAccount();
            }
            else if(sinput==2){
                LoginVerification();
            }
            else{
                System.out.println("Please Enter correct option");
            }
       }
    }

//to create account 
//after creation automatically username and password will bw displayed

static void CreateAccount(){
    sc.nextLine();
    System.out.println("ENTER YOUR FIRST NAME");
    String Fname=sc.nextLine();
    System.out.println("ENTER YOUR LAST NAME");
    String Lname=sc.nextLine();
    System.out.println("Enter  aadhar card number");
    Long aadhar=sc.nextLong();
    sc.nextLine();
    System.out.println("ENTER YOUR AGE");
    int age=sc.nextInt();
 //only if the custoer age is above 18
    if(age>=18){
        try{
            query="INSERT INTO CustomersPersonalDetails" + "(FirstName,LastName,AadharNumber,age,balance) VALUES (' "+ Fname + "' ,' " + Lname + "',"+ aadhar +","+ age +",5000)";//customer details are stored in db
            Statement st=con.createStatement();
            if(st.executeUpdate(query)==1){
                System.out.println("Account created Successfull");
                query="Select * from CustomersPersonalDetails where AadharNumber = " +aadhar+ " ";
                ResultSet rs=st.executeQuery(query);
                rs.next();

        //creating userid and password 

                user=rs.getInt(1);
                passcode=rs.getLong(4);

        //showing account details after creating account
                System.out.println("accountNumber"+ user);
                System.out.println("Name" +"    "+ rs.getString("FirstName") + rs.getString("LastName"));
                System.out.println("passcode"+"     "+passcode);
                System.out.println("balance" +"     " +rs.getInt("balance"));

        // adding the credentials to credentials table in db

                query="INSERT INTO Credentials" + "(UserName , passcode) VALUES ("+user+", "+passcode+")";
                st.executeUpdate(query);
            }
        }
        catch(Exception e){
            System.out.print("ACCOUNT NOT CREATED  check your details" + e);
        }
    }

}

//Loginverification

static void LoginVerification(){
    System.out.println("-------------WELCOME BACK------------- ");
    System.out.println("PLEASE ENTER YOUR USER-ID");
    int EnteredUser=sc.nextInt();
    try{
      Statement st=con.createStatement();
      query="SELECT * from Credentials where UserName="+EnteredUser+"";
      ResultSet rs=st.executeQuery(query);
      if(rs.next()){
        query="Select passcode from Credentials where UserName="+EnteredUser+"";
        int inputuser=rs.getInt(1);
        long  password=rs.getLong(2);
        System.out.println("please enter passcode");
        Long EnteredPasscode=sc.nextLong();
    //checking if entered password is matching with actual password
        if(password==EnteredPasscode){
            System.out.println("Login Successfull");
            System.out.println("--------------WELCOME MR" +inputuser+"--------------------");
            LoginMenu(inputuser);
        }
        else{
            System.out.println("PASSWORD INCORRECT");
        }
     }
     else{
        System.out.println("SORRY !! USER NOT FOUND U HAVE TO CREATE ACCOUNT FIRST");
     }
    }
    catch(Exception e ){
        System.out.println(e);
    }
}

//loginMenu

static void LoginMenu(int accountNumber){
    System.out.println("Select your Option");
    System.out.println("Check Your Balance" + "option 1");
    System.out.println("WithDraw" + "option 2");
    System.out.println("deposit" + "option 3");
    System.out.println("Mini Statement" + "option 4");
    System.out.println("Bank Transfer" + "Option 5");
    Login(accountNumber);
}
static void Login(int accnum){
    int menuoption=sc.nextInt();
    switch(menuoption){
        case 1:
            checkBalance(accnum);
            break;
        case 2:
            WithDraw(accnum);
            break;
        case 3:
            deposit(accnum);
            break;
        case 4:
            ministatement(accnum);
            break;
        case 5:
            bankTransferdetails(accnum);
        break;
        default:
        System.out.println("please select correct option");
    }
}

// checkbalance


private static int checkBalance(int accountNumber){
    System.out.print("BLANACE");
    try{
       ResultSet rs=function(accountNumber);
        rs.next();
        System.out.println(rs.getInt("balance"));
        return rs.getInt("balance");
    }
    catch(Exception e){
        System.out.println(e);
        return 0;
    }
}

//withdraw


private static void WithDraw(int accountNumber) {
    System.out.println("WITHDRAW ::---Select Amount ");
    int WithDrawAmount=sc.nextInt();
    try{
        Statement st=con.createStatement();
        ResultSet rs=function(accountNumber);
        rs.next();
        int balanceAvailable=rs.getInt("balance");
        if(balanceAvailable>=WithDrawAmount){
            balanceAvailable=balanceAvailable-WithDrawAmount;
            String s="withdraw ";
             query="UPDATE CustomersPersonalDetails SET balance="+balanceAvailable+" where CustomerId="+accountNumber+"";
            String ministatement="Insert into MiniStatement"+ "(CustomerId,Date,UpdatedAmount,RemainingAmount,Description) VALUES("+accountNumber+",'"+date+"',"+-(WithDrawAmount)+","+balanceAvailable+",'"+s+"')";
            st.executeUpdate(query);
            st.executeUpdate(ministatement);
        }
        else{
            System.out.println("Insufficient Funds");
        }
    }
    catch(Exception e){
        System.out.println(e);
    }
}

// deposit

private static void deposit(int accountNumber) {
    System.out.println("DEPOSIT");
     int DepositAmount=sc.nextInt();
    try{
        Statement st=con.createStatement();
        ResultSet rs=function(accountNumber);
        rs.next();
        int balanceAvailable=rs.getInt("balance");
        balanceAvailable=balanceAvailable+DepositAmount;
        query="UPDATE CustomersPersonalDetails SET balance="+balanceAvailable+" where CustomerId="+accountNumber+"";
        st.executeUpdate(query);
        String s="received through deposit";
        String receiverstatement="Insert into MiniStatement"+ "(CustomerId,Date,UpdatedAmount,RemainingAmount,Description) VALUES("+accountNumber+",'"+date+"',"+(DepositAmount)+","+balanceAvailable+",'"+s+"')";
        st.executeQuery(receiverstatement);
    }
    catch(Exception e){
        System.out.println(e);
    }
}

//banktraanserdetails

private static void bankTransferdetails(int accountNumber){
    System.out.println("Enter receiver accountnumber");
    int receiver=sc.nextInt();
    System.out.println("Enter amount ");
    int amount=sc.nextInt();
    bankTransfer(accountNumber, receiver, amount);
}

// Banktransfer

private static void bankTransfer(int sender, int receiver, int amount){
    //query="Select * from CustomersPersonalDetails where CustomerId="+sender+"";
    int Ramount=amount+checkBalance(receiver);
    int Samount=checkBalance(sender)-amount;
    String RAmountUpdate="Update  CustomersPersonalDetails SET balance= "+Ramount+" Where CustomerId="+receiver+" ";
    String SAmoutUpdate="Update CustomersPersonalDetails SET balance="+Samount+" Where CustomerId="+sender+" " ;
    String des1 ="Transferred to "+ receiver;
    String des2="Received by" + sender;
    try{
         Statement st=con.createStatement();
         ResultSet rs=function(sender);
         rs.next();
         int senderBalance=rs.getInt("balance");
         String sql="SELECT * FROM CustomersPersonalDetails  where exists (select * from CustomersPersonalDetails where CustomerId ="+receiver+")  ";
         rs=st.executeQuery(sql);
         if(amount<=senderBalance){
            if(rs.next()){

                System.out.println("Receiver account found successfull");
                System.out.println("SUcessfully Tranferred");
                st.executeUpdate(RAmountUpdate);
                System.out.println("Receivers amount updated");
    
                st.executeUpdate(SAmoutUpdate);
                 System.out.println("Sender amount updated");
                String senderstatement="Insert into MiniStatement"+ "(CustomerId,Date,UpdatedAmount,RemainingAmount,Description) VALUES("+sender+",'"+date+"',"+-(amount)+","+senderBalance+",'"+des1+"')";
                String receiverstatement="Insert into MiniStatement"+ "(CustomerId,Date,UpdatedAmount,RemainingAmount,Description) VALUES("+receiver+",'"+date+"',"+(amount)+","+senderBalance+",'"+des2+"')";

                st.executeUpdate(senderstatement);
                st.executeUpdate(receiverstatement);

            }
            else{
                System.out.println("Receiver account not found");
            }
         }
         else{
            System.out.println("Insufficient Funds");
         }
    }
    catch(Exception e){
        System.out.println("receiver not found");
        System.out.println(e);
    }
}


//ministatement

private static void ministatement(int accountNumber) {
    System.out.println("YOUR MINI STATEMENT");
    try{
        query="select * from MiniStatement where CustomerId="+accountNumber+"";
        Statement st=con.createStatement();
       ResultSet rs=st.executeQuery(query);
        while(rs.next()){
            System.out.println("Date" + "       ||" +"UpdatedAmount" +" || "+"remaining amount"+"  ||  "+"Description");
            System.out.println(rs.getDate(2)+"   ||  "+ rs.getInt(3)+"   ||  "+rs.getInt(4)+" ||"+rs.getString(5));
        }
    }
    catch(Exception e){
        System.out.print(e);
    }

}

private static ResultSet function (int accno){
    try{
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("Select * from CustomersPersonalDetails where CustomerId="+accno+" ");
        return rs;
    }
    catch(Exception e){
        return null;
    }
}

}
