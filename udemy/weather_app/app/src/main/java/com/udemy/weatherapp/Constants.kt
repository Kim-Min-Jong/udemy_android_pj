package com.udemy.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {
    fun isNetWorkAvailable(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // 최근 버전 안드로이드 네트워크 확인인
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           val network = connectivityManager.activeNetwork ?: return false
           val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false

            }
       }else{ // 구버전 안드로이드 네트워크 확인
           val networkInfo = connectivityManager.activeNetworkInfo
           return networkInfo != null && networkInfo.isConnectedOrConnecting
       }

    }
}