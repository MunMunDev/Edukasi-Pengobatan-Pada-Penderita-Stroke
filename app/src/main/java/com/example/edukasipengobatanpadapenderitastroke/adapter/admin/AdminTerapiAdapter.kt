package com.example.edukasipengobatanpadapenderitastroke.adapter.admin

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminGaleriHerbalValueBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminTentangStrokeValueBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminTerapiBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Youtube

class AdminTerapiAdapter(
    private var listTerapi: ArrayList<TerapiModel>,
    private var onClick: OnClickItem.ClickAdminTerapi
): RecyclerView.Adapter<AdminTerapiAdapter.ViewHolder>() {

    private var youtube = Youtube()

    class ViewHolder(val binding: ListAdminTerapiBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminTerapiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listTerapi.size+1)
    }

    private fun setShowTextImage(holder: ViewHolder){
        holder.binding.apply {
            tvGambar.visibility = View.VISIBLE
            ivGambar.visibility = View.GONE
        }
    }

    private fun setShowImage(holder: ViewHolder){
        holder.binding.apply {
            ivGambar.visibility = View.VISIBLE
            tvGambar.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNamaTerapi.text = "Nama Terapi"
                tvDeskripsi.text = "Isi Penjelasan"
                tvGambar.text = "Gambar"
                tvSetting.text = ""

                setShowTextImage(holder)

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaTerapi.setBackgroundResource(R.drawable.bg_table_title)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table_title)
                tvGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNamaTerapi.setTextColor(Color.parseColor("#ffffff"))
                tvDeskripsi.setTextColor(Color.parseColor("#ffffff"))
                tvGambar.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNamaTerapi.setTypeface(null, Typeface.BOLD)
                tvDeskripsi.setTypeface(null, Typeface.BOLD)
                tvGambar.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val terapi = listTerapi[(position-1)]

                tvNo.text = "$position"
                tvNamaTerapi.text = terapi.nama_terapi
                tvDeskripsi.text = terapi.deskripsi
                tvSetting.text = ":::"

                setShowImage(holder)

                Glide.with(holder.itemView)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${terapi.gambar}")
                    .error(R.drawable.gambar_error_image)
                    .into(ivGambar)

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNamaTerapi.setBackgroundResource(R.drawable.bg_table)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table)
                tvGambar.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNamaTerapi.setTextColor(Color.parseColor("#000000"))
                tvDeskripsi.setTextColor(Color.parseColor("#000000"))
                tvGambar.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNamaTerapi.setTypeface(null, Typeface.NORMAL)
                tvDeskripsi.setTypeface(null, Typeface.NORMAL)
                tvGambar.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvNamaTerapi.setOnClickListener{
                    onClick.clickItemJudul(terapi.nama_terapi!!, it)
                }
                tvDeskripsi.setOnClickListener {
                    onClick.clickItemDeskripsi(terapi.deskripsi!!, it)
                }
                ivGambar.setOnClickListener {
                    onClick.clickItemGambar(terapi.gambar!!, terapi.nama_terapi!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(terapi, it)
                }
            }
        }
    }
}