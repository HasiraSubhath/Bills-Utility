package com.example.kotlin_bill.models

data class EmployeeModel(
    var billId: String? = null,
    var billType: String? = null,
    var billAmount: String? = null,
    var billNotes: String? = null
)