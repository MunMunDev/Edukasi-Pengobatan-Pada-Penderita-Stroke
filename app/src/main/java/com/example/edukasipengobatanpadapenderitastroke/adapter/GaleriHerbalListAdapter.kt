package com.example.edukasipengobatanpadapenderitastroke.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListGaleriHerbalListBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem

class GaleriHerbalListAdapter(
    private val listGaleriHerbalMain: ArrayList<GaleriHerbalListModel>,
    private val click: OnClickItem.ClickGaleriHerbalList
): RecyclerView.Adapter<GaleriHerbalListAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListGaleriHerbalListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListGaleriHerbalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGaleriHerbalMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val galeriHerbalMain = listGaleriHerbalMain[position]
        holder.binding.apply {
            tvJudulObatHerbal.text = galeriHerbalMain.nama
//r
            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${galeriHerbalMain.gambar}")
                .error(R.drawable.gambar_error_image)
                .into(ivGaleriHerbalList)
//
            holder.itemView.setOnClickListener {
                click.clickItem(galeriHerbalMain, it)
            }
        }
    }
}