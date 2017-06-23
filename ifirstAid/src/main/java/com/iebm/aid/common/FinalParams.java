package com.iebm.aid.common;

import java.util.HashMap;
import java.util.Map;

public class FinalParams {
	public final static Map<String,String> levelValueMap = new HashMap<String,String>();  
	static {  
		levelValueMap.put("alpha", "濒危病人");
		levelValueMap.put("bravo", "危重病人");
		levelValueMap.put("charlie", "急症病人");  
		levelValueMap.put("delta", "非急症病人");  
	} 
}
