package com.xd.focusapp.ui.collection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.R
import com.xd.focusapp.TreeDetail

class AllFragment:Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_all, container, false)
        gridView = view.findViewById(R.id.gridView_all)

        // initialize image list
        imageList = ArrayList()
        // Use custom adapter to put image
        customAdapter = CustomAdapter(imageList, view.context)

        gridView.adapter = customAdapter



        collectionViewModel.imageToShowAll.observe(viewLifecycleOwner, Observer {

            imageList = it
            customAdapter.replace(it)
            gridView.adapter = customAdapter
            customAdapter.notifyDataSetChanged()
        })


        gridView.setOnItemClickListener{adapterView, view, i, l ->

            if(imageList[i].status) {
                val intent = Intent(requireActivity(), TreeDetail::class.java)
                intent.putExtra("image", imageList[i].image)
                intent.putExtra("rarity", imageList[i].getRank())
                intent.putExtra("name", imageList[i].treeName)

                startActivity(intent)
            }
            else{
                val bundle = Bundle()
                bundle.putInt(MyDialog.DIALOG_KEY, MyDialog.LOCK_DIALOG)
                val dialog = MyDialog()
                dialog.arguments = bundle
                dialog.show(parentFragmentManager, "alert")
            }

        }


        return view
    }

}
