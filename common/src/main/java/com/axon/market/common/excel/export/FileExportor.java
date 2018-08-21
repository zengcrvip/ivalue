package com.axon.market.common.excel.export;

import com.axon.market.common.excel.export.impl.NewExcelExportor;
import com.axon.market.common.excel.export.domain.common.ExportConfig;
import com.axon.market.common.excel.export.exception.FileExportException;
import org.apache.poi.ss.usermodel.Workbook;
import com.axon.market.common.excel.export.domain.common.ExportResult;
import com.axon.market.common.excel.export.domain.common.ExportType;
import com.axon.market.common.excel.export.domain.excel.ExportCSVResult;
import com.axon.market.common.excel.export.domain.excel.ExportExcelResult;
import com.axon.market.common.excel.export.impl.CSVExportor;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/7.
 */
public class FileExportor {
    public final static String EXPORT_XML_BASE_PATH = "/properties/framework/export/";

    /**
     * 通过list<T> T可为bean或者map<String, Object>  导出文件
     *
     * @param exportConfig
     * @param data
     * @return
     * @throws FileExportException
     */
    public static ExportResult getExportResult(ExportConfig exportConfig, List<?> data) throws FileExportException {
        ExportType exportType = exportConfig.getExportType();
        switch (exportType) {
            case EXCEL2007:
                //ExcelExportor暂时不用
                //Workbook workbook = new ExcelExportor().getExportResult(data, exportConfig.getExportCells());
                Workbook workbook = new NewExcelExportor().getExportResult(data, exportConfig.getExportCells());
                ExportExcelResult exportExcelResult = new ExportExcelResult();
                exportExcelResult.setWorkbook(workbook);
                exportExcelResult.setFileName(exportConfig.getFileName());
                return exportExcelResult;
            case CSV:
                StringBuilder stringBuilder = new CSVExportor().getExportResult(data, exportConfig.getExportCells());
                ExportCSVResult exportCSVResult = new ExportCSVResult();
                exportCSVResult.setResult(stringBuilder.toString());
                exportCSVResult.setFileName(exportConfig.getFileName());
                return exportCSVResult;
        }
        throw new FileExportException("找不到对应的export type, export type is " + exportType.getNumber());
    }



}
