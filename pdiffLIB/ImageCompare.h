
#ifndef ImageCompare_h
#define ImageCompare_h
#include "RGBAImage.h"

class ImageCompare {

    //Fields
private:
     RGBAImage  firstImage;
     RGBAImage  secondImage;
     double fov;
     double gamma;
     double luminance;
     bool luminanceonly;
     double colorfactor;
     int downsample;

void test ();

public:
    PdiffImageCompare() {
        fov = 45.0;
        gamma = 2.2;
        luminance = 100.0;
        luminanceonly = false;
        colorfactor = 1.0;
        downsample = 0;
    }

    double getFov() {
            return fov;
    }
    void setFov(double fov) {
            this->fov = fov;
   }
    double getGamma() {
            return gamma;
    }
    void setGamma(double gamma) {
            this->gamma = gamma;
   }
    double getLuminance() {
           return luminance;
       }
    void setLuminance(double luminance) {
            this->luminance = luminance;
        }
    bool isLuminanceonly() {
            return luminanceonly;
        }
    void setLuminanceonly(bool luminanceonly) {
           this->luminanceonly = luminanceonly;
       }


    double getColorfactor() {
            return colorfactor;
        }

    void setColorfactor(double colorfactor) {
            this->colorfactor = colorfactor;
        }

    int getDownsample() {
            return downsample;
        }
    void setDownsample(int downsample) {
            this->downsample = downsample;
        }



};





#endif
