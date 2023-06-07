create table cfg_sidebar_main
(
    id           NUMBER PRIMARY KEY,
    role_id      NUMBER,
    status       NUMBER(2, 0),
    path         VARCHAR2(350 CHAR) null,
    title        VARCHAR2(350 CHAR) null,
    type         VARCHAR2(350 CHAR) null,
    icon_type    VARCHAR2(350 CHAR) null,
    collapse     VARCHAR2(350 CHAR) null,
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                    NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                    NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;


create sequence cfg_sidebar_main_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_sidebar_main_trg
    before
        insert
    on cfg_sidebar_main
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_sidebar_main_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/


create table cfg_sidebar_children
(
    id           NUMBER PRIMARY KEY,
    role_id      NUMBER,
    main_id      number references cfg_sidebar_main (id) not null,
    status       NUMBER(2, 0),
    path         VARCHAR2(350 CHAR)                      null,
    title        VARCHAR2(350 CHAR)                      null,
    ab           VARCHAR2(350 CHAR)                      null,
    var_field_1  VARCHAR2(350 CHAR),
    var_field_2  VARCHAR2(350 CHAR),
    var_field_3  VARCHAR2(350 CHAR),
    var_field_4  VARCHAR2(350 CHAR),
    var_field_5  VARCHAR2(350 CHAR),
    var_field_6  VARCHAR2(350 CHAR),
    var_field_7  VARCHAR2(350 CHAR),
    var_field_8  VARCHAR2(350 CHAR),
    var_field_9  VARCHAR2(350 CHAR),
    var_field_10 VARCHAR2(350 CHAR),
    created_by   VARCHAR2(100 CHAR)          DEFAULT 'admin'
                                                         NOT NULL ENABLE,
    created_on   TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate
                                                         NOT NULL ENABLE,
    modified_by  VARCHAR2(100 CHAR)          DEFAULT 'admin',
    modified_on  TIMESTAMP(6) WITH TIME ZONE DEFAULT sysdate,
    delete_by    VARCHAR2(100 CHAR)          DEFAULT 'admin',
    deleted_on   TIMESTAMP(6) WITH TIME ZONE
)
    TABLESPACE qaimssdb_data;


create sequence cfg_sidebar_children_seq minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 cache 20 noorder nocycle;

create trigger cfg_sidebar_children_trg
    before
        insert
    on cfg_sidebar_children
    for each row
begin
    if inserting then
        if :new.id is null then
            select cfg_sidebar_children_seq.nextval
            into :new.id
            from dual;

        end if;

    end if;
end;
/

select *
from CFG_SIDEBAR_MAIN
order by id;

select *
from CFG_SIDEBAR_CHILDREN
order by id;


select *
from CFG_USER_ROLES_ASSIGNMENTS
where USER_ID in (select id from DAT_KEBS_USERS where USER_NAME = 'Ian_Kiprono')
;

-- ID|NAME
-- 806|LIST_COMPANY
-- 807|MODIFY_COMPANY
-- 3|PERMIT_APPLICATION
-- 4|PERMIT_RENEWAL
-- 143|PVOC_APPLICATION_READ

commit;

select *
from CFG_SIDEBAR_MAIN
where role_id in (0, 806, 87, 3, 4);



select id, NAME
from CFG_USER_PRIVILEGES
where ID in (
    select PRIVILEGE_ID
    from CFG_ROLES_PRIVILEGES
    where ROLES_ID in (3, 774)
)
;