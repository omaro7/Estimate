#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <math.h>
#include <syslog.h>
#include <numeric>

#define LOG_TAG "====================== native-lib :"
#include "logger.h"
#include "common.h"

using namespace std;

extern "C" {

    /**
    * AES256 IV
    */
    JNIEXPORT jstring JNICALL
    Java_kr_co_goms_app_estimate_jni_GomsJNI_ivKey(JNIEnv *env, jobject obj) {
        return env->NewStringUTF(KEY_AES256_IV);
    }

    /**
    * AES256 IV
    */
    JNIEXPORT jstring JNICALL
    Java_kr_co_goms_app_estimate_jni_GomsJNI_encryptKey(JNIEnv *env, jobject obj) {
        return env->NewStringUTF(KEY_ENCRYPT);
    }


}
