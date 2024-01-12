package kr.co.goms.app.estimate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
                webView.loadUrl("javascript:execDaumPostCode();");
            }
        });

        webView.loadUrl("http://app.goms.co.kr/101.api/address.php");

    }


}