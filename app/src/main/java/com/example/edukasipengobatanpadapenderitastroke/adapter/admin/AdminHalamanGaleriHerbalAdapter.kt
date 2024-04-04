package com.example.edukasipengobatanpadapenderitastroke.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminGaleriHerbalHalamanBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem


class AdminHalamanGaleriHerbalAdapter(
    private val listHalGaleriHerbal: ArrayList<GaleriHerbalMainModel>,
    private val click: OnClickItem.ClickAdminHalamanGaleriHerbal
): RecyclerView.Adapter<AdminHalamanGaleriHerbalAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminGaleriHerbalHalamanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminGaleriHerbalHalamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHalGaleriHerbal.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val galeriHerbal = listHalGaleriHerbal[position]
        holder.binding.apply {
            tvJudulObatHerbal.text = galeriHerbal.judul
            tvDeskripsiObatHerbal.text = galeriHerbal.deskripsi

            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${galeriHerbal.gambar!!}") // URL Gambar
                .error(R.drawable.gambar_error_image)
                .into(ivObatHerbal) // imageView mana yang akan diterapkan

            ivObatHerbal.setOnClickListener {
                click.clickItemGambar(galeriHerbal.gambar!!, galeriHerbal.judul!!, it)
            }
            holder.itemView.setOnClickListener {
//                Toast.makeText(holder.itemView.context, "klik semua", Toast.LENGTH_SHORT).show()
                click.clickItemNextPage(galeriHerbal, it)
            }
            ivSetting.setOnClickListener {
                click.clickItemSetting(galeriHerbal, it)
            }
        }
    }
}