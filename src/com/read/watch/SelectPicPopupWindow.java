package com.read.watch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SelectPicPopupWindow extends Activity implements OnClickListener {
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private Button btn_take_photo, btn_pick_photo, btn_cancel,video;
	private LinearLayout layout;
	private Intent intent;
	private Bundle extras;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		intent = getIntent();
		sharedpreferences=getSharedPreferences("picchoose",MODE_PRIVATE);
		editor=sharedpreferences.edit();
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		video = (Button) this.findViewById(R.id.video);
		layout = (LinearLayout) findViewById(R.id.pop_layout);
		 extras = getIntent().getExtras();
		
		// ���ѡ�񴰿ڷ�Χ�����������Ȼ�ȡ���㣬������ִ��onTouchEvent()��������������ط�ʱִ��onTouchEvent()��������Activity
		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "��ʾ����������ⲿ�رմ��ڣ�",
						Toast.LENGTH_SHORT).show();
			}
		});
		// ��Ӱ�ť����
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
		video.setOnClickListener(this);
	}

	// ʵ��onTouchEvent���������������Ļʱ���ٱ�Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		//ѡ������������պ�������ﴦ��Ȼ�����Ǽ���ʹ��setResult����Intent�Ա���Դ������ݺ͵���
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			if(requestCode==3)
			{
				Toast.makeText(this, "���������", Toast.LENGTH_LONG).show();
			}
			int j=sharedpreferences.getInt("view", 0);
			int i=sharedpreferences.getInt("number", 0);
			String path=sharedpreferences.getString("pic"+Integer.toString(j-1), null);
			
		if(path==null)
		{
			
			//editor.remove("pic"+Integer.toString(j-1));
			editor.remove("number");
			editor.putInt("number", i+1);
			if(requestCode==1)
			{
			  editor.putString("pic"+Integer.toString(j-1), Environment.getExternalStorageDirectory()+"/nanshihui/pic/"+extras.getString("filename"));	
			  
			}
			else if(requestCode==2)
			{
				  editor.putString("pic"+Integer.toString(j-1), uri2filePath(data.getData()));	

			}
			else if(requestCode==3)
			{
				  editor.putString("pic"+Integer.toString(j-1), Environment.getExternalStorageDirectory()+"/nanshihui/pic/"+extras.getString("videopath")+".mp4");	
                   
			}
		}
		else
		{
			if(requestCode==1)
			{
				  editor.remove("pic"+Integer.toString(j-1));	
			      editor.putString("pic"+Integer.toString(j-1), Environment.getExternalStorageDirectory()+"/nanshihui/pic/"+extras.getString("filename"));	
			}
			else if(requestCode==2)
			{ 
				editor.remove("pic"+Integer.toString(j-1));
				  editor.putString("pic"+Integer.toString(j-1), uri2filePath(data.getData()));	

			}
			else if(requestCode==3)
			{
				editor.remove("pic"+Integer.toString(j-1));
			    editor.putString("pic"+Integer.toString(j-1), Environment.getExternalStorageDirectory()+"/nanshihui/pic/"+extras.getString("videopath")+".mp4");	

			}
		}
			
			
		
			editor.commit();
			//Toast.makeText(this, Integer.toString(number), Toast.LENGTH_LONG).show();
			
			
        if(data!=null)
        {	if (data.getExtras() != null)
				intent.putExtras(data.getExtras());
			if (data.getData()!= null)
				intent.setData(data.getData());
			Toast.makeText(this, Integer.toString(requestCode), Toast.LENGTH_LONG).show();
	
        }
  
		setResult(requestCode, intent);
		finish();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			try {
				File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/pic");
				//����������ActionΪMediaStore.ACTION_IMAGE_CAPTURE��
				//��Щ��ʹ��������Action���ҷ�������Щ�����л�����⣬��������ѡ�����
			//	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				
				Bundle extras = getIntent().getExtras();
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
				String sdStatus = Environment.getExternalStorageState();
			if(sdStatus.equals(Environment.MEDIA_MOUNTED))	
				{
				File f=new File(file, extras.getString("filename"));//localTempImgDir��localTempImageFileName���Լ���������� 
				Uri u=Uri.fromFile(f); 
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, 1);
				}	
			else
				Toast.makeText(this, "û���ڴ濨", Toast.LENGTH_LONG).show();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_pick_photo:
			try {
				//ѡ����Ƭ��ʱ��Ҳһ����������ActionΪIntent.ACTION_GET_CONTENT��
				//��Щ��ʹ��������Action���ҷ�������Щ�����л�����⣬��������ѡ�����
				Intent intent = new Intent();
				intent.setType("image/*;video/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 2);
			} catch (ActivityNotFoundException e) {

			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.video:
			try {
				File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/pic");
				
				Bundle extras = getIntent().getExtras();
				Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE); 
				String sdStatus = Environment.getExternalStorageState();
			if(sdStatus.equals(Environment.MEDIA_MOUNTED))	
				{
				File f=new File(file, extras.getString("videopath")+".mp4");//localTempImgDir��localTempImageFileName���Լ���������� 
				Uri u=Uri.fromFile(f); 
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
				startActivityForResult(intent, 3);
				}	
			else
				Toast.makeText(this, "û���ڴ濨", Toast.LENGTH_LONG).show();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        break;
		default:
			break;
		}

	}
	@Override 
    public void onConfigurationChanged(Configuration config) { 
    super.onConfigurationChanged(config); 
    }
	private String uri2filePath(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }
}
