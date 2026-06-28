SELECT DISTINCT name AS carrier
FROM Carriers
WHERE cid IN (
    SELECT carrier_id
    FROM Flights
    WHERE origin_city = 'Seattle WA' AND dest_city = 'New York NY'
)
ORDER BY carrier;