package com.xd.focusapp.ui.collection

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.xd.focusapp.R

class CustomAdapter(
    var itemModel: ArrayList<Tree>,
    var context: Context
) : BaseAdapter() {
    override fun getCount(): Int {
        return itemModel.size
    }

    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getItem(p0: Int): Any {
        return itemModel[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        if(view == null){
            view = layoutInflater.inflate(R.layout.grid_view, viewGroup, false)
        }
        var imageView = view?.findViewById<ImageView>(R.id.imageView)
        imageView?.setImageResource(itemModel[position].image!!)

        // set image to grayscale
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        // if lock ==> lock it
        if(!itemModel[position].status){
            imageView?.setColorFilter(ColorMatrixColorFilter(matrix))
        }

        return view!!
    }

    fun replace(new:ArrayList<Tree>){
        itemModel = new
    }

}