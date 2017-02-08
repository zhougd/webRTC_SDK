/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public class EngineSdkAccountConfiguration {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected EngineSdkAccountConfiguration(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(EngineSdkAccountConfiguration obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        EngineSdkJNI.delete_EngineSdkAccountConfiguration(swigCPtr);
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
    EngineSdkJNI.EngineSdkAccountConfiguration_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    EngineSdkJNI.EngineSdkAccountConfiguration_change_ownership(this, swigCPtr, true);
  }

  public EngineSdkAccountConfiguration() {
    this(EngineSdkJNI.new_EngineSdkAccountConfiguration(), true);
    EngineSdkJNI.EngineSdkAccountConfiguration_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public void setApplicationDomain(String value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_applicationDomain_set(swigCPtr, this, value);
  }

  public String getApplicationDomain() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_applicationDomain_get(swigCPtr, this);
  }

  public void setLocalAccountId(String value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountId_set(swigCPtr, this, value);
  }

  public String getLocalAccountId() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountId_get(swigCPtr, this);
  }

  public void setLocalAccountName(String value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountName_set(swigCPtr, this, value);
  }

  public String getLocalAccountName() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountName_get(swigCPtr, this);
  }

  public void setLocalAccountPassword(String value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountPassword_set(swigCPtr, this, value);
  }

  public String getLocalAccountPassword() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountPassword_get(swigCPtr, this);
  }

  public void setLocalAccountCredentialsCount(long value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialsCount_set(swigCPtr, this, value);
  }

  public long getLocalAccountCredentialsCount() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialsCount_get(swigCPtr, this);
  }

  public void setLocalAccountCredentialInformation(EnginesdkCredentialInformation value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialInformation_set(swigCPtr, this, EnginesdkCredentialInformation.getCPtr(value), value);
  }

  public EnginesdkCredentialInformation getLocalAccountCredentialInformation() {
    long cPtr = EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialInformation_get(swigCPtr, this);
    return (cPtr == 0) ? null : new EnginesdkCredentialInformation(cPtr, false);
  }

  public void setLocalAccountCredentialResponseBody(String value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialResponseBody_set(swigCPtr, this, value);
  }

  public String getLocalAccountCredentialResponseBody() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountCredentialResponseBody_get(swigCPtr, this);
  }

  public void setLocalAccountInternalId(int value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_localAccountInternalId_set(swigCPtr, this, value);
  }

  public int getLocalAccountInternalId() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_localAccountInternalId_get(swigCPtr, this);
  }

  public void setRegisterReportTimeoutMs(int value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_registerReportTimeoutMs_set(swigCPtr, this, value);
  }

  public int getRegisterReportTimeoutMs() {
    return EngineSdkJNI.EngineSdkAccountConfiguration_registerReportTimeoutMs_get(swigCPtr, this);
  }

  public void setMAccountCallback(EngineSdkAccountCallback value) {
    EngineSdkJNI.EngineSdkAccountConfiguration_mAccountCallback_set(swigCPtr, this, EngineSdkAccountCallback.getCPtr(value), value);
  }

  public EngineSdkAccountCallback getMAccountCallback() {
    long cPtr = EngineSdkJNI.EngineSdkAccountConfiguration_mAccountCallback_get(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkAccountCallback(cPtr, false);
  }

  public EngineSdkAccountConfiguration clone() {
    long cPtr = EngineSdkJNI.EngineSdkAccountConfiguration_clone(swigCPtr, this);
    return (cPtr == 0) ? null : new EngineSdkAccountConfiguration(cPtr, false);
  }

}
