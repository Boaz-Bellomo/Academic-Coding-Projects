#include <stdio.h>

void decipher(int buses[], int length)
{
    // get fixed moving value from first bus number
    int movingValue = buses[0];

    // print every encrypted char
    for (int bus = 1; bus < length; ++bus) {
        char secretChar = buses[bus] + movingValue;
        printf("%c", secretChar);
    }

    // move down a line
    printf("\n");
}
void main(){
    int s1[7] = {9, 74, 92, 90, 105, 92, 107},
            s2[14] = {0, 72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33},
            s3[14] = {0, 89, 111, 117, 32, 71, 111, 116, 32, 84, 104, 105, 115, 33},
            s4[78] = {122, -71, -45, -90, -3, -17, -14, -14, -90, -24, -21, -90, -6, -8, -25, -12, -7, -20, -20, -21, -8, -21, -22, -90, -11, -12, -90, -6, -18, -21, -90, -69, -6, -18, -90, -11, -20, -90, -48, -25, -12, -5, -25, -8, -1, -78, -112, -55, -25, -12, -90, -24, -21, -90, -6, -25, -15, -21, -12, -90, -5, -12, -22, -21, -8, -90, -6, -18, -21, -17, -8, -90, -12, -11, -7, -21, -7, -76},
            s5[18] = {89, -5, 15, 12, 32, -57, 8, 25, 12, -57, 22, 21, 27, 22, -57, 20, 12, -56};
    decipher(s1, 7);
    decipher(s2, 14);
    decipher(s3, 14);
    decipher(s4, 78);
    decipher(s5, 18);


}