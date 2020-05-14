package com.nextcont.mob.model

import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.telephony.TelephonyManager

data class Device(
    val name: String
) {

    fun info(context: Context) {
        // 品牌
        val BRAND = android.os.Build.BRAND
        // 设备名
        val DEVICE = android.os.Build.DEVICE
        // 型号
        val MODEL = android.os.Build.MODEL
        val RELEASE = android.os.Build.VERSION.RELEASE
        // 硬件制造商
        val MANUFACTURER = android.os.Build.MANUFACTURER
        // 厂商
        val PRODUCT = android.os.Build.PRODUCT
        // 唯一识别码
        val FINGERPRINT = android.os.Build.FINGERPRINT

        val name = Settings.Secure.getString(context.contentResolver, "bluetooth_name")

        System.currentTimeMillis()
    }


    fun getNetMode(context: Context): String? {
        var strNetworkType = "未知"
        //        TelephonyManager manager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        manager.getNetworkType();
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val netMode = networkInfo.type
            if (netMode == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI"
                //wifi
            } else if (netMode == ConnectivityManager.TYPE_MOBILE) {
                val networkType = networkInfo.subtype
                strNetworkType = when (networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                    TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                    else -> {
                        val _strSubTypeName = networkInfo.subtypeName
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equals(
                                "TD-SCDMA",
                                ignoreCase = true
                            ) || _strSubTypeName.equals(
                                "WCDMA",
                                ignoreCase = true
                            ) || _strSubTypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            "3G"
                        } else {
                            _strSubTypeName
                        }
                    }
                }
            }
        }
        return strNetworkType
    }

}