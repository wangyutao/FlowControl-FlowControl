package com.flowcontrol.plugins.setting.bean;

public class FCSettingBean {
	public boolean mIsMessageRemind;// 是否消息提醒
	public float mOutOfFlow;// 超过流量提醒
	public int mCheckFlowMinute;// 隔几分钟检测

	public FCSettingBean(boolean isMessageRemind, float outOfFlow, int checkFlowMinute) {
		mIsMessageRemind = isMessageRemind;
		mOutOfFlow = outOfFlow;
		mCheckFlowMinute = checkFlowMinute;
	}

}
