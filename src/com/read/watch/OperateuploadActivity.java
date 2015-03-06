package com.read.watch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.read.watch.PullToRefreshView.OnFooterRefreshListener;
import com.read.watch.PullToRefreshView.OnHeaderRefreshListener;
import com.read.watch.main.NewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class OperateuploadActivity  extends Activity implements OnHeaderRefreshListener,OnFooterRefreshListener,OnClickListener{
	private final int NEWSCOUNT = 5; 
	private final int SUCCESS = 0;
	private final int NONEWS = 1;
	private final int NOMORENEWS = 2;
	private final int LOADERROR = 3;
	private ListView listView; 
	private Historyitem_adaptor adapter;
	private Context context;
	private SharedPreferences.Editor editor;
	private SharedPreferences sharedpreferences;
	private LoadUploadnewAsyncTask loaduploadnewasynctask;
	private PullToRefreshView mPullToRefreshView;
	private ProgressBar mLoadnewsProgress;
	private Button fresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		mLoadnewsProgress = (ProgressBar)findViewById(R.id.refreshprocess);
		fresh = (Button)findViewById(R.id.refresh);
		fresh.setOnClickListener(this);
	
		 mPullToRefreshView = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
		 sharedpreferences=getSharedPreferences("user",MODE_PRIVATE);
		listView = (ListView) findViewById(R.id.listView1);
		editor=sharedpreferences.edit(); 
		adapter = new Historyitem_adaptor(this,listView);
		listView.setAdapter(adapter);
		 
		TextView activitytitle =  (TextView) findViewById(R.id.TextView01);
		activitytitle.setText("�ϴ�����");
		
		
		 
		 mPullToRefreshView.setOnHeaderRefreshListener(this);
	       mPullToRefreshView.setOnFooterRefreshListener(this);
	       listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
				{
					Toast.makeText(OperateuploadActivity.this, "�Ѿ�����", Toast.LENGTH_LONG).show();

					  Intent intent = new Intent(OperateuploadActivity.this,DetailuploadActivity.class);
						Bundle bundle = new Bundle();
					
						NewModel model = adapter.getModel().get(arg2);
						bundle.putString("new_id",model.id);
						bundle.putString("pic_url", model.pic);
						bundle.putString("new_title", model.title);
						bundle.putString("content", model.info);
						bundle.putInt("state", model.new_state);
						bundle.putString("photo2", model.photo2);
						bundle.putString("photo3", model.photo3);
						bundle.putString("video", model.video);
						intent.putExtras(bundle);
						intent.putExtras(bundle);
						startActivityForResult(intent, 1);
					
				}
			});
	       
	      //��ӳ������
	        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
												ContextMenuInfo menuInfo) {
					menu.setHeaderTitle("����");   
					menu.add(0, 0, 0, "ת��");
					menu.add(0, 1, 0, "�ղ�");   
				}
			}); 
	    
	    
	        new Thread(new Runnable(){   

	            public void run(){   

	                try {
						Thread.sleep(1000);
						adapter.clean();
						loaduploadnewasynctask = new LoadUploadnewAsyncTask();
				        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				        loaduploadnewasynctask.execute(time,true);
					   
					   
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}   

	               

	            }   

	        }).start(); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		adapter.clean();
		loaduploadnewasynctask = new LoadUploadnewAsyncTask();
        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
         loaduploadnewasynctask.execute(time,true);
	     adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
				
				loaduploadnewasynctask = new LoadUploadnewAsyncTask();
				String time=new String();
		      //  String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				if(adapter.getCount()>0)
				{NewModel model = adapter.getModel().get(adapter.getCount()-1);
				 time=model.time;
				 String []a=time.split("[ :-]");
				 String c=new String();
				 for(int i=0;i<a.length;i++)
					 c+=a[i];
				 time=c;
				}
				else
					 time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				Toast.makeText(OperateuploadActivity.this, time, Toast.LENGTH_LONG).show();
				loaduploadnewasynctask.execute(time,false);
				  adapter.notifyDataSetChanged();
		
				
			
			    
			    
	}
		}, 1000);
	}
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//设置更新时间
				adapter.clean();
				loaduploadnewasynctask = new LoadUploadnewAsyncTask();
		        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		        loaduploadnewasynctask.execute(time,true);
			     adapter.notifyDataSetChanged();
			   
				mPullToRefreshView.onHeaderRefreshComplete();
				
			
				
			}
		},1000);
				 
	}
	private void reload(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				 adapter.notifyDataSetChanged();
				 loaduploadnewasynctask = new LoadUploadnewAsyncTask();
		        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		        loaduploadnewasynctask.execute(time,true);
	           
				 adapter.notifyDataSetChanged();

		
			}
		}).start();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class LoadUploadnewAsyncTask extends AsyncTask<Object, Integer, Integer>
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
			return getSpeCateNews((String)params[0],adapter,(Boolean)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case NONEWS:
				Toast.makeText(OperateuploadActivity.this, "û����Ϣ", Toast.LENGTH_LONG).show();break;
			case NOMORENEWS:
				Toast.makeText(OperateuploadActivity.this, "û�и�����Ϣ", Toast.LENGTH_LONG).show();break;
			case LOADERROR:
				Toast.makeText(OperateuploadActivity.this, "��ȡ��Ϣʧ��", Toast.LENGTH_LONG).show();break;
			}
			adapter.notifyDataSetChanged();
			
			//���ؽ�����
			mLoadnewsProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	
	
	private int getSpeCateNews(String time,Historyitem_adaptor newsList,Boolean firstTimes)
	{
		if (firstTimes)
		{
			//����ǵ�һ�Σ�����ռ���������
			newsList.clean();
		}
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/getupload";
		String a="";
		String params = "username="+a+"&time="+time;
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
				Log.d("�ٱ�����",new Integer(totalnum).toString());
				if (totalnum>0)
				{
					//��ȡ�������ż���
					JSONArray newslist = dataObject.getJSONArray("newslist");
					for(int i=0;i<newslist.length();i++)
					{
						JSONObject newsObject = (JSONObject)newslist.opt(i); 
						
					
					    newsList.addNew(newsObject.getString("USERNAME"),newsObject.getString("INFOR_ID"),newsObject.getString("TITLE"),newsObject.getString("CONTEXT"),newsObject.getString("PHOTO1"),newsObject.getString("REPORT_TIME"),newsObject.getInt("STATE"),newsObject.getString("PHOTO2"),newsObject.getString("PHOTO3"),newsObject.getString("VIDEO"));
					}
					return SUCCESS;
				}
				else
				{
					if (firstTimes)
					{
						return NONEWS;
					}
					else
					{
						return NOMORENEWS;
					}
				}
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



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.refresh: 
			Toast.makeText(OperateuploadActivity.this, "������", Toast.LENGTH_LONG).show();

			adapter.clean();
			loaduploadnewasynctask = new LoadUploadnewAsyncTask();
	        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	         loaduploadnewasynctask.execute(time,true);
		     adapter.notifyDataSetChanged();
			break;
			default:break;
		}
		
	}
}
