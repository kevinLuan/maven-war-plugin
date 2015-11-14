package org.apache.maven.plugin.model;

public class ScriptTagEntity extends TagEntity {

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

	public boolean isJsCompressFile() {
		return getJsURL().endsWith(".min.js") || "no".equalsIgnoreCase(getCompress());
	}

	public boolean isCssCompressFile() {
		return false;
	}

	@Override
	public boolean isScript() {
		return getUrl() != null;
	}

	@Override
	public boolean isLink() {
		return false;
	}

}
