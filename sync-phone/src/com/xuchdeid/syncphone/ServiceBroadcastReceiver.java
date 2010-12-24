package com.xuchdeid.syncphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceBroadcastReceiver extends BroadcastReceiver {
	private static String START_ACTION = "com.xuchdeid.syncphone.start";
	private static String STOP_ACTION = "com.xuchdeid.syncphone.stop";
	private static String DEBUG_ACTION = "com.xuchdeid.syncphone.debug";
	

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		if (START_ACTION.equals(action)) {
			context.startService(new Intent(context, sync_service.class));
			
		} 
		if (STOP_ACTION.equals(action)) {
			context.stopService(new Intent(context, sync_service.class));
			
		}
		if (DEBUG_ACTION.equals(action)) {
			context.startActivity(new Intent(context, syncphone.class));			
		}
	}
}