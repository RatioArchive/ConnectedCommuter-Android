package com.ratio.connectedcommuter.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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

	String TAG = "BLAHBLAH";
	String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
	String userId = "testUser";
	String appId = "com.example.samplesponsoreddata";
	
	VideoView sponsoredVideo;
	MediaController mController;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v(TAG, "Test comment");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SmiResult sr = SmiSdk.getSDAuth(this, url, userId, appId);
        int state  = sr.getState();
        Log.v(TAG, "state = " + state);
        // get sponsored data url
        String sdUrl = sr.getUrl();
        if(state == SmiResult.SD_AVAILABLE) {
        	Log.v(TAG, "SD_AVAILABLE");
        	//1. use 'http://s3.amazonaws.com/sdmsg/sponsored/msg.png' logo to display sponsored message
            //2. use the 'sdUrl' for requesting the content
        	ImageView sponsoredIV = (ImageView) findViewById(R.id.ivSponsored);
        	sponsoredIV.setVisibility(View.VISIBLE);
        	
        	mController = new MediaController(this);
        	
        	sponsoredVideo = (VideoView)findViewById(R.id.testVideo);
        	Uri videoUri = Uri.parse(sdUrl);
        	sponsoredVideo.setVideoURI(videoUri);
        	
        	
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
}

