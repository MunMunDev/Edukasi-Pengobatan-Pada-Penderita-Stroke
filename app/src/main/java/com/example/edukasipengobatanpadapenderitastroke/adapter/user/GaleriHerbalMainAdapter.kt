package com.example.edukasipengobatanpadapenderitastroke.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListGaleriHerbalMainBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem

class GaleriHerbalMainAdapter(
    private val listGaleriHerbalMain: ArrayList<GaleriHerbalMainModel>,
    private val click: OnClickItem.ClickObatHerbal
): RecyclerView.Adapter<GaleriHerbalMainAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListGaleriHerbalMainBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListGaleriHerbalMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGaleriHerbalMain.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val galeriHerbalMain = listGaleriHerbalMain[position]
        holder.binding.apply {
            tvJudulObatHerbal.text = galeriHerbalMain.judul
            tvDeskripsiObatHerbal.text = galeriHerbalMain.deskripsi

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${galeriHerbalMain.gambar}")
                .error(R.drawable.gambar_error_image)
                .into(ivObatHerbal)


            holder.itemView.setOnClickListener {
                click.clickItem(galeriHerbalMain.id_hal_galeri_herbal!!, galeriHerbalMain.judul!!, it)
            }
        }
    }
}