package com.example.edukasipengobatanpadapenderitastroke.ui.fragment.user.galeri_herbal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.TestimoniAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainViewModel
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawerFragment
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.navigation.NavigationView


class GaleriHerbalFragment : Fragment() {
    private lateinit var view: View
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var kontrolNavigationDrawerFragment: KontrolNavigationDrawerFragment
//    private val viewModel: MainViewModel by viewModels()
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TestimoniAdapter
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin

    private lateinit var drawerLayoutMain: DrawerLayout
    private lateinit var tvNama: TextView
    private lateinit var btnTentangStroke: LinearLayout
    private lateinit var btnGaleriHerbal: LinearLayout
    private lateinit var btnTerapi: LinearLayout
    private lateinit var btnMenuSehat: LinearLayout
    private lateinit var btnTestimoni: TextView
    private lateinit var rvTestimoni: RecyclerView
    private lateinit var smTestimoni: ShimmerFrameLayout
    private lateinit var navView: NavigationView
    private lateinit var ivDrawerView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_galeri_herbal, container, false)

        setDeklrasi(view)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setSharedPreferencesLogin()
        setNavigationDrawer()
        setButton()
        fetchTestimoni()
        getTestimoni()

        return view
    }

    private fun setDeklrasi(view: View) {
        drawerLayoutMain = view.findViewById(R.id.drawerLayoutMain)
        tvNama = view.findViewById(R.id.tvNama)
        btnTentangStroke = view.findViewById(R.id.btnTentangStroke)
        btnGaleriHerbal = view.findViewById(R.id.btnGaleriHerbal)
        btnTerapi = view.findViewById(R.id.btnTerapi)
        btnMenuSehat = view.findViewById(R.id.btnMenuSehat)
        btnTestimoni = view.findViewById(R.id.btnTestimoni)
        rvTestimoni = view.findViewById(R.id.rvTestimoni)
        smTestimoni = view.findViewById(R.id.smTestimoni)
        navView = view.findViewById(R.id.navView)
        ivDrawerView = view.findViewById(R.id.ivDrawerView)
    }


    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(view.context)
        tvNama.text = "Hy, ${sharedPreferencesLogin.getNama()}"
    }

    private fun setNavigationDrawer() {
        kontrolNavigationDrawerFragment = KontrolNavigationDrawerFragment(view.context)
        view.apply {
//            kontrolNavigationDrawerFragment.cekSebagai(navView)
//            kontrolNavigationDrawerFragment.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, GaleriHerbalFragment(), supportFragmentManager)
        }
    }

    private fun setButton() {
        view.apply {
            btnTentangStroke.setOnClickListener {

            }
            btnGaleriHerbal.setOnClickListener {

            }
            btnTerapi.setOnClickListener {

            }
            btnMenuSehat.setOnClickListener {

            }

            btnTestimoni.setOnClickListener {

            }
        }
    }

    private fun fetchTestimoni() {
        viewModel.fetchTestimoni()
    }

    private fun getTestimoni() {
        viewModel.getTestimoni().observe(viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTestimoni(result.message)
                is UIState.Success-> setSuccessTestimoni(result.data)
            }
        }
    }

    private fun setFailureTestimoni(message: String) {
        setStopShimmer()
        Toast.makeText(view.context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTestimoni(data: ArrayList<TestimoniModel>) {
        setAdapter(data)
    }

    private fun setAdapter(data: ArrayList<TestimoniModel>) {
        adapter = TestimoniAdapter(data, sharedPreferencesLogin.getIdUser().toString(), true)
        view.apply {
            rvTestimoni.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            rvTestimoni.adapter = adapter
        }
        setStopShimmer()
    }

    private fun setStopShimmer(){
        view.apply {
            rvTestimoni.visibility = View.VISIBLE

            smTestimoni.stopShimmer()
            smTestimoni.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        view.apply {
            smTestimoni.startShimmer()
            smTestimoni.visibility = View.VISIBLE

            rvTestimoni.visibility = View.GONE
        }
    }

}