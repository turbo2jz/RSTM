package interfaces;

import java.util.*;

/**
 * LogInfoJudgeReturnValue为实现登录信息判断多值返回的接口
 * getReturnValue()为返回多值的虚方法
 * @author always
 *
 */
public interface LogInfoJudgeReturnValue {
	/**
	 * 返回值标记枚举
	 * @param Flag	登录状态标记(key)对应value(Integer)
	 * @param saType 用户类型标记(key)对应value(String)
	 */
    enum ReturnValueTag {
        Flag,saType
    }
    public EnumMap<ReturnValueTag,Object> getReturnValue(Integer flag,String name);
}
