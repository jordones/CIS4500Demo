package ca.jordonsmith.cis4500demo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class UploadService extends IntentService {

    private static final String ACTION_UPLOAD = "ca.jordonsmith.cis4500demo.action.UPLOAD";

    private static final String TEXT = "ca.jordonsmith.cis4500demo.extra.TEXT";

    public UploadService() {
        super("UploadService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpload(Context context, String text) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_UPLOAD);
        intent.putExtra(TEXT, text);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD.equals(action)) {
                final String text = intent.getStringExtra(TEXT);
                handleActionUpload(text);
            }
        }
    }

    /**
     * Handle action to upload in the provided background thread with the provided
     * parameters.
     * Networking code from post at:
     * https://www.wikihow.com/Execute-HTTP-POST-Requests-in-Android
     */
    private void handleActionUpload(String text) {

        /* Make HTTP Post request to /dev/null */
        URL url = null;
        int httpResponseCode = 0;

        try {
            url = new URL("http://devnull-as-a-service.com/dev/null");
        } catch (MalformedURLException e) {
            broadcast("Malformed URL Exception");
            e.printStackTrace();
            return;
        }

        HttpURLConnection client = null;

        try {
            client = (HttpURLConnection) url.openConnection();
            client.setConnectTimeout(2000);
            client.setReadTimeout(2000);
            client.setRequestMethod("POST");
            client.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            outputPost.write(text.getBytes());
            outputPost.flush();
            outputPost.close();

            httpResponseCode = client.getResponseCode();


        } catch(MalformedURLException error) {
            //Handles an incorrectly entered URL
            broadcast("Malformed URL Exception");
            error.printStackTrace();
        }
        catch(SocketTimeoutException error) {
            //Handles URL access timeout.
            broadcast("Socket Timeout Exception");
            error.printStackTrace();
        }
        catch (IOException error) {
            //Handles input and output errors
            broadcast("IO Exception");
            error.printStackTrace();
        }
        finally {
            if (client != null) {
                if (httpResponseCode == 200) {
                    // Text upload was successful, do broadcast receiver things
                    broadcast("Upload complete with status: " + httpResponseCode);
                }
                client.disconnect();
            }
        }
    }

    /*
    * Broadcasts message for our Broadcast Receiver to pick up.
    * */
    private void broadcast(String message) {

        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(UploadActivity.ACTION_UPLOAD_COMPLETE);

        broadCastIntent.putExtra("message", message);

        sendBroadcast(broadCastIntent);
    }

}
