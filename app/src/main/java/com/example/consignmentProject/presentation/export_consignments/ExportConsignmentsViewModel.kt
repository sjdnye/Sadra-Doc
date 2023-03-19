package com.example.consignmentProject.presentation.export_consignments

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
import com.example.consignmentProject.domain.repository.ConsignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class ExportConsignmentsViewModel @Inject constructor(
    private val repository: ConsignmentRepository,
    private val application: Application
) : ViewModel() {

    var numberOfConsignments by mutableStateOf<Int>(0)
    var consignments by mutableStateOf<List<Consignment>?>(null)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun exportConsignmentsToExcel() {
        if (isAccessGranted()) {
            if (numberOfConsignments == 0) {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackBar("There is nothing to export!!"))
                }
            } else {
                createX1File()
            }

        } else {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackBar("Please grant read/write storage permission"))
            }

        }

    }

    fun getConsignmentsByYear(year: String) {
        viewModelScope.launch {
            consignments = repository.getConsignmentsByYear(year.toInt())
            numberOfConsignments = if (consignments != null) {
                consignments!!.size
            } else {
                0
            }
        }
    }

    private fun isAccessGranted(): Boolean {
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

    private fun createX1File() {
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
        sheet.setColumnWidth(0, 30 * 200)
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
            sheet.setColumnWidth(0, 30 * 200)
            sheet.setColumnWidth(1, 20 * 200)
            sheet.setColumnWidth(2, 10 * 200)
            sheet.setColumnWidth(3, 10 * 200)
            sheet.setColumnWidth(4, 20 * 200)
        }
        val folderName = "Export Excel"
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
                _eventFlow.emit(UiEvent.ShowSnackBar("Excel file was created in \"/storage/emulated/0/Export Excel/\" successfully!"))
            }

        } catch (e: Exception) {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackBar("An error occurred : ${e.message.toString()}"))
            }
        }
    }
}

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
}

