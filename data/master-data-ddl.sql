select *
from CFG_KEBS_DESIGNATIONS
-- where id in (1,67)
where DESIGNATION_NAME like '%Testing Engineer%'
order by ID;

select *
from CFG_KEBS_DIRECTORATES
order by ID;

select *
from CFG_KEBS_DEPARTMENTS
-- where DIRECTORATE_ID = 4
order by ID;


select *
from CFG_STANDARDS_CATEGORY
-- where DIRECTORATE_ID = 4
order by ID;



select *
from CFG_KEBS_DESIGNATIONS des
         left outer join CFG_KEBS_DIRECTORATES dir on des.DIRECTORATE_ID = dir.ID
where des.ID = 1;

rollback;

select *
from CFG_KEBS_DIVISIONS
order by ID;

select *
from CFG_KEBS_TOWNS
order by ID;

select *
from CFG_KEBS_COUNTIES;

select *
from CFG_KEBS_REGIONS
order by ID;

alter table CFG_KEBS_REGIONS
    add DESCRIPTIONS VARCHAR2(3599 CHAR);

select *
from CFG_KEBS_TOWNS
order by ID;

--Drop region_id foreign key
-- alter table CFG_KEBS_TOWNS drop constraint SYS_C0044647 /

select *
from CFG_KEBS_SUB_SECTIONS_LEVEL1
order by ID
;


select *
from CFG_USER_ROLES
order by ID;

commit;


select *
from DAT_KEBS_USER_PROFILES;

select *
from CFG_KEBS_SUB_REGIONS
order by ID;


select r.*, sr.SUB_REGION
from CFG_KEBS_SUB_REGIONS sr
         left outer join (
    select r.ID REGION_ID, r.REGION, c.id COUNTY_ID, c.COUNTY, t.id TOWN_id, t.TOWN
    from CFG_KEBS_REGIONS r
             left outer join CFG_KEBS_COUNTIES c on c.REGION_ID = r.ID
             left outer join CFG_KEBS_TOWNS t on t.COUNTY_ID = c.ID
) r on sr.REGION_ID = r.REGION_ID
;


select r.ID as REGION_ID, r.REGION, c.id as COUNTY_ID, c.COUNTY, t.id as TOWN_id, t.TOWN
from CFG_KEBS_REGIONS r
         left outer join CFG_KEBS_COUNTIES c on c.REGION_ID = r.ID
         left outer join CFG_KEBS_TOWNS t on t.COUNTY_ID = c.ID
order by REGION, COUNTY, TOWN
;

select *
from CFG_KEBS_DEPARTMENTS;

select sr.id, SUB_REGION, r.REGION, r.id as REGION_ID
from CFG_KEBS_SUB_REGIONS sr,
     CFG_KEBS_REGIONS r
where sr.REGION_ID = r.ID
  and sr.STATUS = 1
order by ID
;

select de.id, de.DESIGNATION_NAME, di.DIRECTORATE
from CFG_KEBS_DESIGNATIONS de,
     CFG_KEBS_DIRECTORATES di
where de.DIRECTORATE_ID = di.ID
  and de.STATUS = 1
order by de.ID;



select ROWNUM row_id, dep.*, dir.DIRECTORATE
from CFG_KEBS_DIRECTORATES dir
         left outer join (select div.*, dep.DEPARTMENT, dep.DIRECTORATE_ID
                          from CFG_KEBS_DEPARTMENTS dep
                                   left outer join (select sec.*, d.DIVISION, d.DEPARTMENT_ID
                                                    from CFG_KEBS_DIVISIONS d
                                                             left outer join (select sub.*, s.SECTION, s.DIVISION_ID
                                                                              from CFG_KEBS_SECTIONS s
                                                                                       left outer join (select nvl(l2.id, ROWNUM) l2_id,
                                                                                                               nvl(l1.id, ROWNUM) l1_id,
                                                                                                               l2.SUB_SECTION     l2_SUB_sub_SECTION,
                                                                                                               l1.SUB_SECTION     l1_SUB_sub_SECTION,
                                                                                                               l1.SECTION_ID
                                                                                                        from CFG_KEBS_SUB_SECTIONS_LEVEL2 l2
                                                                                                                 left outer join CFG_KEBS_SUB_SECTIONS_LEVEL1 l1 on l2.SUB_SECTIONS_LEVEL1_ID = l1.ID
                                                                                                        where l2.STATUS = 1
                                                                                                        order by l2_id) sub
                                                                                                       on s.id = sub.SECTION_ID
                                                                              where s.STATUS = 1) sec
                                                                             on d.ID = sec.DIVISION_ID) div
                                                   on div.DEPARTMENT_ID = dep.ID
                          where dep.STATUS = 1) dep on dir.ID = dep.DIRECTORATE_ID
where dir.STATUS = 1
  and DIRECTORATE_ID = 6
;


select *
from DAT_KEBS_USERS
where USER_NAME like '%vmuriuki%'
   or EMAIL like '%vincentmuriuki42@gmail.com%'
   or FIRST_NAME like '%Vincent%'
   OR LAST_NAME like '%Muriuki%';


select *
from DAT_KEBS_USERS usersentit0_
where usersentit0_.USER_NAME like '%vmuriuki%'
  AND usersentit0_.LAST_NAME like null
  AND usersentit0_.FIRST_NAME like null
  AND usersentit0_.EMAIL like null
--    or 1 = 1

;

select *
from DAT_KEBS_USER_PROFILES
where USER_ID = 54
order by id
;

select *
from DAT_KEBS_USERS
where id in (243, 228);

select *
from CFG_KEBS_DIRECTORATES
order by ID;

select *
from CFG_KEBS_DEPARTMENTS
where id = 7;

select *
from CFG_KEBS_DIVISIONS
where DEPARTMENT_ID = 7;

select *
from DAT_KEBS_IMPORTER_CONTACT_DETAILS;




