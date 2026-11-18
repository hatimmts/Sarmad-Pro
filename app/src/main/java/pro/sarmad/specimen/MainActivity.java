package pro.sarmad.specimen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

/** Native Android shell for the audited, offline specimen workflow. */
final class MainActivityJavaLegacy extends Activity {
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
        Button print = new Button(this);
        print.setText("Print specimen over Wi‑Fi…");
        print.setOnClickListener(ignored -> {
            PrintManager manager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            manager.print("Sarmad specimen", view.createPrintDocumentAdapter("Sarmad specimen"),
                    new PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4).build());
        });
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(print, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        setContentView(root);
    }
}
