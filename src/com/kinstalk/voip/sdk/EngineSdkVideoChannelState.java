/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.kinstalk.voip.sdk;

public enum EngineSdkVideoChannelState {
  ES_STATE_ON_VIDEO_CHANNEL_CREATED,
  ES_STATE_ON_VIDEO_CHANNEL_ACTIVE,
  ES_STATE_ON_VIDEO_CHANNEL_INACTIVE,
  ES_STATE_ON_VIDEO_CHANNEL_DESTROYED;

  public final int swigValue() {
    return swigValue;
  }

  public static EngineSdkVideoChannelState swigToEnum(int swigValue) {
    EngineSdkVideoChannelState[] swigValues = EngineSdkVideoChannelState.class.getEnumConstants();
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (EngineSdkVideoChannelState swigEnum : swigValues)
      if (swigEnum.swigValue == swigValue)
        return swigEnum;
    throw new IllegalArgumentException("No enum " + EngineSdkVideoChannelState.class + " with value " + swigValue);
  }

  @SuppressWarnings("unused")
  private EngineSdkVideoChannelState() {
    this.swigValue = SwigNext.next++;
  }

  @SuppressWarnings("unused")
  private EngineSdkVideoChannelState(int swigValue) {
    this.swigValue = swigValue;
    SwigNext.next = swigValue+1;
  }

  @SuppressWarnings("unused")
  private EngineSdkVideoChannelState(EngineSdkVideoChannelState swigEnum) {
    this.swigValue = swigEnum.swigValue;
    SwigNext.next = this.swigValue+1;
  }

  private final int swigValue;

  private static class SwigNext {
    private static int next = 0;
  }
}
