package ca.jordonsmith.cis4500demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    /*
    * Ran when the download button is clicked.
    * Starts the download service
    * */
    public void onDownloadClicked(View view) {

        DownloadService.startActionDownload(this);
    }


    /*
    *  Ran when the user clicks on the "show downloaded content" button on screen
    *  This method serves as a means to demonstrate that the file was read from
    *  using the Content Provider that we've set up.
    * */
    public void onShowClicked(View view) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(Uri.parse(DownloadProvider.CONTENT_URI + "readme.md"));

            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream)
            );
            String contentProviderFile = bufferedReader.readLine();

            while (contentProviderFile != null) {
                stringBuffer.append(contentProviderFile).append("\n");
                contentProviderFile = bufferedReader.readLine();
            }

            Toast.makeText(this, "Text via content provider:\n\t" + stringBuffer.toString(), Toast.LENGTH_SHORT).show();
            System.out.println(stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
