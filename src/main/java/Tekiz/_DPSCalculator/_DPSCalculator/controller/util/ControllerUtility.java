package Tekiz._DPSCalculator._DPSCalculator.controller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.HtmlUtils;

@Slf4j
public class ControllerUtility
{
	/**
	 * A method to handle string sanitization before returning the output.
	 * @param inputString The string to be sanitized.
	 * @return A sanitized string.
	 */
	public static String sanitizeString(String inputString){
		if (inputString == null || inputString.isEmpty()){
			log.warn("sanitizeString received null or empty input.");
			return "INVALID_INPUT";
		}
		return HtmlUtils.htmlEscape(inputString);
	}

	/**
	 * A method to handle string sanitization before returning the output.
	 * @param inputEnum An enum to be sanitized.
	 * @return A sanitized string.
	 */
	public static String sanitizeString(Enum<?> inputEnum){
		if (inputEnum == null){
			log.warn("sanitizeString received null input.");
			return "INVALID_INPUT";
		}
		String input = inputEnum.toString();
		return HtmlUtils.htmlEscape(input);
	}
}
