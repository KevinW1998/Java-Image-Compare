/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_kevsoft_imagecompare_PdiffImageCompare */

#ifndef _Included_org_kevsoft_imagecompare_PdiffImageCompare
#define _Included_org_kevsoft_imagecompare_PdiffImageCompare
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_kevsoft_imagecompare_PdiffImageCompare
 * Method:    nativeCompare
 * Signature: (I[BII[BII)Z
 */
JNIEXPORT jboolean JNICALL Java_org_kevsoft_imagecompare_PdiffImageCompare_nativeCompare
  (JNIEnv *, jobject, jint, jbyteArray, jint, jint, jbyteArray, jint, jint);

/*
 * Class:     org_kevsoft_imagecompare_PdiffImageCompare
 * Method:    nativeCompareFailedPixels
 * Signature: ([BII[BII)I
 */
JNIEXPORT jint JNICALL Java_org_kevsoft_imagecompare_PdiffImageCompare_nativeCompareFailedPixels
  (JNIEnv *, jobject, jbyteArray, jint, jint, jbyteArray, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
