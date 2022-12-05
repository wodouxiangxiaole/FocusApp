package com.xd.focusapp.ui.collection
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xd.focusapp.Database
import com.xd.focusapp.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class CollectionViewModel(param: String?) : ViewModel() {

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
        R.drawable.image14,
        R.drawable.flower_marguerite,
        R.drawable.flower_drawing,
        R.drawable.japanese_flower,
        R.drawable.pink_flower,
        R.drawable.purple_flower,
        R.drawable.velvetty_white_flower,
        R.drawable.white_flower
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
        "Japanese Tree",
        "Flower Marguerite",
        "Flower Drawing",
        "Japanese Flower",
        "Pink Flower",
        "Purple Flower",
        "Velvetty White Flower",
        "White Flower"
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
    val imageToShowAll = MutableLiveData<ArrayList<Tree>>()

    private var LEGENDARY = 0
    private var RARE = 1
    private var UNCOMMON = 2
    private var COMMON = 3

    var db:Database


    init {
        db = Database()
        val query = "select * from users where email = '${param}';"
        db.getUser(query)

        Task()
        println("debug --- create db")
    }

    fun Task(){
        CoroutineScope(IO).launch {
            val idList = db.getUnlockedId()


            imageList.clear()

            for (i in 0..images.size - 1) {
                imageList.add(Tree(treeName[i], images[i], i))
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

            imageList[14].setRank(2)
            imageList[15].setRank(3)
            imageList[16].setRank(3)
            imageList[17].setRank(3)
            imageList[18].setRank(3)
            imageList[19].setRank(2)
            imageList[20].setRank(2)

            for(i in idList[0].indices){
                imageList[idList[0][i]].unLock(idList[1][i])
            }

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



            updateOnMainThread(imageListRare, imageListCommon, imageListUncommon, imageListLegendary, imageList)
        }
    }

    fun unlock(rank: Int, whereGetIt:Int): Tree {


        var id:Int

        when (rank) {
            COMMON -> {
                val rand =(0 until imageListCommon.size).random()
                imageListCommon[rand].unLock(whereGetIt)
                id = imageListCommon[rand].id!!
            }
            UNCOMMON -> {
                val rand =(0 until imageListUncommon.size).random()
                imageListUncommon[rand].unLock(whereGetIt)
                id = imageListUncommon[rand].id!!
            }
            RARE -> {
                val rand =(0 until imageListRare.size).random()
                imageListRare[rand].unLock(whereGetIt)
                id = imageListRare[rand].id!!
            }
            else -> {
                val rand =(0 until imageListLegendary.size).random()
                imageListLegendary[rand].unLock(whereGetIt)
                id = imageListLegendary[rand].id!!
            }
        }


        if(imageList[id].toPoints){
            return imageList[id]
        }
        else{
            db.insertUserCollection(imageList[id].id!!, whereGetIt)

            imageToShowRare.value = imageListRare
            imageToShowCommon.value = imageListCommon
            imageToShowUncommon.value = imageListUncommon
            imageToShowLegendary.value = imageListLegendary
            imageToShowAll.value = imageList

            return imageList[id]
        }



    }

    private suspend fun updateOnMainThread(imageListRare:ArrayList<Tree>,imageListCommon:ArrayList<Tree>,imageListUncommon:ArrayList<Tree>,imageListLegendary:ArrayList<Tree>,imageList:ArrayList<Tree>){

        withContext(Main){
            imageToShowRare.value = imageListRare
            imageToShowCommon.value = imageListCommon
            imageToShowUncommon.value = imageListUncommon
            imageToShowLegendary.value = imageListLegendary
            imageToShowAll.value = imageList
        }
    }



}

class CollectionViewModelFactory (param: String?) : ViewModelProvider.Factory{
    private var mParam = param

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CollectionViewModel::class.java))
            return CollectionViewModel(mParam) as T
        throw IllegalAccessException("Unknown ViewModel class")
    }


}