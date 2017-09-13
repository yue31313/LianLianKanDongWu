package com.animalsmatch2;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity  extends Activity {
	private static String Setting_Info = "setting_infos";
	protected static final String Level_Guide="level_guide";
	protected static final String App_Rate = "app_rate";
	
	private Button playbtn;
	private Button recordsbtn;
	private Button morebtn;
	private Button adappbtn;
	private ImageView mainiv;
	
	private MediaPlayer mMediaPlayer;
	private static String[] menufonts={"fonts/helvetical_thin.otf","fonts/hobostd.otf","fonts/helvetical_regular.otf","fonts/roboto_thin.ttf","fonts/roboto_light.ttf"};
	private String[] app_packages={"com.dotsnumbers","com.bwdclock","com.crazybirdscrushsaga","com.memorypuzzlepro","com.pandacalculator"};
	private int screenHeight;
	private int screenWidth;
	private String adappname;
	private String adpackname="";
	private String adappdesc="";
	private String adappicon="";
	private String adappimage="";
	private int guide_id=0;
	private AnimationDrawable mainAnimation;
	private SharedPreferences settings_info;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		playbtn= (Button) findViewById(R.id.playbtn);
		recordsbtn= (Button) findViewById(R.id.recordsbtn);
		morebtn= (Button) findViewById(R.id.morebtn);
		adappbtn= (Button) findViewById(R.id.adappbtn);
		mainiv= (ImageView) findViewById(R.id.mainiv);
		settings_info = getSharedPreferences(Setting_Info, 0);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		screenWidth  = dm.widthPixels;
		
		Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
		playbtn.setTypeface(fonttype);
		morebtn.setTypeface(fonttype);
		recordsbtn.setTypeface(fonttype);
		
		mMediaPlayer=new MediaPlayer();
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.animal1, 1));
		soundPoolMap.put(2, soundPool.load(this, R.raw.animal2, 1));
		
		View.OnClickListener vclick =new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				playSounds(R.raw.click);
				switch(arg0.getId())
				{
					case R.id.recordsbtn:
						Intent intentapp1=new Intent(MenuActivity.this,RecordsActivity.class);
						startActivity(intentapp1);
						break;
					case R.id.playbtn:
						if(settings_info.getInt(Level_Guide, 0)==0)
						{
							ShowMenuLevelMenu();
						}
						else
						{
							Intent intentapp2=new Intent(MenuActivity.this,MainActivity.class);
							startActivity(intentapp2);
						}

						break;
					case R.id.morebtn:
							ShowResumeLevelMenu();
						break;
					case R.id.adappbtn:
						  String applink2="market://details?id=com.fourinone";
						  Uri uri2 = Uri.parse(applink2);  
						  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
						  startActivity(it2);
						break;
				}
			}
		};
		
		playbtn.setOnClickListener(vclick);
		morebtn.setOnClickListener(vclick);
		recordsbtn.setOnClickListener(vclick);
		adappbtn.setOnClickListener(vclick);
		
		if(screenWidth<600)
		{
			Toast.makeText(getApplicationContext(), "Sorry, your device can not support this app.", Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "HD/FHD phones or tablets needed.", Toast.LENGTH_LONG).show();
			finish();
		}
		mainiv.setBackgroundResource(R.anim.main_icon);
		mainAnimation = (AnimationDrawable) mainiv.getBackground();
	}
	
	private void playSounds(int sid) {

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mMediaPlayer = MediaPlayer.create(MenuActivity.this, sid);

		/* 准备播放 */
		// mMediaPlayer.prepare();
		/* 开始播放 */
		mMediaPlayer.start();
		
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
		});
	}
	
	private void ShowResumeLevelMenu() {
		 final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.resume_level_menu);
			Button btnresume = (Button) window.findViewById(R.id.btnresume);
			Button btnapp1 = (Button) window.findViewById(R.id.btnapp1);
			Button btnapp2 = (Button) window.findViewById(R.id.btnapp2);
			Button btnapp3 = (Button) window.findViewById(R.id.btnapp3);
			Button btnapp4 = (Button) window.findViewById(R.id.btnapp4);
			Button btnapp5 = (Button) window.findViewById(R.id.btnapp5);
			Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
			btnresume.setTypeface(fonttype);
			btnresume.setText("OK");
			
			LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
			LayoutParams sq_layout_para = resumelayout.getLayoutParams();
			sq_layout_para.height = screenHeight-60;
			sq_layout_para.width = screenWidth-60;
			resumelayout.setLayoutParams(sq_layout_para);
			
			Animation animation1 = AnimationUtils.loadAnimation(
					MenuActivity.this, R.anim.apppop1);
			btnapp1.setAnimation(animation1);
			btnapp2.setAnimation(animation1);
			btnapp3.setAnimation(animation1);
			btnapp4.setAnimation(animation1);
			btnapp5.setAnimation(animation1);

			
			
			if(isRunApp(app_packages[0]))
				btnapp1.setVisibility(View.GONE);
			if(isRunApp(app_packages[1]))
				btnapp2.setVisibility(View.GONE);
			if(isRunApp(app_packages[2]))
				btnapp3.setVisibility(View.GONE);
			if(isRunApp(app_packages[3]))
				btnapp4.setVisibility(View.GONE);
			if(isRunApp(app_packages[4]))
				btnapp5.setVisibility(View.GONE);
			
			
			
			btnapp1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					  String applink2="market://details?id="+app_packages[0];
					  Uri uri2 = Uri.parse(applink2);  
					  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
					  startActivity(it2);
				}
			});
			
			btnapp2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					  String applink2="market://details?id="+app_packages[1];
					  Uri uri2 = Uri.parse(applink2);  
					  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
					  startActivity(it2);
				}
			});
			
			btnapp3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					  String applink2="market://details?id="+app_packages[2];
					  Uri uri2 = Uri.parse(applink2);  
					  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
					  startActivity(it2);
				}
			});
			
			btnapp4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					  String applink2="market://details?id="+app_packages[3];
					  Uri uri2 = Uri.parse(applink2);  
					  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
					  startActivity(it2);
				}
			});
			
			btnapp5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					  String applink2="market://details?id="+app_packages[4];
					  Uri uri2 = Uri.parse(applink2);  
					  Intent it2 = new Intent(Intent.ACTION_VIEW, uri2);  
					  startActivity(it2);
				}
			});
			
			btnresume.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					dlg.cancel();
				}
			});
	 }
	
	private boolean isRunApp(String packageName) {  
        PackageInfo pi;  
        try {  
            pi = getPackageManager().getPackageInfo(packageName, 0);  
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);  
            // resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);   
            resolveIntent.setPackage(pi.packageName);  
            PackageManager pManager = getPackageManager();  
            List<ResolveInfo> apps = pManager.queryIntentActivities(  
                    resolveIntent, 0);  
  
            ResolveInfo ri = apps.iterator().next();  
            if (ri != null) {  
                return true;
            }  
            else
            {
            	return false;
            }
            
        } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block   
            e.printStackTrace();
            return false;
        }  
    }
	
	
	
	@Override
    protected void onResume() {
        super.onResume();
        adappbtn.setVisibility(View.VISIBLE);
        playEffect(1,0);
        playEffect(2,0);
        mainAnimation.start();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	AudioManager audioManager=null;
    	audioManager=(AudioManager)getSystemService(Service.AUDIO_SERVICE);

    if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
    	//Toast.makeText(main.this, "Down", Toast.LENGTH_SHORT).show();
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                AudioManager.ADJUST_LOWER, 
                AudioManager.FLAG_SHOW_UI);
        	return true;
    }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP)
    {
    	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
                AudioManager.ADJUST_RAISE, 
                AudioManager.FLAG_SHOW_UI);    
    		return true;
    }else if(keyCode==KeyEvent.KEYCODE_BACK)
    {
    	playSounds(R.raw.click);
    	ShowRateMenu();
    	return super.onKeyDown(keyCode, event);    
    }else{
    return super.onKeyDown(keyCode, event);    
    }
    }
	
	private void ShowMenuLevelMenu() {
		final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.guide_menu);
			String[][] guidestr={
					{"Try to match all the animals in the time limit.","Every match wins 1 second."},
					{"Use a hint will consume 2 seconds.","Move to a new level will win 1 hint."},
					{"Whenever give up or fail, start from Level 1.","Try to earn scores as high as possible."}
					};
			

			Button btnyes = (Button) window.findViewById(R.id.btnyes);
			TextView levelinfotv1= (TextView) window.findViewById(R.id.levelinfotv1);
			TextView levelinfotv2= (TextView) window.findViewById(R.id.levelinfotv2);
			
			levelinfotv1.setText(guidestr[guide_id][0]);
			levelinfotv2.setText(guidestr[guide_id][1]);
			
			Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
			btnyes.setTypeface(fonttype);
			levelinfotv1.setTypeface(fonttype);
			levelinfotv2.setTypeface(fonttype);
			
			if(guide_id==2)
			{
				btnyes.setText("Start");
			}
			LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
			LayoutParams sq_layout_para = resumelayout.getLayoutParams();
			sq_layout_para.height = screenHeight-60;
			sq_layout_para.width = screenWidth-60;
			resumelayout.setLayoutParams(sq_layout_para);
			
			
			btnyes.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					playSounds(R.raw.click);
					dlg.cancel();
					guide_id++;
					
					if(guide_id==3)
					{
						settings_info.edit().putInt(Level_Guide, 1).commit();
						Intent intentapp2=new Intent(MenuActivity.this,MainActivity.class);
						startActivity(intentapp2);
					}
					else
					{
						ShowMenuLevelMenu();
					}
				}
			});
	 }
	
	 private void playEffect(int sound, int loop) {
			AudioManager mgr = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
		}
	 
	 private void ShowRateMenu() {
			
			int app_rate=settings_info.getInt(App_Rate,0);
			
			if(app_rate==1)
				return;
			
			final AlertDialog dlg = new AlertDialog.Builder(this).create();
			dlg.setCancelable(false);
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.rateapp_menu);
			Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
			
			Button button1 = (Button) window.findViewById(R.id.button1);
			Button button2 = (Button) window.findViewById(R.id.button2);
			Button button3 = (Button) window.findViewById(R.id.button3);
			TextView text1= (TextView) window.findViewById(R.id.text1);
			TextView text2= (TextView) window.findViewById(R.id.text2);
			
			button1.setTypeface(fonttype);
			button2.setTypeface(fonttype);
			button3.setTypeface(fonttype);
			text2.setTypeface(fonttype);
			
			fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
			text1.setTypeface(fonttype);
			
			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dlg.cancel();
					settings_info.edit().putInt(App_Rate, 1).commit();
					finish();
				}
			});
			
			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dlg.cancel();
					finish();
				}
			});
			
			button3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dlg.cancel();
					settings_info.edit().putInt(App_Rate, 1).commit();
					String applink2="market://details?id=com.animalsmatch2";
					Uri uri2 = Uri.parse(applink2);  
					Intent it = new Intent(Intent.ACTION_VIEW, uri2);  
					startActivity(it);
					finish();
				}
			});
			
		}
}
