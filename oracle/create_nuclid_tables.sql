rem   create_nuclid_tables.sql
rem
rem   Short Description:   Creates tables for handling radio-labelled compounds
rem
rem   Author:              Thomas Sander
rem   Creation Date:       20. June 2002
rem
rem   Function:
rem   Parameters:
rem   Required Files:
rem
rem   Change History:      
rem


/************************************************************************/
set echo on

/***************************Definitions***********************************/
DEFINE tab_space_table=ACT0_TAB
DEFINE tab_space_index=ACT0_IDX


DROP TABLE nuclide_location;
DROP TABLE nuclide_allowance;
DROP TABLE nuclide_location_type;
DROP TABLE nuclide_disposal_route;
DROP TABLE nuclide_disposal_location;
DROP TABLE nuclide_usage;
DROP TABLE nuclide_waste;
DROP TABLE nuclide_bottle;
DROP TABLE nuclide_user;
DROP TABLE nuclide;

PROMPT Creating TABLE nuclide
CREATE TABLE nuclide (
	nuclide_name			VARCHAR2(16)		NOT NULL
		CONSTRAINT pk_nuclide
		PRIMARY KEY
		USING INDEX TABLESPACE &tab_space_index
		STORAGE (INITIAL 2k),
	half_time				FLOAT			NOT NULL,
	kbq_per_freigrenze		FLOAT			NOT NULL
	)
	TABLESPACE &TAB_SPACE_TABLE
	STORAGE (INITIAL 10k);


PROMPT Creating TABLE nuclide_user
CREATE TABLE nuclide_user (
	user_id					VARCHAR2(8)		NOT NULL
		CONSTRAINT pk_nuclide_user
		PRIMARY KEY
		USING INDEX TABLESPACE &tab_space_index
		STORAGE (INITIAL 2k),
	first_name				VARCHAR2(32)	NOT NULL,
	last_name				VARCHAR2(32)	NOT NULL,
	is_active				CHAR(1)			NOT NULL
	)
	TABLESPACE &TAB_SPACE_TABLE
	STORAGE (INITIAL 10k);


PROMPT Creating TABLE nuclide_bottle
CREATE TABLE nuclide_bottle (
    nuclide_bottle_id		NUMBER(8,0)		NOT NULL
		CONSTRAINT pk_nuclide_bottle
		PRIMARY KEY
		USING INDEX TABLESPACE &tab_space_index
		STORAGE (INITIAL 10k),
    nuclide_name			VARCHAR2(5)		NOT NULL
		CONSTRAINT fk_nuclide_bottle_nuclide
		REFERENCES nuclide,
	substance_name      	VARCHAR2(32)	NOT NULL,
	batch_name      		VARCHAR2(32),
    is_liquid               CHAR(1)         NOT NULL,
    activity				FLOAT			NOT NULL,
    volume					FLOAT			NOT NULL,
	location				VARCHAR2(32)	NOT NULL,
	nuclide_user_id			VARCHAR2(8)		NOT NULL
        CONSTRAINT fk_nuclid_bottle_user
        REFERENCES nuclide_user(user_id),
	activity_date			DATE			NOT NULL,
	disposal_user_id		VARCHAR2(8)
        CONSTRAINT fk_nuclid_bottle_disposal_user
        REFERENCES nuclide_user(user_id),
	disposal_date			DATE
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 200K);


PROMPT Creating TABLE nuclide_waste
CREATE TABLE nuclide_waste (
	nuclide_waste_id		NUMBER(8,0)		NOT NULL
		CONSTRAINT pk_nuclide_waste
		PRIMARY KEY
		USING INDEX TABLESPACE &tab_space_index
		STORAGE (INITIAL 10k),
    nuclide_name			VARCHAR2(5)		NOT NULL
		CONSTRAINT fk_nuclide_waste_nuclide
		REFERENCES nuclide,
    is_liquid               CHAR(1)         NOT NULL,
	location				VARCHAR2(32)	NOT NULL,
	creation_user_id		VARCHAR2(8)     NOT NULL
        CONSTRAINT fk_nuclid_waste_creation_user
        REFERENCES nuclide_user(user_id),
	closure_user_id			VARCHAR2(8)
        CONSTRAINT fk_nuclid_waste_closure_user
        REFERENCES nuclide_user(user_id),
	closure_date			DATE,
	disposal_user_id		VARCHAR2(8)
        CONSTRAINT fk_nuclid_waste_disposal_user
        REFERENCES nuclide_user(user_id),
	disposal_route			VARCHAR2(32),
	disposal_activity		FLOAT,
	disposal_date			DATE
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 400K);


PROMPT Creating TABLE nuclide_usage
CREATE TABLE nuclide_usage (
    nuclide_usage_id		NUMBER(8,0)		NOT NULL
        CONSTRAINT pk_nuclide_usage
        PRIMARY KEY
        USING INDEX TABLESPACE &tab_space_index
        STORAGE (INITIAL 20k),
	volume					FLOAT			NOT NULL,
	bio_lab_journal			VARCHAR2(20)	NOT NULL,
	assay_type				VARCHAR2(32)	NOT NULL,
	usage_date				DATE			NOT NULL,
	user_id					VARCHAR2(8)		NOT NULL
        CONSTRAINT fk_nuclid_usage_user
        REFERENCES nuclide_user(user_id),
	nuclide_bottle_id		NUMBER(8,0)		NOT NULL
		CONSTRAINT fk_nuclide_usage_bottle
		REFERENCES nuclide_bottle(nuclide_bottle_id),
	liquid_waste_percentage	FLOAT			NOT NULL,
	liquid_waste_id			NUMBER(8,0)
		CONSTRAINT fk_nuclide_usage_waste1
		REFERENCES nuclide_waste(nuclide_waste_id),
	solid_waste_percentage	FLOAT			NOT NULL,
	solid_waste_id			NUMBER(8,0)
		CONSTRAINT fk_nuclide_usage_waste2
		REFERENCES nuclide_waste(nuclide_waste_id),
	destination				VARCHAR(32),
	new_nuclide_bottle_id	NUMBER(8,0)
	)
	TABLESPACE &tab_space_table
	STORAGE (INITIAL 800k);


PROMPT Creating TABLE nuclide_disposal_route
CREATE TABLE nuclide_disposal_route (
	disposal_route			VARCHAR2(32)	NOT NULL
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 2K);

PROMPT Creating TABLE nuclide_location_type
CREATE TABLE nuclide_location_type (
	location_type			VARCHAR2(16)	NOT NULL
        CONSTRAINT pk_nuclide_location_type
        PRIMARY KEY
        USING INDEX TABLESPACE &tab_space_index
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 2K);

PROMPT Creating TABLE nuclide_allowance
CREATE TABLE nuclide_allowance (
	location_type			VARCHAR2(32)	NOT NULL
		CONSTRAINT fk_nuclide_allowance_loc_type
		REFERENCES nuclide_location_type,
    nuclide_name			VARCHAR2(5)		NOT NULL
		CONSTRAINT fk_nuclide_allowance_nuclide
		REFERENCES nuclide,
	max_amount				FLOAT			NOT NULL	
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 4K);

PROMPT Creating TABLE nuclide_location
CREATE TABLE nuclide_location (
	location				VARCHAR2(32)	NOT NULL,
	location_type			VARCHAR2(16)	NOT NULL
		CONSTRAINT fk_nuclide_location_location_type
		REFERENCES nuclide_location_type
	)
	TABLESPACE &TAB_SPACE_TABLE
    STORAGE (INITIAL 2K);

DROP ROLE nuclide_user;
CREATE ROLE nuclide_user;
GRANT SELECT ON nuclide TO nuclide_user;
GRANT SELECT ON nuclide_user TO nuclide_user;
GRANT SELECT,INSERT,UPDATE ON nuclide_bottle TO nuclide_user;
GRANT SELECT,INSERT,UPDATE ON nuclide_waste TO nuclide_user;
GRANT SELECT,INSERT ON nuclide_usage TO nuclide_user;
GRANT SELECT ON nuclide_disposal_route TO nuclide_user;
GRANT SELECT ON nuclide_location_type TO nuclide_user;
GRANT SELECT ON nuclide_allowance TO nuclide_user;
GRANT SELECT ON nuclide_location TO nuclide_user;

GRANT SELECT ON nuclide TO nuclide_reader;
GRANT SELECT ON nuclide_user TO nuclide_reader;
GRANT SELECT ON nuclide_bottle TO nuclide_reader;
GRANT SELECT ON nuclide_waste TO nuclide_reader;
GRANT SELECT ON nuclide_usage TO nuclide_reader;
GRANT SELECT ON nuclide_disposal_route TO nuclide_reader;
GRANT SELECT ON nuclide_location_type TO nuclide_reader;
GRANT SELECT ON nuclide_allowance TO nuclide_reader;
GRANT SELECT ON nuclide_location TO nuclide_reader;
