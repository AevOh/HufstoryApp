package co.kr.hufstory.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;

/**
 * Created by Hyeong Wook on 2016-03-03.
 */
public class WebFileLoadChromeClient extends WebChromeClient {
    private AppCompatActivity mActivity;
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mFilePathCallback;

    // activity require
    public WebFileLoadChromeClient(AppCompatActivity activity){
        mActivity = activity;
    }

    // VERSION 2.2+
    public void openFileChooser(ValueCallback<Uri> uploadMsg){
        Log.i("fileChoose", "2.2");
        mUploadMsg = uploadMsg;
        startFileOpeningIntent(mActivity);
     }

    // VERSION 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadFile, String accecptType){
        openFileChooser(uploadFile);
    }

    // VERSION 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadFile, String accecptType, String capture){
        openFileChooser(uploadFile);
    }

    // VERSION 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams){
        mFilePathCallback = filePathCallback;
        startFileOpeningIntent(mActivity, fileChooserParams);
        return true;
    }

    // less than API 21
    private void startFileOpeningIntent(AppCompatActivity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(Intent.createChooser(intent, "File Chooser"), MainActivity.S_RC_FILE_CHOOSE);
    }

    // more API 21
    private void startFileOpeningIntent(AppCompatActivity activity, WebChromeClient.FileChooserParams fileChooserParams){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Intent intent = fileChooserParams.createIntent();
                activity.startActivityForResult(intent, MainActivity.S_RC_FILE_CHOOSE);
            } catch (NullPointerException e) {
                Log.e("Error", "intent is null");
            }
        }
    }

    public ValueCallback<Uri> getUploadMsg(){
        return mUploadMsg;
    }

    public ValueCallback<Uri[]> getFilePathCallback(){
        return mFilePathCallback;
    }
}