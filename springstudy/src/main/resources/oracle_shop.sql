create table board(
   num number primary key,
   name varchar2(30),
   pass varchar2(20),
   subject varchar2(100),
   content varchar2(4000),
   file1 varchar2(100),
   regdate date,
   readcnt number(10),
   grp number,
   grplevel number(3),
   grpstep number(5)
)


create table useraccount (
   userid varchar2(10) primary key,
   password varchar2(15),
   username varchar2(20),
   phoneno varchar2(20),
   postcode varchar2(7),
   address varchar2(30),
   email varchar2(50),
   birthday date
);


create table item (
   id number primary key,
   name varchar2(30),
   price number,
   description varchar2(100),
   pictureUrl varchar2(30)
);
insert into item values (1, '레몬', 50,  '레몬에 포함된 구연산은 피로회복에 좋습니다.   비타민C 도 풍부합니다.','lemon.jpg');
insert into item values (2, '오렌지', 100, '비타민C 가 풍부합니다. 맛도 좋습니다.','orange.jpg');
insert into item values (3, '키위', 200,  '비타민C 가 풍부합니다. 다이어트에 좋습니다.','kiui.jpg');
insert into item values (4, '포도', 300,  '폴리페놀을 다량 함유하고 있어 항산화 작용을 합니다.',  'podo.jpg');
insert into item values (5, '딸기', 800,  '비타민C를 다량 함유하고 있습니다.',  'ichigo.jpg');
insert into item values (6, '귤', 1000,  '시네피린을 다량 함유하고 있어 감기예방에 좋습니다.',  'mikan.jpg');
commit;

CREATE TABLE sale (
   saleid number PRIMARY KEY,
   userid varchar2(10) NOT NULL,
   saledate date
);
CREATE TABLE saleitem (
	saleid number REFERENCES sale(saleid),
	seq number ,
	itemid number REFERENCES item(id),
	quantity number ,
	PRIMARY KEY (saleid, seq)             
);

-- create 이후에 외래키 설정 구문
ALTER TABLE saleitem 
 ADD CONSTRAINT saleitem_itemid_fk FOREIGN KEY(itemid)
 REFERENCES item(id) ;