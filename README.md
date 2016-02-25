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
    /* 게시판 테이블 생성 */
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

    /* 게시판 데이터 삽입 */
    INSERT INTO TB_BOARD
        (IDX,TITLE, CONTENTS, HIT_CNT, DEL_GB, CREA_DTM, CREA_ID)
    VALUES(1, 'HAPPY','행복!',0,'N', now(), 'Admin');


    /* DB 인코딩*/
    ALTER DATABASE board DEFAULT CHARACTER SET utf8;
  
    
    /* 첨부파일 테이블 생성 */ 
    CREATE TABLE TB_FILE
    (
        IDX INT AUTO_INCREMENT,
        BOARD_IDX INT NOT NULL,
        ORIGINAL_FILE_NAME VARCHAR(260) NOT NULL,
        STORED_FILE_NAME VARCHAR(36) NOT NULL,
        FILE_SIZE INT,
        CREA_DTM  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
        CREA_ID   VARCHAR(30) NOT NULL,
        DEL_GB    VARCHAR(1) DEFAULT 'N' NOT NULL,
        PRIMARY KEY (IDX)
    );
    
    /* insert procedure(데이터 자동 삽입) */
    DELIMITER $$

    CREATE PROCEDURE my()
    BEGIN
        DECLARE i INT DEFAULT 1;
        WHILE (i < 500) DO
            INSERT INTO TB_BOARD(TITLE, CONTENTS, HIT_CNT, DEL_GB, CREA_DTM, CREA_ID) 
            VALUES(concat('제목 ',i), concat('내용 ',i), 0, 'N', CURRENT_TIMESTAMP, 'Admin');
	    SET i = i + 1;
        END WHILE;
    END$$
    DELIMITER ;
    CALL my();


    /* 페이징 Limit #{START}, #{COUNT} */
    SELECT
	    (select count(*) from tb_board) TOTAL_COUNT,
	    IDX,
	    TITLE,
	    HIT_CNT,
	    CREA_DTM    
    FROM
	    TB_BOARD
    WHERE
  	    DEL_GB = 'N'    
	    ORDER BY IDX DESC
    LIMIT 0, 15;
