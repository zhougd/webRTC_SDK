/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkMediaDataCallback {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkMediaDataCallback(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkMediaDataCallback obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkMediaDataCallback(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    EngineSdkJNI.EngineSdkMediaDataCallback_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    EngineSdkJNI.EngineSdkMediaDataCallback_change_ownership(this, swigCPtr, true);
  }

  public void onSecondDataChannelDataReceived(EngineSdkCallInformation callInfo, byte[] dataBuffer) {
    if (getClass() == EngineSdkMediaDataCallback.class) EngineSdkJNI.EngineSdkMediaDataCallback_onSecondDataChannelDataReceived(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo, dataBuffer); else EngineSdkJNI.EngineSdkMediaDataCallback_onSecondDataChannelDataReceivedSwigExplicitEngineSdkMediaDataCallback(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo, dataBuffer);
  }

  public void onDataChannelStateChanged(EngineSdkCallInformation callInfo) {
    if (getClass() == EngineSdkMediaDataCallback.class) EngineSdkJNI.EngineSdkMediaDataCallback_onDataChannelStateChanged(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo); else EngineSdkJNI.EngineSdkMediaDataCallback_onDataChannelStateChangedSwigExplicitEngineSdkMediaDataCallback(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo);
  }

  public EngineSdkMediaDataCallback() {
    this(EngineSdkJNI.new_EngineSdkMediaDataCallback(), true);
    EngineSdkJNI.EngineSdkMediaDataCallback_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
