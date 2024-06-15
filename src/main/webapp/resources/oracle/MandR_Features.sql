DEFINE tab_space_table=ACT0_TAB
DEFINE tab_space_index=ACT0_IDX

--DROP TABLE "OSIRIS"."NUCLIDE_LOCATION_ALLOWANCE";
DROP TABLE "OSIRIS"."NUCLIDE_LOCATION";

CREATE TABLE "OSIRIS"."NUCLIDE_LOCATION"
   (
    "ID" NUMBER(8,0) NOT NULL
	CONSTRAINT pk_nuclide_location
	PRIMARY KEY
	USING INDEX TABLESPACE &tab_space_index
	STORAGE (INITIAL 10k),    
	"LOCATION" VARCHAR2(32 BYTE), 
	"LOCATION_TYPE" VARCHAR2(16 BYTE),
    "IS_ENABLE" CHAR(1 BYTE)
   ) TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 200K);
    
GRANT SELECT, INSERT, UPDATE, DELETE ON OSIRIS.NUCLIDE_LOCATION TO osiris_writer;    
GRANT SELECT ON OSIRIS.NUCLIDE_LOCATION TO osiris_reader;    

--DROP SEQUENCE OSIRIS.NUCLIDE_LOCATION_ID_SEQ;
CREATE SEQUENCE OSIRIS.NUCLIDE_LOCATION_ID_SEQ MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;
GRANT SELECT ON OSIRIS.NUCLIDE_LOCATION_ID_SEQ to osiris_writer;

--select OSIRIS.NUCLIDE_LOCATION_SEQ.nextval from dual;
--ALTER SEQUENCE OSIRIS.NUCLIDE_LOCATION_ID_SEQ RESTART START WITH 1;

delete from OSIRIS.NUCLIDE_LOCATION;

Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (1,'C-Lab Biology H89.O3.K08.1','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval, 'DMPK H91.O4.E08','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.E10','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'Storage DMPK H91.O4.K16.2','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'C-Lab Biology H89.O2.K07.1','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.K30','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'C-Lab Biology H91.O3.K15','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'C-Lab Biology H91.O4.K15.1','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'C-Lab PPD H91.O4.K16.1','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.K09','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'Toxicology H91.O5.W07','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.E09.1','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.K26','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK H91.O4.K31','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'Toxicology H89.O4.W08','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'Waste room H91.U2.N03','clab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'DMPK BSL2 H91.O4.K25','lab','Y');
Insert into OSIRIS.NUCLIDE_LOCATION (ID,LOCATION,LOCATION_TYPE,IS_ENABLE) values (NUCLIDE_LOCATION_ID_SEQ.nextval,'H91.O4.W01','lab','Y');

ALTER TABLE "OSIRIS"."NUCLIDE_LOCATION" ADD CONSTRAINT "FK_LOCATION_LOCATION_TYPE" FOREIGN KEY ("LOCATION_TYPE")
 REFERENCES "OSIRIS"."NUCLIDE_LOCATION_TYPE" ("LOCATION_TYPE") ENABLE;


DROP TABLE OSIRIS.NUCLIDE_ALLOWANCE;

CREATE TABLE "OSIRIS"."NUCLIDE_ALLOWANCE"
   (
    "ID" NUMBER(8,0) NOT NULL
	CONSTRAINT pk_nuclide_allowance
	PRIMARY KEY
	USING INDEX TABLESPACE &tab_space_index
	STORAGE (INITIAL 10k),  
    "NUCLIDE_NAME"	VARCHAR2(5 BYTE),
    "MAX_AMOUNT"	FLOAT
   ) TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 200K);

GRANT SELECT, INSERT, UPDATE, DELETE ON OSIRIS.NUCLIDE_ALLOWANCE TO osiris_writer;    
GRANT SELECT ON OSIRIS.NUCLIDE_ALLOWANCE TO osiris_reader;

--DROP SEQUENCE OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ;
CREATE SEQUENCE OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;
GRANT SELECT ON OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ to osiris_writer;

ALTER TABLE "OSIRIS"."NUCLIDE_ALLOWANCE" ADD CONSTRAINT "FK_NUCLIDE_NAME" FOREIGN KEY ("NUCLIDE_NAME")
 REFERENCES "OSIRIS"."NUCLIDE" ("NUCLIDE_NAME") ENABLE; 

delete from OSIRIS.NUCLIDE_ALLOWANCE;
 
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'3H',4000000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'14C',900000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'32P',10000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'33P',40000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'35S',20000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'125I',20000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'3H',100000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'14C',9000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'32P',2000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'33P',4000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'35S',20000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'125I',700);
--NEW VALUES
--Storage DMPK H91.O4.K16.2
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'14C', 2500000);
--Waste room H91.U2.N03
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'3H', 24000000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'14C', 8000000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'32P', 60000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'33P', 240000);
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'35S', 120000);	
Insert into OSIRIS.NUCLIDE_ALLOWANCE (ID,NUCLIDE_NAME,MAX_AMOUNT) values (OSIRIS.NUCLIDE_ALLOWANCE_ID_SEQ.nextval,'125I', 120000);

commit;

CREATE TABLE NUCLIDE_LOCATION_ALLOWANCE (
    "ID"   NUMBER(8, 0) NOT NULL
        CONSTRAINT PK_LOCATION_ALLOWANCE PRIMARY KEY
            USING INDEX TABLESPACE &tab_space_index
                STORAGE ( INITIAL 10k ),
    "LOCATION_ID"	     	NUMBER(8, 0) NOT NULL ENABLE,
    "ALLOWANCE_ID"	     	NUMBER(8, 0) NOT NULL ENABLE
)
TABLESPACE &TAB_SPACE_TABLE
    STORAGE ( INITIAL 10k );
    
ALTER TABLE "OSIRIS"."NUCLIDE_LOCATION_ALLOWANCE" ADD CONSTRAINT "FK_LOCATION_ID" FOREIGN KEY ("LOCATION_ID")
 REFERENCES "OSIRIS"."NUCLIDE_LOCATION" ("ID") ENABLE;    
 
 ALTER TABLE "OSIRIS"."NUCLIDE_LOCATION_ALLOWANCE" ADD CONSTRAINT "FK_ALLOWANCE_ID" FOREIGN KEY ("ALLOWANCE_ID")
 REFERENCES "OSIRIS"."NUCLIDE_ALLOWANCE" ("ID") ENABLE;  
 
GRANT SELECT, INSERT, UPDATE, DELETE ON OSIRIS.NUCLIDE_LOCATION_ALLOWANCE TO osiris_writer;  
GRANT SELECT ON OSIRIS.NUCLIDE_LOCATION_ALLOWANCE TO osiris_reader;
 
--DROP SEQUENCE OSIRIS.NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ;  
CREATE SEQUENCE OSIRIS.NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;
GRANT SELECT ON OSIRIS.NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ to osiris_writer;

delete from OSIRIS.nuclide_location_allowance;

--C-Lab Biology H89.O3.K08.1
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,1);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,2);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,4);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,1,6);

--DMPK H91.O4.E08
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,2,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,2,8);

--DMPK H91.O4.E10
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,3,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,3,8);

--Storage DMPK H91.O4.K16.2
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,1);
--32P
--Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,2);
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,4);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,13);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,4,6);

--C-Lab Biology H89.O2.K07.1
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,1);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,2);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,4);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,5,6);

--DMPK H91.O4.K30
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,6,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,6,8);

--C-Lab Biology H91.O3.K15
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,1);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,2);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,4);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,7,6);

--C-Lab Biology H91.O4.K15.1
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,1);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,2);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,4);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,8,6);

--C-Lab PPD H91.O4.K16.1
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,1);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,2);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,3);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,4);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,5);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,9,6);

--DMPK H91.O4.K09
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,10,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,10,8);

--Toxicology H91.O5.W07
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,7);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,8);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,9);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,10);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,11);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,11,12);

--DMPK H91.O4.E09.1
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,12,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,12,8);

--DMPK H91.O4.K26
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,13,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,13,8);

--DMPK H91.O4.K31
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,14,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,14,8);

--Toxicology H89.O4.W08
--125I
--Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,20);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,8);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,9);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,10);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,11);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,15,12);

--Waste room H91.U2.N03
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,16);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,14);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,15);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,18);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,17);
--125I
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,16,19);

--DMPK BSL2 H91.O4.K25
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,17,7);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,17,8);

--H91.O4.W01
--125I !!!!!
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,7);
--32P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,8);
--3H
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,9);
--14C
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,10);
--35S
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,11);
--33P
Insert into OSIRIS.nuclide_location_allowance (ID,location_id,allowance_id) values (NUCLIDE_LOCATION_ALLOWANCE_ID_SEQ.nextval,18,12);


--
update OSIRIS.NUCLIDE_LOCATION set is_enable = 'N' where location = 'C-Lab Biology H91.O4.K15.1';
update OSIRIS.NUCLIDE_LOCATION set is_enable = 'N' where location = 'C-Lab Biology H91.O3.K15';
--?? to verify
update OSIRIS.NUCLIDE_LOCATION set is_enable = 'N' where location = 'C-Lab PPD H91.O4.K16.1';

commit;