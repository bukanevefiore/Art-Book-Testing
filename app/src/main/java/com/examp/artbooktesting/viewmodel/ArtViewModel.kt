package com.examp.artbooktesting.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examp.artbooktesting.model.ImageResponse
import com.examp.artbooktesting.repo.ArtRepositoryInterface
import com.examp.artbooktesting.roomdb.Art
import com.examp.artbooktesting.util.Resource
import kotlinx.coroutines.launch

//@HiltViewModel
class ArtViewModel @ViewModelInject constructor(
        private val repository: ArtRepositoryInterface
) : ViewModel() {

    // Art Fragment
    val artList = repository.getArt()

    // Image Api fragment
    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList : LiveData<Resource<ImageResponse>> get() = images

    private val selectedImage =MutableLiveData<String>()
    val selectedImageUrl : LiveData<String> get() = selectedImage

    //Art Details Fragment
    private var inserArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>> get() = inserArtMsg

    fun resetInsertMsg(){
        inserArtMsg = MutableLiveData<Resource<Art>>()
    }

    // fonksiyonlar..
    fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art) = viewModelScope.launch{
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) =viewModelScope.launch {
        repository.insertAll(art)
    }

    // art ekleme kısmında edittextler için boş ve değişken türü kontrolü
    fun makeArt(name: String,artistName: String,year: String){
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()){
            inserArtMsg.postValue(Resource.error("Enter name,artist,yaer",null))
            return
        }

        val yearInt = try {
            year.toInt()
        }catch (e: Exception) {
            inserArtMsg.postValue(Resource.error("Year should be number",null))
            return
        }

        val art = Art(name,artistName,yearInt,selectedImage.value?: "")
        insertArt(art)
        setSelectedImage("")
        inserArtMsg.postValue(Resource.success(art))
    }


    fun searchForImage(searcString: String) {

        if(searcString.isEmpty()){
            return
        }

        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searcString)
            images.value = response
        }
    }

}