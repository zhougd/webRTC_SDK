/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public enum EngineSdkAccountState {
  ES_STATE_ON_ACC_UNREGISTERED,
  ES_STATE_ON_ACC_REGISTERING,
  ES_STATE_ON_ACC_REGISTERED,
  ES_STATE_ON_ACC_DELETING,
  ES_STATE_ON_ACC_DELETED;

  public final int swigValue() {
    return swigValue;
  }

  public static EngineSdkAccountState swigToEnum(int swigValue) {
    EngineSdkAccountState[] swigValues = EngineSdkAccountState.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (EngineSdkAccountState swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + EngineSdkAccountState.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private EngineSdkAccountState() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private EngineSdkAccountState(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private EngineSdkAccountState(EngineSdkAccountState swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

