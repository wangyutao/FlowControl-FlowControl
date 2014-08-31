package com.flowcontrol.plugins.message;

import android.app.Activity;
import android.content.Intent;

import com.flowcontrol.FCAppController;
import com.flowcontrol.plugins.FCPlugin;

public class FCMessageController extends FCPlugin {
	public static final String NAME = "messageController";

	public FCMessageController(FCAppController app) {
		super(app);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void enable() {
		Activity activity = mApp.getActivity();
		Intent intent = new Intent();
		intent.setClass(activity, FCServiceState_Message.class);
		activity.startService(intent);
	}

	@Override
	public void disable() {
	}

}
