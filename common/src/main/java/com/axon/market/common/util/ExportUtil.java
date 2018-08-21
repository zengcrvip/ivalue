package com.axon.market.common.util;

import com.axon.market.common.util.excel.ExcelCellEntity;
import com.axon.market.common.util.excel.ExcelRowEntity;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 功能描述： 导出工具类
 * Created by zengcr on 2016/12/5.
 */
public class ExportUtil
{
    private static final Logger log = Logger.getLogger(ExportUtil.class);

    /**
     * 字符集
     */
    public static final String charset = "GBK";

    /**
     * 换行符
     */
    public static final String LINE_END = "\n";

    /**
     * 功能描述： 输入参数：<导出数据，并且打包>
     *
     * @param headers  表头字段名称,用逗号分隔
     * @param cols     表头字段值,用逗号分隔
     * @param dataList 后台数据源
     * @param rptName  报表名称
     * @param response 返回对象 返回值: 类型 <说明>
     * @throw 异常描述
     */
    public static void exportData(String headers, String cols, List dataList, String rptName,
                                  HttpServletResponse response)
    {
        ZipOutputStream zos = null;
        DataOutputStream dos = null;
        OutputStream outs = null;
        try
        {
            response.setContentType("application/octet-stream;charset=" + charset + " ");
            String contentDisposition = "attachment;   filename= "
                    + new String(rptName.getBytes(charset), "ISO8859-1") + ".zip";
            response.setHeader("Content-Disposition", contentDisposition);

            outs = response.getOutputStream();
            zos = new ZipOutputStream(outs);
            zos.putNextEntry(new ZipEntry(rptName + ".csv"));
            dos = new DataOutputStream(zos);

            String[] keyList = null;
            if (null != cols)
            {
                keyList = cols.split(",");
            }
            // 写入表头
            if (null != headers)
            {
                dos.write((headers + LINE_END).getBytes(charset));
            }

            StringBuffer buf = new StringBuffer();
            Object value = null;
            // 写入数据
            if (null != dataList && 0 < dataList.size())
            {
                // 如果是Map对象
                if (dataList.get(0) instanceof Map)
                {
                    Map curMap = null;
                    for (int idx = 0; idx < dataList.size(); idx++)
                    {
                        curMap = (Map) dataList.get(idx);
                        // 如果cols为空，使用第一个Map的健顺序
                        if (null == keyList)
                        {
                            keyList = (String[]) curMap.keySet().toArray();
                        }
                        // 情况buf
                        buf.delete(0, buf.length());
                        // 逐行写入文件
                        for (int fidx = 0; fidx < keyList.length; fidx++)
                        {
                            value = curMap.get(keyList[fidx]);
                            if (null == value)
                            {
                                // buf.append("--");
                            }
                            else
                            {
                                buf.append(value);
                            }
                            buf.append("\t,");
                        }
                        buf.append(LINE_END);
                        dos.write(buf.toString().getBytes(charset));
                    }
                }
                else if (null != keyList)
                {
                    Object obj = null;
                    for (int idx = 0; idx < dataList.size(); idx++)
                    {
                        obj = dataList.get(idx);
                        if (null == obj)
                        {
                            continue;
                        }
                        Method[] methods = obj.getClass().getMethods();
                        // 情况buf
                        buf.delete(0, buf.length());
                        // 逐行写入文件
                        for (int fidx = 0; fidx < keyList.length; fidx++)
                        {
                            for (int midx = methods.length - 1; midx >= 0; midx--)
                            {
                                if (methods[midx].getName().equalsIgnoreCase("get" + keyList[fidx]))
                                {
                                    value = methods[midx].invoke(obj);
                                    if (null == value)
                                    {
                                        // buf.append("--");
                                    }
                                    else
                                    {
                                        buf.append(value);
                                    }
                                    buf.append("\t,");
                                    break;
                                }
                            }
                        }
                        buf.append(LINE_END);
                        dos.write(buf.toString().getBytes(charset));
                    }
                }
                else
                {
                    // 需要覆盖bean的toString方法
                    Object obj = null;
                    for (int idx = 0; idx < dataList.size(); idx++)
                    {
                        obj = dataList.get(idx);
                        if (null == obj)
                        {
                            continue;
                        }
                        dos.write((obj + LINE_END).getBytes(charset));
                    }
                }
            }

            dos.flush();

        }
        catch (Exception e)
        {
            log.error("导出数据异常：" + e.getMessage());
        }
        finally
        {
            try
            {
                if (null != dos)
                {
                    dos.close();
                }
                if (null != zos)
                {
                    zos.close();
                }
            }
            catch (Exception e)
            {
                log.error("导出数据异常：" + e.getMessage());
            }
        }
    }

    /**
     * 功能描述：导出Excel公共请求 输入参数：<按照参数定义顺序>
     *
     * @return 返回值void 写出 excel文件
     * @request：fileName 文件名 excelDataListList ：数据集合
     * @request：请求对象
     * @response：返回处理对象，设置vnd.ms-excel返回类型及输出流
     * @throw 异常描述
     */
    public void exportData(String fileName, List<ExcelRowEntity> excelDataListList,
                           HttpServletRequest request, HttpServletResponse response)
    {
        OutputStream out = null;
        try
        {
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            if (null == fileName)
            {
                fileName = "Excel_" + System.currentTimeMillis();
            }
            String strFileName = fileName;
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
            // 导出excel
            out = response.getOutputStream();
            exportExcel(excelDataListList, response.getOutputStream(), strFileName);

        }
        catch (Exception e)
        {
            log.error("导出数据异常：" + e.getMessage());
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    log.error("导出数据异常：" + e.getMessage());
                }
            }
        }

    }

    /**
     * 导出图片
     *
     * @param fileName
     * @param request
     * @param response
     */
    public void exportImage(String imgUrl, String fileName,
                            HttpServletRequest request, HttpServletResponse response)
    {
        OutputStream out = null;
        try
        {
            // 创建流
            BufferedInputStream in = new BufferedInputStream(new URL(imgUrl)
                    .openStream());
            // 生成图片名,进行转码，使其支持中文文件名
            if (null == fileName)
            {
                int index = imgUrl.lastIndexOf("/");
                fileName = imgUrl.substring(index + 1, imgUrl.length());
            }
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            // 导出excel
            out = response.getOutputStream();
            // 生成图片
            byte[] buf = new byte[2048];
            int length = in.read(buf);
            while (length != -1)
            {
                out.write(buf, 0, length);
                length = in.read(buf);
            }
            in.close();

        }
        catch (Exception e)
        {
            log.error("导出图片异常：" + e.getMessage());
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    log.error("导出图片异常：" + e.getMessage());
                }
            }
        }

    }

    /**
     * excel标题样式
     */
    private static final String SKEY_TITLE = "title";

    /**
     * excel小标题样式
     */
    private static final String SKEY_SMALL_TITLE = "smallTitle";

    /**
     * excel报表头样式
     */
    private static final String SKEY_HEAD = "head";

    /**
     * excel数据样式
     */
    private static final String SKEY_DATA = "data";

    /**
     * excel标题样式
     */
    private static final String SKEY_ROW_SPLIT = "rowSplit";

    /**
     * excel合并单元格样式
     */
    private static final String SKEY_MERGE = "merge";

    /**
     * excel默认最大列宽
     */
    private static final short MAX_COLUMN_WIDTH = 20;

    /**
     * excel默认最小列宽
     */
    private static final short MIN_COLUMN_WIDTH = 11;

    /**
     * excel默认支持最大列数
     */
    private static final short MAX_COLUMN_NUMBER = 200;

    private static ExportUtil excelUtil = null;

    private ExportUtil()
    {
    }

    public static ExportUtil getInstance()
    {
        if (excelUtil == null)
        {
            excelUtil = new ExportUtil();
        }
        return excelUtil;
    }

    /**
     * 给定样式导出
     *
     * @param dataList  数据列表
     * @param out       输出流
     * @param sheetName sheet名称
     */
    public void exportExcel(List<ExcelRowEntity> dataList, OutputStream out, String sheetName)
    {
        writeToExcel(dataList, out, sheetName, null, MAX_COLUMN_NUMBER);
    }

    /**
     * 把数据写入excel
     *
     * @param dataList
     * @param out
     * @param sheetName
     * @param newStyleMap
     * @throws IOException
     */
    private void writeToExcel(List<ExcelRowEntity> dataList, OutputStream out, String sheetName,
                              Map<String, HSSFCellStyle> newStyleMap, int maxColumnNumber)
    {

        if (dataList == null)
        {
            return;
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        /**excel默认样式*/
        Map<String, HSSFCellStyle> styleMap = initDefaultStyleMap(workbook);
        // 生成一个虚拟表格
        HSSFSheet sheet = workbook.createSheet(sheetName == null ? "sheet1" : sheetName);
        Boolean[][] juge = new Boolean[dataList.size()][maxColumnNumber];
        for (int row = 0; row < juge.length; row++)
        {
            for (int col = 0; col < juge[row].length; col++)
            {
                juge[row][col] = true;
            }
        }

        for (int rowidx = 0; rowidx < dataList.size(); rowidx++)
        {
            ExcelRowEntity excelRow = dataList.get(rowidx);
            //取行类型
            int rowType = excelRow.getRowType();
            //取行中包含的cell列表
            List<ExcelCellEntity> colList = excelRow.getCellEntityList();
            //创建新行
            HSSFRow row = sheet.createRow(rowidx);
            row.setHeight(excelRow.getRowHeight());
            row.setHeightInPoints(excelRow.getRowHeight());
            //填充每个列
            for (int colidx = 0; colidx < colList.size(); colidx++)
            {
                ExcelCellEntity data = colList.get(colidx);
                int rowSpan = data.getRowSpan();
                int colSpan = data.getColSpan();
                String text = data.getText();
                int readCol = 0;
                for (int col1 = colidx; col1 <= 1000; col1++)
                {
                    if (juge[rowidx][col1])
                    {
                        readCol = col1;
                        break;
                    }
                }
                if (rowSpan * colSpan > 1)
                {
                    for (int row1 = rowidx; row1 <= rowidx + rowSpan - 1; row1++)
                    {
                        for (int col = readCol; col <= readCol + colSpan - 1; col++)
                        {
                            juge[row1][col] = false;
                        }
                    }
                    //被合并的单元格式设置样式，保持统一
                    for (int kk = readCol + 1; kk <= readCol + colSpan - 1; kk++)
                    {
                        HSSFCell cellMerge = row.createCell(kk);
                        cellMerge.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_MERGE));
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowidx, rowidx + rowSpan - 1, readCol, readCol + colSpan - 1));
                }
                else
                {
                    juge[rowidx][readCol] = false;
                }
                HSSFCell cell = row.createCell(readCol);
                cell.setCellValue(text.split("@")[0]);

                //设置excel样式
                switch (rowType)
                {
                    case ExcelRowEntity.TITLE_ROW://大标题行
                        cell.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_TITLE));
                        break;
                    case ExcelRowEntity.SMALL_TITLE_ROW://小标题行
                        cell.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_SMALL_TITLE));
                        break;
                    case ExcelRowEntity.HEAD_ROW://表头行
                        cell.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_HEAD));
                        //modify by ZX 2015-06-04 START----------------
                        int columnWidth = getStrLenth(text) * 256;
                        if (2 == text.split("@").length)
                        {
                            columnWidth = new Integer(text.split("@")[1]);
                        }
                        sheet.setColumnWidth(readCol, columnWidth);
                        //modify by ZX 2015-06-04 START----------------
                        break;
                    case ExcelRowEntity.SPLIT_ROW://分割行
                        cell.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_ROW_SPLIT));
                        break;
                    default://默认是数据行
                        cell.setCellStyle(nvlStyle(newStyleMap, styleMap, ExportUtil.SKEY_DATA));
                }
            }
        }
        try
        {
            workbook.write(out);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 初始化样式
     *
     * @param workbook
     */
    private Map<String, HSSFCellStyle> initDefaultStyleMap(HSSFWorkbook workbook)
    {
        Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
        String fontName = "宋体";
        short HEAD_BACK_COLOR = 9, SPLIT_BACK_COLOR = 10;
        //可以自定义颜色，然后在下面直接引用即可，颜色的索引还必须是 0x08 ~ 0x40 (8 ~ 64) 的数字
        HSSFPalette palette = workbook.getCustomPalette();
        palette.setColorAtIndex(HEAD_BACK_COLOR, (byte) (0xff & 135), (byte) (0xff & 206), (byte) (0xff & 235));
        palette.setColorAtIndex(SPLIT_BACK_COLOR, (byte) (0xff & 176), (byte) (0xff & 224), (byte) (0xff & 230));

        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);     //左边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);    //右边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);    //顶边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //顶边框
        HSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);//字体
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);    //填充的背景颜色
        style.setFont(font);
        styleMap.put(ExportUtil.SKEY_TITLE, style);

        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置边框样式
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);     //左边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);    //右边框
        HSSFFont font1 = workbook.createFont();
        font1.setFontName(fontName);
        font1.setColor(HSSFColor.BLACK.index);
        font1.setFontHeightInPoints((short) 10);//字体
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style1.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
        style1.setFont(font1);
        styleMap.put(ExportUtil.SKEY_SMALL_TITLE, style1);

        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置边框样式
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);     //左边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);    //右边框
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
        style2.setFillForegroundColor(HEAD_BACK_COLOR);    //填充的背景颜色
        HSSFFont font2 = workbook.createFont();
        font2.setFontName(fontName);
        font2.setColor(HSSFColor.BLACK.index);
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font2.setFontHeightInPoints((short) 10);//字体
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setFont(font2);
        styleMap.put(ExportUtil.SKEY_HEAD, style2);

        HSSFCellStyle style3 = workbook.createCellStyle();
        HSSFFont font3 = workbook.createFont();
        font3.setFontName(fontName);
        font3.setColor(HSSFColor.BLACK.index);
        font3.setFontHeightInPoints((short) 10);//字体
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style3.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style3.setFont(font3);
        style3.setWrapText(true);
        styleMap.put(ExportUtil.SKEY_DATA, style3);

        HSSFCellStyle style5 = workbook.createCellStyle();
        style5.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置边框样式
        style5.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        HSSFFont font5 = workbook.createFont();
        font5.setFontName(fontName);
        font5.setColor(HSSFColor.BLACK.index);
        font5.setFontHeightInPoints((short) 10);//字体
        style5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style5.setVerticalAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // 填充单元格
        style5.setFillForegroundColor(SPLIT_BACK_COLOR);    //填充的背景颜色
        style5.setFont(font5);
        style5.setWrapText(true);
        styleMap.put(ExportUtil.SKEY_ROW_SPLIT, style5);

        HSSFCellStyle style6 = workbook.createCellStyle();
        style6.setBorderBottom(HSSFCellStyle.BORDER_THIN);    //设置边框样式
        style6.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style6.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleMap.put(ExportUtil.SKEY_MERGE, style6);

        return styleMap;
    }

    /**
     * 动态计算列宽
     *
     * @param str 列表内容字符串
     * @return
     */
    private int getStrLenth(String str)
    {
        int len = Math.round(str.getBytes().length * 1.2f);
        len = len < ExportUtil.MIN_COLUMN_WIDTH ? ExportUtil.MIN_COLUMN_WIDTH : len;
        len = len > ExportUtil.MAX_COLUMN_WIDTH ? ExportUtil.MAX_COLUMN_WIDTH : len;
        return len;
    }

    /**
     * 取样式，如果第一个样式为空则取第二个，否则取第一个
     *
     * @return
     */
    private HSSFCellStyle nvlStyle(Map<String, HSSFCellStyle> firstMap, Map<String, HSSFCellStyle> secondMap, String key)
    {
        if (firstMap == null)
        {
            return secondMap.get(key);
        }
        HSSFCellStyle cs = firstMap.get(key);
        if (cs == null)
        {
            return secondMap.get(key);
        }
        return cs;
    }
}
