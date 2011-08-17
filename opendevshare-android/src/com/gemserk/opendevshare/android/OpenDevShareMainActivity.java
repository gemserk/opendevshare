package com.gemserk.opendevshare.android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OpenDevShareMainActivity extends Activity {
	private DownloadManager dm;
	private String packageName = "com.gemserk.games.superflyingthing";
	private Button buttonlatest;
	private Button buttonstable;
	private Button buttonmarket;
	private Button buttonUninstall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		buttonlatest = (Button) findViewById(R.id.button_latest);
		buttonlatest.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				OpenDevShareMainActivity.this.downloadAndInstallAPK("http://www.gemserk.com/private/prototipos/superflyingthing-latest/superflyingthing-android.apk");
			}
		});

		buttonstable = (Button) findViewById(R.id.button_stable);
		buttonstable.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				OpenDevShareMainActivity.this.downloadAndInstallAPK("http://www.gemserk.com/private/prototipos/superflyingthing-release/superflyingthing-android.apk");
			}
		});

		buttonmarket = (Button) findViewById(R.id.button_market);
		buttonmarket.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent goToMarket = null;
				goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.gemserk.games.superflyingthing"));
				startActivity(goToMarket);
			}
		});

		buttonUninstall = (Button) findViewById(R.id.button_uninstall);
		buttonUninstall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri uninstallUri = Uri.fromParts("package", packageName, null);
				Intent intent = new Intent(Intent.ACTION_DELETE, uninstallUri);
				startActivity(intent);
			}
		});

	}

	private boolean isPackageInstalled(String packageName) {
		PackageManager pm = getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public void checkStatusOfButtons() {
		boolean app_installed = isPackageInstalled(packageName);
		buttonlatest.setEnabled(!app_installed);
		buttonstable.setEnabled(!app_installed);
		buttonmarket.setEnabled(!app_installed);
		buttonUninstall.setEnabled(app_installed);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkStatusOfButtons();
	}

	public void downloadAndInstallAPK(String url) {

		String packageName = this.getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File appFiles = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
		appFiles.mkdirs();

		final String finalApkFile = new File(appFiles, "/downloadedapk.apk").getAbsolutePath();
		DownloadManager downloadManager = new DownloadManager(this) {
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
