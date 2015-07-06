#ifndef _UTIL_H
#define _UTIL_H

#include <chrono>

std::chrono::nanoseconds nanoDiffSysTimeToNow(std::chrono::system_clock::time_point lastTimePoint);

#endif
