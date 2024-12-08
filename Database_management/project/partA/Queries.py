QUERY_ANSWERS = {
    "Q3":
        """
        SELECT
            InvestorBuyingActions.ID,
            InvestorBuyingActions.BuyingActions,
            InvestorTotalInvestment.TotalInvestment,
            InvestorMostActiveSector.MostActiveSector
        FROM
            InvestorBuyingActions
                JOIN
            InvestorTotalInvestment ON InvestorBuyingActions.ID = InvestorTotalInvestment.ID
                JOIN
            InvestorMostActiveSector  ON InvestorBuyingActions.ID = InvestorMostActiveSector.ID
        ORDER BY
            InvestorBuyingActions.BuyingActions DESC, InvestorMostActiveSector.MostActiveSector ASC;
        """,

    "Q4":
        """
        -- Query to return the number of actions by singular investors for California companies founded before 2000
        SELECT 
            si.ID, 
            COUNT(*) AS numOfActions
        FROM 
            SingleInvestors si
                JOIN 
            California2000Buying c2b ON si.ID = c2b.ID
                JOIN 
            Company C ON C.Symbol = c2b.Symbol
        WHERE 
            C.Location = 'California' AND C.Founded < 2000
        GROUP BY 
            si.ID;
        """
}
