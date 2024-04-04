package com.example.edukasipengobatanpadapenderitastroke.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListGaleriHerbalMainBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem

class TerapiAdapter(
    private val listTerapi: ArrayList<TerapiModel>,
    private val click: OnClickItem.ClickTerapi
): RecyclerView.Adapter<TerapiAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListGaleriHerbalMainBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListGaleriHerbalMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTerapi.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val terapi = listTerapi[position]
        holder.binding.apply {
            tvJudulObatHerbal.text = terapi.nama_terapi
            tvDeskripsiObatHerbal.text = terapi.deskripsi

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${terapi.gambar}")
                .error(R.drawable.gambar_error_image)
                .into(ivObatHerbal)

            holder.itemView.setOnClickListener {
                click.clickItem(terapi, it)
            }
        }
    }
}