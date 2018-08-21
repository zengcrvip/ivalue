package com.axon.market.common.util.excel;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * EXCEL报表模板
 * Created by zengcr on 2016/12/5.
 */
public class ExcelReader
{
    private static final Logger log = Logger.getLogger(ExcelReader.class);

    private Workbook workbook = null;
    private Sheet sheet = null;

    public ExcelReader(InputStream is) throws Exception
    {
        try
        {
            if (!is.markSupported())
            {
                is = new PushbackInputStream(is, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(is))
            {
                workbook = new HSSFWorkbook(is);
                sheet = workbook.getSheetAt(0);
                sheet.setForceFormulaRecalculation(true);
            }
            else if (POIXMLDocument.hasOOXMLHeader(is))
            {
                workbook = new XSSFWorkbook(is);
                sheet = workbook.getSheetAt(0);
                sheet.setForceFormulaRecalculation(true);
            }
            else
            {
                log.error("Template is Not Excel File");
                throw new RuntimeException("不是有效的xls文件或xlsx文件");
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    public Workbook getWorkbook()
    {
        return this.workbook;
    }

    public int getRowNums()
    {
        return this.sheet.getPhysicalNumberOfRows();
    }

    /**
     * 保存为文件
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param filepath
     */
    public void saveAsFile(String filepath)
    {
        FileOutputStream fos = null;
        try
        {
            File file = new File(filepath);
            fos = new FileOutputStream(file);
            workbook.write(fos);
        }
        catch (Exception e)
        {
            log.error("Save Excel as File Failed: " + e.getMessage());
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (Exception e)
            {

            }
        }
    }

    /**
     * 读取单元格（合并单元格不适用）
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public Object readCell(int rowIndex, int colIndex)
    {
        Row row = sheet.getRow(rowIndex);
        if (row != null)
        {
            Cell cell = row.getCell(colIndex);
            return getCellValue(cell);
        }
        else
        {
            return null;
        }
    }

    /**
     * 读取行（合并单元格不适用）
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param rowIndex
     * @return
     */
    public String[] readRow(int rowIndex)
    {
        Row row = sheet.getRow(rowIndex);
        if (row != null)
        {
            List<String> list = new ArrayList<String>();
            int count = 0;
            for (int i = 0; count < row.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = row.getCell(i);
                if (cell != null)
                {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    list.add(cell.getStringCellValue());
                    count++;
                }
                else
                {
                    list.add(null);
                }
            }
            return list.toArray(new String[0]);
        }
        else
        {
            return null;
        }
    }

    /**
     * 获取单元格（合并单元格不适用）
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell)
    {
        String item = null;
        if (cell != null)
        {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            item = cell.getStringCellValue();
        }
        return item;
    }
}
