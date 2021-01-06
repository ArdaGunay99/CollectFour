package CollectFour;
import CollectFour.SocketAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientConnector extends SocketAction {
    static final int PORTNUM = 5000;
    private String userName;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean login(){
        return true; // başarılı olursa
    }
    public boolean signUp(){
        return true; // başarılı olursa
    }



    public MyClientConnector(String userName) throws IOException{
        super(new Socket("localhost",PORTNUM));
        this.userName = userName;
    }




    public static void main(String[] args) {
        String userName="arda";
        String password;
        MyClientConnector client= null;
        boolean islogin = false;
        Scanner scan = new Scanner(System.in);



        while (!islogin) {
            System.out.println("Press L to login or S to sign up and login: ");
            String temp = scan.nextLine();
            if(temp.equalsIgnoreCase("l")){
                islogin = true;
            }
            else if(temp.equalsIgnoreCase("s")){
                islogin = client.signUp();
            }

        }

        try {
            client = new MyClientConnector(userName);
            new Thread(client).start();
            client.send(userName);// to set the handler thread's userName

        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {
            System.out.print("> ");
            // read message from user
            String msg = scan.nextLine();
            // logout if message is LOGOUT
            if(msg.equalsIgnoreCase("LOGOUT")) {
                client.send("LOGOUT");
                break;
            }

            // regular text message
            else {
                client.send(msg);
            }
        }
        // close resource
        scan.close();
        // client completed its job. disconnect client.
        client.closeConnections();




    }





    public void run() {
        while(true) {
            try {
                // read the message form the input datastream
                String msg = receive();
                // print the message
                System.out.println(msg);
                System.out.print("> ");
            }
            catch(IOException e) {
                System.out.println( "Server has closed the connection: " + e );
                break;
            }

        }
    }





}