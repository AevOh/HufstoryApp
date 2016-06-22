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
public class WebChromeFileLoadClient extends android.webkit.WebChromeClient {

    private AppCompatActivity mActivity;
    private ValueCallback<Uri> mUploadMsg = null;
    private ValueCallback<Uri[]> mFilePathCallback = null;
    private int mResultSerial;

    // activity require
    public WebChromeFileLoadClient(AppCompatActivity activity, int resultSerial){
        mActivity = activity;
        mResultSerial = resultSerial;
    }

    // VERSION 2.2+
    public void openFileChooser(ValueCallback<Uri> uploadMsg){
        startFileOpeningIntent(mActivity);
        mUploadMsg = uploadMsg;
     }

    // VERSION 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadFile, String accecptType){
        openFileChooser(uploadFile);
    }

    // VERSION 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadFile, String accecptType, String capture){
        openFileChooser(uploadFile);
    }

    public void getUploadedRCFile(int resultCode, Intent data){
        Uri result = null;

        if (data != null || resultCode == mActivity.RESULT_OK)
            result = data.getData();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && mUploadMsg != null) {
            mUploadMsg.onReceiveValue(result);
            mUploadMsg = null;

        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mFilePathCallback != null){
            mFilePathCallback.onReceiveValue(android.webkit.WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mFilePathCallback = null;
        }
    }

    // VERSION 5.0+
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams){
        startFileOpeningIntent(mActivity, fileChooserParams);
        mFilePathCallback = filePathCallback;
        return true;
    }

    // less than API 21
    private void startFileOpeningIntent(AppCompatActivity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(Intent.createChooser(intent, "File Chooser"), mResultSerial);
    }

    // more API 21
    private void startFileOpeningIntent(AppCompatActivity activity, android.webkit.WebChromeClient.FileChooserParams fileChooserParams){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Intent intent = fileChooserParams.createIntent();
                activity.startActivityForResult(intent, mResultSerial);
            } catch (NullPointerException e) {
                Log.e("Error", "intent is null");
            }
        }
    }
}