package com.xd.focusapp.ui.collection
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xd.focusapp.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.random.Random


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

    var imageToShow = MutableLiveData<ArrayList<Tree>>()
    val update = MutableLiveData<Int>()

    var test: LiveData<ArrayList<Tree>> = imageToShow


    init {
        Task()
    }

    private fun Task(){
        CoroutineScope(IO).launch {

            for (i in 0..images.size - 1) {
                var image = images[i]
                imageList.add(Tree(treeName[i], image))
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

            updateOnMainThread(imageList)
        }
    }

    private suspend fun updateOnMainThread(modalList: ArrayList<Tree>){
        withContext(Main){
            imageToShow.value = modalList
            test = imageToShow
        }
    }


    fun setUnlock(rank:Int){

        CoroutineScope(IO).launch {
            val randomInts = generateSequence {
                Random.nextInt(0,imageList.size - 1)
            }.distinct().take(imageList.size - 1).toSet().shuffled()

            var set = false

            randomInts.forEach{ e->
                if((imageList[e].getRank() == rank) && !set){
                    imageList[e].unLock()
                    set = true
                }
            }

            println("debug2: ----- setUnlock() called")

            imageList.add(Tree("n", R.drawable.image1))

            updateOnMainThread(imageList)
        }


    }



}