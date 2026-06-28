SELECT DISTINCT C.name AS carrier
FROM Flights F
JOIN Carriers C ON F.carrier_id = C.cid
WHERE F.origin_city = 'Seattle WA' AND F.dest_city = 'New York NY'
ORDER BY carrier;