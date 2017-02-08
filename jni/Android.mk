LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := EngineSDK
LOCAL_SRC_FILES := EngineSDK.cpp

include $(BUILD_SHARED_LIBRARY)
