package com.nextcont.mob.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nextcont.mob.R
import com.nextcont.mob.model.Account
import com.nextcont.mob.network.MobApi
import com.nextcont.mob.util.DialogUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LaunchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        supportActionBar?.hide()

    }

    override fun onStart() {
        super.onStart()
        uploadDevice()
    }

    private fun uploadDevice() {
        val deviceName = Settings.Secure.getString(contentResolver, "bluetooth_name")
        val fingerprint = android.os.Build.FINGERPRINT

        MobApi.deviceRegister(deviceName, fingerprint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ resp ->

                if (resp.disabled) {
                    DialogUtil.showAlert(this, "该应用已失效") {
                        findViewById<ProgressBar>(R.id.iProgress).visibility = GONE
                        findViewById<TextView>(R.id.iTipsText).visibility = GONE
                    }
                    return@subscribe
                }

                Account.deviceId = resp.deviceId

                resp.user?.let { user ->
                    // 已登录
                    if (resp.expired) {
                        DialogUtil.showAlert(this, "登录已过期", action = {
                            startActivity(Intent(this, LoginActivity::class.java))
                        })
                    } else {
                        Account.user = user
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }

                }?: kotlin.run {
                    // 未登录
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }, { e ->
                DialogUtil.showAlert(this, e.localizedMessage) {
                    uploadDevice()
                }
            })
    }
}
