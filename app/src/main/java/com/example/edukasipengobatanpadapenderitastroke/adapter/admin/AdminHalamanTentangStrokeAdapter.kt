package com.example.edukasipengobatanpadapenderitastroke.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ListAdminTentangStrokeHalamanBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem


class AdminHalamanTentangStrokeAdapter(
    private val listTentangStroke: ArrayList<TentangStrokeListModel>,
    private val click: OnClickItem.ClickAdminTentangStroke
): RecyclerView.Adapter<AdminHalamanTentangStrokeAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAdminTentangStrokeHalamanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListAdminTentangStrokeHalamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                click.clickItem(tentangStroke, tentangStroke.id_hal_tentang_stroke!!, tentangStroke.halaman!!, it)
            }

            ivSetting.setOnClickListener {
                click.clickItem(tentangStroke, tentangStroke.id_hal_tentang_stroke!!, tentangStroke.halaman!!, it)
            }
        }
    }
}