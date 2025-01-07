#include <stdio.h>
#include <strings.h>
#include <ctype.h>
#define TRUE 1
#define FALSE 0
#define BOOL int


BOOL isAnagram(char str1[], char str2[], int n)
{
    // for etch char in str1
    for(int lSrt1 = 0; lSrt1 < n; lSrt1++){
        int numOfRepetitionsStr1 = 0;

        // check how many times the char is in str1
        for (int letter = 0; letter < n ; ++letter) {
            
            // convert letters to lower case
            char lowerL1 = tolower(str1[letter]);
            char lowerL2 = tolower(str1[lSrt1]);
            if(lowerL1 == lowerL2){
                numOfRepetitionsStr1++;
            }
        }

        // check if there are as many chars of the same type in str2
        int numOfRepetitionsStr2 = 0;
        for (int lStr2 = 0; lStr2 < n; ++lStr2) {
            char lowerL1 = tolower(str1[lSrt1]);
            char lowerL2 = tolower(str2[lStr2]);
            if(lowerL1 == lowerL2){
                numOfRepetitionsStr2++;
            }
        }

        if (numOfRepetitionsStr1 != numOfRepetitionsStr2){
            return FALSE;
        }
    }
    return TRUE;
}

int main()
{
    BOOL t;
    char str1[4][17] = {"AA", "ABCDEFG", "ABCDTFG", "This is a string"};
    char str2[4][17] = {"aA", "GFEDCBA", "GFEDCBA", "Tthiiisssnarg   "};
    int size[] = {2, 7, 7, 16};
    for(int i = 0; i < 4; i ++)
    {
    t = isAnagram(str1[i], str2[i], size[i]);
    if (t == TRUE)
        printf("'%s':'%s' are anagram\n", str1[i], str2[i]);
    else
        printf("'%s':'%s' are not anagram\n",  str1[i], str2[i]);
    }
    return 0;
}