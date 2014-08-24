package com.flowcontrol.plugins.setting.state;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.flowcontrol.FCAppController;
import com.flowcontrol.R;
import com.flowcontrol.log_manager.FCLog;
import com.flowcontrol.plugins.setting.FCSettingBase;

public class FCSetting_MainView extends FCSettingBase {

	public FCSetting_MainView(FCAppController controller) {
		super(controller);
	}

	public void showView() {
		mApp.getActivity().setContentView(R.layout.setting_main);
		mApp.getActivity().findViewById(R.id.special_setting).setOnClickListener(onClickListener);

		Switch isOpenSwitch = (Switch) mApp.getActivity().findViewById(R.id.is_open_switch);
		isOpenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					FCLog.i("打开");
				} else {
					FCLog.i("关闭");
				}
			}
		});

		Spinner flowSpinner = (Spinner) mApp.getActivity().findViewById(R.id.out_of_flow_spinner);
		flowSpinner.setOnItemSelectedListener(spinnerSelectListener);

		Spinner check_flow_spinner = (Spinner) mApp.getActivity().findViewById(R.id.check_flow_spinner);
		check_flow_spinner.setOnItemSelectedListener(spinnerSelectListener);

		mApp.getActivity().findViewById(R.id.detail_back).setOnClickListener(onClickListener);

	}

	Spinner.OnItemSelectedListener spinnerSelectListener = new Spinner.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()) {
			case R.id.out_of_flow_spinner:
				String[] remindFlowArray = mApp.getActivity().getResources().getStringArray(R.array.remind_flow);
				String remindFlowString = remindFlowArray[position];
				int remindFlowInt = Integer.parseInt(remindFlowString.substring(0, 2));
				FCLog.i("select remind flow = " + remindFlowInt);

				break;
			case R.id.check_flow_spinner:
				String[] checkFlowArray = mApp.getActivity().getResources().getStringArray(R.array.every_minute_check_flow);
				String checkFlowString = checkFlowArray[position];
				int checkFlowInt = Integer.parseInt(checkFlowString.substring(0, 1));
				FCLog.i("select remind flow = " + checkFlowInt);

				break;
			default:
				break;
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			FCLog.i(" onNothingSelected ");
		}
	};

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.special_setting:
				FCLog.i("特殊设置。。。。。。");
				mApp.getSettingControll().toSettingFilterPage();

				break;

			case R.id.detail_back:
				mApp.getMainController().enable();
			default:
				break;
			}
		}
	};

}
