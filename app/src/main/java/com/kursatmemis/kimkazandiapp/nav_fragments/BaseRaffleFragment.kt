package com.kursatmemis.kimkazandiapp.nav_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.kursatmemis.kimkazandiapp.DetailRaffleActivity
import com.kursatmemis.kimkazandiapp.MainActivity
import com.kursatmemis.kimkazandiapp.R
import com.kursatmemis.kimkazandiapp.adapters.RaffleHomeAdapter
import com.kursatmemis.kimkazandiapp.models.Raffle

abstract class BaseRaffleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(getFragmentLayout(), container, false)
        val dataSource = mutableListOf<Raffle>()
        val adapter = RaffleHomeAdapter(requireContext(), dataSource)
        val listView = fragmentLayout.findViewById<ListView>(getListViewId())
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val raffle = dataSource[position]
            val intent = Intent(context, DetailRaffleActivity::class.java)
            intent.putExtra("raffle", raffle)
            startActivity(intent)
        }

        val run = Runnable {
            val data = MainActivity.db?.raffleDao()?.getRafflesByUrl(getRaffleUrl())
            activity?.runOnUiThread {
                if (data != null) {
                    updateUI(dataSource, data, adapter)
                }
            }
        }
        Thread(run).start()

        return fragmentLayout
    }

    abstract fun getFragmentLayout(): Int

    abstract fun getListViewId(): Int

    abstract fun getRaffleUrl(): String

    private fun updateUI(dataSource: MutableList<Raffle>, data: List<Raffle>, adapter: RaffleHomeAdapter?) {
        dataSource.clear()
        dataSource.addAll(data)
        adapter?.notifyDataSetChanged()
    }
}




