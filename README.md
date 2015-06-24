# Image Compare (old Name: Java Image Compare)

This is a library for comparing images. It is based on pdiff with some improvements.
FreeImage source files are directly in the libs folder. 
You can compile it with nearly any compiler (check Makefiles). 
You can find more information here [FreeImage](http://freeimage.sourceforge.net/).

## Requirements to build
* CMake
* A compiler of your choice.

## How to build
1. Clone this project
2. Be sure to have a compiler configured (I recommend MinGw/g++)
3. Build FreeImage in /libs/FreeImage/
4. Configure the cmake project.
  1. JDK_Path is the path of the JDK. This is required to make the library bindings for java. To do this you can set cmake variable argument to "-DJDK_PATH:PATH="{Your Path}", where {Your Path} is the path to your jdk.

