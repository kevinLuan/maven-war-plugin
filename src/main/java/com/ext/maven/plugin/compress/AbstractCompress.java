package com.ext.maven.plugin.compress;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class AbstractCompress {
	public static String charset = "UTF-8";

	public static AbstractCompress getJavaScriptCompressInstance() {
		return new JavaScriptCompress();
	}

	public static AbstractCompress getCSSCompressInstance() {
		return new CSSCompress();
	}

	public abstract void compressDir(String dir, String outFile) throws IOException;

	public abstract void compress(String src, String desc) throws IOException;

	public abstract void compress(String[] srcs, String outFile) throws IOException;

	protected String generatedFileName(String filePath) {
		int lastIndex = filePath.lastIndexOf(".");
		String startWith = filePath.substring(0, lastIndex), endWith = filePath.substring(lastIndex);
		return startWith + "_min_2013_tmp" + endWith;
	}

	public long mergeFile(String[] merge, String outFile) throws IOException {
		FileInputStream fis = null;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		DataOutputStream out = null;
		long size = 0;
		new File(outFile).delete();
		try {
			fos = new FileOutputStream(outFile, true);
			out = new DataOutputStream(fos);
			for (int i = 0; i < merge.length; i++) {
				try {
					fis = new FileInputStream(merge[i]);
					dis = new DataInputStream(fis);
					int temp = -1;
					byte[] b = new byte[2048];
					while ((temp = dis.read(b)) != -1) {
						fos.write(b, 0, temp);
						size += temp;
					}
				} finally {
					if (dis != null) {
						dis.close();
						dis = null;
					}
					if (fis != null) {
						fis.close();
						fis = null;
					}
					fos.flush();
				}
			}
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
			if (fos != null) {
				fos.close();
				fos = null;
			}
		}
		return size;
	}

	public long mergeFile(String[] merge, OutputStream output) throws IOException {
		FileInputStream fis = null;
		DataInputStream dis = null;
		DataOutputStream out = null;
		long size = 0;
		try {
			for (int i = 0; i < merge.length; i++) {
				try {
					fis = new FileInputStream(merge[i]);
					dis = new DataInputStream(fis);
					int temp = -1;
					byte[] b = new byte[2048];
					while ((temp = dis.read(b)) != -1) {
						output.write(b, 0, temp);
						size += temp;
					}
				} finally {
					if (dis != null) {
						dis.close();
						dis = null;
					}
					if (fis != null) {
						fis.close();
						fis = null;
					}
					output.flush();
				}
			}
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
		}
		return size;
	}

	protected void deleteTmpFile(String[] compressJs) {
		for (int i = 0; i < compressJs.length; i++) {
			new File(compressJs[i]).delete();
		}

	}

	public void clearInValidData(List<String> srcs, AbstractCompress compress) {
		if (srcs != null) {
			for (int i = 0; i < srcs.size();) {
				if (!compress.getType().equals(srcs.get(i))) {
					srcs.remove(i);
				} else {
					i++;
				}
			}
		}
	}

	public static enum CompressType {
		CSS(".css"), JAVASCRIPT(".js");
		private String postfix;

		private CompressType(String postfix) {
			this.postfix = postfix;
		}

		public boolean equals(String filePath) {
			if (filePath != null)
				return filePath.toLowerCase().endsWith(postfix);
			return false;
		}
	}

	public abstract CompressType getType();
}
