/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkAudioBiInfo {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkAudioBiInfo(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkAudioBiInfo obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkAudioBiInfo(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public EngineSdkAudioBiInfo() {
    this(EngineSdkJNI.new_EngineSdkAudioBiInfo(), true);
  }

  public void setFractionLost(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_fractionLost_set(swigCPtr, this, value);
  }

  public String getFractionLost() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_fractionLost_get(swigCPtr, this);
  }

  public void setCumulativeLost(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_cumulativeLost_set(swigCPtr, this, value);
  }

  public String getCumulativeLost() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_cumulativeLost_get(swigCPtr, this);
  }

  public void setExtendedMax(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_extendedMax_set(swigCPtr, this, value);
  }

  public String getExtendedMax() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_extendedMax_get(swigCPtr, this);
  }

  public void setJitterSamples(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_jitterSamples_set(swigCPtr, this, value);
  }

  public String getJitterSamples() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_jitterSamples_get(swigCPtr, this);
  }

  public void setRttMs(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_rttMs_set(swigCPtr, this, value);
  }

  public String getRttMs() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_rttMs_get(swigCPtr, this);
  }

  public void setBpsSend(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_bpsSend_set(swigCPtr, this, value);
  }

  public String getBpsSend() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_bpsSend_get(swigCPtr, this);
  }

  public void setBpsReceive(String value) {
    EngineSdkJNI.EngineSdkAudioBiInfo_bpsReceive_set(swigCPtr, this, value);
  }

  public String getBpsReceive() {
    return EngineSdkJNI.EngineSdkAudioBiInfo_bpsReceive_get(swigCPtr, this);
  }

}
