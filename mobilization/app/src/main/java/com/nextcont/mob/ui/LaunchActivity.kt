package com.nextcont.mob.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.nextcont.mob.R
import com.nextcont.mob.model.Account
import com.nextcont.mob.network.MobApi
import com.nextcont.mob.util.Util
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LaunchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        supportActionBar?.hide()

        uploadDevice()
    }

    private fun uploadDevice() {
        val deviceName = Settings.Secure.getString(contentResolver, "bluetooth_name")
        val fingerprint = android.os.Build.FINGERPRINT
        MobApi.deviceRegister(deviceName, fingerprint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp ->

                Account.deviceId = resp.deviceId

                resp.user?.let {
                    // 已登录
                }?: kotlin.run {
                    // 未登录
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }, { e ->
                Util.showAlert(this, e.localizedMessage) {
                    uploadDevice()
                }
            })
    }
}
