SELECT c.name, ROUND((SUM(f.canceled) * 100.0 / COUNT(*)), 1) AS cancel_percentage
FROM FLIGHTS f
JOIN CARRIERS c ON f.carrier_id = c.cid
WHERE f.origin_city = 'Seattle WA'
GROUP BY c.name
HAVING ROUND((SUM(f.canceled) * 100.0 / COUNT(*)), 1) > 0.5
ORDER BY cancel_percentage ASC;
