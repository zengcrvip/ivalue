package com.axon.market.web.servlet;

import com.axon.market.common.bean.SystemConfigBean;
import com.axon.market.common.util.SpringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet implementation class UploadCommonServlet
 */
public class UploadCommonServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(UploadCommonServlet.class.getName());

    private static final long serialVersionUID = 1L;

    protected String dirTemp = "upload/widget/temp";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadCommonServlet()
    {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        //文件保存目录路径  
        String savePath = this.getServletContext().getRealPath("/") + "upload";

        // 临时文件目录   
        String tempPath = this.getServletContext().getRealPath("/") + dirTemp;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String ymd = sdf.format(new Date());
        String relativePath = "upload" + File.separator + ymd + File.separator;
        savePath += File.separator + ymd + File.separator;
        //创建文件夹  
        File dirFile = new File(savePath);
        if (!dirFile.exists())
        {
            dirFile.mkdirs();
        }

        tempPath += File.separator + ymd + File.separator;
        //创建临时文件夹  
        File dirTempFile = new File(tempPath);
        if (!dirTempFile.exists())
        {
            dirTempFile.mkdirs();
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(20 * 1024 * 1024); //设定使用内存超过5M时，将产生临时文件并存储于临时目录中。     
        factory.setRepository(new File(tempPath)); //设定存储临时文件的目录。     
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        try
        {
            List items = upload.parseRequest(request);
            Iterator itr = items.iterator();

            while (itr.hasNext())
            {
                FileItem item = (FileItem) itr.next();
                String fileName = item.getName();
                if (!item.isFormField())
                {
                    try
                    {
                        File uploadedFile = new File(savePath, fileName);

                        OutputStream os = new FileOutputStream(uploadedFile);
                        InputStream is = item.getInputStream();
                        byte buf[] = new byte[1024];//可以修改 1024 以提高读取速度  
                        int length = 0;
                        while ((length = is.read(buf)) > 0)
                        {
                            os.write(buf, 0, length);
                        }
                        //关闭流    
                        os.flush();
                        os.close();
                        is.close();
                        LOG.info("上传成功！路径 ：" + savePath + fileName);
                        out.print(relativePath + fileName);
                    }
                    catch (Exception e)
                    {
                        LOG.error("Upload File Error. ", e);
                    }
                }
            }
        }
        catch (FileUploadException e)
        {
            LOG.error("", e);
        }
        out.flush();
        out.close();
    }
}
