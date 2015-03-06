package com.read.watch;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.read.watch.main.SyncImageLoader;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class DetailuploadActivity extends Activity implements OnClickListener{
	private final int NEWSCOUNT = 5; //����������Ŀ
	private final int SUCCESS = 0;//���سɹ�
	private final int NONEWS = 1;//����Ŀ��û������
	private final int NOMORENEWS = 2;//����Ŀ��û�и�������
	private final int LOADERROR = 3;//����ʧ��
	private TextView title;
    private TextView content;
    private TextView textt;
	private ImageView imageView;
	private SyncImageLoader syncImageLoader;
	private Button more;
	private SetpermissionAsyncTask setpermissionasynctask;
	private ProgressBar mLoadnewsProgress;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private String context=new String();
	private String id;
	private VideoView newvideoView;
	private MediaController mController;
	private String VIDEOURL ;
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private LinearLayout operatedetail;
	private Button agree;
	private Button cancel;
	private String infoid;
	private ImageView photo2;
	private ImageView photo3;
	private int state;
	private EditText remarkcontent;
	private String PHOTO2;
	private String PHOTO3;
	private String PHOTO1;
	private Button writenew;
	private String CONTENT;
	private String TITLE;
	private WritenewsAsyncTask writenewsasynctask;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operatedetail);
		Bundle extras = getIntent().getExtras();
		writenew=(Button)findViewById(R.id.writenew);
		writenew.setOnClickListener(this);
		sharedpreferences=getSharedPreferences("user",MODE_PRIVATE);
		operatedetail=(LinearLayout)findViewById(R.id.operate);
		editor=sharedpreferences.edit();
		int i=sharedpreferences.getInt("usertype", 1);
		if(i>1)
		{
			state=extras.getInt("state");
			switch(extras.getInt("state"))
		{
		
		case 1:operatedetail.setVisibility(View.VISIBLE);
		agree=(Button)findViewById(R.id.uploadagree);
		agree.setText("����");
		agree.setOnClickListener(this);
		cancel=(Button)findViewById(R.id.uploadcancel);
		cancel.setOnClickListener(this);
		break;
		case 2:operatedetail.setVisibility(View.VISIBLE);
		agree=(Button)findViewById(R.id.uploadagree);
		agree.setText("�϶�");
		agree.setOnClickListener(this);
		cancel=(Button)findViewById(R.id.uploadcancel);
		cancel.setOnClickListener(this);break;
		default:operatedetail.setVisibility(View.GONE);break;
		}
			
			remarkcontent=(EditText)findViewById(R.id.remark);
			
			writenew.setText("����");
		}
		else
		{
			operatedetail.setVisibility(View.GONE);
		}
		photo2 = (ImageView)findViewById(R.id.uploadphoto2);
		photo3 = (ImageView)findViewById(R.id.uploadphoto3);
		newvideoView=(VideoView)findViewById(R.id.myvideoview);
		// ʵ����MediaController
		mController=new MediaController(this);
		newvideoView.setMediaController(mController);
		// ΪMediaControllerָ�����Ƶ�VideoView
		mController.setMediaPlayer(newvideoView);
		VIDEOURL=new String("");
		Uri uri = Uri.parse(VIDEOURL);  
		newvideoView.setVideoURI(uri); 
		if(VIDEOURL.equals(""))
			newvideoView.setVisibility(View.INVISIBLE);
		mController.setPrevNextListeners(new OnClickListener() {
			
			@Override
			public void onClick(View v) {					
				Toast.makeText(DetailuploadActivity.this, "��һ��",0).show();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(DetailuploadActivity.this, "��һ��",0).show();
			}
		});
		
		mNewsData=new  ArrayList<HashMap<String, Object>>();
		imageView = (ImageView)findViewById(R.id.detail_image);
		title = (TextView)findViewById(R.id.title);
		textt = (TextView)findViewById(R.id.textt);
		content = (TextView)findViewById(R.id.text);
		mLoadnewsProgress=(ProgressBar)findViewById(R.id.progressBar1);
		syncImageLoader = new SyncImageLoader();
		
		
		//Log.d("bug",extras.getString("ItemTitle"));
		imageView.setBackgroundResource(R.drawable.p1);
		photo2.setBackgroundResource(R.drawable.p1);
		photo3.setBackgroundResource(R.drawable.p1);
		
		//imageView.setBackgroundDrawable(extras.getString("image_i"));
		TITLE=extras.getString("new_title");
		CONTENT=extras.getString("content");
		
		title.setText(extras.getString("new_title"));
		textt.setText(extras.getString("new_title"));
		if(extras.getString("content")!=null)
		{content.setText((extras.getString("content")));}
		String a =extras.getString("pic_url");
		PHOTO1=extras.getString("pic_url");
		id=extras.getString("new_id");
	//	 reload();
		syncImageLoader.loadImage(1,a,imageLoadListener);
		VIDEOURL=extras.getString("video");
		
		if(MediaFile.isVideoFileType(VIDEOURL))
		{
			 uri = Uri.parse(VIDEOURL);  
		newvideoView.setVideoURI(uri); 
		newvideoView.setVisibility(View.VISIBLE);
		}
		PHOTO2 =extras.getString("photo2");
		if(MediaFile.isImageFileType(PHOTO2))
			{
		syncImageLoader.loadImage(2,PHOTO2,imageLoadListenerphoto2);
		photo2.setVisibility(View.VISIBLE);
			}
		PHOTO3 =extras.getString("photo3");
		 if(MediaFile.isImageFileType(PHOTO3))
			 {
		syncImageLoader.loadImage(3,PHOTO3,imageLoadListenerphoto3);
		photo3.setVisibility(View.VISIBLE);
			 }
		 mLoadnewsProgress.setVisibility(View.INVISIBLE); 
	}
	
	SyncImageLoader.OnImageLoadListener imageLoadListener = new SyncImageLoader.OnImageLoadListener(){

		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			
			imageView.setBackgroundDrawable(drawable);
				
			}
		
		@Override
		public void onError(Integer t) {
			
			imageView.setBackgroundResource(R.drawable.p2);
			
		}
		
	};
	SyncImageLoader.OnImageLoadListener imageLoadListenerphoto2 = new SyncImageLoader.OnImageLoadListener(){

		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			
			photo2.setBackgroundDrawable(drawable);
				
			}
		
		@Override
		public void onError(Integer t) {
			
			photo2.setBackgroundResource(R.drawable.p2);
			
		}
		
	};
	SyncImageLoader.OnImageLoadListener imageLoadListenerphoto3 = new SyncImageLoader.OnImageLoadListener(){

		@Override
		public void onImageLoad(Integer t, Drawable drawable) {
			
			photo3.setBackgroundDrawable(drawable);
				
			}
		
		@Override
		public void onError(Integer t) {
			
			photo3.setBackgroundResource(R.drawable.p2);
			
		}
		
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String temp="";
		switch(v.getId())
		{
		
		case R.id.uploadagree:
			
			temp=remarkcontent.getText().toString();
			setpermissionasynctask=new SetpermissionAsyncTask();
			setpermissionasynctask.execute(temp,"0");
			break;
		case R.id.uploadcancel:
			
			temp=remarkcontent.getText().toString();
			setpermissionasynctask=new SetpermissionAsyncTask();
			setpermissionasynctask.execute(temp,"1");
			break;
		case R.id.writenew:
			if(writenew.getText().equals("����"))
			{writenewsasynctask=new  WritenewsAsyncTask();
			writenewsasynctask.execute("1","1");}
			else
			{
				Toast.makeText(DetailuploadActivity.this, "��û��Ȩ��Ŷ", Toast.LENGTH_LONG).show();

			}
			break;
		}
		
	}

	public class SetpermissionAsyncTask extends AsyncTask<Object, Integer, Integer>
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
			return setpermission((String)params[0],(String)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case SUCCESS:
				Toast.makeText(DetailuploadActivity.this, "�����ɹ�", Toast.LENGTH_LONG).show();
				agree.setClickable(false);
				cancel.setClickable(false);
				break;
			case LOADERROR:
				Toast.makeText(DetailuploadActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();break;
			}
			
			//���ؽ�����
			mLoadnewsProgress.setVisibility(View.GONE); 
		
		
		}
	}
	
	/**
	 * ��ȡָ�����͵������б�
	 * @param cid ����ID
	 * @param newsList ����������Ϣ�ļ���
	 * @param startnid ��ҳ
	 * @param firstTimes	�Ƿ��һ�μ���
	 */
	private int setpermission(String remark,String cancelnum)
	{
		
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/setstate";
		String params = "state="+state+"&inforid="+id+"&remark="+remark+"&cancel="+cancelnum;
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
				Boolean result = jsonObject.getBoolean("result");
				if(result)
					return SUCCESS;
				else
					return LOADERROR;
		
				
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
	
	public class WritenewsAsyncTask extends AsyncTask<Object, Integer, Integer>
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
			return writenews((String)params[0],(String)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case SUCCESS:
				Toast.makeText(DetailuploadActivity.this, "�����ɹ�", Toast.LENGTH_LONG).show();
				agree.setClickable(false);
				cancel.setClickable(false);
				break;
			case LOADERROR:
				Toast.makeText(DetailuploadActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();break;
			}
			
			//���ؽ�����
			mLoadnewsProgress.setVisibility(View.GONE); 
		
		
		}
	}
	
	/**
	 * ��ȡָ�����͵������б�
	 * @param cid ����ID
	 * @param newsList ����������Ϣ�ļ���
	 * @param startnid ��ҳ
	 * @param firstTimes	�Ƿ��һ�μ���
	 */
	private int writenews(String remark,String cancelnum)
	{
		
		//����URL���ַ���
		String reporttime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String url = "http://192.168.100.100:8080/suiyipic/writenews";
		String params = "CONTENT="+CONTENT+"&INFO_ID="+id+"&TITLE="+TITLE+"&REPORTTIME="+reporttime+"&PHOTO1="+PHOTO1+"&PHOTO2="+PHOTO2+"&PHOTO3="+PHOTO3+"&VIDEO="+VIDEOURL;
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
				JSONObject jsonObject1=jsonObject.getJSONObject("data");
				Boolean result = jsonObject1.getBoolean("bool");
				System.out.println(result);
				if(result)
					return SUCCESS;
				else
					return LOADERROR;
		
				
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
