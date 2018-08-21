package com.axon.market.common.domain.kafkaservice;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 解析kafka的文件对象
 * zegncr
 */
public class LogInfo
{
	/**
	 * 场景类型
	 */
	private String sceneType;

	/**
	 * 任务ID
	 */
	private String sceneId;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 数据的时间戳
	 */
	private Long time;

	/**
	 * 流量
	 */
	private String gsmData;
	/**
	 * 活动编码
	 */
	private String saleId;
	/**
	 * 波次编码
	 */
	private String saleBoidId;
	/**
	 * 客户群编码
	 */
	private String aimSubId;

	/**
	 * 全部属性
	 */
	private String[] fields;


	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Long getTime()
	{
		return time;
	}

	public void setTime(Long time)
	{
		this.time = time;
	}

	public String getSceneType()
	{
		return sceneType;
	}

	public void setSceneType(String sceneType)
	{
		this.sceneType = sceneType;
	}

	public String getSaleId()
	{
		return saleId;
	}

	public void setSaleId(String saleId)
	{
		this.saleId = saleId;
	}

	public String getSaleBoidId()
	{
		return saleBoidId;
	}

	public void setSaleBoidId(String saleBoidId)
	{
		this.saleBoidId = saleBoidId;
	}

	public String getAimSubId()
	{
		return aimSubId;
	}

	public void setAimSubId(String aimSubId)
	{
		this.aimSubId = aimSubId;
	}

	public String[] getFields()
	{
		return fields;
	}

	public void setFields(String[] fields)
	{
		this.fields = fields;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public String getGsmData() {
		return gsmData;
	}

	public void setGsmData(String gsmData) {
		this.gsmData = gsmData;
	}

	/**
	 * 将kafka服务端传过来的CSV格式的数据转换为LogInfo对象
	 * @param value
	 */
	public LogInfo(String value)
	{
		if (StringUtils.isEmpty(value))
		{
			throw new IllegalArgumentException("value cannot empty!");
		}
		String[] fields = value.split("\t");
		if (fields.length < 4)
		{
			throw new IllegalArgumentException("fields length cannot less than 4!");
		}
		this.sceneType = fields[0];
        this.sceneId = fields[1];
		this.phone = fields[2];
		this.time = NumberUtils.toLong(fields[3]);
		if(fields.length == 5){
			this.gsmData = fields[4];
		}
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "LogInfo [sceneType=" + sceneType + ", sceneId=" + sceneId + ", phone="
				+ phone + ", time=" + time + ", gsmData=" + gsmData + "]";
	}
	
}
