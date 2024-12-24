package com.abdullah.lostfound.Repositories

import com.abdullah.lostfound.dataSource.CloudinaryUploadHelper


class StorageRepository {

    fun uploadFile(filePath:String,onComplete: (Boolean,String?) -> Unit){
        CloudinaryUploadHelper().uploadFile(filePath,onComplete)
    }
}