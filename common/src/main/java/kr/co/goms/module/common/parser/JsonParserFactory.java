package kr.co.goms.module.common.parser;

import android.util.ArrayMap;

import java.util.Map;

public class JsonParserFactory {

	public Object create(String factoryType)
	{
		for (Map.Entry<String, Object> entry : JsonParserManager.I().getParserList().entrySet()) {
			String key = entry.getKey();
			if (factoryType.equals(key)) {
				Object object = entry.getValue();
				return object;
			}
		}
		return null;
	}
}
