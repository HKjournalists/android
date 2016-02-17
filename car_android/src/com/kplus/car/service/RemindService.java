package com.kplus.car.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

public class RemindService extends IntentService
{
	public RemindService()
	{
		super("com.kplus.car.service.VoiceVibratorService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		float streamMaxVolumeNotice = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		float streamVolumeNotice = am.getStreamVolume(AudioManager.STREAM_RING);
		float volumeRing = streamVolumeNotice/streamMaxVolumeNotice;
		try{
			MediaPlayer mMediaPlayer = MediaPlayer.create(RemindService.this, RingtoneManager.getActualDefaultRingtoneUri(RemindService.this, RingtoneManager.TYPE_NOTIFICATION));
			mMediaPlayer.setLooping(false);
			mMediaPlayer.setVolume(volumeRing, volumeRing);
			mMediaPlayer.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
