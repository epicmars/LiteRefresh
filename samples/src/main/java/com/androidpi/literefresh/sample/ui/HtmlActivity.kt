/*
 * Copyright 2018 yinpinjiu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidpi.literefresh.sample.ui

import androidx.databinding.ViewDataBinding
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.androidpi.literefresh.sample.R
import com.androidpi.literefresh.sample.base.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_html.*

class HtmlActivity : BaseActivity() {

    var mSettings: WebSettings? = null
    var mUrl: String? = null

    companion object {
        const val ACTION_VIEW = "com.androidpi.literefresh.sample.ui.HtmlActivity.ACTION_VIEW"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        // configurate web view
        configWebView(web_view)
        // load web page
        mUrl = intent?.data?.toString()
        web_view.loadUrl(mUrl)
    }

    /**
     * Configurate the web view.
     */
    fun configWebView(webView: WebView) {
        mSettings = webView.settings
        mSettings?.javaScriptEnabled = true
        mSettings?.builtInZoomControls = true
        mSettings?.displayZoomControls = false
        mSettings?.allowUniversalAccessFromFileURLs = true

        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                super.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (pb.visibility == View.GONE) return
                pb.progress = newProgress
                if (pb.progress >= pb.max) {
                    pb.visibility = View.GONE
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

}
