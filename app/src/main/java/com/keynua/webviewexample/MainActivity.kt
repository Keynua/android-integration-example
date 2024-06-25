package com.keynua.webviewexample

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var buttonOpenWebView: Button
    private lateinit var idTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var idLayout: LinearLayout
    private lateinit var statusLayout: LinearLayout

    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 100
        /**
         * TODO: Change Keynua URL according to the current environment
         * PROD: https://sign.keynua.com
         * STG: https://sign.stg.keynua.com
         */
        private const val KEYNUA_URL = "https://sign.stg.keynua.com"
        // TODO: Change this eventURL with your customURL APP
        private const val EVENT_URL = "keynuaapp://com.keynua.webviewexample"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ask for camera and micro permissions
        requestPermissions()

        webView = findViewById(R.id.webview)
        buttonOpenWebView = findViewById(R.id.buttonOpenWebView)
        idLayout = findViewById(R.id.idLayout)
        statusLayout = findViewById(R.id.statusLayout)
        idTextView = findViewById(R.id.idText)
        statusTextView = findViewById(R.id.statusText)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.mediaPlaybackRequiresUserGesture = false

        // enable google chrome inspect
        WebView.setWebContentsDebuggingEnabled(true)

        // Manage WebClient events
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                Log.d("WebViewURL", "URL: ${request?.url}")
                /**
                 * Validate that the URL is the same as the one sent in EventURL
                 */
                if (request?.url.toString().startsWith(EVENT_URL)) {
                    closeWebView()
                    val queryParams = extractQueryParams(request?.url.toString())
                    queryParams.forEach { (key, value) ->
                        Log.d("WebViewURLParams", "$key: $value")
                    }
                    /**
                     * If it's an Identification, it will return "identificationId". If
                     * it's a Contract, it will return "contractId"
                     */
                    idTextView.text = queryParams["identificationId"] ?: queryParams["contractId"]
                    statusTextView.text = queryParams["status"]
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebViewURL", "URL loaded: $url")
            }
        }

        // Manage ChromeClient events
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread {
                    Log.d("onPermissionRequest", "Request origin request -> ${request.origin.toString()}")
                    // You can validate if the origin request is Keynua
                    request.grant(request.resources)
                }
            }
        }

        // Action when button is clicked
        buttonOpenWebView.setOnClickListener {
            webView.visibility = View.VISIBLE
            buttonOpenWebView.visibility = View.GONE
            idLayout.visibility = View.GONE
            statusLayout.visibility = View.GONE
            // TODO: Replace this Token with a valid token
            val token = "USER-TOKEN-HERE"
            val uri = Uri.parse("${KEYNUA_URL}?token=${token}")
            val uriBuilder = uri.buildUpon()
            uriBuilder.appendQueryParameter("eventURL", EVENT_URL)
            webView.loadUrl(uriBuilder.build().toString())
        }
    }

    private fun requestPermissions() {
        val permissionsNeeded = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,  Manifest.permission.MODIFY_AUDIO_SETTINGS)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsNeeded, MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE) {
            closeWebView()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // Reload the webview if all the permissions has been granted
            webView.reload()
        } else {
            // The process will fail if the user decline the permissions
            Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show()
        }
    }

    fun closeWebView() {
        webView.visibility = View.GONE
        buttonOpenWebView.visibility = View.VISIBLE
        idLayout.visibility = View.VISIBLE
        statusLayout.visibility = View.VISIBLE
    }


    fun extractQueryParams(url: String): Map<String, String> {
        val uri = Uri.parse(url)
        val queryParameters = mutableMapOf<String, String>()

        for (paramName in uri.queryParameterNames) {
            val paramValue = uri.getQueryParameter(paramName)
            if (paramValue != null) {
                queryParameters[paramName] = paramValue
            }
        }

        return queryParameters
    }

}