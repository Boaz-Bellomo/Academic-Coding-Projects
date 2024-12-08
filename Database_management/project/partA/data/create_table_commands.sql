

CREATE TABLE Company(
    Symbol          VARCHAR(40)  PRIMARY KEY,
    Sector          VARCHAR(40),
    Location        VARCHAR(40),
    Founded         INTEGER
);


CREATE TABLE Stock(
    Symbol          VARCHAR(40),
    tDate           DATE,
    Price           FLOAT,
    PRIMARY KEY (Symbol, tDate),
    FOREIGN KEY (Symbol) REFERENCES Company ON DELETE CASCADE
);



CREATE TABLE Buying(
	ID              INTEGER,
    tDate           DATE,
    Symbol          VARCHAR(40),
    BQuantity       INTEGER,
    PRIMARY KEY (tDate, ID,Symbol),
    FOREIGN KEY (Symbol, tDate) REFERENCES Stock ON DELETE CASCADE,
);


