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
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminGaleriHerbalValueBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminTentangStrokeValueBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminMenuSehatBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Youtube

class AdminMenuSehatAdapter(
    private var listMenuSehat: ArrayList<MenuSehatModel>,
    private var onClick: OnClickItem.ClickAdminMenuSehat
): RecyclerView.Adapter<AdminMenuSehatAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminMenuSehatBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminMenuSehatBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listMenuSehat.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJudul.text = "Nama Menu Sehat"
                tvDeskripsi.text = "Isi Penjelasan"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJudul.setBackgroundResource(R.drawable.bg_table_title)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJudul.setTextColor(Color.parseColor("#ffffff"))
                tvDeskripsi.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJudul.setTypeface(null, Typeface.BOLD)
                tvDeskripsi.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val terapi = listMenuSehat[(position-1)]

                tvNo.text = "$position"
                tvJudul.text = terapi.judul
                tvDeskripsi.text = terapi.deskripsi
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJudul.setBackgroundResource(R.drawable.bg_table)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJudul.setTextColor(Color.parseColor("#000000"))
                tvDeskripsi.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJudul.setTypeface(null, Typeface.NORMAL)
                tvDeskripsi.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJudul.setOnClickListener{
                    onClick.clickItemJudul(terapi.judul!!, it)
                }
                tvDeskripsi.setOnClickListener {
                    onClick.clickItemDeskripsi(terapi.deskripsi!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(terapi, it)
                }
            }
        }
    }
}