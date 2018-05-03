package com.hust.grid.leesf.bean;

public class IpInfo {

	public IpInfo(String ipAddress, int port, String location,
			String anonymousType, String type, String confirmTime, String responseSpeed) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.location = location;
		this.anonymousType = anonymousType;
		this.type = type;
		this.confirmTime = confirmTime;
		this.responseSpeed = responseSpeed;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAnonymousType() {
		return anonymousType;
	}

	public void setAnonymousType(String anonymousType) {
		this.anonymousType = anonymousType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getResponseSpeed() {
		return responseSpeed;
	}

	public void setResponseSpeed(String responseSpeed) {
		this.responseSpeed = responseSpeed;
	}

	private String ipAddress;	//IP地址
	private int port; 			//端口	
	private String location; 	//位置
	private String anonymousType; //是否匿名
	private String type; 		//类型
	private String confirmTime; //最后验证时间
	private String responseSpeed; //响应速度
}
