package com.xd.focusapp.ui.collection
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xd.focusapp.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class CollectionViewModel : ViewModel() {
    var images = intArrayOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6,
        R.drawable.image7,
        R.drawable.image8,
        R.drawable.image9,
        R.drawable.image10,
        R.drawable.image11,
        R.drawable.image12,
        R.drawable.image13,
        R.drawable.image14
    )

    var treeName = arrayOf(
        "Autumn Tree",
        "Chestnut Tree",
        "Tree Conifer",
        "Tree Full Of Snow",
        "Tree No Leaves",
        "Tree In Autumn",
        "Large Green Tree",
        "Palm Tree",
        "Red Tree",
        "Dead Tree",
        "Normal Tree",
        "Light Green Tree",
        "Top Palm Tree",
        "Japanese Tree"
    )

    private var imageList:ArrayList<Tree> = ArrayList()

    private var imageListRare:ArrayList<Tree> = ArrayList()
    private var imageListCommon:ArrayList<Tree> = ArrayList()
    private var imageListUncommon:ArrayList<Tree> = ArrayList()
    private var imageListLegendary:ArrayList<Tree> = ArrayList()

//    var imageToShow = MutableLiveData<ArrayList<Tree>>()
//    val update = MutableLiveData<Int>()

    var imageToShowRare = MutableLiveData<ArrayList<Tree>>()
    var imageToShowCommon = MutableLiveData<ArrayList<Tree>>()
    var imageToShowUncommon = MutableLiveData<ArrayList<Tree>>()
    var imageToShowLegendary = MutableLiveData<ArrayList<Tree>>()

    private var LEGENDARY = 0
    private var RARE = 1
    private var UNCOMMON = 2
    private var COMMON = 3

    init {
        Task()
    }

    fun Task(){
        CoroutineScope(IO).launch {

            imageList.clear()

            for (i in 0..images.size - 1) {
                imageList.add(Tree(treeName[i], images[i]))
            }

            imageList[0].setRank(1)
            imageList[1].setRank(1)
            imageList[2].setRank(2)
            imageList[3].setRank(0)
            imageList[4].setRank(3)
            imageList[5].setRank(2)
            imageList[6].setRank(3)
            imageList[7].setRank(3)
            imageList[8].setRank(2)
            imageList[9].setRank(2)
            imageList[10].setRank(3)
            imageList[11].setRank(2)
            imageList[12].setRank(3)
            imageList[13].setRank(1)



            for (i in images.indices) {
                if(imageList[i].getRank() == RARE){
                    imageListRare.add(imageList[i])
                }
                else if(imageList[i].getRank() == LEGENDARY){
                    imageListLegendary.add(imageList[i])
                }
                else if(imageList[i].getRank() == COMMON){
                    imageListCommon.add(imageList[i])
                }
                else if(imageList[i].getRank() == UNCOMMON){
                    imageListUncommon.add(imageList[i])
                }
            }
            updateOnMainThread(imageListRare, imageListCommon, imageListUncommon, imageListLegendary)
        }
    }

    fun unlock(rank: Int){
        CoroutineScope(IO).launch {

            when (rank) {
                COMMON -> {
                    val rand =(0 until imageListCommon.size).shuffled().last()
                    imageListCommon[rand].unLock()
                    println("debug: $rand")
                }
                UNCOMMON -> {
                    val rand =(0 until imageListUncommon.size).shuffled().last()
                    imageListUncommon[rand].unLock()
                    println("debug: $rand")
                }
                RARE -> {
                    val rand =(0 until imageListRare.size).shuffled().last()
                    imageListRare[rand].unLock()
                    println("debug: $rand")
                }
                else -> {
                    val rand =(0 until imageListLegendary.size).shuffled().last()
                    imageListLegendary[rand].unLock()
                    println("debug: $rand")
                }
            }

            updateOnMainThread(imageListRare, imageListCommon, imageListUncommon, imageListLegendary)
        }
    }

    private suspend fun updateOnMainThread(imageListRare:ArrayList<Tree>,imageListCommon:ArrayList<Tree>,imageListUncommon:ArrayList<Tree>,imageListLegendary:ArrayList<Tree>){
        withContext(Main){
            imageToShowRare.value = imageListRare
            imageToShowCommon.value = imageListCommon
            imageToShowUncommon.value = imageListUncommon
            imageToShowLegendary.value = imageListLegendary
        }
    }




}