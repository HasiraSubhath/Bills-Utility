package com.example.kotlin_bill.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kotlin_bill.R
import com.example.kotlin_bill.models.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var tvBillId: TextView
    private lateinit var tvBillType: TextView
    private lateinit var tvBillAmount: TextView
    private lateinit var tvEmpSalary: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("billId").toString(),
                intent.getStringExtra("billType").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("billId").toString()
            )
        }

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }





    private fun initView() {
        tvBillId = findViewById(R.id.tvBillId)
        tvBillType = findViewById(R.id.tvBillType)
        tvBillAmount = findViewById(R.id.tvBillAmount)
        tvEmpSalary = findViewById(R.id.tvEmpSalary)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //passing data
        tvBillId.text = intent.getStringExtra("billId")
        tvBillType.text = intent.getStringExtra("billType")
        tvBillAmount.text = intent.getStringExtra("billAmount")
        tvEmpSalary.text = intent.getStringExtra("empSalary")

    }

    private fun openUpdateDialog(
        billId: String,
        billType: String

    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etBillType = mDialogView.findViewById<EditText>(R.id.etBillType)
        val etBillAmount = mDialogView.findViewById<EditText>(R.id.etBillAmount)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //update
        etBillType.setText(intent.getStringExtra("billType").toString())
        etBillAmount.setText(intent.getStringExtra("billAmount").toString())
        etEmpSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating $billType Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                billId,
                etBillType.text.toString(),
                etBillAmount.text.toString(),
                etEmpSalary.text.toString()
            )

            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvBillType.text = etBillType.text.toString()
            tvBillAmount.text = etBillAmount.text.toString()
            tvEmpSalary.text = etEmpSalary.text.toString()

            alertDialog.dismiss()

        }

    }

    private fun updateEmpData(
        id: String,
        name: String,
        age: String,
        salary: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val empInfo = EmployeeModel(id, name, age, salary)
        dbRef.setValue(empInfo)
    }
}