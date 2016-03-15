package co.kr.hufstory.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import java.util.zip.Inflater;

import co.kr.hufstory.R;

/**
 * Created by Hyeong Wook on 2016-03-08.
 */
public class WebViewManager {
    private AppCompatActivity mActivity;
    private FrameLayout mFrameLayout;
    private WebView mWebview;
    private LayoutInflater mInflater;
    private View mView;
    private WebFileLoadChromeClient mWebFileLoadChromeClient;
    private boolean mOnWebView;

    public WebViewManager(WebView webView, AppCompatActivity mainActivity){
        mWebview = webView;
        mActivity = mainActivity;
    }

    public boolean launchWebView(){
        mOnWebView = true;
        return false; // webView가 띄워짐으로써 꺼지는 것.
    }

    public boolean onWebView(){
        return mOnWebView;
    }

    private WebView initialWebView(int id){
        mInflater = LayoutInflater.from(mActivity.getBaseContext());
        mView = mInflater.inflate(R.layout.webview, null, false);
        mWebFileLoadChromeClient = new WebFileLoadChromeClient(mActivity);
        WebView webView;

        webView = (WebView)mView.findViewById(id);
        webView.setWebViewClient(new WebViewClient() {
            // momo enable calling
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent calling = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    mActivity.startActivity(calling);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(mWebFileLoadChromeClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        return webView;
    }
}
