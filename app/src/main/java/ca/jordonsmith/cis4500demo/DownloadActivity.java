package ca.jordonsmith.cis4500demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DownloadActivity extends AppCompatActivity {


    private DownloadReceiver downloadReceiver;

    // Unique identifier for the broadcast sent when uploading is done
    public static final String ACTION_DOWNLOAD_COMPLETE = "ca.jordonsmith.cis4500demo.broadcast.UPLOAD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

    }

    // Ran when the app comes into the foreground after being hidden
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DOWNLOAD_COMPLETE);
        downloadReceiver = new DownloadReceiver();
        registerReceiver(downloadReceiver, intentFilter);
    }

    // Ran when an app goes into the background and is hidden
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(downloadReceiver);
    }


    public void onDownloadClicked(View view) {

        DownloadService.startActionDownload(this);
    }

    public void onShowClicked(View view) {
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(DownloadProvider.CONTENT_URI + "readme.md")));md
//        finish();
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(Uri.parse(DownloadProvider.CONTENT_URI + "readme.md"));
//            Toast.makeText(this, inputStream.read(), Toast.LENGTH_SHORT).show();
            System.out.println(inputStream.read());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
