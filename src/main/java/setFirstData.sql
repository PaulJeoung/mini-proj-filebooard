CREATE DATABASE sqlplus;
USE sqlplus;

CREATE TABLE mvcboard (
	idx INT primary key,
    name VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    postdate TIMESTAMP DEFAULT NOW(),
    ofile VARCHAR(300),
    sfile VARCHAR(30),
    downcount INT(5) DEFAULT 0 NOT NULL,
    pass VARCHAR(50) NOT NULL,
    visitcount INT DEFAULT 0 NOT NULL
);

INSERT INTO sqlplus.mvcboard (idx, name, title, content, pass) VALUES (1, '김유신', '자료실 제목 1입니다. ', '내용', '1234');
INSERT INTO sqlplus.mvcboard (idx, name, title, content, pass) VALUES (2, '장보고', '자료실 제목 2입니다. ', '내용', '1234');
INSERT INTO sqlplus.mvcboard (idx, name, title, content, pass) VALUES (3, '이순신', '자료실 제목 3입니다. ', '내용', '1234');
INSERT INTO sqlplus.mvcboard (idx, name, title, content, pass) VALUES (4, '강감찬', '자료실 제목 4입니다. ', '내용', '1234');
INSERT INTO sqlplus.mvcboard (idx, name, title, content, pass) VALUES (5, '대조영', '자료실 제목 5입니다. ', '내용', '1234');

SELECT * FROM sqlplus.mvcboard;