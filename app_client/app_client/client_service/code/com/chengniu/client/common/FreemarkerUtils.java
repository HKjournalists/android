package com.chengniu.client.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerUtils {
	private static Configuration cfg = null;

	private static Configuration init() throws Exception {
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(FreemarkerUtils.class
					.getClassLoader().getResource("").getFile()));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDefaultEncoding("utf-8");
			cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
		}
		return cfg;
	}

	public static String process(String template, Map<String, ?> data,
			String path) throws Exception {
		if (path != null)
			template = path + template;
		Configuration cfg = init();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Writer w = new OutputStreamWriter(out, "utf-8");
		Template temp = cfg.getTemplate(template);
		temp.process(data, w);
		out.flush();
		return new String(out.toByteArray(), 0, out.size(), "utf-8").trim();
	}
}