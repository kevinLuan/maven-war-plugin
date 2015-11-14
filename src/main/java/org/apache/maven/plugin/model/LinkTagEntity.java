package org.apache.maven.plugin.model;

public class LinkTagEntity extends TagEntity {

	public boolean isCssCompressFile() {
		return getCssURL().endsWith(".min.css") || "no".equalsIgnoreCase(getCompress());
	}

	@Override
	public String getCssURL() {
		return parserCssURL(getUrl());
	}

	@Override
	public boolean isLink() {
		return getCssURL() != null;
	}

	@Override
	public boolean isScript() {
		return false;
	}

	public String getJsURL() {
		return null;
	}

	public boolean isJsCompressFile() {
		return false;
	}

}
