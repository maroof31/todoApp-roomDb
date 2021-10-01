package com.maroof.todoapproom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var dateEdt:TextInputEditText
    lateinit var timeInptLay:TextInputLayout
    lateinit var timeEdt:TextInputEditText
    lateinit var spinnerCategory:Spinner
    var finalDate = 0L
    var finalTime = 0L


    private val labels = arrayListOf("Personal", "Business", "Insurance", "Shopping", "Banking")

    val db by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
      dateEdt=findViewById<TextInputEditText>(R.id.dateEdt)
        val timeEdt=findViewById<TextInputEditText>(R.id.timeEdt)
        val saveBtn=findViewById<MaterialButton>(R.id.saveBtn)
        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)

          setUpSpinner()
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateEdt -> {
                setListener()
            }
            R.id.timeEdt -> {
               timeSetListner()
            }
            R.id.saveBtn -> {
               saveTodo()
            }
        }

    }

    private  fun saveTodo(){
        val category = spinnerCategory.selectedItem.toString()
        val titleInpLay=findViewById<TextInputLayout>(R.id.timeInptLay)
        val taskInpLay=findViewById<TextInputLayout>(R.id.taskInpLay)
        val title = titleInpLay.editText?.text.toString()
        val description = taskInpLay.editText?.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            val id = withContext(Dispatchers.IO) {
                return@withContext db.todoDao().insertTask(
                    TodoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                    )
                )
            }
            finish()
        }
    }

  private  fun setUpSpinner(){
         val adapter=ArrayAdapter<String>(
             this,
             android.R.layout.simple_spinner_dropdown_item,
             labels
         )
      labels.sort()
      spinnerCategory=findViewById(R.id.spinnerCategory)
      spinnerCategory.adapter=adapter
  }

   private fun timeSetListner(){
       myCalendar = Calendar.getInstance()

       timeSetListener =
           TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, min: Int ->
               myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
               myCalendar.set(Calendar.MINUTE, min)
               updateTime()

           }

       val timePickerDialog = TimePickerDialog(
           this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
           myCalendar.get(Calendar.MINUTE),false)

       timePickerDialog.show()
   }

    private fun updateTime() {
        val myFormat="h:mm a"
        val sdf=SimpleDateFormat(myFormat)
        timeEdt=findViewById(R.id.timeEdt)
        timeEdt.setText(sdf.format(myCalendar.time))
    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()

            }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        //Mon, 5 Jan 2020
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        dateEdt.setText(sdf.format(myCalendar.time))
     timeInptLay=findViewById(R.id.timeInptLay)
        timeInptLay.visibility = View.VISIBLE

    }

}
