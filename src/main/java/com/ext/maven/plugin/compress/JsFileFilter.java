package com.ext.maven.plugin.compress;

import java.io.File;
import java.io.FilenameFilter;

public class JsFileFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(".js");
	}
}
