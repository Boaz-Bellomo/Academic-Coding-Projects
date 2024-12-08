from django.shortcuts import redirect
from django.contrib import messages
from .models import Transactions
from .models import Buying
from .models import Investor


# Create your views here.
def index(request):
    return render(request, 'index.html')


def dictfetchall(cursor):
    "Return all rows from a cursor as a dict"
    columns = [col[0] for col in cursor.description]
    return [dict(zip(columns, row)) for row in cursor.fetchall()]


from django.shortcuts import render
from django.db import connection

def QueryResults(request):

    # SQL query for diversified investors
    # This query selects the name of each investor and the total amount they spent on buying stocks.
    # It only includes investors who have bought stocks from at least 6 different sectors on any recorded day.
    sql_query_diversified_investors = """
        SELECT 
            i.Name AS InvestorName,  -- Selects the investor's name
            ROUND(SUM(b.BQuantity * st.Price), 3) AS TotalSpent  -- Calculates the total spent by the investor
        FROM 
            Buying AS b
                INNER JOIN 
            Investor AS i ON b.ID = i.ID  -- Joins the Buying and Investor tables on investor ID
                INNER JOIN 
            Stock AS st ON b.Symbol = st.Symbol AND b.tDate = st.tDate  -- Joins with the Stock table to get stock prices
                INNER JOIN 
            Company AS c ON b.Symbol = c.Symbol  -- Joins with the Company table
        WHERE 
            b.ID IN (
                SELECT DISTINCT b.ID
                FROM Buying AS b
                        INNER JOIN 
                    Company AS c 
                        ON 
                    b.Symbol = c.Symbol  -- Inner query to filter investors based on sector count
                GROUP BY b.ID, b.tDate
                HAVING COUNT(DISTINCT c.Sector) >= 6  -- Having clause to check the number of sectors
            )
        GROUP BY i.Name  -- Groups results by investor name
        ORDER BY TotalSpent DESC  -- Orders the results by the total spent in descending order
        """

    # Query for popular companies (Query 2)
    # This query identifies popular companies that have been traded on every trading day documented in the database
    # and have no other company in the same sector meeting this criterion.
    # It then determines the investor with the highest number of shares for each such company.
    sql_query_popular_companies = """
    WITH TradingDays AS (
        SELECT DISTINCT tDate
        FROM Buying
        -- Gets all the distinct trading days to compare against company trading days
    ),
    CompanyTradingDays AS (
        SELECT 
            b.Symbol, 
            COUNT(DISTINCT b.tDate) AS TradingDayCount
        FROM Buying b
        GROUP BY b.Symbol
        -- Counts the number of distinct trading days for each company
    ),
    PopularCompanies AS (
        SELECT 
            c.Symbol
        FROM Company c
                INNER JOIN 
            CompanyTradingDays ctd 
                ON 
            c.Symbol = ctd.Symbol
        WHERE 
            ctd.TradingDayCount = (SELECT COUNT(*) FROM TradingDays)
        AND NOT EXISTS (
            SELECT 1
            FROM Company c2
            INNER JOIN CompanyTradingDays ctd2 ON c2.Symbol = ctd2.Symbol
            WHERE 
                c2.Sector = c.Sector AND 
                c2.Symbol != c.Symbol AND 
                ctd2.TradingDayCount = (SELECT COUNT(*) FROM TradingDays)
            -- Ensures no other company in the same sector has been traded on every trading day
        )
        -- Identifies companies that have been traded every trading day and are unique in their sector
    ),
    TopInvestors AS (
        SELECT 
            b.Symbol, 
            i.Name, 
            b.ID,
            SUM(b.BQuantity) AS TotalQuantity,
            RANK() OVER (PARTITION BY b.Symbol ORDER BY SUM(b.BQuantity) DESC) AS rn
        FROM Buying b
                INNER JOIN 
            Investor i 
                ON 
            b.ID = i.ID
        GROUP BY b.Symbol, i.Name, b.ID
        -- Ranks investors for each company based on the total quantity of shares
    )
    SELECT 
        pc.Symbol, 
        ti.Name, 
        ti.TotalQuantity
    FROM PopularCompanies pc
            INNER JOIN 
        TopInvestors ti 
            ON 
        pc.Symbol = ti.Symbol AND
        ti.rn = 1
    -- Selects the top investor (rank = 1) for each popular company
    ORDER BY pc.Symbol ASC;
    -- Orders the result set by company symbol in ascending order
    """

    # Query for profitable companies (Query 3)
    # This query identifies "profitable companies" whose stock price on the last recorded day is at least 6% higher
    # than on the first recorded day. It then calculates the number of investors who bought shares on the first
    # trading day for each profitable company.
    sql_query_profitable_companies = """
    WITH FirstLastPrices AS (
        SELECT 
            Symbol,
            MIN(CASE WHEN tDate = FirstTradingDay THEN Price ELSE NULL END) AS FirstDayPrice,
            MAX(CASE WHEN tDate = LastTradingDay THEN Price ELSE NULL END) AS LastDayPrice
        FROM 
            Stock
                CROSS JOIN (
            SELECT 
                MIN(tDate) AS FirstTradingDay, 
                MAX(tDate) AS LastTradingDay 
            FROM Stock) AS FL
        GROUP BY Symbol
        -- This CTE calculates the first and last recorded stock price for each company
    ),
    ProfitableCompanies AS (
        SELECT 
            Symbol
        FROM 
            FirstLastPrices
        WHERE 
            (LastDayPrice / FirstDayPrice - 1) * 100 > 6
        -- This CTE filters for companies where the last price is at least 6% higher than the first
    ),
    InvestorFirstDayBuys AS (
        SELECT 
            b.Symbol, 
            COUNT(DISTINCT b.ID) AS NumberOfInvestors
        FROM 
            Buying AS b
        WHERE 
            b.tDate = (SELECT MIN(tDate) FROM Stock)
        GROUP BY b.Symbol
        -- This CTE counts the number of investors that bought shares on the first trading day for each company
    )
    SELECT 
        pc.Symbol,
        COALESCE(ifdb.NumberOfInvestors, 0) AS NumberOfInvestors
    FROM 
        ProfitableCompanies pc
            LEFT JOIN 
        InvestorFirstDayBuys ifdb ON pc.Symbol = ifdb.Symbol
    -- This final SELECT joins the CTEs to get the number of investors for each profitable company
    ORDER BY 
        pc.Symbol ASC;
    -- Orders the result set by the company symbol in ascending order
    """



    with connection.cursor() as cursor:
        # Execute and fetch Query 1 results
        cursor.execute(sql_query_diversified_investors)
        query1_results = cursor.fetchall()

        # Execute and fetch Query 2 results
        cursor.execute(sql_query_popular_companies)
        query2_results = cursor.fetchall()

        # Execute and fetch Query 3 results
        cursor.execute(sql_query_profitable_companies)
        query3_results = cursor.fetchall()

    # Pass all results to the template
    return render(request, 'QueryResults.html', {
        'query1_results': query1_results,
        'query2_results': query2_results,
        'query3_results': query3_results,
    })




def add_transaction(request):
    # Handles form submission
    if request.method == 'POST':
        # Extracts form data
        investor_id = request.POST.get('investor_id')
        transaction_amount = float(request.POST.get('transaction_amount'))

        # Checks if the provided investor ID exists in the database
        investor_exists = Investor.objects.filter(id=investor_id).exists()

        if not investor_exists:
            # Sends error message if investor ID is not found
            messages.error(request, 'Investor ID does not exist.')
        else:
            # Checks if a transaction has already been made by the same investor today
            sql_query2 = """
               SELECT TOP 1 tDate
               FROM Stock
               ORDER BY tDate DESC
               """
            with connection.cursor() as cursor:
                cursor.execute(sql_query2)
                # Assuming there will always be at least one record
                latest_date = cursor.fetchone()[0]  # fetchone() returns a tuple, and we're interested in the first item

            today = latest_date
            if Transactions.objects.filter(id=investor_id, tdate=today).exists():
                messages.error(request, 'You have already made a transaction today.')
            else:
                try:
                    # Inserts the new transaction into the database
                    with connection.cursor() as cursor:
                        cursor.execute("INSERT INTO Transactions (id, tamount, tdate) VALUES (%s, %s, %s)",
                                       [investor_id, transaction_amount, today])
                    # Updates the investor's balance. Ensure you have appropriate logic for balance updates
                    investor = Investor.objects.get(id=investor_id)
                    investor.amount += transaction_amount
                    investor.save()
                    # Success message after adding the transaction
                    messages.success(request, 'Transaction added successfully.')
                except Exception as e:
                    # Handles any exceptions during the process
                    messages.error(request, f"Error when adding transaction: {e}")
        # Redirects back to the page to display the form and last 10 transactions again
        return redirect('add_transaction')

    # Fetches the last 10 transactions for GET requests or following POST request handling
    sql_query = """
        SELECT TOP 10 tDate, ID, TAmount
        FROM Transactions
        ORDER BY tDate DESC, ID DESC;
    """
    with connection.cursor() as cursor:
        cursor.execute(sql_query)
        transactions = dictfetchall(cursor)

    # Renders the page with the transactions data for display
    return render(request, 'add_transaction.html', {'transactions': transactions})






def BuyStocks(request):
    # return the last 10 stock buys
    sql_query1 = """
                    SELECT TOP 10 tDate, ID, Symbol, BQuantity
                    FROM Buying
                    ORDER BY tDate DESC, ID DESC, Symbol ASC;
                    """
    with connection.cursor() as cursor:
        cursor.execute(sql_query1)
        last10Buys = dictfetchall(cursor)

    if request.method == 'POST' and request.POST:
        ID = request.POST.get('ID')
        company = request.POST.get('company')
        quantity = request.POST.get('quantity')

        # make sure ID and company exists
        investorNotExist = True
        sql_query2 = """
                        SELECT ID
                        FROM Investor
                        """
        with connection.cursor() as cursor:
            cursor.execute(sql_query2)
            investorList = dictfetchall(cursor)

        investorIDs = [investor['ID'] for investor in investorList]
        if int(ID) in investorIDs:
            investorNotExist = False

        # get last trading day
        sql_query4 = """
                        SELECT TOP 1 tDate
                        FROM Stock
                        ORDER BY tDate DESC
                        """
        with connection.cursor() as cursor:
            cursor.execute(sql_query4)
            tDateRow = dictfetchall(cursor)
            tDate = tDateRow[0]['tDate']

        companyNotExist = True
        alreadyBoughtFlag = False
        notEnoughAmount = False
        price = 0  # Initialize price variable

        sql_query3 = """
                        SELECT Symbol
                        FROM Company 
                        """
        with connection.cursor() as cursor:
            cursor.execute(sql_query3)
            companyList = dictfetchall(cursor)

        companySymbols = [company['Symbol'] for company in companyList]
        if company in companySymbols:
            companyNotExist = False

            # get buying price
            sql_query5 = """
                                       SELECT Price
                                       FROM Stock
                                       WHERE tDate = %s AND Symbol = %s 
                                       """
            with connection.cursor() as cursor:
                cursor.execute(sql_query5, [tDate, company])
                priceRow = dictfetchall(cursor)
                if priceRow:
                    price = priceRow[0]['Price']  # Assign price if it exists

            if not investorNotExist:
                # check if investor made already a purchase today
                sql_query7 = """
                                            SELECT ID
                                            FROM Buying
                                            WHERE tDate = %s AND Symbol = %s 
                                            """
                with connection.cursor() as cursor:
                    cursor.execute(sql_query7, [tDate, company])
                    buyerListRow = dictfetchall(cursor)
                    if buyerListRow:
                        buyerList = buyerListRow[0]['ID']

                if buyerListRow and int(ID) in [buyerList]:
                    alreadyBoughtFlag = True
                else:
                    # check if price is not larger than available amount for the investor
                    sql_query6 = """
                                            SELECT Amount
                                            FROM Investor
                                            WHERE ID = %s
                                            """
                    with connection.cursor() as cursor:
                        cursor.execute(sql_query6, [ID])
                        availableAmountRow = dictfetchall(cursor)
                        availableAmount = availableAmountRow[0]['Amount']

                    if availableAmount < price * int(quantity):
                        notEnoughAmount = True

        # print error if needed
        if notEnoughAmount or investorNotExist or companyNotExist or alreadyBoughtFlag:
            context = {
                "sql_res": last10Buys,
                "amountFlag": notEnoughAmount,
                "investorExistFlag": investorNotExist,
                "companyExistFlag": companyNotExist,
                "alreadyBoughtFlag": alreadyBoughtFlag
            }

            return render(request, 'BuyStocks.html', context)
        else:
            # subtract price from investor Amount
            # Updates the investor's balance. Ensure you have appropriate logic for balance updates
            investor = Investor.objects.get(id=ID)
            availableAmount = availableAmount - price
            investor.amount = availableAmount
            investor.save()  # Save the updated investor object

            # update purchase in Buying
            with connection.cursor() as cursor:
                cursor.execute("INSERT INTO Buying (tDate, ID, Symbol, BQuantity) VALUES (%s, %s, %s, %s)",
                               [tDate, ID, company, quantity])

            return redirect('BuyStocks')

    return render(request, 'BuyStocks.html', {"sql_res": last10Buys})
