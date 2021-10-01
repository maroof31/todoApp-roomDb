package com.maroof.todoapproom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    @Insert
    suspend fun insertTask(todoModel: TodoModel):Long

    //get all unfinished tasks
    @Query("SELECT * FROM TodoModel WHERE isFinished!=0")
    fun getTask():LiveData<List<TodoModel>>

    //FINISH A TASK
    @Query("UPDATE todomodel SET isFinished=1 WHERE ID=:uid")
    fun finishTask(uid:Long)

    //delete a task
    @Query("DELETE FROM TodoModel WHERE ID=:uid")
    suspend fun deteleTask(uid:Long)
}