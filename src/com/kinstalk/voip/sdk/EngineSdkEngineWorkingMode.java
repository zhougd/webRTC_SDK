/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public enum EngineSdkEngineWorkingMode {
  ES_ENGINE_WORKING_MODE_NORMAL,
  ES_ENGINE_WORKING_MODE_STANDBY;

  public final int swigValue() {
    return swigValue;
  }

  public static EngineSdkEngineWorkingMode swigToEnum(int swigValue) {
    EngineSdkEngineWorkingMode[] swigValues = EngineSdkEngineWorkingMode.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (EngineSdkEngineWorkingMode swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + EngineSdkEngineWorkingMode.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private EngineSdkEngineWorkingMode() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private EngineSdkEngineWorkingMode(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private EngineSdkEngineWorkingMode(EngineSdkEngineWorkingMode swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}

