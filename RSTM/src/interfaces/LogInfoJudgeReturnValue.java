package interfaces;

import java.util.*;

/**
 * LogInfoJudgeReturnValueΪʵ�ֵ�¼��Ϣ�ж϶�ֵ���صĽӿ�
 * getReturnValue()Ϊ���ض�ֵ���鷽��
 * @author always
 *
 */
public interface LogInfoJudgeReturnValue {
	/**
	 * ����ֵ���ö��
	 * @param Flag	��¼״̬���(key)��Ӧvalue(Integer)
	 * @param saType �û����ͱ��(key)��Ӧvalue(String)
	 */
    enum ReturnValueTag {
        Flag,saType
    }
    public EnumMap<ReturnValueTag,Object> getReturnValue(Integer flag,String name);
}
