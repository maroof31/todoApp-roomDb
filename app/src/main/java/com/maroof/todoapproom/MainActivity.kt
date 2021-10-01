package com.maroof.todoapproom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var list = arrayListOf<TodoModel>()
    val title="gnd"
    val category ="category"
    val description = "descriptio jnd"
    val date:Long=23432475297
    val time:Long=99477547547
    val isf=-1
    lateinit var simpleItemTouchcallback:ItemTouchHelper.SimpleCallback
    val d:TodoModel= TodoModel(title,description,category,date,time,isf)
   lateinit var adapter:TodoAdapter


    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        adapter = TodoAdapter(list)
        Log.d("${list.size}","listsize")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val todoRv = findViewById<RecyclerView>(R.id.rvtodo)
        todoRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            Log.d("hhh","dkjdsk")
        }

        db.todoDao().getTask().observe(this, Observer
        {
            if (!it.isNullOrEmpty()) {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            } else {
                list.clear()
                Log.d("${list.size}","listsize")
                adapter.notifyDataSetChanged()
            }
        })
        initswipe()
        val itemTouchHelper=ItemTouchHelper(simpleItemTouchcallback)
        itemTouchHelper.attachToRecyclerView(todoRv)
    }

   fun initswipe() {
        simpleItemTouchcallback = object : ItemTouchHelper.SimpleCallback(
           0,
           ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
           override fun onMove(
               recyclerView: RecyclerView,
               viewHolder: RecyclerView.ViewHolder,
               target: RecyclerView.ViewHolder
           ): Boolean
           {
              return false
           }

           override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)
            {
               val position=viewHolder.adapterPosition
               if(direction==ItemTouchHelper.LEFT){
                   GlobalScope.launch(Dispatchers.Main) {
                       db.todoDao().deteleTask(adapter.getItemId(position))
                   }

               }else if(direction==ItemTouchHelper.RIGHT){
                   GlobalScope.launch(Dispatchers.Main){
                       db.todoDao().finishTask(adapter.getItemId(position))
                       adapter.notifyItemChanged(position)
                   }
               }
           }






       }

   }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history->{
                startActivity(Intent(this,HistoryActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun openNewTask(view: View) {
        startActivity(Intent(this, TaskActivity::class.java))
    }
}