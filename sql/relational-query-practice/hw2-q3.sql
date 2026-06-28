SELECT TOP 1 day_of_week, AVG(arrival_delay) AS delay
FROM FLIGHTS
JOIN WEEKDAYS ON FLIGHTS.day_of_week_id = WEEKDAYS.did
GROUP BY day_of_week
ORDER BY delay DESC;