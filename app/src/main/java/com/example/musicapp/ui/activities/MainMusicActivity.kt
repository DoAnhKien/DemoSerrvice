package com.example.musicapp.ui.activities


import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.example.musicapp.R
import com.example.musicapp.adapter.TotalMusicAdapter
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.databinding.ActivityMainMusicBinding
import com.example.musicapp.model.SongItem
import com.example.musicapp.service.MusicService
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.top_menu.*

class MainMusicActivity : BaseActivity<ActivityMainMusicBinding>(),
    NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private val STORAGE_PERMISSION = 1000
    private var sbTime: SeekBar? = null
    private var musicService: MusicService? = null
    private val handler = Handler()


    fun getMusicService(): MusicService? {
        return musicService
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.musicService

        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
        }
    }

    override fun initLayout(): Int = R.layout.activity_main_music

    override fun init() {
        changeTheColorOfTheStatusBar()
    }

    override fun initViews() {
        setUpForTabLayoutAndViewpager()
        checkThePermission()
    }


    private fun checkThePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION
            )
        } else {
            startMusic()
        }
    }

    private fun startMusic() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun setUpForTabLayoutAndViewpager() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        vpMain.adapter = TotalMusicAdapter(supportFragmentManager)
        tabMain.setupWithViewPager(vpMain)
    }

    override fun setOnClickForViews() {
        imv_menu_drawer.setOnClickListener(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMusic()
            } else {
                Toast.makeText(this, "you are so stupid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorBottom)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!) {
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            } else {
                binding?.drawerLayout?.close()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_song -> {
                vpMain.currentItem = 0
            }
            R.id.nav_albums -> {
                vpMain.currentItem = 1
            }
            R.id.nav_artist -> {
                vpMain.currentItem = 3
            }
            R.id.mini_game -> {

            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imv_menu_drawer -> {
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onBackPressed() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}