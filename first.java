package pack1;

import java.util.Scanner;
import java.sql.*;

public class first {
    public static void main(String arg[]) {
           
        signup_login ob1=new signup_login();
        System.out.println("Welcome");
        System.out.println("Select 1 for registration, 2 for login");
        Scanner sc=new Scanner(System.in);
        int sec=sc.nextInt();
        try {
            if(sec==1)
            {
                System.out.println("Please enter your phoneno:");
                Scanner ph=new Scanner(System.in);
                long phone=ph.nextLong();
                ob1.display(phone);            
            }
            else if(sec==2)
                ob1.display();
         
            else
                System.out.println("Please enter correct choice.");
            
        }
        catch(Exception e) {
            System.out.println("Please enter valid phone number");
        }
    }   
}


class signup_login extends details {
    
    String username,password,name,u,p;
    int age;
    public String uname1;
    
    void display(long ph) {        
        try {        
            System.out.println("Enter your name:");
            Scanner n=new Scanner(System.in);
            name=n.next();
        
            System.out.println("Enter username:");
            Scanner u=new Scanner(System.in);
            username=u.next();
        
            System.out.println("Enter password:");
            Scanner p1=new Scanner(System.in);
            password=n.next(); 
            
            Class.forName("java.sql.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/project?useSSL=false","root","yagna");
            Statement s=con.createStatement();
            String sql="insert into userinfo(name,phoneno,username,password) values('"+(name)+"',"+(ph)+",'"+(username)+"','"+(password)+"')";
            s.executeUpdate(sql);
            System.out.println("Account has been created.");
            s.close();
            con.close();        
        }
        
        catch(Exception e)
        {
            System.out.println("Error:\n"+e);
            return;
        }
        choice ob4=new choice();
        ob4.choices(username);
    }
    
    void display() {
        System.out.println("Enter username");
        Scanner user=new Scanner(System.in);
        u=user.next();
        System.out.println("Enter password");
        Scanner pass=new Scanner(System.in);
        p=pass.next();
        super.checking(u,p);        
    }
}

abstract class details {
    
    abstract void display(long ph);
    
    abstract void display();
    
    public void checking(String user,String pass) {
        int eq=0;
        String sql="select * from userinfo"; 
        try {
            Class.forName("java.sql.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/project?useSSL=false","root","yagna");
            Statement s=con.createStatement();
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()) {
                String uname=rs.getString("username");
                String passwd=rs.getString("password");
                if(uname.equals(user) && passwd.equals(pass)) {
                    eq=1;
                    break;
                }
                else
                    eq=0;
            }
            if(eq==1) {
                choice ch=new choice();
                ch.choices(user);
            }
            else
                System.out.println("Please enter valid username/password.");           
                   
            rs.close();
            s.close();
            con.close();
        }
        catch(Exception e) {
            System.out.println("There was an error.Please try again later."+e);
        }

    }
}       

class choice extends Thread {
      
    public void choices(String u) {

        System.out.println("Select 1 to offer a ride, 2 to search for a ride");
        Scanner c=new Scanner(System.in);
        int ch=c.nextInt();
        if(ch==1)
            new offer(u).start();
        else if(ch==2)
            new find().start();
        else {
            System.out.println("Please enter correct choice");
            return;
        }        
    }
}

class offer extends Thread {
    
    String uname;
    offer(String uname1) {
        uname=uname1;
    }
    public void run() {
        System.out.println("Enter source:");
        Scanner so=new Scanner(System.in);
        String source=so.next();
        System.out.println("Enter destination:");
        Scanner de=new Scanner(System.in);
        String dest=de.next(); 
        try {
            signup_login ob=new signup_login();
            Class.forName("java.sql.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/project?useSSL=false","root","yagna");
            Statement s=con.createStatement();
            String sql="update userinfo set source='"+(source)+"',dest='"+(dest)+"' where username='"+(uname)+"'";
            s.executeUpdate(sql);
            System.out.println("The details will be saved. Thank you.");
            s.close();
            con.close();
        }
        catch(Exception e) {
            System.out.println("There was an error. Please try again later.");
        }
    }
}

class find extends Thread {
    
    public void run() {
        int count=0;
        System.out.println("Enter source:");
        Scanner so=new Scanner(System.in);
        String source=so.next();
        System.out.println("Enter destination:");
        Scanner de=new Scanner(System.in);
        String dest=de.next(); 
        try {
            String sql="select name,phoneno,source,dest from userinfo where source='"+(source)+"'&& dest='"+(dest)+"'";
            Class.forName("java.sql.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/project?useSSL=false","root","yagna");
            Statement s=con.createStatement();
            ResultSet rs=s.executeQuery(sql);
            while(rs.next()) {
                String name=rs.getString("name");
                String phone=rs.getString("phoneno");
                System.out.println("Ride found:\n");
                System.out.println("Name: "+name+"  Phone no.: "+phone+"\n");
                count++;
            }
            rs.close();
            s.close();
            con.close();
            
            if(count==0)
                System.out.println("Ride not found, please try again later.");
        }
        catch(Exception e) {
            System.out.println("There was an error. Please try again later.\n"+e);
        }
    }
}