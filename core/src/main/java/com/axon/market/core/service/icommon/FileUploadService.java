package com.axon.market.core.service.icommon;


import com.axon.market.common.bean.SystemConfigBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by chenyu on 2016/6/23.
 */
@Component("fileUploadService")
public class FileUploadService
{
    private static final Logger LOG = Logger.getLogger(FileUploadService.class.getName());

    @Autowired
    @Qualifier("systemConfigBean")
    private SystemConfigBean systemConfigBean;

    /**
     * @param request
     * @param fileName
     * @return
     * @throws IOException
     */
    public File fileUpload(HttpServletRequest request, String fileName) throws IOException
    {
        File localFile = null;
        try
        {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request))
            {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iterator = multiRequest.getFileNames();
                while (iterator.hasNext())
                {
                    MultipartFile file = multiRequest.getFile(iterator.next());
                    if (file != null)
                    {
                        String myFileName = file.getOriginalFilename();
                        if (StringUtils.isNotEmpty(myFileName))
                        {
                            String path = systemConfigBean.getLocalFilePath() + fileName;
                            localFile = new File(path);
                            file.transferTo(localFile);
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            LOG.error("File upload error ", e);
            throw e;
        }
        return localFile;
    }
}
