#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring

JNICALL
Java_com_opencv_businesscamera_opencvc1_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Image is saved";
    return env->NewStringUTF(hello.c_str());

}
