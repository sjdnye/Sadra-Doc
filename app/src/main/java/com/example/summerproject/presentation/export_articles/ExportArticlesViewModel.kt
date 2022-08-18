package com.example.summerproject.presentation.export_articles

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summerproject.data.local.Article
import com.example.summerproject.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class ExportArticlesViewModel @Inject constructor(
    private val repository: ArticleRepository,
    private val application: Application
) : ViewModel() {

    var numberOfArticles by mutableStateOf<Int>(0)
    var articles by mutableStateOf<List<Article>?>(null)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun exportArticlesToExcel() {
        if (isAccessGranted()) {
            if (numberOfArticles == 0) {
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

    fun getArticlesByYear(year: String) {
        viewModelScope.launch {
            articles = repository.getArticlesByYear(year)
            numberOfArticles = if (articles != null) {
                articles!!.size
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
        val sheet: Sheet = wb.createSheet("Articles Excel Sheet")
        //Now column and row
        val row: Row = sheet.createRow(0)

        cell = row.createCell(0)
        cell.setCellValue("1'st author's name")
        cell = row.createCell(1)
        cell.setCellValue("1'st author's family")

        cell = row.createCell(2)
        cell.setCellValue("2'nd author's name")
        cell = row.createCell(3)
        cell.setCellValue("2'nd author's family")

        cell = row.createCell(4)
        cell.setCellValue("3'rd author's name")
        cell = row.createCell(5)
        cell.setCellValue("3'rd author's family")

        cell = row.createCell(6)
        cell.setCellValue("English title")

        cell = row.createCell(7)
        cell.setCellValue("Vol.")

        cell = row.createCell(8)
        cell.setCellValue("No.")

        cell = row.createCell(9)
        cell.setCellValue("Institute")

        cell = row.createCell(10)
        cell.setCellValue("Magazine's name")

        cell = row.createCell(11)
        cell.setCellValue("Magazine's type")

        cell = row.createCell(12)
        cell.setCellValue("Year")


        //column width
        sheet.setColumnWidth(0, 30 * 200)
        sheet.setColumnWidth(1, 30 * 200)
        sheet.setColumnWidth(2, 30 * 200)
        sheet.setColumnWidth(3, 30 * 200)
        sheet.setColumnWidth(4, 30 * 200)
        sheet.setColumnWidth(5, 30 * 200)
        sheet.setColumnWidth(6, 200 * 200)
        sheet.setColumnWidth(7, 10 * 200)
        sheet.setColumnWidth(8, 10 * 200)
        sheet.setColumnWidth(9, 180 * 200)
        sheet.setColumnWidth(10, 70 * 200)
        sheet.setColumnWidth(11, 20 * 200)
        sheet.setColumnWidth(12, 10 * 200)


        for (i in 0 until articles!!.size) {
            val row1: Row = sheet.createRow(i + 1)
            cell = row1.createCell(0)
            cell.setCellValue(articles!![i].authorName_1)

            cell = row1.createCell(1)
            cell.setCellValue(articles!![i].authorFamily_1)

            cell = row1.createCell(2)
            cell.setCellValue(articles!![i].authorName_2)

            cell = row1.createCell(3)
            cell.setCellValue(articles!![i].authorFamily_2)

            cell = row1.createCell(4)
            cell.setCellValue(articles!![i].authorName_3)

            cell = row1.createCell(5)
            cell.setCellValue(articles!![i].authorFamily_3)

            cell = row1.createCell(6)
            cell.setCellValue(articles!![i].englishTitle)

            cell = row1.createCell(7)
            cell.setCellValue(articles!![i].vol)

            cell = row1.createCell(8)
            cell.setCellValue(articles!![i].No)

            cell = row1.createCell(9)
            cell.setCellValue(articles!![i].institute)

            cell = row1.createCell(10)
            cell.setCellValue(articles!![i].articleTitle)

            cell = row1.createCell(11)
            cell.setCellValue(articles!![i].articleType)

            cell = row1.createCell(12)
            cell.setCellValue(articles!![i].year)


            //column width
            sheet.setColumnWidth(0, 30 * 200)
            sheet.setColumnWidth(1, 30 * 200)
            sheet.setColumnWidth(2, 30 * 200)
            sheet.setColumnWidth(3, 30 * 200)
            sheet.setColumnWidth(4, 30 * 200)
            sheet.setColumnWidth(5, 30 * 200)
            sheet.setColumnWidth(6, 200 * 200)
            sheet.setColumnWidth(7, 10 * 200)
            sheet.setColumnWidth(8, 10 * 200)
            sheet.setColumnWidth(9, 180 * 200)
            sheet.setColumnWidth(10, 70 * 200)
            sheet.setColumnWidth(11, 20 * 200)
            sheet.setColumnWidth(12, 10 * 200)
        }
        val folderName = "Export Excel"
        val fileName = "${folderName}${System.currentTimeMillis()}.xls"


        try {

            val file = File("/storage/emulated/0/${File.separator}${folderName}${File.separator}${fileName}")
            val folder = File("/storage/emulated/0/${File.separator}${folderName}")
            if (!folder.exists()){
                folder.mkdir()
            }
            if (!file.exists()){
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

