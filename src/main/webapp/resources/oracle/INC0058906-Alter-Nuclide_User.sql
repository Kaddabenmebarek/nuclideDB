
alter table nuclide_user add ROLE VARCHAR2(32 BYTE);
Insert into OSIRIS.NUCLIDE_USER (USER_ID,FIRST_NAME,LAST_NAME,IS_ACTIVE,ROLE) values ('BUERGROL','Rolf','B�rgi','Y',null);
Insert into OSIRIS.NUCLIDE_USER (USER_ID,FIRST_NAME,LAST_NAME,IS_ACTIVE,ROLE) values ('MANKOKA1','Karim','Mankour','Y',null);
update nuclide_user set role = 'admin' where user_id in ('ENGRO1', 'WICKIMI1', 'BORTOLC', 'BENMEKA1', 'MANKOKA1');
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'EKAMBAN';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'FARINEH1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'HOFMANS';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'KLOPFEN';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'LAMAZEM';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MEIERS';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'PANDINE';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'WISCHKA1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'BIRKERM';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'BRANDJE1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'FABREFR1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'HOERNEJ';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'HOLDEMA1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MARRIEJ';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MAWETJ';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MEYERS';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MOLLEAN1';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MORRISK';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'MURPHYMA';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'RITZD';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'SANDERT';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'SEELANS';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'SEGRESJ';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'STRASSD';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'THENOZL';
update nuclide_user set IS_ACTIVE = 'N' where user_id = 'WINISTS';
update nuclide_user set IS_ACTIVE = 'Y' where user_id = 'BRUYERT';
update nuclide_user set IS_ACTIVE = 'Y' where user_id = 'BUTSCHB';
update nuclide_user set IS_ACTIVE = 'Y' where user_id = 'LUEDIUR1';
update nuclide_user set IS_ACTIVE = 'Y' where user_id = 'MIRAVTO1';
update nuclide_user set LAST_NAME = 'Grieder' where FIRST_NAME = 'Ursula';
update nuclide_user set FIRST_NAME = 'Valentina' where LAST_NAME = 'Marchesin';
update nuclide_user set user_id = 'BUERGRO1' where user_id = 'BUERGROL';
--update nuclide_user set user_id = 'FARINHE1' where user_id = 'FARINEH1';
update nuclide_user set user_id = 'MAWETJ' where last_name = 'Mawet';

commit;