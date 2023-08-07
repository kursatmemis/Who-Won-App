package com.kursatmemis.kimkazandiapp

import android.content.Context
import androidx.room.Room
import com.kursatmemis.kimkazandiapp.configs.AppDataBase
import com.kursatmemis.kimkazandiapp.models.Raffle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup

class Result {

    // Çekilişleri ön izleme verilerini alır. Bu veriler listview'lar üzerinde gösterilecektir.
    fun rafflePreview(url: String, context: Context): MutableList<Raffle> = runBlocking {
        val raffles = mutableListOf<Raffle>()
        val db = Room.databaseBuilder(context, AppDataBase::class.java, "AppDataBase").build()
        val connection = Jsoup.connect(url)
        val doc = connection.timeout(15000).get()
        val elements = doc.select("div.col-sm-3.col-lg-3.item")

        val deferredRaffles = elements.map { element ->
            async(Dispatchers.IO) {
                val clickPageUrl = element.select("div.img a").attr("href")
                val imgLink = element.select("div.img a img").attr("src")
                val description = element.select("div h4").text()
                val iconDescriptions = element.select("div.title span.date").eachText()

                if (clickPageUrl.isNotEmpty()) {
                    val iconTimeDesc = iconDescriptions.getOrNull(0) ?: "-"
                    val iconGiftDesc = iconDescriptions.getOrNull(1) ?: "-"
                    val iconPriceDesc = iconDescriptions.getOrNull(2) ?: "-"
                    val raffle = Raffle(
                        null,
                        url,
                        clickPageUrl,
                        imgLink,
                        description,
                        context.getString(R.string.iconTimeLink),
                        context.getString(R.string.iconGiftLink),
                        context.getString(R.string.iconPriceLink),
                        iconTimeDesc,
                        iconGiftDesc,
                        iconPriceDesc,
                        false
                    )
                    db.raffleDao().insert(raffle)
                    raffleDetailImportantInformation(
                        "${context.getString(R.string.BASE_URL)}${clickPageUrl}",
                        context
                    )
                    raffleDetailDescriptions(
                        "${context.getString(R.string.BASE_URL)}${clickPageUrl}",
                        context
                    )
                    raffle
                } else {
                    null
                }
            }
        }

        raffles.addAll(deferredRaffles.awaitAll().filterNotNull())
        raffles
    }

    // Çekilişin önemli bilgiler tablosundaki verileri alır.
    private fun raffleDetailImportantInformation(url: String, context: Context): String {
        val connection = Jsoup.connect(url)
        val doc = connection.timeout(15000).get()
        val elementsKalanSure = doc.select(".kalanSure h4 label")
        val stringBuilder = StringBuilder()

        for (element in elementsKalanSure) {
            val text = element.ownText()
            val value = element.parent()?.ownText() ?: "-"

            stringBuilder.append("$text $value\n")
        }

        val resultString = stringBuilder.toString()
        saveData(url, resultString, context)
        return resultString
    }

    // Çekilişin detay açıklamalarını alır.
    private fun raffleDetailDescriptions(url: String, context: Context): String {
        val connection = Jsoup.connect(url)
        val doc = connection.timeout(15000).get()
        val titleElements = doc.select(".secondGallery")
        val formattedText = StringBuilder()
        var firstItem = true

        for (element in titleElements) {
            val text = element.wholeText().trim()
            val items = text.split("\n").map { it.trim() }

            if (firstItem) {
                firstItem = false
            } else {
                formattedText.append("\n")
            }

            formattedText.append(items.joinToString("\n"))
        }

        saveData("$url-desc", formattedText.toString(), context)
        return formattedText.toString()
    }

    // Shared pref. e ilgili veriyi kaydeder.
    private fun saveData(key: String, data: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("$key", data)
        editor.apply()
    }
}