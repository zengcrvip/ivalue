package com.axon.market.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 2016/12/7.
 */
public class FileUtil
{
    private static byte[] column = {0x01};

    private static String col = new String(column);

    private static byte[] line = {0x0A};

    private static String lin = new String(line);

    public static void main(String[] args) throws IOException
    {
        System.out.println("start");
        LineIterator iterator = null;
        int count = 0;
        try
        {
            String line;
            List<String> lineList = new ArrayList<String>();
            File file = new File("E:\\pdr_send.txt"), newFile = new File("E:\\pdr_send");
            iterator = FileUtils.lineIterator(file, "UTF-8");
            while (iterator.hasNext())
            {
                line = iterator.next();
                if (StringUtils.isEmpty(line) || line.split("\\|").length != 9)
                {
                    count++;
                    continue;
                }

                String[] str = generte(line, 0, 1, 2, 3, 4, 5, 6, 8);

                lineList.add(StringUtils.join(str, "|").replace("\"", ""));
            }

            if (CollectionUtils.isNotEmpty(lineList))
            {
                FileUtils.writeLines(newFile, "UTF-8", lineList);
                lineList.clear();
            }
        }
        finally
        {
            if (iterator != null)
            {
                iterator.close();
            }
        }
        System.out.println(count);
        System.out.println("end");
    }

    private static String[] generte(String line, Integer... count)
    {
        String[] str = line.split("\\|");
        for (Integer num : count)
        {
            if ("\"\"".equals(str[num]) || "\"".equals(str[num]))
            {
                str[num] = "0";
            }
        }
        return str;
    }
}
