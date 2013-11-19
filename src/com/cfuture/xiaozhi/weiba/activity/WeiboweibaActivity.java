package com.cfuture.xiaozhi.weiba.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cfuture.xiaozhi.weiba.FileUitl.FileUtil;
import com.cfuture.xiaozhi.weiba.FileUitl.PhoneType;
import com.cfuture.xiaozhi.weiba.other.ShellCommand;

/**
 * 主界面
 * 
 * @author cfuture_小智
 * 
 */
public class WeiboweibaActivity extends Activity {

	/** Called when the activity is first created. */

	private Context mContext;
	private Button bSure;
	private Button bHelp;
	private Spinner phoneModelSpin;

	private ShellCommand shellCommand;
	private List<PhoneType> phones;
	private boolean isRoot;
	private Handler handler;
	private ProgressDialog pgd;

	// 已选择的手机型号
	private String modelSelected;
	private String munaSelected;

	/** 路径常量 **/
	public final static String SYSTEM_PATH = "/system";
	private final static String SYSTEM_BUILD = "system/build.prop";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mContext = this;
		shellCommand = new ShellCommand();
		initSU();
		initSpinner();
		pgd = new ProgressDialog(mContext);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// 取消对话框
				pgd.dismiss();
				Toast.makeText(mContext, getString(R.string.tip_after_modify),
						Toast.LENGTH_SHORT).show();
			}

		};
		// 如果没ROOT,按钮设为不可用
		if (!isRoot) {
			bSure.setClickable(false);
		}
		bSure = (Button) findViewById(R.id.sure);
		bSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// handler.sendEmptyMessage(MESSAGE_CREATE_PROCESS);
				pgd.setMessage("正在修改,请不要取消");
				pgd.show();
				new Thread() {
					public void run() {
						modifyPhoneType(modelSelected, munaSelected);
					}
				}.start();
			}
		});
		bHelp = (Button) findViewById(R.id.help);
		bHelp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,HelpActivity.class);
				startActivity(intent);
			}});
	}

	/**
	 * 
	 * 初始化机型选择下拉列表框
	 */
	private void initSpinner() {
		phoneModelSpin = (Spinner) findViewById(R.id.spinner);
		phones = new ArrayList<PhoneType>();
		InputStream input = null;
		try {
			input = getAssets().open("phonemodel/android.txt");
			List<String> datas = FileUtil.readLines(input);
			for (String string : datas) {
				String[] result = string.split(" {8,}");
				if (result.length != 3) {
					continue;
				}
				PhoneType phone = new PhoneType();
				phone.setShowFrom(result[0]);
				phone.setModel(result[1]);
				phone.setManufacturer(result[2]);
				phones.add(phone);
			}
			phoneModelSpin.setAdapter(new mSpinnerAdapter(phones));

			// 设置选择事件
			phoneModelSpin
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							PhoneType phoneType = phones.get(position);

							modelSelected = phoneType.getModel();
							munaSelected = phoneType.getManufacturer();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							Toast.makeText(mContext, "Nothing Select",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 初始化su,判断是否ROOT
	 */
	private void initSU() {

		if (shellCommand.canSU()) {
			isRoot = true;
		} else {
			isRoot = false;
			Toast.makeText(mContext, "您的手机没有ROOT权限,不能修修改,请ROOT后使用该软件",
					Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 修改厂商与手机型号
	 * 
	 * @param model
	 * @param manu
	 */
	private void modifyPhoneType(String model, String manu) {

		FileUtil fileUtil = new FileUtil();
		try {
			shellCommand.su.runWaitFor(ShellCommand.MOUNT_SYSTEM_RW);
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean writeable = FileUtil.isDirWrite(SYSTEM_PATH);
		if (!writeable) {

			fileUtil.modifyFilePression(SYSTEM_PATH, ShellCommand.CHMOD_0777);
		}
		if (!FileUtil.isDirWrite(SYSTEM_BUILD)) {
			fileUtil.modifyFilePression(SYSTEM_BUILD, ShellCommand.CHMOD_0777);
		}

		// 修改文件中的手机厂商型号
		fileUtil.modifyPhoneType(model, manu);

		// 把文件的权限降回原来的权限
		fileUtil.modifyFilePression(SYSTEM_BUILD, ShellCommand.CHMOD_0644);

		// 完成操作,取消进度条
		handler.sendEmptyMessage(1);
		// fileUtil.modifyFilePression(path, permission)
	}

	/**
	 * spinner Adapter适配器
	 * 
	 * @author cfuture_小智
	 * @Description
	 */
	class mSpinnerAdapter extends BaseAdapter {
		private List<PhoneType> mPhoneModels;

		public mSpinnerAdapter(List<PhoneType> phoneModels) {
			mPhoneModels = phoneModels;
		}

		@Override
		public int getCount() {
			return mPhoneModels.size();
		}

		@Override
		public Object getItem(int position) {

			return mPhoneModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater li = getLayoutInflater();
			View item = li.inflate(R.layout.phone_item, null);
			TextView phoneName = (TextView) item.findViewById(R.id.phone_model);
			phoneName.setText(mPhoneModels.get(position).getShowFrom());
			return item;
		}

	}
}