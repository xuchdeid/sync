package com.xuchdeid.syncpc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.*;


public class syncpc {
	//socket
	public static String TOOL = "/home/xuchdeid/android-sdk-linux_86/tools/";
	public static String START = "com.xuchdeid.syncphone.start";
	public static String STOP = "com.xuchdeid.syncphone.stop";
	public static String S = "adb shell am broadcast -a ";
	public static int PORT1 = 1988;
	public static int PORT2 = 2010;
	//commend
	public static int OK = -2;
	public static int APPEXIT = -1;
	public static int GETCONTACT = 0;
	public static int PUTCONTACT = 1;
	
	public static ArrayList<String> mContacts = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException, InterruptedException{
		DataInputStream fromServer;
		DataOutputStream toServer;
		System.out.println("start");
		Runtime run = Runtime.getRuntime();
		try {
			run.exec(TOOL + S + STOP);
			Thread.sleep(3000);	
			run.exec(TOOL + "adb forward tcp:" + PORT1 +" tcp:" + PORT2);
			Thread.sleep(3000);		
			run.exec(TOOL + S + START);
			Thread.sleep(5000);			
			System.out.println("begin");

		}catch (IOException e) {
			System.out.println(e);
		}
		////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		Socket socket = null;
		try{
			InetAddress serverAddr = null;  
			serverAddr = InetAddress.getByName("127.0.0.1");
			socket = new Socket(serverAddr, PORT1);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			if(fromServer.readInt() == OK){
				System.out.println("OK");
				boolean flag = true; 
				while(flag){
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.print("input number(exit:-1,contacts:0):");
					String strWord = br.readLine();
					int cmd = Integer.parseInt(strWord);
					toServer.writeInt(cmd);
					toServer.flush();					

					if(cmd == APPEXIT){
						flag = false;
					}
					if(cmd == GETCONTACT){
						int b = fromServer.readInt();
						if(b == OK){
							String mContact;
							//ArrayList<String> mContacts = new ArrayList<String>();
							b = fromServer.readInt();
							for(int i = 0;i < b;i++){
								mContact = fromServer.readUTF();
								mContacts.add(mContact);
							}
							System.out.println("There are " + b + " Items");
							for(int i = 0;i < b;i++){
								System.out.println(mContacts.get(i));
							}
							//flag = false;//out

						}
					}
				}
			}
		} catch (UnknownHostException e1) {  
 
        } catch (Exception e2) {  

        } finally {  
            try {  
                if (socket != null) {  
                    socket.close();  
                    System.out.println("socket.close()");  
                }  
            } catch (IOException e) {  

            }  
        }
        /*int sum = mContacts.size();
        String mContact;
        Gson gson = new Gson();
        contact cc;
        for(int i = 0;i < sum;i++){
        	mContact = mContacts.get(i);
        	cc = gson.fromJson(mContact, contact.class);
        	System.out.println(cc.DISPLAY_NAME);
        }*/
	}
	
}
