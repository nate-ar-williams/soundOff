package com.narwilliamsburg.soundoff;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoundOffActivity extends Activity {

	private Button soundOffButton;
	private boolean isSoundOffButtonPressed = false;

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
	}
	
	public void onClickSoundOffButton(View view) {
		SoundOffActivity.this.isSoundOffButtonPressed = !SoundOffActivity.this.isSoundOffButtonPressed;
		AudioManager audioManager = (AudioManager) view
					.getContext()
					.getSystemService(Context.AUDIO_SERVICE);
		setAllStreamMutes(this.isSoundOffButtonPressed, audioManager);
		toggleButtonText();
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
		
		// We can't do this on API 8
		//if (audioManager.isVolumeFixed()) {
			//return;
		//}
	}
	
	private void toggleButtonText() {
		if (this.isSoundOffButtonPressed) {
			this.soundOffButton.setText(R.string.sound_on);
		}
		else {
			this.soundOffButton.setText(R.string.sound_off);
		}
	}
}
