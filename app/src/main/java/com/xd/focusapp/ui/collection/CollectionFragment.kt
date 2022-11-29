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
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xd.focusapp.R
import com.xd.focusapp.TreeDetail
import com.xd.focusapp.databinding.FragmentCollectionBinding
import com.xd.focusapp.ui.collection.Game.LaunchGame
import com.xd.focusapp.ui.login.ActivitySignup
import com.xd.focusapp.ui.spinner.SpinnerViewModel


class CollectionFragment : Fragment() {

    private var _binding: FragmentCollectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rareFragment:RareFragment
    private lateinit var commonFragment:CommonFragment
    private lateinit var unCommonFragment: UncommonFragment
    private lateinit var legendaryFragment: LegendaryFragment
    private lateinit var allFragment: AllFragment

    private lateinit var fragments:ArrayList<Fragment>
    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var myFragmentStateAdapter: MyFragmentStateAdapter
    private val tabTitles = arrayOf("ALL", "COMMON", "UNCOMMON", "RARE", "LEGENDARY")
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectionViewModel =
            ViewModelProvider(requireActivity()).get(CollectionViewModel::class.java)

        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rareFragment = RareFragment()
        commonFragment = CommonFragment()
        unCommonFragment = UncommonFragment()
        legendaryFragment = LegendaryFragment()
        allFragment = AllFragment()

        fragments = ArrayList()
        fragments.add(allFragment)
        fragments.add(commonFragment)
        fragments.add(unCommonFragment)
        fragments.add(rareFragment)
        fragments.add(legendaryFragment)


        tabLayout = root.findViewById(R.id.tab)
        viewPager2 = root.findViewById(R.id.viewpager)


        myFragmentStateAdapter = MyFragmentStateAdapter(requireActivity(), fragments)
        viewPager2.adapter = myFragmentStateAdapter

        tabConfigurationStrategy = TabLayoutMediator.TabConfigurationStrategy(){
                tab: TabLayout.Tab, position:Int ->
            tab.text = tabTitles[position]
        }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()




        return root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}