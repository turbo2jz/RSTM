package interfaces;

import java.util.EnumMap;

/**
 * ReturnValue为实现LogInfoJudgeReturnValue的类
 * @author always
 *
 */
public class ReturnValue implements LogInfoJudgeReturnValue{
	
	/**
	 * getReturnValue()用于返回后台数据库存储过程loginfojuge返回的信息
	 * @param flag 登录标记
	 * @param saType 登录用户类型
	 * @return EnumMap<ReturnValueTag, Object>
	 */
	public EnumMap<ReturnValueTag, Object> getReturnValue(Integer flag,
			String saType) {
		EnumMap<ReturnValueTag, Object> res=
				new EnumMap<ReturnValueTag, Object>(ReturnValueTag.class);
		res.put(ReturnValueTag.Flag, flag);
		res.put(ReturnValueTag.saType, saType);
		return res;
	}

}
