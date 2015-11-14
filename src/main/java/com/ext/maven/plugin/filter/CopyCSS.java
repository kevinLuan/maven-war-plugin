package com.ext.maven.plugin.filter;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.war.util.Logger;

import com.ext.maven.plugin.compress.AbstractCompress;

public class CopyCSS extends CopyFileAdapter {
	public void copy(File source, File destination) throws IOException {
		if (Constants.isSkip(source)) {
			super.copy(source, destination);
			return;
		}
		try {
			if (Constants.isMatchesCssCompress(source)) {
				if (!destination.getParentFile().exists()) {
					destination.getParentFile().mkdirs();
				}
				try {
					AbstractCompress.getCSSCompressInstance().compress(source.getAbsolutePath(), destination.getAbsolutePath());
					Logger.info("[OK]\tcompress.css:" + source.getAbsolutePath());
					return;
				} catch (Exception ex) {
					Logger.error("[ERROR]\tcompress.css:" + source.getAbsolutePath(), ex);
				}
			}
			super.copy(source, destination);
		} finally {
			if (productMap.containsKey(source)) {
				copyMd5File(source, destination,productMapMd5.get(source));
				productMap.remove(source);
			}
		}
	}

}
