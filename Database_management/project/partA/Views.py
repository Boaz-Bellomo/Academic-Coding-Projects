VIEWS_DICT = {
    "Q3":
        [
            """
            -- Creating a VIEW to find all investors that traded on every single possible trading day,
            CREATE VIEW InvestingEveryDay AS(
                SELECT ID
                FROM Buying
                GROUP BY ID
                HAVING COUNT(DISTINCT tDate) = (SELECT COUNT(DISTINCT tDate) FROM Stock)
            );
            """,

            """
            -- Creating a VIEW to identify active investors who bought stocks from at least two different companies
            -- on every single possible trading day, using the InvestingEveryDay View
            CREATE VIEW ActiveInvestors AS
            (
                SELECT distinct Buying.ID
                FROM Buying
                WHERE
                    -- in investor list that traded every single possible day
                    Buying.ID IN (SELECT ID FROM InvestingEveryDay)
                    AND
                    -- not in list of investors that traded every single possible day but not from more than 2 companies a day
                    Buying.ID not IN (  SELECT distinct Buying.ID
                                        FROM Buying
                                        WHERE Buying.ID IN (SELECT ID FROM InvestingEveryDay)
                                        group by Buying.ID, tDate
                                        HAVING COUNT(DISTINCT Symbol) <2)
            );
            """,

            """
            --
            -- Creating a VIEW to count the number of buying actions for each active investor
            CREATE VIEW InvestorBuyingActions AS (
                SELECT
                    Buying.ID,
                    COUNT(*) AS BuyingActions
                FROM
                    Buying
                WHERE
                    Buying.ID IN (SELECT ID FROM ActiveInvestors)
                GROUP BY
                Buying.ID
            );
            """,

            """
            --
            -- Creating a VIEW to calculate the total investment in buying stocks for each active investor
            CREATE VIEW InvestorTotalInvestment AS(
                SELECT
                    Buying.ID,
                    ROUND(SUM(Stock.Price * Buying.BQuantity), 3) AS TotalInvestment
                FROM
                    Buying JOIN Stock
                        ON
                    Buying.Symbol = Stock.Symbol AND
                    Buying.tDate = Stock.tDate
                WHERE
                    Buying.ID IN (SELECT ID FROM ActiveInvestors)
                GROUP BY
                Buying.ID
            );
            """,

            """
            --
            -- Creating a VIEW to find the number of actions in each sector for each relevant investor
            CREATE VIEW InvestorActionNumInSector AS(
                SELECT
                    Buying.ID,
                    Company.Sector AS Sector,
                    COUNT(*) AS SectorActions
                FROM
                    Buying JOIN Company
                        ON
                    Buying.Symbol = Company.Symbol
                WHERE
                    Buying.ID IN (SELECT ID FROM ActiveInvestors)
                GROUP BY
                    Buying.ID, Company.Sector
            );
            """,

            """
            --
            -- Creating a VIEW to determine the most active sector for each investor
            CREATE VIEW InvestorMostActiveSector AS
            WITH RankedSectors AS (
                SELECT
                    ID,
                    Sector,
                    COUNT(*) AS SectorActions,
                    RANK() OVER (PARTITION BY ID ORDER BY COUNT(*) DESC, Sector ASC) AS SectorRank
                FROM Buying JOIN Company ON Buying.Symbol = Company.Symbol
                WHERE Buying.ID IN (SELECT ID FROM ActiveInvestors)
                GROUP BY Buying.ID, Sector
            )
            SELECT
                ID,
                Sector AS MostActiveSector
            FROM RankedSectors
            WHERE SectorRank = 1;
            """
        ]
    ,
    "Q4":
        [
        """
        --
        -- Create a VIEW to find "Great Find" companies based on single buying events and price increases
        CREATE VIEW GreatFindCompanies AS
        SELECT DISTINCT
            b.Symbol
        FROM
            Buying b
                JOIN
            (SELECT Symbol, tDate, Price,
                    LEAD(Price) OVER (PARTITION BY Symbol ORDER BY tDate) AS NextDayPrice,
                    LEAD(tDate) OVER (PARTITION BY Symbol ORDER BY tDate) AS NextTradeDay
             FROM Stock) AS StockWithNextDay
        ON
            b.Symbol = StockWithNextDay.Symbol
        WHERE
            (SELECT COUNT(*) FROM Buying b2 WHERE b2.Symbol = b.Symbol) = 1
            AND (StockWithNextDay.NextDayPrice / StockWithNextDay.Price) > 1.02
            AND StockWithNextDay.tDate < (SELECT MAX(tDate) FROM Stock)
            AND b.tDate = StockWithNextDay.tDate;
        """,

        """
        --
        -- Identify singular investors who invested in "Great Find" companies
        CREATE VIEW SingleInvestors AS
        SELECT DISTINCT b.ID
        FROM Buying b
        WHERE EXISTS (
            SELECT 1
            FROM GreatFindCompanies g
            WHERE b.Symbol = g.Symbol
        );
        """,

        """
        --
        -- Focus on California companies founded before 2000
        CREATE VIEW CaliforniaAfter2000 AS
        SELECT DISTINCT c.Symbol
        FROM Company c
        WHERE c.Location = 'California' AND c.Founded < 2000;
        """,

        """
        --
        -- Link buying actions to California companies founded before 2000
        CREATE VIEW California2000Buying AS
        SELECT b.ID, b.Symbol
        FROM Buying b
        JOIN CaliforniaAfter2000 c2k ON b.Symbol = c2k.Symbol;
        """
        ]
}
