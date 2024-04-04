package com.example.edukasipengobatanpadapenderitastroke.utils

import android.view.View
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
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
}

    interface ClickTentangStroke{
        fun clickItem(id_hal_tentang_stroke: String, halaman: String, it: View)
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

    interface ClickTestimoni{
        fun clickGambar(gambar: String, nama: String, it: View)
    }

    // ADMIN
    interface ClickAdminTentangStroke{
        fun clickItem(hal_tentang_stroke:TentangStrokeListModel, id_hal_tentang_stroke: String, halaman: String, it: View)
    }

    interface ClickAdminValueTentangStroke{
        fun clickItemHalaman(halaman: String, it: View)
        fun clickItemJudul(judul: String, it: View)
        fun clickItemDeskripsi(deskripsi: String, it: View)
        fun clickItemSetting(tentangStroke: TentangStrokeDetailModel, it: View)
    }

    interface ClickAdminHalamanGaleriHerbal{
        fun clickItemGambar(gambar: String, title:String, it: View)
        fun clickItemNextPage(galeriHerbal: GaleriHerbalMainModel, it: View)
        fun clickItemSetting(galeriHerbal: GaleriHerbalMainModel, it: View)
    }

    interface ClickAdminValueGaleriHerbal{
        fun clickItemJudul(judul: String, it: View)
        fun clickItemDeskripsi(deskripsi:String, it: View)
        fun clickItemTataCaraPengolahan(tataCaraPengolahan:String, it: View)
        fun clickItemGambar(gambar: String, title:String, it: View)
        fun clickItemYoutube(linkYoutube:String, it: View)
        fun clickItemSetting(galeriHerbal: GaleriHerbalListModel, it: View)
    }

    interface ClickAdminTerapi{
        fun clickItemJudul(namaTerapi: String, it: View)
        fun clickItemDeskripsi(deskripsi:String, it: View)
        fun clickItemGambar(gambar: String, title:String, it: View)
        fun clickItemSetting(terapi: TerapiModel, it: View)
    }

    interface ClickAdminMenuSehat{
        fun clickItemJudul(namaMenuSehat: String, it: View)
        fun clickItemDeskripsi(deskripsi:String, it: View)
        fun clickItemSetting(menuSehat: MenuSehatModel, it: View)
    }

}