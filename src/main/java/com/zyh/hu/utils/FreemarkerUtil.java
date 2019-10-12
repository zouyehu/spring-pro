package com.zyh.hu.utils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * FREEMARKER 处理工具类
 *
 * @author HU
 */
@SuppressWarnings("deprecation")
public class FreemarkerUtil {
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtil.class);
    private static final String ENCODING = "UTF-8";
    private static Configuration cfg;

    static {
        cfg = new Configuration();
        cfg.setTemplateLoader(new TemplateLoader() {
            public Object findTemplateSource(String s) throws IOException {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream(s);
                return inputStream;
            }

            public long getLastModified(Object o) {
                return -1L;
            }

            public Reader getReader(Object o, String s) throws IOException {
                return new InputStreamReader((InputStream) o);
            }

            public void closeTemplateSource(Object o) throws IOException {
                ((InputStream) o).close();
            }
        });
    }

    /**
     * 通过模板生成字符串
     *
     * @param templatePath 模板路径
     * @param values       数据项
     * @param charset      字符编码
     * @return 数据填充后的字符串
     */
    public static String getString(String templatePath, Map<String, Object> values, String charset) {
        Writer writer = null;
        try {
            Template template = cfg.getTemplate(templatePath, charset);
            writer = new StringWriter();
            template.process(values, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            logger.error("template incorrent, exception" + e);
            throw new RuntimeException("template incorrent, exception" + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    logger.error("template incorrent, exception" + e);
                    throw new RuntimeException("template incorrent, exception" + e);
                }
            }
        }
    }

    public static String getString(String template, Map<String, Object> values) {
        return FreemarkerUtil.getString(template, values, ENCODING);
    }
}
