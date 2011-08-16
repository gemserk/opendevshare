package com.gemserk.opendevshare.android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OpenDevShareMainActivity extends Activity {
	private long enqueue;
	private DownloadManager dm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button buttonlatest = (Button) findViewById(R.id.button_latest);
		buttonlatest.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				OpenDevShareMainActivity.this.downloadAndInstallAPK("http://www.gemserk.com/private/prototipos/superflyingthing-latest/superflyingthing-android.apk");
			}
		});
		Button buttonstable = (Button) findViewById(R.id.button_stable);
		buttonstable.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				OpenDevShareMainActivity.this.downloadAndInstallAPK("http://www.gemserk.com/private/prototipos/superflyingthing-release/superflyingthing-android.apk");
			}
		});
	}

	
	public void downloadAndInstallAPK(String url) {
		File externalFilesDir = getExternalFilesDir(null);
		final String finalApkFile = externalFilesDir.getAbsolutePath() + "/downloadedapk.apk";
		 DownloadManager downloadManager = new DownloadManager(this){
			@Override
			protected void onPostExecute(Drawable result) {
				super.onPostExecute(result);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(finalApkFile)), "application/vnd.android.package-archive");
				startActivity(intent);  
			} 
		 };
		
		downloadManager.execute("http://www.gemserk.com/private/prototipos/superflyingthing-latest/superflyingthing-android.apk", finalApkFile);
	}
}
