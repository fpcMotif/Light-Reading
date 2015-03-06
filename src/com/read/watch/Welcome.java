package com.read.watch;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class Welcome extends Activity {
    /** Called when the activity is first created. */
	 private final int SPLASH_DISPLAY_LENGHT = 5000; //�ӳ�����  
	 private ImageView image=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**ȫ�����ã����ش�������װ��*/ 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**����������View�ģ����Դ������е����β��ֱ����غ������Ȼ��Ч*/ 
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.welcome);
        image=(ImageView)findViewById(R.id.welcome_desk);
        AnimationSet  animationset=new AnimationSet(true);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(5500);
        animationset.addAnimation(alphaAnimation);
        image.startAnimation(animationset);
        new Handler().postDelayed(new Runnable(){ 
        	  
            @Override 
            public void run() { 
                Intent mainIntent = new Intent(Welcome.this,MainActivity.class); 
                Welcome.this.startActivity(mainIntent); 
                Welcome.this.finish(); 
            } 
               
           }, SPLASH_DISPLAY_LENGHT); 
    }
}
