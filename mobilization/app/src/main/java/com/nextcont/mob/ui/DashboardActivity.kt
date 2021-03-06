package com.nextcont.mob.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.nextcont.mob.R
import com.nextcont.mob.model.Account
import com.nextcont.mob.ui.fragment.AccountFragment
import com.nextcont.mob.ui.fragment.EvaluationFragment
import com.nextcont.mob.ui.fragment.UsersFragment
import com.nextcont.mob.util.Util
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DashboardActivity : AppCompatActivity() {

    private lateinit var iTab: TabLayout
    private lateinit var iViewPager: ViewPager

    private lateinit var iTopView: LinearLayout
    private lateinit var iTipsText: TextView

    private val sPool = Executors.newScheduledThreadPool(1)    //2个线程

    private var fragments = emptyList<Fragment>()
    private var titles = emptyList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Account.user?.let {
            if (it.isAdmin) {
                fragments = listOf(EvaluationFragment(), UsersFragment(), AccountFragment())
                titles = listOf("训练考核", "预备役", "我")
            } else {
                fragments = listOf(EvaluationFragment(), AccountFragment())
                titles = listOf("训练考核", "我")
            }
        }


        iTab = findViewById(R.id.iTab)
        iTopView = findViewById(R.id.iTopView)
        iTipsText = findViewById(R.id.iTipsText)


        iTab.addTab(iTab.newTab().setIcon(R.mipmap.ic_run))
        iTab.addTab(iTab.newTab().setIcon(R.mipmap.ic_users))
        iTab.addTab(iTab.newTab().setIcon(R.mipmap.ic_user))


        val demoCollectionPagerAdapter = DemoCollectionPagerAdapter(supportFragmentManager)
        iViewPager = findViewById(R.id.iViewPager)
        iViewPager.adapter = demoCollectionPagerAdapter

        iTab.setupWithViewPager(iViewPager)

        val taskOne = object : TimerTask() {

            override fun run() {
                updateView()
            }
        }

        sPool.scheduleWithFixedDelay(taskOne,0,1000, TimeUnit.MILLISECONDS)
    }

    private fun updateView() {
        runOnUiThread {
            iTopView.visibility = VISIBLE
            var showEdit = false
            if (Util.isConnectedWifi(this) || Util.isAirplaneModeOn(this)) {
                iTipsText.text = "当前网络环境不可信，权限变更为: 只读"
                iTopView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWarning))
                showEdit = false
            } else {
                iTipsText.text = ""
                iTipsText.text = "当前网络可信任，已建立安全连接"
                iTopView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
                showEdit = true
            }
            if (fragments.isNotEmpty()) {
                (fragments[0] as EvaluationFragment).updateList(showEdit)
            }
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    inner class DemoCollectionPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getCount(): Int  = fragments.size

        override fun getItem(i: Int): Fragment {
            return fragments[i]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }


    }

}
