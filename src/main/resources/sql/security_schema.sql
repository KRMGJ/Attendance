-- 파일명: security_schema.sql
-- 목적: Spring Security + DB 기반 권한 매핑용 최소 스키마
-- 구성: USERS, AUTHORITIES, ROLE_HIERARCHY, ROLE_URL, ROLE_METHOD, ROLE_POINTCUT, ROLE_REGEX
-- 주의: USERS/AUTHORITIES는 "인증/권한" 전용. 실제 업무 정보(EMPLOYEE 등)와 분리 운영.

--------------------------------------------------------------------------------
-- (선택) 기존 객체 정리
--------------------------------------------------------------------------------
-- DROP TABLE ROLE_REGEX      CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_POINTCUT   CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_METHOD     CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_URL        CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_HIERARCHY  CASCADE CONSTRAINTS;
-- DROP TABLE AUTHORITIES     CASCADE CONSTRAINTS;
-- DROP TABLE USERS           CASCADE CONSTRAINTS;

--------------------------------------------------------------------------------
-- 1) USERS: 로그인 인증 전용 사용자 테이블
--------------------------------------------------------------------------------
CREATE TABLE USERS (
  USERNAME   VARCHAR2(100)   NOT NULL,  -- 로그인 ID. 애플리케이션 전역 고유
  PASSWORD   VARCHAR2(200)   NOT NULL,  -- BCrypt 등 해시 저장
  ENABLED    NUMBER(1)       DEFAULT 1 NOT NULL,  -- 1=활성, 0=비활성
  EMAIL      VARCHAR2(200),               -- 선택. 필요 시 UNIQUE 인덱스 부여
  CREATED_AT DATE            DEFAULT SYSDATE NOT NULL,
  CONSTRAINT PK_USERS PRIMARY KEY (USERNAME)
);

COMMENT ON TABLE USERS IS '인증 전용 사용자 테이블(업무용 EMPLOYEE와 분리)';
COMMENT ON COLUMN USERS.USERNAME   IS '로그인 ID(고유)';
COMMENT ON COLUMN USERS.PASSWORD   IS '비밀번호 해시(예: BCrypt)';
COMMENT ON COLUMN USERS.ENABLED    IS '계정 활성화 플래그(1/0)';
COMMENT ON COLUMN USERS.EMAIL      IS '이메일(선택)';
COMMENT ON COLUMN USERS.CREATED_AT IS '생성일';

-- (선택) 이메일 유니크 인덱스
-- CREATE UNIQUE INDEX UX_USERS_EMAIL ON USERS(EMAIL);

--------------------------------------------------------------------------------
-- 2) AUTHORITIES: 사용자 ↔ 권한 매핑
--------------------------------------------------------------------------------
CREATE TABLE AUTHORITIES (
  USERNAME  VARCHAR2(100) NOT NULL,  -- USERS.USERNAME 참조
  AUTHORITY VARCHAR2(50)  NOT NULL,  -- 예: ROLE_ADMIN, ROLE_USER
  CONSTRAINT PK_AUTHORITIES PRIMARY KEY (USERNAME, AUTHORITY),
  CONSTRAINT FK_AUTHORITIES_USER FOREIGN KEY (USERNAME)
    REFERENCES USERS(USERNAME) ON DELETE CASCADE
);

COMMENT ON TABLE AUTHORITIES IS '사용자 권한 매핑 테이블';
COMMENT ON COLUMN AUTHORITIES.USERNAME  IS 'USERS.USERNAME 참조';
COMMENT ON COLUMN AUTHORITIES.AUTHORITY IS '권한 코드(ROLE_*)';

--------------------------------------------------------------------------------
-- 3) ROLE_HIERARCHY: 권한 계층(선택)
--    예: ROLE_ADMIN > ROLE_MANAGER > ROLE_USER 형태의 상하위 승계 정의
--------------------------------------------------------------------------------
CREATE TABLE ROLE_HIERARCHY (
  PARENT_ROLE VARCHAR2(50) NOT NULL,  -- 상위 권한
  CHILD_ROLE  VARCHAR2(50) NOT NULL,  -- 하위 권한
  CONSTRAINT PK_ROLE_HIERARCHY PRIMARY KEY (PARENT_ROLE, CHILD_ROLE),
  CONSTRAINT CHK_ROLE_HIER_NOT_SAME CHECK (PARENT_ROLE <> CHILD_ROLE)
);

COMMENT ON TABLE ROLE_HIERARCHY IS '권한 계층 테이블(상위→하위 승계)';
COMMENT ON COLUMN ROLE_HIERARCHY.PARENT_ROLE IS '상위 권한';
COMMENT ON COLUMN ROLE_HIERARCHY.CHILD_ROLE  IS '하위 권한';

--------------------------------------------------------------------------------
-- 4) ROLE_URL: URL 패턴 ↔ 권한 매핑
--    e.g. /admin/** 는 ROLE_ADMIN 필요
--------------------------------------------------------------------------------
CREATE TABLE ROLE_URL (
  AUTHORITY    VARCHAR2(50)   NOT NULL,   -- 요구 권한
  ROLE_PATTERN VARCHAR2(400)  NOT NULL,   -- URL 패턴(안티패턴 또는 정확경로)
  ROLE_SORT    NUMBER(10)     DEFAULT 0 NOT NULL,  -- 정렬/우선순위
  CONSTRAINT PK_ROLE_URL PRIMARY KEY (AUTHORITY, ROLE_PATTERN)
);

COMMENT ON TABLE ROLE_URL IS 'URL 패턴-권한 매핑 테이블';
COMMENT ON COLUMN ROLE_URL.AUTHORITY    IS '요구 권한';
COMMENT ON COLUMN ROLE_URL.ROLE_PATTERN IS 'URL 패턴';
COMMENT ON COLUMN ROLE_URL.ROLE_SORT    IS '정렬 우선순위';

-- 패턴 검색 최적화 인덱스(선택)
CREATE INDEX IX_ROLE_URL_PATTERN ON ROLE_URL(ROLE_PATTERN);

--------------------------------------------------------------------------------
-- 5) ROLE_METHOD: 메서드 시큐리티 매핑(선택)
--    AOP 메서드 시그니처 또는 표현식과 권한 매핑
--------------------------------------------------------------------------------
CREATE TABLE ROLE_METHOD (
  AUTHORITY    VARCHAR2(50)   NOT NULL,
  ROLE_PATTERN VARCHAR2(400)  NOT NULL,   -- 메서드 패턴/시그니처
  ROLE_SORT    NUMBER(10)     DEFAULT 0 NOT NULL,
  CONSTRAINT PK_ROLE_METHOD PRIMARY KEY (AUTHORITY, ROLE_PATTERN)
);

COMMENT ON TABLE ROLE_METHOD IS '메서드 보안 패턴-권한 매핑';
COMMENT ON COLUMN ROLE_METHOD.AUTHORITY    IS '요구 권한';
COMMENT ON COLUMN ROLE_METHOD.ROLE_PATTERN IS '메서드 패턴/시그니처';
COMMENT ON COLUMN ROLE_METHOD.ROLE_SORT    IS '정렬 우선순위';

--------------------------------------------------------------------------------
-- 6) ROLE_POINTCUT: 포인트컷 기반 매핑(선택)
--------------------------------------------------------------------------------
CREATE TABLE ROLE_POINTCUT (
  AUTHORITY    VARCHAR2(50)   NOT NULL,
  ROLE_PATTERN VARCHAR2(400)  NOT NULL,   -- 포인트컷 표현식
  ROLE_SORT    NUMBER(10)     DEFAULT 0 NOT NULL,
  CONSTRAINT PK_ROLE_POINTCUT PRIMARY KEY (AUTHORITY, ROLE_PATTERN)
);

COMMENT ON TABLE ROLE_POINTCUT IS '포인트컷 표현식-권한 매핑';
COMMENT ON COLUMN ROLE_POINTCUT.AUTHORITY    IS '요구 권한';
COMMENT ON COLUMN ROLE_POINTCUT.ROLE_PATTERN IS '포인트컷 표현식';
COMMENT ON COLUMN ROLE_POINTCUT.ROLE_SORT    IS '정렬 우선순위';

--------------------------------------------------------------------------------
-- 7) ROLE_REGEX: 정규식 URL 매핑(선택)
--    정규식 매처를 사용할 때의 URL ↔ 권한 매핑
--------------------------------------------------------------------------------
CREATE TABLE ROLE_REGEX (
  AUTHORITY    VARCHAR2(50)   NOT NULL,
  ROLE_PATTERN VARCHAR2(400)  NOT NULL,   -- 정규식 패턴
  ROLE_SORT    NUMBER(10)     DEFAULT 0 NOT NULL,
  CONSTRAINT PK_ROLE_REGEX PRIMARY KEY (AUTHORITY, ROLE_PATTERN)
);

COMMENT ON TABLE ROLE_REGEX IS '정규식 URL-권한 매핑';
COMMENT ON COLUMN ROLE_REGEX.AUTHORITY    IS '요구 권한';
COMMENT ON COLUMN ROLE_REGEX.ROLE_PATTERN IS '정규식 URL 패턴';
COMMENT ON COLUMN ROLE_REGEX.ROLE_SORT    IS '정렬 우선순위';

--------------------------------------------------------------------------------
-- (선택) 초기 샘플 데이터
--------------------------------------------------------------------------------
 INSERT INTO USERS (USERNAME, PASSWORD, ENABLED, EMAIL) VALUES ('admin', '{bcrypt}해시값', 1, 'admin@example.com');
 INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('admin', 'ROLE_ADMIN');
 INSERT INTO ROLE_URL (AUTHORITY, ROLE_PATTERN, ROLE_SORT) VALUES ('ROLE_ADMIN', '/admin/**', 10);
 INSERT INTO ROLE_URL (AUTHORITY, ROLE_PATTERN, ROLE_SORT) VALUES ('ROLE_USER',  '/mypage/**', 20);
 INSERT INTO ROLE_HIERARCHY (PARENT_ROLE, CHILD_ROLE) VALUES ('ROLE_ADMIN', 'ROLE_USER');
 
 CREATE TABLE LETTNLOGINLOG (
  LOG_ID char(20) NOT NULL,
  CONECT_ID varchar2(20) ,
  CONECT_IP varchar2(23) ,
  CONECT_MTHD char(4) ,
  ERROR_OCCRRNC_AT char(1) ,
  ERROR_CODE char(3) ,
  CREAT_DT DATE ,
  CONSTRAINT LETTNLOGINLOG_PK PRIMARY KEY (LOG_ID)
) ;

CREATE TABLE LETTSSYSLOGSUMMARY (
  OCCRRNC_DE char(20) NOT NULL,
  SRVC_NM varchar2(255) NOT NULL,
  METHOD_NM varchar2(60) NOT NULL,
  CREAT_CO number(10,0) ,
  UPDT_CO number(10,0) ,
  RDCNT number(10,0) ,
  DELETE_CO number(10,0) ,
  OUTPT_CO number(10,0) ,
  ERROR_CO number(10,0) ,
  CONSTRAINT LETTSSYSLOGSUMMARY_PK PRIMARY KEY (OCCRRNC_DE,SRVC_NM,METHOD_NM)
) ;

-- 끝.
