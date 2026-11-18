package pro.sarmad.specimen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.ArrayAdapter
import java.io.IOException

/** Native shell: SAF selection and truthful system printing for safe specimens. */
class MainActivity : Activity() {
    private var selectedUri: Uri? = null
    private lateinit var selectedLabel: TextView
    private lateinit var printMode: Spinner

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val web = WebView(this).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            loadUrl("file:///android_asset/specimen.html")
        }
        selectedLabel = TextView(this).apply {
            text = "No local document selected (selection is not persisted or uploaded).\nPrinter duplex capability is decided by Android's system picker; unavailable duplex prints as simplex."
            setTextColor(Color.DKGRAY)
        }
        val import = Button(this).apply {
            text = "Import PDF/image/document (local SAF)"
            setOnClickListener { startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }, REQUEST_DOCUMENT) }
        }
        printMode = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Simplex", "Duplex Long Edge", "Duplex Short Edge"))
        }
        val print = Button(this).apply {
            text = "Print specimen over Wi‑Fi…"
            setOnClickListener { printSpecimen() }
        }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(import, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            addView(selectedLabel, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            addView(printMode, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            addView(print, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            addView(web, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        }
        setContentView(root)
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?) {
        super.onActivityResult(request, result, data)
        if (request == REQUEST_DOCUMENT && result == RESULT_OK) {
            selectedUri = data?.data
            selectedUri?.let { uri ->
                // Touch the stream locally to validate access; never upload or persist the URI.
                contentResolver.openAssetFileDescriptor(uri, "r")?.close()
                selectedLabel.text = "Selected locally: ${uri.lastPathSegment ?: uri} (${contentResolver.getType(uri) ?: "unknown"})"
            } ?: run { selectedLabel.text = "No document selected." }
        }
    }

    private fun printSpecimen() {
        val duplex = when (printMode.selectedItemPosition) {
            1 -> PrintAttributes.DUPLEX_MODE_LONG_EDGE
            2 -> PrintAttributes.DUPLEX_MODE_SHORT_EDGE
            else -> PrintAttributes.DUPLEX_MODE_NONE
        }
        val attributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setDuplexMode(duplex)
            .build()
        val manager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        manager.print("Sarmad specimen", SpecimenPrintAdapter(), attributes)
    }

    private class SpecimenPrintAdapter : PrintDocumentAdapter() {
        private var page: PdfDocument? = null
        override fun onLayout(old: PrintAttributes?, new: PrintAttributes, signal: CancellationSignal,
                              callback: LayoutResultCallback, extras: Bundle?) {
            if (signal.isCanceled) { callback.onLayoutCancelled(); return }
            page = PdfDocument()
            callback.onLayoutFinished(PrintDocumentInfo.Builder("specimen.pdf").setPageCount(1).build(),
                old != new)
        }
        override fun onWrite(ranges: Array<out PageRange>, destination: ParcelFileDescriptor,
                             signal: CancellationSignal, callback: WriteResultCallback) {
            if (signal.isCanceled) { callback.onWriteCancelled(); return }
            if (ranges.none { it == PageRange.ALL_PAGES || it.start <= 0 && it.end >= 0 }) {
                callback.onWriteFinished(emptyArray()); return
            }
            val pdf = page ?: PdfDocument()
            val info = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val outPage = pdf.startPage(info)
            val paint = Paint().apply { color = Color.BLACK; textSize = 24f }
            outPage.canvas.drawText("SYNTHETIC SPECIMEN / ARCHIVAL STUDY", 48f, 100f, paint)
            paint.color = Color.RED; paint.textSize = 18f
            outPage.canvas.drawText("SPECIMEN / NOT LEGAL TENDER / NOT FOR CIRCULATION", 48f, 150f, paint)
            paint.textSize = 16f
            outPage.canvas.drawText("عَيِّنَة / لَيْسَتْ عُمْلَةً قَانُونِيَّةً / مَمْنُوعَةٌ مِنَ التَّدَاوُل", 48f, 180f, paint)
            pdf.finishPage(outPage)
            try { pdf.writeTo(ParcelFileDescriptor.AutoCloseOutputStream(destination)); callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES)) }
            catch (_: IOException) { callback.onWriteFailed("Unable to write specimen PDF") }
            finally { pdf.close() }
        }
    }

    companion object { private const val REQUEST_DOCUMENT = 4201 }
}
