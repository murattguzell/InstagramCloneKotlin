package com.muratguzel.instakotlin.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.muratguzel.instakotlin.R
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule


fun ImageView.imageDownload(url: String?, placeholder: CircularProgressDrawable) {
    val options = RequestOptions().placeholder(placeholder).error(R.drawable.ic_profile)
    if (url.isNullOrEmpty()) { // URL boş veya null ise
        options.error(R.drawable.ic_profile) // Default resmi göster
    }
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}

fun placeHolderCreate(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

