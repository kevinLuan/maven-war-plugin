package org.apache.maven.plugin.model;

import java.io.File;

public abstract class TagEntity {
	private String id = null;
	private String url = null;
	private boolean ignore;
	private String code;
	private File file;
	/*合并请求时，不做压缩*/
	private String compress;//yes|no 默认：yes

	public abstract String getJsURL();

	public abstract String getCssURL();

	public boolean isRemoteFile() {
		if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
			return true;
		}
		return false;
	}

	public abstract boolean isJsCompressFile();

	public abstract boolean isCssCompressFile();

	public abstract boolean isScript();

	public abstract boolean isLink();

	public static String parserCssURL(String url) {
		if (url != null) {
			int index = url.indexOf(".css?");
			if (index > 0) {
				return url.substring(0, index) + ".css";
			}
		}
		return url;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getCompress() {
		return compress;
	}

	public void setCompress(String compress) {
		this.compress = compress;
	}

}
