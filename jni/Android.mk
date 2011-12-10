# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS += -I$(LOCAL_PATH)/boost/include/
LOCAL_CPPFLAGS += -fexceptions
LOCAL_CPPFLAGS += -frtti

LOCAL_LDLIBS += -L$(LOCAL_PATH)/boost/lib/ -lboost_system  -lz
LOCAL_MODULE    := echonest-codegen
LOCAL_SRC_FILES := AudioBufferInput.cpp Base64.cpp Fingerprint.cpp MatrixUtility.cpp SubbandAnalysis.cpp Codegen.cpp  Whitening.cpp AudioStreamInput.cpp 


include $(BUILD_SHARED_LIBRARY)
