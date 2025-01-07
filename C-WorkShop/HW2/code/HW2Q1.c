#include <stdio.h>
#include <math.h>

void change(int n, int coins[], float to_change)
{
    int roundc = round(to_change * 100);
    int totalNumOfCoins = 0;

    // for etch coin, check what is the max coin number that fits in to_change
    for(int coin = 0 ; coin < n; coin++){

        // if to_change is larger than the coin value
        if(roundc >= coins[coin]){
            int numOfCoin = roundc/coins[coin];
            printf("%d coins of type %d \n", numOfCoin, coins[coin]);
            roundc = roundc% coins[coin];
            totalNumOfCoins += numOfCoin;

        } else {
            printf("%d coins of type %d\n", 0, coins[coin]);
        }
    }

    if(roundc != 0){
        printf("Exact change is impossible! %d reminded.\n", roundc);
    } else {
        printf("Returned exact change!\n");
    }

    printf("%d coins are needed for change\n", totalNumOfCoins);
}

int main(int args, char **argv)
{
    int arr1[] = {1000, 500, 100, 50, 10};
    int n1 = 5;
    float to_change = 6.80;
    change(n1, arr1, to_change);
    printf("\n");
    
    int arr2[] = {2500, 1700, 1500, 89, 5, 4};
    int n2 = 6;
    to_change = 22.33;
    change(n2, arr2, to_change);
    printf("\n");

    int arr3[] = {1700, 392, 5, 4};
    int n3 = 4;
    change(n3, arr3, to_change);
    return 0;
}

