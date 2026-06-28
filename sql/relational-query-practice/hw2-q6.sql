SELECT c.name AS carrier,
       MAX(f.price) AS max_price
FROM FLIGHTS AS f
JOIN CARRIERS AS c ON f.carrier_id = c.cid
WHERE (f.origin_city = 'Seattle WA' AND f.dest_city = 'New York NY')
   OR (f.origin_city = 'New York NY' AND f.dest_city = 'Seattle WA')
GROUP BY c.name;
