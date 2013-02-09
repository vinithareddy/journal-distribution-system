SELECT t3.subscriberNumber, t2.subscriptionID, t2.copies, t2.endyear, t3.subtype
  FROM subscription t1, subscriptiondetails t2, subscriber t3
 WHERE     t1.subscriberID = t3.id
       AND t3.subtype >= 8
       AND t3.subtype <= 10
       AND t1.id = t2.subscriptionID
       AND (t2.journalGroupID = 1 or t2.journalGroupID = 12 or t2.journalGroupID = 13)
       AND t2.endYear >= 2013
       -- and t3.subscriberNumber = '1108'
ORDER BY CONVERT(t3.subscriberNumber, UNSIGNED);


SELECT t3.subscriberNumber, t2.subscriptionID, t2.copies, t2.endyear, t3.subtype
  FROM subscription t1, subscriptiondetails t2, subscriber t3
 WHERE     t1.subscriberID = t3.id
       AND t3.subtype = 11
       AND t1.id = t2.subscriptionID
       AND (t2.journalGroupID = 1 or t2.journalGroupID = 12 or t2.journalGroupID = 13)
       AND t2.endYear >= 2013
       -- and t3.subscriberNumber = '1108'
ORDER BY CONVERT(t3.subscriberNumber, UNSIGNED);