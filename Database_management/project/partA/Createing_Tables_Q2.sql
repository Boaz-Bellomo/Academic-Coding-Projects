CREATE TABLE Investors (
    InvestorID VARCHAR(9) PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Email VARCHAR(255) UNIQUE,
    RegistrationDate DATE NOT NULL,

    CHECK (InvestorID LIKE '^[1-9][0-9]{8}$'),
    CHECK (year(DateOfBirth) < 2006),
    CHECK (Email LIKE '%_@_%')
);


CREATE TABLE PremiumInvestors (
    PremiumInvestorID VARCHAR(9) PRIMARY KEY,
    FinancialGoals TEXT,
    FOREIGN KEY (PremiumInvestorID) REFERENCES Investors(InvestorID) ON DELETE CASCADE,
);


CREATE TABLE Employees (
    EmployeeID VARCHAR(9) PRIMARY KEY,
    FOREIGN KEY (EmployeeID) REFERENCES PremiumInvestors(PremiumInvestorID) ON DELETE CASCADE
);


CREATE TABLE BeginnerInvestors (
    BeginnerInvestorID VARCHAR(9) PRIMARY KEY,
    GuidedByID VARCHAR(9) NOT NULL,
    FOREIGN KEY (BeginnerInvestorID) REFERENCES Investors(InvestorID) ON DELETE CASCADE,
    FOREIGN KEY (GuidedByID) REFERENCES Employees(EmployeeID), --ON DELETE CASCADE
    -- -> all ID is stored in Investor, no need to ON DELETE CASCADE twice

    UNIQUE (BeginnerInvestorID, GuidedByID)
);


CREATE TABLE Companies (
    Symbol VARCHAR(10) PRIMARY KEY,
    Sector VARCHAR(255),
    Founded INTEGER,
    Location VARCHAR(255)
);


CREATE TABLE Rivalry (
    Company1Symbol VARCHAR(10),
    Company2Symbol VARCHAR(10),
    Cause TEXT NOT NULL,
    FOREIGN KEY (Company1Symbol) REFERENCES Companies(Symbol) ON DELETE CASCADE,
    FOREIGN KEY (Company2Symbol) REFERENCES Companies(Symbol), --ON DELETE CASCADE
    -- -> all Symbol is stored in Companies, no need to ON DELETE CASCADE twice

    PRIMARY KEY (Company1Symbol, Company2Symbol),
    CHECK (Company1Symbol < Company2Symbol)
);


CREATE TABLE RivalryDocumentation (
    RivalryCompany1Symbol VARCHAR(10),
    RivalryCompany2Symbol VARCHAR(10),
    EmployeeID VARCHAR(9) UNIQUE,
    SummeryReportContent TEXT NOT NULL,
    FOREIGN KEY (RivalryCompany1Symbol, RivalryCompany2Symbol)
        REFERENCES Rivalry(Company1Symbol, Company2Symbol) ON DELETE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID) ON DELETE CASCADE,

    PRIMARY KEY (EmployeeID, RivalryCompany1Symbol, RivalryCompany2Symbol),
    UNIQUE (RivalryCompany1Symbol, RivalryCompany2Symbol)
);

-- Create TradingAccounts Table
-- Each investor has at least one trading account.
CREATE TABLE TradingAccounts (
    AccountID VARCHAR(10) PRIMARY KEY,
    InvestorID VARCHAR(9) NOT NULL,
    AvailableFunds DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (InvestorID) REFERENCES Investors(InvestorID) ON DELETE CASCADE,

    UNIQUE (AccountID, InvestorID)
);


CREATE TABLE Transactions (
    Date DATE,
    AccountID VARCHAR(10) NOT NULL,
    TransactionDate DATE NOT NULL,
    Amount DECIMAL(15,2) NOT NULL,
    Checker VARCHAR(9),
    IsLegal BIT,
    FOREIGN KEY (AccountID) REFERENCES TradingAccounts(AccountID) ON DELETE CASCADE,
    FOREIGN KEY (Checker) REFERENCES Employees(EmployeeID) ON DELETE NO ACTION,

    PRIMARY KEY (Date, AccountID),
    CHECK (Amount >= 1000)
);


CREATE TABLE Stocks (
    Symbol VARCHAR(10),
    Date DATE,
    Value DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (Symbol) REFERENCES Companies(Symbol) ON DELETE CASCADE,

    PRIMARY KEY (Symbol, Date)
);



CREATE TABLE StockPurchases (
    AccountID VARCHAR(10),
    Amount INTEGER NOT NULL,
    PurchaseDate DATE,
    Symbol VARCHAR(10),
    Value DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (AccountID) REFERENCES TradingAccounts(AccountID) ON DELETE CASCADE,
    FOREIGN KEY (Symbol, PurchaseDate) REFERENCES Stocks(Symbol, Date) ON DELETE CASCADE,

    PRIMARY KEY (AccountID, PurchaseDate, Symbol),
);


DROP TABLE IF EXISTS StockPurchases;
DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS TradingAccounts;
DROP TABLE IF EXISTS RivalryDocumentation;
DROP TABLE IF EXISTS Rivalry;
DROP TABLE IF EXISTS Stocks;
DROP TABLE IF EXISTS Companies;
DROP TABLE IF EXISTS BeginnerInvestors;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS PremiumInvestors;
DROP TABLE IF EXISTS Investors;