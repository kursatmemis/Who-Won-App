package com.kursatmemis.kimkazandiapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity("raffle")
data class Raffle(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val url: String?,
    val clickPageUrl: String?,
    val imgLink: String?,
    val description: String?,
    val iconTimeLink: String?,
    val iconGiftLink: String?,
    val iconPriceLink: String?,
    val iconTimeDesc: String?,
    val iconGiftDesc: String?,
    val iconPriceDesc: String?,
    val isFollow: Boolean?
) : Serializable
