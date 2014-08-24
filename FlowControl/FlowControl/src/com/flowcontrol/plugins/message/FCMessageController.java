package com.flowcontrol.plugins.message;

import android.app.Activity;
import android.content.Intent;

import com.flowcontrol.FCAppController;
import com.flowcontrol.plugins.FCPlugin;

public class FCMessageController extends FCPlugin {
	public static final String NAME = "messageController";

	public FCMessageController(FCAppController app) {
		super(app);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void enable() {
		Activity activity = mApp.getActivity();
		Intent intent = new Intent();
		intent.setClass(activity, FCMessageService.class);

		activity.startService(intent);
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

}
