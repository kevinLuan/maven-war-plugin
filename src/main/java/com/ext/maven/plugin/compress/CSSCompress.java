package com.ext.maven.plugin.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.mozilla.javascript.EvaluatorException;

import com.ext.maven.plugin.filter.Constants;
import com.yahoo.platform.yui.compressor.CssCompressor;

public class CSSCompress extends AbstractCompress {
	public void compressDir(String dir, String outFile) throws IOException {
		File file = new File(dir);
		if (file.isDirectory()) {
			File[] srcFiles = file.listFiles(new CSSFileFilter());
			if (srcFiles != null && srcFiles.length > 0) {
				String[] compress = new String[srcFiles.length];
				for (int i = 0; i < srcFiles.length; i++) {
					compress[i] = generatedFileName(srcFiles[i].getAbsolutePath());
					try {
						compress(srcFiles[i].getAbsolutePath(), compress[i]);
					} catch (Exception e) {
						Constants.error(srcFiles[i].getAbsolutePath(), e);
					}
				}
				mergeFile(compress, outFile);
				deleteTmpFile(compress);
			}
		} else {
			compress(file.getAbsolutePath(), outFile);
		}
	}

	public void compress(String src, String desc) throws IOException {
		Reader in = null;
		Writer out = null;
		int linebreakpos = -1;
		try {
			in = new InputStreamReader(new FileInputStream(src), charset);
			CssCompressor compressor = new CssCompressor(in);
			out = new OutputStreamWriter(new FileOutputStream(desc), charset);
			compressor.compress(out, linebreakpos);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			in = null;
			out = null;
		}
	}

	@Override
	public void compress(String[] srcs, String outFile) throws IOException {
		for (int i = 0; i < srcs.length; i++) {
			String[] compress = new String[srcs.length];
			compress[i] = generatedFileName(srcs[i]);
			try {
				compress(srcs[i], compress[i]);
			} catch (Exception e) {
				Constants.error(srcs[i], e);
			}
			mergeFile(compress, outFile);
			deleteTmpFile(compress);
		}
	}

	@Override
	public CompressType getType() {
		return CompressType.CSS;
	}

	public static String compress(String css) throws EvaluatorException, IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(css.getBytes());
		try {
			Reader reader = new InputStreamReader(byteArrayInputStream);
			return compress(reader);
		} finally {
			byteArrayInputStream.close();
			byteArrayInputStream = null;
		}
	}

	public static String compress(Reader in) throws EvaluatorException, IOException {
		Writer out = null;
		int linebreakpos = -1;
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		try {
			CssCompressor compressor = new CssCompressor(in);
			out = new OutputStreamWriter(arrayOutputStream);
			compressor.compress(out, linebreakpos);
			out.flush();
			byte[] b = arrayOutputStream.toByteArray();
			return new String(b);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (arrayOutputStream != null) {
				arrayOutputStream.close();
			}
			arrayOutputStream = null;
			in = null;
			out = null;
		}
	}
}