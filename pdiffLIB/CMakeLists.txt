# Example Command Line:
# -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_20"
# -DUSE_FREEIMAGE:BOOL=ON
# -DCOMPILE_EXECUTABLE:BOOL=ON

# Examples:
# --> -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_20" -DUSE_FREEIMAGE:BOOL=OFF -DCOMPILE_EXECUTABLE:BOOL=OFF
# --> -DJDK_PATH:PATH="C:\Program Files (x86)\Java\jdk1.8.0_25" -DUSE_FREEIMAGE:BOOL=OFF -DCOMPILE_EXECUTABLE:BOOL=OFF

# bin is the recommended build folder!

project(pdiff)

# Settings
set(JDK_PATH "C:/Java/jdk1.8.0_45" CACHE PATH "The path to the JDK")
set(USE_FREEIMAGE ON CACHE BOOL "Set to on, if you want to use freeimage! (Free Image must be compiled!)")
set(COMPILE_EXECUTABLE ON CACHE BOOL "Set to on, to compile the test unit/executable")
MESSAGE( STATUS "[Image Compare] [User-Value] JDK Path:         " ${JDK_PATH} )



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
endif()

#TODO: Static also for other platforms



#includes
MESSAGE( STATUS "[Image Compare] Adding JDK Includes" )
FIND_PATH(JDK_INCLUDES_DIR jni.h
  ${JDK_PATH}/include # for windows and linux
  ${JDK_PATH}/Contents/Home/include # for mac
)
FIND_PATH(JDK_MD_INCLUDES_DIR jni_md.h
  ${JDK_PATH}/include/win32 # for windows
  ${JDK_PATH}/Contents/Home/include/darwin # for mac
)

IF(JDK_INCLUDES_DIR)
  include_directories(${JDK_INCLUDES_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK (jni.h) includes! Wrong JDK Path?" )
ENDIF()
IF(JDK_MD_INCLUDES_DIR)
  include_directories(${JDK_MD_INCLUDES_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK (jni_md.h) includes! Wrong JDK Path?" )
ENDIF()

#libraries
set(FILES_CORE PerceptualDiff.cpp Metric.h Metric.cpp LPyramid.h LPyramid.cpp RGBAImage.h RGBAImage.cpp CompareArgs.cpp CompareArgs.h ImageCompare.cpp ImageCompare.h util.h util.cpp ThreadedQueue.h)
set(FILES_JAVA org_kevsoft_imagecompare_PdiffImageCompare.cpp org_kevsoft_imagecompare_PdiffImageCompare.h)

# Add link dirs
MESSAGE( STATUS "[Image Compare] Adding JDK libraries" )

# Find link dir
FIND_LIBRARY(JDK_LINK_DIR jvm
    ${JDK_PATH}/lib
    ${JDK_PATH}/jre/lib/amd64/server
    ${JDK_PATH}/Contents/Home/jre/lib/server
  )

# Create library
add_library(${PROJECT_NAME} SHARED ${FILES_CORE} ${FILES_JAVA})
IF(COMPILE_EXECUTABLE)
  add_executable(${PROJECT_NAME}-test ${FILES_CORE} executable/main.cpp)
ENDIF(COMPILE_EXECUTABLE)
# Hints for linker
MESSAGE( STATUS "[Image Compare] Link JDK libraries" )


IF(JDK_LINK_DIR)
  target_link_libraries(${PROJECT_NAME} ${JDK_LINK_DIR})
ELSE()
  MESSAGE( SEND_ERROR "[Image Compare] Did not find JDK library!" )
ENDIF(JDK_LINK_DIR)


MESSAGE( STATUS "[Image Compare] Set output path" )
INSTALL(TARGETS ${PROJECT_NAME} DESTINATION bin)




# Adding FreeImage
IF(USE_FREEIMAGE)
  MESSAGE( STATUS "[Image Compare] [USING FREEIMAGE]" )
  add_definitions( -DDEF_USE_FREEIMAGE )
  MESSAGE( STATUS "[Image Compare] Looking for FreeImage includes" )
  FIND_PATH(FREEIMAGE_INCLUDE_DIR FreeImage.h
    /usr/local/include
    /usr/include
    /opt/local/include
    ../libs/FreeImage/Source
  )

  MESSAGE( STATUS "[Image Compare] Looking for FreeImage library" )
  FIND_LIBRARY(FREEIMAGE_LIBRARY freeimage
    /usr/lib
    /usr/local/lib
    /opt/local/lib
    ../libs/FreeImage
  )


  IF(FREEIMAGE_INCLUDE_DIR)
    MESSAGE( STATUS "[Image Compare] Set output path" )
    IF(FREEIMAGE_LIBRARY)
      SET( FREEIMAGE_FOUND "YES" )
      SET( FREEIMAGE_LIBRARIES ${FREEIMAGE_LIBRARY} )
    ELSE()
      MESSAGE( SEND_ERROR "[Image Compare] Did not find FreeImage libraries!" )
    ENDIF(FREEIMAGE_LIBRARY)
  ELSE()
    MESSAGE( SEND_ERROR "[Image Compare] Did not find FreeImage includes!" )
  ENDIF(FREEIMAGE_INCLUDE_DIR)

  IF(FREEIMAGE_FOUND)
    INCLUDE_DIRECTORIES(${FREEIMAGE_INCLUDE_DIR})
    TARGET_LINK_LIBRARIES(${PROJECT_NAME} ${FREEIMAGE_LIBRARY})
  ENDIF(FREEIMAGE_FOUND)
ELSE()
  MESSAGE( STATUS "[Image Compare] [USING NOT FREEIMAGE]" )
ENDIF(USE_FREEIMAGE)

# activate C++11
MESSAGE( STATUS "[Image Compare] Force C++11 support" )
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 11)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD_REQUIRED ON)


#target_link_libraries(${PROJECT_NAME} jvm)

