package com.example.edukasipengobatanpadapenderitastroke.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTentangStrokeDetailBinding


class TentangStrokeDetailAdapter(
    private val listTentangStrokeDetail: ArrayList<TentangStrokeDetailModel>
): RecyclerView.Adapter<TentangStrokeDetailAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListTentangStrokeDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTentangStrokeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTentangStrokeDetail.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tentangStroke = listTentangStrokeDetail[position]
        holder.binding.apply {
            tvJudulTentangStroke.text = tentangStroke.judul
            tvDeskripsilTentangStroke.text = tentangStroke.deskripsi
        }
    }
}