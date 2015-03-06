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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class DetailActivity extends Activity implements OnClickListener{
	private final int NEWSCOUNT = 5; //����������Ŀ
	private final int SUCCESS = 0;//���سɹ�
	private final int NONEWS = 1;//����Ŀ��û������
	private final int NOMORENEWS = 2;//����Ŀ��û�и�������
	private final int LOADERROR = 3;//����ʧ��
	private TextView title;
    private TextView content;
    private TextView textt;
	private ImageView imageView;
	private ImageView photo2;
	private ImageView photo3;

	private SyncImageLoader syncImageLoader;
	private Button more;
	private LoadNewsAsyncTask loadNewsAsyncTask;
	private ProgressBar mLoadnewsProgress;
	private ArrayList<HashMap<String, Object>> mNewsData;
	private String context=new String();
	private String id;
	private VideoView newvideoView;
	private MediaController mController;
	private String VIDEOURL ;
	private String PHOTO2;
	private String PHOTO3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
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
				Toast.makeText(DetailActivity.this, "��һ��",0).show();
			}
		}, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(DetailActivity.this, "��һ��",0).show();
			}
		});
		
		mNewsData=new  ArrayList<HashMap<String, Object>>();
		imageView = (ImageView)findViewById(R.id.detail_image);
		photo2 = (ImageView)findViewById(R.id.photo2);
		photo3 = (ImageView)findViewById(R.id.photo3);
		
		title = (TextView)findViewById(R.id.title);
		textt = (TextView)findViewById(R.id.textt);
		content = (TextView)findViewById(R.id.text);
		mLoadnewsProgress=(ProgressBar)findViewById(R.id.progressBar1);
		syncImageLoader = new SyncImageLoader();
		Bundle extras = getIntent().getExtras();
		more=(Button)findViewById(R.id.more);
		more.setOnClickListener(this);
		//Log.d("bug",extras.getString("ItemTitle"));
		imageView.setBackgroundResource(R.drawable.p1);
		photo2.setBackgroundResource(R.drawable.p1);

		photo3.setBackgroundResource(R.drawable.p1);

	
		//imageView.setBackgroundDrawable(extras.getString("image_i"));
		title.setText(extras.getString("new_title"));
		textt.setText(extras.getString("new_title"));
		if(extras.getString("content")!=null)
		{content.setText((extras.getString("content")));}
		String a =extras.getString("pic_url");
		id=extras.getString("new_id");
		 reload();
		 if(!MediaFile.isImageFileType(a))
			 imageView.setVisibility(View.INVISIBLE);
		syncImageLoader.loadImage(1,a,imageLoadListener);
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
		switch(v.getId())
		{
		case R.id.more:
			Toast.makeText(this, "��Ҫ����Ŷ", Toast.LENGTH_LONG).show();break;
		}
		
	}
	private void reload(){
		new Thread(new Runnable(){
			@Override
			public void run() {
		       loadNewsAsyncTask = new LoadNewsAsyncTask();;
		       
				loadNewsAsyncTask.execute(id,true);
				//Toast.makeText(DetailActivity.this,context, Toast.LENGTH_LONG).show();

			



		//	loadDate();
				
				//sendMessageDely(LOAD_IMAGE, 500);
			}
		}).start();
	}
	public class LoadNewsAsyncTask extends AsyncTask<Object, Integer, Integer>
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
			return getSpeCateNews((String)params[0],(Boolean)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case SUCCESS:
				System.out.println(context);
				content.setText(context);
				if(MediaFile.isImageFileType(PHOTO2))
				{
					
					
					syncImageLoader.loadImage(2,PHOTO2,imageLoadListenerphoto2);
					photo2.setVisibility(View.VISIBLE);
				}
				if(MediaFile.isImageFileType(PHOTO3))
				{
					
					
					syncImageLoader.loadImage(3,PHOTO3,imageLoadListenerphoto3);
					photo3.setVisibility(View.VISIBLE);
				}
				break;
			case NONEWS:
				Toast.makeText(DetailActivity.this, "û����Ϣ", Toast.LENGTH_LONG).show();break;
			case NOMORENEWS:
				Toast.makeText(DetailActivity.this, "û�и�����Ϣ", Toast.LENGTH_LONG).show();break;
			case LOADERROR:
				Toast.makeText(DetailActivity.this, "��ȡ��Ϣʧ��", Toast.LENGTH_LONG).show();break;
			}
			if(!VIDEOURL.equals(""))
			{
				Uri uri = Uri.parse(VIDEOURL);  
			newvideoView.setVideoURI(uri); 
			newvideoView.setVisibility(View.VISIBLE);
			}
			else newvideoView.setVisibility(View.GONE);
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
	private int getSpeCateNews(String time,Boolean firstTimes)
	{
		
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/getNews";
		String params = "nid="+id;
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
				int totalnum = dataObject.getInt("totalnum");
				System.out.println(totalnum);

				if (totalnum>0)
				{
						JSONObject newsObject = dataObject.getJSONObject("news");
						context = newsObject.getString("CONTEXT");
						
						VIDEOURL=newsObject.getString("VIDEO");
						PHOTO2=newsObject.getString("PHOTO2");
						PHOTO3=newsObject.getString("PHOTO3");
						System.out.println("PHOTO2:"+PHOTO2);
						System.out.println("PHOTO3:"+PHOTO2);
						System.out.println(VIDEOURL);
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
}
