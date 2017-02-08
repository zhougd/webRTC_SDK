package com.kinstalk.voip.sdk.interfaces;

import com.kinstalk.voip.sdk.model.WeaverRequest;

public interface WeaverRequestListener
{
	public void onRequestFinshed(WeaverRequest req);
}
