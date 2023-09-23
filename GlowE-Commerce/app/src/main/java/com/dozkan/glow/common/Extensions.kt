package com.dozkan.glow.common


import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

fun View.gone(){
    visibility=View.GONE
}
fun View.visible(){
    visibility=View.VISIBLE
}

fun TextView.setStrikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
