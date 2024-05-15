package com.example.taskManagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskappnew.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

// Define the VALID_PRIORITIES constant outside of the Task class
val VALID_PRIORITIES = setOf("1", "2", "3")

class AddEditTaskActivity : AppCompatActivity() {
    lateinit var taskTitleEdt : EditText
    lateinit var taskDesscriptionEdt : EditText
    lateinit var taskPriorityEdt : EditText
    lateinit var taskDueDateEdt : EditText
    lateinit var addupdateBtn : Button
    lateinit var viewModel: TaskViewModel
    var taskID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)
        taskTitleEdt = findViewById(R.id.idEditTaskTitle)
        taskDesscriptionEdt = findViewById(R.id.idEditTaskDescription)
        taskPriorityEdt = findViewById(R.id.idEditTaskPriority)
        taskDueDateEdt = findViewById(R.id.idEditTaskDueDate)
        addupdateBtn = findViewById(R.id.idBtnAddUpdate)
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            TaskViewModel::class.java)

        val taskType = intent.getStringExtra("taskType")
        if(taskType.equals("Edit")){
            val taskTitle = intent.getStringExtra("taskTitle")
            val taskDesc = intent.getStringExtra("taskDescription")
            val taskPriority = intent.getStringExtra("taskPriority")
            val taskDueDate = intent.getStringExtra("taskDueDate")
            taskID = intent.getIntExtra("taskID", -1)
            addupdateBtn.setText("Update Task")
            taskTitleEdt.setText(taskTitle)
            taskDesscriptionEdt.setText(taskDesc)
            taskPriorityEdt.setText(taskPriority)
            taskDueDateEdt.setText(taskDueDate)
        }else{
            addupdateBtn.setText("Save Task")
        }

        addupdateBtn.setOnClickListener {
            val taskTitle = taskTitleEdt.text.toString()
            val taskDescription = taskDesscriptionEdt.text.toString()
            val taskPriority = taskPriorityEdt.text.toString()
            val taskDueDate = taskDueDateEdt.text.toString()

            // Check for empty fields
            if (taskTitle.isEmpty() || taskDescription.isEmpty()) {
                Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate due date format
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            sdf.isLenient = false
            try {
                sdf.parse(taskDueDate) // If this line throws an exception, then the format is invalid
            } catch (e: ParseException) {
                Toast.makeText(this, "Invalid due date format", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validate priority
            if (taskPriority !in VALID_PRIORITIES) {
                Toast.makeText(this, "Invalid priority", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (taskType.equals("Edit")){
                if(taskTitle.isNotEmpty() && taskDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val currentDate:String = sdf.format(Date())
                    val updateTask = Task(taskTitle,taskDescription,taskPriority,taskDueDate,currentDate)
                    updateTask.id = taskID
                    viewModel.updateTask(updateTask)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            }else{
                if (taskTitle.isNotEmpty() && taskDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val currentDate:String = sdf.format(Date())
                    viewModel.addTask(Task(taskTitle, taskDescription, taskPriority, taskDueDate, currentDate))
                    Toast.makeText(this, "Note Added..", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
            }
    }
}
