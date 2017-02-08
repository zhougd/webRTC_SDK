/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkCallCallback {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkCallCallback(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkCallCallback obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkCallCallback(swigCPtr);
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
    EngineSdkJNI.EngineSdkCallCallback_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    EngineSdkJNI.EngineSdkCallCallback_change_ownership(this, swigCPtr, true);
  }

  public void onInfoReceived(String controlInfo) {
    if (getClass() == EngineSdkCallCallback.class) EngineSdkJNI.EngineSdkCallCallback_onInfoReceived(swigCPtr, this, controlInfo); else EngineSdkJNI.EngineSdkCallCallback_onInfoReceivedSwigExplicitEngineSdkCallCallback(swigCPtr, this, controlInfo);
  }

  public void onCallNetworkQualityChanged(EngineSdkInSessionNetworkQualityIndication networkQualityInd) {
    if (getClass() == EngineSdkCallCallback.class) EngineSdkJNI.EngineSdkCallCallback_onCallNetworkQualityChanged(swigCPtr, this, networkQualityInd.swigValue()); else EngineSdkJNI.EngineSdkCallCallback_onCallNetworkQualityChangedSwigExplicitEngineSdkCallCallback(swigCPtr, this, networkQualityInd.swigValue());
  }

  public void onCallWaitForRemoteWakeupTimeout(String wakeupCallerNum, String wakeupCalleeNum, String lastWaitingSipCallId) {
    if (getClass() == EngineSdkCallCallback.class) EngineSdkJNI.EngineSdkCallCallback_onCallWaitForRemoteWakeupTimeout(swigCPtr, this, wakeupCallerNum, wakeupCalleeNum, lastWaitingSipCallId); else EngineSdkJNI.EngineSdkCallCallback_onCallWaitForRemoteWakeupTimeoutSwigExplicitEngineSdkCallCallback(swigCPtr, this, wakeupCallerNum, wakeupCalleeNum, lastWaitingSipCallId);
  }

  public void onMainVideoChannelWonderfulSaved(EngineSdkCallInformation callInfo, String filePath) {
    if (getClass() == EngineSdkCallCallback.class) EngineSdkJNI.EngineSdkCallCallback_onMainVideoChannelWonderfulSaved(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo, filePath); else EngineSdkJNI.EngineSdkCallCallback_onMainVideoChannelWonderfulSavedSwigExplicitEngineSdkCallCallback(swigCPtr, this, EngineSdkCallInformation.getCPtr(callInfo), callInfo, filePath);
  }

  public EngineSdkCallCallback() {
    this(EngineSdkJNI.new_EngineSdkCallCallback(), true);
    EngineSdkJNI.EngineSdkCallCallback_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

}
