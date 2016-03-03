package com.newgame.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.newgame.sdk.AndroidShare;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				AndroidShare as = new AndroidShare(
						MainActivity.this,
						"����---������ķ�����������allen",
						"http://img6.cache.netease.com/cnews/news2012/img/logo_news.png");
				as.show();
			}
		});

	}

}
