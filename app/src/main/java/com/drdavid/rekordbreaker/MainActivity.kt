package com.drdavid.rekordbreaker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drdavid.rekordbreaker.ui.theme.RekordBreakerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.web.*

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val webState = rememberWebViewState(url = "https://rekordbreakers.com")
            val uiController = rememberSystemUiController()
            val navController = rememberWebViewNavigator()
            val progressNumber = remember{ mutableStateOf(0.0f)}
            val visibility = remember { mutableStateOf(false)}
            RekordBreakerTheme {
                // A surface container using the 'background' color from the theme
                uiController.setStatusBarColor(color = Color(0xFF035C2A))
                Column {
//                    TopAppBar(
//                        title = { Text(text = "RekordBreakers") },
//                        navigationIcon = if (navController.canGoBack) {
//                            {
//                                IconButton(onClick = { navController.navigateBack() }) {
//                                    Icon(
//                                        imageVector = Icons.Filled.ArrowBack,
//                                        contentDescription = "Back"
//                                    )
//                                }
//                            }
//                        } else {
//                            null
//                        }
//                    )
                    if (visibility.value){
                        LinearProgressIndicator(
                            progress = progressNumber.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .width(2.dp),
                        )
                    }else{
                        Spacer(modifier = Modifier.fillMaxWidth())
                    }
                    WebView(
                        state = webState,
                        modifier = Modifier.fillMaxSize(),
                        navigator = navController,
                        captureBackPresses = true,
                        client = object : AccompanistWebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                visibility.value = true
                                Log.d(TAG, "onPageStarted: is called")
                                
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                visibility.value = false
                                Log.d(TAG, "onPageFinished: is finishes")
                            }
                        },
                        chromeClient = object : AccompanistWebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                progressNumber.value = ((newProgress.toFloat() / 100))
                                Log.d(TAG, "onProgressChanged: $progressNumber")
                                Log.d(TAG, "onProgressChanged: $newProgress")
                            }
                        },
                        onCreated = {
                            it.settings.javaScriptEnabled = true
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RekordBreakerTheme {
        Greeting("Android")
    }
}