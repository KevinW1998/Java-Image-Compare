#include "util.h"


std::chrono::nanoseconds nanoDiffSysTimeToNow(std::chrono::system_clock::time_point lastTimePoint)
{
    std::chrono::system_clock::duration diff = std::chrono::system_clock::now() - lastTimePoint;
    return std::chrono::duration_cast<std::chrono::nanoseconds>(diff);
}
