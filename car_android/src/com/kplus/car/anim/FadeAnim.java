package com.kplus.car.anim;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class FadeAnim {

	public static void fadeVisible(int duration, int startOffset, int step, View... views) {
		for (int i = 0; i < views.length; i++) {
			AlphaAnimation anim = new AlphaAnimation(0, 1);
			anim.setDuration(duration);
			anim.setStartOffset(startOffset + step * i);
			views[i].setVisibility(View.VISIBLE);
			views[i].startAnimation(anim);
		}
	}

	public static void fadeVisible(int duration, int startOffset, View... views) {
		fadeVisible(duration, startOffset, 0, views);
	}

	public static void fadeVisible(int duration, View... views) {
		fadeVisible(duration, 0, views);
	}

	public static void fadeVisible(View... views) {
		fadeVisible(300, views);
	}

	public static void fadeInvisible(int duration, int startOffset, int step, View... views) {
		for (int i = 0; i < views.length; i++) {
			AlphaAnimation anim = new AlphaAnimation(1, 0);
			anim.setDuration(duration);
			anim.setStartOffset(startOffset + step * i);
			views[i].setVisibility(View.INVISIBLE);
			views[i].startAnimation(anim);
		}
	}

	public static void fadeInvisible(int duration, int startOffset, View... views) {
		fadeInvisible(duration, startOffset, 0, views);
	}

	public static void fadeInvisible(int duration, View... views) {
		fadeInvisible(duration, 0, views);
	}

	public static void fadeInvisible(View... views) {
		fadeInvisible(300, views);
	}

	public static void fadeGone(int duration, int startOffset, int step, View... views) {
		for (int i = 0; i < views.length; i++) {
			final View view = views[i];
			AlphaAnimation anim = new AlphaAnimation(1, 0);
			anim.setDuration(duration);
			anim.setStartOffset(startOffset + step * i);
			anim.setAnimationListener(new OnAnimEndListener() {
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.GONE);
				}
			});
			view.setVisibility(View.VISIBLE);
			view.startAnimation(anim);
		}
	}

	public static void fadeGone(int duration, int startOffset, View... views) {
		fadeGone(duration, startOffset, 0, views);
	}

	public static void fadeGone(int duration, View... views) {
		fadeGone(duration, 0, views);
	}

	public static void fadeGone(View... views) {
		fadeGone(300, views);
	}

}
