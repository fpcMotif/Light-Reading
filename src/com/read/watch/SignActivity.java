package com.read.watch;



/**
 * 
 */


import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * ����Activity
 
 */
public class SignActivity extends Activity implements OnClickListener{
	private SigninAsyncTask signintask;
	private EditText username;
	private EditText password;
	private ProgressBar mLoadnewsProgress;
	private Button signin;
	private Button signback;
	private Button more;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		mLoadnewsProgress = (ProgressBar)findViewById(R.id.progressBar1);
		signin = (Button)findViewById(R.id.signin);
		signin.setOnClickListener(this);
		more = (Button)findViewById(R.id.more);
		more.setOnClickListener(this);
		username=(EditText)findViewById(R.id.sign_edit_account);
		password=(EditText)findViewById(R.id.sign_edit_pwd);
		 
		 
		signback = (Button)findViewById(R.id.signback);
		signback.setOnClickListener(this);
		 
		
	    
	     
	}
	
	
	

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.signin: 
			signintask=new SigninAsyncTask();
			signintask.execute(username.getText().toString(),password.getText().toString());
			
			break;
		case R.id.signback: 
			finish();
			break;
		case R.id.more:
			Toast.makeText(SignActivity.this, "��û��Ȩ��Ŷ", Toast.LENGTH_LONG).show();break;

			default:break;
		}
		
	}
	
	
	public class SigninAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			
			//��ʾ������
			mLoadnewsProgress.setVisibility(View.VISIBLE); 
		
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			return getsignin((String)params[0],(String)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case 1:
				Toast.makeText(SignActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				
				break;
			case 2:
				Toast.makeText(SignActivity.this, "������ע���û���", Toast.LENGTH_LONG).show();break;
			case 3:
				Toast.makeText(SignActivity.this, "�����޷�����", Toast.LENGTH_LONG).show();break;
			}
			
			
			//���ؽ�����
			mLoadnewsProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	private int getsignin(String username,String password)
	{
	
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/signin";
		String params = "username="+username+"&password="+password;
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			//��Get��ʽ���󣬲���÷��ؽ��
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			//��ȡ�����룬0��ʾ�ɹ�
			int retCode = jsonObject.getInt("ret");
			System.out.println(retCode);
			if (0==retCode)
			{
				JSONObject dataObject = jsonObject.getJSONObject("data");
				//��ȡ������Ŀ
				Boolean bool = dataObject.getBoolean("bool");
				System.out.println(bool);
				if (bool)
				{
					
					
					return 1;
				}
				else
				{
					return 2;
				}
			}
			else
			{
				return 3;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return 3;
		}
	}
	
}
