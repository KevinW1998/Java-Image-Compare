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
3. Build FreeImage in /libs/FreeImage/
4. Configure the cmake project.
  1. JDK_Path is the path of the JDK. This is required to make the library bindings for java. To do this you can set cmake variable argument to "-DJDK_PATH:PATH="{Your Path}", where {Your Path} is the path to your jdk.

