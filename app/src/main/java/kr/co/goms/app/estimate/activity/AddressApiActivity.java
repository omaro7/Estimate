package kr.co.goms.app.estimate.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import kr.co.goms.app.estimate.BuildConfig;
import kr.co.goms.app.estimate.R;
import kr.co.goms.module.common.activity.CustomActivity;

public class AddressApiActivity extends CustomActivity {

    private WebView webView;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String postcode, String address) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("postcode", postcode);
            extra.putString("address", address);
            intent.putExtras(extra);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_api);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        webView.setBackgroundColor(getResources().getColor(kr.co.goms.module.common.R.color.transparent, null));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //webView.loadUrl("javascript:execDaumPostCode();");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                // Handle the JavaScript alert here
                // You can display a dialog or perform any custom action
                // For example, showing an AlertDialog:
                new AlertDialog.Builder(AddressApiActivity.this)
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> result.confirm())
                        .setCancelable(false)
                        .show();

                // Return true to indicate that you've handled the alert
                return true;
            }
        });

        // The data you want to send in the request body
        byte[] value = Base64.encode(BuildConfig.APPLICATION_ID.getBytes(), Base64.DEFAULT);   //kr.co.goms.app.estimate
        String postData = "key=" + new String(value);

        Log.d("POST", "postData : " + postData);

        // Convert the data to bytes
        byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
        webView.postUrl(BuildConfig.ADDRESS_SERVER_URL, postDataBytes);

    }


}