--wastes disposal date before waste closure date:
--select w.nuclide_waste_id, w.closure_date, w.closure_user_id, w.disposal_user_id, w.disposal_date, usr.user_id, usr.is_active from nuclide_waste w, nuclide_user usr where closure_date > disposal_date and w.disposal_user_id = usr.user_id;    
update nuclide_waste set disposal_date = TO_DATE('05-JUL-11', 'DD-MON-RR') where nuclide_waste_id = 1497;
update nuclide_waste set disposal_date = TO_DATE('15-APR-11', 'DD-MON-RR') where nuclide_waste_id = 1568;
update nuclide_waste set disposal_date = TO_DATE('23-MAY-11', 'DD-MON-RR') where nuclide_waste_id = 1603;
update nuclide_waste set disposal_date = TO_DATE('18-JUL-17', 'DD-MON-RR') where nuclide_waste_id = 2037;

--tracer usage date after waste closure date
--SELECT u.nuclide_usage_id, b.nuclide_bottle_id, w.nuclide_waste_id, TO_CHAR(u.usage_date, 'DD-MON-YYYY') as usage_date, TO_CHAR(w.closure_date, 'DD-MON-YYYY') as waste_closure_date, s.user_id, s.is_active FROM osiris.nuclide_usage u, osiris.nuclide_bottle b,    osiris.nuclide_user s, osiris.nuclide_waste w WHERE u.nuclide_bottle_id = b.nuclide_bottle_id AND u.user_id = s.user_id AND ( u.liquid_waste_id = w.nuclide_waste_id OR u.solid_waste_id = w.nuclide_waste_id ) AND u.usage_date > w.closure_date;
update nuclide_usage set usage_date = TO_DATE('05-APR-2004', 'DD-MON-RR') where nuclide_usage_id = 974	and liquid_waste_id = 271;
update nuclide_usage set usage_date = TO_DATE('05-APR-2004', 'DD-MON-RR') where nuclide_usage_id = 975	and liquid_waste_id = 271;
update nuclide_usage set usage_date = TO_DATE('17-AUG-2004', 'DD-MON-RR') where nuclide_usage_id = 1482 and liquid_waste_id = 350;
update nuclide_usage set usage_date = TO_DATE('17-AUG-2004', 'DD-MON-RR') where nuclide_usage_id = 1483 and liquid_waste_id = 350;
update nuclide_usage set usage_date = TO_DATE('26-JUL-2005', 'DD-MON-RR') where nuclide_usage_id = 3048 and liquid_waste_id = 560;
update nuclide_usage set usage_date = TO_DATE('26-JUL-2005', 'DD-MON-RR') where nuclide_usage_id = 3049 and liquid_waste_id = 560;
update nuclide_usage set usage_date = TO_DATE('11-APR-2008', 'DD-MON-RR') where nuclide_usage_id = 6391 and solid_waste_id = 1022;
update nuclide_usage set usage_date = TO_DATE('11-APR-2008', 'DD-MON-RR') where nuclide_usage_id = 6392 and solid_waste_id = 1022;
update nuclide_usage set usage_date = TO_DATE('11-APR-2008', 'DD-MON-RR') where nuclide_usage_id = 6393 and solid_waste_id = 1022;
update nuclide_usage set usage_date = TO_DATE('11-APR-2008', 'DD-MON-RR') where nuclide_usage_id = 6390 and solid_waste_id = 1022;
update nuclide_usage set usage_date = TO_DATE('11-APR-2008', 'DD-MON-RR') where nuclide_usage_id = 6402 and solid_waste_id = 1022;
update nuclide_usage set usage_date = TO_DATE('17-SEP-2008', 'DD-MON-RR') where nuclide_usage_id = 6840 and solid_waste_id = 1081;
update nuclide_usage set usage_date = TO_DATE('24-SEP-2008', 'DD-MON-RR') where nuclide_usage_id = 6840 and liquid_waste_id = 1055;
update nuclide_usage set usage_date = TO_DATE('15-DEC-2008', 'DD-MON-RR') where nuclide_usage_id = 7648 and solid_waste_id = 1135;
update nuclide_usage set usage_date = TO_DATE('02-APR-2009', 'DD-MON-RR') where nuclide_usage_id = 7518 and liquid_waste_id = 1088;
update nuclide_usage set usage_date = TO_DATE('09-JUN-2010', 'DD-MON-RR') where nuclide_usage_id = 8880 and solid_waste_id = 1378;
update nuclide_usage set usage_date = TO_DATE('25-JUN-2010', 'DD-MON-RR') where nuclide_usage_id = 8880 and liquid_waste_id = 1377;
update nuclide_usage set usage_date = TO_DATE('04-AUG-2010', 'DD-MON-RR') where nuclide_usage_id = 9139 and liquid_waste_id = 1413;
update nuclide_usage set usage_date = TO_DATE('04-AUG-2010', 'DD-MON-RR') where nuclide_usage_id = 9140 and liquid_waste_id = 1413;
update nuclide_usage set usage_date = TO_DATE('06-AUG-2010', 'DD-MON-RR') where nuclide_usage_id = 9139 and solid_waste_id = 1409;
update nuclide_usage set usage_date = TO_DATE('06-AUG-2010', 'DD-MON-RR') where nuclide_usage_id = 9140 and solid_waste_id = 1409;
update nuclide_usage set usage_date = TO_DATE('02-NOV-2010', 'DD-MON-RR') where nuclide_usage_id = 9409 and liquid_waste_id = 1469;
update nuclide_usage set usage_date = TO_DATE('09-NOV-2010', 'DD-MON-RR') where nuclide_usage_id = 9409 and solid_waste_id = 1453;
update nuclide_usage set usage_date = TO_DATE('11-JAN-2011', 'DD-MON-RR') where nuclide_usage_id = 9581 and liquid_waste_id = 1489;
update nuclide_usage set usage_date = TO_DATE('18-FEB-2011', 'DD-MON-RR') where nuclide_usage_id = 9581 and solid_waste_id = 1505;
update nuclide_usage set usage_date = TO_DATE('14-APR-2011', 'DD-MON-RR') where nuclide_usage_id = 9994 and liquid_waste_id = 1572;
update nuclide_usage set usage_date = TO_DATE('15-APR-2011', 'DD-MON-RR') where nuclide_usage_id = 10051 and liquid_waste_id = 1569;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2012', 'DD-MON-RR') where nuclide_usage_id = 11343 and liquid_waste_id = 1759;
update nuclide_usage set usage_date = TO_DATE('16-APR-2012', 'DD-MON-RR') where nuclide_usage_id = 11457 and liquid_waste_id = 1798;
update nuclide_usage set usage_date = TO_DATE('26-APR-2012', 'DD-MON-RR') where nuclide_usage_id = 11457 and solid_waste_id = 1778;
update nuclide_usage set usage_date = TO_DATE('13-AUG-2014', 'DD-MON-RR') where nuclide_usage_id = 13281 and solid_waste_id = 2138;
update nuclide_usage set usage_date = TO_DATE('09-DEC-2016', 'DD-MON-RR') where nuclide_usage_id = 14637 and solid_waste_id = 2428;
update nuclide_usage set usage_date = TO_DATE('20-DEC-2016', 'DD-MON-RR') where nuclide_usage_id = 14637 and liquid_waste_id = 2431;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14902 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14903 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14901 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14904 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14906 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14907 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14905 and solid_waste_id = 2464;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14902 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14903 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14901 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14904 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14906 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14907 and liquid_waste_id = 2460;
update nuclide_usage set usage_date = TO_DATE('02-MAR-2017', 'DD-MON-RR') where nuclide_usage_id = 14905 and liquid_waste_id = 2460;


--tracer usage date after waste disposal date
--SELECT    u.nuclide_usage_id,    b.nuclide_bottle_id,    w.nuclide_waste_id,    TO_CHAR(u.usage_date, 'DD-MON-YYYY') AS usage_date,    TO_CHAR(w.disposal_date, 'DD-MON-YYYY') AS waste_disposal_date,    TO_CHAR(w.closure_date, 'DD-MON-YYYY') AS waste_closure_date,    s.user_id,    s.is_active FROM    osiris.nuclide_usage u,    osiris.nuclide_bottle b,    osiris.nuclide_user s,    osiris.nuclide_waste w WHERE    u.nuclide_bottle_id = b.nuclide_bottle_id    AND u.user_id = s.user_id    AND ( u.liquid_waste_id = w.nuclide_waste_id          OR u.solid_waste_id = w.nuclide_waste_id )    AND u.usage_date > w.disposal_date;
update nuclide_usage set usage_date = TO_DATE('24-SEP-08', 'DD-MON-RR') where nuclide_usage_id = 6840 and liquid_waste_id = 1055;
update nuclide_usage set usage_date = TO_DATE('15-DEC-08', 'DD-MON-RR') where nuclide_usage_id = 7648 and solid_waste_id = 1135;
update nuclide_usage set usage_date = TO_DATE('15-APR-11', 'DD-MON-RR') where nuclide_usage_id = 10051 and liquid_waste_id = 1569;
update nuclide_usage set usage_date = TO_DATE('9-MAY-11', 'DD-MON-RR') where nuclide_usage_id = 10121 and liquid_waste_id = 1603;
update nuclide_usage set usage_date = TO_DATE('13-JUL-11', 'DD-MON-RR') where nuclide_usage_id = 9581 and liquid_waste_id = 1489;
update nuclide_usage set usage_date = TO_DATE('13-JUL-11', 'DD-MON-RR') where nuclide_usage_id = 9581 and solid_waste_id = 1505;


--Daughter tracer usage date before parent initial activity date
--select u.nuclide_usage_id, b.nuclide_bottle_id, u.new_nuclide_bottle_id, u.usage_date, b.activity_date,  usr.user_id, usr.is_active  FROM nuclide_bottle b, nuclide_usage u, nuclide_user usr WHERE u.nuclide_bottle_id = b.nuclide_bottle_id AND u.usage_date < b.activity_date AND u.new_nuclide_bottle_id IS NOT NULL and u.user_id = usr.user_id;
update nuclide_usage set usage_date = TO_DATE('16-FEB-04', 'DD-MON-RR') where nuclide_usage_id = 632;


--Daughter usage date before parent usage date
--SELECT    u.nuclide_usage_id,    u.usage_date    AS daughter_usage_date,    u2.usage_date   AS parent_usage_date,    u.new_nuclide_bottle_id AS daughter_id,    u.nuclide_bottle_id AS parent_id FROM    nuclide_usage u,    nuclide_usage u2 WHERE    u.nuclide_bottle_id = u2.new_nuclide_bottle_id    AND u.usage_date < u2.usage_date AND u.new_nuclide_bottle_id is not null;
update nuclide_usage set usage_date = TO_DATE('21-NOV-06', 'DD-MON-RR') where nuclide_usage_id = 4799;
update nuclide_usage set usage_date = TO_DATE('13-FEB-18', 'DD-MON-RR') where nuclide_usage_id = 15470;


--Daughter tracer disposal date before parent initial activity date
--select b.nuclide_bottle_id, b.disposal_date ,b2.activity_date FROM nuclide_bottle b, nuclide_usage u,nuclide_bottle b2 WHERE b.nuclide_bottle_id = u.new_nuclide_bottle_id AND b2.nuclide_bottle_id = u.nuclide_bottle_id AND b.disposal_date < b2.activity_date;
update nuclide_bottle set disposal_date = TO_DATE('16-FEB-04', 'DD-MON-RR') where nuclide_bottle_id = 309;
