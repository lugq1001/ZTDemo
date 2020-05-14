package com.nextcont.mob.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.nextcont.mob.R
import com.nextcont.mob.model.Account
import kotlin.system.exitProcess

class DashboardActivity : AppCompatActivity() {

    private lateinit var iLogoutButton: Button
    private lateinit var iDisableButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        iLogoutButton = findViewById(R.id.iLogoutButton)
        iDisableButton = findViewById(R.id.iDisableButton)

        iLogoutButton.setOnClickListener {
            logout()
        }

        iDisableButton.setOnClickListener {
            disable()
        }
    }

    private fun logout() {
        val account = Account.load()
        account.login = false
        account.save()
        finish()
    }

    private fun disable() {
        val account = Account.load()
        account.login = false
        account.disableTime = System.currentTimeMillis()
        account.save()
        finish()
    }
}
