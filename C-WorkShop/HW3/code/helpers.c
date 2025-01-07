#include "helpers.h"
#include <math.h>

/* fill in your details here:
    ID1: 207036211
    ID2: 209120195
    Don't forget to comment your code
    Good Luck!
*/

// Create an entirely black image
// Demonstrate changing values of pixels
void blacksquare(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            if (image[i][j].rgbtBlue > 128 ||
                image[i][j].rgbtGreen > 128 ||
                image[i][j].rgbtRed > 128)
            {
                image[i][j].rgbtBlue = 255;
                image[i][j].rgbtGreen = 255;
                image[i][j].rgbtRed = 255;
            }
            else
            {
                image[i][j].rgbtBlue = 0;
                image[i][j].rgbtGreen = 0;
                image[i][j].rgbtRed = 0;
            }
        }
    }
}

// Convert image to grayscale
void grayscale(int height, int width, RGBTRIPLE image[height][width])
{
    int mean;

    // for etch row of pixels in image
    for (int row = 0; row < height; row++){
        // for etch pixel in row
        for(int pixel = 0; pixel < width; pixel++){

            // get mean value of pixel cooler
             mean = (image[row][pixel].rgbtBlue +
                        image[row][pixel].rgbtGreen +
                        image[row][pixel].rgbtRed) / 3;

            // input mean value to etch of the pixel cooler
            image[row][pixel].rgbtBlue = mean;
            image[row][pixel].rgbtGreen = mean;
            image[row][pixel].rgbtRed = mean;
        }
    }
}

// Convert image to sepia
void sepia(int height, int width, RGBTRIPLE image[height][width])
{
    int sepiaRed, sepiaGreen, sepiaBlue;

    // for etch row of pixels in image
    for (int row = 0 ; row < height; row++) {
        // for etch pixel in row
        for (int pixel = 0; pixel < width; pixel++){

            // use sepia calculation to calc the sepia value of etch of the pixel cooler
             sepiaRed = round(.393 * image[row][pixel].rgbtRed +
                                    .769 * image[row][pixel].rgbtGreen +
                                    .189 * image[row][pixel].rgbtBlue);

             sepiaGreen = round(.349 * image[row][pixel].rgbtRed +
                                    .686 * image[row][pixel].rgbtGreen +
                                    .168 * image[row][pixel].rgbtBlue);

             sepiaBlue = round(.272 * image[row][pixel].rgbtRed +
                                    .534 * image[row][pixel].rgbtGreen +
                                    .131 * image[row][pixel].rgbtBlue);

            // reduce sepia value to 255 if it is over to not exceed 8 bit
            if (sepiaRed > 255){
                sepiaRed = 255;
            }
            if (sepiaBlue > 255){
                sepiaBlue = 255;
            }
            if (sepiaGreen > 255){
                sepiaGreen = 255;
            }

            // input new sepia value to etch of the pixel cooler
            image[row][pixel].rgbtBlue  =  sepiaBlue;
            image[row][pixel].rgbtRed   =  sepiaRed;
            image[row][pixel].rgbtGreen =  sepiaGreen;
        }
    }
}

// Reflect image horizontally
void reflect(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE mirrorPixel, originalPixel;

    // for etch row of pixels in image
    for (int row = 0 ; row < height; row++){
        // for etch pixel in row
        for (int pixel = 0; pixel < width/2 ; pixel++){

            // save both values of original and mirror pixels
            mirrorPixel = image[row][width - pixel- 1];
            originalPixel = image[row][pixel];

            // replace values
            image[row][pixel] = mirrorPixel;
            image[row][width - pixel- 1] = originalPixel;
        }
    }
}
