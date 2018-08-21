package com.axon.market.common.excel.export.service;

import com.axon.market.common.excel.export.domain.common.ExportCell;
import com.axon.market.common.excel.export.exception.FileExportException;

import java.util.List;

/**
 * Created by stark.zhang on 2015/11/6.
 */
public interface IFileExportor {
    /**
     * 数据导出
     * @param data
     * @param exportCells
     * @return
     * @throws FileExportException
     */
    public Object getExportResult(List<?> data, List<ExportCell> exportCells) throws FileExportException;


}
