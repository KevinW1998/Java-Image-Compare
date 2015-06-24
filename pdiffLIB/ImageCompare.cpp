class ImageCompare {

    //Fields
private:
     BufferedImage  firstImage;
     BufferedImage  secondImage;
     double scalefactor;
     Dimension sizeScale;

    //private
    BufferedImage  optimizeImage(BufferedImage  img){
        BufferedImage  opImg = img;
        if(sizeScale!=null){
            opImg = ImageUtils.scaleToFixSize(sizeScale, opImg);
        }
        if(scalefactor!=1.0){
            opImg = ImageUtils.scale(scalefactor, opImg);
        }
        return opImg;
    }
    bool needOptimization(){
        return scalefactor == 1.0 ||
                sizeScale != 0;
    }
public:
    //Normal Getter Setter
    BufferedImage  getFirstSourceImage() {

        return firstImage;
    }
     void setFirstSourceImage(BufferedImage  firstImage) {
        this.firstImage = firstImage;
    }
     BufferedImage  getSecondSourceImage() {
        return secondImage;
    }
     void setSecondSourceImage(BufferedImage  secondImage) {
        this.secondImage = secondImage;
    }
     double getScalefactor() {
        return scalefactor;
    }
    void setScale(double scalefactor) {
        this.scalefactor = scalefactor;
    }
     Dimension getSizeScale() {
        return sizeScale;
    }
     void setSizeScale(Dimension sizeScale) {
        this.sizeScale = sizeScale;
    }

    //Special Getter/Setter
     BufferedImage  getFirstOptimizedImage(){
        if(needOptimization()){
            return optimizeImage(firstImage);
        }else{
            return getFirstSourceImage();
        }
    }

     BufferedImage  getSecondOptimizedImage(){
        if(needOptimization()){
            return optimizeImage(secondImage);
        }else{
            return getSecondSourceImage();
        }

    }

}
