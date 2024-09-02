package com.hslee.fingerprint.ui.theme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hslee.fingerprint.databinding.ItemCardBinding
import com.hslee.fingerprint.ui.theme.model.CardInfo


class CardAdapter(private val data: ArrayList<CardInfo>) : RecyclerView.Adapter<CardAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CardInfo, position: Int) {
            binding.tvTitle.text = "${data.title}"
            binding.tvDescription.text = "${data.des}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size
}
