package com.example.webapp
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
    private val websiteURL = "https://www.sipi.edu.bd/"
    private lateinit var webview: WebView
    private lateinit var mySwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!CheckNetwork.isInternetAvailable(this)) {
            setContentView(R.layout.activity_main)
            AlertDialog.Builder(this)
                .setTitle("No internet connection available")
                .setMessage("Please Check you're Mobile data or Wifi network.")
                .setPositiveButton("Ok") { dialog, which ->
                    finish()
                }
                .show()
        } else {
            webview = findViewById(R.id.webView)
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true
            webview.overScrollMode = WebView.OVER_SCROLL_NEVER
            webview.loadUrl(websiteURL)
            webview.webViewClient = WebViewClientDemo()
        }

        mySwipeRefreshLayout = findViewById(R.id.swipeContainer)
        mySwipeRefreshLayout.setOnRefreshListener {
            webview.reload()
        }
    }

    override fun onBackPressed() {
        if (webview.isFocused && webview.canGoBack()) {
            webview.goBack()
        } else {
            AlertDialog.Builder(this)
                .setTitle("EXIT")
                .setMessage("Are you sure. You want to close this app?")
                .setPositiveButton("Yes") { dialog, which ->
                    finish()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private inner class WebViewClientDemo : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mySwipeRefreshLayout.isRefreshing = false
        }
    }
}

object CheckNetwork {
    private const val TAG = "CheckNetwork"

    fun isInternetAvailable(context: Context): Boolean {
        val info = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info == null) {
            Log.d(TAG, "no internet connection")
            return false
        } else {
            if (info.isConnected) {
                Log.d(TAG, "internet connection available...")
                return true
            } else {
                Log.d(TAG, "internet connection")
                return true
            }
        }
    }
}


