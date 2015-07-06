#include "org_kevsoft_imagecompare_PdiffImageCompare.h"

#include "CompareArgs.h"
#include "RGBAImage.h"
#include "Metric.h"
#include "ThreadedQueue.h"

#include <iostream>
#include <utility>

extern "C" JNIEXPORT jint JNICALL Java_org_kevsoft_imagecompare_PdiffImageCompare_nativeCompareFailedPixels
  (JNIEnv * env, jobject theCompareObject,
   jbyteArray pixOfImage1, jint width1, jint height1,
   jbyteArray pixOfImage2, jint width2, jint height2){

    jbyte* iPixOfImage1 = env->GetByteArrayElements(pixOfImage1, NULL);
    jbyte* iPixOfImage2 = env->GetByteArrayElements(pixOfImage2, NULL);
    jclass compareClass = env->GetObjectClass(theCompareObject);

    //const char* packagePathToImageCompare = "Lorg/kevsoft/imagecompare/PdiffImageCompare;";
    jdouble fov = env->GetDoubleField(theCompareObject, env->GetFieldID(compareClass, "fov", "D"));
    jdouble gamma = env->GetDoubleField(theCompareObject, env->GetFieldID(compareClass, "gamma", "D"));
    jdouble luminance = env->GetDoubleField(theCompareObject, env->GetFieldID(compareClass, "luminance", "D"));
    jboolean luminanceonly = env->GetBooleanField(theCompareObject, env->GetFieldID(compareClass, "luminanceonly", "Z"));
    jdouble colorfactor = env->GetDoubleField(theCompareObject, env->GetFieldID(compareClass, "colorfactor", "D"));
    jint downsample = env->GetIntField(theCompareObject, env->GetFieldID(compareClass, "downsample", "I"));

    CompareArgs args;
    args.ImgA = RGBAImage::newImageByData(width1, height1, (unsigned char*)iPixOfImage1);
    args.ImgB = RGBAImage::newImageByData(width2, height2, (unsigned char*)iPixOfImage2);

    args.ThresholdPixels = -1;
    args.FieldOfView = (float)fov;
    args.Gamma = (float)gamma;
    args.Luminance = (float)luminance;
    args.LuminanceOnly = luminanceonly;
    args.ColorFactor = (float)colorfactor;
    args.DownSample = downsample;

    args.outputDebugTime = false;
    args.Verbose = true;

    env->ReleaseByteArrayElements(pixOfImage1, iPixOfImage1, JNI_ABORT);
    env->ReleaseByteArrayElements(pixOfImage2, iPixOfImage2, JNI_ABORT);

    bool success = Yee_Compare(args);
    if(!success){
        std::cout << args.ErrorStr << std::endl;
        return -1;
    }else{
        return args.pixelsFailed;
    }

}



extern "C" JNIEXPORT JNIEXPORT void JNICALL Java_org_kevsoft_imagecompare_PdiffImageCompare_nativeCompareFailedPixelsMultiple
    (JNIEnv *env, jclass staticPdiffClass, jobjectArray objectToTest, jobject out){
    int lengthOfArray = env->GetArrayLength(objectToTest);
    int threads = 8;
    for(int i = 0; i < lengthOfArray; i++){
        jobject nextPdiffObject = env->GetObjectArrayElement(objectToTest, i);
        ThreadedQueue<jobject> in;
        ThreadedQueue<std::pair<jobject, int> > out;

    }
}
