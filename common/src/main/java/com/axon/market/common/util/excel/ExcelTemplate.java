package com.axon.market.common.util.excel;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EXCEL报表模板
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author zengcr
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ExcelTemplate
{
    private static final Logger log = Logger.getLogger(ExcelTemplate.class);

    private final String DATE_STYLE = "yyyy-MM-dd";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_STYLE);

    private Workbook workbook = null;

    private Sheet sheet = null;

    private int rowItemsCol = -1;

    private Map<Integer, String> rowItems = null;

    /**
     * 禁止实例化
     */
    private ExcelTemplate()
    {

    }

    private ExcelTemplate(Workbook workbook, int sheetIdx)
    {
        this.workbook = workbook;
        this.sheet = workbook.getSheetAt(0);
        this.sheet.setForceFormulaRecalculation(true);
    }

    /**
     * 创建Excel模板
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param filePath
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static ExcelTemplate createTemplate(String filePath)
    {
        InputStream is = ExcelTemplate.class.getClassLoader().getResourceAsStream(filePath);
        ExcelTemplate template = new ExcelTemplate();
        if (template.loadTemplate(is))
        {
            return template;
        }
        else
        {
            return null;
        }
    }

    public static ExcelTemplate createTemplate(File file) throws Exception
    {
        InputStream is = new FileInputStream(file);
        ExcelTemplate template = new ExcelTemplate();
        if (template.loadTemplate(is))
        {
            return template;
        }
        else
        {
            return null;
        }
    }

    /**
     * 从Sheet创建模板
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param sheetIdx
     * @return
     * @throws Exception
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public ExcelTemplate openTemplate(int sheetIdx) throws Exception
    {
        if (sheetIdx > -1 && sheetIdx < this.workbook.getNumberOfSheets())
        {
            return new ExcelTemplate(this.workbook, sheetIdx);
        }
        else
        {
            log.error("Open Excel Template failed: Invalid Sheet Index");
            return null;
        }
    }

    private boolean loadTemplate(InputStream is)
    {
        boolean result = false;
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
                result = true;
            }
            else if (POIXMLDocument.hasOOXMLHeader(is))
            {
                workbook = new XSSFWorkbook(is);
                sheet = workbook.getSheetAt(0);
                sheet.setForceFormulaRecalculation(true);
                result = true;
            }
            else
            {
                log.error("Template is Not Excel File");
            }
        }
        catch (Exception e)
        {
            log.error("Create Excel Template failed: " + e.getMessage());
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
        return result;
    }

    public Workbook getWorkbook()
    {
        return this.workbook;
    }

    /**
     * 获取行数
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public int getRowsNumber()
    {
        return this.sheet.getPhysicalNumberOfRows();
    }

    /**
     * 写入HttpResponse
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param response
     * @param fileName
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void saveToHttpResponse(HttpServletResponse response, String fileName)
    {
        try
        {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            this.workbook.write(response.getOutputStream());
        }
        catch (IOException e)
        {
            log.error("Save Excel to HttpServletResponse failed: " + e.getMessage());
        }
    }

    /**
     * 保存为文件
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param filepath
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * 顺序数据写入
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param beginRow
     * @param data
     * @param rowWriter
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void writeData(int beginRow, List<Map<String, Object>> data, RowWriter rowWriter)
    {
        for (int i = 0; i < data.size(); i++)
        {
            Map<String, Object> rowd = data.get(i);
            rowWriter.write(this, beginRow + i, rowd);
        }
    }

    /**
     * 按行项目写入
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param items
     * @param data
     * @param rowWriter
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void writeItems(Map<Integer, String> items, Map<String, Map<String, ?>> data, RowWriter rowWriter)
    {
        for (Integer row : items.keySet())
        {
            Object rowKey = items.get(row);
            Map<String, ?> rowData = data.get(rowKey);
            if (rowData != null)
            {
                rowWriter.write(this, row, rowData);
            }
        }
    }

    /**
     * 写入一行数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param row
     * @param beginCol
     * @param rowdata
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void writeRow(int row, int beginCol, Object[] rowdata)
    {
        for (int i = 0; i < rowdata.length; i++)
        {
            writeCell(row, beginCol + i, rowdata[i]);
        }
    }

    /**
     * 写入单元格数据
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param rowIndex
     * @param colIndex
     * @param value
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void writeCell(int rowIndex, int colIndex, Object value)
    {
        Row row = sheet.getRow(rowIndex);
        if (null == row)
        {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(colIndex);
        if (null == cell)
        {
            cell = row.createCell(colIndex);
        }

        if (value == null)
        {
            return;
        }
        else if (value instanceof String)
        {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((String) value);
        }
        else if (Number.class.isAssignableFrom(value.getClass()))
        {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Number) value).doubleValue());
        }
        else if (value instanceof Date)
        {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(dateFormat.format((Date) value));
        }
        else
        {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(String.valueOf(value));
        }
    }

    /**
     * 写入图片
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param resize
     * @param is
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void writePicture(int row, int col, double resize, InputStream is)
    {
        try
        {
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            CreationHelper helper = workbook.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setRow1(row);
            anchor.setCol1(col);
            anchor.setDx1(0);
            anchor.setDy1(0);
            Drawing drawing = sheet.createDrawingPatriarch();
            Picture picture = drawing.createPicture(anchor, pictureIdx);
            picture.resize(resize);
        }
        catch (Exception e)
        {
            log.error("Write Picture Failed: " + e.getMessage());
        }
    }

    /**
     * 合并单元格
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     * @param value
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void mergeCells(int row1, int col1, int row2, int col2, Object value)
    {
        CellRangeAddress cra = new CellRangeAddress(row1, row2, col1, col2);
        sheet.addMergedRegion(cra);
        if (value != null)
        {
            writeCell(row1, col1, value);
        }
    }

    /**
     * 获取行项目，可以writeItems
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param colIndex
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public Map<Integer, String> getRowItems(int colIndex)
    {
        if (colIndex != this.rowItemsCol)
        {
            this.rowItemsCol = colIndex;
            this.rowItems = new HashMap<Integer, String>();
            for (int rowNo = 0; rowNo < sheet.getPhysicalNumberOfRows(); rowNo++)
            {
                Row row = sheet.getRow(rowNo);
                Cell cell = row.getCell(colIndex);
                if (cell != null)
                {
                    Object item = getCellValue(cell);
                    if (item != null && !"".equals(item))
                    {
                        rowItems.put(rowNo, String.valueOf(item));
                    }
                    cell.setCellValue("");
                }
            }
        }
        return this.rowItems;
    }

    /**
     * 读取单元格（合并单元格不适用）
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param rowIndex
     * @param colIndex
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
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

    /**
     * 删除行
     * 功能描述: <br>
     * 〈功能详细描述〉
     *
     * @param start
     * @param end
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void removeRows(int start, int end)
    {
        for (int i = start; i <= end; i++)
        {
            Row row = sheet.getRow(i);
            if (row != null)
            {
                sheet.removeRow(row);
            }
        }
    }

    /**
     * 行写入接口，定制行写入
     * 〈一句话功能简述〉<br>
     * 〈功能详细描述〉
     *
     * @author 15050873
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public interface RowWriter
    {
        public void write(ExcelTemplate template, int rowIndex, Map<String, ?> data);
    }

}
