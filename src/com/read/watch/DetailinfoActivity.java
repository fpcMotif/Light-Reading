package com.read.watch;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetailinfoActivity  extends Activity  implements OnClickListener {
	private Button back;
	private Button edit;
	private TextView localusername;
	private TextView phone;
	private TextView truename;
	private TextView sex;
	private TextView address;
	private TextView email;
	private TextView usertype;
	private final int SUCCESS = 0;//���سɹ�
	private final int NONEWS = 1;//����Ŀ��û������
	private final int NOMORENEWS = 2;//����Ŀ��û�и�������
	private final int LOADERROR = 3;//����ʧ��
	private ProgressBar mLoadnewsProgress;
	private GetinfoAsyncTask myTask;
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private HashMap<String, Object> hashmap=new HashMap<String, Object>();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);
		sharedpreferences=getSharedPreferences("user",MODE_PRIVATE);
		editor=sharedpreferences.edit();
		localusername=(TextView)findViewById(R.id.localusername);
		phone=(TextView)findViewById(R.id.phone);
		truename=(TextView)findViewById(R.id.truename);
		sex=(TextView)findViewById(R.id.sex);
		address=(TextView)findViewById(R.id.address);
		email=(TextView)findViewById(R.id.email);
		usertype=(TextView)findViewById(R.id.usertype);
		mLoadnewsProgress=(ProgressBar)findViewById(R.id.progressBar1);	
		back=(Button)findViewById(R.id.back);	
		back.setOnClickListener(this);
		edit=(Button)findViewById(R.id.edit);	
		edit.setOnClickListener(this);
		myTask = new GetinfoAsyncTask();
		//AsyncTask��ʵ��
	
		//���ﻻ����Ҫ�ϴ����ļ�·��
			
		myTask.execute();
				
		   
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.back: 
			finish();
			break;
		case R.id.edit:
			Intent intent=new Intent(DetailinfoActivity.this,ChangeInfoActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}
	
	public class GetinfoAsyncTask extends AsyncTask<Object, Integer, Integer>
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
			return getinfo();
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(DetailinfoActivity.this, "û�������Ϣ", Toast.LENGTH_LONG).show();break;
			case NOMORENEWS:
				Toast.makeText(DetailinfoActivity.this, "û�и�����Ϣ", Toast.LENGTH_LONG).show();break;
			case LOADERROR:
				Toast.makeText(DetailinfoActivity.this, "��ȡ��Ϣʧ��", Toast.LENGTH_LONG).show();break;
			case SUCCESS:
				phone.setText((String)hashmap.get("MOBILEPHONE"));
				editor.putString("MOBILEPHONE", (String)hashmap.get("MOBILEPHONE"));
				
				truename.setText((String)hashmap.get("TRUENAME"));
				editor.putString("TRUENAME", (String)hashmap.get("TRUENAME"));
				
				sex.setText((String)hashmap.get("SEX"));
				editor.putString("SEX", (String)hashmap.get("SEX"));
				
				address.setText((String)hashmap.get("DOMICILE"));
				editor.putString("DOMICILE", (String)hashmap.get("DOMICILE"));
				
				email.setText((String)hashmap.get("E_MAIL"));
				editor.putString("E_MAIL", (String)hashmap.get("E_MAIL"));
				
				
				localusername.setText((String)hashmap.get("USERNAME"));
				editor.putString("USERNAME", (String)hashmap.get("USERNAME"));
				if((Integer)hashmap.get("USERTYPE")==1)
				usertype.setText("��ͨ�û�");
				else usertype.setText("����Ա");
				
				editor.putInt("USERTYPE", (Integer)hashmap.get("USERTYPE"));
				editor.commit();
				break;
			}
			
		
			//���ؽ�����
			mLoadnewsProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	
	
	private int getinfo()
	{
		
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/peopleinfo";
		String username=sharedpreferences.getString("username", null);
		String password=sharedpreferences.getString("password", null);
		String params = "username="+username+"&password="+password;
	
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
				JSONObject dataObject = jsonObject.getJSONObject("data");
				//��ȡ������Ŀ
				int totalnum = dataObject.getInt("totalnum");
				if (totalnum>0)
				{
				
					JSONObject detail = dataObject.getJSONObject("detail");
					
					hashmap=new HashMap<String, Object>();
					hashmap.put("USERNAME", detail.getString("USERNAME"));
					hashmap.put("TRUENAME", detail.getString("TRUENAME"));
					hashmap.put("MOBILEPHONE", detail.getString("MOBILEPHONE"));
					hashmap.put("E_MAIL", detail.getString("E_MAIL"));
					hashmap.put("DOMICILE", detail.getString("DOMICILE"));
					hashmap.put("SEX", detail.getString("SEX"));
					hashmap.put("USERTYPE", detail.getInt("USERTYPE"));
					return SUCCESS;
				}
				else 
					return NONEWS;
				
			}
			else
			{
				return LOADERROR;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return LOADERROR;
		}
	}
	
}
