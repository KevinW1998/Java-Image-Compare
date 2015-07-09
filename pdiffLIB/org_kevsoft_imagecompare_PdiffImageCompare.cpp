#include "org_kevsoft_imagecompare_PdiffImageCompare.h"

#include "CompareArgs.h"
#include "RGBAImage.h"
#include "Metric.h"
#include "ThreadedQueue.h"

#include <iostream>
#include <utility>

#include <thread>
#include <memory>

#include <chrono>
#include "util.h"


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
    args.Verbose = false;

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

#define CHECK_JCLASS(jvm_env, clsVar, clsName) \
    if(!clsVar) {\
        std::string errMsg = "Did not find class ";\
        errMsg += clsName;\
        jvm_env->ThrowNew(jvm_env->FindClass("java/lang/ClassNotFoundException"), errMsg.c_str());\
        return;\
    }

#define CHECK_JMETHODID(jvm_env, methodIDVar, methodName) \
    if(!methodIDVar) {\
        std::string errMsg = "Did not find method ";\
        errMsg += methodName;\
        jvm_env->ThrowNew(jvm_env->FindClass("java/lang/NoSuchMethodError"), errMsg.c_str());\
        return;\
    }

extern "C" JNIEXPORT JNIEXPORT void JNICALL Java_org_kevsoft_imagecompare_PdiffImageCompare_nativeCompareFailedPixelsMultiple
    (JNIEnv *env, jclass /*staticPdiffClass*/, jobjectArray objectToTest, jint numThreads, jobject outHashMap){
    //using namespace std::chrono;
    //system_clock::time_point current = system_clock::now();

    int lengthOfArray = env->GetArrayLength(objectToTest);
    //Don't use more threads than needed.
    numThreads = std::min(lengthOfArray, (int)numThreads);

    //Query all java classes and methods:
    jclass PdiffImageCompareClass = env->FindClass("org/kevsoft/imagecompare/PdiffImageCompare");
    jclass ImageCompareClass = env->GetSuperclass(PdiffImageCompareClass);
    jclass ImageUtilsClass = env->FindClass("org/kevsoft/imagecompare/ImageUtils");
    jclass BufferedImageClass = env->FindClass("java/awt/image/BufferedImage");

    CHECK_JCLASS(env, PdiffImageCompareClass, "org.kevsoft.imagecompare.PdiffImageCompare")
    CHECK_JCLASS(env, ImageCompareClass, "org.kevsoft.imagecompare.ImageCompare")
    CHECK_JCLASS(env, ImageUtilsClass, "org.kevsoft.imagecompare.ImageUtils")
    CHECK_JCLASS(env, BufferedImageClass, "java.awt.image.BufferedImage")

    jmethodID ImageCompare_getFirstOptimizedImageMethodID = env->GetMethodID(ImageCompareClass, "getFirstOptimizedImage", "()Ljava/awt/image/BufferedImage;");
    jmethodID ImageCompare_getSecondOptimizedImageMethodID = env->GetMethodID(ImageCompareClass, "getSecondOptimizedImage", "()Ljava/awt/image/BufferedImage;");

    jmethodID BufferedImage_getWidthMethodID = env->GetMethodID(BufferedImageClass, "getWidth", "()I");
    jmethodID BufferedImage_getHeightMethodID = env->GetMethodID(BufferedImageClass, "getHeight", "()I");

    jmethodID ImageUtils_getBytePixelsMethodID = env->GetStaticMethodID(ImageUtilsClass, "getBytePixels", "(Ljava/awt/image/BufferedImage;)[B");

    CHECK_JMETHODID(env, ImageCompare_getFirstOptimizedImageMethodID, "org.kevsoft.imagecompare.ImageCompare.getFirstOptimizedImage");
    CHECK_JMETHODID(env, ImageCompare_getSecondOptimizedImageMethodID, "org.kevsoft.imagecompare.ImageCompare.getSecondOptimizedImage");
    CHECK_JMETHODID(env, BufferedImage_getWidthMethodID, "java.awt.image.BufferedImage.getWidth");
    CHECK_JMETHODID(env, BufferedImage_getHeightMethodID, "java.awt.image.BufferedImage.getHeight");
    CHECK_JMETHODID(env, ImageUtils_getBytePixelsMethodID, "org.kevsoft.imagecompare.ImageUtils.getBytePixels");


    ThreadedQueue<std::pair<jobject, std::shared_ptr<CompareArgs> > > in;
    ThreadedQueue<std::pair<jobject, int> > out;
    std::vector<std::thread> threads;

    for(int i = 0; i < lengthOfArray; ++i){
        jobject nextObj = env->GetObjectArrayElement(objectToTest, i);

        jdouble fov = env->GetDoubleField(nextObj, env->GetFieldID(PdiffImageCompareClass, "fov", "D"));
        jdouble gamma = env->GetDoubleField(nextObj, env->GetFieldID(PdiffImageCompareClass, "gamma", "D"));
        jdouble luminance = env->GetDoubleField(nextObj, env->GetFieldID(PdiffImageCompareClass, "luminance", "D"));
        jboolean luminanceonly = env->GetBooleanField(nextObj, env->GetFieldID(PdiffImageCompareClass, "luminanceonly", "Z"));
        jdouble colorfactor = env->GetDoubleField(nextObj, env->GetFieldID(PdiffImageCompareClass, "colorfactor", "D"));
        jint downsample = env->GetIntField(nextObj, env->GetFieldID(PdiffImageCompareClass, "downsample", "I"));

        // Get both buffered images:
        jobject bufferedImageFirst = env->CallObjectMethod(nextObj, ImageCompare_getFirstOptimizedImageMethodID);
        if(env->ExceptionCheck()){return;}
        jobject bufferedImageSecond = env->CallObjectMethod(nextObj, ImageCompare_getSecondOptimizedImageMethodID);
        if(env->ExceptionCheck()){return;}

        if(!bufferedImageFirst || !bufferedImageSecond){
            env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"), "Failed to allocate memory for optimized pdiff image!");
            return;
        }

        jint width1 = env->CallIntMethod(bufferedImageFirst, BufferedImage_getWidthMethodID);
        jint height1 = env->CallIntMethod(bufferedImageFirst, BufferedImage_getHeightMethodID);
        jint width2 = env->CallIntMethod(bufferedImageSecond, BufferedImage_getWidthMethodID);
        jint height2 = env->CallIntMethod(bufferedImageSecond, BufferedImage_getHeightMethodID);

        if(width1 != width2 || height1 != height2){
            env->ThrowNew(env->FindClass("java/lang/RuntimeException"), "Different sizes in optimized Image!");
            return;
        }

        // Get pixel byte array object
        jobject byteArrayObject1 = env->CallStaticObjectMethod(ImageUtilsClass, ImageUtils_getBytePixelsMethodID, bufferedImageFirst);
        jobject byteArrayObject2 = env->CallStaticObjectMethod(ImageUtilsClass, ImageUtils_getBytePixelsMethodID, bufferedImageSecond);
        if(!byteArrayObject1 || !byteArrayObject2){
            env->ThrowNew(env->FindClass("java/lang/OutOfMemoryError"), "Failed to allocate memory for optimized pdiff image byte array!");
            return;
        }

        // Convert to byte array
        jbyteArray* byteArray1 = reinterpret_cast<jbyteArray*>(&byteArrayObject1);
        jbyteArray* byteArray2 = reinterpret_cast<jbyteArray*>(&byteArrayObject2);

        // Convert to native byte array
        jbyte* iPixOfImage1 = env->GetByteArrayElements(*byteArray1, NULL);
        jbyte* iPixOfImage2 = env->GetByteArrayElements(*byteArray2, NULL);

        std::shared_ptr<CompareArgs> args = std::shared_ptr<CompareArgs>(new CompareArgs());
        args->ImgA = RGBAImage::newImageByData(width1, height1, (unsigned char*)iPixOfImage1);
        args->ImgB = RGBAImage::newImageByData(width2, height2, (unsigned char*)iPixOfImage2);

        args->ThresholdPixels = -1;
        args->FieldOfView = (float)fov;
        args->Gamma = (float)gamma;
        args->Luminance = (float)luminance;
        args->LuminanceOnly = luminanceonly;
        args->ColorFactor = (float)colorfactor;
        args->DownSample = downsample;

        args->outputDebugTime = false;
        args->Verbose = false;

        // Release native byte array
        env->ReleaseByteArrayElements(*byteArray1, iPixOfImage1, JNI_ABORT);
        env->ReleaseByteArrayElements(*byteArray2, iPixOfImage2, JNI_ABORT);

        in.push(std::make_pair(nextObj, args));
    }
    //std::cout << "Time taken to take down JVM: " << nanoDiffSysTimeToNow(current).count() << std::endl;
    //std::cout << "Using num of cores: " << numThreads << std::endl;

    //system_clock::time_point currentBeforeCompare = system_clock::now();
    for(int i = 0; i < numThreads; ++i){
        threads.emplace_back([&in, &out](){
            while(!in.isEmpty()){
                std::pair<jobject, std::shared_ptr<CompareArgs> > nextComparing = in.pop();

                bool success = Yee_Compare(*(nextComparing.second));
                if(!success){
                    std::cout << nextComparing.second->ErrorStr << std::endl;
                    out.push(std::make_pair(nextComparing.first, -1));
                }else{
                    out.push(std::make_pair(nextComparing.first, nextComparing.second->pixelsFailed));
                }
            }
        });
    }

    //sync all threads
    for(int i = 0; i < threads.size(); ++i)
        threads[i].join();
    //std::cout << "Time taken to take down Images: " << nanoDiffSysTimeToNow(currentBeforeCompare).count() << std::endl;

    while(!out.isEmpty()){
        std::pair<jobject, int> nextResult = out.pop();

        jclass intClass = env->FindClass("java/lang/Integer");
        jmethodID intClassConstructorMethodID = env->GetMethodID(intClass, "<init>", "(I)V");
        jobject newIntObj = env->NewObject(intClass, intClassConstructorMethodID, nextResult.second);


        jclass hashMapClass = env->GetObjectClass(outHashMap);
        jmethodID hashMapPutMethodID = env->GetMethodID(hashMapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        env->CallObjectMethod(outHashMap, hashMapPutMethodID, nextResult.first, newIntObj);

        env->DeleteLocalRef(newIntObj);
    }
}
