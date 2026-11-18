package pro.sarmad.specimen;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** Native Android shell for the audited, offline specimen workflow. */
public final class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        WebView view = new WebView(this);
        view.setWebViewClient(new WebViewClient());
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true); // required for local-only controls and storage
        settings.setDomStorageEnabled(true);
        // Required for relative CSS/JS bundled beside specimen.html; no network permission is declared.
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        view.loadUrl("file:///android_asset/specimen.html");
        setContentView(view);
    }
}
