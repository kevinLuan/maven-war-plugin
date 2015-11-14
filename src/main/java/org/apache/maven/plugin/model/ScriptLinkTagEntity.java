package org.apache.maven.plugin.model;

public class ScriptLinkTagEntity extends TagEntity {
	private boolean isJS = false;
	private boolean isCSS = false;
	private String absolutePath;
	private long fileLength = 0;

	public void setFileLength(long length) {
		this.fileLength = length;
	}

	public long getFileLength() {
		return this.fileLength;
	}

	public String getJsURL() {
		if (getUrl() != null) {
			int index = getUrl().indexOf(".js?");
			if (index > 0) {
				return getUrl().substring(0, index) + ".js";
			}
		}
		return getUrl();
	}

	public String getCssURL() {
		if (getUrl() != null) {
			int index = getUrl().indexOf(".css?");
			if (index > 0) {
				return getUrl().substring(0, index) + ".css";
			}
		}
		return getUrl();
	}

	public boolean isRemoteFile() {
		if (getUrl() != null && (getUrl().startsWith("http://") || getUrl().startsWith("https://"))) {
			return true;
		}
		return false;
	}

	public boolean isJsCompressFile() {
		setUrl(getJsURL());
		return getUrl().endsWith(".min.js") || "no".equalsIgnoreCase(getCompress());
	}

	public boolean isCssCompressFile() {
		setUrl(getJsURL());
		return getUrl().endsWith(".min.css") || "no".equalsIgnoreCase(getCompress());
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getAbsolutePath() {
		return this.absolutePath;
	}

	@Override
	public boolean isScript() {
		return isJS;
	}

	@Override
	public boolean isLink() {
		return isCSS;
	}

	public void setScript(boolean b) {
		this.isJS = b;
	}

	public void setStyle(boolean b) {
		this.isCSS = b;
	}

}
