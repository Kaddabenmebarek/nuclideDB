DEFINE tab_space_table=ACT0_TAB
DEFINE tab_space_index=ACT0_IDX

--REVINFO--
CREATE TABLE revinfo
(
    REV      NUMBER(10,0) NOT NULL ENABLE,
    revtstmp NUMBER(19,0),
    PRIMARY KEY ( rev ) USING INDEX TABLESPACE &tab_space_index
	STORAGE (INITIAL 10k)
);
CREATE SEQUENCE hibernate_sequence MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE;
grant select on hibernate_sequence to osiris_writer;

--NUCLIDE_BOTTLE_AUD--
CREATE TABLE nuclide_bottle_aud (
    nuclide_bottle_id		NUMBER(8,0)		NOT NULL,
    nuclide_name			VARCHAR2(5)		NOT NULL,
	substance_name      	VARCHAR2(32)	NOT NULL,
	batch_name      		VARCHAR2(32),
    is_liquid               CHAR(1)         NOT NULL,
    activity				FLOAT			NOT NULL,
    volume					FLOAT			NOT NULL,
	location				VARCHAR2(32)	NOT NULL,
	nuclide_user_id			VARCHAR2(8)		NOT NULL,
	activity_date			DATE			NOT NULL,
	disposal_user_id		VARCHAR2(8),
	disposal_date			DATE,
	REV 					NUMBER(10,0) NOT NULL ENABLE,
	REVTYPE 				NUMBER(3,0),
	FOREIGN KEY ("REV") REFERENCES "REVINFO" ("REV") ENABLE);
	
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
    execute immediate 'CREATE SEQUENCE nuclide_bottle_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_bottle_id || ' NOCYCLE CACHE 20 NOORDER';
    execute immediate 'grant select on nuclide_bottle_seq_id to osiris_writer';
  end if;
end;

	
--NUCLIDE_WASTE_AUD--
CREATE TABLE nuclide_waste_aud (
	nuclide_waste_id		NUMBER(8,0)		NOT NULL,
    nuclide_name			VARCHAR2(5)		NOT NULL,
    is_liquid               CHAR(1)         NOT NULL,
	location				VARCHAR2(32)	NOT NULL,
	creation_user_id		VARCHAR2(8)     NOT NULL,
	closure_user_id			VARCHAR2(8),
	closure_date			DATE,
	disposal_user_id		VARCHAR2(8),
	disposal_route			VARCHAR2(32),
	disposal_activity		FLOAT,
	disposal_date			DATE,
	REV 					NUMBER(10,0) NOT NULL ENABLE,
	REVTYPE 				NUMBER(3,0),
	FOREIGN KEY ("REV") REFERENCES "REVINFO" ("REV") ENABLE);	

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
    execute immediate 'CREATE SEQUENCE nuclide_waste_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_waste_id || ' NOCYCLE CACHE 20 NOORDER';
    execute immediate 'grant select on nuclide_waste_seq_id to osiris_writer';
  end if;
end;

	
--NUCLIDE_USAGE_AUD--	
CREATE TABLE nuclide_usage_aud (
    nuclide_usage_id		NUMBER(8,0)		NOT NULL,
	volume					FLOAT			NOT NULL,
	bio_lab_journal			VARCHAR2(20)	NOT NULL,
	assay_type				VARCHAR2(32)	NOT NULL,
	usage_date				DATE			NOT NULL,
	user_id					VARCHAR2(8)		NOT NULL,
	nuclide_bottle_id		NUMBER(8,0)		NOT NULL,
	liquid_waste_percentage	FLOAT			NOT NULL,
	liquid_waste_id			NUMBER(8,0),
	solid_waste_percentage	FLOAT			NOT NULL,
	solid_waste_id			NUMBER(8,0),
	destination				VARCHAR(32),
	new_nuclide_bottle_id	NUMBER(8,0),
	REV 					NUMBER(10,0) NOT NULL ENABLE,
	REVTYPE 				NUMBER(3,0),
	FOREIGN KEY ("REV") REFERENCES "REVINFO" ("REV") ENABLE);
	
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
    execute immediate 'CREATE SEQUENCE nuclide_usage_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_usage_id || ' NOCYCLE CACHE 20 NOORDER';
    execute immediate 'grant select on nuclide_usage_seq_id to osiris_writer';
  end if;
end;	
	
--NUCLIDE_ATTACHED_AUD--	
CREATE TABLE nuclide_attached_aud (
    nuclide_attached_id		NUMBER(8,0)		NOT NULL,
	file_name				VARCHAR2(100)	NOT NULL,
	file_type				VARCHAR2(10)	NOT NULL,
    file_path				VARCHAR2(300)	NOT NULL,
    file_fullpath			VARCHAR2(400)	NOT NULL,    
	file_date				DATE			NOT NULL,
	user_id					VARCHAR2(8),
	nuclide_bottle_id		NUMBER(8,0),
	REV 					NUMBER(10,0) NOT NULL ENABLE,
	REVTYPE 				NUMBER(3,0),
	FOREIGN KEY ("REV") REFERENCES "REVINFO" ("REV") ENABLE);
	
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
    execute immediate 'CREATE SEQUENCE nuclide_attached_seq_id MINVALUE 1 NOMAXVALUE INCREMENT BY 1 START WITH ' || max_attached_id || ' NOCYCLE CACHE 20 NOORDER';
    execute immediate 'grant select on nuclide_attached_seq_id to osiris_writer';
  end if;
end;	
	
--NUCLIDE_USER_AUD--	
CREATE TABLE nuclide_user_aud (
	user_id					VARCHAR2(8)		NOT NULL,
	first_name				VARCHAR2(32)	NOT NULL,
	last_name				VARCHAR2(32)	NOT NULL,
	is_active				CHAR(1)			NOT NULL,
	ROLE 					VARCHAR2(32 BYTE),
	REV 					NUMBER(10,0) NOT NULL ENABLE,
	REVTYPE 				NUMBER(3,0),
	FOREIGN KEY ("REV") REFERENCES "REVINFO" ("REV") ENABLE);	