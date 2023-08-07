package com.kursatmemis.kimkazandiapp.nav_fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.kursatmemis.kimkazandiapp.DetailRaffleActivity
import com.kursatmemis.kimkazandiapp.R
import com.kursatmemis.kimkazandiapp.adapters.FavorilerimAdapter
import com.kursatmemis.kimkazandiapp.configs.AppDataBase
import com.kursatmemis.kimkazandiapp.models.Raffle


class TakipEttiklerimFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var raffleHomeDataSource: MutableList<Raffle>
    private lateinit var raffleHomeAdapter: FavorilerimAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val takipEttiklerimLayout = inflater.inflate(R.layout.fragment_takip_ettiklerim, container, false)

        listView = takipEttiklerimLayout.findViewById(R.id.listViewTakipEttiklerim)
        raffleHomeDataSource = mutableListOf()
        raffleHomeAdapter = FavorilerimAdapter(requireContext(), raffleHomeDataSource)
        listView.adapter = raffleHomeAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val raffle = raffleHomeDataSource[position]
            val intent = Intent(context, DetailRaffleActivity::class.java)
            intent.putExtra("raffle", raffle)
            startActivity(intent)
        }
        Log.w("Calisma sirasi", "diger fragment calisti")

        return takipEttiklerimLayout
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }

    private fun updateListView() {
        val db = Room.databaseBuilder(requireContext(), AppDataBase::class.java, "AppDataBase")
            .build()

        val run = Runnable {
            val data = db.raffleDao().getFollowedRaffle()
            activity?.runOnUiThread {
                raffleHomeDataSource.clear()
                raffleHomeDataSource.addAll(data)
                raffleHomeAdapter.notifyDataSetChanged()
            }
        }
        Thread(run).start()
    }
}


