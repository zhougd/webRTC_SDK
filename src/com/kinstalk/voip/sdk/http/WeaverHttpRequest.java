package com.kinstalk.voip.sdk.http;

import java.util.Hashtable;

public class WeaverHttpRequest
{
	private String url;
	private byte[] body;
	private Hashtable<String, String> headers = new Hashtable<String, String>();
	private String method = "POST";
	private int responseCode;
	private byte[] responseData;
	private String contentType;
	private int timeoutMS = 20000;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public byte[] getBody()
	{
		return body;
	}

	public void setBody(byte[] body)
	{
		this.body = body;
	}

	public Hashtable<String, String> getHeaders()
	{
		return headers;
	}

	public void addHeader(String key, String value)
	{
		headers.put(key, value);
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}

	public byte[] getResponseData()
	{
		return responseData;
	}

	public void setResponseData(byte[] responseData)
	{
		this.responseData = responseData;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public int getTimeoutMS()
	{
		return timeoutMS;
	}

	public void setTimeoutMS(int timeoutMS)
	{
		this.timeoutMS = timeoutMS;
	}

}
