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

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.ext.maven.plugin.filter.Constants;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class JavaScriptCompress extends AbstractCompress {
	public void compressDir(String dir, String outFile) throws IOException {
		File file = new File(dir);
		if (file.isDirectory()) {
			File[] srcFiles = file.listFiles(new JsFileFilter());
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
		CompressUtils.checkJsFileIllegalityWord(new File(src));
		int linebreakpos = -1;
		try {
			in = new InputStreamReader(new FileInputStream(src), charset);
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, new MyErrorReporter());
			out = new OutputStreamWriter(new FileOutputStream(desc), charset);
			boolean verbose = true;
			boolean munge = true;
			boolean preserveAllSemiColons = true, disableOptimizations = false;
			compressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			in = null;
			out = null;
		}
	}

	public static String compress(String javascript) throws EvaluatorException, IOException {
		CompressUtils.checkIllegalityWord(javascript);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(javascript.getBytes());
		try {
			Reader reader = new InputStreamReader(byteArrayInputStream);
			return compress(reader);
		} finally {
			if (byteArrayInputStream != null) {
				byteArrayInputStream.close();
			}
			byteArrayInputStream = null;
		}
	}

	public static String compress(Reader in) throws EvaluatorException, IOException {
		Writer out = null;
		int linebreakpos = -1;
		ByteArrayOutputStream osArrayOutputStream = new ByteArrayOutputStream();
		try {
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, new MyErrorReporter());
			out = new OutputStreamWriter(osArrayOutputStream);
			boolean verbose = true;
			boolean munge = true;
			boolean preserveAllSemiColons = true, disableOptimizations = false;
			compressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
			out.flush();
			byte[] b = osArrayOutputStream.toByteArray();
			return new String(b);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (osArrayOutputStream != null) {
				osArrayOutputStream.close();
			}
			osArrayOutputStream = null;
			in = null;
			out = null;
		}
	}

	static class MyErrorReporter implements ErrorReporter {

		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			if (line < 0)
				System.out.println("  " + message);
			else
				System.out.println("  " + line + ':' + lineOffset + ':' + message);
		}

		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
			if (line < 0)
				Constants.error("[comress.js.error]" + message);
			else
				Constants.error("[comress.js.error]" + line + ':' + lineOffset + ':' + message);
		}

		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			error(message, sourceName, line, lineSource, lineOffset);
			return new EvaluatorException(message);
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
		return CompressType.JAVASCRIPT;
	}
}
