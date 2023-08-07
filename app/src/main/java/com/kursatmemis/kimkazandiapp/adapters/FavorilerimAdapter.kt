package com.kursatmemis.kimkazandiapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kursatmemis.kimkazandiapp.R
import com.kursatmemis.kimkazandiapp.models.Raffle

class FavorilerimAdapter(context: Context, private val dataSource: List<Raffle>) :
    ArrayAdapter<Raffle>(context, R.layout.raffle_home_layout, dataSource) {

    private val BASE_URL = "https://www.kimkazandi.com"
    private val glide = Glide.with(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.raffle_home_layout, parent, false)
            viewHolder = ViewHolder()
            viewHolder.raffleImageImageView = view.findViewById(R.id.raffleImageImageView)
            viewHolder.raffleDescriptionTextView = view.findViewById(R.id.raffleDescriptionTextView)
            viewHolder.iconTimeImageView = view.findViewById(R.id.iconTimeImageView)
            viewHolder.timeTextView = view.findViewById(R.id.timeTextView)
            viewHolder.iconGiftImageView = view.findViewById(R.id.iconGiftImageView)
            viewHolder.giftTextView = view.findViewById(R.id.giftTextView)
            viewHolder.iconPriceImageView = view.findViewById(R.id.iconPriceImageView)
            viewHolder.priceTextView = view.findViewById(R.id.priceTextView)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val raffleHome = dataSource[position]
        val imgLink = "$BASE_URL${raffleHome.imgLink}"
        val description = raffleHome.description
        val iconTimeLink = raffleHome.iconTimeLink
        val iconGiftLink = raffleHome.iconGiftLink
        val iconPriceLink = raffleHome.iconPriceLink

        glide.load(imgLink).into(viewHolder.raffleImageImageView)
        viewHolder.raffleDescriptionTextView.text = description
        glide.load(iconTimeLink).into(viewHolder.iconTimeImageView)
        viewHolder.timeTextView.text = raffleHome.iconTimeDesc
        glide.load(iconGiftLink).into(viewHolder.iconGiftImageView)
        viewHolder.giftTextView.text = raffleHome.iconGiftDesc
        glide.load(iconPriceLink).into(viewHolder.iconPriceImageView)
        viewHolder.priceTextView.text = raffleHome.iconPriceDesc

        return view!!
    }

    private class ViewHolder {
        lateinit var raffleImageImageView: ImageView
        lateinit var raffleDescriptionTextView: TextView
        lateinit var iconTimeImageView: ImageView
        lateinit var timeTextView: TextView
        lateinit var iconGiftImageView: ImageView
        lateinit var giftTextView: TextView
        lateinit var iconPriceImageView: ImageView
        lateinit var priceTextView: TextView
    }

}