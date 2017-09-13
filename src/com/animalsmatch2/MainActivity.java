package com.animalsmatch2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.animalsmatch2.R;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class MainActivity extends Activity  implements OnTouchListener, AdListener{

	private static String Setting_Info = "setting_infos";
	protected static final String Level_ID="level_id";
	protected static final String Levels_Time="level_time";
	
	protected static final String Level_Scores="level_scores";
	protected static final String Level_Best_Scores="level_best_scores";
	protected static final String Level_Hints="level_hints";
	
	
	private static final String LOG_TAG = "InterstitialSample";
	private InterstitialAd interstitialAd;
	
	
	private static final String TAG = "WeAndroids";
	private ImageView[][] dots_grids;
	private int[][] dots_grids_values; 
	private int[] dots_images={R.drawable.anguilla,R.drawable.canard,R.drawable.crabe,R.drawable.croco,R.drawable.escargot,R.drawable.goupil ,R.drawable.grouyer,R.drawable.horse,R.drawable.koala,R.drawable.medusa,R.drawable.monkey,R.drawable.morso,R.drawable.oursblanc,R.drawable.oursbrun,R.drawable.pico,R.drawable.poulpo,R.drawable.rino,R.drawable.saint_jack,R.drawable.tiger,R.drawable.toro,R.drawable.ursino,R.drawable.zebra};
	private int[] dots_animals={R.drawable.anguilla,R.drawable.canard,R.drawable.crabe,R.drawable.croco,R.drawable.escargot,R.drawable.goupil ,R.drawable.grouyer,R.drawable.horse,R.drawable.koala,R.drawable.medusa,R.drawable.monkey,R.drawable.morso,R.drawable.oursblanc,R.drawable.oursbrun,R.drawable.pico,R.drawable.poulpo,R.drawable.rino,R.drawable.saint_jack,R.drawable.tiger,R.drawable.toro,R.drawable.ursino,R.drawable.zebra};
	private int[] dots_xmas={R.drawable.xmas_1,R.drawable.xmas_2,R.drawable.xmas_3,R.drawable.xmas_4,R.drawable.xmas_5,R.drawable.xmas_6 ,R.drawable.xmas_7,R.drawable.xmas_8,R.drawable.xmas_9,R.drawable.xmas_10,R.drawable.xmas_11,R.drawable.xmas_12,R.drawable.xmas_13,R.drawable.xmas_14,R.drawable.xmas_15,R.drawable.xmas_16,R.drawable.xmas_17,R.drawable.xmas_18,R.drawable.xmas_19,R.drawable.xmas_20,R.drawable.xmas_21,R.drawable.xmas_22};
	private int[] dots_flags={R.drawable.flag_argentina,R.drawable.flag_australia,R.drawable.flag_brazil,R.drawable.flag_britain,R.drawable.flag_canada,R.drawable.flag_china ,R.drawable.flag_egypt,R.drawable.flag_france,R.drawable.flag_german,R.drawable.flag_india,R.drawable.flag_japan,R.drawable.flag_kosovo,R.drawable.flag_malaysia,R.drawable.flag_moldova,R.drawable.flag_russian,R.drawable.flag_singapore,R.drawable.flag_southkorea,R.drawable.flag_srilanka,R.drawable.flag_thailand,R.drawable.flag_turkmenistan,R.drawable.flag_usa,R.drawable.flag_wales};
	private int screenHeight;
	private int screenWidth;
	private int each_dot_width;
	private ArrayList<Integer[]> dot_numbers;
	
	private ArrayList<Integer[]> dot_numbers_match;
	
	private MediaPlayer mMediaPlayer;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	//private int[] levels_time={180,160,150,140,130,120,120,120,100,95,90};
	private int levels_time=240;
	private int[] animals_number={16,17,18,19,20,21,22,23,23,23};
	private int level_id=1;
	private int level_scores=0;
	private int level_best_scores=0;
	private int level_hints=9;
	
	private Handler scorestime_handler;
	private Runnable scorestime_runnable;
	
	private Handler hinttime_handler;
	private Runnable hinttime_runnable;
	
	
	private Handler checkmatchtime_handler;
	private Runnable checkmatchtime_runnable;
	
	private Handler countdowntime_handler;
	private Runnable countdowntime_runnable;

	private Handler dotsclear_handler;
	private Runnable dotsclear_runnable;

	private Handler connection_handler;
	private Runnable connection_runnable;
	
	private Handler Interstitial_handler;
	private Runnable Interstitial_runnable;
	
	private int countdown_seconds;
	private boolean first_time=true;
	private Integer dot1_x;
	private Integer dot1_y;
	private Integer dot2_x;
	private Integer dot2_y;
	
	private TextView leveltime_tv;
	private Button levelinfo_tv;
	private TextView levelscore_tv;
	
	private Button levelpause_btn;
	private Button levelpattern_btn;
	
	private Button levelmenu_btn;
	private Button leveltip_btn;
	
	private SharedPreferences settings_info;
	private ProgressBar progressBarHorizontal;
	private static String[] menufonts={"fonts/helvetical_thin.otf","fonts/hobostd.otf","fonts/helvetical_regular.otf","fonts/roboto_thin.ttf","fonts/roboto_light.ttf"};
	
	private String today_str;
	
	private int hint_x1;
	private int hint_y1;
	private int hint_x2;
	private int hint_y2;
	private int hinttime_count=4;
	private String[] app_packages={"com.dotsnumbers","com.bwdclock","com.crazybirdscrushsaga","com.memorypuzzlepro","com.pandacalculator"};
	private int dotsxy3=0;
	private boolean interstitialAd_ready=false; 
	private boolean exit_menu=false; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		interstitialAd = new InterstitialAd(this, "ca-app-pub-2889354728299527/6651345299");
		interstitialAd.setAdListener(this);
		
		dots_grids=new ImageView[12][16];
		dots_grids_values=new int[12][16];
		dot_numbers=new ArrayList<Integer[]>();
		dot_numbers_match=new ArrayList<Integer[]>();
		
		hint_x1=0;
		hint_y1=0;
		hint_x2=0;
		hint_y2=0;
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date d =new Date();
		today_str =format.format(d);
		
		settings_info = getSharedPreferences(Setting_Info, 0);
		level_id=settings_info.getInt(Level_ID,1);
		level_scores=settings_info.getInt(Level_Scores,0);
		level_best_scores=settings_info.getInt(Level_Best_Scores+today_str,0);
		levels_time=settings_info.getInt(Levels_Time+level_id,240);
		level_hints=settings_info.getInt(Level_Hints,9);
		
		//Toast.makeText(getApplicationContext(), "levels_time="+levels_time, Toast.LENGTH_SHORT).show();
		
		leveltime_tv= (TextView) findViewById(R.id.leveltime_tv);
		levelscore_tv= (TextView) findViewById(R.id.levelscore_tv);
		
		levelpause_btn= (Button) findViewById(R.id.levelpause_btn);
		levelmenu_btn= (Button) findViewById(R.id.levelmenu_btn);
		leveltip_btn= (Button) findViewById(R.id.leveltip_btn);
		levelpattern_btn= (Button) findViewById(R.id.levelpattern_btn);
		
		
		Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
		leveltime_tv.setTypeface(fonttype);
		levelpause_btn.setTypeface(fonttype);
		levelmenu_btn.setTypeface(fonttype);
		leveltip_btn.setTypeface(fonttype);
		levelscore_tv.setTypeface(fonttype);
		
		leveltip_btn.setText("Hints "+level_hints);
		levelscore_tv.setText(""+level_scores);
		
		dots_grids[0][00] = (ImageView) findViewById(R.id.dot_iv11);
		dots_grids[0][01] = (ImageView) findViewById(R.id.dot_iv12);
		dots_grids[0][02] = (ImageView) findViewById(R.id.dot_iv13);
		dots_grids[0][03] = (ImageView) findViewById(R.id.dot_iv14);
		dots_grids[0][04] = (ImageView) findViewById(R.id.dot_iv15);
		dots_grids[0][05] = (ImageView) findViewById(R.id.dot_iv16);
		dots_grids[0][06] = (ImageView) findViewById(R.id.dot_iv17);
		dots_grids[0][7] = (ImageView) findViewById(R.id.dot_iv18);
		dots_grids[0][8] = (ImageView) findViewById(R.id.dot_iv19);
		dots_grids[0][9] = (ImageView) findViewById(R.id.dot_iv110);
		dots_grids[0][10] = (ImageView) findViewById(R.id.dot_iv111);
		dots_grids[0][11] = (ImageView) findViewById(R.id.dot_iv112);
		dots_grids[0][12] = (ImageView) findViewById(R.id.dot_iv113);
		dots_grids[0][13] = (ImageView) findViewById(R.id.dot_iv114);
		dots_grids[0][14] = (ImageView) findViewById(R.id.dot_iv115);
		dots_grids[0][15] = (ImageView) findViewById(R.id.dot_iv116);
		
		dots_grids[1][00] = (ImageView) findViewById(R.id.dot_iv21);
		dots_grids[1][01] = (ImageView) findViewById(R.id.dot_iv22);
		dots_grids[1][02] = (ImageView) findViewById(R.id.dot_iv23);
		dots_grids[1][03] = (ImageView) findViewById(R.id.dot_iv24);
		dots_grids[1][04] = (ImageView) findViewById(R.id.dot_iv25);
		dots_grids[1][05] = (ImageView) findViewById(R.id.dot_iv26);
		dots_grids[1][06] = (ImageView) findViewById(R.id.dot_iv27);
		dots_grids[1][7] = (ImageView) findViewById(R.id.dot_iv28);
		dots_grids[1][8] = (ImageView) findViewById(R.id.dot_iv29);
		dots_grids[1][9] = (ImageView) findViewById(R.id.dot_iv210);
		dots_grids[1][10] = (ImageView) findViewById(R.id.dot_iv211);
		dots_grids[1][11] = (ImageView) findViewById(R.id.dot_iv212);
		dots_grids[1][12] = (ImageView) findViewById(R.id.dot_iv213);
		dots_grids[1][13] = (ImageView) findViewById(R.id.dot_iv214);
		dots_grids[1][14] = (ImageView) findViewById(R.id.dot_iv215);
		dots_grids[1][15] = (ImageView) findViewById(R.id.dot_iv216);

		dots_grids[2][00] = (ImageView) findViewById(R.id.dot_iv31);
		dots_grids[2][01] = (ImageView) findViewById(R.id.dot_iv32);
		dots_grids[2][02] = (ImageView) findViewById(R.id.dot_iv33);
		dots_grids[2][03] = (ImageView) findViewById(R.id.dot_iv34);
		dots_grids[2][04] = (ImageView) findViewById(R.id.dot_iv35);
		dots_grids[2][05] = (ImageView) findViewById(R.id.dot_iv36);
		dots_grids[2][06] = (ImageView) findViewById(R.id.dot_iv37);
		dots_grids[2][7] = (ImageView) findViewById(R.id.dot_iv38);
		dots_grids[2][8] = (ImageView) findViewById(R.id.dot_iv39);
		dots_grids[2][9] = (ImageView) findViewById(R.id.dot_iv310);
		dots_grids[2][10] = (ImageView) findViewById(R.id.dot_iv311);
		dots_grids[2][11] = (ImageView) findViewById(R.id.dot_iv312);
		dots_grids[2][12] = (ImageView) findViewById(R.id.dot_iv313);
		dots_grids[2][13] = (ImageView) findViewById(R.id.dot_iv314);
		dots_grids[2][14] = (ImageView) findViewById(R.id.dot_iv315);
		dots_grids[2][15] = (ImageView) findViewById(R.id.dot_iv316);

		dots_grids[3][00] = (ImageView) findViewById(R.id.dot_iv41);
		dots_grids[3][01] = (ImageView) findViewById(R.id.dot_iv42);
		dots_grids[3][02] = (ImageView) findViewById(R.id.dot_iv43);
		dots_grids[3][03] = (ImageView) findViewById(R.id.dot_iv44);
		dots_grids[3][04] = (ImageView) findViewById(R.id.dot_iv45);
		dots_grids[3][05] = (ImageView) findViewById(R.id.dot_iv46);
		dots_grids[3][06] = (ImageView) findViewById(R.id.dot_iv47);
		dots_grids[3][7] = (ImageView) findViewById(R.id.dot_iv48);
		dots_grids[3][8] = (ImageView) findViewById(R.id.dot_iv49);
		dots_grids[3][9] = (ImageView) findViewById(R.id.dot_iv410);
		dots_grids[3][10] = (ImageView) findViewById(R.id.dot_iv411);
		dots_grids[3][11] = (ImageView) findViewById(R.id.dot_iv412);
		dots_grids[3][12] = (ImageView) findViewById(R.id.dot_iv413);
		dots_grids[3][13] = (ImageView) findViewById(R.id.dot_iv414);
		dots_grids[3][14] = (ImageView) findViewById(R.id.dot_iv415);
		dots_grids[3][15] = (ImageView) findViewById(R.id.dot_iv416);

		dots_grids[4][00] = (ImageView) findViewById(R.id.dot_iv51);
		dots_grids[4][01] = (ImageView) findViewById(R.id.dot_iv52);
		dots_grids[4][02] = (ImageView) findViewById(R.id.dot_iv53);
		dots_grids[4][03] = (ImageView) findViewById(R.id.dot_iv54);
		dots_grids[4][04] = (ImageView) findViewById(R.id.dot_iv55);
		dots_grids[4][05] = (ImageView) findViewById(R.id.dot_iv56);
		dots_grids[4][06] = (ImageView) findViewById(R.id.dot_iv57);
		dots_grids[4][7] = (ImageView) findViewById(R.id.dot_iv58);
		dots_grids[4][8] = (ImageView) findViewById(R.id.dot_iv59);
		dots_grids[4][9] = (ImageView) findViewById(R.id.dot_iv510);
		dots_grids[4][10] = (ImageView) findViewById(R.id.dot_iv511);
		dots_grids[4][11] = (ImageView) findViewById(R.id.dot_iv512);
		dots_grids[4][12] = (ImageView) findViewById(R.id.dot_iv513);
		dots_grids[4][13] = (ImageView) findViewById(R.id.dot_iv514);
		dots_grids[4][14] = (ImageView) findViewById(R.id.dot_iv515);
		dots_grids[4][15] = (ImageView) findViewById(R.id.dot_iv516);


		dots_grids[5][00] = (ImageView) findViewById(R.id.dot_iv61);
		dots_grids[5][01] = (ImageView) findViewById(R.id.dot_iv62);
		dots_grids[5][02] = (ImageView) findViewById(R.id.dot_iv63);
		dots_grids[5][03] = (ImageView) findViewById(R.id.dot_iv64);
		dots_grids[5][04] = (ImageView) findViewById(R.id.dot_iv65);
		dots_grids[5][05] = (ImageView) findViewById(R.id.dot_iv66);
		dots_grids[5][06] = (ImageView) findViewById(R.id.dot_iv67);
		dots_grids[5][7] = (ImageView) findViewById(R.id.dot_iv68);
		dots_grids[5][8] = (ImageView) findViewById(R.id.dot_iv69);
		dots_grids[5][9] = (ImageView) findViewById(R.id.dot_iv610);
		dots_grids[5][10] = (ImageView) findViewById(R.id.dot_iv611);
		dots_grids[5][11] = (ImageView) findViewById(R.id.dot_iv612);
		dots_grids[5][12] = (ImageView) findViewById(R.id.dot_iv613);
		dots_grids[5][13] = (ImageView) findViewById(R.id.dot_iv614);
		dots_grids[5][14] = (ImageView) findViewById(R.id.dot_iv615);
		dots_grids[5][15] = (ImageView) findViewById(R.id.dot_iv616);
		
		dots_grids[6][00] = (ImageView) findViewById(R.id.dot_iv71);
		dots_grids[6][01] = (ImageView) findViewById(R.id.dot_iv72);
		dots_grids[6][02] = (ImageView) findViewById(R.id.dot_iv73);
		dots_grids[6][03] = (ImageView) findViewById(R.id.dot_iv74);
		dots_grids[6][04] = (ImageView) findViewById(R.id.dot_iv75);
		dots_grids[6][05] = (ImageView) findViewById(R.id.dot_iv76);
		dots_grids[6][06] = (ImageView) findViewById(R.id.dot_iv77);
		dots_grids[6][7] = (ImageView) findViewById(R.id.dot_iv78);
		dots_grids[6][8] = (ImageView) findViewById(R.id.dot_iv79);
		dots_grids[6][9] = (ImageView) findViewById(R.id.dot_iv710);
		dots_grids[6][10] = (ImageView) findViewById(R.id.dot_iv711);
		dots_grids[6][11] = (ImageView) findViewById(R.id.dot_iv712);
		dots_grids[6][12] = (ImageView) findViewById(R.id.dot_iv713);
		dots_grids[6][13] = (ImageView) findViewById(R.id.dot_iv714);
		dots_grids[6][14] = (ImageView) findViewById(R.id.dot_iv715);
		dots_grids[6][15] = (ImageView) findViewById(R.id.dot_iv716);
		
		dots_grids[7][00] = (ImageView) findViewById(R.id.dot_iv81);
		dots_grids[7][01] = (ImageView) findViewById(R.id.dot_iv82);
		dots_grids[7][02] = (ImageView) findViewById(R.id.dot_iv83);
		dots_grids[7][03] = (ImageView) findViewById(R.id.dot_iv84);
		dots_grids[7][04] = (ImageView) findViewById(R.id.dot_iv85);
		dots_grids[7][05] = (ImageView) findViewById(R.id.dot_iv86);
		dots_grids[7][06] = (ImageView) findViewById(R.id.dot_iv87);
		dots_grids[7][7] = (ImageView) findViewById(R.id.dot_iv88);
		dots_grids[7][8] = (ImageView) findViewById(R.id.dot_iv89);
		dots_grids[7][9] = (ImageView) findViewById(R.id.dot_iv810);
		dots_grids[7][10] = (ImageView) findViewById(R.id.dot_iv811);
		dots_grids[7][11] = (ImageView) findViewById(R.id.dot_iv812);
		dots_grids[7][12] = (ImageView) findViewById(R.id.dot_iv813);
		dots_grids[7][13] = (ImageView) findViewById(R.id.dot_iv814);
		dots_grids[7][14] = (ImageView) findViewById(R.id.dot_iv815);
		dots_grids[7][15] = (ImageView) findViewById(R.id.dot_iv816);
		
		dots_grids[8][00] = (ImageView) findViewById(R.id.dot_iv91);
		dots_grids[8][01] = (ImageView) findViewById(R.id.dot_iv92);
		dots_grids[8][02] = (ImageView) findViewById(R.id.dot_iv93);
		dots_grids[8][03] = (ImageView) findViewById(R.id.dot_iv94);
		dots_grids[8][04] = (ImageView) findViewById(R.id.dot_iv95);
		dots_grids[8][05] = (ImageView) findViewById(R.id.dot_iv96);
		dots_grids[8][06] = (ImageView) findViewById(R.id.dot_iv97);
		dots_grids[8][7] = (ImageView) findViewById(R.id.dot_iv98);
		dots_grids[8][8] = (ImageView) findViewById(R.id.dot_iv99);
		dots_grids[8][9] = (ImageView) findViewById(R.id.dot_iv910);
		dots_grids[8][10] = (ImageView) findViewById(R.id.dot_iv911);
		dots_grids[8][11] = (ImageView) findViewById(R.id.dot_iv912);
		dots_grids[8][12] = (ImageView) findViewById(R.id.dot_iv913);
		dots_grids[8][13] = (ImageView) findViewById(R.id.dot_iv914);
		dots_grids[8][14] = (ImageView) findViewById(R.id.dot_iv915);
		dots_grids[8][15] = (ImageView) findViewById(R.id.dot_iv916);
		
		dots_grids[9][00] = (ImageView) findViewById(R.id.dot_iv101);
		dots_grids[9][01] = (ImageView) findViewById(R.id.dot_iv102);
		dots_grids[9][02] = (ImageView) findViewById(R.id.dot_iv103);
		dots_grids[9][03] = (ImageView) findViewById(R.id.dot_iv104);
		dots_grids[9][04] = (ImageView) findViewById(R.id.dot_iv105);
		dots_grids[9][05] = (ImageView) findViewById(R.id.dot_iv106);
		dots_grids[9][06] = (ImageView) findViewById(R.id.dot_iv107);
		dots_grids[9][7] = (ImageView) findViewById(R.id.dot_iv108);
		dots_grids[9][8] = (ImageView) findViewById(R.id.dot_iv109);
		dots_grids[9][9] = (ImageView) findViewById(R.id.dot_iv1010);
		dots_grids[9][10] = (ImageView) findViewById(R.id.dot_iv1011);
		dots_grids[9][11] = (ImageView) findViewById(R.id.dot_iv1012);
		dots_grids[9][12] = (ImageView) findViewById(R.id.dot_iv1013);
		dots_grids[9][13] = (ImageView) findViewById(R.id.dot_iv1014);
		dots_grids[9][14] = (ImageView) findViewById(R.id.dot_iv1015);
		dots_grids[9][15] = (ImageView) findViewById(R.id.dot_iv1016);
		
		dots_grids[10][00] = (ImageView) findViewById(R.id.dot_iv1101);
		dots_grids[10][01] = (ImageView) findViewById(R.id.dot_iv1102);
		dots_grids[10][02] = (ImageView) findViewById(R.id.dot_iv1103);
		dots_grids[10][03] = (ImageView) findViewById(R.id.dot_iv1104);
		dots_grids[10][04] = (ImageView) findViewById(R.id.dot_iv1105);
		dots_grids[10][05] = (ImageView) findViewById(R.id.dot_iv1106);
		dots_grids[10][06] = (ImageView) findViewById(R.id.dot_iv1107);
		dots_grids[10][7] = (ImageView) findViewById(R.id.dot_iv1108);
		dots_grids[10][8] = (ImageView) findViewById(R.id.dot_iv1109);
		dots_grids[10][9] = (ImageView) findViewById(R.id.dot_iv11010);
		dots_grids[10][10] = (ImageView) findViewById(R.id.dot_iv11011);
		dots_grids[10][11] = (ImageView) findViewById(R.id.dot_iv11012);
		dots_grids[10][12] = (ImageView) findViewById(R.id.dot_iv11013);
		dots_grids[10][13] = (ImageView) findViewById(R.id.dot_iv11014);
		dots_grids[10][14] = (ImageView) findViewById(R.id.dot_iv11015);
		dots_grids[10][15] = (ImageView) findViewById(R.id.dot_iv11016);
		
		dots_grids[11][00] = (ImageView) findViewById(R.id.dot_iv1201);
		dots_grids[11][01] = (ImageView) findViewById(R.id.dot_iv1202);
		dots_grids[11][02] = (ImageView) findViewById(R.id.dot_iv1203);
		dots_grids[11][03] = (ImageView) findViewById(R.id.dot_iv1204);
		dots_grids[11][04] = (ImageView) findViewById(R.id.dot_iv1205);
		dots_grids[11][05] = (ImageView) findViewById(R.id.dot_iv1206);
		dots_grids[11][06] = (ImageView) findViewById(R.id.dot_iv1207);
		dots_grids[11][7] = (ImageView) findViewById(R.id.dot_iv1208);
		dots_grids[11][8] = (ImageView) findViewById(R.id.dot_iv1209);
		dots_grids[11][9] = (ImageView) findViewById(R.id.dot_iv12010);
		dots_grids[11][10] = (ImageView) findViewById(R.id.dot_iv12011);
		dots_grids[11][11] = (ImageView) findViewById(R.id.dot_iv12012);
		dots_grids[11][12] = (ImageView) findViewById(R.id.dot_iv12013);
		dots_grids[11][13] = (ImageView) findViewById(R.id.dot_iv12014);
		dots_grids[11][14] = (ImageView) findViewById(R.id.dot_iv12015);
		dots_grids[11][15] = (ImageView) findViewById(R.id.dot_iv12016);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHeight = dm.heightPixels;
		screenWidth  = dm.widthPixels;
		each_dot_width = (screenHeight-25)/12;
		mMediaPlayer=new MediaPlayer();
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(this, R.raw.select6, 1));
		soundPoolMap.put(2, soundPool.load(this, R.raw.connect1, 1));
		soundPoolMap.put(3, soundPool.load(this, R.raw.warning6, 1));
		soundPoolMap.put(4, soundPool.load(this, R.raw.fail2, 1));
		soundPoolMap.put(5, soundPool.load(this, R.raw.win2, 1));
		soundPoolMap.put(6, soundPool.load(this, R.raw.select10, 1));
		soundPoolMap.put(7, soundPool.load(this, R.raw.tick1, 1));
		
		progressBarHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
		
		levelinfo_tv= (Button) findViewById(R.id.levelinfo_tv);
		
		//countdown_seconds=levels_time[level_id-1];
		countdown_seconds=levels_time;
		leveltime_tv.setText(""+countdown_seconds);
		levelinfo_tv.setText("Level "+level_id);
		levelinfo_tv.setTypeface(fonttype);
		
		progressBarHorizontal.setMax(countdown_seconds);
		progressBarHorizontal.setProgress(countdown_seconds);
		if(each_dot_width>128)
			each_dot_width=128;
		
		//Toast.makeText(getApplicationContext(), "screenWidth="+screenWidth, Toast.LENGTH_SHORT).show();
		
		for (int i = 0; i <12; i++) {
			for (int j = 0; j <16; j++) {
				dots_grids_values[i][j]=0;
				LayoutParams sq_layout_para = dots_grids[i][j].getLayoutParams();
				sq_layout_para.height = each_dot_width;
				if(i==0||i==11)
					sq_layout_para.height = each_dot_width;
				
				sq_layout_para.width = each_dot_width+6;
				if(j==0||j==15)
					sq_layout_para.width = each_dot_width+6;
				
				dots_grids[i][j].setLayoutParams(sq_layout_para);
				dots_grids[i][j].setOnTouchListener(this);
			}
		}
		
		for(int i=0;i<16;i++)
		{
			dots_grids[0][i].setBackgroundResource(R.drawable.blank);
			dots_grids[11][i].setBackgroundResource(R.drawable.blank);
		}
		
		for(int i=0;i<12;i++)
		{
			dots_grids[i][0].setBackgroundResource(R.drawable.blank);
			dots_grids[i][15].setBackgroundResource(R.drawable.blank);
		}
		
		ArrayList<Integer> numbers_array1 = new ArrayList<Integer>();
		ArrayList<Integer> numbers_array2 = new ArrayList<Integer>();
		for(int i=0;i<10*14/2;i++)
		{
			Random temp_random = new Random(i*1000+System.currentTimeMillis());
			int temp_no = temp_random.nextInt(dots_images.length)+1;
			numbers_array1.add(temp_no);
			numbers_array2.add(temp_no);
		}
		
		for(int i=0;i<10*14/2;i++)
		{
			numbers_array1.add(numbers_array2.get(numbers_array2.size()-1));
			numbers_array2.remove(numbers_array2.size()-1);
		}
		
		for(int i=1;i<11;i++)
			for(int j=1;j<15;j++)
		 {
			 Random temp_random = new Random(i*j*1000+i+j+System.currentTimeMillis());
			 int temp_no = temp_random.nextInt(numbers_array1.size());
			 if(i==0||i==11||j==0||j==15)
			 {
				 dots_grids_values[i][j]=0;
			 }
			 else
			 {
				 dots_grids_values[i][j]=numbers_array1.get(temp_no);
			 	 numbers_array1.remove(temp_no);
			 }
		 }
		

		
		numbers_array1 = new ArrayList<Integer>();
		for(int i=0;i<12;i++)
			for(int j=0;j<16;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					numbers_array1.add(dots_grids_values[i][j]);
				}
			}
		
		for(int i=0;i<12;i++)
			for(int j=0;j<16;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					 Random temp_random = new Random(i*j*1000+i+j);
					 int temp_no = temp_random.nextInt(numbers_array1.size());
					 dots_grids_values[i][j]=numbers_array1.get(temp_no);
					 numbers_array1.remove(temp_no);
				}
			}
		

		
		for(int i=0;i<12;i++)
			for(int j=0;j<16;j++)
			{
				if(dots_grids_values[i][j]>0)
				{
					dots_grids[i][j].setBackgroundResource(dots_images[dots_grids_values[i][j]-1]);
				}
			}
		
		
		
		checkmatchtime_handler = new Handler();
		checkmatchtime_runnable = new Runnable() {
			@Override
			public void run() {
				check_match_available();
				checkmatchtime_handler.postDelayed(this, 2000);
			}
		};
		
		countdowntime_handler = new Handler();
		countdowntime_runnable = new Runnable() {
			@Override
			public void run() {
				countdown_seconds--;
				progressBarHorizontal.setProgress(countdown_seconds);
				leveltime_tv.setText(""+countdown_seconds);
				if(countdown_seconds<=20)
					playEffect(7,0);
				if(countdown_seconds>0)
					countdowntime_handler.postDelayed(countdowntime_runnable, 1000);
				else
				{
					ShowFailLevelMenu();
				}
			}
		};
		
		scorestime_handler = new Handler();
		scorestime_runnable = new Runnable() {
			@Override
			public void run() {
				int score=Integer.valueOf(levelscore_tv.getText().toString());
				if(score<level_scores)
				{
					score++;
					levelscore_tv.setText(String.valueOf(score));
					scorestime_handler.postDelayed(this, 50);
				}
			}
		};
		
		
		hinttime_handler = new Handler();
		hinttime_runnable = new Runnable() {
			@Override
			public void run() {
				
				if(hinttime_count==4 || hinttime_count==2)
				{
					dots_grids[hint_x1][hint_y1].setImageResource(R.drawable.blank);
					dots_grids[hint_x2][hint_y2].setImageResource(R.drawable.blank);
				}
				else
				{
					dots_grids[hint_x1][hint_y1].setImageResource(R.drawable.selecthint);
					dots_grids[hint_x2][hint_y2].setImageResource(R.drawable.selecthint);
				}
				 
				hinttime_count--;
				if(hinttime_count>0)
					hinttime_handler.postDelayed(this, 150);
			}
		};
		
		dotsclear_handler = new Handler();
		dotsclear_runnable = new Runnable() {
			@Override
			public void run() {
				int summary_value=0;
				for(int i=0;i<12;i++)
					for(int j=0;j<16;j++)
					{
						summary_value=summary_value+dots_grids_values[i][j];
						if(dots_grids_values[i][j]==0)
						{
							dots_grids[i][j].setBackgroundResource(R.drawable.blank);
							dots_grids[i][j].setImageResource(R.drawable.blank);
						}
					}
				if(summary_value==0)
				{
					ShowNextLevelMenu();
				}
			}
		};
		
				
		Interstitial_handler = new Handler();
		Interstitial_runnable = new Runnable() {
			@Override
			public void run() {
			      AdRequest adRequest = new AdRequest();
			      //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
			      interstitialAd.loadAd(adRequest);
			}
		};
		
		connection_handler = new Handler();
		connection_runnable = new Runnable() {
			@Override
			public void run() {
				scorestime_handler.postDelayed(scorestime_runnable, 50);
				Show_Connection();
				playEffect(2,0);
				dotsclear_handler.postDelayed(dotsclear_runnable, 150);
			}
		};
		
		View.OnClickListener vclick =new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				playSounds(R.raw.click);
				switch(arg0.getId())
				{
					case R.id.levelpattern_btn:
						ShowPatternSelectlMenu();
					break;
					case R.id.levelpause_btn:
						ShowResumeLevelMenu();
						break;
					case R.id.levelmenu_btn:
						ShowMenuLevelMenu();
						break;
					case R.id.leveltip_btn:
						if(level_hints>0)
						{
							if(hint_x1==0)
							{
								if(dot_numbers.size()==1)
								{
									int presx=dot_numbers.get(0)[0];
									int presy=dot_numbers.get(0)[1];
									dot_numbers.remove(0);
									dots_grids[presx][presy].setImageResource(R.drawable.blank);
									
								}
								show_hints();
							}
						}
						break;
				}
			}
		};
		
		levelmenu_btn.setOnClickListener(vclick);
		levelpause_btn.setOnClickListener(vclick);
		leveltip_btn.setOnClickListener(vclick);
		levelpattern_btn.setOnClickListener(vclick);
	}


	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(event.getAction()==MotionEvent.ACTION_UP)
		{
			Log.d(TAG, "Up");
			for (int i = 1; i < 11; i++)
			for (int j = 1; j < 15; j++)
			if(arg0.equals(dots_grids[i][j])&dots_grids_values[i][j]!=0)
			{
				if(hint_x1>0)
				{
					if((i==hint_x1 & j==hint_y1) ||(i==hint_x2 & j==hint_y2))
					{
						
					}
					else
					{
						playEffect(3, 0);
						Toast.makeText(getApplicationContext(), "Please select the 2 animals with hints.", Toast.LENGTH_SHORT).show();
						return true;
					}
				}
				Log.d(TAG, "dots_grids="+i+","+j);
				playEffect(6, 0);
				Integer[] dot={i,j};
				dot_numbers.add(dot);
				dots_grids[i][j].setImageResource(R.drawable.select);
				if(dot_numbers.size()==2)
				{
					if(hint_x1>0)
					{
					  if(hint_x1==dot_numbers.get(0)[0]&hint_y1==dot_numbers.get(0)[1]&hint_x2==dot_numbers.get(1)[0]&hint_y2==dot_numbers.get(1)[1])
					  {
						 hint_x1=0;
					  }
					  
					  if(hint_x1==dot_numbers.get(1)[0]&hint_y1==dot_numbers.get(1)[1]&hint_x2==dot_numbers.get(0)[0]&hint_y2==dot_numbers.get(0)[1])
					  {
						 hint_x1=0;
					  }
					}
					Clear_Dots();
				}
			}
		}
		
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			Log.d(TAG, "Down");
		}
		return true;
	}

	private void Clear_Dots() {
		// TODO Auto-generated method stub
		dot1_x=dot_numbers.get(0)[0];
		dot1_y=dot_numbers.get(0)[1];
		
		dot2_x=dot_numbers.get(1)[0];
		dot2_y=dot_numbers.get(1)[1];
		if(Check_Dots_xy2(dot1_x,dot1_y,dot2_x,dot2_y))
		{
			if(dots_grids_values[dot1_x][dot1_y]==dots_grids_values[dot2_x][dot2_y])
			{
				Log.d(TAG, "Can match ... ");
				dot_numbers.remove(0);
				dot_numbers.remove(0);
				
				dots_grids_values[dot1_x][dot1_y]=0;
				dots_grids_values[dot2_x][dot2_y]=0;
				countdown_seconds++;
				get_scores(dot1_x,dot1_y,dot2_x,dot2_y);
				leveltime_tv.setText(""+countdown_seconds);
				progressBarHorizontal.setProgress(countdown_seconds);
				connection_handler.postDelayed(connection_runnable, 150);
			}
			else
			{
				//playEffect(3,0);
				if(dot1_x==dot2_x&dot1_y==dot2_y)
				{
					dot_numbers.remove(0);
					dot_numbers.remove(0);
				}
				else
					dot_numbers.remove(0);

				
				Log.d(TAG, "Can not match ... ");
				dots_grids[dot1_x][dot1_y].setImageResource(R.drawable.blank);
				
				//dots_grids[dot2_x][dot2_y].setImageResource(R.drawable.blank);
			}
		}
		else
		{
			//playEffect(3,0);
			if(dot1_x==dot2_x&dot1_y==dot2_y)
			{
				dot_numbers.remove(0);
			}
			else
			{
				if(dots_grids_values[dot1_x][dot1_y]==dots_grids_values[dot2_x][dot2_y])
				{
					    playEffect(3,0);
				}
			}
			
			dot_numbers.remove(0);
			dots_grids[dot1_x][dot1_y].setImageResource(R.drawable.blank);
			
			Log.d(TAG, "Can not move");
			
			//dots_grids[dot2_x][dot2_y].setImageResource(R.drawable.blank);
		}
	}
	
	private boolean Check_Dots_xy2(int x1, int y1, int x2, int y2)
	{

		dot_numbers_match=new ArrayList<Integer[]>();
		//click it self
		if(x1==x2 & y1==y2)
		{
			return false;
		}
		
		if(x1==x2)
		{
			int movevalue=0;
			if(y1<y2)
			{
				for(int i=y1+1;i<y2;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			else
			{
				for(int i=y2+1;i<y1;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match1={x1,y1};
				Integer[] dot_numbers_match2={x2,y2};
				dot_numbers_match.add(dot_numbers_match1);
				dot_numbers_match.add(dot_numbers_match2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 0");
				return true;
			}
		}
		
		if(y1==y2)
		{
			int movevalue=0;
			if(x1<x2)
			{
				for(int i=x1+1;i<x2;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			else
			{
				for(int i=x2+1;i<x1;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match1={x1,y1};
				Integer[] dot_numbers_match2={x2,y2};
				dot_numbers_match.add(dot_numbers_match1);
				dot_numbers_match.add(dot_numbers_match2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 1");
				return true;
			}
		}
		
		//x1 y1  -- x1 y2
		int movex1y1x2y2=0;
		if(y2>y1)
		{
			for(int i=y1+1;i<=y2;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		else
		{
			for(int i=y2;i<=y1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		
		//x1 y2  -- x2 y2
		if(x2>x1)
		{
			for(int i=x1+1;i<=x2-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		else
		{
			for(int i=x2+1;i<=x1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		
		if(movex1y1x2y2==0)
		{
			Integer[] dot_numbers_match1={x1,y1};
			Integer[] dot_numbers_match2={x1,y2};
			Integer[] dot_numbers_match3={x2,y2};
			
			dot_numbers_match.add(dot_numbers_match1);
			dot_numbers_match.add(dot_numbers_match2);
			dot_numbers_match.add(dot_numbers_match3);
			
			Log.d(TAG, "Move to "+x2+","+y2);
			Log.d(TAG, "Can match ... 0 0");
			return true;
		}
		
		
			//x1 y1  -- x2 y1
				movex1y1x2y2=0;
				if(x2>x1)
				{
					for(int i=x1+1;i<=x2;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				else
				{
					for(int i=x2;i<=x1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				//x2 y1  -- x2 y2
				if(y2>y1)
				{
					for(int i=y1+1;i<=y2-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				else
				{
					for(int i=y2+1;i<=y1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				
				if(movex1y1x2y2==0)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y1};
					Integer[] dot_numbers_match3={x2,y2};
					
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					dot_numbers_match.add(dot_numbers_match3);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 1 1");
					return true;
				}
			
		
		// row - x check
		for(int i=1;i<16;i++)
		{
			int move1=Check_Dots_x(x1,y1,y1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1-i)+","+i);
				int move2=Check_Dots_y(y1-i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1-i));
					int move3=Check_Dots_x(x2,y1-i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match1={x1,y1};
							Integer[] dot_numbers_match2={x1,y1-i};
							Integer[] dot_numbers_match3={x2,y1-i};
							Integer[] dot_numbers_match4={x2,y2};
							dot_numbers_match.add(dot_numbers_match1);
							dot_numbers_match.add(dot_numbers_match2);
							dot_numbers_match.add(dot_numbers_match3);
							dot_numbers_match.add(dot_numbers_match4);
						
							Log.d(TAG, "Move to "+x2+","+y2);
							Log.d(TAG, "Can match ... 4");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1-i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1,y1-i};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+x2+","+y2);
						Log.d(TAG, "Can match ... 3");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1-i)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 2");
					return true;
				}
			}
			
			
			move1=Check_Dots_x(x1,y1,y1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1+i)+","+i);
				int move2=Check_Dots_y(y1+i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1+i));
					int move3=Check_Dots_x(x2,y1+i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match1={x1,y1};
							Integer[] dot_numbers_match2={x1,y1+i};
							Integer[] dot_numbers_match3={x2,y1+i};
							Integer[] dot_numbers_match4={x2,y2};
							dot_numbers_match.add(dot_numbers_match1);
							dot_numbers_match.add(dot_numbers_match2);
							dot_numbers_match.add(dot_numbers_match3);
							dot_numbers_match.add(dot_numbers_match4);
							Log.d(TAG, "Move to "+x2+","+(y2));
							Log.d(TAG, "Can match ... 7");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1+i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1,y1+i};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+x2+","+(y2));
						Log.d(TAG, "Can match ... 6");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1+i)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					Log.d(TAG, "Move to "+x2+","+(y2));
					Log.d(TAG, "Can match ... 5");
					return true;
				}
			}
			
		}
		
//		//column y check
		for(int i=1;i<12;i++)
		{
			int move1=Check_Dots_y(y1,x1,x1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1-i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1-i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1-i)+","+(y2));
					int move3=Check_Dots_y(y2,x1-i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1-i,y1};
						Integer[] dot_numbers_match3={x1-i,y2};
						Integer[] dot_numbers_match4={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						dot_numbers_match.add(dot_numbers_match4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 10");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1-i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1-i,y1};
						Integer[] dot_numbers_match3={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 9");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1-i & y2==y1)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 8");
					return true;
				}
			}
			
			
			move1=Check_Dots_y(y1,x1,x1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1+i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1+i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1+i)+","+(y2));
					int move3=Check_Dots_y(y2,x1+i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1+i,y1};
						Integer[] dot_numbers_match3={x1+i,y2};
						Integer[] dot_numbers_match4={x2,y2};
						
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						dot_numbers_match.add(dot_numbers_match4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 13");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1+i)
					{
						Integer[] dot_numbers_match1={x1,y1};
						Integer[] dot_numbers_match2={x1+i,y1};
						Integer[] dot_numbers_match3={x2,y2};
						dot_numbers_match.add(dot_numbers_match1);
						dot_numbers_match.add(dot_numbers_match2);
						dot_numbers_match.add(dot_numbers_match3);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 12");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1+i & y2==y1)
				{
					Integer[] dot_numbers_match1={x1,y1};
					Integer[] dot_numbers_match2={x2,y2};
					dot_numbers_match.add(dot_numbers_match1);
					dot_numbers_match.add(dot_numbers_match2);
					
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 11");
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	private boolean Check_Dots_xy3(int x1, int y1, int x2, int y2)
	{

		ArrayList<Integer[]> dot_numbers_match_check=new ArrayList<Integer[]>();
		//click it self
		if(x1==x2 & y1==y2)
		{
			return false;
		}
		
		if(x1==x2)
		{
			int movevalue=0;
			if(y1<y2)
			{
				for(int i=y1+1;i<y2;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			else
			{
				for(int i=y2+1;i<y1;i++)
				{
					movevalue=movevalue+dots_grids_values[x1][i];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match_check1={x1,y1};
				Integer[] dot_numbers_match_check2={x2,y2};
				dot_numbers_match_check.add(dot_numbers_match_check1);
				dot_numbers_match_check.add(dot_numbers_match_check2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 0");
				return true;
			}
		}
		
		if(y1==y2)
		{
			int movevalue=0;
			if(x1<x2)
			{
				for(int i=x1+1;i<x2;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			else
			{
				for(int i=x2+1;i<x1;i++)
				{
					movevalue=movevalue+dots_grids_values[i][y1];
				}
			}
			if(movevalue==0)
			{
				Integer[] dot_numbers_match_check1={x1,y1};
				Integer[] dot_numbers_match_check2={x2,y2};
				dot_numbers_match_check.add(dot_numbers_match_check1);
				dot_numbers_match_check.add(dot_numbers_match_check2);
				
				Log.d(TAG, "Move to "+x2+","+y2);
				Log.d(TAG, "Can match ... 1");
				return true;
			}
		}
		
		//x1 y1  -- x1 y2
		int movex1y1x2y2=0;
		if(y2>y1)
		{
			for(int i=y1+1;i<=y2;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		else
		{
			for(int i=y2;i<=y1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[x1][i];
		}
		
		//x1 y2  -- x2 y2
		if(x2>x1)
		{
			for(int i=x1+1;i<=x2-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		else
		{
			for(int i=x2+1;i<=x1-1;i++)
				movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y2];
		}
		
		if(movex1y1x2y2==0)
		{
			Integer[] dot_numbers_match_check1={x1,y1};
			Integer[] dot_numbers_match_check2={x1,y2};
			Integer[] dot_numbers_match_check3={x2,y2};
			
			dot_numbers_match_check.add(dot_numbers_match_check1);
			dot_numbers_match_check.add(dot_numbers_match_check2);
			dot_numbers_match_check.add(dot_numbers_match_check3);
			
			Log.d(TAG, "Move to "+x2+","+y2);
			Log.d(TAG, "Can match ... 0 0");
			return true;
		}
		
		
			//x1 y1  -- x2 y1
				movex1y1x2y2=0;
				if(x2>x1)
				{
					for(int i=x1+1;i<=x2;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				else
				{
					for(int i=x2;i<=x1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[i][y1];
				}
				//x2 y1  -- x2 y2
				if(y2>y1)
				{
					for(int i=y1+1;i<=y2-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				else
				{
					for(int i=y2+1;i<=y1-1;i++)
						movex1y1x2y2=movex1y1x2y2+dots_grids_values[x2][i];
				}
				
				if(movex1y1x2y2==0)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y1};
					Integer[] dot_numbers_match_check3={x2,y2};
					
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					dot_numbers_match_check.add(dot_numbers_match_check3);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 1 1");
					return true;
				}
			
		
		// row - x check
		for(int i=1;i<16;i++)
		{
			int move1=Check_Dots_x(x1,y1,y1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1-i)+","+i);
				int move2=Check_Dots_y(y1-i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1-i));
					int move3=Check_Dots_x(x2,y1-i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match_check1={x1,y1};
							Integer[] dot_numbers_match_check2={x1,y1-i};
							Integer[] dot_numbers_match_check3={x2,y1-i};
							Integer[] dot_numbers_match_check4={x2,y2};
							dot_numbers_match_check.add(dot_numbers_match_check1);
							dot_numbers_match_check.add(dot_numbers_match_check2);
							dot_numbers_match_check.add(dot_numbers_match_check3);
							dot_numbers_match_check.add(dot_numbers_match_check4);
						
							Log.d(TAG, "Move to "+x2+","+y2);
							Log.d(TAG, "Can match ... 4");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1-i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1,y1-i};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+x2+","+y2);
						Log.d(TAG, "Can match ... 3");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1-i)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					
					Log.d(TAG, "Move to "+x2+","+y2);
					Log.d(TAG, "Can match ... 2");
					return true;
				}
			}
			
			
			move1=Check_Dots_x(x1,y1,y1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+x1+","+(y1+i)+","+i);
				int move2=Check_Dots_y(y1+i,x1,x2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+x2+","+(y1+i));
					int move3=Check_Dots_x(x2,y1+i,y2);
					if(move3==dots_grids_values[x2][y2])
					{
							Integer[] dot_numbers_match_check1={x1,y1};
							Integer[] dot_numbers_match_check2={x1,y1+i};
							Integer[] dot_numbers_match_check3={x2,y1+i};
							Integer[] dot_numbers_match_check4={x2,y2};
							dot_numbers_match_check.add(dot_numbers_match_check1);
							dot_numbers_match_check.add(dot_numbers_match_check2);
							dot_numbers_match_check.add(dot_numbers_match_check3);
							dot_numbers_match_check.add(dot_numbers_match_check4);
							Log.d(TAG, "Move to "+x2+","+(y2));
							Log.d(TAG, "Can match ... 7");
							return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & y2==y1+i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1,y1+i};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+x2+","+(y2));
						Log.d(TAG, "Can match ... 6");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1 & y2==y1+i)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					Log.d(TAG, "Move to "+x2+","+(y2));
					Log.d(TAG, "Can match ... 5");
					return true;
				}
			}
			
		}
		
//		//column y check
		for(int i=1;i<12;i++)
		{
			int move1=Check_Dots_y(y1,x1,x1-i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1-i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1-i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1-i)+","+(y2));
					int move3=Check_Dots_y(y2,x1-i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1-i,y1};
						Integer[] dot_numbers_match_check3={x1-i,y2};
						Integer[] dot_numbers_match_check4={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						dot_numbers_match_check.add(dot_numbers_match_check4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 10");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1-i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1-i,y1};
						Integer[] dot_numbers_match_check3={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 9");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1-i & y2==y1)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 8");
					return true;
				}
			}
			
			
			move1=Check_Dots_y(y1,x1,x1+i);
			if(move1==0)
			{
				Log.d(TAG, "Move to "+(x1+i)+","+(y1)+","+i);
				int move2=Check_Dots_x(x1+i,y1,y2);
				if(move2==0)
				{
					Log.d(TAG, "Move to "+(x1+i)+","+(y2));
					int move3=Check_Dots_y(y2,x1+i,x2);
					if(move3==dots_grids_values[x2][y2])
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1+i,y1};
						Integer[] dot_numbers_match_check3={x1+i,y2};
						Integer[] dot_numbers_match_check4={x2,y2};
						
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						dot_numbers_match_check.add(dot_numbers_match_check4);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 13");
						return true;
					}
				}
				else
				{
					if(move2==dots_grids_values[x2][y2] & x2==x1+i)
					{
						Integer[] dot_numbers_match_check1={x1,y1};
						Integer[] dot_numbers_match_check2={x1+i,y1};
						Integer[] dot_numbers_match_check3={x2,y2};
						dot_numbers_match_check.add(dot_numbers_match_check1);
						dot_numbers_match_check.add(dot_numbers_match_check2);
						dot_numbers_match_check.add(dot_numbers_match_check3);
						
						Log.d(TAG, "Move to "+(x2)+","+(y2));
						Log.d(TAG, "Can match ... 12");
						return true;
					}
				}
			}
			else
			{
				if(move1==dots_grids_values[x2][y2] & x2==x1+i & y2==y1)
				{
					Integer[] dot_numbers_match_check1={x1,y1};
					Integer[] dot_numbers_match_check2={x2,y2};
					dot_numbers_match_check.add(dot_numbers_match_check1);
					dot_numbers_match_check.add(dot_numbers_match_check2);
					
					Log.d(TAG, "Move to "+(x2)+","+(y2));
					Log.d(TAG, "Can match ... 11");
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void ConnectionXY(int x1, int y1, int x2, int y2)
	{
		for(int i=x1;i<=x2;i++)
		{
			dots_grids[i][y1].setImageResource(R.drawable.connect);
		}
		for(int i=x2;i<=x1;i++)
		{
			dots_grids[i][y1].setImageResource(R.drawable.connect);
		}
		
		for(int i=y1;i<=y2;i++)
		{
			dots_grids[x1][i].setImageResource(R.drawable.connect);
		}
		for(int i=y2;i<=y1;i++)
		{
			dots_grids[x1][i].setImageResource(R.drawable.connect);
		}
	}
	
	private void Show_Connection()
	{
		// dot_numbers_match.size() == 2   connect straightly 
		// dot_numbers_match.size() == 3   connect with 1 angle
		// dot_numbers_match.size() == 4   connect with 2 angles
		
		if(dot_numbers_match.size()==2)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		}
		
		if(dot_numbers_match.size()==3)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  int x3=dot_numbers_match.get(2)[0];
		  int y3=dot_numbers_match.get(2)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		  ConnectionXY(x2,y2,x3,y3);
		}
		
		if(dot_numbers_match.size()==4)
		{
		  int x1=dot_numbers_match.get(0)[0];
		  int y1=dot_numbers_match.get(0)[1];
		
		  int x2=dot_numbers_match.get(1)[0];
		  int y2=dot_numbers_match.get(1)[1];
		  
		  int x3=dot_numbers_match.get(2)[0];
		  int y3=dot_numbers_match.get(2)[1];
		  
		  int x4=dot_numbers_match.get(3)[0];
		  int y4=dot_numbers_match.get(3)[1];
		  
		  ConnectionXY(x1,y1,x2,y2);
		  ConnectionXY(x2,y2,x3,y3);
		  ConnectionXY(x3,y3,x4,y4);
		}
	}
	
	
	private int Check_Dots_x(int x, int y1,int y2)
	{
		int value=0;
		int yy1=y1+1;
		int yy2=y2;
		
		if(y2<0||y2>15)
			return -1;
		
		if( y1>y2)
		{
			yy1=y2;
			yy2=y1-1;
		}
		
		for(int i=yy1;i<=yy2;i++)
		{
			value=value+dots_grids_values[x][i];
		}
		
		return value;
	}
	
	private int Check_Dots_y(int y, int x1,int x2)
	{
		int value=0;
		int xx1=x1+1;
		int xx2=x2;
		
		if(x2<0||x2>11)
			return -1;
		
		if( x1>x2)
		{
			xx1=x2;
			xx2=x1-1;
		}
		
		for(int i=xx1;i<=xx2;i++)
		{
			value=value+dots_grids_values[i][y];
		}

		return value;
	}
	
	private boolean Check_Dots_xy(int x1, int y1, int x2, int y2)
	{
		int x=0, y=0;
		if(x1>1)
		{
			x=x1-1;
			y=y1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(x1<12)
		{
			x=x1+1;
			y=y1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(y1>1)
		{
			x=x1;
			y=y1-1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		if(y1<14)
		{
			x=x1;
			y=y1+1;
			if(x==x2 & y==y2)
			{
				return true;
			}
			else
			{
				if(dots_grids_values[x][y]==0)
				{
					Check_Dots_xy(x,y,x2,y2);
				}
			}
		}
		
		return false;
	}

	private void playSounds(int sid) {

		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mMediaPlayer = MediaPlayer.create(MainActivity.this, sid);

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
	    	ShowMenuLevelMenu();
	    	return true; 
	    }else{
	    return super.onKeyDown(keyCode, event);    
	    }
	    }
	 
	 @Override
	    protected void onResume() { 
	     	super.onResume();
			if(first_time)
	     	{
	     		countdowntime_handler.postDelayed(countdowntime_runnable, 1000);
	     		checkmatchtime_handler.postDelayed(checkmatchtime_runnable, 2000);
	     		Interstitial_handler.postDelayed(Interstitial_runnable, 1500);
	     		first_time=false;
	     		playSounds(R.raw.start);
	     	}
	 }
	 
		@Override
	    public void onDestroy() 
		{
	        super.onDestroy();
	        countdowntime_handler.removeCallbacks(countdowntime_runnable);
	        checkmatchtime_handler.removeCallbacks(checkmatchtime_runnable);
	        Log.d("WeAndroids", "Destroy====================================");
	    }
		
//		@Override
//		public void onConfigurationChanged(Configuration newConfig)
//		{
//	        // TODO Auto-generated method stub
//			
//	        if (this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) 
//	        {
//	           // Nothing need to be done here
//	            
//	        } else {
//	           // Nothing need to be done here
//	        }
//	        super.onConfigurationChanged(newConfig);
//	    }
		
		 private void ShowResumeLevelMenu() {
			 countdowntime_handler.removeCallbacks(countdowntime_runnable);
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
				
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				
				Animation animation1 = AnimationUtils.loadAnimation(
						MainActivity.this, R.anim.apppop1);
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
						countdowntime_handler.postDelayed(countdowntime_runnable, 500);
					}
				});
		 }
		 
		 private void ShowMenuLevelMenu() {
			 countdowntime_handler.removeCallbacks(countdowntime_runnable);
			 final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.menu_level_menu);

				Button btnyes = (Button) window.findViewById(R.id.btnyes);
				Button btnno = (Button) window.findViewById(R.id.btnno);
				TextView levelinfotv= (TextView) window.findViewById(R.id.levelinfotv);
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnyes.setTypeface(fonttype);
				btnno.setTypeface(fonttype);
				levelinfotv.setTypeface(fonttype);
				
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				
				btnno.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
						playSounds(R.raw.click);
						dlg.cancel();
						countdowntime_handler.postDelayed(countdowntime_runnable, 500);
					}
				});
				
				btnyes.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						
						if(level_best_scores<level_scores)
							level_best_scores=level_scores;
						
						settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
						
						settings_info.edit().putInt(Level_ID, 1).commit();
						settings_info.edit().putInt(Level_Scores, 0).commit();
						settings_info.edit().putInt(Level_Hints, 9).commit();
						if (interstitialAd.isReady()&interstitialAd_ready) 
						{
							exit_menu=true;
							interstitialAd.show();
						}
						else
						{
							finish();
						}
						
					}
				});
		 }
		 
		 private void ShowNextLevelMenu() {
			    countdowntime_handler.removeCallbacks(countdowntime_runnable);
			    final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.next_level_menu);
				Button btnnext = (Button) window.findViewById(R.id.btnnext);
				TextView levelcleartv= (TextView) window.findViewById(R.id.levelcleartv);
				TextView levelscoretv= (TextView) window.findViewById(R.id.levelscoretv);
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnnext.setTypeface(fonttype);
				levelcleartv.setTypeface(fonttype);
				levelscoretv.setTypeface(fonttype);
				levelcleartv.setText("Level "+level_id+" cleared!");
				//level_scores=level_scores+levels_time[level_id-1]-countdown_seconds;
				level_scores=level_scores+countdown_seconds;
				levelscoretv.setText(""+level_scores);
				scorestime_handler.postDelayed(scorestime_runnable, 50);
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				playEffect(5,0);
				
				if(level_best_scores<level_scores)
					level_best_scores=level_scores;
				
				int difftime=countdown_seconds/3;
				if(difftime>=20)
					difftime=20;
				else if(difftime>=15)
					difftime=15;
				else if(difftime>=10)
					difftime=10;
				else if(difftime>=5)
					difftime=5;
				else
					difftime=2;
				
				if(level_id<=2)
					difftime=15;
				
				level_hints=level_hints+1;
				settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
				settings_info.edit().putInt(Levels_Time+(level_id+1), levels_time-difftime).commit();
				settings_info.edit().putInt(Level_Hints, level_hints).commit();
				
				btnnext.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						level_id=level_id+1;
						settings_info.edit().putInt(Level_ID, level_id).commit();
						settings_info.edit().putInt(Level_Scores, level_scores).commit();
						dlg.cancel();
						
						if (interstitialAd.isReady()&interstitialAd_ready) 
						{
							interstitialAd.show();
						}
						else
						{
							finish();
							Intent intentapp=new Intent(MainActivity.this,MainActivity.class);
							startActivity(intentapp);
						}
					}
				});
		 }
		 
		 private void ShowPatternSelectlMenu() {
			    final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(true).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.menu_pattern_menu);
				
				Button btn_animals = (Button) window.findViewById(R.id.btn_animals);
				Button btn_christmas = (Button) window.findViewById(R.id.btn_christmas);
				Button btn_flags = (Button) window.findViewById(R.id.btn_flags);
				
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				
				btn_animals.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						for(int i=0;i<dots_images.length;i++)
							dots_images[i]=dots_animals[i];
						
						for(int i=0;i<12;i++)
							for(int j=0;j<16;j++)
							{
								if(dots_grids_values[i][j]>0)
								{
									dots_grids[i][j].setBackgroundResource(dots_images[dots_grids_values[i][j]-1]);
								}
							}
					}
				});
				
				btn_christmas.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						for(int i=0;i<dots_images.length;i++)
							dots_images[i]=dots_xmas[i];
						
						for(int i=0;i<12;i++)
							for(int j=0;j<16;j++)
							{
								if(dots_grids_values[i][j]>0)
								{
									dots_grids[i][j].setBackgroundResource(dots_images[dots_grids_values[i][j]-1]);
								}
							}
					}
				});
				
				btn_flags.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						dlg.cancel();
						for(int i=0;i<dots_images.length;i++)
							dots_images[i]=dots_flags[i];
						
						for(int i=0;i<12;i++)
							for(int j=0;j<16;j++)
							{
								if(dots_grids_values[i][j]>0)
								{
									dots_grids[i][j].setBackgroundResource(dots_images[dots_grids_values[i][j]-1]);
								}
							}
					}
				});
				
		 }
		 
		 private void ShowFailLevelMenu() {
			    final AlertDialog dlg = new AlertDialog.Builder(this).setCancelable(false).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.fail_level_menu);
				
				Button btnok = (Button) window.findViewById(R.id.btnok);
				TextView levelinfotv= (TextView) window.findViewById(R.id.levelinfotv);
				TextView levelfailtv= (TextView) window.findViewById(R.id.levelfailtv);
				
				TextView levelscoretv1= (TextView) window.findViewById(R.id.levelscoretv1);
				TextView levelbestscoretv1= (TextView) window.findViewById(R.id.levelbestscoretv1);
				TextView levelscoretv2= (TextView) window.findViewById(R.id.levelscoretv2);
				TextView levelbestscoretv2= (TextView) window.findViewById(R.id.levelbestscoretv2);
				
				Typeface fonttype = Typeface.createFromAsset(this.getAssets(),menufonts[1]);
				btnok.setTypeface(fonttype);
				levelinfotv.setTypeface(fonttype);
				levelfailtv.setTypeface(fonttype);
				levelscoretv1.setTypeface(fonttype);
				levelbestscoretv1.setTypeface(fonttype);
				levelscoretv2.setTypeface(fonttype);
				levelbestscoretv2.setTypeface(fonttype);
				
				levelinfotv.setText("You are stopped by Level "+level_id+"!");
				LinearLayout resumelayout= (LinearLayout) window.findViewById(R.id.resumelayout);
				LayoutParams sq_layout_para = resumelayout.getLayoutParams();
				sq_layout_para.height = screenHeight-60;
				sq_layout_para.width = screenWidth-60;
				resumelayout.setLayoutParams(sq_layout_para);
				playEffect(4,0);
				if(level_best_scores<level_scores)
					level_best_scores=level_scores;
				
				levelscoretv2.setText(""+level_scores);
				levelbestscoretv2.setText(""+level_best_scores);
				
				settings_info.edit().putInt(Level_Best_Scores+today_str, level_best_scores).commit();
				settings_info.edit().putInt(Level_Hints, 9).commit();
				
				btnok.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playSounds(R.raw.click);
						level_id=1;
						level_scores=0;
						settings_info.edit().putInt(Level_ID, level_id).commit();
						settings_info.edit().putInt(Level_Scores, level_scores).commit();
						dlg.cancel();
						if (interstitialAd.isReady()&interstitialAd_ready) 
						{
							exit_menu=true;
							interstitialAd.show();
						}
						else
						{
							finish();
						}
					}
				});
		 }
		 
		 private void get_scores(int x1,int y1, int x2, int y2)
		 {
			 level_scores=level_scores+10;
//			 int xspace=Math.abs(x1-x2);
//			 int yspace=Math.abs(y1-y2);
//			 
//			 if(xspace+yspace>1)
//			 {
//				 level_scores=level_scores+xspace+yspace-1;
//			 }
		 }
		 
		 
		 
		 
		 private void show_hints()
		 {
			hint_x1=0;
			hint_y1=0;
			hint_x2=0;
			hint_y2=0;
				
			 for(int i=1;i<11;i++)
				 for(int j=1;j<15;j++)
				 {
					 if(dots_grids_values[i][j]>0)
					 {
						 
						 for(int ii=1;ii<11;ii++)
							 for(int jj=1;jj<15;jj++)
							 {
								 if(dots_grids_values[i][j]==dots_grids_values[ii][jj])
								 {
									 if(Check_Dots_xy2(i,j,ii,jj))
									 {
										 hint_x1=i;
										 hint_y1=j;
										 hint_x2=ii;
										 hint_y2=jj;
											
										 dots_grids[i][j].setImageResource(R.drawable.selecthint);
										 dots_grids[ii][jj].setImageResource(R.drawable.selecthint);
										 level_hints--;
										 leveltip_btn.setText("Hints "+level_hints);
										 settings_info.edit().putInt(Level_Hints, level_hints).commit();
										 
										 if(countdown_seconds>2)
										 {
											 countdown_seconds=countdown_seconds-2;
											 progressBarHorizontal.setProgress(countdown_seconds);
										 }
										 hinttime_count=4;
										 hinttime_handler.postDelayed(hinttime_runnable, 150);
										 return;
									 }
								 }
							 }
					 }
				 }
		 }
		 
		 private void check_match_available()
		 {
				 for(int i=1;i<11;i++)
					 for(int j=1;j<15;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 
							 for(int ii=1;ii<11;ii++)
								 for(int jj=1;jj<15;jj++)
								 {
									 if(dots_grids_values[i][j]==dots_grids_values[ii][jj])
									 {
										 if(Check_Dots_xy3(i,j,ii,jj))
										 {
											 return;
										 }
									 }
								 }
						 }
					 }
				 
				 
				 ArrayList<Integer> numbers_array1 = new ArrayList<Integer>();
				 
				 for(int i=1;i<11;i++)
					 for(int j=1;j<15;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 numbers_array1.add(dots_grids_values[i][j]);
						 }
					 }
				 
				 
				 for(int i=1;i<11;i++)
					 for(int j=1;j<15;j++)
					 {
						 if(dots_grids_values[i][j]>0)
						 {
							 Random temp_random = new Random(i*j*1000+i+j);
							 int temp_no = temp_random.nextInt(numbers_array1.size());
							 dots_grids_values[i][j]=numbers_array1.get(temp_no);
							 dots_grids[i][j].setBackgroundResource(dots_images[dots_grids_values[i][j]-1]);
							 numbers_array1.remove(temp_no);
						 }
					 }
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
		public void onDismissScreen(Ad arg0) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "onDismissScreen");
			
			finish();
			if(!exit_menu)
			{
			 Intent intentapp=new Intent(MainActivity.this,MainActivity.class);
			 startActivity(intentapp);
			}
		}


		@Override
		public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
			// TODO Auto-generated method stub
		    String message = "onFailedToReceiveAd (" + arg1 + ")";
		    Log.d(LOG_TAG, message);
		}


		@Override
		public void onLeaveApplication(Ad arg0) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "onLeaveApplication");
		}


		@Override
		public void onPresentScreen(Ad arg0) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "onPresentScreen");
		}


		@Override
		public void onReceiveAd(Ad arg0) {
			// TODO Auto-generated method stub
			Log.d(LOG_TAG, "onReceiveAd");
			if (arg0 == interstitialAd) 
			{
				interstitialAd_ready=true;
			}
		}
}
