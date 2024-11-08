package de.douglas.util;

import java.time.Duration;

public class Constants {
	public static final Duration DEFAULT_IMPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(30);
	public static final Duration DEFAULT_EXPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(30);

	public static final String EXTENT_REPORT_FILE_NAME = "./target/extent-report/index.html";
	public static final String TEST_DATA_EXCEL_FILE_NAME = "TestData.xlsx";

	public static final String ACTION_KEY = "action";
	public static final String MESSAGE_KEY = "message";
	public static final String TEXT_KEY = "text";

	public static final String HTML_LOG_HEADER = "HEADER_HERE";
	public static final String HTML_LOG_CODE = "CODE_HERE";
	public static final String HTML_LOG_IMAGE_BASE64 = "IMAGE_BASE64";
	public static final String EXTENT_LOG_SAMPLE =
					"<div class='d-flex align-items-center'>\n" +
					"    <div class='container ml-0 pl-0'>\n" +
					"        <div class='fw-lighter fs-3'>" + HTML_LOG_HEADER + "</div>\n" +
					"        <code>" + HTML_LOG_CODE + "</code>\n" +
					"    </div>\n" +
					"    <a type='button' class='btn btn-lg d-flex float-right btn-primary mt-0 mb-0' data-featherlight='image' href='" + HTML_LOG_IMAGE_BASE64 + "'>Click here for Evidence</a>\n" +
					"</div>";
}
