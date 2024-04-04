package com.example.edukasipengobatanpadapenderitastroke.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTentangStrokeDetailBinding


class MenuSehatAdapter(
    private val listMenuSehat: ArrayList<MenuSehatModel>
): RecyclerView.Adapter<MenuSehatAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListTentangStrokeDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTentangStrokeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMenuSehat.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menuSehat = listMenuSehat[position]
        holder.binding.apply {
            tvJudulTentangStroke.text = menuSehat.judul
            tvDeskripsilTentangStroke.text = menuSehat.deskripsi
        }
    }
}