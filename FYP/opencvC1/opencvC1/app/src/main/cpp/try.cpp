//
// Created by Tay on 27/3/2018.
//
#include <jni.h>
#include <string>
#include <opencv2/core.hpp>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_opencv_businesscamera_opencvc1_MainActivity_validate(JNIEnv *env, jobject, jlong addrGray, jlong addrRgba) {

    cv::Rect();
    cv::Mat();
    std::string hello2="Hello from validate";

    return env->NewStringUTF(hello2.c_str());

}
