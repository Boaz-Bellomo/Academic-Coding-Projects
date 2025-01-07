#include <stdio.h>

void handle_passengers(int bus_num, int age[], int assist[], int length)
{
    printf("\n### Welcome to bus line %d! Have a nice ride! ###\n\n", bus_num);

    // for etch passenger at the bus
    for (int passenger = 0; passenger < length; passenger++){

        // check if requested help
        if (assist[passenger] == 1) {

            // if he is over 80, help
            if (age[passenger] >= 80){
                printf("Passenger %d is %d years old, allow me to help you.\n",
                       (passenger + 1) , age[passenger]);
            } else { // if not, don't
                printf("I'm sorry! %d is not old enough to receive assistant.\n", age[passenger]);
            }

        }

        // if passenger is less than 8, let him ride for free
        if (age[passenger] <= 8){
            printf("Welcome passenger %d! You ride free of charge!\n", (passenger + 1));
        }
    }
}

void main()
{
    // the following code will run a few simple tests of your implementation
    // you're encourged to write more scenarios to test your code,
    // but submit the main function unchanged
    int age[] = {24, 32, 80, 90, 8, 9},
            assist[] = {1, 0, 0, 1, 0, 1},
            length = 6,
            bus_num = 19,
            age1[] = {1, 2, 54, 89},
            assist1[] = {0, 1, 0, 1},
            length1 = 4,
            bus_num1 = 8745,
            age2[] = {8, 90},
            assist2[] = {1, 1},
            length2 = 2,
            bus_num2 = 17,
            age3[] = {20, 30, 1, 42, 102, 95, 8, 7, 9, 27},
            assist3[] = {0, 1, 0, 0, 0, 1, 0, 1, 1, 0},
            length3 = 10,
            bus_num3 = 1,
            age4[] = {3, 3, 82, 42, 32},
            assist4[] = {0, 1, 1, 1, 0},
            length4 = 5,
            bus_num4 = 100000001;

    handle_passengers(bus_num, age, assist, length);
    handle_passengers(bus_num1, age1, assist1, length1);
    handle_passengers(bus_num2, age2, assist2, length2);
    handle_passengers(bus_num3, age3, assist3, length3);
    handle_passengers(bus_num4, age4, assist4, length4);
}
