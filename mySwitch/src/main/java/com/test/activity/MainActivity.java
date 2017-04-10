package com.test.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myswitch.R;

public class MainActivity extends Activity{
	public static final String TAG = "MainActivity";
	Switch mSwitch;
	private AutoCompleteTextView mAutoCompleteTextView;
	private MultiAutoCompleteTextView mMultiAutoCompleteTextView;
	private String[] strs=new String[]{
			"dddd8",
			"dddd7",
			"dddd6",
			"dddd5",
			"dddd4",
			"dddd3",
			"dddd2",
			"dddd1"
	};
	ArrayAdapter<String> adapter;
	int id=R.layout.activity_main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{

//		requestWindowFeature(Window.FEATURE_PROGRESS);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		setContentView(R.layout.activity_main);
//		mAutoCompleteTextView= (AutoCompleteTextView) findViewById(R.id.mAutoCompleteTextView);
//		mMultiAutoCompleteTextView= (MultiAutoCompleteTextView) findViewById(R.id.mMultiAutoCompleteTextView);
//
//		strs=getResources().getStringArray(R.array.strs);
//	//	UserAdapter adapter = new UserAdapter(this, R.layout.list_item);
//
//		adapter = new ArrayAdapter<>(this, R.layout.textview,strs);
//
////		adapter.add(new User(10, "小智", "男"));
////		adapter.add(new User(10, "小霞", "女"));
//		mAutoCompleteTextView.setAdapter(adapter);
//		mMultiAutoCompleteTextView.setAdapter(adapter);
//		mMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//		setProgressBarVisibility(true);
//		//setProgressBarIndeterminateVisibility(true);
//		setProgress(4500);

		// requestWindowFeature();的取值
		// 1.DEFAULT_FEATURES：系统默认状态，一般不需要指定
		// 2.FEATURE_CONTEXT_MENU：启用ContextMenu，默认该项已启用，一般无需指定
		// 3.FEATURE_CUSTOM_TITLE：自定义标题。当需要自定义标题时必须指定。如：标题是一个按钮时
		// 4.FEATURE_INDETERMINATE_PROGRESS：不确定的进度
		// 5.FEATURE_LEFT_ICON：标题栏左侧的图标
		// 6.FEATURE_NO_TITLE：无标题
		// 7.FEATURE_OPTIONS_PANEL：启用“选项面板”功能，默认已启用。
		// 8.FEATURE_PROGRESS：进度指示器功能
		// 9.FEATURE_RIGHT_ICON:标题栏右侧的图标

//        showFEATURE_INDETERMINATE_PROGRESS();

//        showFEATURE_CUSTOM_TITLE();

//        showFEATURE_LEFT_ICON();

//        showFEATURE_NO_TITLE();

		showFEATURE_PROGRESS();

		}catch (Exception e){
			Log.e(TAG, "onCreate: ",e );
		}

	}



	private void showFEATURE_INDETERMINATE_PROGRESS() {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(id);
		getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS, R.layout.progress);
		// 必须得加上否则显示不出效果 可以通过这个在以后设置显示或隐藏
		setProgressBarIndeterminateVisibility(true);
	}
	private void showFEATURE_CUSTOM_TITLE() {
		// 自定义标题。当需要自定义标题时必须指定。如：标题是一个按钮时
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(id);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customtitle);
	}
	private void showFEATURE_LEFT_ICON()
	{
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(id);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
	}
	private void showFEATURE_NO_TITLE()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(id);
		// 加上这句设置为全屏 不加则只隐藏title
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	private void showFEATURE_PROGRESS()
	{
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setProgressBarVisibility(true);
		setContentView(id);
		setTitle("");
		getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		// 通过线程来改变ProgressBar的值
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(1000);
						Message m = new Message();
						m.what = (i + 1) * 20;
						MainActivity.this.myMessageHandler.sendMessage(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	Handler myMessageHandler = new Handler() {
		// @Override
		public void handleMessage(Message msg) {
			// 设置标题栏中前景的一个进度条进度值
			setProgress(100 * msg.what);
			// 设置标题栏中后面的一个进度条进度值
			setSecondaryProgress(100 * msg.what + 10);
			super.handleMessage(msg);
		}
	};

	class UserAdapter extends ArrayAdapter<User> {
		private int mResourceId;

		public UserAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.mResourceId = textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			User user = getItem(position);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(mResourceId, null);
			TextView nameText = (TextView) view.findViewById(R.id.name);
			TextView ageText = (TextView) view.findViewById(R.id.age);
			TextView sexText = (TextView) view.findViewById(R.id.sex);

			nameText.setText(user.getName());
			ageText.setText(user.getAge());
			sexText.setText(user.getSex());

			return view;
		}
	}

	class User {
		private int mAge;
		private String mName;
		private String mSex;

		public User(int age, String name, String sex) {
			this.mAge = age;
			this.mName = name;
			this.mSex = sex;
		}

		public String getName() {
			return this.mName;
		}

		public String getAge() {
			return this.mAge + "";
		}

		public String getSex() {
			return this.mSex;
		}
	}
}
