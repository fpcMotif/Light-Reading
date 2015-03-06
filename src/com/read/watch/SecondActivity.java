package com.read.watch;

/**
 * http://localhost:8080/suiyipic/uploadnew?TITLE=JINTIN&USERNAME=123&ILLEGAL_TYPE=123&CONTENT=123&PHOTO1=1&PHOTO2=2&PHOTO3=3&VIDEO=4&AREAID=123&REPORTTIME=2012121212020202
 �û��ı������ϴ�
 */


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class SecondActivity extends Activity implements OnClickListener {
	
	private TextuploadAsyncTask textuploadtask;
	private ProgressBar muploadProgress;
	private ProgressBar mPgBar;
	private TextView mTvProgress;
	private MyTask mTask;
	private String actionUrl = "http://192.168.100.100:8080/upload_file_service/upload.jsp";
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private SharedPreferences sharedpreferencesuser;
	private LoginAsyncTask logintask;
	private ImageView imageView;
	private TextView uploadImageResult;
	private ProgressBar progressBar;
    private View view; //�����¼�İ�ťλ�������
	private String picPath = null;
	private String filename;   //����Ƭ�ĵ�ַ������
	private ProgressDialog progressDialog;
    private String PHOTO1="";
    private String PHOTO2="";
    private String PHOTO3="";
    private String VIDEO="";
	private Button pic0;
	private Button pic1;
	private Button pic2;
	private Button pic3;    //�ϴ�������ͼƬ
	private Button resort;
	private Button push;
	private Button temppic;
	private Dialog dialog;
	private AlertDialog.Builder builder ;
	ImageView show;
	EditText textcontent;
	EditText texttitle;
	LinearLayout background;
	private int permission=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2);
		sharedpreferences=getSharedPreferences("picchoose",MODE_PRIVATE);
		sharedpreferencesuser=getSharedPreferences("user",MODE_PRIVATE);
		editor=sharedpreferences.edit();
		int number=sharedpreferences.getInt("number", 0);
		File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/pic");
		if(!file.exists())
			file.mkdirs();
		background=(LinearLayout) findViewById(R.id.background);
		resort=(Button)findViewById(R.id.resort);
		resort.setOnClickListener(this);
		pic0=(Button)findViewById(R.id.pic0);
		pic0.setOnClickListener(this);
		pic1=(Button)findViewById(R.id.pic1);
		pic1.setOnClickListener(this);
		pic2=(Button)findViewById(R.id.pic2);
		pic2.setOnClickListener(this);
		pic3=(Button)findViewById(R.id.pic3);
		pic3.setOnClickListener(this);
		push=(Button)findViewById(R.id.push);
		push.setOnClickListener(this);
		muploadProgress=(ProgressBar)findViewById(R.id.progressBartab2);
	//	video=(Button)findViewById(R.id.video);
	//	video.setOnClickListener(this);
	//	other=(Button)findViewById(R.id.other);
	//	other.setOnClickListener(this);
		//show=(ImageView)findViewById(R.id.pic1);
		
		
		
		//background.setBackgroundResource(R.drawable.bottle_toast_bg);
		textcontent=(EditText)findViewById(R.id.textcontent);
		texttitle=(EditText)findViewById(R.id.texttitle);
		String first =sharedpreferences.getString("pic0", null);
		Toast.makeText(this, Integer.toString(number), Toast.LENGTH_LONG).show();
		switch(number)
		{
		case 0: break;
		case 1: 
			first =sharedpreferences.getString("pic0", null);
		
		if(first!=null)
			{
			if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
				pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
			}
			else pic0.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
	     pic0.setVisibility(View.VISIBLE);
	     pic1.setVisibility(View.VISIBLE);
	     }
		break;
		case 2:
			first =sharedpreferences.getString("pic0", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
			{
					Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic0.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
		    pic0.setVisibility(View.VISIBLE);
		    }
		    first =sharedpreferences.getString("pic1", null);
		    if(first!=null)
			{
		    	if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic1.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else  pic1.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic1.setVisibility(View.VISIBLE);}
		    pic2.setVisibility(View.VISIBLE);
			break;
		case 3:
			first =sharedpreferences.getString("pic0", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
				{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
					pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
				}
				else
					pic0.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
		    pic0.setVisibility(View.VISIBLE);}
		    first =sharedpreferences.getString("pic1", null);
		    if(first!=null)
			{
		    	if(MediaFile.isVideoFileType(first))
				{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
					pic1.setBackgroundDrawable(new BitmapDrawable(videoThumb));
				}
				else
					pic1.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic1.setVisibility(View.VISIBLE);}
			first =sharedpreferences.getString("pic2", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic2.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic2.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic2.setVisibility(View.VISIBLE);}
			
			pic3.setVisibility(View.VISIBLE);
			break;
		case 4:
			first =sharedpreferences.getString("pic0", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
			{
					Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic0.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
		    pic0.setVisibility(View.VISIBLE);}
		    first =sharedpreferences.getString("pic1", null);
		    if(first!=null)
			{
		    	if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic1.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic1.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic1.setVisibility(View.VISIBLE);}
			first =sharedpreferences.getString("pic2", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic2.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic2.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic2.setVisibility(View.VISIBLE);}
			first =sharedpreferences.getString("pic3", null);
			if(first!=null)
			{
				if(MediaFile.isVideoFileType(first))
			{Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(first, Thumbnails.MINI_KIND);
			pic3.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		}
		else
			pic3.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(first,50,50)));
			pic3.setVisibility(View.VISIBLE);}
		break;
		default:
			break;
		}
		Toast.makeText(this, "˳��ִ��", Toast.LENGTH_LONG).show();
		String currentuser=sharedpreferencesuser.getString("username", null);
		String currentpass=sharedpreferencesuser.getString("password", null);
		logintask = new LoginAsyncTask();    
		logintask.execute(currentuser,currentpass);
	}
	
	@Override
	public void onClick(View v) {
		view=v;
		Bundle bundle=new Bundle();
		Intent intent=new Intent(SecondActivity.this,SelectPicPopupWindow.class);
		String videopath=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		filename=videopath + ".jpg";
		bundle.putString("filename",filename);
		bundle.putString("videopath",videopath);
		switch (v.getId())
		{
		case R.id.resort: 
			textcontent.setText(""); 
			texttitle.setText(""); 
			Toast.makeText(this, "�Ѿ����", Toast.LENGTH_LONG).show();
			pic1.setVisibility(View.GONE);
			pic2.setVisibility(View.GONE);
			pic3.setVisibility(View.GONE);
			pic0.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic1.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic2.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic3.setBackgroundResource(R.anim.rootblock_icon_add_bg);
		editor.clear();
		editor.commit();
			break;
		case R.id.pic0:
			bundle.putInt("value", 1);
			editor.putInt("view", 1);
			editor.commit();
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		//Toast.makeText(this, "������Ƭ", Toast.LENGTH_LONG).show();
		break;
		case R.id.pic1:
			bundle.putInt("value", 2);
			editor.putInt("view", 2);
			editor.commit();
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		//Toast.makeText(this, "������Ƭ", Toast.LENGTH_LONG).show();
		break;
		case R.id.pic2:
			bundle.putInt("value", 3);
			intent.putExtras(bundle);
			editor.putInt("view", 3);
			editor.commit();
			startActivityForResult(intent,1);
		//Toast.makeText(this, "������Ƭ", Toast.LENGTH_LONG).show();
		break;
		case R.id.pic3:
			bundle.putInt("value", 4);
			editor.putInt("view", 4);
			editor.commit();
			intent.putExtras(bundle);
			startActivityForResult(intent,1);
			break;
		//Toast.makeText(this, "������Ƭ", Toast.LENGTH_LONG).show();
		case R.id.push:
		//	int number=sharedpreferences.getInt("number", 0);
			
			if(texttitle.getText().toString().equals("")||textcontent.getText().toString().equals(""))
			{
				Toast.makeText(this, "����д����", Toast.LENGTH_LONG).show();
			break;
			}
			String currentuser=sharedpreferencesuser.getString("username", null);
			String currentpass=sharedpreferencesuser.getString("password", null);
			logintask = new LoginAsyncTask();    
			logintask.execute(currentuser,currentpass);
			
			int number=sharedpreferences.getInt("number", 0);
		if(currentuser!=null&&permission!=0){		
			
			
			textuploadtask=new TextuploadAsyncTask();
			
			PHOTO1=sharedpreferences.getString("pic0", "");
			PHOTO2=sharedpreferences.getString("pic1", "");
			PHOTO3=sharedpreferences.getString("pic2", "");
			VIDEO=sharedpreferences.getString("pic3", "");
			String ILLEGAL_TYPE="";
			String AREAID=sharedpreferencesuser.getString("place", "");
			String REPORTTIME= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());;
			textuploadtask.execute(texttitle.getText().toString(),currentuser,ILLEGAL_TYPE,textcontent.getText().toString(),PHOTO1,PHOTO2,PHOTO3,VIDEO,AREAID,REPORTTIME);
			
			
			
			if(number>0)
			{	
			String url = "http://192.168.100.100:8080/suiyipic/UploadServlet";
			//�����view���ϴ����ȵĵ���
			
			View upView = getLayoutInflater().inflate(R.layout.filebrowser_uploading, null);
			mPgBar = (ProgressBar)upView.findViewById(R.id.pb_filebrowser_uploading);
			mTvProgress = (TextView)upView.findViewById(R.id.tv_filebrowser_uploading);
			temppic=(Button)upView.findViewById(R.id.temppic);
	
	
			builder = new AlertDialog.Builder(SecondActivity.this);
			builder.setTitle("�ϴ�����").setView(upView).setNegativeButton("�ر�",new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
            	  			dialog.dismiss();
              					}
							}
						).create();
			dialog = builder.show();
			mTask = new MyTask();
			//AsyncTask��ʵ��
		
			//���ﻻ����Ҫ�ϴ����ļ�·��
				
					mTask.execute("1", url);
					
			}
					
					
					
					
		}
		else
		{
			Toast.makeText(this, "����ע��", Toast.LENGTH_LONG).show();
		}
			
		break;
		case R.id.temppic:
			//dialog.dismiss();
	       Toast.makeText(this, "����û", Toast.LENGTH_LONG).show();
			break;
		default:Toast.makeText(this, "û�д���", Toast.LENGTH_LONG).show();break;
		}
		
		}
		// TODO Auto-generated method stub
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, Integer.toString(resultCode)+"ʶ��", Toast.LENGTH_LONG).show();
		switch (resultCode) {
		case 1:
		//	Bundle bundle = data.getExtras();
		//	Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
		//	FileOutputStream b = null;
			File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/pic");
			if(!file.exists()){
				file.mkdir();
			}
			/*String numberSign= uService.queryPhoto(ID, day);
			Integer number=0;
			if(numberSign!=null)
			{
				String temp[]=numberSign.split(".jpg");
				System.out.println("numberSign"+numberSign);
				System.out.println("temp[0]"+temp[0]);
				numberSign=temp[0].substring(temp[0].length()-1);
				
				number=Integer.parseInt(numberSign)+1;
				System.out.println("number"+number);
			}
			System.out.println("numbers"+number.toString());
			*/
			
			//String fileName = "/sdcard/myImage/"+ID+day+number.toString()+".jpg";
			String fileName = Environment.getExternalStorageDirectory()+"/nanshihui/pic/"+filename;
			//uService.addNote(ID, day, this.photoSign, fileName);
			//int c=Integer.parseInt(a)+1;
			//a=((Integer)c).toString();
			
			/*
			 BitmapFactory.Options options = new BitmapFactory.Options();  
	           options.inJustDecodeBounds = true;  
	           // ��ȡ���ͼƬ�Ŀ�͸�  
	           Bitmap bitmap = BitmapFactory.decodeFile(fileName, options); //��ʱ����bmΪ��  
	               //�������ű�  
	           int be = (int)(options.outHeight / (float)50);  
	           int ys = options.outHeight % 50;//������  
	           float fe = ys / (float)50;  
	           if(fe>=0.5)be = be + 1;  
	           if (be <= 0)  
	               be = 1;  
	           options.inSampleSize = be;  
	        
	           //���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false  
	           options.inJustDecodeBounds = false;  
	           bitmap=BitmapFactory.decodeFile(fileName,options);  
		*/
			
			setBitmap();
			
			
		 /*    Toast.makeText(this, "����ɹ�", Toast.LENGTH_SHORT).show();
	           // name1=fileName;
		   //  String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";//��Ƭ����
			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// ������д���ļ�
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//intent = new Intent(NoteChoice.this,Note.class);
			//Bundle data1 = new Bundle();
			//data1.putString("day", day);
			//data1.putString("id", ID);
			//intent.putExtras(data1);
			*/
			
			
		/*	
			try {
				Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),fileName, null, null));
			
				Bitmap image;
				try {
					//��������Ǹ���Uri��ȡBitmapͼƬ�ľ�̬����
					image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
					if (image != null) {
						Toast.makeText(this, Integer.toString(123), Toast.LENGTH_LONG).show();
						setBitmap(createThumbnail(image,""));
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			//setBitmap(createThumbnail(bitmap,""));
	       */
			
			break;
		case 2:
			
			
			
			if (data != null) {
				//ȡ�÷��ص�Uri,������ѡ����Ƭ��ʱ�򷵻ص�����Uri��ʽ���������������еû�����Uri�ǿյģ�����Ҫ�ر�ע��
				/*Uri mImageCaptureUri = data.getData();
				//���ص�Uri��Ϊ��ʱ����ôͼƬ��Ϣ���ݶ�����Uri�л�á����Ϊ�գ���ô���Ǿͽ�������ķ�ʽ��ȡ
				if (mImageCaptureUri != null) {
					Bitmap image;
					try {
						//��������Ǹ���Uri��ȡBitmapͼƬ�ľ�̬����
						image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
						if (image != null) {
							Toast.makeText(this, Integer.toString(123), Toast.LENGTH_LONG).show();
							show.setImageBitmap(image);
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} 
			
*/
				 int u=sharedpreferences.getInt("view", 0);
					String y=sharedpreferences.getString("pic"+Integer.toString(u-1),null);
				if(MediaFile.isVideoFileType(y))
				{
					  if(y!=null)
				      { Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(y, Thumbnails.MINI_KIND);
				      switch(u)
						{
						case 1 :
						    
							pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
						//	pic1.setBackgroundResource(R.anim.rootblock_icon_add_bg);
							pic1.setVisibility(View.VISIBLE);
							
							break;
						case 2 :
							
							pic1.setBackgroundDrawable(new BitmapDrawable(videoThumb));
						//	pic2.setBackgroundResource(R.anim.rootblock_icon_add_bg);
							pic2.setVisibility(View.VISIBLE);
							break;
						case 3 :
							
							pic2.setBackgroundDrawable(new BitmapDrawable(videoThumb));
						//	pic3.setBackgroundResource(R.anim.rootblock_icon_add_bg);
							pic3.setVisibility(View.VISIBLE);
							break;
						case 4 :
							
							pic3.setBackgroundDrawable(new BitmapDrawable(videoThumb));
							break;
						default: break;
						}
				      }
			   }
				else
			 
				 setBitmap();
			    /*  Cursor cursor = getContentResolver().query(uri, null, null,null, null);  
			      cursor.moveToFirst();  
			      // String imgNo = cursor.getString(0); // ͼƬ���  
			       imgPath = cursor.getString(1); // ͼƬ�ļ�·��  
			       String imgSize = cursor.getString(2); // ͼƬ��С  
			       String imgName = cursor.getString(3); // ͼƬ�ļ���  
			       fileName = imgName;  
			       Toast.makeText(this,imgPath, Toast.LENGTH_LONG).show();
			   String   fileSize = imgSize;  
			      // Log.e(��uri��, uri.toString());  
			      ContentResolver cr = this.getContentResolver();  
			      try {  
			        Bitmap bitmap1 = BitmapFactory.decodeStream(cr.openInputStream(uri));  
			    	//setBitmap(bitmap1);
			        setBitmap(createThumbnail(bitmap1,""));
			       
					
			      } catch (FileNotFoundException e) {  
			       // Log.e(��Exception��, e.getMessage(),e);  
			      }  
							
			}
			else {
				Bundle extras = data.getExtras();
				if (extras != null) {
					//��������Щ���պ��ͼƬ��ֱ�Ӵ�ŵ�Bundle�е��������ǿ��Դ��������ȡBitmapͼƬ
					Bitmap image = extras.getParcelable("data");
					if (image != null) {
						 setBitmap(createThumbnail(image,""));
					}
				}
			}
*/
			}
			break;
		case 3:
			 Uri uriVideo = data.getData();
			 int u=sharedpreferences.getInt("view", 0);
			String y=sharedpreferences.getString("pic"+Integer.toString(u-1),null);
	      if(y!=null)
	      { Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(y, Thumbnails.MINI_KIND);
	      switch(u)
			{
			case 1 :
			    
				pic0.setBackgroundDrawable(new BitmapDrawable(videoThumb));
			//	pic1.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic1.setVisibility(View.VISIBLE);
				
				break;
			case 2 :
				
				pic1.setBackgroundDrawable(new BitmapDrawable(videoThumb));
			//	pic2.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic2.setVisibility(View.VISIBLE);
				break;
			case 3 :
				
				pic2.setBackgroundDrawable(new BitmapDrawable(videoThumb));
			//	pic3.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic3.setVisibility(View.VISIBLE);
				break;
			case 4 :
				
				pic3.setBackgroundDrawable(new BitmapDrawable(videoThumb));
				break;
			default: break;
			}
	      }
			break;
		default:
			break;

		}
	}
	private void setBitmap()                  //��bitmap�����Ӧ�İ�ť������
	{
	
		String first=new String();
		int u=sharedpreferences.getInt("view", 0);
		switch(u)
		{
		case 1 :
		     first=sharedpreferences.getString("pic0", null);
			pic0.setBackgroundDrawable(new BitmapDrawable(	convertToBitmap(first,50,50)));
		//	pic1.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic1.setVisibility(View.VISIBLE);
			
			break;
		case 2 :
			first=sharedpreferences.getString("pic1", null);
			pic1.setBackgroundDrawable(new BitmapDrawable(	convertToBitmap(first,50,50)));
		//	pic2.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic2.setVisibility(View.VISIBLE);
			break;
		case 3 :
			first=sharedpreferences.getString("pic2", null);
			pic2.setBackgroundDrawable(new BitmapDrawable(	convertToBitmap(first,50,50)));
		//	pic3.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			pic3.setVisibility(View.VISIBLE);
			break;
		case 4 :
			first=sharedpreferences.getString("pic3", null);
			pic3.setBackgroundDrawable(new BitmapDrawable(	convertToBitmap(first,50,50)));
			break;
		default: break;
		}
		
	}
	/** ��������ͼ,��������ͼ�ļ�·�� */
    public Bitmap createThumbnail(Bitmap source, String fileName){
        int oldW = source.getWidth();
        int oldH = source.getHeight();
        
        int w = Math.round((float)oldW/50);  //MAX_SIZEΪ����ͼ���ߴ�
        int h = Math.round((float)oldH/50);
        
        int newW = 0;
        int newH = 0;
        
        if(w <= 1 && h <= 1){
          //  return saveBitmap(source, fileName);
        	return source;
        }
        
        int i = w > h ? w : h;  //��ȡ���ű���
        
        newW = oldW/i;
        newH = oldH/i;
        
        Bitmap imgThumb = ThumbnailUtils.extractThumbnail(source, newW, newH);  //�ؼ����룡��
        
       // return saveBitmap(imgThumb, fileName);
        return imgThumb;//ע��saveBitmap����Ϊ����ͼƬ������·����private����
    }
    
    /** ������Ƶ����ͼ����������ͼ�ļ�·�� */
    public String createVideoThumbnail(String filePath, String fileName){
        Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(filePath, Thumbnails.MINI_KIND);  //�ؼ����룡��
        return saveBitmap(videoThumb, fileName);  //ע��saveBitmap����Ϊ����ͼƬ������·����private����

    }
    /** ��Uriת�����ļ�·�� */
    private String uri2filePath(Uri uri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }
    private String saveBitmap(Bitmap source, String fileName)
    {
    	Bitmap bitmap = (Bitmap) source;// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
		FileOutputStream b = null;

		File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/cache");
	if(!file.exists())
		file.mkdirs();
	
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);// ������д���ļ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
    }
	
	
	
	@Override 
    public void onConfigurationChanged(Configuration config) { 
    super.onConfigurationChanged(config); 
    } 
	
	
	
	public Bitmap convertToBitmap(String path, int w, int h) {
		        BitmapFactory.Options opts = new BitmapFactory.Options();
		           // ����Ϊtureֻ��ȡͼƬ��С
		        opts.inJustDecodeBounds = true;
		         opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
	           // ����Ϊ��
		         BitmapFactory.decodeFile(path, opts);
	           int width = opts.outWidth;
		            int height = opts.outHeight;
		            float scaleWidth = 0.f, scaleHeight = 0.f;
		           if (width > w || height > h) {
		               // ����
	               scaleWidth = ((float) width) / w;
	               scaleHeight = ((float) height) / h;
	           }
	          opts.inJustDecodeBounds = false;
	           float scale = Math.max(scaleWidth, scaleHeight);
		          opts.inSampleSize = (int)scale;
		            WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		           return Bitmap.createScaledBitmap(weak.get(), w, h, true);
		       }
	
	private class MyTask extends AsyncTask<String, Integer, String>{
     private String path=new String();
     private int  time=0;
		@Override
		protected void onPostExecute(String result) {
			mTvProgress.setText(result);
			
			if(!result.equals("�ϴ�ʧ��"))
			{	
				Toast.makeText(SecondActivity.this, "�ļ��ݽ��ɹ�", Toast.LENGTH_LONG).show();
				textcontent.setText(""); 
				texttitle.setText(""); 
				pic1.setVisibility(View.GONE);
				pic2.setVisibility(View.GONE);
				pic3.setVisibility(View.GONE);
				pic0.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic1.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic2.setBackgroundResource(R.anim.rootblock_icon_add_bg);
				pic3.setBackgroundResource(R.anim.rootblock_icon_add_bg);
			editor.clear();
			editor.commit();
			   dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			
			mTvProgress.setText("loading...");
		
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if(time==0){if(MediaFile.isVideoFileType(path))
			{
				Bitmap videoThumb = ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);
			    temppic.setBackgroundDrawable(new BitmapDrawable(videoThumb));
		   }
		   else
			temppic.setBackgroundDrawable(new BitmapDrawable(convertToBitmap(path,50,50)));
			
			time=1;}
			mPgBar.setProgress(values[0]);
			mTvProgress.setText("loading..." + values[0] + "%");
			
		}

		@Override
		protected String doInBackground(String... params) {
			int number=sharedpreferences.getInt("number", 0);
			for(int u=0;u<number;u++)
			 {
				
				String temp=sharedpreferences.getString("pic"+Integer.toString(u), null);
				if(temp!=null)
				   {
					
			String filePath = temp;
			File cache=new File(filePath);
			if(!MediaFile.isVideoFileType(filePath))
			{	
	     File file = new File(Environment.getExternalStorageDirectory()+"/nanshihui/cache");
	     if(!file.exists())
			{
				file.mkdir();
			}
	     cache=new File(file,cache.getName());
			FileOutputStream b = null;
			
			Bitmap bit= scalePicture(filePath,1024,768);
			try {
				b = new FileOutputStream(cache.toString());
				bit.compress(Bitmap.CompressFormat.JPEG, 90, b);// ������д���ļ�
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			filePath=cache.toString();
			
			}
			
			
			path=temp;
			
			String uploadUrl = params[1];
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "******";
			try {
				URL url = new URL(uploadUrl);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();
				httpURLConnection.setDoInput(true);
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setUseCaches(false);
				httpURLConnection.setRequestMethod("POST");
				httpURLConnection.setConnectTimeout(6*1000);
				httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
				httpURLConnection.setRequestProperty("Charset", "UTF-8");
				httpURLConnection.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				DataOutputStream dos = new DataOutputStream(httpURLConnection
						.getOutputStream());
				dos.writeBytes(twoHyphens + boundary + end);
				dos
						.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
								+ filePath.substring(filePath.lastIndexOf("/") + 1)
								+ "\"" + end);
				dos.writeBytes(end);

				FileInputStream fis = new FileInputStream(filePath);
				long total = fis.available();
				String totalstr = String.valueOf(total);
				Log.d("�ļ���С", totalstr);
				byte[] buffer = new byte[8192]; // 8k
				int count = 0;
				int length = 0;
				while ((count = fis.read(buffer)) != -1) {
					dos.write(buffer, 0, count);
					length += count;
					publishProgress((int) ((length / (float) total) * 100));
					//Ϊ����ʾ����,����500����
					//Thread.sleep(1000);
				}			
				fis.close();
				dos.writeBytes(end);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
				dos.flush();

				InputStream is = httpURLConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(isr);
				@SuppressWarnings("unused")
				String result = br.readLine();
				dos.close();
				is.close();
				//return "�ϴ��ɹ�";
		}catch (Exception e) {
			e.printStackTrace();
			return "�ϴ�ʧ��";
		}	
	}
				
					
			 }
		
			return "�ϴ�����";
	
		}
		
	}
	
	public static Bitmap scalePicture(String filename, int maxWidth,int maxHeight) {
	     Bitmap bitmap = null;
	    try {
	BitmapFactory.Options opts = new BitmapFactory.Options();
	                        opts.inPurgeable = true;
	                        opts.inInputShareable = true;
	                        //��ֹ�ڴ����
	                        BitmapFactory.decodeFile(filename, opts);
	                        int srcWidth = opts.outWidth;
	                        int srcHeight = opts.outHeight;
	                        int desWidth = 0;
	                        int desHeight = 0;
	                        // ���ű���
	                        double ratio = 0.0;
	                        if (srcWidth > srcHeight) {
	                                ratio = srcWidth / maxWidth;
	                                desWidth = maxWidth;
	                                desHeight = (int) (srcHeight / ratio);
	                        } else {
	                                ratio = srcHeight / maxHeight;
	                                desHeight = maxHeight;
	                                desWidth = (int) (srcWidth / ratio);
	                        }
	                        // ���������ȡ��߶�
	                        BitmapFactory.Options newOpts = new BitmapFactory.Options();
	                        newOpts.inSampleSize = (int) (ratio) + 1;
	                        newOpts.inJustDecodeBounds = false;
	                        newOpts.outWidth = desWidth;
	                        newOpts.outHeight = desHeight;
	                        bitmap = BitmapFactory.decodeFile(filename, newOpts);

	                } catch (Exception e) {
	                        // TODO: handle exception
	                }
	    return bitmap;
	               
	        }
	public class LoginAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			
			muploadProgress.setVisibility(View.VISIBLE); 
		
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			return getlogin((String)params[0],(String)params[1]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case 1:
				Toast.makeText(SecondActivity.this, "����У��ɹ�", Toast.LENGTH_LONG).show();
			permission=1;
				break;
			case 2:
				Toast.makeText(SecondActivity.this, "��������޸ģ������µ�½���ϴ�", Toast.LENGTH_LONG).show();break;
			case 3:
				Toast.makeText(SecondActivity.this, "�����޷�����", Toast.LENGTH_LONG).show();break;
			}
			
			
			//���ؽ�����
			muploadProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	
	
	private int getlogin(String username,String password)
	{
	
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/login";
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
	public class TextuploadAsyncTask extends AsyncTask<Object, Integer, Integer>
	{
		
		@Override
		protected void onPreExecute()
		{
			
			//��ʾ������
			muploadProgress.setVisibility(View.VISIBLE); 
		
		}

		@Override
		protected Integer doInBackground(Object... params)
		{
			return Textupload((String)params[0],(String)params[1],(String)params[2],(String)params[3],(String)params[4],(String)params[5],(String)params[6],(String)params[7],(String)params[8],(String)params[9]);
		}

		@Override
		protected void onPostExecute(Integer result)
		{
			//���ݷ���ֵ��ʾ��ص�Toast
			switch (result)
			{
			case 1:
				Toast.makeText(SecondActivity.this, "�ı��ϴ��ɹ�", Toast.LENGTH_LONG).show();
				textcontent.setText(""); 
				texttitle.setText(""); 
				break;
			case 2:
				Toast.makeText(SecondActivity.this, "�ϴ�ʧ�ܻ������Ѿ��ϴ���ͬ��������", Toast.LENGTH_LONG).show();break;
			case 3:
				Toast.makeText(SecondActivity.this, "�����޷�����", Toast.LENGTH_LONG).show();break;
			}
			
			
			//���ؽ�����
			muploadProgress.setVisibility(View.INVISIBLE); 
		
		
		}
	}
	
	
	private int Textupload(String TITLE,String USERNAME,String ILLEGAL_TYPE,String CONTENT,String PHOTO1,String PHOTO2,String PHOTO3,String VIDEO,String AREAID,String REPORTTIME)
	{
	
		//����URL���ַ���
		String url = "http://192.168.100.100:8080/suiyipic/uploadnew";
		String params = "TITLE="+TITLE+"&USERNAME="+USERNAME+"&ILLEGAL_TYPE="+ILLEGAL_TYPE+"&CONTENT="+CONTENT+"&PHOTO1="+PHOTO1+"&PHOTO2="+PHOTO2+"&PHOTO3="+PHOTO3+"&VIDEO="+VIDEO+"&AREAID="+AREAID+"&REPORTTIME="+REPORTTIME+"&SHA2=nosha2";
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
	

