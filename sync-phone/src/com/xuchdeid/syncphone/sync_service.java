package com.xuchdeid.syncphone;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class sync_service extends Service implements contacts.transform{	
	public static String TAG = "TAG";
	//socket
	private ServerSocket serverSocket;
	public static int PORT1 = 1988;
	public static int PORT2 = 2010;
	//commend
	public static int OK = -2;
	public static int APPEXIT = -1;
	public static int GETCONTACT = 0;
	public static int PUTCONTACT = 1;
	
	DataOutputStream outputToClick;
	DataInputStream inputFromClick;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onCreate() {
		super.onCreate();
		new Thread() {  
            public void run() {  
					try {
						Listen();
					} catch (IOException e) {
						e.printStackTrace();
					}			 
            };  
        }.start();
	}
	
	public void Listen() throws IOException{
		serverSocket = null;
        try {   
            serverSocket = new ServerSocket(PORT2);     
            boolean flag = true;
            Socket client = serverSocket.accept();
            inputFromClick = new DataInputStream(client.getInputStream());
            outputToClick = new DataOutputStream(client.getOutputStream());
            outputToClick.writeInt(OK);
            while(flag){
            	int cmd = inputFromClick.readInt();
            	if(cmd == APPEXIT){
            		flag = false;
            		client.close();
            	}
            	if(cmd == GETCONTACT){
            		outputToClick.writeInt(OK);
            		contacts con = new contacts(this,this);
            		con.getCursor();

            	}
            }
        }catch (IOException e) {
            e.printStackTrace();  
        }
        this.stopService(new Intent(this, sync_service.class));
    
	}
	
	public void onDestroy(){
		super.onDestroy();
	}

	public void put(String a) {
		// TODO Auto-generated method stub
		try {
			outputToClick.writeUTF(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void put(int a) {
		// TODO Auto-generated method stub
		try {
			outputToClick.writeInt(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
