package com.example.gittrend.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.gittrend.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkConnectionInterceptor @Inject constructor(
    context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isInternetAvailable()) {
            throw NoInternetException("No Internet")
        }

        return chain.proceed(chain.request())
    }

    /*private fun isInternetAvailable(): Boolean{
        val  connectivityManager=applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also { network -> return network!=null }
    }*/
    private fun isInternetAvailable(): Boolean {
        var result = false
        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            } else {
                result = cm.activeNetworkInfo?.isConnected ?: false
            }
        }
        return result
    }
}