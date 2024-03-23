package com.example.edukasipengobatanpadapenderitastroke.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTestimoniBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.TanggalDanWaktu

class TestimoniAdapter(
    private val listTestimoni: ArrayList<TestimoniModel>,
    private val idUser: String,
    private var checkTestimoni: Boolean     // Kalau true maka testimoni halaman home
): RecyclerView.Adapter<TestimoniAdapter.ViewHolder>() {

    private val tanggalDanWaktu = TanggalDanWaktu()

    class ViewHolder(val binding: ListTestimoniBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTestimoniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        var size = listTestimoni.size
        if(checkTestimoni){
            size = if(listTestimoni.size<3){
                listTestimoni.size
            } else{
                3
            }
        } else{
            listTestimoni.size
        }

        return size

//        return if(checkTestimoni){
//            3
//        } else{
//            listTestimoni.size
//        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testimoni = listTestimoni[position]
        holder.binding.apply {
            val valueIdUser = testimoni.id_user!!
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

            if(idUser == valueIdUser){
                tvInisial.setBackgroundResource(R.drawable.bg_circle)
                tvNama.text = "Anda"
                tvInisial.text = "A"
            } else{
                val mathRandom = (Math.random()*5).toInt()
                tvInisial.setBackgroundResource(listBgCircle[mathRandom])
            }
        }
    }
}