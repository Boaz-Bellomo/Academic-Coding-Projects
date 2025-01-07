/*
* bitAnd ‐ x&y using only ~ and |
* Example: bitAnd(6, 5) = 4
* Legal ops: ~ |
* Max ops: 8
* Rating: 1
*/
int bitAnd(int x, int y) {
    return ~((~x)|(~y));
}

/*
* getByte ‐ Extract byte n from word x
* Bytes numbered from 0 (LSB) to 3 (MSB)
* Examples: getByte(0x12345678,1) = 0x56
* Legal ops: ! ~ & ^ | + << >>
* Max ops: 6
* Rating: 2
*/
int getByte(int x, int n) {
    return (x >> (n << 3)) & 0xFF;
}

/*
* logicalShift ‐ shift x to the right by n, using a logical shift
* Can assume that 0 <= n <= 31
* Examples: logicalShift(0x87654321,4) = 0x08765432
* Legal ops: ! ~ & ^ | + << >>
* Max ops: 20
* Rating: 3
*/
int logicalShift(int x, int n) {
    int mask = (1 << (32 - n)) - 1;
    return x >> n & mask;
}
