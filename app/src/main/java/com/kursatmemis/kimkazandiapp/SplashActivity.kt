package com.kursatmemis.kimkazandiapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import com.kursatmemis.kimkazandiapp.configs.AppDataBase
import java.lang.StringBuilder
import java.util.concurrent.CountDownLatch

class SplashActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private val followedRafflesUrls = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imageView = findViewById(R.id.loadingIconImageView)
        startAnimation()
        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
        val isMoreThanThreeHoursAgo = isLastOpenedMoreThanThreeHoursAgo()

        if (isMoreThanThreeHoursAgo) {
            clearDatabase()
            callService()
            val currentTime = System.currentTimeMillis()
            saveLastUpdateTime(currentTime)
            Toast.makeText(this, "Servis çağrıldı..", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Veriler için database kullanılacak.", Toast.LENGTH_SHORT).show()
            goToMainActivity()
        }
    }

    // Verilerin servisten alındığı son saatin üç saatten fazla olup olmadığı bilgisini retrun eder.
    private fun isLastOpenedMoreThanThreeHoursAgo(): Boolean {
        val lastUpdateTime = sharedPreferences.getLong("lastTime", 0)
        val currentTime = System.currentTimeMillis()
        val timeDiff = currentTime - lastUpdateTime
        return lastUpdateTime == 0L || timeDiff > 10800000
    }

    // Ekranda bir animasyon başlatarak kullanıcıya geri bildirim sağlar.
    private fun startAnimation() {
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)
        imageView.startAnimation(rotateAnimation)
    }

    // Servisten verileri alır ve MainActivity'e geçiş için gerekli methodu çağırır.
    // Result class'ı içinde servisten gelen veriler veritabanına kaydedilecektir.
    private fun callService() {
        val result = Result()
        val run = Runnable {
            result.rafflePreview("https://www.kimkazandi.com/cekilisler", this)
            result.rafflePreview("https://www.kimkazandi.com/cekilisler/yeni-baslayanlar", this)
            result.rafflePreview("https://www.kimkazandi.com/cekilisler/telefon-tablet-kazan", this)
            result.rafflePreview("https://www.kimkazandi.com/cekilisler/tatil-kazan", this)
            result.rafflePreview("https://www.kimkazandi.com/cekilisler/bedava-katilim", this)
            result.rafflePreview("https://www.kimkazandi.com/cekilisler/araba-kazan", this)
            goToMainActivity()
        }
        Thread(run).start()
    }

    // Veri tabanındaki tüm kayıtları siler. Ancak bunu yapmadan önce kullanıcının takipte olduğu
    // çekilişleri elde der.
    private fun clearDatabase() {
        val db = Room.databaseBuilder(this, AppDataBase::class.java, "AppDataBase")
            .build()

        val latch = CountDownLatch(1)

        val runGetFollowedRaffles = Runnable {
            val followedRaffles = db.raffleDao().getFollowedRaffle()
            for (i in followedRaffles) {
                followedRafflesUrls.append(i.clickPageUrl)
                followedRafflesUrls.append("\n")
            }
            latch.countDown()
        }

        val runDeleteAllRaffles = Runnable {
            latch.await()
            db.raffleDao().deleteAll()
        }

        Thread(runGetFollowedRaffles).start()
        Thread(runDeleteAllRaffles).start()
    }

    // MainActivity'e geçişi sağlar. Aynı zamanda kullanıcının takipte olduğu çekiliş linklerini
    // hedef activity'e gönderir.
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("followedRafflesUrls", followedRafflesUrls.toString())
        startActivity(intent)
        finish()
    }

    // Verilerin servisten geldiği son zamanı günceller.
    private fun saveLastUpdateTime(time: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong("lastTime", time)
        editor.apply()
    }
}