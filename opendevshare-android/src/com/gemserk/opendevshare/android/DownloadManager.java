package com.gemserk.opendevshare.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

public class DownloadManager extends AsyncTask<String, Integer, Drawable>
{

    private Drawable d;
    private HttpURLConnection conn;
    private InputStream stream; //to read
    private ByteArrayOutputStream out; //to write

    private double fileSize;
    private double downloaded; // number of bytes downloaded
    private int status = DOWNLOADING; //status of current process

    private ProgressDialog progressDialog;

    private static final int MAX_BUFFER_SIZE = 1024; //1kb
    private static final int DOWNLOADING = 0;
    private static final int COMPLETE = 1;
	private final Context context;

    public DownloadManager(Context context)
    {
        this.context = context;
		d          = null;
        conn       = null;
        fileSize   = 0;
        downloaded = 0;
        status     = DOWNLOADING;
    }

    public boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting(); 
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    protected Drawable doInBackground(String... url)
    {
        try
        {
            String filename = url[1];
            if (isOnline())
            {
                conn     = (HttpURLConnection) new URL(url[0]).openConnection();
                fileSize = conn.getContentLength();
                out      = new ByteArrayOutputStream((int)fileSize);
                conn.connect();
                System.out.println("Downloading "+ fileSize +  "bytes of "  + url[0]);
                stream = conn.getInputStream();
                // loop with step
                while (status == DOWNLOADING)
                {
                    byte buffer[];

                    if (fileSize - downloaded > MAX_BUFFER_SIZE)
                    {
                        buffer = new byte[MAX_BUFFER_SIZE];
                    }
                    else
                    {
                        buffer = new byte[(int) (fileSize - downloaded)];
                    }
                    int read = stream.read(buffer);

                    if (read == -1)
                    {
                        publishProgress(100);
                        break;
                    }
                    // writing to buffer
                    out.write(buffer, 0, read);
                    downloaded += read;
                    // update progress bar
                    publishProgress((int) ((downloaded / fileSize) * 100));
                } // end of while

                if (status == DOWNLOADING)
                {
                    status = COMPLETE;
                }
                try
                {
                    FileOutputStream fos = new FileOutputStream(filename);
                    fos.write(out.toByteArray());
                    fos.close();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                    return null;
                }

                d = Drawable.createFromStream((InputStream) new ByteArrayInputStream(out.toByteArray()), "filename");
                return d;
            } // end of if isOnline
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }// end of catch
    } // end of class DownloadManager()

    @Override
    protected void onProgressUpdate(Integer... changed)
    {
        progressDialog.setProgress(changed[0]);
    }

    @Override
    protected void onPreExecute()
    {
        progressDialog = new ProgressDialog(context); // your activity
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Downloading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Drawable result)
    {
        progressDialog.dismiss();
        // do something
    }
}
