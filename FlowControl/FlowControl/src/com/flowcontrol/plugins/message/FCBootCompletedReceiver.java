package com.flowcontrol.plugins.message;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.flowcontrol.FCAppController;
import com.flowcontrol.log_manager.FCLog;
import com.flowcontrol.plugins.main.bean.MainInformationBean;

/**
 * 
 * 
 * @author yejunrong
 * 
 */
public class FCBootCompletedReceiver extends BroadcastReceiver {

	public FCBootCompletedReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		FCAppController mApp = (FCAppController) context.getApplicationContext();

		FCLog.i("FCBootCompletedReceiver onReceive=========");

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {// 开机启动广播
			//

			Intent serviceIntent = new Intent(context, FCMessageService.class);
			context.startService(serviceIntent);

		} else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {// 关机广播
			//
			List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

			for (int i = 0; i < packs.size(); i++) {
				PackageInfo p = packs.get(i);
				if ((p.versionName == null) || !filterApp(p.applicationInfo)) {
					continue;
				}

				String appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
				// String pname = p.packageName;
				// String versionName = p.versionName;
				// int versionCode = p.versionCode;
				int uid = p.applicationInfo.uid;

				// 如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
				long rx = TrafficStats.getUidRxBytes(uid);
				// 如果返回-1，代表不支持使用该方法，注意必须是2.2以上的
				long tx = TrafficStats.getUidTxBytes(uid);
				long userFlow = 0;
				if ((rx + tx) > 0) {
					userFlow = rx + tx;
				}

				MainInformationBean databaseBean = mApp.getLocationContext().getInformationTable().getAppInformation(appname, uid);
				databaseBean.mUserFlow = userFlow + databaseBean.mUserFlow;
				int updateResult = mApp.getLocationContext().getInformationTable().updateAppInformationUserFlow(databaseBean);
				FCLog.i("updateResult=" + updateResult);
				FCLog.i("手机即将关闭，需要 update 流量 appname=" + appname + " uid=" + uid + "  userFlow=" + userFlow + "  databaseBean.mUserFlow="
						+ databaseBean.mUserFlow);
			}

		}
	}

	public boolean filterApp(ApplicationInfo info) {
		// 有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 判断是不是系统应用
			return true;
		}
		return false;
	}
}
