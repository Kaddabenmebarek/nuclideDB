/*update nuclide_usage set new_nuclide_bottle_id = 4006 where new_nuclide_bottle_id = 4020;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4006,'14C','ACT-1002-4391E',1074.85,500,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','1 mM EtOH');
update nuclide_usage set nuclide_bottle_id = 4006 where nuclide_bottle_id = 4020;
delete from nuclide_bottle where nuclide_bottle_id = 4020;

update nuclide_usage set new_nuclide_bottle_id = 4007 where new_nuclide_bottle_id = 4040;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4007,'14C','ACT-1002-4391E',2149.7,500,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','2 mM');
update nuclide_usage set nuclide_bottle_id = 4007 where nuclide_bottle_id = 4040;
delete from nuclide_bottle where nuclide_bottle_id = 4040;

update nuclide_usage set new_nuclide_bottle_id = 4008 where new_nuclide_bottle_id = 4060;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4008,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM');
delete from nuclide_bottle where nuclide_bottle_id = 4060;

update nuclide_usage set new_nuclide_bottle_id = 4009 where new_nuclide_bottle_id = 4061;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4009,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM');
delete from nuclide_bottle where nuclide_bottle_id = 4061;

update nuclide_usage set new_nuclide_bottle_id = 4010 where new_nuclide_bottle_id = 4080;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4010,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM in 50% EtOH');
delete from nuclide_bottle where nuclide_bottle_id = 4080;

update nuclide_usage set new_nuclide_bottle_id = 4011 where new_nuclide_bottle_id = 4081;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4011,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µMin 10% CH3CN');
delete from nuclide_bottle where nuclide_bottle_id = 4081;*/

--SWICH BACK
update nuclide_usage set new_nuclide_bottle_id = 4020 where new_nuclide_bottle_id = 4006;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4020,'14C','ACT-1002-4391E',1074.85,500,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','1 mM EtOH');
update nuclide_usage set nuclide_bottle_id = 4020 where nuclide_bottle_id = 4006;
delete from nuclide_bottle where nuclide_bottle_id = 4006;

update nuclide_usage set new_nuclide_bottle_id = 4040 where new_nuclide_bottle_id = 4007;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4040,'14C','ACT-1002-4391E',2149.7,500,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','2 mM');
update nuclide_usage set nuclide_bottle_id = 4040 where nuclide_bottle_id = 4007;
delete from nuclide_bottle where nuclide_bottle_id = 4007;

update nuclide_usage set new_nuclide_bottle_id = 4060 where new_nuclide_bottle_id = 4008;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4060,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM');
delete from nuclide_bottle where nuclide_bottle_id = 4008;

update nuclide_usage set new_nuclide_bottle_id = 4061 where new_nuclide_bottle_id = 4009;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4061,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM');
delete from nuclide_bottle where nuclide_bottle_id = 4009;

update nuclide_usage set new_nuclide_bottle_id = 40080 where new_nuclide_bottle_id = 4010;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4080,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µM in 50% EtOH');
delete from nuclide_bottle where nuclide_bottle_id = 4010;

update nuclide_usage set new_nuclide_bottle_id = 4081 where new_nuclide_bottle_id = 4011;
Insert into OSIRIS.NUCLIDE_BOTTLE (NUCLIDE_BOTTLE_ID,NUCLIDE_NAME,SUBSTANCE_NAME,ACTIVITY,VOLUME,LOCATION,NUCLIDE_USER_ID,ACTIVITY_DATE,DISPOSAL_USER_ID,DISPOSAL_DATE,IS_LIQUID,BATCH_NAME) values (4081,'14C','ACT-1002-4391E',21.497,1000,'DMPK H91.O4.E08','SCHAEUM',to_date('23-OCT-19','DD-MON-RR'),null,null,'Y','10 µMin 10% CH3CN');
delete from nuclide_bottle where nuclide_bottle_id = 4011;


--REMOVE CACHE PRE-ALLOCATION 20
drop SEQUENCE nuclide_bottle_seq_id;
declare
 max_bottle_id number;
begin
  select MAX(nuclide_bottle_id)  + 1 into max_bottle_id from nuclide_bottle;
  If max_bottle_id > 0 then
    begin
            execute immediate 'DROP SEQUENCE nuclide_bottle_seq_id';
      exception when others then
        null;
    end;
    execute immediate 'CREATE SEQUENCE nuclide_bottle_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_bottle_id || ' NOCYCLE NOCACHE NOORDER';
    execute immediate 'grant select on nuclide_bottle_seq_id to osiris_writer';
  end if;
end;


drop SEQUENCE nuclide_waste_seq_id;
declare
 max_waste_id number;
begin
  select MAX(nuclide_waste_id)  + 1 into max_waste_id from nuclide_waste;
  If max_waste_id > 0 then
    begin
            execute immediate 'DROP SEQUENCE nuclide_waste_seq_id';
      exception when others then
        null;
    end;
    execute immediate 'CREATE SEQUENCE nuclide_waste_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_waste_id || ' NOCYCLE NOCACHE NOORDER';
    execute immediate 'grant select on nuclide_waste_seq_id to osiris_writer';
  end if;
end;


drop SEQUENCE nuclide_usage_seq_id;
declare
 max_usage_id number;
begin
  select MAX(nuclide_usage_id)  + 1 into max_usage_id from nuclide_usage;
  If max_usage_id > 0 then
    begin
            execute immediate 'DROP SEQUENCE nuclide_usage_seq_id';
      exception when others then
        null;
    end;
    execute immediate 'CREATE SEQUENCE nuclide_usage_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_usage_id || ' NOCYCLE NOCACHE NOORDER';
    execute immediate 'grant select on nuclide_usage_seq_id to osiris_writer';
  end if;
end;	
	
	
drop SEQUENCE nuclide_attached_seq_id;
declare
 max_attached_id number;
begin
  select MAX(nuclide_attached_id)  + 1 into max_attached_id from nuclide_attached;
  If max_attached_id > 0 then
    begin
            execute immediate 'DROP SEQUENCE nuclide_attached_seq_id';
      exception when others then
        null;
    end;
    execute immediate 'CREATE SEQUENCE nuclide_attached_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_attached_id || ' NOCYCLE NOCACHE NOORDER';
    execute immediate 'grant select on nuclide_attached_seq_id to osiris_writer';
  end if;
end;	
	