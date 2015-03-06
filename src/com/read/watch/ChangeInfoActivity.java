package com.read.watch;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class ChangeInfoActivity  extends Activity  implements OnClickListener {
	private EditText phone;
	private EditText truename;
	private EditText sex;
	private EditText address;
	private EditText email;
	private Button back;
	private Button change;
	private String message;
	private TextView localusername;
	
	private TextView usertype;
	private final int SUCCESS = 0;//���سɹ�
	private final int NONEWS = 1;//����Ŀ��û������
	private final int NOMORENEWS = 2;//����Ŀ��û�и�������
	private final int LOADERROR = 3;//����ʧ��
	private ProgressBar mLoadnewsProgress;
	private ChangeTask myTask;
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private HashMap<String, Object> hashmap=new HashMap<String, Object>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeinfo);
		sharedpreferences=getSharedPreferences("user",MODE_PRIVATE);
		editor=sharedpreferences.edit();
		localusername=(TextView)findViewById(R.id.localusername);
		localusername.setText(sharedpreferences.getString("USERNAME", null));
		phone=(EditText)findViewById(R.id.editphone);
		truename=(EditText)findViewById(R.id.editname);
		sex=(EditText)findViewById(R.id.editsex);
		address=(EditText)findViewById(R.id.editaddress);
		email=(EditText)findViewById(R.id.editemail);
		usertype=(TextView)findViewById(R.id.usertype);
		mLoadnewsProgress=(ProgressBar)findViewById(R.id.progressBar1);	
		back=(Button)findViewById(R.id.back);	
		back.setOnClickListener(this);
		
		change=(Button)findViewById(R.id.change);	
		change.setOnClickListener(this);
		String temp=sharedpreferences.getString("MOBILEPHONE", null);
		phone.setText(temp.toString());
		temp=sharedpreferences.getString("TRUENAME", null);
		truename.setText(temp);
		temp=sharedpreferences.getString("SEX", null);
		sex.setText(temp);
		temp=sharedpreferences.getString("DOMICILE", null);
		address.setText(temp);
		temp=sharedpreferences.getString("E_MAIL", null);
		email.setText(temp);
		
		
		if(sharedpreferences.getInt("USERTYPE", 0)==1)
		{
			usertype.setText("��ͨ�û�");
			
		}
		else 
			usertype.setText("����Ա");
		//myTask = new GetinfoAsyncTask();
		//AsyncTask��ʵ��
	
		//���ﻻ����Ҫ�ϴ����ļ�·��
			
		//myTask.execute();
				
		   
	}
	
	public class ChangeTask extends AsyncTask<Object, Integer, Integer>
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
			return change((String)params[0],(String)params[1],(String)params[2],(String)params[3],(String)params[4]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(ChangeInfoActivity.this, "�޸�ʧ��", Toast.LENGTH_LONG).show();break;
			case SUCCESS:
				Toast.makeText(ChangeInfoActivity.this, "�޸ĳɹ�", Toast.LENGTH_LONG).show();break;
		    default:
		    	Toast.makeText(ChangeInfoActivity.this, message, Toast.LENGTH_LONG).show();break;
			
			
			}
			
			mLoadnewsProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	
	
	private int change(String sex,String mail,String name,String mobile,String address)
	{
		
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/infochange";
		String username=sharedpreferences.getString("username", null);
		String password=sharedpreferences.getString("password", null);
		String params = "username="+username+"&password="+password+"&sex="+sex+"&email="+mail+"&truename="+name+"&phone="+mobile+"&address="+address;
	
		SyncHttp syncHttp = new SyncHttp();
		try
		{
			//��Get��ʽ���󣬲���÷��ؽ��
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			//��ȡ�����룬0��ʾ�ɹ�
			int retCode = jsonObject.getInt("ret");
			if (0==retCode)
			{
				Boolean result = jsonObject.getBoolean("result");
				if (result==true)
				{
				
					return SUCCESS;
				}
				else 
					return NONEWS;
				
			}
			else
			{
				message=jsonObject.getString("msg");
				return LOADERROR;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			
			return LOADERROR;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.back: 
			
			Intent intent=new Intent(ChangeInfoActivity.this,DetailinfoActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.change:
			
			if((sex.getText().toString().equals("��")||sex.getText().toString().equals("Ů")))
				{myTask=new ChangeTask();
			
			myTask.execute((String)sex.getText().toString(),(String)email.getText().toString(),(String)truename.getText().toString(),(String)phone.getText().toString(),(String)address.getText().toString());
				}
			else
				Toast.makeText(ChangeInfoActivity.this, "�Ա�����Ŷ", Toast.LENGTH_LONG).show();
				break;
		}
	}
	
}
