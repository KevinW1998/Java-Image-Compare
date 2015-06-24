/*
RGBAImage.h
Copyright (C) 2006 Yangli Hector Yee

This program is free software; you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation; either version 2 of the License,
or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program;
if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

#ifndef _RGAIMAGE_H
#define _RGBAIMAGE_H

#include <string>
#ifdef DEF_USE_FREEIMAGE
#include <FreeImage.h>
#endif

/** Class encapsulating an image containing R,G,B,A channels.
 *
 * Internal representation assumes data is in the ABGR format, with the RGB
 * color channels premultiplied by the alpha value.  Premultiplied alpha is
 * often also called "associated alpha" - see the tiff 6 specification for some
 * discussion - http://partners.adobe.com/asn/developer/PDFS/TN/TIFF6.pdf
 *
 */
class RGBAImage
{
	RGBAImage(const RGBAImage&);
	RGBAImage& operator=(const RGBAImage&);
public:
	RGBAImage(int w, int h, const char *name = 0)
	{
		Width = w;
		Height = h;
		if (name) Name = name;
		Data = new unsigned int[w * h];
	};
	~RGBAImage() { if (Data) delete[] Data; }
	unsigned char Get_Red(unsigned int i) { return (Data[i] & 0xFF); }
	unsigned char Get_Green(unsigned int i) { return ((Data[i]>>8) & 0xFF); }
	unsigned char Get_Blue(unsigned int i) { return ((Data[i]>>16) & 0xFF); }
	unsigned char Get_Alpha(unsigned int i) { return ((Data[i]>>24) & 0xFF); }
	void Set(unsigned char r, unsigned char g, unsigned char b, unsigned char a, unsigned int i)
	{ Data[i] = r | (g << 8) | (b << 16) | (a << 24); }
	int Get_Width(void) const { return Width; }
	int Get_Height(void) const { return Height; }
	void Set(int x, int y, unsigned int d) { Data[x + y * Width] = d; }
	unsigned int Get(int x, int y) const { return Data[x + y * Width]; }
	unsigned int Get(int i) const { return Data[i]; }
	const std::string &Get_Name(void) const { return Name; }
    RGBAImage* DownSample() const;
    static RGBAImage* newImageByData(int w, int h, unsigned char* data){
        RGBAImage* img = new RGBAImage(w,h);
        memcpy(img->Data, data, w*h*4);
        return img;
    }

#ifdef DEF_USE_FREEIMAGE
    static RGBAImage* ReadFromFile(const char* filename)
    {
        const FREE_IMAGE_FORMAT fileType = FreeImage_GetFileType(filename);
        if(FIF_UNKNOWN == fileType)
        {
            printf("Unknown filetype %s\n", filename);
            return 0;
        }

        FIBITMAP* freeImage = 0;
        if(FIBITMAP* temporary = FreeImage_Load(fileType, filename, 0))
        {
            freeImage = FreeImage_ConvertTo32Bits(temporary);
            FreeImage_Unload(temporary);
        }
        if(!freeImage)
        {
            printf( "Failed to load the image %s\n", filename);
            return 0;
        }

        const int w = FreeImage_GetWidth(freeImage);
        const int h = FreeImage_GetHeight(freeImage);

        RGBAImage* result = new RGBAImage(w, h, filename);
        // Copy the image over to our internal format, FreeImage has the scanlines bottom to top though.
        unsigned int* dest = result->Data;
        for( int y=0; y < h; y++, dest += w )
        {
            const unsigned int* scanline = (const unsigned int*)FreeImage_GetScanLine(freeImage, h - y - 1 );
            memcpy(dest, scanline, sizeof(dest[0]) * w);
        }

        FreeImage_Unload(freeImage);
        return result;
    }
#endif
	
protected:
	int Width;
	int Height;
	std::string Name;
	unsigned int *Data;
};

#endif
