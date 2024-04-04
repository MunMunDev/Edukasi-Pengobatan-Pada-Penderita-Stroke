package com.example.edukasipengobatanpadapenderitastroke.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTestimoniBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.TanggalDanWaktu

class AdminTestimoniAdapter(
    private val listTestimoni: ArrayList<TestimoniModel>,
    private val click: OnClickItem.ClickTestimoni
): RecyclerView.Adapter<AdminTestimoniAdapter.ViewHolder>() {

    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListTestimoniBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTestimoniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTestimoni.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testimoni = listTestimoni[position]
        holder.binding.apply {
            val nama = testimoni.nama!!
            val tanggal = tanggalDanWaktu.konversiBulan(testimoni.tanggal!!)
            val valueTestimoni = testimoni.testimoni!!
            val bintang = testimoni.bintang!!.trim().toInt()
            val firstLetter = nama.substring(0, 1)

            tvNama.text = nama
            tvTanggal.text = tanggal
            tvTestimoni.text = valueTestimoni
            tvInisial.text = firstLetter

            val listBgCircle = ArrayList<Int>()
            listBgCircle.add(R.drawable.bg_circle_1)
            listBgCircle.add(R.drawable.bg_circle_2)
            listBgCircle.add(R.drawable.bg_circle_3)
            listBgCircle.add(R.drawable.bg_circle_4)
            listBgCircle.add(R.drawable.bg_circle_5)

            when (bintang) {
                1 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                }
                2 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                }
                3 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                }
                4 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang4.setImageResource(R.drawable.icon_star_aktif)
                }
                5 -> {
                    ivBintang1.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang2.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang3.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang4.setImageResource(R.drawable.icon_star_aktif)
                    ivBintang5.setImageResource(R.drawable.icon_star_aktif)
                }
            }

            val mathRandom = (Math.random()*5).toInt()
            tvInisial.setBackgroundResource(listBgCircle[mathRandom])

            if(testimoni.gambar!!.trim().isEmpty()){
                ivBukti.visibility = View.GONE
            } else{
                ivBukti.visibility = View.VISIBLE
                Glide.with(holder.itemView)
                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${testimoni.gambar!!}") // URL Gambar
                    .error(R.drawable.gambar_error_image)
                    .into(ivBukti) // imageView mana yang akan diterapkan
            }

            ivBukti.setOnClickListener {
                click.clickGambar(testimoni.gambar!!, testimoni.nama!!, it)
            }
        }
    }
}