package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.dao.PhotoDao
import com.openclassrooms.realestatemanager.models.Photo


class PhotoDataRepository(val photoDao: PhotoDao) {

    // --- GET ---

    fun getPhotos(realEstateId: Long): LiveData<List<Photo>> {
        return this.photoDao.getPhotos(realEstateId)
    }

    // --- CREATE ---

    fun createPhoto(photo: Photo) {
        photoDao.insertPhoto(photo)
    }

    // --- DELETE ---
    fun deletePhoto(photoId: Long) {
        photoDao.deletePhoto(photoId)
    }

    // --- UPDATE ---
    fun updatePhoto(photo: Photo) {
        photoDao.updatePhoto(photo)
    }

}
