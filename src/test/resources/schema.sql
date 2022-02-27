CREATE TABLE man (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(64) default '默认' COMMENT '名称',
  sex int default 0,
  age int default 0,
  id_no varchar(32) default '',
  created datetime ,
  updated datetime ,
  is_del tinyint default 0,
  PRIMARY KEY (id)
);
INSERT INTO man(id, name, age, id_no, created, updated, is_del) VALUES
(1, 'Alice', 18, 'admin','2021-02-20 21:34:13', '2021-02-20 21:34:16', 0),
(2, 'Boy', 6, 'boy','2021-02-20 21:34:13', '2021-02-20 21:34:16', 0),
(3, 'Che', 6, 'boy','2021-02-20 21:34:13', '2021-02-20 21:34:16', 0),
(4, 'Dan', 6, 'boy','2021-02-20 21:34:13', '2021-02-20 21:34:16', 0),
(5, 'El', 6, 'boy','2021-02-20 21:34:13', '2021-02-20 21:34:16', 0)
;
