SELECT origin_city, dest_city, actual_time AS time
FROM Flights AS F
WHERE actual_time = (
    SELECT MAX(actual_time)
    FROM Flights
    WHERE origin_city = F.origin_city
)
GROUP BY origin_city, dest_city, actual_time
ORDER BY origin_city, dest_city;
