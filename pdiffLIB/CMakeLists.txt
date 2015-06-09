

project(pdiff)

set(JDK_PATH "C:/Program Files (x86)/Java/jdk1.8.0_25" CACHE PATH "The path to the JDK")
MESSAGE( STATUS "JDK Path:         " ${JDK_PATH} )


cmake_minimum_required(VERSION 2.8)
aux_source_directory(. SRC_LIST)

#http://stackoverflow.com/questions/20790001/jni-does-not-work-with-stdcall
if (MINGW)
    MESSAGE( STATUS "MINGW" )
     set(CMAKE_C_FLAGS "-Wl,--kill-at ${CMAKE_C_FLAGS} -static-libgcc")
     set(CMAKE_CXX_FLAGS "-Wl,--kill-at ${CMAKE_CXX_FLAGS} -static-libgcc -static-libstdc++")
     set(CMAKE_SHARED_LIBRARY_LINK_C_FLAGS "${CMAKE_SHARED_LIBRARY_LINK_C_FLAGS} -static-libgcc -s")
     set(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS "${CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS} -static-libgcc -static-libstdc++ -s")
endif()

#LINK_DIRECTORIES(${JDK_PATH}/lib)
#TARGET_LINK_LIBRARIES(${PROJECT_NAME} jvm.lib)


#includes
include_directories(${JDK_PATH}/include)
IF (WIN32)
  # include for windows
  include_directories(${JDK_PATH}/include/win32)
ELSE()
  # set stuff for other systems
  include_directories(${JDK_PATH}/include/linux)
ENDIF()

#libraries

#add_library(jvm STATIC IMPORTED)
#set_property(TARGET jvm PROPERTY IMPORTED_LOCATION ${JDK_PATH}/lib/jvm)


link_directories(${JDK_PATH}/lib)
add_library(${PROJECT_NAME} SHARED PerceptualDiff.cpp Metric.h Metric.cpp LPyramid.h Lpyramid.cpp RGBAImage.h RGBAImage.cpp CompareArgs.cpp CompareArgs.h org_kevsoft_imagecompare_PdiffImageCompare.h org_kevsoft_imagecompare_PdiffImageCompare.cpp)
target_link_libraries(${PROJECT_NAME} jvm)


#target_link_libraries(${PROJECT_NAME} jvm)

