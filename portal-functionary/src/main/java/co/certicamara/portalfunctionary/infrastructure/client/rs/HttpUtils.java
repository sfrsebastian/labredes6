package co.certicamara.portalfunctionary.infrastructure.client.rs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class HttpUtils {
	
	public static String createParamsString(Iterator<Entry<String, List<String>>> requestParamsIterator) throws UnsupportedEncodingException {
		
		String paramsString = "";
		while(requestParamsIterator.hasNext()) {
			
			if(paramsString.isEmpty()) {
				
				paramsString += "?";
				
			} else {
				
				paramsString += "&";
				
			}
			
			Entry<String, List<String>> requestParam = requestParamsIterator.next();
			paramsString += requestParam.getKey() + "=" + URLEncoder.encode(requestParam.getValue().get(0), "UTF-8");
			
		}
		
		return paramsString;
	}

}
