/**
 * @Project: 
 * @Title: SplashActivity.java
 * @Package com.cfuture.xiaozhi
 * @Description 开机动画类
 * @author xiaozhi(kaixinzhizhi@21cn.com)
 * @data 2012-3-31 下午08:04:39
 * @Copyright 2012 www.lurencun.com
 * @version v1.0 
 */
package com.cfuture.xiaozhi.weiba.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private ImageView splashImgView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);
		splashImgView = (ImageView) findViewById(R.id.splashImgView);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				showImg(0.2f, R.drawable.cfuture_logo_320dpi);
				Toast.makeText(SplashActivity.this, "正在载入程序", Toast.LENGTH_LONG)
						.show();
			}
		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				startActivity(new Intent(SplashActivity.this,
						WeiboweibaActivity.class));
				finish();
			}
		}, 2000);

	}

	private void showImg(float startAlpha, int drawableId) {
		splashImgView.setImageDrawable(getResources().getDrawable(drawableId));
		AlphaAnimation animation = new AlphaAnimation(startAlpha, 1.0f);
		animation.setDuration(1000);
		splashImgView.startAnimation(animation);
	}

}
