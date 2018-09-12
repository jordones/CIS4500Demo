package ca.jordonsmith.cis4500demo;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadService extends IntentService {

    private static final String ACTION_DOWNLOAD = "ca.jordonsmith.cis4500demo.action.DOWNLOAD";

    public DownloadService() {
        super("DownloadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDownload(Context context) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                handleActionDownload();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     * https://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
     */
    private void handleActionDownload() {

        try {
            String result = download();
            writeResultToFile(result);
            broadcast(result);
        } catch (IOException e) {
            broadcast(e.getMessage());
        }
    }

    private void broadcast(String message) {

        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(DownloadActivity.ACTION_DOWNLOAD_COMPLETE);

        broadCastIntent.putExtra("message", message);

        sendBroadcast(broadCastIntent);
    }

    public static String download() throws IOException {
        /* Make HTTP GET request to download the readme */
        URL url = null;
        StringBuffer stringBuffer = new StringBuffer();

        url = new URL(
                "https://raw.githubusercontent.com/jordones/CIS4500Demo/master/README.md"
        );

        HttpURLConnection client = null;

        client = (HttpURLConnection) url.openConnection();
        client.setInstanceFollowRedirects(true);
        client.setConnectTimeout(2000);
        client.setReadTimeout(2000);
        client.setRequestMethod("GET");

        BufferedReader inputStream = new BufferedReader(
            new InputStreamReader(client.getInputStream())
        );

        // Read response data from the web url
        String webOutput = inputStream.readLine();

        while(webOutput != null) {
            stringBuffer.append(webOutput).append("\n");
            webOutput = inputStream.readLine();
        }

        client.disconnect();

        return stringBuffer.toString();

    }

    private void writeResultToFile(String result) {
        try {

            FileOutputStream outputStreamWriter = openFileOutput("readme.md", Context.MODE_PRIVATE);
            outputStreamWriter.write(result.getBytes());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            broadcast(e.getMessage());
        }
    }
}
