package com.axon.market.common.constant.ikeeper;

import com.axon.market.common.bean.ResultVo;

/**
 * Created by yuanfei on 2017/8/15.
 */
public enum KeeperAppResponseCodeEnum
{
    /**
     * 错误码:
     *      0：暂不处理
     *      2: 其他
     *      3：
     *      4：参数信息错误码
     *      5：数据库操作，数据结果类
     *      6：短信发送语音类错误
     *      7：权限类错误
     *      8：其他
     *      9：失效
     *
     */
    NOT_HANDLED("0001","暂不处理"),

    PARAM_LOSE("4001","参数丢失"),

    PARAM_FORMAT_ERR("4002","参数格式错误"),

    DB_OPERATE_ERR("5001","数据库操作失败"),

    DB_RESULT_ERR("5002","数据查询异常，数据不存在"),

    DB_DATA_INVALID("5003","数据已失效"),

    SMS_SEND_ERR("6001","短信发送失败"),

    CALL_ERR("6002","调用话+接口失败"),

    ORDER_ERR("6003","调用产品订购接口失败"),

    INSUFFICIENT_AUTHORITY("7001","权限不足"),

    OTHER_ERR("8000","未知原因，请联系管理员");

    private ResultVo result;

    KeeperAppResponseCodeEnum(String code,String desc)
    {
        ResultVo resultVo = new ResultVo();
        resultVo.setResultCode(code);
        resultVo.setResultMsg(desc);
        this.result = resultVo;
    }

    public ResultVo getValue()
    {
        return result;
    }

    public ResultVo getValue(String desc)
    {
        result.setResultMsg(desc);
        return result;
    }

    public ResultVo getValue(String code,String desc)
    {
        result.setResultCode(code);
        result.setResultMsg(desc);
        return result;
    }
}
