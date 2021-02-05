package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity

@Dao
interface ElectionDao {

    @Update
    suspend fun updateElection(election: ElectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg electionEntity: ElectionEntity)

    @Query("select * from election_table where isFollowed = 1")
    fun getFollowedElections(): LiveData<List<ElectionEntity>>

    @Query("Select * from election_table")
    fun getAllElections(): LiveData<List<ElectionEntity>>

    @Query("select * from election_table where id = :id")
    fun getElectionById(id: Int) : LiveData<ElectionEntity>

    @Delete
    suspend fun deleteElection(election: ElectionEntity)

    @Query("delete from election_table")
    suspend fun clear()

}