package com.example.musicdroid;

import android.os.Bundle;
import android.view.Menu;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;


public class MusicDroidActivity extends ListActivity 
{

	/* Variable Declaration */
	private int mCurTime = 0;	
	//private static final String MEDIA_PATH = new String("/sdcard/");
	private static final String MEDIA_PATH ="/sdcard/";
	private List<String> mSongs = new ArrayList<String>();
	private MediaPlayer mMediaPlayer = new MediaPlayer();
	private int mSongIndex = 0;
	private int mDuration = 0;
	private ImageButton mPlay;
	private ImageButton mPause;
	private ImageButton mNext;
	private ImageButton mPrevious;
	private ImageButton mStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mPrevious = (ImageButton)findViewById(R.id.ImageButton01);
        mPlay = (ImageButton)findViewById(R.id.ImageButton02);
        mNext = (ImageButton)findViewById(R.id.ImageButton04);
        mPause = (ImageButton)findViewById(R.id.ImageButton03);
        mStop = (ImageButton)findViewById(R.id.ImageButton05);
        mPrevious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				prevSong();
			}
		});
        mPlay.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				playSong(mSongIndex, mCurTime);
			}
		});
        mPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCurTime = getCurrentDuration(mSongIndex);			  
				mMediaPlayer.pause();
			}
		});
        mNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nextSong();
			}
		});
        mStop.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mMediaPlayer.stop();
				mMediaPlayer.reset();
				mCurTime=0;
			}
		});

	}

	 public void onResume(){
			super.onResume();				
			updateSongList();
			ArrayAdapter<String> songList = new ArrayAdapter<String>(this,R.layout.songlist, mSongs);
			setListAdapter(songList);
		}
		
		public void updateSongList() {
			File home = new File(MEDIA_PATH);
			if (home.listFiles(new Mp3Filter()).length > 0) {
				for (File file : home.listFiles(new Mp3Filter())) {
					mSongs.add(file.getName());
				}									
			}
		}

		protected void onListItemClick(ListView l, View v, int position, long id) {		
			playSong(position, 0);
		}
			
		public void nextSong() {
			if (++mSongIndex >= mSongs.size())
				mSongIndex = 0;
				playSong(mSongIndex,0);
		}

		public void prevSong() {
			mSongIndex--;
			if (mSongIndex > 0)
				playSong(mSongIndex,0);
			else {
				mSongIndex = 0;
				playSong(mSongIndex,0);
			}
		}
		public int getSongDuration(int index)
		{	
			mDuration=mMediaPlayer.getDuration();
			Log.v("Song Duration", String.valueOf(mDuration));
			return mDuration;
		}
		public int getCurrentDuration(int index){
			int current_position;
			current_position=mMediaPlayer.getCurrentPosition();
			Log.v("Current Dur:",String.valueOf(current_position));
			return current_position;
		}
		
		class Mp3Filter implements FilenameFilter {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".mp3"));
			}
		}
		public void playSong(final int position, final int cur_pos) {
			try {			          
				mMediaPlayer.reset();			
				mMediaPlayer.setDataSource(MEDIA_PATH + mSongs.get(position));	
				mMediaPlayer.prepare();	
				mMediaPlayer.seekTo(cur_pos);	
				mMediaPlayer.start();	
				mDuration=position;
			    mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			    	public void onCompletion(MediaPlayer arg0) {
						nextSong();
					}
				});			
			} catch (IOException e) {
				Log.v(getString(R.string.app_name), e.getMessage());
			}
		}
		public void onStop(){
			super.onStop();
			mMediaPlayer.stop();
			mMediaPlayer.release();
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_droid, menu);
		return true;
	}

}
