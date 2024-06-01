package com.example.edukasipengobatanpadapenderitastroke.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R

class Youtube() {
    fun setToYoutubeVideo(context: Context, urlVideo: String) {
        val id = searchIdUrlVideo(urlVideo)
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

    fun setImageFromYoutube(
        context: Context,
        urlVideo: String,
        imageView: ImageView
    ) {
        val id = searchIdUrlVideo(urlVideo)
        Glide.with(context)
            .load("https://img.youtube.com/vi/$id/0.jpg") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(imageView) // imageView mana yang akan diterapkan
    }

//    private fun searchIdUrlVideo(urlVideo: String): String {
//        var url = ""
//        url = try {
//            val arrayUrlImageVideo = urlVideo.split("v=")
//            arrayUrlImageVideo[1]
//        } catch (ex: Exception){
//            try {
//                val arrayUrlImageVideo = urlVideo.split("si=")
//                arrayUrlImageVideo[1]
//            } catch (ex: Exception){
//                "0"
//            }
//        }
//        return url
//    }
    private fun searchIdUrlVideo(urlVideo: String): String {
        var url = ""
        try {
            val arrayUrlIdVideo = urlVideo.split("v=")
            url = arrayUrlIdVideo[1]

            try {
                val arraySearchUrlSymbol = url.split("&")
                url = arraySearchUrlSymbol[0]
            } catch (_: Exception){
                url = arrayUrlIdVideo[1]
            }
        } catch (ex: Exception){
            try {
                val arrayUrlIdVideo = urlVideo.split("si=")
                url = arrayUrlIdVideo[1]
            } catch (ex: Exception){
                url = "0"
            }
        }
        return url
    }
}