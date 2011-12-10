#include <string.h>
#include <jni.h>
 
jstring Java_org_cuiBono_CuiBono_getCodeGen(JNIEnv* env, jobject javaThis) {
  return (*env)->NewStringUTF(env, "Hello from native code!");
}
