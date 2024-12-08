QUERY_ANSWERS = {
    "Q2":
        """"
            SELECT u.cName                    AS Country,
                   COUNT(DISTINCT i.authorID) AS UsersReceivedShare
            FROM Users u
                     JOIN Interactions i ON u.ID = i.authorID
            WHERE u.cName IN (
                -- Identifies countries with at least 30 unique users
                SELECT cName
                FROM Users
                GROUP BY cName
                HAVING COUNT(DISTINCT ID) >= 30)
              AND i.iType = 'share'
            GROUP BY u.cName
            -- Added u.cName ASC for alphabetical sorting in case of a tie
            ORDER BY UsersReceivedShare DESC, u.cName ASC;
        """
    ,
    "Q3":
        """
            SELECT u.ID   AS UserID,
                   u.name AS UserName
            FROM Users u
            WHERE NOT EXISTS (
                -- Filters out users who have had interactions from non-followers
                SELECT 1
                FROM Interactions i
                         LEFT JOIN Follows f ON i.authorID = f.ID2 AND i.uID = f.ID1
                WHERE i.authorID = u.ID
                  AND f.ID1 IS NULL)
            ORDER BY u.ID;
        """
    ,
	"Q4":
        """
            SELECT u.ID                                                             AS UserID,
                   u.name                                                           AS UserName,
                   (SELECT COUNT(DISTINCT f.ID2) FROM Follows f WHERE f.ID1 = u.ID) AS NumberOfFollowers
            FROM Users u
            WHERE
              -- Confirms the user has a follower from each country
                (SELECT COUNT(DISTINCT cName) FROM Users) =
                (SELECT COUNT(DISTINCT u2.cName)
                 FROM Follows f
                          JOIN Users u2 ON f.ID2 = u2.ID
                 WHERE f.ID1 = u.ID)
              AND u.ID IN (
                -- Ensures at least 65 distinct international activities
                SELECT i.authorID
                FROM Interactions i
                         JOIN Users u2 ON i.uID = u2.ID
                WHERE u.cName <> u2.cName
                GROUP BY i.authorID
                HAVING COUNT(DISTINCT CONCAT(i.authorID, '_', i.cNum, '_', i.iType)) >= 65)
            GROUP BY u.ID, u.name
            ORDER BY NumberOfFollowers DESC;
        """
}
