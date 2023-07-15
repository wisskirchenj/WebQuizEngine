drop table if exists quiz_answer;
drop table if exists quiz_options;
drop table if exists quiz_completion;
drop table if exists quiz;
drop sequence if exists quiz_sequence;

create sequence quiz_sequence minvalue 0 start with 0 increment by 50;
create table quiz (id bigint not null, text varchar(255), title varchar(255), username varchar(255), primary key (id));
create table quiz_answer (quiz_id bigint not null, answer integer);
create table quiz_options (quiz_id bigint not null, options varchar(255));
create table quiz_completion (completion_id bigserial not null, completed_at timestamp(6), username varchar(255), id bigint, primary key (completion_id));

alter table quiz_answer add constraint FKoxi2td1x8cc3y4a0vlsg2hfnc foreign key (quiz_id) references quiz;
alter table quiz_options add constraint FKsx28j7orq6asg17veq9nblhw8 foreign key (quiz_id) references quiz;
alter table quiz_completion add constraint FK8qcqvt5lwgafvof8xv33xx0og foreign key (id) references quiz;