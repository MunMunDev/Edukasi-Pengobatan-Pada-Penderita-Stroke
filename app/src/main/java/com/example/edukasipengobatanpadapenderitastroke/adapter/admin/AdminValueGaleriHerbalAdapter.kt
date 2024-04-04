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
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminGaleriHerbalValueBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminTentangStrokeValueBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Youtube

class AdminValueGaleriHerbalAdapter(
    private var listValGaleriHerbal: ArrayList<GaleriHerbalListModel>,
    private var onClick: OnClickItem.ClickAdminValueGaleriHerbal
): RecyclerView.Adapter<AdminValueGaleriHerbalAdapter.ViewHolder>() {

    private var youtube = Youtube()

    class ViewHolder(val binding: ListAdminGaleriHerbalValueBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminGaleriHerbalValueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listValGaleriHerbal.size+1)
    }

    private fun setShowTextImage(holder: ViewHolder){
        holder.binding.apply {
            tvGambar.visibility = View.VISIBLE
            tvYoutube.visibility = View.VISIBLE

            ivGambar.visibility = View.GONE
            ivYoutube.visibility = View.GONE
        }
    }

    private fun setShowImage(holder: ViewHolder){
        holder.binding.apply {
            ivGambar.visibility = View.VISIBLE
            ivYoutube.visibility = View.VISIBLE

            tvGambar.visibility = View.GONE
            tvYoutube.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJudul.text = "Judul Isi"
                tvDeskripsi.text = "Isi Penjelasan"
                tvTataCaraPengolahan.text = "Tata Cara Pengolahan"
                tvGambar.text = "Gambar"
                tvYoutube.text = "Youtube"
                tvSetting.text = ""

                setShowTextImage(holder)

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJudul.setBackgroundResource(R.drawable.bg_table_title)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table_title)
                tvTataCaraPengolahan.setBackgroundResource(R.drawable.bg_table_title)
                tvGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvYoutube.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJudul.setTextColor(Color.parseColor("#ffffff"))
                tvDeskripsi.setTextColor(Color.parseColor("#ffffff"))
                tvTataCaraPengolahan.setTextColor(Color.parseColor("#ffffff"))
                tvGambar.setTextColor(Color.parseColor("#ffffff"))
                tvYoutube.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJudul.setTypeface(null, Typeface.BOLD)
                tvDeskripsi.setTypeface(null, Typeface.BOLD)
                tvTataCaraPengolahan.setTypeface(null, Typeface.BOLD)
                tvGambar.setTypeface(null, Typeface.BOLD)
                tvYoutube.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val galeriHerbal = listValGaleriHerbal[(position-1)]

                tvNo.text = "$position"
                tvJudul.text = galeriHerbal.nama
                tvDeskripsi.text = galeriHerbal.deskripsi
                tvTataCaraPengolahan.text = galeriHerbal.deskripsi
                tvSetting.text = ":::"

                setShowImage(holder)

                Glide.with(holder.itemView)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${galeriHerbal.gambar}")
                    .error(R.drawable.gambar_error_image)
                    .into(ivGambar)

                // set Image Youtube
                youtube.setImageFromYoutube(holder.itemView.context, galeriHerbal.youtube!!, ivYoutube)

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJudul.setBackgroundResource(R.drawable.bg_table)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table)
                tvTataCaraPengolahan.setBackgroundResource(R.drawable.bg_table)
                tvGambar.setBackgroundResource(R.drawable.bg_table)
                tvYoutube.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJudul.setTextColor(Color.parseColor("#000000"))
                tvDeskripsi.setTextColor(Color.parseColor("#000000"))
                tvTataCaraPengolahan.setTextColor(Color.parseColor("#000000"))
                tvGambar.setTextColor(Color.parseColor("#000000"))
                tvYoutube.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJudul.setTypeface(null, Typeface.NORMAL)
                tvDeskripsi.setTypeface(null, Typeface.NORMAL)
                tvTataCaraPengolahan.setTypeface(null, Typeface.NORMAL)
                tvGambar.setTypeface(null, Typeface.NORMAL)
                tvYoutube.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJudul.setOnClickListener{
                    onClick.clickItemJudul(galeriHerbal.nama!!, it)
                }
                tvDeskripsi.setOnClickListener {
                    onClick.clickItemDeskripsi(galeriHerbal.deskripsi!!, it)
                }
                tvTataCaraPengolahan.setOnClickListener {
                    onClick.clickItemTataCaraPengolahan(galeriHerbal.deskripsi!!, it)
                }
                ivGambar.setOnClickListener {
                    onClick.clickItemGambar(galeriHerbal.gambar!!, galeriHerbal.nama!!, it)
                }
                ivYoutube.setOnClickListener {
//                    youtube.setToYoutubeVideo(holder.itemView.context, galeriHerbal.youtube!!)
                    onClick.clickItemYoutube(galeriHerbal.youtube!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(galeriHerbal, it)
                }
            }
        }
    }
}