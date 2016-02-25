# Spring-Board
Spring으로 게시판 만들기

## 개발환경

* eclipse Mars
* tomcat 8.0.30
* jdk 8.0
* maven 3.3.9
* STS(Spring Tool Suite)
* JSDT jQuery
* MySQL 5.6.29

## 참고

http://addio3305.tistory.com/32


***
### MySQL Query
`/* 게시판 테이블 생성 */`

    CREATE TABLE TB_BOARD(
      IDX INT AUTO_INCREMENT PRIMARY KEY COMMENT '인덱스',
      PARENT_IDX INT COMMENT '부모글 인덱스',
      TITLE VARCHAR(100) NOT NULL COMMENT '제목',
      CONTENTS VARCHAR(2000) NOT NULL COMMENT '내용',
      HIT_CNT INT NOT NULL COMMENT '조회수',
      DEL_GB VARCHAR(1) DEFAULT 'N' NOT NULL COMMENT '삭제구분',
      CREA_DTM DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일자',
      CREA_ID VARCHAR(30) NOT NULL COMMENT '생성자 ID'
    )COMMENT = '게시판';
