package per.zc.util;

import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SearchFilter {

	public enum Operator {
		EQ, LIKE, GT, LT, GTE, NEQ, LTE,GTES,LTES
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StrKit.isBlank((String) value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = key.split("_");
			if (names.length <2 ) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}

			String filedName = names[1];
			String filedNamea="";
			// 从下标1开始
			for(int i=1;i<names.length;i++){
				filedNamea=filedNamea+names[i]+"_";
			}
			if(filedNamea.substring(filedNamea.length()-1,filedNamea.length()).equals("_"))
				filedNamea=filedNamea.substring(0, filedNamea.length() - 1);
			// 为了处理 sera_id 这样字段查询 暂且用 替换方法
			filedName=filedNamea;
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);

			filters.put(key, filter);
		}

		return filters;
	}
}