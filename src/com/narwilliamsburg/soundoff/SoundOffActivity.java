package com.narwilliamsburg.soundoff;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SoundOffActivity extends Activity {

	private Button soundOffButton;
	private boolean isSoundOffButtonPressed = false;
	private TextView lengthTextView;
	private int lengthIndex = 1;
	private List<String> lengths = new ArrayList<>();
	private static final int NUM_LENGTHS = 25;
	
	public SoundOffActivity() {
		super();
		initializeLengths();
	}
	
	private void initializeLengths() {
		for (int i = 0; i < NUM_LENGTHS; i++) {
			if (i == 0) {
				this.lengths.add("Indefinite");
			}
			else {
				StringBuilder sb = new StringBuilder();
				sb.append(i);
				sb.append(" hour");
				if (i != 1) {
					sb.append("s");
				}
				this.lengths.add(sb.toString());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_off);

		this.soundOffButton = (Button) findViewById(R.id.soundOffButton);
		this.soundOffButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickSoundOffButton(v);
			}
		});
		
		this.lengthTextView = (TextView) findViewById(R.id.textViewLength);
		resetLength();
	}
	
	public void onClickSoundOffButton(final View view) {
		this.isSoundOffButtonPressed = !this.isSoundOffButtonPressed;
		final AudioManager audioManager = (AudioManager) view
					.getContext()
					.getSystemService(Context.AUDIO_SERVICE);
		setAllStreamMutes(this.isSoundOffButtonPressed, audioManager);
		toggleSoundOffButtonText();
		if (this.lengthIndex == 0) {
			return;
		}
		Handler handler = new Handler();
		Runnable finished = new Runnable() {
			
			@Override
			public void run() {
					resetLength();
					onClickSoundOffButton(view);
				}
		};
		handler.postDelayed(
				finished,
				TimeUnit.HOURS.toMillis(this.lengthIndex));
	}
	
	private void setAllStreamMutes(boolean shouldMute, AudioManager audioManager) {
		if (shouldMute) {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		else {
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
		// TODO unclear if alarm is actually muting. It doesn't work on emulator
		audioManager.setStreamMute(AudioManager.STREAM_ALARM, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_DTMF, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_RING, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, shouldMute);
		audioManager.setStreamMute(AudioManager.STREAM_ALARM, shouldMute);
		
		// We can't do this on API 8
		//if (audioManager.isVolumeFixed()) {
			//return;
		//}
	}
	
	private void toggleSoundOffButtonText() {
		if (this.isSoundOffButtonPressed) {
			this.soundOffButton.setText(R.string.sound_on);
		}
		else {
			this.soundOffButton.setText(R.string.sound_off);
		}
	}
	
	private void resetLength() {
		this.lengthIndex = 1;
		this.lengthTextView.setText(this.lengths.get(this.lengthIndex));
	}
	
	public void incrementLengthIndex(View v) {
		if (this.lengthIndex < NUM_LENGTHS - 1) {
			this.lengthIndex++;
		}
		else {
			this.lengthIndex = 0;
		}
		this.lengthTextView.setText(this.lengths.get(this.lengthIndex));
	}
	
	public void decrementLengthIndex(View v) {
		if (this.lengthIndex > 0) {
			this.lengthIndex--;
		}
		else {
			this.lengthIndex = NUM_LENGTHS - 1;
		}
		this.lengthTextView.setText(this.lengths.get(this.lengthIndex));
	}
}