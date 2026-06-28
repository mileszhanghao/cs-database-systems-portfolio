SELECT DISTINCT F2.dest_city AS city
FROM Flights F1
JOIN Flights F2 ON F1.dest_city = F2.origin_city
WHERE F1.origin_city = 'Seattle WA' AND F2.dest_city <> 'Seattle WA'
  AND NOT EXISTS (
    SELECT 1
    FROM Flights F3
    WHERE F3.origin_city = 'Seattle WA' AND F3.dest_city = F2.dest_city
  )
ORDER BY city;