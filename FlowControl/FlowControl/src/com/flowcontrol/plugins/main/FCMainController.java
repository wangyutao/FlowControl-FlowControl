package com.flowcontrol.plugins.main;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.flowcontrol.FCAppController;
import com.flowcontrol.R;
import com.flowcontrol.plugins.FCPlugin;
import com.flowcontrol.plugins.main.bean.MainInformationBean;
import com.flowcontrol.plugins.main.state.FCMainViewBase;
import com.flowcontrol.plugins.main.state.FCMainView_AppList;
import com.flowcontrol.plugins.main.state.FCMainView_AppListDelete;
import com.flowcontrol.plugins.main.state.FCMainView_Detail;
import com.flowcontrol.plugins.main.ui.MainListAdapter;

public class FCMainController extends FCPlugin {
	public static final String NAME = "main";
	public List<MainInformationBean> mInformationBeans = new ArrayList<MainInformationBean>();
	FCMainViewBase mMainView;
	public boolean mMainListChang = false;
	public MainListAdapter mMainListAdapter;

	public FCMainController(FCAppController app) {
		super(app);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void enable() {

		mInformationBeans = mApp.getLocationContext().getInformationTable().getAllInformation();
		if (mInformationBeans.size() == 0) {
			mInformationBeans = getInstalledApps();
		}
		mMainView = new FCMainView_AppList(mApp);
		showView();
	}

	public void showView() {

		mApp.getMainHandler().post(new Runnable() {
			public void run() {

				mMainView.showView();
			}
		});
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	public void changToDelete() {
		mMainView = new FCMainView_AppListDelete(mApp);
		showView();
	}

	public void changToDetail(MainInformationBean bean) {
		if (mMainView.getClass() == FCMainView_AppList.class) {
			mMainView = new FCMainView_Detail(mApp, bean);

			showView();
		}
	}

	public void changToMainList() {
		if (mMainView.getClass() == FCMainView_Detail.class) {
			mMainView = new FCMainView_AppList(mApp);

			mApp.getMainHandler().post(new Runnable() {
				public void run() {
					mApp.getActivity().setContentView(R.layout.app_list);
					mMainView.showView();
				}
			});
		}
	}

	/**
	 * 获取系统所有已安装的APP信息
	 * 
	 * @param getSysPackages
	 * @return
	 */
	public static ArrayList<MainInformationBean> getInstalledApps() {
		ArrayList<MainInformationBean> beans = new ArrayList<MainInformationBean>();
		List<PackageInfo> packs = mApp.getActivity().getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((p.versionName == null) || !filterApp(p.applicationInfo)) {
				continue;
			}

			String appname = p.applicationInfo.loadLabel(mApp.getActivity().getPackageManager()).toString();
			String pname = p.packageName;
			String versionName = p.versionName;
			int versionCode = p.versionCode;
			int uid = p.applicationInfo.uid;

			long rx = TrafficStats.getUidRxBytes(uid);
			long tx = TrafficStats.getUidTxBytes(uid);
			long userFlow = 0;
			if ((rx + tx) > 0) {
				userFlow = rx + tx;
			}

			MainInformationBean newInfo = new MainInformationBean(appname, true, pname, versionName, versionCode, uid, userFlow, 0);

			boolean insertResult = mApp.getLocationContext().getInformationTable().insertAppInformation(newInfo);
			if (!insertResult) {// 说明不是新增的
				MainInformationBean databaseBean = mApp.getLocationContext().getInformationTable().getAppInformation(appname, uid);
				newInfo.mUserFlow = userFlow + databaseBean.mUserFlow;
				newInfo.mLimitUserFlow = databaseBean.mLimitUserFlow;
			}

			beans.add(newInfo);
		}

		return beans;
	}

	/**
	 * 判断某一个应用程序是不是系统的应用程序， 如果是返回true，否则返回false。
	 */
	public static boolean filterApp(ApplicationInfo info) {
		// 有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {// 判断是不是系统应用
			return true;
		}
		return false;
	}

}
