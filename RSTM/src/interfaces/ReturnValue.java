package interfaces;

import java.util.EnumMap;

/**
 * ReturnValueΪʵ��LogInfoJudgeReturnValue����
 * @author always
 *
 */
public class ReturnValue implements LogInfoJudgeReturnValue{
	
	/**
	 * getReturnValue()���ڷ��غ�̨���ݿ�洢����loginfojuge���ص���Ϣ
	 * @param flag ��¼���
	 * @param saType ��¼�û�����
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
