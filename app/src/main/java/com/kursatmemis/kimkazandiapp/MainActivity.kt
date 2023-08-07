package com.kursatmemis.kimkazandiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.kursatmemis.kimkazandiapp.configs.AppDataBase

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout

    companion object {
        var db: AppDataBase? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setToggle()

        db = Room.databaseBuilder(this, AppDataBase::class.java, "AppDataBase")
            .build()

        val followedRaffles = intent.getStringExtra("followedRafflesUrls")?.split("\n")
        updateFollowedRaffles(followedRaffles)
    }

    // Verilerin servisten tekrar gelmesi durumunda, kullanıcının daha önceden takip ettiği
    // çekişileri kaybetmemek için takip edilen çekilişleri günceller.
    private fun updateFollowedRaffles(followedRaffles: List<String>?) {
        val clickPageUrles = mutableListOf<String>()
        val run = Runnable {
            val raffles = db?.raffleDao()?.getAll()
            if (raffles != null) {
                for (raffle in raffles) {
                    if (followedRaffles!!.contains(raffle.clickPageUrl.toString())) {
                        if (!clickPageUrles.contains(raffle.clickPageUrl)) {
                            db?.raffleDao()?.updateRaffleFollowStatus(raffle.id!!, true)
                            clickPageUrles.add(raffle.clickPageUrl.toString())
                        }
                    }
                }
            }
        }
        Thread(run).start()
    }

    // Toolbar'ı set eder ve sol üstteki 3 noktaya tıklayarak drawer'in açılmasını sağlar.
    private fun setToggle() {
        NavigationUI.setupWithNavController(navigationView, navHostFragment.navController)
        toolbar.title = "kimkazandi.com"
        val toggle =
            ActionBarDrawerToggle(this@MainActivity, drawer, toolbar, 0, 0)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    // Layout dosyasındaki view'ları koda bind eder.
    private fun bindViews() {
        navigationView = findViewById(R.id.navigationView)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        toolbar = findViewById(R.id.toolbar)
        drawer = findViewById(R.id.drawer)
    }

}