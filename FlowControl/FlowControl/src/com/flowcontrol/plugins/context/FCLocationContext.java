package com.flowcontrol.plugins.context;

import android.net.TrafficStats;

import com.flowcontrol.FCAppController;
import com.flowcontrol.plugins.FCPlugin;
import com.flowcontrol.plugins.db.FCDBHelper;
import com.flowcontrol.plugins.db.FCDB_Information;

public class FCLocationContext extends FCPlugin {
	public static final String NAME = "context";
	private FCDBHelper helper_;

	public FCLocationContext(FCAppController app) {
		super(app);
		helper_ = new FCDBHelper(app);
	}

	public String getName() {
		return NAME;
	}

	@Override
	public void enable() {
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	public long getUserFlow(int uid) {
		// 如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
		long rx = TrafficStats.getUidRxBytes(uid);
		// 如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
		long tx = TrafficStats.getUidTxBytes(uid);
		long userFlow = 0;
		if ((rx + tx) > 0) {
			userFlow = rx + tx;
		}
		return userFlow;
	}

	public FCDB_Information getInformationTable() {
		return helper_.getInformation();
	}
}
