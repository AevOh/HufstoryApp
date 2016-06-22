package co.kr.hufstory.main;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import co.kr.hufstory.R;
import co.kr.hufstory.Util.WebChromeFileLoadClient;

/**
 * Created by Hyeong Wook on 2016-03-08.
 */
public class WebViewManager {
    private MainActivity mActivity;
    private FrameLayout mContainer;
    private WebView mWebView;
    private LayoutInflater mInflater;
    private View mView;
    private boolean mOnWebView;
    private boolean mOnFragment;

    public WebViewManager(int webViewId, MainActivity mainActivity, FrameLayout webViewContainer, WebChromeFileLoadClient fileLoadClient){
        mActivity = mainActivity;
        mContainer = webViewContainer;

        mWebView = initialWebView(webViewId);
        mWebView.setWebChromeClient(fileLoadClient);
    }

    private WebView initialWebView(int id) {
        mInflater = LayoutInflater.from(mActivity.getBaseContext());
        mView = mInflater.inflate(R.layout.webview, null, false);

        WebView webView = (WebView)mView.findViewById(id);
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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        return webView;
    }

    public void startWebView(String url){
        mActivity.onWebViewTrigger();
        mOnWebView = true;
        mOnFragment = false;

        mContainer.removeView(mWebView);
        mWebView.loadUrl(url);

        mContainer.addView(mWebView);

    }

    public void endWebView(){
        mOnWebView = false;
        mOnFragment = true;

        mContainer.removeView(mWebView);
    }

    public void returnLastWebView(){
        mOnWebView = true;
        mOnFragment = false;

        mActivity.onWebViewTrigger();
        mContainer.removeView(mWebView);
        mContainer.addView(mWebView);
    }

    public void goBackWebViewToHome(){
        mOnWebView = true;
        mOnFragment = false;

        mActivity.onWebViewTrigger();
        mContainer.removeView(mWebView);

        while(mWebView.canGoBack())
            mWebView.goBack();

        mContainer.addView(mWebView);
    }

    public void webViewbackAction(){
        if(mWebView.canGoBack())
            mWebView.goBack();
        else {
            mOnWebView = false;
            mActivity.onBackPressed();
        }
    }

    public boolean onWebView(){
        return mOnWebView;
    }

    public boolean onFragment(){
        return mOnFragment;
    }

    public WebView getWebView(){
        return mWebView;
    }
}
