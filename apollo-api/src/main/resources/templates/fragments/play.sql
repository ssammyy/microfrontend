
 create table prod (ID number(19,0) not null, 
prod_desc varchar2(255 char) not null, 
eff_endt timestamp not null, 
eff_stdt timestamp not null, 
is_approved number(1,0),
prod_nm varchar2(255 char) not null,
ID number(19,0) not null, primary key (ID))





 create table prod_mnfr_rf (ID number(19,0) not null,
  eff_endt timestamp not null,
   eff_stdt timestamp not null,
    mnfr_nm varchar2(255 char) not null, 
    primary key (prod_mnfr_uid))


create sequence DATA_KEBS_USERS_seq start with 1 increment by  1
 


 alter table DAT_KEBS_STANDARD_TRANSACTION add constraint   g9su2ayi8iacifabkt7 foreign key (ID) references DATA_KEBS_USERS
