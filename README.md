# Java Image Compare

This is a library for comparing images in java. It is based on pdiff with some improvements.
The native dependencies are included in the resource folder. If you want to compile the native library for your platform (pdiffLIB) then use cmake to do so.
This library is based on the work of Perceptual Image Diff algorithm written in C++

## Building java snapshot
You can use maven to build the library.

## Building the native library
### Requirements
* CMake
* A compiler of your choice.

### How to build
1. Configure the project with cmake. 
2. Be sure to have a compiler configured (I recommend MinGw/g++)
3. Set following variables
   Either use: 
   * JDK_PATH  -  Will try to find out the library path (jvm.lib, jvm.dylib, jvm.so), the include file path (jni.h) and the include file path for the md file (jni_md.h)
   
   or if the above fails to configure, you can set these three variables:
   
   * JDK_PATH_INCLUDE_JNI       -  Path to jni.h
   * JDK_PATH_INCLUDE_JNI_MD    -  Path to jni_md.h
   * JDK_PATH_LIBRARY           -  Path to jvm.lib (windows) or jvm.dylib (OS X) or jvm.so (Linux)

4. If you compiled your native library then copy your compiled library to /src/main/resources/natives/{arch}bit/{your library}
   Where {arch} is the architecture 32bit or 64bit. Be sure that your libary is called either libpdiff.dll, libpdiff.dylib, libpdiff.so.
   
5. Now you can build your snapshot and use it.