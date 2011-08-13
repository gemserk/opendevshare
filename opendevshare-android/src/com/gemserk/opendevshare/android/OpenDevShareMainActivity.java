package com.gemserk.opendevshare.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class OpenDevShareMainActivity extends Activity {

	private LinearLayout page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

}
