# Example Command Line:
# -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_20"
# -DUSE_FREEIMAGE:BOOL=ON
# -DCOMPILE_EXECUTABLE:BOOL=ON

# Examples:
# --> -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_20"
# --> -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_25"
# --> -DJDK_PATH:PATH="/usr/lib/jvm/java-1.8.0-openjdk-amd64"

# OR:
# --> -DJDK_PATH_INCLUDE_JNI:PATH="C:\jdk\jdk1.8.0_45\include" -DJDK_PATH_INCLUDE_JNI_MD:PATH="C:\jdk\jdk1.8.0_45\include\win32" -DJDK_PATH_LIBRARY:FILEPATH="C:\jdk\jdk1.8.0_45\lib\jvm.lib"

# bin is the recommended build folder!

project(pdiff)

# Settings
set(JDK_PATH "" CACHE PATH "The path to the JDK")
set(JDK_PATH_INCLUDE_JNI "" CACHE PATH "(Optional) The direct path to jni.h")
set(JDK_PATH_INCLUDE_JNI_MD "" CACHE PATH "(Optional) The direct path to jni_md.h")
set(JDK_PATH_LIBRARY "" CACHE FILEPATH "The direct filepath to the jni library")

cmake_minimum_required(VERSION 2.8)
aux_source_directory(. SRC_LIST)

#http://stackoverflow.com/questions/20790001/jni-does-not-work-with-stdcall
# For windows
IF(MINGW)
  MESSAGE( STATUS "[Image Compare] Detected MINGW, using --kill-at" )
  set(CMAKE_C_FLAGS "-Wl,--kill-at ${CMAKE_C_FLAGS} -static -static-libgcc")
  set(CMAKE_CXX_FLAGS "-Wl,--kill-at ${CMAKE_CXX_FLAGS} -static -static-libgcc -static-libstdc++")
  set(CMAKE_SHARED_LIBRARY_LINK_C_FLAGS "${CMAKE_SHARED_LIBRARY_LINK_C_FLAGS} -static-libgcc -s")
  set(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS "${CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS} -static-libgcc -static-libstdc++ -s")
ELSE()
  MESSAGE( STATUS "[Image Compare] Using NON-MINGW, building static" )
endif()




#includes
MESSAGE( STATUS "[Image Compare] Adding JDK Includes" )
IF(JDK_PATH)
  FIND_PATH(JDK_INCLUDES_DIR jni.h
    ${JDK_PATH}/include # for windows and linux
    ${JDK_PATH}/Contents/Home/include # for mac
  )
  FIND_PATH(JDK_MD_INCLUDES_DIR jni_md.h
    ${JDK_PATH}/include/win32 # for windows
    ${JDK_PATH}/Contents/Home/include/darwin # for mac
    ${JDK_PATH}/include/linux #for ubuntu
  )
ELSE()
  set(JDK_INCLUDES_DIR ${JDK_PATH_INCLUDE_JNI})
  set(JDK_MD_INCLUDES_DIR ${JDK_PATH_INCLUDE_JNI_MD})
ENDIF(JDK_PATH)

IF(IS_DIRECTORY ${JDK_INCLUDES_DIR})
  include_directories(${JDK_INCLUDES_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK (jni.h) includes! Wrong JDK Path?" )
ENDIF(IS_DIRECTORY ${JDK_INCLUDES_DIR})
IF(IS_DIRECTORY ${JDK_MD_INCLUDES_DIR})
  include_directories(${JDK_MD_INCLUDES_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK (jni_md.h) includes! Wrong JDK Path?" )
ENDIF(IS_DIRECTORY ${JDK_MD_INCLUDES_DIR})

#libraries
set(FILES_CORE PerceptualDiff.cpp Metric.h Metric.cpp LPyramid.h LPyramid.cpp RGBAImage.h RGBAImage.cpp CompareArgs.cpp CompareArgs.h ImageCompare.cpp ImageCompare.h util.h util.cpp ThreadedQueue.h)
set(FILES_JAVA org_kevsoft_imagecompare_PdiffImageCompare.cpp org_kevsoft_imagecompare_PdiffImageCompare.h)

# Add link dirs
MESSAGE( STATUS "[Image Compare] Adding JDK libraries" )

# Find link dir
IF(JDK_PATH)
  FIND_LIBRARY(JDK_LINK_DIR jvm
    ${JDK_PATH}/lib
    ${JDK_PATH}/jre/lib/amd64/server
    ${JDK_PATH}/Contents/Home/jre/lib/server
  )
ELSE()
  set(JDK_LINK_DIR ${JDK_PATH_LIBRARY})
ENDIF(JDK_PATH)


# Create library
add_library(${PROJECT_NAME} SHARED ${FILES_CORE} ${FILES_JAVA})
# Hints for linker
MESSAGE( STATUS "[Image Compare] Link JDK libraries" )


IF(EXISTS ${JDK_LINK_DIR})
  target_link_libraries(${PROJECT_NAME} ${JDK_LINK_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK library!" )
ENDIF(EXISTS ${JDK_LINK_DIR})


MESSAGE( STATUS "[Image Compare] Set output path" )
INSTALL(TARGETS ${PROJECT_NAME} DESTINATION bin)


# activate C++11
MESSAGE( STATUS "[Image Compare] Force C++11 support" )
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD_REQUIRED ON)


#target_link_libraries(${PROJECT_NAME} jvm)

