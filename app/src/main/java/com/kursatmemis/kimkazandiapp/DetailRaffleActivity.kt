package com.kursatmemis.kimkazandiapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kursatmemis.kimkazandiapp.models.Raffle

class DetailRaffleActivity : AppCompatActivity() {

    private lateinit var importantInfoTextView: TextView
    private lateinit var titleAndDescTextView: TextView
    private lateinit var raffleDetailImageImageView: ImageView
    private lateinit var addToFollowButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_raffle)

        bindViews()

        val sharedPreferences = this.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val raffle = intent.getSerializableExtra("raffle") as Raffle
        Glide.with(this).load("${getString(R.string.BASE_URL)}${raffle.imgLink}").into(raffleDetailImageImageView)
        setButtonText(raffle)

        val run = Runnable {
            runOnUiThread {
                val url = "https://www.kimkazandi.com${raffle.clickPageUrl}"
                val importantInfoText = sharedPreferences.getString(url, null)
                val descriptionText = sharedPreferences.getString("$url-desc", null)
                importantInfoTextView.text = importantInfoText
                titleAndDescTextView.text = descriptionText
            }
        }
        Thread(run).start()

        addToFollowButton.setOnClickListener {
            if (addToFollowButton.text == getString(R.string.detail_activity_button_text_follow)) {
                followRaffle(raffle)
            } else {
                unfRaffle(raffle)
            }
        }
    }

    // Kullanıcı Takip Et/Bırak butonuna bastığında, buton text'i o an Takip Et ise
    // çekilişi takip eder ve buton textini 'Takibi Bırak' olarak günceller.
    private fun followRaffle(raffle: Raffle) {
        val run = Runnable {
            MainActivity.db?.raffleDao()?.updateRaffleFollowStatus(raffle.id!!, true)
        }
        Thread(run).start()
        addToFollowButton.text = getString(R.string.detail_activity_button_text_unf)
    }

    // Kullanıcı Takip Et/Bırak butonuna bastığında, buton text'i o an Takibi Bırak ise
    // çekilişi takipten bırakır ve buton textini 'Takip Et' olarak günceller.
    private fun unfRaffle(raffle: Raffle) {
        val run = Runnable {
            MainActivity.db?.raffleDao()?.updateRaffleFollowStatus(raffle.id!!, false)
        }
        Thread(run).start()
        addToFollowButton.text = getString(R.string.detail_activity_button_text_follow)
    }

    // Kullanıcının çekilişi takip edip etmeme durumuna göre butonın text'ini set eder.
    private fun setButtonText(raffle: Raffle) {
        val isFollowing = isFollowing(raffle)
        if (isFollowing!!) {
            addToFollowButton.text = getString(R.string.detail_activity_button_text_unf)
        } else {
            addToFollowButton.text = getString(R.string.detail_activity_button_text_follow)
        }
    }

    // Parametre olarak aldığı çekilişi kullanıcının takip edip - etmediği bilgisini return eder.
    private fun isFollowing(raffle: Raffle): Boolean? {
        return raffle.isFollow
    }

    // Layout dosyasındaki view'ları koda bind eder.
    private fun bindViews() {
        importantInfoTextView = findViewById(R.id.importantInfoTextView)
        titleAndDescTextView = findViewById(R.id.titleAndDescTextView)
        raffleDetailImageImageView = findViewById(R.id.raffleDetailImageImageView)
        addToFollowButton = findViewById(R.id.addToFollowButton)
    }
}





