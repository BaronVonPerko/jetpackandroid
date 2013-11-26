package com.chrisperko.androidgameframework;

import com.chrisperko.androidgameframework.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
	public static final String SAVE_FILE = "SaveFile";
	public GamePanel game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Restore saved data
    	SharedPreferences file = getSharedPreferences(SAVE_FILE, 0);
    	int money = file.getInt("money", 0);
        
        // Don't show title at top of the screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // force landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
        
        // hide the notification / battery / signal / etc bar
        getWindow().setFlags(	WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 	WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        game = new GamePanel(this);
        setContentView(game);
        game.setMoney(money);
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	SharedPreferences file = getSharedPreferences(SAVE_FILE, 0);
    	SharedPreferences.Editor editor = file.edit();
    	editor.putInt("money", game.getMoney());
    	editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
//    @Override
//    public void onBackPressed() {  }
}
