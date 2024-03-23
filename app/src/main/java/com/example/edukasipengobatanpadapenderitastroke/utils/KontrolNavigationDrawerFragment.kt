package com.example.edukasipengobatanpadapenderitastroke.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.login.LoginActivity

class KontrolNavigationDrawerFragment(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
//            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
//            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }

    private fun commitFragment(fragmentManager: FragmentManager, fragment: Fragment){
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flMain, fragment)
//        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(
        navigation: com.google.android.material.navigation.NavigationView,
        navigationLayout: DrawerLayout,
        igNavigation:ImageView,
        fragment: Fragment,
        supportFragmentManager: FragmentManager
    ){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
                    R.id.userNavDrawerHome -> {
//                        val fragmentManager : FragmentManager = supportFragmentManager
//                        val fragmentTransaction = fragmentManager.beginTransaction()
//                        fragmentTransaction.replace(R.id.flMain, fragment)
//                        fragmentTransaction.commit()

                        commitFragment(supportFragmentManager, fragment)
                        commitFragment(supportFragmentManager, fragment)
                    }
                    R.id.userNavDrawerTentangStroke -> {
                        commitFragment(supportFragmentManager, fragment)
                    }
                    R.id.userNavDrawerGaleriHerbal -> {
                        commitFragment(supportFragmentManager, fragment)
                    }
                    R.id.userNavDrawerTerapi -> {
                        commitFragment(supportFragmentManager, fragment)
                    }
                    R.id.userNavDrawerMenuSehat -> {
                        commitFragment(supportFragmentManager, fragment)
                    }
                    R.id.userBtnKeluar ->{
//                        logout(activity)
                    }

//                    R.id.userNavDrawerHome -> {
//                        val intent = Intent(context, MainActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userNavDrawerPlafon -> {
//                        val intent = Intent(context, PlafonActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userNavDrawerRiwayatPembayaran -> {
//                        val intent = Intent(context, RiwayatPembayaranActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userNavDrawerAkun -> {
//                        val intent = Intent(context, AkunActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userBtnKeluar ->{
//                        logout(activity)
//                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
//                    R.id.adminNavDrawerHome -> {
//                        val intent = Intent(context, AdminMainActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerJenisPlafon -> {
//                        val intent = Intent(context, AdminJenisPlafonActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerPlafon -> {
//                        val intent = Intent(context, AdminPlafonActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerPesanan -> {
//                        val intent = Intent(context, AdminDaftarPesananActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerRiwayatPesanan -> {
////                        val intent = Intent(context, AdminVideoActivity::class.java)
////                        context.startActivity(intent)
////                        activity.finish()
//                    }
//                    R.id.adminNavDrawerAkun -> {
//                        val intent = Intent(context, AdminSemuaAkunActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminBtnKeluar ->{
//                        logout(activity)
//                    }
                }

            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        sharedPreferences.setLogin(0, "", "","", "","")
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity.finish()

    }
}