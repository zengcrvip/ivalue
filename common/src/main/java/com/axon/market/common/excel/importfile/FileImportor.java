package com.axon.market.common.excel.importfile;

import com.axon.market.common.excel.importfile.domain.common.ImportResult;
import com.axon.market.common.excel.importfile.exception.FileImportException;

import java.io.File;

/**
 * Created by stark.zhang on 2015/11/19.
 */
public abstract class FileImportor {

    public abstract ImportResult getImportResult(File file, String fileName) throws FileImportException;

}
