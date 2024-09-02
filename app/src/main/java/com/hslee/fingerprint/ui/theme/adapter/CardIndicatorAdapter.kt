package com.hslee.fingerprint.ui.theme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hslee.fingerprint.R
import com.hslee.fingerprint.databinding.ItemCardBinding
import com.hslee.fingerprint.databinding.ItemCardIndicatorBinding
import com.hslee.fingerprint.ui.theme.model.CardIndicatorInfo


class CardIndicatorAdapter(private val data: ArrayList<CardIndicatorInfo>, context: Context) : RecyclerView.Adapter<CardIndicatorAdapter.PagerViewHolder>() {
    private val context = context

    inner class PagerViewHolder(private val binding: ItemCardIndicatorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CardIndicatorInfo) {
            if (data.flag) {
                binding.ivIndicator.background = context.getDrawable(R.drawable.card_indicator_on)
            } else {
                binding.ivIndicator.background = context.getDrawable(R.drawable.card_indicator_off)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(ItemCardIndicatorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}
