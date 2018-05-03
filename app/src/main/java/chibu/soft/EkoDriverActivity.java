package chibu.soft;



import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
//import android.hardware.Sensor;
//import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import com.google.ads.*;
 


public class EkoDriverActivity extends Activity  {
	
	 public boolean mBackPressed = false;
	 public static boolean getHighScore = false;
	 //public static String highScoreName;
	 
	 //private AdView adView;
	
	// public EditText input = null;
	// public AlertDialog.Builder dialog = null;
	 
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // requesting to turn the title OFF
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        // making it full screen
	        
	        //This is used to force the screen to be in portrait
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	 		 
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // set our MainGamePanel as the View
	        setContentView(new EkoDriverView(this,null)); //Same name with your view class
	        
	        
	      /*  input = new EditText(this);
	        dialog = new AlertDialog.Builder(this);
	        dialog.setTitle("Enter High Score!");
	        dialog.setMessage("Enter your name: ");
	        dialog.setView(input);
	        dialog.setPositiveButton("OK", EkoDriverActivity.this);*/
	        
	   

//	        adView = new AdView(this, AdSize.BANNER, "E83D20734F72FB3108F104ABC0FFC738");
//	       	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//	       	    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//	       	lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//	       	adView.setLayoutParams(lp);
//
//	       	RelativeLayout layout = new RelativeLayout(this);
//	       	layout.addView(new EkoDriverView(this,null));
//	       	
//	       	
//	       	layout.addView(adView);
//	       	adView.loadAd(new AdRequest());
//
//	       	setContentView(layout);
	       	
	       	
	       //	layout = new LinearLayout(getContext());
//	        adView = new AdView(this, AdSize.BANNER, "E83D20734F72FB3108F104ABC0FFC738");
//	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//		       	    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//	    	lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//	       	adView.setLayoutParams(lp);
//	       	
//	     	RelativeLayout layout = new RelativeLayout(this);
//	       	layout.addView(new EkoDriverView(this,null));

	   /*     
	        // Create the adView
	        adView = new AdView(this, AdSize.BANNER, "E83D20734F72FB3108F104ABC0FFC738");
	        
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
       	    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
   	        adView.setLayoutParams(lp);
   	        
   	        RelativeLayout layout = new RelativeLayout(this);
	       	layout.addView(new EkoDriverView(this,null));


	        // Initiate a generic request to load it with an ad
	        adView.loadAd(new AdRequest());*/

	       // adview = (AdView)this.findViewById(R.id.adView);
	       // adView.loadAd(new AdRequest());

	        
	        EkoDriverView.settings = this.getPreferences(0);
	        
	        }
	    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            	mBackPressed = true;
            	
            	EkoDriverView.backbuttonpressed = true;
            	
                break;
           
            }
        }
       return true;
       // return super.onKeyDown(keyCode, event);
    }
    

    
  /*  @Override
    protected Dialog onCreateDialog(int id) {

        return new AlertDialog.Builder(EkoDriverActivity.this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Title")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
             		 highScoreName = input.getText().toString();
             	    // Do something with value!
             	    }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
        }*/
}