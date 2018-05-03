package chibu.soft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chibu.soft.R;

//import Chibu.Soft.EkoDriverView.EkoThread;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;

//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;

//In java programs you need this to implement a new surface
public class EkoDriverView extends SurfaceView implements Callback {
	
	private Context mContext;
	private EkoThread thread;
	public static SharedPreferences settings;   //This is basically for load loading data key and value
	public static boolean backbuttonpressed = false;
	SurfaceHolder holder;
	public static Editor editor;   //This is basically for saving data with key and value
	
	public static boolean accAvail = false;
	
	
	public static int deviceWidth, deviceHeight;
	
	final int gameLoading = 0, gameStarting = 1, gameinplay = 2, gameOver = 3, gameScores = 4, gameOptions = 5, gameSelectCar = 6,
              gameController = 7, gameCredits = 9, gameSounds = 10, gameHelp = 11;
	final int easy = 1, meduim = 2, hard = 3, harder = 4;
	final int easyDrive = 1000, mediumDrive = 900, hardDrive = 1100, HarderDrive = 1300, hardestDrive = 1500, ganstarDrive = 1700;
	final int spad = 1, apad = 2;
	
	iSettings isetting;
	
	// private Bitmap mBackgroundImageNear;
	
	//bit map is same thing as texture 2d in C#
	Sprite prestart;
	//Bitmap scaled;

	
	Sprite prestart1,start, pausedbackground;

	Sprite background;

	Sprite[] GameCar;
	
	Car playersCar;
	
	//List<Car> mCar = new ArrayList<Car>();
	
    List<Car> enemyCars = new ArrayList<Car>();   //find out how to creat list in java (google)
    int[] Clanes = new int[7];
    
	 List<SmokenFire> smokenFire = new ArrayList<SmokenFire>();  ////find out how to creat list in java (google)
	
	
	 float screenY = 800;  //This is meant to be the hieght of the phone screen
     int Defaultpad = 1;

     Vector2 backgroundposition1;

     //Vector2 car8position;


     Sprite[] lifes;
     
     Vector2 bCirclePosition, leftPosition, rightPosition, upPosition, downPosition;//sCirclePosition;
     Sprite bigcircle, left, right, up, down; //smallcircle;
     
   //accelerometer  (google) using accelerometer in java
    // Vector3 accelerometerVector;
    // object accelerometerVectorLock = new object();

     //Advert Control  //For advert we will use admob
     //static AdGameComponent adGameComponent;
     //static DrawableAd bannerAd;
     //string adUnitID;

     Sprite scoreimage,speedimage; //Score 
     String score = "0";

     //SpriteFont myfont, highfont;  //Check out how to load fonts in java
     int myscore = 0;
     int scorevalue = 0;
     String CSpeed = " ";
     float Pspeed;

     int gameState = gameLoading;
     boolean Carhit = false, removelife = false;
     int Hitspace = 0;
     int life = 5;
     boolean gamepaused, newhighscore, nonewhighscore;

     //Smoke n Fire
     int SmokeCount = 0, NextSmoke = 0, NextFire = 0;
     Vector2 smokenFirePosition;
     Sprite[] smoking;
     boolean crashed = false, kaboom = false;


     ///<HighScore Implementation>
     Vector2 highScorePosition, highScorePosition2;

     String newhighscorename; int scoredetails;
     ///</End>
     //Last vectors for add implementation
     Vector2 iScore, iDscore, ilife, iSpeed, iDspeed, iPaused;
     boolean loadData = false; 
     ///<Characters Used for all Inputs On eko driver>
     
     //Home Screen Enter
    // Bitmap Enter; Vector2 enterPosition;

     ///Start screen
     Sprite StartText; Vector2 StartTextPosition;
     Sprite HighScoreText; Vector2 HighScoreTextPosition;
     Sprite loadGame; Vector2 loadGameTextPosition;
     Sprite Options ; Vector2 optionsTextPosition;
     Sprite Credits; Vector2 creditTextPosition;


     ///HighScore Screen
     Sprite lblhighscore;

     ///Game Paused
     Sprite lblPaused;
     Sprite resume; Vector2 resumeTextPosition;
     Sprite restart; Vector2 restartTextPosition;
     Sprite SaveExit; Vector2 SaveExitTextPosition;


     ///Game Over
     Sprite lblgameover;
     Sprite retry; Vector2 retryPosition;
     Sprite Main; Vector2 MainPosition;
     ///End
     ///</End Input>

     //Game Options
     Sprite lblgameoptions;
     Sprite Controller; Vector2 ControllerTextPosition;
     Sprite Help ; Vector2 HelpTextPosition;
     Sprite Sound; Vector2 SoundPosition;

     //Controller
     Sprite lblcontroller; Vector2 padPosition;
     Sprite iControllerA, iControllerB;

     //GameSound
     Sprite lblsounds; Vector2 bSoundPosition;
     Sprite btnSoundEnabled, btnSoundDisabled;
     //SoundEffect soundEffect; 
     boolean played = true, soundEnabled = false, mediaEnabled = true;
     //SoundEffect Crash, Slife;
     //Song song;
     private static SoundPool soundPool;
     private static HashMap soundPoolMap; 
     
     public static final int Crash = R.raw.kaboom;
     public static final int Slife = R.raw.lifesound;
     public static final int Racesound = R.raw.racecar;
     public static final int Song = R.raw.poppintough;
     
     MediaPlayer mp;

     
     //GameCredit
     Sprite[] lblcredits;

     //GameTips
     Sprite[] lblhelp;

    // Random value;
     
    public Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    
   // public Rect myrec = new Rect(0,0,deviceWidth,deviceHeight);

     Paint lPadColor, rPadColor , uPadColor , dPadColor;
     
     int[] Lanes;// = new int[7];
     //float carD = iHeight(500); //car distance to remove 
     //float carD2 = iHeight(250); //car distance to make same speed
     
     float lastY = 0;

     boolean ActionDown = false;
     
     Vector2 touchPosition;
     //PhoneApplicationService appService = PhoneApplicationService.Current;
     
     //private EditText input = null;
     private AlertDialog.Builder alert = null;
     //boolean GetName = false;
     //AlertDialog.Builder alert;
     //String mhighScoreName;
     
     //accelerometer
     public static float accValue[];
    
     protected void ContentLoad() throws Exception
    {
     Resources res = mContext.getResources();
    	 
    	 //This is how they load texture2D in java
    	 // mTitleBG = BitmapFactory.decodeResource(mRes, R.drawable.title_hori);
     
    	 
    	 WindowManager mWinMgr = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
	     deviceWidth = mWinMgr.getDefaultDisplay().getWidth();
	     deviceHeight = mWinMgr.getDefaultDisplay().getHeight();
	     
	     screenY = deviceHeight;
	     
	     touchPosition = new Vector2();
	     smokenFirePosition = new Vector2();
	    // value = new Random();
	     
	     loadSound(mContext); //This is used to load sound into memory
	     
	     lPadColor = new Paint(); rPadColor = new Paint(); uPadColor = new Paint(); dPadColor = new Paint();
	     
	     isetting = new iSettings();
	     
	     Lanes = new int[7];
	     Lanes[0] = iWidth(5); 
		 Lanes[1] = iWidth(70) ;
		 Lanes[2] = iWidth(70) * 2;
		 Lanes[3] = iWidth(70) * 3;
		 Lanes[4] = iWidth(70) * 4;
	     Lanes[5] = iWidth(70) * 5;
	     Lanes[6] = iWidth(70) * 6;
	     
    	 
    	 iDscore = new Vector2(8, 12);
         iDspeed = new Vector2(8, 56);

         iScore =new Vector2(112, 36);// new Vector2(112, 4);
         iSpeed = new Vector2(112, 80);//new Vector2(112, 49);

         ilife = new Vector2(346, 12);
         iPaused = new Vector2(148, 22);
  
         //Page that shows before game starts
         //if (gameState == gameLoading)
        // {
         
         highScorePosition = new Vector2();
         highScorePosition2 = new Vector2();
         
           pausedbackground = new Sprite(res,R.drawable.paused,false);
        	
             prestart = new Sprite(res,R.drawable.startpage,false);
             
        	 
             prestart1 = new Sprite(res,R.drawable.loadpage,false);               
            		  //BitmapFactory.decodeResource(res, R.drawable.loadpage);        
         //}

         //When Start Screen is showing
        // if (gameState == gameStarting)
        // {
            
             //start = BitmapFactory.decodeResource(res, R.drawable.startimage);
             start =new Sprite(res, R.drawable.startimage,false);
            
             //StartText = BitmapFactory.decodeResource(res, R.drawable.btnstart);
             StartText = new Sprite(res, R.drawable.btnstart,false);
             
             HighScoreText = new Sprite(res,R.drawable.btnhighscores,false);
             
             Options = new Sprite(res,R.drawable.btnoptions,false);
             
             loadGame =  new Sprite(res,R.drawable.btnload,false);
             
             Credits = new Sprite(res,R.drawable.btncredits,false);

             StartTextPosition = new Vector2(486,338);
             loadGameTextPosition = new Vector2(486,426);
             optionsTextPosition = new Vector2(486,514);
             HighScoreTextPosition = new Vector2(486, 602);
             creditTextPosition = new Vector2(486, 690);
             
             
             played = true;
        // }
         
       //When game is playing
         //if (gameState == gameinplay)
         //{
             // ad Control
             //bannerAd = adGameComponent.CreateAd(adUnitID, new Rectangle(0, 0, GraphicsDevice.Viewport.Bounds.Width, 80));
             //bannerAd.AdRefreshed += new EventHandler(bannerAd_AdRefreshed);
             //bannerAd.ErrorOccurred += new EventHandler<Microsoft.Advertising.AdErrorEventArgs>(bannerAd_ErrorOccurred);
            
             resume =  new Sprite(res,R.drawable.btnresume,false);//Load bitmap
             restart =  new Sprite(res,R.drawable.btnrestart,false);
             
            
             SaveExit = new Sprite(res,R.drawable.btnsaveexit,false);
             lblPaused = new Sprite(res,R.drawable.lblpaused,false);
            
             resumeTextPosition = new Vector2(486,200);
             restartTextPosition = new Vector2(486,300);
             SaveExitTextPosition = new Vector2(486,400);

             
             lifes = new Sprite[8];
             lifes[0] = new Sprite(res,R.drawable.life1,false);
             lifes[1] = new Sprite(res,R.drawable.life2,false);
             lifes[2] = new Sprite(res,R.drawable.life3,false);
             lifes[3] = new Sprite(res,R.drawable.life4,false);
             lifes[4] = new Sprite(res,R.drawable.life5,false);
             lifes[5] = new Sprite(res,R.drawable.life6,false);
             lifes[6] = new Sprite(res,R.drawable.life7,false);
             lifes[7] = new Sprite(res,R.drawable.life8,false);
             

             scoreimage = new Sprite(res,R.drawable.score,false); 
             speedimage = new Sprite(res,R.drawable.speed,false); 
             

             background = new Sprite(res,R.drawable.newracebackground,false);

             GameCar = new Sprite[37];
             GameCar[0] =  new Sprite(res,R.drawable.audia2,false); GameCar[15] = new Sprite(res,R.drawable.accordbrown,false);
             GameCar[1] = new Sprite(res,R.drawable.audia6,false); GameCar[16] = new Sprite(res,R.drawable.accorddarkblue,false);
             GameCar[2] = new Sprite(res,R.drawable.audir8,false); GameCar[17] = new Sprite(res,R.drawable.mazda6,false);
             GameCar[3] = new Sprite(res,R.drawable.audis5,false); GameCar[18] = new Sprite(res,R.drawable.mazdar8,false);
             GameCar[4] = new Sprite(res,R.drawable.bmwx6,false); GameCar[19] = new Sprite(res,R.drawable.seat,false);
             GameCar[5] = new Sprite(res,R.drawable.camaro,false); GameCar[20] = new Sprite(res,R.drawable.tunderred,false);
             GameCar[6] = new Sprite(res,R.drawable.crossfire,false); GameCar[21] = new Sprite(res,R.drawable.accordgreen,false);
             GameCar[7] = new Sprite(res,R.drawable.crvblack,false); GameCar[22] = new Sprite(res,R.drawable.molelue,false);
             GameCar[8] = new Sprite(res,R.drawable.crvgreen,false); GameCar[23] = new Sprite(res,R.drawable.accordpink,false);
             GameCar[9] = new Sprite(res,R.drawable.accordblack,false); GameCar[24] = new Sprite(res,R.drawable.accordred,false);
             GameCar[10] = new Sprite(res,R.drawable.danfo,false); GameCar[25] = new Sprite(res,R.drawable.accordwhite,false);
             GameCar[11] = new Sprite(res,R.drawable.dogde,false);
             GameCar[12] = new Sprite(res,R.drawable.f250,false);
             GameCar[13] = new Sprite(res,R.drawable.accordblue,false);
             GameCar[14] = new Sprite(res,R.drawable.fj,false);
            
             GameCar[26] = new Sprite(res,R.drawable.accordyellow,false);
             GameCar[27] = new Sprite(res,R.drawable.corrolablue,false);
             GameCar[28] = new Sprite(res,R.drawable.corrolalightblue,false);
    
             GameCar[29] = new Sprite(res,R.drawable.corrolapink,false);
             GameCar[30] = new Sprite(res,R.drawable.corrolayellow,false);
             GameCar[31] = new Sprite(res,R.drawable.truckbrown,false); GameCar[32] = new Sprite(res,R.drawable.truckred,false);
             GameCar[33] = new Sprite(res,R.drawable.truckblue,false); GameCar[34] = new Sprite(res,R.drawable.getlife,false);
             GameCar[35] = new Sprite(res,R.drawable.brtred,false);
             GameCar[36] = new Sprite(res,R.drawable.brtblue,false);
             

             smoking = new Sprite[19];

             smoking[0] = new Sprite(res,R.drawable.littlesmoke,false); smoking[1] = new Sprite(res,R.drawable.littlesmoke1,false);
             smoking[2] = new Sprite(res,R.drawable.littlesmoke2,false); smoking[3] = new Sprite(res,R.drawable.littlesmoke3,false);
             smoking[4] = new Sprite(res,R.drawable.littlesmoke4,false); smoking[5] = new Sprite(res,R.drawable.littlesmoke5,false);
             smoking[6] = new Sprite(res,R.drawable.littlesmoke6,false); smoking[7] = new Sprite(res,R.drawable.littlesmoke7,false);
             smoking[8] = new Sprite(res,R.drawable.mediumsmoke8,false); smoking[9] = new Sprite(res,R.drawable.mediumsmoke9,false);
             smoking[10] = new Sprite(res,R.drawable.mediumsmoke10,false); smoking[11] = new Sprite(res,R.drawable.mediumsmoke11,false);
             smoking[12] = new Sprite(res,R.drawable.mediumsmoke12,false); smoking[13] = new Sprite(res,R.drawable.mediumsmoke13,false);
             smoking[14] = new Sprite(res,R.drawable.mediumsmoke14,false); smoking[15] = new Sprite(res,R.drawable.mediumsmoke15,false);
             smoking[16] = new Sprite(res,R.drawable.explosion3,false); smoking[17] = new Sprite(res,R.drawable.explosion2,false);
             smoking[18] = new Sprite(res,R.drawable.explosion1,false);
             
      

             bigcircle = new Sprite(res,R.drawable.bigcircle,false);
             left = new Sprite(res,R.drawable.leftpad,false);
             right = new Sprite(res,R.drawable.rightpad,false);
             up = new Sprite(res,R.drawable.uppad,false);
             down = new  Sprite(res,R.drawable.downpad,false);
            

             backgroundposition1 = new Vector2();
             
             backgroundposition1.dX = 0;
             backgroundposition1.dY = 0 - background.getHeight();

             //car8position = new Vector2();
             //car8position.dX = iWidth(220); car8position.dY = iHeight(810);


             leftPosition = new Vector2(12, 646);
             rightPosition = new Vector2(180, 646);
             upPosition = new Vector2(62, 606);
             downPosition = new Vector2(62, 751);

             bCirclePosition = new Vector2(2, 596);
             //sCirclePosition = new Vector2(bCirclePosition.X + (bigcircle.Bounds.Width / 2 - smallcircle.Bounds.Width / 2),
             //    bCirclePosition.Y + (bigcircle.Bounds.Height / 2 - smallcircle.Bounds.Height / 2));

             played = true;
        // }

         //When game is showing game over
        // if (gameState == gameOver)
        // {
             // settings = new Settings();
             // newhighscore = true;

            

             lblgameover = new  Sprite(res,R.drawable.lblgameover,false);
             retry = new  Sprite(res,R.drawable.goretry,false);
             Main = new  Sprite(res,R.drawable.gomenu,false);

             scoreimage = new  Sprite(res,R.drawable.score,false);

             retryPosition = new Vector2(486, 462);
             MainPosition = new Vector2(486, 562);

             played = true;
        // }

         //when game is showing highscore
        // if (gameState == gameScores)
        // {
            
             lblhighscore = new  Sprite(res,R.drawable.lblhighscores,false);
        // }

        // if (gameState == gameOptions)
        // {
            
             
             lblgameoptions = new  Sprite(res,R.drawable.lbloptions,false);
            
             Controller = new  Sprite(res,R.drawable.btncontroller,false);
             Sound = new  Sprite(res,R.drawable.btnsounds,false);
             Help = new  Sprite(res,R.drawable.btnhelpnabout,false);

             ControllerTextPosition = new Vector2(486,415);
             SoundPosition = new Vector2(486, 515);
             HelpTextPosition = new Vector2(486,615);

             played = true;
        // }

         //if (gameState == gameController)
         //{
             lblcontroller = new  Sprite(res,R.drawable.lblcontroller,false);
            
             iControllerA = new  Sprite(res,R.drawable.btncontrollerenabled,false);
             iControllerB = new  Sprite(res,R.drawable.btncontrollerdisabled,false);
             padPosition = new Vector2(486, 469);

             played = true;
        // }

         //if (gameState == gameSounds)
         //{
             lblsounds = new  Sprite(res,R.drawable.lblsounds,false);

             btnSoundEnabled = new  Sprite(res,R.drawable.btnsoundenabled,false);
             btnSoundDisabled = new  Sprite(res,R.drawable.btnsounddisabled,false);
             bSoundPosition = new Vector2(486,469);

             played = true;
         //}

         //if (gameState == gameCredits)
        // {
              lblcredits  = new Sprite[7];

        
             lblcredits[0] = new  Sprite(res,R.drawable.lblcredits,false);
             lblcredits[1] = new Sprite(res,R.drawable.producer,false);
             lblcredits[2] = new Sprite(res,R.drawable.progammer,false);
             lblcredits[3] = new Sprite(res,R.drawable.physic,false);
             lblcredits[4] = new Sprite(res,R.drawable.maya,false);
             lblcredits[5] = new Sprite(res,R.drawable.chibusoft,false);
             lblcredits[6] = new Sprite(res,R.drawable.contact,false);
        // }

         //if(gameState == gameHelp)
         //{
             lblhelp = new Sprite[4]; 
             
             lblhelp[0] = new  Sprite(res,R.drawable.lblhelpnabout,false);
             lblhelp[1] = new  Sprite(res,R.drawable.helpstory,false);
             lblhelp[2] = new  Sprite(res,R.drawable.version,false);
             lblhelp[3] = new  Sprite(res,R.drawable.support,false);


    }
     
     
     
     void ResetContent()
     {
    	  if (gameState == gameStarting)
         {
    		  StartTextPosition = new Vector2(486,338);
              loadGameTextPosition = new Vector2(486,426);
              optionsTextPosition = new Vector2(486,514);
              HighScoreTextPosition = new Vector2(486, 602);
              creditTextPosition = new Vector2(486, 690);
              
              played = true;
         }
    	  else if (gameState == gameinplay)
          {
    		  resumeTextPosition = new Vector2(486,200);
              restartTextPosition = new Vector2(486,300);
              SaveExitTextPosition = new Vector2(486,400);
              
              backgroundposition1 = new Vector2();
              
              backgroundposition1.dX = 0;
              backgroundposition1.dY = 0 - background.getHeight();

             // car8position = new Vector2();
             // car8position.dX = 220; car8position.dY = 810;


              leftPosition = new Vector2(12, 646);
              rightPosition = new Vector2(180, 646);
              upPosition = new Vector2(62, 606);
              downPosition = new Vector2(62, 751);

              bCirclePosition = new Vector2(2, 596);
              played = true;
          }
    	  else  if (gameState == gameOver)
    	  {
    		  retryPosition = new Vector2(486, 462);
              MainPosition = new Vector2(486, 562);

              played = true;
    	  }
    	  else if (gameState == gameOptions)
    	  {
    		  ControllerTextPosition = new Vector2(486,415);
              SoundPosition = new Vector2(486, 515);
              HelpTextPosition = new Vector2(486,615);

              played = true;
    	  }
    	  else if (gameState == gameController)
    	  {
    		  padPosition = new Vector2(486, 469);

              played = true;
    	  }
    	  else if (gameState == gameSounds)
    	  {
    		  bSoundPosition = new Vector2(486,469);

              played = true;
    	  }
     }
     
    
     
     @SuppressWarnings("unchecked")
	  public static void initSounds(Context context){
         soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
    	// soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
         
         soundPoolMap = new HashMap(3);    

         soundPoolMap.put( Crash, soundPool.load(context, R.raw.kaboom, 1) );
         soundPoolMap.put( Slife, soundPool.load(context, R.raw.lifesound, 2) );
         soundPoolMap.put( Racesound, soundPool.load(context, R.raw.racecar, 3) );
         soundPoolMap.put( Song, soundPool.load(context, R.raw.poppintough, 4) );
        
         }
     
     public void playMedia(Context context,boolean MediaEnabled)
     {
    	 if(mp == null)
    	 {
    	  mp = MediaPlayer.create(context, Song); 
         mp.setVolume(0.2f, 0.2f);
	     mp.setLooping(true);
    	 }
	     if(MediaEnabled == true)
	     {
	     mp.start(); 
	     }
	     else
	     {
	    	 mp.release();
	    	 mp = null;
	    	 
	     }
     }
     
     
     public static void playSound(int soundID,int repeat,boolean stop)
     {
 	   // if(soundPool == null || soundPoolMap == null)
 	    //{
 	    //initSounds(context);
 	   // }
 	    
 	   float volume = 0.8f; //for setting volume
 	   
 	   if(stop == false)
 	   {
 		  soundPool.play((Integer) soundPoolMap.get(soundID), volume, volume, 1, repeat, 1f);
 	   }
 	   else  soundPool.stop(soundID);
 	  
 	  
     }
     
     public static void loadSound(Context context)
     {
 	    if(soundPool == null || soundPoolMap == null)
 	    {
 	    initSounds(context);
 	    }
     }
     
     void PlaySound()
     {
         if (gameState == gameStarting && played == true && soundEnabled == true)
         {
             //soundEffect.Play();
             playSound(Racesound,0,false);
             played = false;
         }

         else if (gameState == gameOptions && played == true && soundEnabled == true)
         {
             //soundEffect.Play();
        	 playSound(Racesound,0,false);
             played = false;
         }

         else if (gameState == gameController && played == true && soundEnabled == true)
         {
             //soundEffect.Play();
        	 playSound(Racesound,0,false);
             played = false;
         }

         else if (gameState == gameSounds && played == true && soundEnabled == true)
         {
             //soundEffect.Play();
        	 playSound(Racesound,0,false);
             played = false;
         }

         else if (gamepaused == true && played == true && soundEnabled == true )
         {
            // soundEffect.Play();
        	 playSound(Racesound,0,false);
             played = false;
         }

         if (gameState != gameLoading && mediaEnabled == true)
         {
             if (soundEnabled == true)
             {
                    // MediaPlayer.Volume = 0.1f;
                    // MediaPlayer.IsRepeating = true;
                    // MediaPlayer.Play(song);   
            	    // mp.start();
            	 playMedia(mContext,mediaEnabled);
                     mediaEnabled = false;
             }
         }

         if (soundEnabled == false && mediaEnabled == false) { 
        	 //mp.stop();
        	 playMedia(mContext,false);
        	 mediaEnabled = true; }
     }
    	
     void PrestartingDraw(Canvas canvas) throws Exception
     {
         Hitspace += 1;
         
         deviceWidth = canvas.getWidth();
		 deviceHeight = canvas.getHeight();

        if (Hitspace > 0 && Hitspace < 80)
         {
        	//canvas.drawBitmap(prestart, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
        	prestart.Draw(canvas);
             
         }

         if (Hitspace >= 80 && Hitspace < 160)
         {
        	// canvas.drawBitmap(prestart1, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
        	 prestart1.Draw(canvas);
         }

        
        if (Hitspace > 200)
         {
            Hitspace = 0;
             gameState = gameStarting;
           // LoadContent();
          }
         //if (MediaPlayer.GameHasControl == false)
         //{
         //    soundEnabled = false;
         //}
        
        soundEnabled = true;
     }
     
    
     
     public class Sprite
     {
    	private Bitmap Texture2D;
    	private double DefaultDeviceWidth = 480, DefaultDeviceheight =800;
    	int Texture2DWidth, Texture2DHeight;
    	
    	
    	private Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    	//private Paint mpaint = new Paint();	
    	
    	
     public Sprite(Resources res,int texture2D) throws Exception
     {
    	 Texture2D = BitmapFactory.decodeResource(res, texture2D); 
    	 
    	 Texture2DWidth = Texture2D.getWidth();
    	 Texture2DHeight = Texture2D.getHeight();
     }
     
     
	public Sprite(Resources res,int texture2D,boolean filter) throws Exception
     {
    	 Texture2D = BitmapFactory.decodeResource(res, texture2D); 
    	 Texture2DWidth = Texture2D.getWidth();
    	 Texture2DHeight = Texture2D.getHeight();
    	 
    	// deviceWidth = canvas.getWidth();
		// deviceHeight = canvas.getHeight();

    	 
    	 //200(target screen width)/100(default screen width) = 2
    	 double width = deviceWidth / DefaultDeviceWidth; 
    	 width = width * Texture2DWidth;
    	 
    	 
    	 double height = deviceHeight / DefaultDeviceheight;
    	 height = height * Texture2DHeight;

    	 Texture2D = Bitmap.createScaledBitmap(Texture2D, (int)width, (int)height, true);
    	 
    	//Texture2D = Bitmap.createScaledBitmap(Texture2D,  deviceWidth, deviceHeight, filter);
    	
    	
    	 
    	 Texture2DWidth = Texture2D.getWidth();
    	 Texture2DHeight = Texture2D.getHeight();
     }
     
     public int getWidth()
     {
    	 return Texture2DWidth;
     }
     
     public int getHeight()
     {
    	 return Texture2DHeight;
     }
     
     public void Draw(Canvas canvas,float x,float y) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, x, y, paint);
     }
     
     public void Draw(Canvas canvas,Vector2 vector2) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, vector2.dX, vector2.dY, paint);
     }
     
     public void Draw(Canvas canvas,Vector2 vector2,Paint paint) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, vector2.dX, vector2.dY, paint);
     }
     
     public void Draw(Canvas canvas,float x,float y,Paint paint) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, x, y, paint);
    	 //paint.setColor(android.R.color.white);    
     }
     
     public void Draw(Canvas canvas,float x,float y,boolean fullscreen) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
     }
     
     public void Draw(Canvas canvas) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
     }
     
     public void Draw(Canvas canvas,Paint paint) throws Exception
     {
    	 canvas.drawBitmap(Texture2D, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
     }

     }
   

     public int iHeight(int y)
     {
    	 // Use 960 * 60 = 57600 / 480 = 120
    	 
       	 int olddeviceheight = 800;
    	 
    	 y = (deviceHeight * y ) / olddeviceheight;
    	    
    	 return y; 	 
     }
     
     public float iHeight(float y)
     {
    	 // Use 960 * 60 = 57600 / 480 = 120
    	 
       	 float olddeviceheight = 800;
    	 
    	 y = (deviceHeight * y ) / olddeviceheight;
    	    
    	 return y; 	 
     }
     
     
     public int iWidth(int x)
     {
    	// Use 960 * 60 = 57600 / 480 = 120
    	
    	 int olddevicewidth = 480;
    	
    	 x = (deviceWidth * x) / olddevicewidth;
    			 
    	return x;
     }
     
     public float iWidth(float x)
     {
    	// Use 960 * 60 = 57600 / 480 = 120
    	
    	 float olddevicewidth = 480;
    	
    	 x = (deviceWidth * x) / olddevicewidth;
    			 
    	return x;
     }
     
     
     public EkoDriverView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 		// TODO Auto-generated constructor stub
 		//Note the attribute here is to help save thing in xml
 		holder = getHolder();
 		holder.addCallback(this);
 		mContext = context;
 		setFocusable(true);
 	}
     
     @Override
     public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
 		// TODO Auto-generated method stub

 	}
     
    
     
 	@Override
 	public void surfaceCreated(SurfaceHolder holder) {
 		// TODO Auto-generated method stub
 		
 
		try{
			ContentLoad();
			editor = settings.edit();
			if(settings.getInt("playercarType", 0) == 0)
			{
			isetting = new iSettings();
			isetting.createSettings();
			}
			else
			{
				LoadGame();
	 			gameState = gameLoading;
			}
			
			
		}
		catch(Exception ex)
		{
			ex.getMessage();
				System.exit(0); 
		}
 		
 		thread = new EkoThread(holder);
 		thread.start();
 		
 	}

 	@Override
 	public void surfaceDestroyed(SurfaceHolder holder) {
 		// TODO Auto-generated method stub
 		
 		try{
			editor = settings.edit();
			//SaveGameData();
			editor.commit();
		}
		catch(Exception ex){}
 		thread.destroy();
 	}
     
 	
 	
     //Java is crazy you have create a vector2  class before you can use it kai
     public  class Vector2{
 		private int dX = 0, dY = 0;
 		
 		private int defaultDeviceWidth = 480; //This should be the original width worked with
 		private int defaultDeviceHeight = 800; //This should be the original height worked
 		
 		public Vector2()
 		{
 			dX = 0; dY = 0;
 		}
 		
 		public Vector2(int x, int y){
 			
 			y = (deviceHeight * y) / defaultDeviceHeight; //this get y position for new screen
 			x = (deviceWidth * x) / defaultDeviceWidth;  //this gets the x position for new screen
 			dX = x; dY = y; 
 			
 			
 		}
 		public Vector2(float x, float y){
 			//y = (deviceHeight * y) / defaultDeviceHeight; //this get y position for new screen
 			//x = (deviceWidth * x) / defaultDeviceWidth;  //this gets the x position for new screen
 			dX = (int)x; dY = (int)y; 
 		}
 		public Vector2(double x, double y){
 			
 			y = (deviceHeight * y) / defaultDeviceHeight; //this get y position for new screen
 			x = (deviceWidth * x) / defaultDeviceWidth;  //this gets the x position for new screen
 			dX = (int)x; dY = (int)y; 
 			
 		}
 
 		
 		public int getX(){return dX;}
 		public int getY(){return dY;}
 		public void setX(int x){dX =  (int)x;}
 		public void setY(int y){dY =  (int)y;}
 		public void setX(float x){dX = (int)x;}
 		public void setY(float y){dY = (int)y;}
 		public void setX(double x){dX = (int)x;}
 		public void setY(double y){dY = (int)y;}
 	}
     
     
     public static class iSettings
     {
    	 public static boolean Paused, Media ,SoundEnabled ,loadData; 
    	 
    	 public static int playercarType, scoreValue , Defaultpad ,lifes ,gamestate;
    	 
    	 public static float playercarSpeed ,playercarX ,playercarY ,screenY;
    	 
    	 //public static String[] highscore, highscoreName ;
    	 
    	 public static String[] highscore = new String[10];
    	 public static String[] highscoreName = new String[10];
    	 
    	 public String Smoke, Enemycar;
    	 
    	 public static String[] iClanes = new String[7];
    	 
 
    	 public void createSettings()
    	 {
    		 Paused = false;
    		 editor.putBoolean("Paused", Paused);
    		 
    		 
             Enemycar = " ";
             editor.putString("Enemycar", Enemycar);
             
             iClanes[0] = "0";
             editor.putString("iClanes0", "0");
             
             iClanes[1] = "0";
             editor.putString("iClanes1", "0");
             
             iClanes[2] = "0";
             editor.putString("iClanes2", "0");
             
             iClanes[3] = "0";
             editor.putString("iClanes3", "0");
             
             iClanes[4] = "0";
             editor.putString("iClanes4", "0");
             
             iClanes[5] = "0";
             editor.putString("iClanes5", "0");
             
             iClanes[6] = "0";
             editor.putString("iClanes6", "0");
             
            
             Smoke = " ";
             editor.putString("Smoke", Smoke);

             Media = true;
             editor.putBoolean("Media", Media);

             screenY = deviceHeight ;
             editor.putFloat("screenY", screenY);

             scoreValue = 0;
             editor.putInt("scoreValue", scoreValue);

             SoundEnabled = true;
             editor.putBoolean("SoundEnabled", SoundEnabled);

             lifes = 5;
             editor.putInt("lifes", lifes);

             gamestate = 0;
             editor.putInt("gamestate", gamestate);

             Defaultpad = 1;
             editor.putInt("Defaultpad", Defaultpad);

             highscore = new String[10];
             highscoreName = new String[10];

             highscoreName[0] = "Winston"; highscore[0] = "21500";
             editor.putString("highscoreName0", highscoreName[0]); editor.putString("highscore0", highscore[0]);

             highscoreName[1] = "Garvey"; highscore[1] = "17500";
             editor.putString("highscoreName1", highscoreName[1]); editor.putString("highscore1", highscore[1]);

             highscoreName[2] = "Mr Lee"; highscore[2] = "14100";
             editor.putString("highscoreName2", highscoreName[2]); editor.putString("highscore2", highscore[2]);

             highscoreName[3] = "Ebele"; highscore[3] = "12200";
             editor.putString("highscoreName3", highscoreName[3]); editor.putString("highscore3", highscore[3]);

             highscoreName[4] = "Yinkus"; highscore[4] = "8900";
             editor.putString("highscoreName4", highscoreName[4]); editor.putString("highscore4", highscore[4]);

             highscoreName[5] = "Zenachi"; highscore[5] = "7100";
             editor.putString("highscoreName5", highscoreName[5]); editor.putString("highscore5", highscore[5]);

             highscoreName[6] = "chinwe"; highscore[6] = "6950";
             editor.putString("highscoreName6", highscoreName[6]); editor.putString("highscore6", highscore[6]);

             highscoreName[7] = "Onochie"; highscore[7] = "5645";
             editor.putString("highscoreName7", highscoreName[7]); editor.putString("highscore7", highscore[7]);

             highscoreName[8] = "Chumzy"; highscore[8] = "4995";
             editor.putString("highscoreName8", highscoreName[8]); editor.putString("highscore8", highscore[8]);

             highscoreName[9] = "Nicole"; highscore[9] = "2200";
             editor.putString("highscoreName9", highscoreName[9]); editor.putString("highscore9", highscore[9]);
             
             editor.commit();
             
    	 }
   
    	 
    	  
         
  

     }

     	 void LoadGame()
     {    

             int type = settings.getInt("playercarType", 0); //settings.playercarType;
     
             float x = settings.getFloat("playercarX", 0);//settings.playercarX;
             float y = settings.getFloat("playercarY", 0);//settings.playercarY;
             float speed = settings.getFloat("playercarSpeed", 0);//settings.playercarSpeed;
             boolean crashed = false;
             playersCar = new Car(type, speed, x, y, crashed);

            // CarSpeed(); //Attention needed

             screenY = settings.getFloat("screenY", 0);//settings.screenY;

            // Defaultpad = settings.Defaultpad;

             scorevalue = settings.getInt("scoreValue", 0);//settings.scoreValue;

             score = String.valueOf(scorevalue) ;

             life = settings.getInt("lifes", 5);//settings.lifes;
             
             loadData = settings.getBoolean("loadData", true);

             //sCirclePosition.X = settings.SmallCircleX;

             //sCirclePosition.Y = settings.SmallCircleY;

             gamepaused = true;

             //enemyCars = new List<Car>[7];
             enemyCars = new ArrayList<Car>();
             
             //smokenFire = new ist<SmokenFire>();
             smokenFire = new ArrayList<SmokenFire>();
             Clanes = new int[7];
             
             Clanes[0] = Integer.parseInt(settings.getString("iClanes0", "0"));
             Clanes[1] = Integer.parseInt(settings.getString("iClanes1", "0"));
             Clanes[2] = Integer.parseInt(settings.getString("iClanes2", "0"));
             Clanes[3] = Integer.parseInt(settings.getString("iClanes3", "0"));
             Clanes[4] = Integer.parseInt(settings.getString("iClanes4", "0"));
             Clanes[5] = Integer.parseInt(settings.getString("iClanes5", "0"));
             Clanes[6] = Integer.parseInt(settings.getString("iClanes6", "0"));
            

             if (settings.getString("Enemycar", " ").equals(" ") == false)
             {
                 String[] Enemies = settings.getString("Enemycar", " ").split("&");
               
                 for (int j = 0; j < Enemies.length; ++j)
                 {
                   String[] iEnemies = Enemies[j].split(",");

                   int Type = Integer.parseInt(iEnemies[0]);
                   float Speed = Float.valueOf(iEnemies[1]);
                   float X = Float.valueOf(iEnemies[2]);
                   float Y = Float.valueOf(iEnemies[3]);
                   boolean icrashed = Boolean.getBoolean(iEnemies[4]);

                   enemyCars.add(new Car(Type, Speed, X, Y,icrashed));
                 }

             }

             if (settings.getString("Smoke", " ").equals(" ") == false)
             {
            	 String[] smoking = settings.getString("Smoke", " ").split("&");
                 for (int j = 0; j < smoking.length; ++j)
                 {
                   String[] smokeLane = smoking[j].split(",");

                   int smoketype = Integer.parseInt(smokeLane[0]);
                   float smokespeed = Float.valueOf(smokeLane[1]);
                   float smokex = Float.valueOf(smokeLane[2]);
                   float smokey = Float.valueOf(smokeLane[3]); 

                     smokenFire.add(new SmokenFire(smoketype, smokespeed, smokex, smokey));
                 }

             }
             
             gameState = gameinplay;
     }
	
     
     public void SaveGame()
     {
    	
    	 if (gameState == gameinplay)
         {
                       
             if (enemyCars.size() != 0)
             {
            	 editor.putString("iClanes0", String.valueOf(Clanes[0]));
            	 editor.putString("iClanes1", String.valueOf(Clanes[1]));
            	 editor.putString("iClanes2", String.valueOf(Clanes[2]));
            	 editor.putString("iClanes3", String.valueOf(Clanes[3]));
            	 editor.putString("iClanes4", String.valueOf(Clanes[4]));
            	 editor.putString("iClanes5", String.valueOf(Clanes[5]));
            	 editor.putString("iClanes6", String.valueOf(Clanes[6]));
                
            	 isetting.Enemycar = " ";
       
            	
                 for (int j = 0; j < enemyCars.size(); j++)
                 {
                     //if (settings.Smoke == " ")
                    	if(isetting.Enemycar == " ")
                     {
                    		isetting.Enemycar = String.valueOf(enemyCars.get(j).type);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).speed);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).x);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).y);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).crashed);
                     }
                     //else if (settings.Smoke.EndsWith("&") == true)
                    
                    	else if (isetting.Enemycar.endsWith("&") )
                     {
                         //settings.Smoke += smokenFire[j].type.ToString();
                    		isetting.Enemycar += String.valueOf(enemyCars.get(j).type);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).speed);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).x);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).y);
                    		isetting.Enemycar += "," + String.valueOf(enemyCars.get(j).crashed);
                     }
                     if (j < enemyCars.size() - 1) isetting.Enemycar += "&";

                 }
                 
                 editor.putString("Enemycar", isetting.Enemycar);
             }

             if (smokenFire.size() != 0)
             {
                // settings.Smoke = " ";
            	 isetting.Smoke = " ";
            	// editor.putString("Smoke", " ");
            	
                 for (int j = 0; j < smokenFire.size(); j++)
                 {
                     //if (settings.Smoke == " ")
                    	if( isetting.Smoke == " ")
                     {
                        // settings.Smoke = smokenFire[j].type.ToString();
                    		isetting.Smoke = String.valueOf(smokenFire.get(j).type);
                        // settings.Smoke += "," + smokenFire[j].speed.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).speed);
                        // settings.Smoke += "," + smokenFire[j].x.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).x);
                        // settings.Smoke += "," + smokenFire[j].y.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).y);
                     }
                     //else if (settings.Smoke.EndsWith("&") == true)
                    
                    	else if (isetting.Smoke.endsWith("&") )
                     {
                         //settings.Smoke += smokenFire[j].type.ToString();
                    		isetting.Smoke += String.valueOf(smokenFire.get(j).type);
                         //settings.Smoke += "," + smokenFire[j].speed.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).speed);
                        //settings.Smoke += "," + smokenFire[j].x.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).x);
                        // settings.Smoke += "," + smokenFire[j].y.ToString();
                    		isetting.Smoke += "," + String.valueOf(smokenFire.get(j).y);
                     }
                     if (j < smokenFire.size() - 1) isetting.Smoke += "&";
                 }
                 editor.putString("Smoke", isetting.Smoke);
             }

         }
             //settings.playercarSpeed = playersCar.speed;
             editor.putFloat("playercarSpeed", playersCar.speed);
            // settings.playercarType = playersCar.type;
             editor.putInt("playercarType", playersCar.type);
             // settings.playercarX = playersCar.x;
             editor.putFloat("playercarX", playersCar.x);
            // settings.playercarY = playersCar.y;
             editor.putFloat("playercarY", playersCar.y);

             //settings.screenY = screenY;
             editor.putFloat("screenY", screenY);

            // settings.scoreValue = scorevalue;
             editor.putInt("scoreValue", scorevalue);

             //settings.lifes = life;
             editor.putInt("lifes", life);

            // settings.Defaultpad = Defaultpad;
             editor.putInt("Defaultpad", Defaultpad);

           //  settings.SoundEnabled = soundEnabled;
             editor.putBoolean("SoundEnabled", soundEnabled);

            // settings.loadData = true;
             if (gameState == gameinplay)  editor.putBoolean("loadData", true);
             //else  editor.putBoolean("loadData", false);

             editor.commit();
            // settings.Save();
             
     }

   
     void RestartGame()
     {

         screenY = deviceHeight;
         playersCar = new Car(Car.Camaro, 550, iWidth(220), 0 ,false);
         enemyCars = new ArrayList<Car>();
         Clanes = new int[7];
         screenY = deviceHeight;
       
         smokenFire = new ArrayList<SmokenFire>();
         
        //value = new Random();
         Hitspace = 0;
         Carhit = false;
         newhighscore = true;
         scorevalue = 0;
         nonewhighscore = false;
         gamepaused = false;
     }
     
     @Override
		public boolean  onTouchEvent(MotionEvent event)
	     {
	    	 touchPosition = new Vector2(event.getX(),event.getY()); //get coordinated
	    	 int action = event.getAction(); //Get types of gesture
	    	 
	    	 if (action == event.ACTION_DOWN)
	    	 {
	    		 GameTouch(touchPosition);
	    		 
	    		 if( gameState == gameinplay)
	     		 {
	    		  ActionDown = true;
	     		 }
	         }
	    	  if(action == event.ACTION_UP )
	    	  {
	    		  if( gameState == gameinplay)
		     		 {
	    			  ActionDown = false;
	    			  lPadColor.setColor(Color.WHITE);
		     		 }
	    	  }
	    	
	    	
	    	 return true;
	     }
    		 
    //Gets all touch locations on the fone
    void GameTouch(Vector2 touchPosition)
    {
    	 //When game in start screen)
        if (gameState == gameStarting)
        {
            //When start is pressed
            if (touchPosition.dX >= StartTextPosition.dX &&
             touchPosition.dX < StartTextPosition.dX + StartText.getWidth() &&
             touchPosition.dY >= StartTextPosition.dY &&
             touchPosition.dY < StartTextPosition.dY + StartText.getHeight())
            {
                gameState = gameinplay;
                life = 5;
                scorevalue = 0;
                score = "0";
                CSpeed = "";
                Hitspace = 0;
                Carhit = false;
                newhighscore = true;
                nonewhighscore = false;
                RestartGame();
               /// LoadContent();
            }

            //when Highscore button is click on the screen
            if (touchPosition.dX >= HighScoreTextPosition.dX &&
            touchPosition.dX < HighScoreTextPosition.dX + HighScoreText.getWidth() &&
            touchPosition.dY >= HighScoreTextPosition.dY &&
            touchPosition.dY < HighScoreTextPosition.dY + HighScoreText.getHeight())
            {
                if (loadData == true)
                {
                    gameState = gameScores;
                   /// LoadContent();
                }
                else
                {
                    gameState = gameCredits;
                   /// LoadContent();
                }
            }

            //when Options button is click on the screen
            if (touchPosition.dX >= optionsTextPosition.dX &&
            touchPosition.dX < optionsTextPosition.dX + Options.getWidth() &&
            touchPosition.dY >= optionsTextPosition.dY &&
            touchPosition.dY < optionsTextPosition.dY + Options.getHeight())
            {
                if (loadData == true)
                {
                    gameState = gameOptions;
                   /// LoadContent();
                }
                else
                {
                    gameState = gameScores;  //Remeber to un comment and remove game options
                	//gameState = gameOptions;
                   /// LoadContent();
                }
            }

            ////Code runs when load button is pressed on the screen
            if (touchPosition.dX >= loadGameTextPosition.dX &&
             touchPosition.dX < loadGameTextPosition.dX + loadGame.getWidth() &&
             touchPosition.dY >= loadGameTextPosition.dY &&
             touchPosition.dY < loadGameTextPosition.dY + loadGame.getHeight())
            {
                //gameState = gameStarting;
                if (loadData == true)
                {
                    LoadGame();
                }
                else
                {
                    gameState = gameOptions;
                   /// LoadContent();
                }
                
                //gamepaused = false;
           
            }

            //when Highscore button is click on the screen
            if (touchPosition.dX >= creditTextPosition.dX &&
            touchPosition.dX < creditTextPosition.dX + Credits.getWidth() &&
            touchPosition.dY >= creditTextPosition.dY &&
            touchPosition.dY < creditTextPosition.dY + Credits.getHeight())
            {
                if (loadData == true)
                {
                    gameState = gameCredits;
                   /// LoadContent();
                }
                else
                {
                    gameState = gameScores;
                   // gameState = gameCredits; //pls remove later
                   /// LoadContent();
                }
            }
            return;
        }


        //When Game screen in GameOver
        if (gameState == gameOver)
        {
            //When Retry is clicked
            if (touchPosition.dX >= retryPosition.dX &&
              touchPosition.dX < retryPosition.dX + retry.getWidth() &&
              touchPosition.dY >= retryPosition.dY &&
              touchPosition.dY < retryPosition.dY + retry.getHeight())
            {
                gameState = gameinplay;
                life = 5;
                scorevalue = 0;
                score = "0";
                CSpeed = "";
                Hitspace = 0;
                Carhit = false;
                newhighscore = true;
                nonewhighscore = false;
                RestartGame();
               /// LoadContent();
                ResetContent();
            }

            //When Main menu Button is clicked 
            if (touchPosition.dX >= MainPosition.dX &&
          touchPosition.dX < MainPosition.dX + Main.getWidth() &&
          touchPosition.dY >= MainPosition.dY &&
          touchPosition.dY < MainPosition.dY + Main.getHeight())
            {
                gameState = gameStarting;
                scorevalue = 0;
                score = "0";
                CSpeed = "";
                life = 5;
                Carhit = false;
                Hitspace = 0;
                gamepaused = false;
                RestartGame();
                ///LoadContent();
                ResetContent();
            }
            return;
        }


        //When Game is in Paused Screen
        if (gameState == gameinplay && gamepaused == true)
        {
            //when paused code below on touch or resume is pressed
            if (touchPosition.dX >= resumeTextPosition.dX &&
           touchPosition.dX < resumeTextPosition.dX + resume.getWidth() &&
           touchPosition.dY >= resumeTextPosition.dY &&
           touchPosition.dY < resumeTextPosition.dY + resume.getHeight())
            {
                gamepaused = false;
            }

            //When Retart Button is clicked 
            if (touchPosition.dX >= restartTextPosition.dX &&
          touchPosition.dX < restartTextPosition.dX + restart.getWidth() &&
          touchPosition.dY >= restartTextPosition.dY &&
          touchPosition.dY < restartTextPosition.dY + restart.getHeight())
            {
                gameState = gameinplay;
                scorevalue = 0;
                score = "0";
                CSpeed = "";
                life = 5;
                Carhit = false;
                Hitspace = 0;
                gamepaused = false;
                newhighscore = true;
                nonewhighscore = false;
                RestartGame();
               /// LoadContent();
            }

            //When Save n Exit Button is clicked 
            if (touchPosition.dX >= SaveExitTextPosition.dX &&
          touchPosition.dX < SaveExitTextPosition.dX + SaveExit.getWidth() &&
          touchPosition.dY >= SaveExitTextPosition.dY &&
          touchPosition.dY < SaveExitTextPosition.dY + SaveExit.getHeight())
            {
                
                Hitspace = 0;
                newhighscore = true;
                nonewhighscore = false;
                SaveGame();
                gameState = gameStarting;
                gamepaused = false;
               /// LoadContent();
                ResetContent();
                loadData = true;
            }
        }


        //When Game is in Options screen
        if (gameState == gameOptions)
        {
            //Controller button
            if (touchPosition.dX >= ControllerTextPosition.dX &&
         touchPosition.dX < ControllerTextPosition.dX + Controller.getWidth() &&
         touchPosition.dY >= ControllerTextPosition.dY &&
         touchPosition.dY < ControllerTextPosition.dY + Controller.getHeight())
            {
               // gameState = gameController;
             //new code under
            	 gameState = gameSounds;
            }

            //Sounds button
            if (touchPosition.dX >= SoundPosition.dX &&
         touchPosition.dX < SoundPosition.dX + Sound.getWidth() &&
         touchPosition.dY >= SoundPosition.dY &&
         touchPosition.dY < SoundPosition.dY + Sound.getHeight())
            {
               // gameState = gameSounds;
              ///  LoadContent();
            	//new code
            	 gameState = gameHelp;
            }

            //Credits button
       /*     if (touchPosition.dX >= HelpTextPosition.dX &&
         touchPosition.dX < HelpTextPosition.dX + Help.getWidth() &&
         touchPosition.dY >= HelpTextPosition.dY &&
         touchPosition.dY < HelpTextPosition.dY + Help.getHeight())
            {
                gameState = gameHelp;
              ///  LoadContent();
            }*/

        }


        //Game is in Controller State
        if (gameState == gameController)
        {

            //Onscreen Controller pad
            if (touchPosition.dX >= padPosition.dX &&
        touchPosition.dX < padPosition.dX + iControllerA.getWidth() &&
        touchPosition.dY >= padPosition.dY &&
        touchPosition.dY < padPosition.dY + iControllerA.getHeight())
            {
            	if( accAvail == true)
            	{
                if (Defaultpad == spad)
                {
                    Defaultpad = apad;
                        return;
                }
                if (Defaultpad == apad)
                {
                    Defaultpad = spad;
                    return;
                }
            	}
            	else
            	{
            		Defaultpad = spad;
            	}
            }
        }

        if (gameState == gameSounds)
        {
            if (touchPosition.dX >= bSoundPosition.dX &&
        touchPosition.dX < bSoundPosition.dX + btnSoundEnabled.getWidth() &&
        touchPosition.dY >= bSoundPosition.dY &&
        touchPosition.dY < bSoundPosition.dY + btnSoundEnabled.getHeight())
            {
                if (soundEnabled == true)
                {
                    soundEnabled = false;
                    return;
                }
                if (soundEnabled == false)
                {
                    soundEnabled = true;
                    return;
                }
          }
      }
      
    }
     
    void LeftRight(Vector2 touchPosition) //ref float playerCarX
    {
    	
        // = Color.White; lPadColor = Color.White; uPadColor = Color.White; dPadColor = Color.White;

        
                if (touchPosition.dX >= leftPosition.dX &&
                 touchPosition.dX < leftPosition.dX + left.getWidth() &&
                 touchPosition.dY >= leftPosition.dY &&
                 touchPosition.dY < leftPosition.dY + left.getHeight() && (playersCar.x - iWidth(14)) > 0)
                {
                   // xstart -= 5;
                	
                    //if (xstart + iWidth(48) < deviceWidth && (xstart - 8) > 0) playerCarX = xstart;
                	if (playersCar.x + iWidth(48) < deviceWidth && (playersCar.x - iWidth(8)) > 0) playersCar.x -= 5;
                	
                    lPadColor.setAlpha(175);
                    lPadColor.setColor(Color.GREEN);
                }

                if (touchPosition.dX >= rightPosition.dX &&
                touchPosition.dX < rightPosition.dX + right.getWidth() &&
                touchPosition.dY >= rightPosition.dY &&
                touchPosition.dY < rightPosition.dY + right.getHeight() &&
                (playersCar.x + iWidth(52)) < deviceWidth)
                {
                    //xstart += 5;
                	
                    //if (xstart + iWidth(48) < deviceWidth && (xstart - 8) > 0) playerCarX = xstart;
                	if (playersCar.x + iWidth(48) < deviceWidth && (playersCar.x - iWidth(8)) > 0) playersCar.x += 5;
                    //rPadColor.setColor(Color.GREEN);
                }

                if (touchPosition.dX >= upPosition.dX &&
                touchPosition.dX < upPosition.dX + up.getWidth() &&
                touchPosition.dY >= upPosition.dY &&
                touchPosition.dY < upPosition.dY + up.getHeight() )
                {
                   if(playersCar.speed < 1200)playersCar.speed +=2;
                   //uPadColor.setColor(Color.GREEN);
                }

                if (touchPosition.dX >= downPosition.dX &&
                touchPosition.dX < downPosition.dX + down.getWidth() &&
                touchPosition.dY >= downPosition.dY &&
                touchPosition.dY < downPosition.dY + down.getHeight())
                {
                    if (playersCar.speed > 600) playersCar.speed -= 5;
                    //dPadColor.setColor(Color.GREEN);
                }
        
            }
    
    //these creates the surface or takes over the surface of the phone
    private SurfaceHolder mSurfaceHolder;
        
     //This is the thread that calls update and draw method in your games
 	public class EkoThread extends Thread{
 		
		public EkoThread(SurfaceHolder surfaceHolder){
 			mSurfaceHolder = surfaceHolder;
 		} 
 		
 	// desired fps
 	//private final static int 	MAX_FPS = 50;	//50
 	// maximum number of frames to be skipped
 	//private final static int	MAX_FRAME_SKIPS = 5;	
 	// the frame period
 	//private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	
   // public double gameTime1 = 0;
 	
		 //new 
		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 30;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;  

 	@Override
 	public void run() {
 		Canvas c;
 		//Log.d(TAG, "Starting game loop");

 		//long beginTime;		// the time when the cycle begun
 		//long timeDiff;		// the time it took for the cycle to execute
 		//int sleepTime;		// ms to sleep (<0 if we're behind)
 		//int framesSkipped;	// number of frames being skipped 
 		
 		//new
 	     // work out how long its been since the last update, this
 	      // will be used to calculate how far the entities should
 	      // move this loop
 	      long now = System.nanoTime();
 	      long updateLength = now - lastLoopTime;
 	      lastLoopTime = now;
 	      double delta = updateLength / ((double)OPTIMAL_TIME);

 	  

 		//sleepTime = 0;
 		
 		while (true) {
 			c = null;
 			// try locking the canvas for exclusive pixel editing
 			// in the surface
 			try {
 				c = mSurfaceHolder.lockCanvas();
 				synchronized (mSurfaceHolder) {
 					//beginTime = System.currentTimeMillis();
 					//framesSkipped = 0;	// resetting the frames skipped
 					
 					
 							
 					// update game state 
 					OnUpdate(delta);
 					
 					//if( ActionDown == true)
		     		// {
		     		//	 LeftRight(touchPosition);
		     		// }
 					// render state to the screen
 					// draws the canvas on the panel
 					doDraw(c);	
 					
 					//new
 					 // we want each frame to take 10 milliseconds, to do this
 				      // we've recorded when we started the frame. We add 10 milliseconds
 				      // to this and then factor in the current time to give 
 				      // us our final value to wait for
 				      // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
 				      try{
 				    	  Thread.sleep((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);
 				    	  }
 				    	  catch (Exception e) {
							// TODO: handle exception
						}
 					
 					// calculate how long did the cycle take
 					//timeDiff = System.currentTimeMillis() - beginTime;
 					// calculate sleep time
 					//sleepTime = (int)(FRAME_PERIOD - timeDiff);
 					
// 					if (sleepTime > 0) {
// 						// if sleepTime > 0 we're OK
// 						try {
// 							// send the thread to sleep for a short period
// 							// very useful for battery saving
// 							Thread.sleep(sleepTime);	
// 						} catch (InterruptedException e) {}
// 					}
// 					
// 					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
// 						// we need to catch up
// 						// update without rendering
// 						OnUpdate(); 
// 						// add frame period to check if in next frame
// 						sleepTime += FRAME_PERIOD;	
// 						framesSkipped++;
//					}
 				}
 			} 
 				finally {
 				// in case of an exception the surface is not left in 
 				// an inconsistent state
 				if (c != null) {
 					mSurfaceHolder.unlockCanvasAndPost(c);
 				}
 			}	// end finally
 		}
 	}

 		//long mLastTime; double gameTime;
 		
 		
 		private void OnUpdate(double gameTime){
 			//long now = System.currentTimeMillis();
 			//if (mLastTime > now) return;
 			//if (mLastTime ==0)mLastTime = now;
 			//gameTime = (now-mLastTime)/3000.0;//3333
 			//mLastTime = now;
 			
 			
 			
 			try{
 			     if(backbuttonpressed == true)
 			     {
 			    	if (gameState == gameinplay)
                    {
                        if (gamepaused == true)
                        {
                            gamepaused = false;
                            resumeTextPosition = new Vector2(486, 200);
                            restartTextPosition = new Vector2(486, 300);
                            SaveExitTextPosition = new Vector2(486, 400);
                            played = true;
                            backbuttonpressed = false;
                            return;
                        }
                        if (gamepaused == false) {gamepaused = true; backbuttonpressed = false; ResetContent(); return; }
                        //this.Exit();
                    }
                    if (gameState == gameStarting)
                    {
                        // gameState = gameLoading; //commented out
                        gamepaused = false;
                        gameState = gameStarting;
                        gameState = gameLoading;
                        backbuttonpressed = false;
                        System.exit(0);  //This Code exits the system
                    }
                    if (gameState == gameOver)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                        //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                    if (gameState == gameScores)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                    if (gameState == gameOptions)
                    {
                        gameState = gameStarting;
                        scorevalue = 0;
                        life = 5;
                        Carhit = false;
                        Hitspace = 0;
                        gamepaused = false;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                     if (gameState == gameController)
                    {
                        gameState = gameOptions;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                     if (gameState == gameSounds)
                    {
                        gameState = gameOptions;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                    if (gameState == gameCredits)
                    {
                        gameState = gameStarting;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                    if (gameState == gameHelp)
                    {
                        gameState = gameOptions;
                      //LoadContent(); //replaced with resetContent
                        ResetContent();
                        backbuttonpressed = false;
                        return;
                    }
                }
 			     
 			    PlaySound();// Get how to play songs

               if (gameState == gameinplay && gamepaused == false)
                {
                    updatebackground( gameTime);

                    //Updating players car
                    updateCar(gameTime);

                    //Updating enemy car 
                    if (scorevalue <= 10000) updateEnemyCars(gameTime, easy, easyDrive);
                    else if (scorevalue > 10000 && scorevalue <= 25000) updateEnemyCars(gameTime, meduim, mediumDrive);
                    else if (scorevalue > 25000 && scorevalue <= 45000) updateEnemyCars(gameTime, hard, hardDrive);
                    else if (scorevalue > 450000) updateEnemyCars(gameTime, harder, HarderDrive);
                    

                    EnemyupdateCar(gameTime);
                    


                    //Update Smoke n fire
                    if (smokenFire.size() > 0 || life < 4) UpdateSmoke(gameTime, playersCar);


                    // TODO: Add your update logic here
                    if (screenY - playersCar.y < iHeight(500))
                    {
                       if (life > 0) UpdateScore();
                        CarSpeed();
                    }
                    
                    if (Defaultpad == 1 && ActionDown == true) LeftRight(touchPosition); // steeringControl(ref playersCar.x); 
                    else if (Defaultpad == 2)
                    {
                    	if(accAvail) //if true
                    	{
                        myaccelerometer();
                    	}
                    }
                    

                }
 	                
 		        }
 			catch(Exception ex){
 				ex.getMessage();
 				System.exit(0); 
 			}
 		}
 		
 		
 		
 		void drawSmoke(Canvas canvas,SmokenFire smoke)
        {
            if (smoke.type == SmokenFire.littleFire || smoke.type == SmokenFire.HeavyFire
              || smoke.type == SmokenFire.CloudlyFire)
            	{
            	smokenFirePosition.dX = (int)smoke.x;
            	}
            else
            	smokenFirePosition.dX = (int)smoke.x - iWidth(18);
           

            smokenFirePosition.dY = (int)screenY - (int)smoke.y;
            Sprite smokenFireTexture;

            if (smoke.type == SmokenFire.littleSmoke) smokenFireTexture = smoking[0];
            else if (smoke.type == SmokenFire.littleSmoke1) smokenFireTexture = smoking[1];
            else if (smoke.type == SmokenFire.littleSmoke2) smokenFireTexture = smoking[2];
            else if (smoke.type == SmokenFire.littleSmoke3) smokenFireTexture = smoking[3];
            else if (smoke.type == SmokenFire.littleSmoke4) smokenFireTexture = smoking[4];
            else if (smoke.type == SmokenFire.littleSmoke5) smokenFireTexture = smoking[5];
            else if (smoke.type == SmokenFire.littleSmoke6) smokenFireTexture = smoking[6];
            else if (smoke.type == SmokenFire.littleSmoke7) smokenFireTexture = smoking[7];
            else if (smoke.type == SmokenFire.mediumSmoke8) smokenFireTexture = smoking[8];
            else if (smoke.type == SmokenFire.mediumSmoke9) smokenFireTexture = smoking[9];
            else if (smoke.type == SmokenFire.mediumSmoke10) smokenFireTexture = smoking[10];
            else if (smoke.type == SmokenFire.mediumSmoke11) smokenFireTexture = smoking[11];
            else if (smoke.type == SmokenFire.mediumSmoke12) smokenFireTexture = smoking[12];
            else if (smoke.type == SmokenFire.mediumSmoke13) smokenFireTexture = smoking[13];
            else if (smoke.type == SmokenFire.mediumSmoke14) smokenFireTexture = smoking[14];
            else if (smoke.type == SmokenFire.mediumSmoke15) smokenFireTexture = smoking[15];
            else if (smoke.type == SmokenFire.littleFire) smokenFireTexture = smoking[16];
            else if (smoke.type == SmokenFire.HeavyFire) smokenFireTexture = smoking[17];
            else if (smoke.type == SmokenFire.CloudlyFire) smokenFireTexture = smoking[18];
            else smokenFireTexture = null;

            if (smokenFireTexture != null)
            {
               // spriteBatch.Begin();
                //spriteBatch.Draw(smokenFireTexture, smokenFirePosition, Color.White);
                //spriteBatch.End();
            	try {
					smokenFireTexture.Draw(canvas, smokenFirePosition);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
 		
 		void updateCar(double gameTime)//ref Car car
        {
 			
 			 playersCar.y  += ((float)(gameTime * playersCar.speed));  
        }
 		
 		void removeEnemy(int j)
 		{
 			float aa2 = Lanes[0] + iWidth(5);
         	float aa3 = Lanes[1] + iWidth(5);
         	float aa4 = Lanes[2] + iWidth(5);
         	float aa5 = Lanes[3] + iWidth(5);
         	float aa6 = Lanes[4] + iWidth(5);
         	float aa7 = Lanes[5] + iWidth(5);
         	float aa8 = Lanes[6] + 2;
         	
         	
         	if(enemyCars.get(j).x == aa2)
         		Clanes[0] -= 1;
         	
         	else if(enemyCars.get(j).x == aa3)
         		Clanes[1] -= 1;
         	
         	else if(enemyCars.get(j).x == aa4)
         		Clanes[2] -= 1;
         	
         	else if(enemyCars.get(j).x == aa5) 
         		Clanes[3] -= 1;
         	
         	else if(enemyCars.get(j).x == aa6)
         		Clanes[4] -= 1;
         	
         	else if(enemyCars.get(j).x == aa7)
         		Clanes[5] -= 1;
         	
         	else if(enemyCars.get(j).x == aa8)
         		Clanes[6] -= 1;
         	
         	enemyCars.remove(j);
 		}
 		
 	
 		
 		void EnemyupdateCar(double gameTime)
 		{
 			 //looping to update there speed and remove the one more than 500 from players car
 			
             for (int j = 0; j < enemyCars.size(); ++j)
             {
                
               
                int enemysize = enemyCars.size();
                 List<Car> iCarSpeed = enemyCars;
                 for (int k = 0; k < enemysize; ++k)
                 {
              
                 	 if (j == k) continue;
                 	 if(iCarSpeed.get(j).x == iCarSpeed.get(k).x)
                 	{
                     if (iCarSpeed.get(j).y > iCarSpeed.get(k).y && iCarSpeed.get(j).y - iCarSpeed.get(k).y <= iHeight(250.0F))
                     {
                    	 enemyCars.get(k).speed = enemyCars.get(j).speed;
                     }                
                 	}
                 }	
                                
                 enemyCars.get(j).y += ((float)(gameTime * enemyCars.get(j).speed));
                 
                 CollusionDetect(enemyCars.get(j));
                 if (removelife == true) {
                	// enemyCars.remove(j);
                	 removeEnemy(j);
                	 removelife = false; }
                 if (crashed == true) { 
                	 enemyCars.get(j).crashed = true;  
                	// enemyCars.remove(j);
                	 removeEnemy(j);
                	 }
                
                 crashed = false;
                 
   
                 if (playersCar.y - enemyCars.get(j).y > 500)
                 {
                	 removeEnemy(j);
                 	
                 }
             }
         
 		}
 		
 		
 		 void updatebackground( double gameTime)
         {
             float speed = playersCar.speed;
             //Used to reduce speed of background to make car move faster until its Y psoition is 250
             if (screenY - playersCar.y > iHeight(470)) { 
            	 speed -= 100; }
             float distance = (float)(gameTime * speed);
             screenY += distance;
             backgroundposition1.dY += distance;
             if (backgroundposition1.dY > 0)
             {
            	 backgroundposition1.dY = backgroundposition1.dY - background.getHeight();
             }

         }
 		  			 
 		 void updateEnemyCars(double gameTime, int level, int Drive)
         {
 			 try
 			 {
 				float Ylane;
 				//float Xlane = 0;
 	             for (int i = 0; i < 7; ++i)
 	             {
 	                 //Using to determine the number of cars to be placed in one lane and generated car properties randomly
 	                 while (Clanes[i] < level)
 	                 {             	 
 	                     int tStart = Car.AudiA2;
 	                     int tStop = Car.BRTRed;
 	                     if (i == 6)
 	                     {
 	                         tStart = Car.BRTRed;
 	                         tStop = Car.BRTBlue + 1;
 	                     }
 	                     //Min + (int)(Math.random() * ((Max - Min) + 1)) //these is how to generate random with a range
 	                     //int type = value.Next(tStart, tStop);
 	                      int type = tStart + (int)(Math.random() * ((tStop - tStart)));
 	                     //Color color = Color.White;
 	                      int varSpeed = 200;
 	                      //for (int k = 0; k < enemyCars[i].Count; ++k)
 	                      //    if (enemyCars[i][k].speed > varSpeed && enemyCars[i][k].y > screenY) varSpeed = enemyCars[i][k].speed + value.Next(0,50);
 	                      //if (varSpeed > 450) varSpeed = 450;
 	                    
 	                      //int varX = 0;
 	                      //if (type == Car.SEDAN) varX = 20;
 	                      int varX = iWidth(5);
 	                      //if(type == Car.Getlife && life >= 8) type = value.Next(tStart, tStop);
 	                       if(type == Car.Getlife && life >= 8) type = tStart + (int)(Math.random() * ((tStop - tStart)));
 	                       if (type == Car.BRTBlue || type == Car.BRTRed) { varSpeed = 100; varX = 2; }


 	                      //looping to generate random speed for each enemy car created
 	                      // int speed =  c (varSpeed) + value.Next(0, 290);
 	                       int speed = (varSpeed) + (int)(Math.random() * ((390 - 1) + 1));;

 	                       float x = varX + Lanes[i];
 	                       float varY = screenY;
 	                      

 	                       //looping to set them @ speed as same vanishing point (varY) and place them  200 + random value above
 	                       for (int k = 0; k < enemyCars.size(); ++k)
 	                       {
 	                       if (enemyCars.get(k).x == x) 
 	                    	 {
 	                    	    Ylane =  enemyCars.get(k).y;
 	                    	    if (enemyCars.get(k).y > varY) varY = enemyCars.get(k).y;
 	                    	 }
 	                       }
 	                       
 	                     float y = (varY + 200) + 200 + (int)(Math.random() * ((Drive - 200) + 1));
 	                   
 	                     enemyCars.add(new Car(type, speed, x, y, false)); //.Add(new Car(type, speed, x, y, false));
 	                    Clanes[i] += 1;
 	                     
 	                 }
 	                            
                      
 	             }
 	             
 	            
 	               
 	             
 	             }
 		   catch(Exception ex){
				ex.getMessage();
			}
         }


 		  
 		  void CollusionDetect(Car iEnemycar)//Car car, ref Car playerscar
 		  {
 			  try
 			  {
 			  //playersCar
 			// car8position.dY = (int)(screenY - car.y);
 			 float carY = screenY - playersCar.y;
 			 float enemyY = screenY - iEnemycar.y;
 			 
             //if (car8position.dY < -225) return;  //choose your own based on screen size
 			if (enemyY < -225) return; 
             
             Sprite carTexture;
             if (iEnemycar.type == Car.AudiA2)
             {
                 carTexture = GameCar[0];
             }
             else if (iEnemycar.type == Car.AudiA6)
             {
                 carTexture = GameCar[1];
             }

             else if (iEnemycar.type == Car.AudiR8)
             {
                 carTexture = GameCar[2];
             }

             else if (iEnemycar.type == Car.AudiS5)
             {
                 carTexture = GameCar[3];
             }

             else if (iEnemycar.type == Car.BmwX6)
             {
                 carTexture = GameCar[4];
             }
             else if (iEnemycar.type == Car.Camaro)
             {
                 carTexture = GameCar[5];
             }
             else if (iEnemycar.type == Car.Crossfire)
             {
                 carTexture = GameCar[6];
             }

             else if (iEnemycar.type == Car.Crvblack)
             {
                 carTexture = GameCar[7];
             }

             else if (iEnemycar.type == Car.Crvgreen)
             {
                 carTexture = GameCar[8];
             }

             else if (iEnemycar.type == Car.accordblack)
             {
                 carTexture = GameCar[9];
             }
             else if (iEnemycar.type == Car.Danfo)
             {
                 carTexture = GameCar[10];
             }
             else if (iEnemycar.type == Car.Dogde)
             {
                 carTexture = GameCar[11];
             }

             else if (iEnemycar.type == Car.F250)
             {
                 carTexture = GameCar[12];
             }

             else if (iEnemycar.type == Car.accordblue)
             {
                 carTexture = GameCar[13];
             }
             else if (iEnemycar.type == Car.Fj)
             {
                 carTexture = GameCar[14];
             }
             else if (iEnemycar.type == Car.accordbrown)
             {
                 carTexture = GameCar[15];
             }
             else if (iEnemycar.type == Car.accorddarkblue)
             {
                 carTexture = GameCar[16];
             }
             else if (iEnemycar.type == Car.mazda6)
             {
                 carTexture = GameCar[17];
             }

             else if (iEnemycar.type == Car.mazdaR8)
             {
                 carTexture = GameCar[18];
             }

             else if (iEnemycar.type == Car.seat)
             {
                 carTexture = GameCar[19];
             }

             else if (iEnemycar.type == Car.Tunderred)
             {
                 carTexture = GameCar[20];
             }
             else if (iEnemycar.type == Car.accordgreen)
             {
                 carTexture = GameCar[21];
             }
             else if (iEnemycar.type == Car.Molelue)
             {
                 carTexture = GameCar[22];
             }
             else if (iEnemycar.type == Car.accordpink)
             {
                 carTexture = GameCar[23];
             }
             else if (iEnemycar.type == Car.accordred)
             {
                 carTexture = GameCar[24];
             }
             else if (iEnemycar.type == Car.accordwhite)
             {
                 carTexture = GameCar[25];
             }

             else if (iEnemycar.type == Car.accordyellow)
             {
                 carTexture = GameCar[26];
             }
             else if (iEnemycar.type == Car.corrolablue)
             {
                 carTexture = GameCar[27];
             }
             else if (iEnemycar.type == Car.corrolalightblue)
             {
                 carTexture = GameCar[28];
             }
             else if (iEnemycar.type == Car.corrolapink)
             {
                 carTexture = GameCar[29];
             }
             else if (iEnemycar.type == Car.corrolayellow)
             {
                 carTexture = GameCar[30];
             }
             else if (iEnemycar.type == Car.Truckbrown)
             {
                 carTexture = GameCar[31];
             }
             else if (iEnemycar.type == Car.Truckred)
             {
                 carTexture = GameCar[32];
             }

             else if (iEnemycar.type == Car.Truckblue)
             {
                 carTexture = GameCar[33];
             }
             else if (iEnemycar.type == Car.Getlife)
             {
                 carTexture = GameCar[34];
             }

             else if (iEnemycar.type == Car.BRTRed)
             {
                 carTexture = GameCar[35];
             }
             else if (iEnemycar.type == Car.BRTBlue)
             {
                 carTexture = GameCar[36];
             }
             else
             {
                 carTexture = GameCar[5];
             }
 			             
 
             
 			 if((playersCar.x + GameCar[5].getWidth()) >= iEnemycar.x &&
 		    		 playersCar.x  <= (iEnemycar.x + carTexture.getWidth()) &&
 		    		 (carY + GameCar[5].getHeight())  >= enemyY &&
 		    				 carY  <= (enemyY + carTexture.getHeight()))
 			 {
                 if (iEnemycar.type == Car.Getlife && life < 8) {
                     life += 1; removelife = true;
                    if(soundEnabled == true) playSound(Slife,0,false);  
                 }
                else if(Carhit == false)
                     {
                     Carhit = true;
                     if (soundEnabled == true) playSound(Crash,0,false);//Crash.Play();
                     smokenFire =  new ArrayList<SmokenFire>();;
                     Explosion(iHeight(200), iWidth(128), playersCar.x, playersCar.y, playersCar.speed, GameCar[5].getHeight(), GameCar[5].getWidth());
                     Pspeed = playersCar.speed;
                     playersCar.speed = iEnemycar.speed - 70;
                     life -= 1;
                     SmokeCount = 0;
                     crashed = true;
                     }
 			 }
 			 
 			 
 			 if (life == 0 )
             {
                 if (newhighscore == true)
                 {
                     HighScoreChecker();
                     SaveGame();
                 }

                 if (nonewhighscore == true) { gameState = gameScores; }
                 if (nonewhighscore == false) { gameState = gameOver; }

                // LoadContent();

                 newhighscore = false;
                 loadData = false;
                 iSettings.loadData = false;
             }
 		  
 		  }
 		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 		  }
 			 void Explosion(float firehieght,float firewidth, float x,float y,float speed,float height,float width)
 	        {
 	            float firex = x - (firewidth / 2 - width / 2);
 	            float firey = y - (firehieght / 2 - height / 2);
 	            float Firespeed = speed;
 	            int fireType = SmokenFire.littleFire;

 	            smokenFire.add(new SmokenFire(fireType, Firespeed, firex, firey));
 	        }

 		  
 		 void UpdateScore()
         {
             myscore += 1;
             if (playersCar.speed > 500 && playersCar.speed < 550)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 5;
                     else scorevalue += 10;
                     myscore = 0;
                     score =  String.valueOf(scorevalue);//.ToString();
                 }
             }
             else if (playersCar.speed > 550 && playersCar.speed < 600)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 10;
                     else scorevalue += 20;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 600 &&  playersCar.speed < 650)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 20;
                     else scorevalue += 30;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 650)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 30;
                     else scorevalue += 40;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 700)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 40;
                     else scorevalue += 50;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 750)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 50;
                     else scorevalue += 60;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 850)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 60;
                     else scorevalue += 70;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else if (playersCar.speed > 950)
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 70;
                     else scorevalue += 80;
                     myscore = 0;
                     score = String.valueOf(scorevalue);//scorevalue.ToString();
                 }
             }
             else
             {
                 if (myscore == 10)
                 {
                     if (playersCar.x < 415) scorevalue += 1;
                     else scorevalue += 2;
                     myscore = 0;
                     score =String.valueOf(scorevalue);// scorevalue.ToString();
                 }
             }
         }

 		  void UpdateSmoke(double gameTime,Car players)
 	        {
 	            if(life < 4 )SmokeCount += 1;

 	            float speed = players.speed;
 	            float x = players.x;
 	            float y = (playersCar.y - 3);

 	            if (life == 1 && SmokeCount == 20)
 	            {
 	                int type = SmokenFire.mediumSmoke15;

 	                        smokenFire.add(new SmokenFire(type, speed, x, y));
 	                        SmokeCount = 0;
 	                        //smokenFire
 	             }
 	            else if (life == 2 && SmokeCount == 25)
 	            {
 	                int type = SmokenFire.mediumSmoke12;

 	                smokenFire.add(new SmokenFire(type, speed, x, y));
 	                SmokeCount = 0;
 	            }
 	            else if (life == 3 && SmokeCount == 35)
 	            {
 	                int type = SmokenFire.littleSmoke7;

 	                smokenFire.add(new SmokenFire(type, speed, x, y));
 	                SmokeCount = 0;
 	            }
 	            
 	            
 	            if (smokenFire.size() > 0)
 	            {
 	                for (int j = 0; j < smokenFire.size(); ++j)
 	                {
 	                   // SmokenFire smoke = smokenFire.get(j);
 	                    //smokeSpeed(gameTime, ref smoke);
 	                    
 	                   if (smokenFire.get(j).type == SmokenFire.littleFire || smokenFire.get(j).type == SmokenFire.HeavyFire || smokenFire.get(j).type == SmokenFire.CloudlyFire)
 	                  {
 	                	  smokenFire.get(j).speed -= 1;
 	                	 smokenFire.get(j).y += ((float)(gameTime * smokenFire.get(j).speed));
 	                      NextFire += 1;
 	                      if (NextFire == 5)
 	                      {
 	                          if (smokenFire.get(j).type == SmokenFire.littleFire) smokenFire.get(j).type += 1;
 	                          else if (smokenFire.get(j).type == SmokenFire.HeavyFire) smokenFire.get(j).type += 1;
 	                          else if (smokenFire.get(j).type == SmokenFire.CloudlyFire) { kaboom = true;  }
 	                          NextFire = 0;
 	                      }
 	                  }
 	                  else
 	                  {
 	                	 smokenFire.get(j).y += ((float)(gameTime * smokenFire.get(j).speed));
 	                      NextSmoke += 1;
 	                     smokenFire.get(j).speed -= 6;
 	                      if (NextSmoke == 7)
 	                      {
 	                          if (smokenFire.get(j).type != 0) smokenFire.get(j).type -= 1;
 	                          NextSmoke = 0;
 	                      }
 	                  }
 	                    
 	                    if (smokenFire.get(j).type == SmokenFire.littleSmoke) smokenFire.remove(j);
 	                   // else if (smoke.type == SmokenFire.CloudlyFire) smokenFire.RemoveAt(j); 
 	                    else if (smokenFire.get(j).type == SmokenFire.CloudlyFire && kaboom == true)
 	                    {
 	                        smokenFire.remove(j);
 	                        kaboom = false;
 	                    }
 	                }
 	            }
 	            }
 		  
 		 void CarSpeed()
 	        {
 	            
 	            float speed = 0.2f * playersCar.speed;
 	            String checkspeed = String.valueOf(speed);// speed.ToString();
 	            if (checkspeed.length() > 5) checkspeed = checkspeed.substring(0, 5);
 	            CSpeed = checkspeed + "KM";
 	        }
 		  
 		 void CarIntersected(Canvas canvas)
 	        {
 	            Hitspace += 2;
 	            if (Hitspace >= 10 && Hitspace < 20)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 2;
 	                 drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 30 && Hitspace < 40)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 2;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 50 && Hitspace < 70)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 4;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 80 && Hitspace < 90)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 2;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 80 && Hitspace < 90)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 5;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 100 && Hitspace < 110)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 5;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 120 && Hitspace < 130)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 5;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 140 && Hitspace < 150)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 3;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace >= 160 && Hitspace < 170)
 	            {
 	                if (playersCar.speed <= 600) playersCar.speed += 3;
 	               drawCar(canvas, playersCar);
 	            }
 	            if (Hitspace == 180)
 	            {
 	                playersCar.speed = 600;
 	                Hitspace = 0;
 	                Carhit = false;
 	            }
 	        }
 		  
 		 void DrawLifes(Canvas canvas) throws Exception
         {
             Sprite Gamelife;
             if (life == 8) { Gamelife = lifes[7]; }
             else if (life == 7) { Gamelife = lifes[6]; }
             else if (life == 6) { Gamelife = lifes[5]; }
             else if (life == 5) { Gamelife = lifes[4]; }
             else if (life == 4) { Gamelife = lifes[3]; }
             else if (life == 3) { Gamelife = lifes[2]; }
             else if (life == 2) { Gamelife = lifes[1]; }
             else if (life == 1) { Gamelife = lifes[0]; }
             else { Gamelife = lifes[0]; }
            
             Gamelife.Draw(canvas, ilife);
            // spriteBatch.Begin();
            // spriteBatch.Draw(Gamelife, ilife, Color.White);
            // spriteBatch.End();
         }
 		 
 		
 		 
 		Handler mHandler = new Handler()
 		{
 		    public void handleMessage(Message msg)
 		    {
 		       //Display Alert
 		    	alert = new AlertDialog.Builder(mContext);
 	      	   alert.setTitle("NEW HIGHSCORE!!!" + " " + scorevalue);
 	      	   alert.setMessage("Insert your name (maximum characters 12)");

 	      	   // Set an EditText view to get user input 
 	      	   final EditText input = new EditText(mContext);
 	      	   alert.setView(input);
 	      	 
 	      	   
 	      	   //Use this to choose the type of input you want to collect
 	      	   //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
 	      	   //builder.setView(input);

 	      	  
 	      	   alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
 	      	   public void onClick(DialogInterface dialog, int whichButton) {
 	      		newhighscorename = input.getText().toString();
 	      	    // Do something with value!
 	      	 if (newhighscorename != null)
             {
                 if (newhighscorename.length() > 12)
                 {
                     newhighscorename = newhighscorename.substring(0, 12);
                     editor.putString("highscoreName" + String.valueOf(scoredetails), newhighscorename);
                     //.highscoreName[scoredetails] = newhighscorename;
                 }
                 else
                 {
                     //settings.highscoreName[scoredetails] = newhighscorename;
                     editor.putString("highscoreName" + String.valueOf(scoredetails), newhighscorename);
                 }
             }
 	      	 
 	      	 editor.commit();
 	      	    }
 	      	   });
 	      	   
 	      	   
 	      	   alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 	      	    public void onClick(DialogInterface dialog, int whichButton) {
 	      	        // Canceled.
 	      	    	
 	      	   }
 	      	   });

 	      	    alert.show();
 		    }
 		};
 		
 		
 		 void HighScoreChecker()
         {
             int i, j; int k = 0;
             String name = "Player One";
             String name2;
             
             for (i = 0; i < iSettings.highscore.length; i++) //hard coded
             {
            	
            	 j = Integer.parseInt(settings.getString("highscore" + String.valueOf(i), "0"));//.highscore[i]);
                 name2 = settings.getString("highscoreName" + String.valueOf(i), null); // defValue)highscoreName[i];

                 if (scorevalue > j)
                 {
                     if (newhighscore == true)
                     {
                         //GetName();
                    	 scoredetails = i;
                    	 mHandler.sendEmptyMessage(0);
                         //settings.highscoreName[i] = name;
                    	 if(newhighscorename == null)//added code
                         editor.putString("highscoreName" + String.valueOf(i), name);
                    	 else  //added code
                    		 editor.putString("highscoreName" + String.valueOf(i), newhighscorename); // added code
                        // settings.highscore[i] = Convert.ToString(scorevalue);
                         editor.putString("highscore" + String.valueOf(i), String.valueOf(scorevalue));
                         scorevalue = j;
                         name = name2;
                         newhighscore = false;
                         nonewhighscore = true;
                     }


                     if (newhighscore == false && k == 1)
                     {
                         //settings.highscoreName[i] = name;
                    	 editor.putString("highscoreName" + String.valueOf(i), name);
                         //settings.highscore[i] = Convert.ToString(scorevalue);
                    	 editor.putString("highscore" + String.valueOf(i), String.valueOf(scorevalue) );
                         scorevalue = j;
                         name = name2;
                     }

                     k = 1;
                 }
             }
             
             editor.commit();
         }
 		 
         
         void myaccelerometer()
         {

              float YSprite, XSprite,ZSprite, xstart = playersCar.x;
                      
             XSprite = accValue[0];
             YSprite = accValue[1];
             ZSprite = accValue[2];
             
             // float accVector = accValue[0];
             //lock (accelerometerVectorLock)
            // {
             //    accVector = accelerometerVector;
             //}
             //Move car left or right
             XSprite = (100 * XSprite);
             if (XSprite < -27) xstart -= 10.4f;
             else if (XSprite >= iWidth(-27) && XSprite < iWidth(-24)) xstart -= 9.2f;
             else if (XSprite >= iWidth(-24) && XSprite < iWidth(-21)) xstart -= 8.1f;
             else if (XSprite >= iWidth(-21) && XSprite < iWidth(-18)) xstart -= 7.9f;
             else if (XSprite >= iWidth(-18) && XSprite < iWidth(-15)) xstart -= 6.8f;
             else if (XSprite >= iWidth(-15) && XSprite < iWidth(-12)) xstart -= 4.7f;
             else if (XSprite >= iWidth(-12) && XSprite < iWidth(-9)) xstart -= 3.6f;
             else if (XSprite >= iWidth(-9) && XSprite < iWidth(-6)) xstart -= 2.5f;
             else if (XSprite >= iWidth(-6) && XSprite < iWidth(-3)) xstart -= 1;
             else if (XSprite >= iWidth(3) && XSprite < iWidth(6)) xstart += 1;
             else if (XSprite >= iWidth(6) && XSprite < iWidth(9)) xstart += 2.5f;
             else if (XSprite >= iWidth(9) && XSprite < iWidth(12)) xstart += 3.6f;
             else if (XSprite >= iWidth(12) && XSprite < iWidth(15)) xstart += 4.7f;
             else if (XSprite >= iWidth(15) && XSprite < iWidth(18)) xstart += 6.8f;
             else if (XSprite >= iWidth(18) && XSprite < iWidth(21)) xstart += 7.9f;
             else if (XSprite >= iWidth(21) && XSprite < iWidth(24)) xstart += 8.1f;
             else if (XSprite >= iWidth(24) && XSprite < iWidth(27)) xstart += 9.2f;
             else if (XSprite >= iWidth(27)) xstart += 10.4f;

             if (xstart + iWidth(48) < deviceWidth && (xstart - iWidth(8)) > 0) playersCar.x = xstart; 

             //Increase and reduce car speed
             YSprite = (100 * YSprite);
             ZSprite = (100 * ZSprite);

             if (YSprite > iHeight(-45) && playersCar.speed < 1000 && Carhit == false) playersCar.speed += 2;
             else if (YSprite < iHeight(-60) && playersCar.speed > 500 && Carhit == false) playersCar.speed -= 5;
             
         }
         
         void drawCar(Canvas canvas, Car car) 
         {
             //Used to update vector before drawing individual car
            // car8position.dX = (int)car.x;
        	 float carX = car.x;
             //Getting diference from view vanishing point to position vector.Y
             //car8position.dY = (int)screenY - (int)car.y;
             float carY = screenY - car.y;

             if (carY < -220) return;
             Sprite carTexture;
             if (car.type == Car.AudiA2)
             {
                 carTexture = GameCar[0];
             }
             else if (car.type == Car.AudiA6)
             {
                 carTexture = GameCar[1];
             }

             else if (car.type == Car.AudiR8)
             {
                 carTexture = GameCar[2];
             }

             else if (car.type == Car.AudiS5)
             {
                 carTexture = GameCar[3];
             }

             else if (car.type == Car.BmwX6)
             {
                 carTexture = GameCar[4];
             }
             else if (car.type == Car.Camaro)
             {
                 carTexture = GameCar[5];
             }
             else if (car.type == Car.Crossfire)
             {
                 carTexture = GameCar[6];
             }

             else if (car.type == Car.Crvblack)
             {
                 carTexture = GameCar[7];
             }

             else if (car.type == Car.Crvgreen)
             {
                 carTexture = GameCar[8];
             }

             else if (car.type == Car.accordblack)
             {
                 carTexture = GameCar[9];
             }
             else if (car.type == Car.Danfo)
             {
                 carTexture = GameCar[10];
             }
             else if (car.type == Car.Dogde)
             {
                 carTexture = GameCar[11];
             }

             else if (car.type == Car.F250)
             {
                 carTexture = GameCar[12];
             }

             else if (car.type == Car.accordblue)
             {
                 carTexture = GameCar[13];
             }
             else if (car.type == Car.Fj)
             {
                 carTexture = GameCar[14];
             }
             else if (car.type == Car.accordbrown)
             {
                 carTexture = GameCar[15];
             }
             else if (car.type == Car.accorddarkblue)
             {
                 carTexture = GameCar[16];
             }
             else if (car.type == Car.mazda6)
             {
                 carTexture = GameCar[17];
             }

             else if (car.type == Car.mazdaR8)
             {
                 carTexture = GameCar[18];
             }

             else if (car.type == Car.seat)
             {
                 carTexture = GameCar[19];
             }

             else if (car.type == Car.Tunderred)
             {
                 carTexture = GameCar[20];
             }
             else if (car.type == Car.accordgreen)
             {
                 carTexture = GameCar[21];
             }
             else if (car.type == Car.Molelue)
             {
                 carTexture = GameCar[22];
             }
             else if (car.type == Car.accordpink)
             {
                 carTexture = GameCar[23];
             }
             else if (car.type == Car.accordred)
             {
                 carTexture = GameCar[24];
             }
             else if (car.type == Car.accordwhite)
             {
                 carTexture = GameCar[25];
             }

             else if (car.type == Car.accordyellow)
             {
                 carTexture = GameCar[26];
             } 
             else if (car.type == Car.corrolablue)
             {
                 carTexture = GameCar[27];
             }
             else if (car.type == Car.corrolalightblue)
             {
                 carTexture = GameCar[28];
             }
             else if (car.type == Car.corrolapink)
             {
                 carTexture = GameCar[29];
             }
             else if (car.type == Car.corrolayellow)
             {
                 carTexture = GameCar[30];
             }
             else if (car.type == Car.Truckbrown)
             {
                 carTexture = GameCar[31];
             }
             else if (car.type == Car.Truckred)
             {
                 carTexture = GameCar[32];
             }

             else if (car.type == Car.Truckblue)
             {
                 carTexture = GameCar[33];
             }

             else if (car.type == Car.Getlife)
             {
                 carTexture = GameCar[34];
             }
             else if (car.type == Car.BRTRed)
             {
                 carTexture = GameCar[35];
             }
             else if (car.type == Car.BRTBlue)
             {
                 carTexture = GameCar[36];
             }
             else 
             {
                 carTexture = GameCar[5];
             }

     
                 //spriteBatch.Begin();
                 //spriteBatch.Draw(carTexture, car8position, Color.White);
                 //spriteBatch.End();
             try {
				carTexture.Draw(canvas, carX,carY);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         }
         
         
 		private void doDraw(Canvas canvas){
			deviceWidth = canvas.getWidth();
			deviceHeight = canvas.getHeight();
			 
			try{
				if(deviceWidth>deviceHeight || deviceWidth <= 0 || deviceHeight <= 0)return;
				
	
				// canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.droid_1), 10, 10, null);
				
			 
				 if (gameState == gameLoading)
	               {
	                    PrestartingDraw(canvas);
	                }
				 else if(gameState == gameStarting)
				 { 
					 //canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					 start.Draw(canvas);
					 
					  if (loadData == true)
					 {

						// canvas.drawBitmap(StartText, StartTextPosition.dX,StartTextPosition.dY, paint);
						 StartText.Draw(canvas, StartTextPosition.dX, StartTextPosition.dY);
						// canvas.drawBitmap(loadGame, loadGameTextPosition.dX, loadGameTextPosition.dY, paint);
						 loadGame.Draw(canvas, loadGameTextPosition.dX, loadGameTextPosition.dY);
						// canvas.drawBitmap(Options, optionsTextPosition.dX,optionsTextPosition.dY, paint);
						 Options.Draw(canvas, optionsTextPosition.dX, optionsTextPosition.dY);
	                   
						// canvas.drawBitmap(HighScoreText, HighScoreTextPosition.dX,HighScoreTextPosition.dY, paint);
						 HighScoreText.Draw(canvas, HighScoreTextPosition.dX, HighScoreTextPosition.dY);
	                       
						// canvas.drawBitmap(Credits, creditTextPosition.dX, creditTextPosition.dY, paint);
						 Credits.Draw(canvas, creditTextPosition.dX, creditTextPosition.dY);
	                

	                        if (StartTextPosition.dX > iWidth(27)) StartTextPosition.dX -= iWidth(18);
	                        if (loadGameTextPosition.dX > iWidth(27)) loadGameTextPosition.dX -= iWidth(15);
	                        if (optionsTextPosition.dX > iWidth(27)) optionsTextPosition.dX -= iWidth(12);
	                        if (HighScoreTextPosition.dX > iWidth(27)) HighScoreTextPosition.dX -= iWidth(9);
	                        if (creditTextPosition.dX > iWidth(29)) creditTextPosition.dX -= iWidth(8);
					 }
					 else if (loadData == false)
	                    {
	                       
						 //canvas.drawBitmap(StartText, StartTextPosition.dX,StartTextPosition.dY, paint);
	                      StartText.Draw(canvas, StartTextPosition.dX, StartTextPosition.dY);
						 //canvas.drawBitmap(Options, loadGameTextPosition.dX,loadGameTextPosition.dY, paint);
						 Options.Draw(canvas, loadGameTextPosition.dX, loadGameTextPosition.dY);
						 //canvas.drawBitmap(HighScoreText, optionsTextPosition.dX,optionsTextPosition.dY, paint);
						 HighScoreText.Draw(canvas, optionsTextPosition.dX, optionsTextPosition.dY);
						// canvas.drawBitmap(Credits, HighScoreTextPosition.dX, HighScoreTextPosition.dY, paint);
						 Credits.Draw(canvas, HighScoreTextPosition.dX, HighScoreTextPosition.dY);

	                        if (StartTextPosition.dX > iWidth(27)) StartTextPosition.dX -= iWidth(18);
	                        if (loadGameTextPosition.dX > iWidth(27)) loadGameTextPosition.dX -= iWidth(15);
	                        if (optionsTextPosition.dX > iWidth(27)) optionsTextPosition.dX -= iWidth(12);
	                        if (HighScoreTextPosition.dX > iWidth(27)) HighScoreTextPosition.dX -= iWidth(9);
	                        // if (HighScoreTextPosition.X > 27) HighScoreTextPosition.X -= 9;
	                    }
				 }
				 else if(gameState == gameOver)
				 {
					 
	                   
					// canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					  start.Draw(canvas);
	                    
	                 
					
					  lblgameover.Draw(canvas, iWidth(87), iHeight(337));
					  scoreimage.Draw(canvas, iWidth(124), iHeight(414));
	                   
	                   
					  Paint p = new Paint();
						p.setColor(Color.BLACK);
						p.setTextSize(18);
					 
	                    canvas.drawText(score, iWidth(240), iHeight(407), p);
	                    paint.setColor(Color.WHITE);
	                    
	                 
	                    //canvas.drawBitmap(retry,retryPosition.dX,retryPosition.dY,paint);
	                    retry.Draw(canvas, retryPosition.dX, retryPosition.dY);
	                   
	                    //canvas.drawBitmap(Main,MainPosition.dX,MainPosition.dY,paint);
	                    Main.Draw(canvas, MainPosition.dX, MainPosition.dY);

	                    if (retryPosition.dX > iWidth(64)) retryPosition.dX -= iWidth(12);
	                    if (MainPosition.dX > iWidth(64)) MainPosition.dX -= iWidth(15);
					 
				 }
				 else if(gameState == gameScores)
				 {
					 //editor.commit();
					 
					Paint p = new Paint();
					p.setColor(Color.BLACK);
					if(deviceWidth < 320) p.setTextSize(18);
					else if(deviceWidth > 320 && deviceWidth < 800 ) p.setTextSize(20);
					else if(deviceWidth > 800 ) p.setTextSize(28);
					else p.setTextSize(18);
					//paint.setStyle(Style.FILL);
					// p.setTextSize(getResources().getDimension(R.dimen.text_Size));
	                   
					 // canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					  start.Draw(canvas);
					  //canvas.drawBitmap(lblhighscore, iWidth(80), iHeight(279), paint);
					  lblhighscore.Draw(canvas, iWidth(80), iHeight(274));

	                 
					   highScorePosition.dX = iWidth(340); 
					   highScorePosition.dY = iHeight(343);
					   highScorePosition2.dX = iWidth(18); 
					   highScorePosition2.dY = iHeight(343);
					  // float value = iHeight(45);
					   
					   
					 canvas.drawText(settings.getString("highscore0", null),highScorePosition.dX,highScorePosition.dY, p);  
					   
	                   // spriteBatch.DrawString(highfont, settings.highscore[0], highScorePosition, Color.Black);
					  // canvas.drawText(settings.highscore0,highScorePosition.dX,highScorePosition.dY, paint);
	                   // spriteBatch.DrawString(highfont, " 1." + settings.highscoreName[0], highScorePosition2, Color.Black);
					   canvas.drawText(" 1." + settings.getString("highscoreName0", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 1)); highScorePosition2.dY = (iHeight(343 + 45 * 1));
	                   // spriteBatch.DrawString(highfont, settings.highscore[1], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore1", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, " 2." + settings.highscoreName[1], highScorePosition2, Color.Black);
	                    canvas.drawText(" 2."  + settings.getString("highscoreName1", null),highScorePosition2.dX,highScorePosition2.dY,p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 2)); highScorePosition2.dY = (iHeight(343 + 45 * 2));
	                    //spriteBatch.DrawString(highfont, settings.highscore[2], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore2", null),highScorePosition.dX,highScorePosition.dY, p);
	                    //spriteBatch.DrawString(highfont, " 3." + settings.highscoreName[2], highScorePosition2, Color.Black);
	                    canvas.drawText(" 3." + settings.getString("highscoreName2", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 3)); highScorePosition2.dY = (iHeight(343+ 45 * 3));
	                    //spriteBatch.DrawString(highfont, settings.highscore[3], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore3", null),highScorePosition.dX,highScorePosition.dY, p);
	                    //spriteBatch.DrawString(highfont, " 4." + settings.highscoreName[3], highScorePosition2, Color.Black);
	                    canvas.drawText(" 4." + settings.getString("highscoreName3", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 4)); highScorePosition2.dY = (iHeight(343+ 45 * 4));
	                    //spriteBatch.DrawString(highfont, settings.highscore[4], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore4", null),highScorePosition.dX,highScorePosition.dY, p);
	                    //spriteBatch.DrawString(highfont, " 5." + settings.highscoreName[4], highScorePosition2, Color.Black);
	                    canvas.drawText(" 5." +  settings.getString("highscoreName4", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 5)); highScorePosition2.dY = (iHeight(343 + 45 * 5));
	                    //spriteBatch.DrawString(highfont, settings.highscore[5], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore5", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, " 6." + settings.highscoreName[5], highScorePosition2, Color.Black);
	                    canvas.drawText(" 6." +  settings.getString("highscoreName5", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 6)); highScorePosition2.dY = (iHeight(343 + 45 * 6));
	                   // spriteBatch.DrawString(highfont, settings.highscore[6], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore6", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, " 7." + settings.highscoreName[6], highScorePosition2, Color.Black);
	                    canvas.drawText(" 7." + settings.getString("highscoreName6", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                   
	                    highScorePosition.dY = (iHeight(343 + 45 * 7)); highScorePosition2.dY = (iHeight(343 + 45 * 7));
	                    //spriteBatch.DrawString(highfont, settings.highscore[7], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore7", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, " 8." + settings.highscoreName[7], highScorePosition2, Color.Black);
	                    canvas.drawText(" 8." + settings.getString("highscoreName7", null),highScorePosition2.dX,highScorePosition2.dY, p);
                        
	                    	                    
	                    highScorePosition.dY = (iHeight(343 + 45 * 8)); highScorePosition2.dY = (iHeight(343 + 45 * 8));
	                   // spriteBatch.DrawString(highfont, settings.highscore[8], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore8", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, " 9." + settings.highscoreName[8], highScorePosition2, Color.Black);
	                    canvas.drawText(" 9." + settings.getString("highscoreName8", null),highScorePosition2.dX,highScorePosition2.dY, p);

	                    highScorePosition.dY = (iHeight(343 + 45 * 9)); highScorePosition2.dY = (iHeight(343 + 45 * 9));
	                   // spriteBatch.DrawString(highfont, settings.highscore[9], highScorePosition, Color.Black);
	                    canvas.drawText(settings.getString("highscore9", null),highScorePosition.dX,highScorePosition.dY, p);
	                   // spriteBatch.DrawString(highfont, "10." + settings.highscoreName[9], highScorePosition2, Color.Black);
	                    canvas.drawText(" 10." + settings.getString("highscoreName9", null),highScorePosition2.dX,highScorePosition2.dY, p);



				 }
				 else  if(gameState == gameOptions)
				 {
					// paint.setColor(android.R.color.white);
	                  
					 //canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					 start.Draw(canvas);
					 				 
					
					 // canvas.drawBitmap(lblgameoptions,iWidth(148),iHeight(316),paint);
					  lblgameoptions.Draw(canvas, iWidth(148), iHeight(316));
	                    
	                //  under maintance for accelerator
	               //   Controller.Draw(canvas, ControllerTextPosition.dX, ControllerTextPosition.dY);  
	               //   Help.Draw(canvas, HelpTextPosition.dX, HelpTextPosition.dY); 
	               //   Sound.Draw(canvas, SoundPosition);
					  
					  Sound.Draw(canvas, ControllerTextPosition);
					  Help.Draw(canvas, SoundPosition); 

	                    if (ControllerTextPosition.dX > iWidth(12)) ControllerTextPosition.dX -= iWidth(18);
	                    if (SoundPosition.dX > iWidth(12)) SoundPosition.dX -= iWidth(15);
	                    //if (HelpTextPosition.dX > iWidth(12)) HelpTextPosition.dX -= iWidth(12);
				 }
				 else if(gameState == gameController)
				 {
					 //canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					 start.Draw(canvas);
					 
					 //canvas.drawBitmap(lblcontroller,iWidth(96),iHeight(307),paint);
					 lblcontroller.Draw(canvas, iWidth(96), iHeight(307));

	                    if (Defaultpad == apad)
	                    {
	                     	//canvas.drawBitmap(iControllerA,padPosition.dX,padPosition.dY,paint);
	                    	iControllerA.Draw(canvas, padPosition);
	                    }
	                    else if (Defaultpad == spad)
	                    {
	                    	//canvas.drawBitmap(iControllerB,padPosition.dX,padPosition.dY,paint);
	                    	iControllerB.Draw(canvas, padPosition);
	                    }
	                    if (padPosition.dX > iWidth(12)) padPosition.dX -= iWidth(21);
				 }
				 else  if(gameState == gameSounds)
				 {
					// canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					 start.Draw(canvas);
	                 
					// canvas.drawBitmap(lblsounds, iWidth(146),iHeight(316), paint);
					 lblsounds.Draw(canvas, iWidth(146), iHeight(316));

	                    if (soundEnabled == true)
	                    {
	                    	// canvas.drawBitmap(btnSoundEnabled, bSoundPosition.dX,bSoundPosition.dY, paint);
	                    	 btnSoundEnabled.Draw(canvas, bSoundPosition);
	                    }
	                    else if (soundEnabled == false)
	                    {
	                    	//canvas.drawBitmap(btnSoundDisabled, bSoundPosition.dX,bSoundPosition.dY, paint);
	                    	btnSoundDisabled.Draw(canvas, bSoundPosition);
	                    }
	                    if (bSoundPosition.dX > 72) bSoundPosition.dX -= 15;
				 }
				 else  if(gameState == gameCredits)
				 {
					//canvas.drawBitmap(start, null, new Rect(0,0,deviceWidth,deviceHeight), paint);
					  start.Draw(canvas);
	                   
					 //canvas.drawBitmap(lblcredits[0], iWidth(146),iHeight(284), paint);
					 lblcredits[0].Draw(canvas, iWidth(146), iHeight(284));
	                  
					// canvas.drawBitmap(lblcredits[1], iWidth(194),iHeight(354), paint);
					 lblcredits[1].Draw(canvas, iWidth(194), iHeight(354));
	                  
					 //canvas.drawBitmap(lblcredits[2], iWidth(181),iHeight(431), paint);
					 lblcredits[2].Draw(canvas, iWidth(181), iHeight(431));
	                  
					 //canvas.drawBitmap(lblcredits[3], iWidth(181),iHeight(508), paint);
					 lblcredits[3].Draw(canvas, iWidth(181), iHeight(508));
	                  
					 //canvas.drawBitmap(lblcredits[4], iWidth(181),iHeight(585), paint);
					 lblcredits[4].Draw(canvas, iWidth(181), iHeight(585));
	                  
					// canvas.drawBitmap(lblcredits[5], iWidth(70),iHeight(599), paint);
					 lblcredits[5].Draw(canvas, iWidth(70), iHeight(599));
	                  
					// canvas.drawBitmap(lblcredits[6], iWidth(42),iHeight(754), paint);
					 lblcredits[6].Draw(canvas, iWidth(42), iHeight(754));
				 }
				 else  if(gameState == gameHelp)
				 { 
					  start.Draw(canvas);
					
	                  //  canvas.drawBitmap(lblhelp[0], iWidth(68),iHeight(300), paint);
	                    lblhelp[0].Draw(canvas, iWidth(68), iHeight(300));
	                    
	                   // canvas.drawBitmap(lblhelp[1], iWidth(40),iHeight(375), paint);
	                    lblhelp[1].Draw(canvas, iWidth(40), iHeight(375));
	                   // canvas.drawBitmap(lblhelp[2], iWidth(153),iHeight(720), paint);
	                    lblhelp[2].Draw(canvas, iWidth(68), iHeight(720));
	                  //  canvas.drawBitmap(lblhelp[3], iWidth(40),iHeight(748), paint);
	                    lblhelp[3].Draw(canvas, iWidth(48), iHeight(748));
				 }
				 else if(gameState == gameinplay)
				 {
					//int alphaAmount = 128; // some value 0-255 where 0 is fully transparent and 255 is fully opaque
                 	Paint p = new Paint();
                 	//Set Blue Color
                 	p.setColor(Color.WHITE);
                 	//Set transparency roughly at 50%
                 	p.setAlpha(125);
                 //	p.setTextScaleX(scaleX)
                 	
					    //spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
	                    //spriteBatch.Draw(background, backgroundposition1, Color.White);
	                    //spriteBatch.End();
	                    background.Draw(canvas, backgroundposition1);

	                    backgroundposition1.dY += background.getHeight();
	                    //spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.Opaque);
	                    //spriteBatch.Draw(background, backgroundposition1, Color.White);
	                    //spriteBatch.End();
	                    background.Draw(canvas, backgroundposition1);
	                    
	                    backgroundposition1.dY -= background.getHeight();

	                    for (int i = 0; i < enemyCars.size(); ++i)
	                    {
	                          drawCar(canvas,enemyCars.get(i));
	                    }

	                    if (Carhit == true && gamepaused == false)
	                    {
	                        CarIntersected(canvas); 
	                    }
	                    else
	                    {
	                        drawCar(canvas, playersCar);
	                    }

	                    if (smokenFire.size() > 0)
	                    {
	                        for (int j = 0; j < smokenFire.size(); ++j)
	                        {
	                            drawSmoke(canvas, smokenFire.get(j));
	                        }

	                    }

	                   

	                    if (Defaultpad == 1)
	                    {
	                       
	                    	bigcircle.Draw(canvas, bCirclePosition, p);
                   
	                    	left.Draw(canvas, leftPosition, lPadColor);
	                    	right.Draw(canvas, rightPosition, rPadColor);
	                    	up.Draw(canvas, upPosition, uPadColor);
	                    	down.Draw(canvas, downPosition, dPadColor);
	                    	
	                    	 // lPadColor.setColor(Color.WHITE); rPadColor.setColor(Color.WHITE);
	                         // uPadColor.setColor(Color.WHITE); dPadColor.setColor(Color.WHITE);
	                    }

	                    //spriteBatch.Begin();
	                    //spriteBatch.Draw(scoreimage, iDscore, Color.White);//12
	                    //spriteBatch.Draw(speedimage, iDspeed, Color.White);//56
	                    //spriteBatch.End();
	                    scoreimage.Draw(canvas, iDscore);
	                    speedimage.Draw(canvas, iDspeed);
	                    
	                    Paint p2 = new Paint();
						p2.setColor(0xffff6600); //This set the color to orange
						if(deviceWidth < 320) p2.setTextSize(16);
						else if(deviceWidth >= 320 && deviceWidth < 800 ) p2.setTextSize(20);
						else if(deviceWidth >= 800 ) p2.setTextSize(28);
						else p2.setTextSize(18);
						
						canvas.drawText(score, iScore.dX,iScore.dY, p2);
						
						p2.setColor(0xffffff00); //This set the color to yellow
						
						canvas.drawText(CSpeed,iSpeed.dX,iSpeed.dY, p2);

	                    //spriteBatch.Begin();
	  //Attention need  //spriteBatch.DrawString(myfont, score, iScore, Color.Orange);//4
	                   // spriteBatch.DrawString(myfont, CSpeed, iSpeed, Color.Yellow);//49
	                   // spriteBatch.End();

	                    DrawLifes(canvas);

	                    if (gamepaused == true)
	                    {
	                        //spriteBatch.Begin();
	                        //spriteBatch.Draw(pausedbackground, new Rectangle(0, 0, viewportX, viewportY), Color.White * 0.8f);
	                       // spriteBatch.Draw(lblPaused, iPaused, Color.White);
	                        //spriteBatch.End(); 
	                    	pausedbackground.Draw(canvas, p);
	                    	lblPaused.Draw(canvas, iPaused);

	                       // spriteBatch.Begin(SpriteSortMode.BackToFront, BlendState.AlphaBlend);
	                        //spriteBatch.Draw(resume, resumeTextPosition, Color.White);
	                       // spriteBatch.Draw(restart, restartTextPosition, Color.White);
	                       // spriteBatch.Draw(SaveExit, SaveExitTextPosition, Color.White);
	                       // spriteBatch.End();
	                    	resume.Draw(canvas,resumeTextPosition);
	                    	restart.Draw(canvas,restartTextPosition);
	                    	SaveExit.Draw(canvas,SaveExitTextPosition);

	                        if (resumeTextPosition.dX > iWidth(75)) resumeTextPosition.dX -= iWidth(18);
	                        if (restartTextPosition.dX > iWidth(75)) restartTextPosition.dX -= iWidth(15);
	                        if (SaveExitTextPosition.dX > iWidth(75)) SaveExitTextPosition.dX -= iWidth(12);

	                    }
				 }
			}
			catch(Exception ex){
				ex.getMessage();
 				System.exit(0); 
			}
		}
 	}
 	 
}
