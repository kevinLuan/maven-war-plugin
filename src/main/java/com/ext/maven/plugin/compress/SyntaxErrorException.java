package com.ext.maven.plugin.compress;

import org.mozilla.javascript.EvaluatorException;

public class SyntaxErrorException extends EvaluatorException {
	private static final long serialVersionUID = -3447285860562978948L;

	public SyntaxErrorException(String detail) {
		super(detail);
	}

	public SyntaxErrorException(String detail, String sourceName, int lineNumber, String lineSource, int columnNumber) {
		super(detail, sourceName, lineNumber, lineSource, columnNumber);
	}

	public SyntaxErrorException(String detail, String sourceName, int lineNumber) {
		super(detail, sourceName, lineNumber);
	}

}
