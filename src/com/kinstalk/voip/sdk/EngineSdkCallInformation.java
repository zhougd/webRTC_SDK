/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkCallInformation {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkCallInformation(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkCallInformation obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkCallInformation(swigCPtr);
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
    EngineSdkJNI.EngineSdkCallInformation_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    EngineSdkJNI.EngineSdkCallInformation_change_ownership(this, swigCPtr, true);
  }

  public EngineSdkCallInformation() {
    this(EngineSdkJNI.new_EngineSdkCallInformation(), true);
    EngineSdkJNI.EngineSdkCallInformation_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public void setCallConfiguration(EngineSdkCallConfiguration value) {
    EngineSdkJNI.EngineSdkCallInformation_callConfiguration_set(swigCPtr, this, EngineSdkCallConfiguration.getCPtr(value), value);
  }

  public EngineSdkCallConfiguration getCallConfiguration() {
    long cPtr = EngineSdkJNI.EngineSdkCallInformation_callConfiguration_get(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkCallConfiguration(cPtr, false);
  }

  public void setCallState(EngineSdkCallState value) {
    EngineSdkJNI.EngineSdkCallInformation_callState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkCallState getCallState() {
    return EngineSdkCallState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_callState_get(swigCPtr, this));
  }

  public void setCallEndReason(EngineSdkCallEndReason value) {
    EngineSdkJNI.EngineSdkCallInformation_callEndReason_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkCallEndReason getCallEndReason() {
    return EngineSdkCallEndReason.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_callEndReason_get(swigCPtr, this));
  }

  public void setCallDeclinedReason(String value) {
    EngineSdkJNI.EngineSdkCallInformation_callDeclinedReason_set(swigCPtr, this, value);
  }

  public String getCallDeclinedReason() {
    return EngineSdkJNI.EngineSdkCallInformation_callDeclinedReason_get(swigCPtr, this);
  }

  public void setCallStartTime(int value) {
    EngineSdkJNI.EngineSdkCallInformation_callStartTime_set(swigCPtr, this, value);
  }

  public int getCallStartTime() {
    return EngineSdkJNI.EngineSdkCallInformation_callStartTime_get(swigCPtr, this);
  }

  public void setCallEndTime(int value) {
    EngineSdkJNI.EngineSdkCallInformation_callEndTime_set(swigCPtr, this, value);
  }

  public int getCallEndTime() {
    return EngineSdkJNI.EngineSdkCallInformation_callEndTime_get(swigCPtr, this);
  }

  public void setCallSessionId(String value) {
    EngineSdkJNI.EngineSdkCallInformation_callSessionId_set(swigCPtr, this, value);
  }

  public String getCallSessionId() {
    return EngineSdkJNI.EngineSdkCallInformation_callSessionId_get(swigCPtr, this);
  }

  public void setCallResultReport(EngineSdkCallResultReport value) {
    EngineSdkJNI.EngineSdkCallInformation_callResultReport_set(swigCPtr, this, EngineSdkCallResultReport.getCPtr(value), value);
  }

  public EngineSdkCallResultReport getCallResultReport() {
    long cPtr = EngineSdkJNI.EngineSdkCallInformation_callResultReport_get(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkCallResultReport(cPtr, false);
  }

  public void setAudioTransmitingChannelState(EngineSdkAudioChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_audioTransmitingChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkAudioChannelState getAudioTransmitingChannelState() {
    return EngineSdkAudioChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_audioTransmitingChannelState_get(swigCPtr, this));
  }

  public void setAudioReceivingChannelState(EngineSdkAudioChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_audioReceivingChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkAudioChannelState getAudioReceivingChannelState() {
    return EngineSdkAudioChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_audioReceivingChannelState_get(swigCPtr, this));
  }

  public void setMainVideoTransmitingChannelState(EngineSdkVideoChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_mainVideoTransmitingChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkVideoChannelState getMainVideoTransmitingChannelState() {
    return EngineSdkVideoChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_mainVideoTransmitingChannelState_get(swigCPtr, this));
  }

  public void setMainVideoReceivingChannelState(EngineSdkVideoChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_mainVideoReceivingChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkVideoChannelState getMainVideoReceivingChannelState() {
    return EngineSdkVideoChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_mainVideoReceivingChannelState_get(swigCPtr, this));
  }

  public void setSecondVideoChannelState(EngineSdkVideoChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_secondVideoChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkVideoChannelState getSecondVideoChannelState() {
    return EngineSdkVideoChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_secondVideoChannelState_get(swigCPtr, this));
  }

  public void setDataChannelState(EngineSdkDataChannelState value) {
    EngineSdkJNI.EngineSdkCallInformation_dataChannelState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkDataChannelState getDataChannelState() {
    return EngineSdkDataChannelState.swigToEnum(EngineSdkJNI.EngineSdkCallInformation_dataChannelState_get(swigCPtr, this));
  }

  public EngineSdkCallInformation clone() {
    long cPtr = EngineSdkJNI.EngineSdkCallInformation_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkCallInformation(cPtr, false);
  }

  public boolean copyFromOther(EngineSdkCallInformation other) {
    return EngineSdkJNI.EngineSdkCallInformation_copyFromOther(swigCPtr, this, EngineSdkCallInformation.getCPtr(other), other);
  }

  public boolean sendInfo(String controlInfo) {
    return EngineSdkJNI.EngineSdkCallInformation_sendInfo(swigCPtr, this, controlInfo);
  }

  public int sendDataOnMainVideoChannel(EngineSdkVideoPixelFormat pixelFormat, int frameWidth, int frameHeight, char frameRotation, byte[] dataBuffer) {
    return EngineSdkJNI.EngineSdkCallInformation_sendDataOnMainVideoChannel__SWIG_0(swigCPtr, this, pixelFormat.swigValue(), frameWidth, frameHeight, frameRotation, dataBuffer);
  }

  public int sendDataOnMainVideoChannel(EngineSdkVideoPixelFormat pixelFormat, int frameWidth, int frameHeight, char frameRotation, int bufferOffset, byte[] dataBuffer) {
    return EngineSdkJNI.EngineSdkCallInformation_sendDataOnMainVideoChannel__SWIG_1(swigCPtr, this, pixelFormat.swigValue(), frameWidth, frameHeight, frameRotation, bufferOffset, dataBuffer);
  }

  public int sendDataOnSecondVideoChannel(EngineSdkVideoPixelFormat pixelFormat, int frameWidth, int frameHeight, char frameRotation, byte[] dataBuffer) {
    return EngineSdkJNI.EngineSdkCallInformation_sendDataOnSecondVideoChannel(swigCPtr, this, pixelFormat.swigValue(), frameWidth, frameHeight, frameRotation, dataBuffer);
  }

  public int sendDataOnDataChannel(String dataBuffer, int bufferCapSize, int startPosition, int dataLen) {
    return EngineSdkJNI.EngineSdkCallInformation_sendDataOnDataChannel(swigCPtr, this, dataBuffer, bufferCapSize, startPosition, dataLen);
  }

  public boolean setTransmitingAudioMute(boolean mute) {
    return EngineSdkJNI.EngineSdkCallInformation_setTransmitingAudioMute(swigCPtr, this, mute);
  }

  public boolean setTransmitingVideoPaused(boolean pause) {
    return EngineSdkJNI.EngineSdkCallInformation_setTransmitingVideoPaused(swigCPtr, this, pause);
  }

  public boolean setDown2Audio(boolean pause) {
    return EngineSdkJNI.EngineSdkCallInformation_setDown2Audio(swigCPtr, this, pause);
  }

  public boolean setReceivingVideoPaused(boolean pause) {
    return EngineSdkJNI.EngineSdkCallInformation_setReceivingVideoPaused(swigCPtr, this, pause);
  }

  public boolean requestRemotePauseTransmitingVideo(boolean pause, boolean allowResetByPeer) {
    return EngineSdkJNI.EngineSdkCallInformation_requestRemotePauseTransmitingVideo(swigCPtr, this, pause, allowResetByPeer);
  }

  public boolean setRemoteMainVideoAdjustmentRangeLimitation(long callLocalToken, short frameHeight, short frameWidth, char remoteFps, short remoteKbps) {
    return EngineSdkJNI.EngineSdkCallInformation_setRemoteMainVideoAdjustmentRangeLimitation(swigCPtr, this, callLocalToken, frameHeight, frameWidth, remoteFps, remoteKbps);
  }

  public boolean clearRemoteMainVideoAdjustmentRangeLimitation(long callLocalToken) {
    return EngineSdkJNI.EngineSdkCallInformation_clearRemoteMainVideoAdjustmentRangeLimitation(swigCPtr, this, callLocalToken);
  }

  public boolean setLocalMainVideoAdjustmentRangeLimitation(long callLocalToken, short frameheight, short framewidth, char localFps, short localKbps) {
    return EngineSdkJNI.EngineSdkCallInformation_setLocalMainVideoAdjustmentRangeLimitation(swigCPtr, this, callLocalToken, frameheight, framewidth, localFps, localKbps);
  }

  public boolean clearLocalMainVideoAdjustmentRangeLimitation(long callLocalToken) {
    return EngineSdkJNI.EngineSdkCallInformation_clearLocalMainVideoAdjustmentRangeLimitation(swigCPtr, this, callLocalToken);
  }

  public boolean synchronizeLocalVideoFilterToRemote(long callLocalToken, long filterType) {
    return EngineSdkJNI.EngineSdkCallInformation_synchronizeLocalVideoFilterToRemote(swigCPtr, this, callLocalToken, filterType);
  }

  public boolean captureWonderfulMoment(String videoFileFullPath) {
    return EngineSdkJNI.EngineSdkCallInformation_captureWonderfulMoment(swigCPtr, this, videoFileFullPath);
  }

}
