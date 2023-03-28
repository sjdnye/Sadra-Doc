package com.example.consignmentProject.presentation.export_consignments.admin_export

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.consignmentProject.data.local.Consignment
import com.example.utils.CONSIGNMENT_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AdminExportDataViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val application: Application
) : ViewModel() {

    var isMonthSwitch by mutableStateOf(false)
    var isYearSwitch by mutableStateOf(false)

    var numberOfConsignments by mutableStateOf<Int>(0)
    var consignments by mutableStateOf<List<Consignment>>(emptyList())
    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = MutableSharedFlow<AdminUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var job: Job? = null
    private var jobAll: Job? = null

    var selectedYear by mutableStateOf("")
    var selectedMonth by mutableStateOf("")


    fun exportConsignmentsToExcel() {
        if (isAccessGranted()) {
            if (numberOfConsignments == 0) {
                viewModelScope.launch {
                    _eventFlow.emit(AdminUiEvent.ShowSnackBar("There is nothing to export!!"))
                }
            } else {
                createAdminX1File()
            }
        } else {
            viewModelScope.launch {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar("Please grant read/write storage permission"))
            }
        }
    }


    fun getConsignments() {
        try {
            val mapMonthToNumber: Int = when (selectedMonth) {
                "فروردین" -> 1
                "اردیبهشت" -> 2
                "خرداد" -> 3
                "تیر" -> 4
                "مرداد" -> 5
                "شهریور" -> 6
                "مهر" -> 7
                "آبان" -> 8
                "آذر" -> 9
                "دی" -> 10
                "بهمن" -> 11
                "اسفند" -> 12
                else -> 0
            }
            if (isYearSwitch && isMonthSwitch) {
                if (!selectedYear.isNullOrBlank() && !selectedMonth.isNullOrBlank()) {
                    getConsignmentsByYearAndMonth(
                        year = selectedYear.toInt(),
                        month = mapMonthToNumber
                    )
                }
            } else if (isYearSwitch) {
                if (!selectedYear.isNullOrBlank()) {
                    getConsignmentsByYear(year = selectedYear.toInt())
                }
            } else {
                if (mapMonthToNumber > 0) {
                    getConsignmentsByMonth(month = mapMonthToNumber)
                }
            }

        } catch (e: Exception) {
            viewModelScope.launch {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar(e.message.toString()))
            }
        }
    }

    private fun getConsignmentsByYear(year: Int) {
        consignments = emptyList()
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(500L)
            isLoading = true
            try {
                val tempConsignments = mutableListOf<Consignment>()
                val querySnapshot = firestore.collection(CONSIGNMENT_COLLECTION)
                    .whereEqualTo("year", year)
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    document.toObject<Consignment>()?.let { tempConsignments.add(it) }
                }
                consignments = tempConsignments
                numberOfConsignments = if (consignments.isNotEmpty()) {
                    consignments.size
                } else {
                    0
                }
            } catch (e: Exception) {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar(e.message.toString()))
            }
            isLoading = false
        }
    }

    private fun getConsignmentsByMonth(month: Int) {
        consignments = emptyList()
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(500L)
            isLoading = true
            try {
                val tempConsignments = mutableListOf<Consignment>()
                val querySnapshot = firestore.collection(CONSIGNMENT_COLLECTION)
                    .whereEqualTo("month", month)
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    document.toObject<Consignment>()?.let { tempConsignments.add(it) }
                }
                consignments = tempConsignments
                numberOfConsignments = if (consignments.isNotEmpty()) {
                    consignments.size
                } else {
                    0
                }
            } catch (e: Exception) {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar(e.message.toString()))
            }
            isLoading = false
        }

    }

    private fun getConsignmentsByYearAndMonth(year: Int, month: Int) {
        consignments = emptyList()
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(500L)
            isLoading = true
            try {
                val tempConsignments = mutableListOf<Consignment>()
                val querySnapshot = firestore.collection(CONSIGNMENT_COLLECTION)
                    .whereEqualTo("year", year)
                    .whereEqualTo("month", month)
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    document.toObject<Consignment>()?.let { tempConsignments.add(it) }
                }
                consignments = tempConsignments
                numberOfConsignments = if (consignments.isNotEmpty()) {
                    consignments.size
                } else {
                    0
                }
            } catch (e: Exception) {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar(e.message.toString()))
            }
            isLoading = false
        }

    }

    fun getAllConsignments() {
        consignments = emptyList()
        jobAll?.cancel()
        jobAll = CoroutineScope(Dispatchers.IO).launch {
            isLoading = true
            try {
                val tempConsignments = mutableListOf<Consignment>()
                val querySnapshot = firestore.collection(CONSIGNMENT_COLLECTION)
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    document.toObject<Consignment>()?.let { tempConsignments.add(it) }
                }
                consignments = tempConsignments
                numberOfConsignments = if (consignments.isNotEmpty()) {
                    consignments.size
                } else {
                    0
                }
            } catch (e: Exception) {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar("Error : " + e.message.toString()))
            }
            isLoading = false
        }
    }


    private fun createAdminX1File() {
        val wb: Workbook = HSSFWorkbook()
        var cell: Cell?
        val sheet: Sheet = wb.createSheet("Consignment Excel Sheet")
        //Now column and row
        val row: Row = sheet.createRow(0)

        cell = row.createCell(0)
        cell.setCellValue("Title")
        cell = row.createCell(1)
        cell.setCellValue("Document No.")
        cell = row.createCell(2)
        cell.setCellValue("Weight")
        cell = row.createCell(3)
        cell.setCellValue("Cost")
        cell = row.createCell(4)
        cell.setCellValue("Date")

        //column width
        sheet.setColumnWidth(0, 60 * 200)
        sheet.setColumnWidth(1, 20 * 200)
        sheet.setColumnWidth(2, 10 * 200)
        sheet.setColumnWidth(3, 10 * 200)
        sheet.setColumnWidth(4, 20 * 200)


        for (i in 0 until consignments!!.size) {
            val row1: Row = sheet.createRow(i + 1)
            cell = row1.createCell(0)
            cell.setCellValue(consignments!![i].title)

            cell = row1.createCell(1)
            cell.setCellValue(consignments!![i].documentNumber.toString())

            cell = row1.createCell(2)
            cell.setCellValue(consignments!![i].weight.toString())

            cell = row1.createCell(3)
            cell.setCellValue(consignments!![i].cost.toString())

            cell = row1.createCell(4)
            cell.setCellValue(
                "${consignments!![i].year.toString()}\\${consignments!![i].month.toString()}\\${consignments!![i].day.toString()}"
            )


            //column width
            sheet.setColumnWidth(0, 60 * 200)
            sheet.setColumnWidth(1, 20 * 200)
            sheet.setColumnWidth(2, 10 * 200)
            sheet.setColumnWidth(3, 10 * 200)
            sheet.setColumnWidth(4, 20 * 200)
        }
        val folderName = "Import Excel"
        val fileName = "${folderName}${System.currentTimeMillis()}.xls"
        try {

            val file =
                File("/storage/emulated/0/${File.separator}${folderName}${File.separator}${fileName}")
            val folder = File("/storage/emulated/0/${File.separator}${folderName}")
            if (!folder.exists()) {
                folder.mkdir()
            }
            if (!file.exists()) {
                file.createNewFile()
            }
            val outputStream = FileOutputStream(file)
            wb.write(outputStream)

            outputStream.flush()
            outputStream.close()

            viewModelScope.launch {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar("Articles added to  excel file successfully!"))
            }

        } catch (e: Exception) {
            viewModelScope.launch {
                _eventFlow.emit(AdminUiEvent.ShowSnackBar("An error occurred : ${e.message.toString()}"))
            }
        }
    }

    fun isAccessGranted(): Boolean {
        val hasAccessWriteStoragePermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasAccessReadStoragePermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


        if (!hasAccessReadStoragePermission || !hasAccessWriteStoragePermission) {
            return false
        }
        return true
    }

    sealed class AdminUiEvent {
        data class ShowSnackBar(val message: String) : AdminUiEvent()
    }
}