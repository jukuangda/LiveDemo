package tv.newtv.demo.livedemo.util

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import tv.newtv.demo.livedemo.LiveDemoApp
import tv.newtv.demo.livedemo.data.bean.ChannelBean

object ExcelUtil {
    fun getChannelsByAssets(): List<ChannelBean> {
        val channels = ArrayList<ChannelBean>()
        val workBook = WorkbookFactory.create(LiveDemoApp.appContext.resources.assets.open("channels.xlsx"))
        val sheet = workBook.getSheetAt(0)
        for (i in 1 until sheet.lastRowNum) {
            val row = sheet.getRow(i)
            val channel = ChannelBean()
            for (j in 0 until row.lastCellNum) {
                val cell = row.getCell(j)
                cell.setCellType(CellType.STRING)
                when (j) {
                    0 -> {
                        channel.name = cell.stringCellValue
                    }
                    1 -> {
                        channel.url = cell.stringCellValue
                    }
                    2 -> {
                        channel.type = cell.stringCellValue
                    }
                }
            }
            channels.add(channel)
        }
        return channels
    }
}