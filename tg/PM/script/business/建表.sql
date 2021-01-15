create table tg_interfacelog
(
  pk_log    CHAR(20) not null,
  srcsystem VARCHAR2(50),
  dessystem VARCHAR2(50),
  method    VARCHAR2(200),
  srcparm   CLOB,
  result    CHAR(1),
  msg       CLOB,
  exedate   CHAR(19),
  operator  VARCHAR2(50),
  pk_relation VARCHAR2(100),
  dr        INTEGER default 0,
  ts        CHAR(19) default to_char(sysdate,'yyyy-MM-dd hh24:mi:ss')
);
