/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkEngineInformation {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkEngineInformation(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkEngineInformation obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkEngineInformation(swigCPtr);
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
    EngineSdkJNI.EngineSdkEngineInformation_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    EngineSdkJNI.EngineSdkEngineInformation_change_ownership(this, swigCPtr, true);
  }

  public EngineSdkEngineInformation() {
    this(EngineSdkJNI.new_EngineSdkEngineInformation(), true);
    EngineSdkJNI.EngineSdkEngineInformation_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public void setEngineConfiguration(EngineSdkEngineConfiguration value) {
    EngineSdkJNI.EngineSdkEngineInformation_engineConfiguration_set(swigCPtr, this, EngineSdkEngineConfiguration.getCPtr(value), value);
  }

  public EngineSdkEngineConfiguration getEngineConfiguration() {
    long cPtr = EngineSdkJNI.EngineSdkEngineInformation_engineConfiguration_get(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkEngineConfiguration(cPtr, false);
  }

  public void setEngineState(EngineSdkEngineState value) {
    EngineSdkJNI.EngineSdkEngineInformation_engineState_set(swigCPtr, this, value.swigValue());
  }

  public EngineSdkEngineState getEngineState() {
    return EngineSdkEngineState.swigToEnum(EngineSdkJNI.EngineSdkEngineInformation_engineState_get(swigCPtr, this));
  }

  public EngineSdkEngineInformation clone() {
    long cPtr = EngineSdkJNI.EngineSdkEngineInformation_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkEngineInformation(cPtr, false);
  }

}