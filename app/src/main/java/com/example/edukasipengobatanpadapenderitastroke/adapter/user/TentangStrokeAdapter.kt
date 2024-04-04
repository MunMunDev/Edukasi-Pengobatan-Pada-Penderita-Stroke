package com.example.edukasipengobatanpadapenderitastroke.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListTentangStrokeBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem


class TentangStrokeAdapter(
    private val listTentangStroke: ArrayList<TentangStrokeListModel>,
    private val click: OnClickItem.ClickTentangStroke
): RecyclerView.Adapter<TentangStrokeAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListTentangStrokeBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListTentangStrokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTentangStroke.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tentangStroke = listTentangStroke[position]
        holder.binding.apply {
            tvJudulTentangStroke.text = tentangStroke.halaman

            btnTentangStroke.setOnClickListener {
                click.clickItem(tentangStroke.id_hal_tentang_stroke!!, tentangStroke.halaman!!, it)
            }
        }
    }
}