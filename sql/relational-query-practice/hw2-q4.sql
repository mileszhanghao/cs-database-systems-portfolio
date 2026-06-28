SELECT DISTINCT c.name AS name
FROM FLIGHTS AS f
JOIN CARRIERS AS c ON f.carrier_id = c.cid
GROUP BY c.name, f.month_id, f.day_of_month
HAVING COUNT(*) > 1000;
