package com.xd.focusapp.ui.collection

import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.xd.focusapp.R
import com.xd.focusapp.TreeDetail
import com.xd.focusapp.databinding.FragmentCollectionBinding
import com.xd.focusapp.ui.spinner.SpinnerViewModel


class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var gridView:GridView
    private lateinit var imageList:ArrayList<Tree>

    private lateinit var customAdapter:CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectionViewModel =
            ViewModelProvider(requireActivity()).get(CollectionViewModel::class.java)

        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gridView = root.findViewById(R.id.gridView)

        println("debug2: onCreate() Called")

        // initialize image list
        imageList = ArrayList()
        // Use custom adapter to put image
        customAdapter = CustomAdapter(imageList, root.context)

        gridView.adapter = customAdapter

        collectionViewModel.imageToShow.observe(viewLifecycleOwner, Observer {

            customAdapter.replace(it)
            customAdapter.notifyDataSetChanged()
        })



        gridView.setOnItemClickListener{adapterView, view, i, l ->

            val intent = Intent(requireActivity(), TreeDetail::class.java)

            startActivity(intent)

        }


        return root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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

}