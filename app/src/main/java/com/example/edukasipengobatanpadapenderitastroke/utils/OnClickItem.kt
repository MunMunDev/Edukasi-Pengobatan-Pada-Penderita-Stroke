package com.example.edukasipengobatanpadapenderitastroke.utils

import android.view.View
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.UsersModel

interface OnClickItem {
//    interface ClickJenisPlafon{
//        fun clickItemSetting(jenisPlafon: JenisPlafonModel, it: View)
//        fun clickItemKeunggulan(jenisPlafon: JenisPlafonModel, it: View)
//    }
//
//    interface ClickPlafon{
//        fun clickItemPlafon(plafon: PlafonModel, it: View)
//        fun clickItemImage(jenisPlafon:String, image: String)
//    }
//
//    interface ClickPesanan{
//        fun clickItemPesanan(pesanan: PesananModel, it: View)
//        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
//    }
//
//    interface ClickAdminPesanan{
//        fun clickItemSetting(pesanan: PesananModel, it: View)
//        fun clickAlamatPesanan(alamat: String, it: View)
//        fun clickGambarPesanan(gambar: String, jenisPlafon:String, it: View)
//    }
    interface ClickAkun{
        fun clickItemSetting(akun: UsersModel, it: View)
//        fun clickItemNama(nama: String, it: View)
        fun clickItemAlamat(alamat: String, it: View)
    }

    interface ClickObatHerbal{
        fun clickItem(id: String, judul:String, it: View)
    }

    interface ClickGaleriHerbalList{
        fun clickItem(galeriHerbalList: GaleriHerbalListModel, it: View)
    }

    interface ClickTerapi{
        fun clickItem(terapi: TerapiModel, it: View)
    }
}