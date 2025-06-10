package com.example.mochilar.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TravelDao {
    @Insert
    suspend fun insertTravel(travel: Travel)

    @Query("SELECT * FROM travels WHERE userId = :userId")
    suspend fun getTravelsByUser(userId: Int): List<Travel>

    @Query("DELETE FROM travels WHERE id = :travelId")
    suspend fun deleteTravel(travelId: Int)

    @Update
    suspend fun updateTravel(travel: Travel)

    @Query("SELECT * FROM travels WHERE id = :travelId LIMIT 1")
    suspend fun getTravelById(travelId: Int): Travel?
}