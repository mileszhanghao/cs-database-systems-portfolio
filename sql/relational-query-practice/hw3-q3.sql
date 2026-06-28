SELECT origin_city,
       COALESCE(100.0 * COUNT(CASE WHEN actual_time < 90 THEN 1 END) / COUNT(*), 0) AS percentage
FROM Flights
WHERE canceled = 0
GROUP BY origin_city
ORDER BY percentage, origin_city;
