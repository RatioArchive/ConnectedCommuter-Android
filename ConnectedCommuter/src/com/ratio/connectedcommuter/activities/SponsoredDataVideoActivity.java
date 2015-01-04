package com.ratio.connectedcommuter.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.MediaController.MediaPlayerControl;

import com.datami.smi.SmiResult;
import com.datami.smi.SmiSdk;
import com.ratio.connectedcommuter.R;

public class SponsoredDataVideoActivity extends Activity implements MediaPlayerControl, OnCompletionListener {
	Boolean demo = true;
	SponsoredDataVideoActivity rootActivity;
	String TAG = "BLAHBLAH";
	String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
	String userId = "testUser";
	String appId = "com.ratio.connectedcommuter";
	int connectionType = 0;
	String demoVideoPath;
	
	VideoView sponsoredVideo;
	MediaController mController;
	
	Uri videoUri;
	Uri sponsoredUri;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v(TAG, "Test comment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsored_data_video);
        
        rootActivity = this;
        demoVideoPath = "android.resource://" + getPackageName() + "/" + R.raw.got_s04e10;
        if(demo) {
        	videoUri = Uri.parse(demoVideoPath);
        } else {
        	videoUri = Uri.parse(url);
        }
        
        SmiResult sr = null;
        int state = -1;
        try{
        	sr = SmiSdk.getSDAuth(this, url, userId, appId);
        	state  = sr.getState();
        } catch(Exception e) {
        	Log.v(TAG, "EXCEPTION");
        }
        Log.v(TAG, "state = " + state);
        ImageView sponsoredIV = (ImageView) findViewById(R.id.ivSponsored);
        sponsoredVideo = (VideoView)findViewById(R.id.testVideo);
        if(state == SmiResult.WIFI || state == -1) {
        	connectionType = 1;
        	sponsoredIV.setVisibility(View.GONE);
        	
        	mController = new MediaController(this);
        	sponsoredVideo.setVideoURI(videoUri);
        	mController.setMediaPlayer(sponsoredVideo);
        	sponsoredVideo.setMediaController(mController);
        	sponsoredVideo.setOnCompletionListener(this);
        	sponsoredVideo.start();
        }
        // get sponsored data url
        if(state == SmiResult.SD_AVAILABLE) {
        	Log.v(TAG, "SD_AVAILABLE");
        	connectionType = 2;
        	//1. use 'http://s3.amazonaws.com/sdmsg/sponsored/msg.png' logo to display sponsored message
            //2. use the 'sdUrl' for requesting the content
        	sponsoredIV.setVisibility(View.VISIBLE);
        	
        	mController = new MediaController(this);
        	
        	String sdUrl = "";
        	if(demo) {
        		sdUrl = demoVideoPath;
        	} else {
        		sdUrl = sr.getUrl();
        	}

        	sponsoredUri = Uri.parse(sdUrl);
        	sponsoredVideo.setVideoURI(sponsoredUri);
        	
        	mController.setMediaPlayer(sponsoredVideo);
        	sponsoredVideo.setMediaController(mController);
        	sponsoredVideo.setOnCompletionListener(this);
        	sponsoredVideo.start();
        } else if(state == SmiResult.SD_NOT_AVAILABLE) {
        	// non sponsored connection
        	Log.v(TAG, "SD_NOT_AVAILABLE");
        } else if(state == SmiResult.WIFI) {
        	// wifi connection
        } else {
        	Log.v(TAG, "Nothing");
        }
        
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        MyReceiver connectionReceiver = new MyReceiver();
        registerReceiver(connectionReceiver, filter);
    }

	@Override
	public void start() {
		sponsoredVideo.start();
	}

	@Override
	public void pause() {
		sponsoredVideo.pause();
	}

	@Override
	public int getDuration() {
		return sponsoredVideo.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return sponsoredVideo.getCurrentPosition();
	}

	@Override
	public void seekTo(int pos) {
		sponsoredVideo.seekTo(pos);
	}

	@Override
	public boolean isPlaying() {
		return sponsoredVideo.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		return sponsoredVideo.getBufferPercentage();
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return false;
	}

	@Override
	public boolean canSeekForward() {
		return false;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if ( !isFinishing() ) {
			finish();
		}
	}
	
	public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }
    
    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
        	if(isConnectedWifi(context) && connectionType != 1) {
        		ImageView sponsoredIV = (ImageView) findViewById(R.id.ivSponsored);
                sponsoredIV.setVisibility(View.GONE);
                String videoUrl = "";
                if(demo) {
                	videoUrl = demoVideoPath;
                }else {
                	videoUrl = url;
                }
                Uri uri = Uri.parse(videoUrl);
                sponsoredVideo.setVideoURI(uri);
                sponsoredVideo.start();
        	} else if(isConnectedMobile(context)) {
        		ImageView sponsoredIV = (ImageView) findViewById(R.id.ivSponsored);
                sponsoredVideo = (VideoView)findViewById(R.id.testVideo);
                SmiResult sr = null;
                int state = -1;
                try{
                	sr = SmiSdk.getSDAuth(rootActivity, url, userId, appId);
                	state  = sr.getState();
                } catch(Exception e) {
                	Log.v(TAG, "EXCEPTION");
                }
                if(state == SmiResult.SD_AVAILABLE) {
                	String sdUrl = "";
                	if(demo) {
                		sdUrl = demoVideoPath;
                	} else {
                		if(sr != null) {
                			sdUrl = sr.getUrl();
                		}
                	}
                	sponsoredIV.setVisibility(View.VISIBLE);
                	Uri uri = Uri.parse(sdUrl);
                    sponsoredVideo.setVideoURI(uri);
                    sponsoredVideo.start();
                }
        	} else {
        		//No internet - Do something at some point
        	}
        }

        public boolean isConnectedWifi(Context context){
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
        }

        public boolean isConnectedMobile(Context context){
            NetworkInfo info = getNetworkInfo(context);
            return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
        }
    }
}

