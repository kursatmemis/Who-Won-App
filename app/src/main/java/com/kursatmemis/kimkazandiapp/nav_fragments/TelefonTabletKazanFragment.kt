package com.kursatmemis.kimkazandiapp.nav_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.kursatmemis.kimkazandiapp.DetailRaffleActivity
import com.kursatmemis.kimkazandiapp.MainActivity
import com.kursatmemis.kimkazandiapp.R
import com.kursatmemis.kimkazandiapp.adapters.RaffleHomeAdapter
import com.kursatmemis.kimkazandiapp.models.Raffle

class TelefonTabletKazanFragment : BaseRaffleFragment() {

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun getListViewId(): Int {
        return R.id.listViewHome
    }

    override fun getRaffleUrl(): String {
        return "https://www.kimkazandi.com/cekilisler/telefon-tablet-kazan"
    }

}

