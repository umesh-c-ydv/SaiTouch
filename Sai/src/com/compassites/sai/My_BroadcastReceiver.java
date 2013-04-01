package com.compassites.sai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class My_BroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Intent i = new Intent(arg0, Splash.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		arg0.startActivity(i);

	}

}
