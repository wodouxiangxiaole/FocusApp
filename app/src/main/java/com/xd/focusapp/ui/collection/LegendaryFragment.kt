package com.xd.focusapp.ui.collection

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.R
import com.xd.focusapp.databinding.FragmentCollectionBinding

class LegendaryFragment:Fragment() {
    private lateinit var gridView: GridView
    private lateinit var imageList:ArrayList<Tree>

    private lateinit var customAdapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val collectionViewModel =
            ViewModelProvider(requireActivity()).get(CollectionViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_legendary, container, false)
        gridView = view.findViewById(R.id.gridView_legendary)

        // initialize image list
        imageList = ArrayList()
        // Use custom adapter to put image
        customAdapter = CustomAdapter(imageList, view.context)

        gridView.adapter = customAdapter



        collectionViewModel.imageToShowLegendary.observe(viewLifecycleOwner, Observer {
            imageList = it
            customAdapter.replace(it)
            customAdapter.notifyDataSetChanged()
        })

        return view
    }
}