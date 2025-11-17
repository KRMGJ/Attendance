-- 파일명: schema_all.sql
-- 목적: 전체 스키마(보안 + 근태/연차 + 공통 IDGEN)

--------------------------------------------------------------------------------
-- (선택) 기존 객체 정리
--------------------------------------------------------------------------------
-- DROP TABLE ROLE_REGEX      		CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_POINTCUT   		CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_METHOD     		CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_URL        		CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_HIERARCHY  		CASCADE CONSTRAINTS;
-- DROP TABLE AUTHORITIES     		CASCADE CONSTRAINTS;
-- DROP TABLE USERS           		CASCADE CONSTRAINTS;
-- DROP TABLE ROLE_MASTER     		CASCADE CONSTRAINTS;
-- DROP TABLE LETTNLOGINLOG   		CASCADE CONSTRAINTS;
-- DROP TABLE LETTSSYSLOGSUMMARY	CASCADE CONSTRAINTS;

-- DROP TABLE leave_grant_log CASCADE CONSTRAINTS;
-- DROP TABLE leave_request CASCADE CONSTRAINTS;
-- DROP TABLE leave_balance CASCADE CONSTRAINTS;
-- DROP TABLE attendance CASCADE CONSTRAINTS;
-- DROP TABLE session_limit CASCADE CONSTRAINTS;
-- DROP TABLE holiday_calendar CASCADE CONSTRAINTS;
-- DROP TABLE employee CASCADE CONSTRAINTS;
-- DROP TABLE COMTECOPSEQ CASCADE CONSTRAINTS;

--------------------------------------------------------------------------------
-- 공통 시퀀스 테이블 (eGov IDGEN용)
--------------------------------------------------------------------------------
CREATE TABLE COMTECOPSEQ (
  TABLE_NAME   VARCHAR2(60) NOT NULL,
  NEXT_ID      NUMBER(30)   NOT NULL,
  PRIMARY KEY (TABLE_NAME)
);

--------------------------------------------------------------------------------
-- 1) USERS: 로그인 인증 전용 사용자 테이블
--------------------------------------------------------------------------------
CREATE TABLE USERS (
  USERNAME   VARCHAR2(100)   NOT NULL,  -- 로그인 ID. 애플리케이션 전역 고유
  PASSWORD   VARCHAR2(200)   NOT NULL,  -- 해시 저장
  ENABLED    NUMBER(1)       DEFAULT 1 NOT NULL,  -- 1=활성, 0=비활성
  EMAIL      VARCHAR2(200)   NOT NULL,  -- 이메일
  CREATED_AT DATE            DEFAULT SYSDATE NOT NULL,
  CONSTRAINT PK_USERS PRIMARY KEY (USERNAME)
);

COMMENT ON TABLE USERS IS '인증 전용 사용자 테이블(업무용 EMPLOYEE와 분리)';
COMMENT ON COLUMN USERS.USERNAME   IS '로그인 ID(고유)';
COMMENT ON COLUMN USERS.PASSWORD   IS '비밀번호 해시';
COMMENT ON COLUMN USERS.ENABLED    IS '계정 활성화 플래그(1/0)';
COMMENT ON COLUMN USERS.EMAIL      IS '이메일';
COMMENT ON COLUMN USERS.CREATED_AT IS '생성일';

CREATE UNIQUE INDEX UX_USERS_EMAIL ON USERS(EMAIL);
CREATE INDEX IX_USERS_CREATED_AT   ON USERS(CREATED_AT);

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

CREATE INDEX IX_AUTH_AUTHORITY ON AUTHORITIES(AUTHORITY);

--------------------------------------------------------------------------------
-- 3) ROLE_HIERARCHY: 권한 계층
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

CREATE INDEX IX_RH_CHILD ON ROLE_HIERARCHY(CHILD_ROLE);

--------------------------------------------------------------------------------
-- 4) ROLE_URL: URL 패턴 ↔ 권한 매핑
--------------------------------------------------------------------------------
CREATE TABLE ROLE_URL (
  AUTHORITY    VARCHAR2(50)   NOT NULL,   -- 요구 권한
  ROLE_PATTERN VARCHAR2(400)  NOT NULL,   -- URL 패턴
  ROLE_SORT    NUMBER(10)     DEFAULT 0 NOT NULL,  -- 정렬/우선순위
  CONSTRAINT PK_ROLE_URL PRIMARY KEY (AUTHORITY, ROLE_PATTERN)
);

COMMENT ON TABLE ROLE_URL IS 'URL 패턴-권한 매핑 테이블';
COMMENT ON COLUMN ROLE_URL.AUTHORITY    IS '요구 권한';
COMMENT ON COLUMN ROLE_URL.ROLE_PATTERN IS 'URL 패턴';
COMMENT ON COLUMN ROLE_URL.ROLE_SORT    IS '정렬 우선순위';

CREATE INDEX IX_ROLE_URL_PATTERN   ON ROLE_URL(ROLE_PATTERN);
CREATE INDEX IX_ROLE_URL_AUTH_SORT ON ROLE_URL(AUTHORITY, ROLE_SORT);

--------------------------------------------------------------------------------
-- 5) ROLE_METHOD: 메서드 시큐리티 매핑
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

CREATE INDEX IX_ROLE_METHOD_AUTH_SORT ON ROLE_METHOD(AUTHORITY, ROLE_SORT);
CREATE INDEX IX_ROLE_METHOD_PATTERN   ON ROLE_METHOD(ROLE_PATTERN);

--------------------------------------------------------------------------------
-- 6) ROLE_POINTCUT: 포인트컷 기반 매핑
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

CREATE INDEX IX_ROLE_PC_AUTH_SORT ON ROLE_POINTCUT(AUTHORITY, ROLE_SORT);
CREATE INDEX IX_ROLE_PC_PATTERN   ON ROLE_POINTCUT(ROLE_PATTERN);

--------------------------------------------------------------------------------
-- 7) ROLE_REGEX: 정규식 URL 매핑
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

CREATE INDEX IX_ROLE_REGEX_AUTH_SORT ON ROLE_REGEX(AUTHORITY, ROLE_SORT);
CREATE INDEX IX_ROLE_REGEX_PATTERN   ON ROLE_REGEX(ROLE_PATTERN);

--------------------------------------------------------------------------------
-- 8) ROLE_MASTER: 권한 마스터
--------------------------------------------------------------------------------
CREATE TABLE ROLE_MASTER (
  AUTHORITY VARCHAR2(50) PRIMARY KEY
);

COMMENT ON TABLE ROLE_MASTER IS '권한 마스터 테이블';
COMMENT ON COLUMN ROLE_MASTER.AUTHORITY IS '권한';

--------------------------------------------------------------------------------
-- 9) 로그인/시스템 로그 테이블
--------------------------------------------------------------------------------
CREATE TABLE LETTNLOGINLOG (
  LOG_ID      CHAR(20)      NOT NULL,
  CONECT_ID   VARCHAR2(20),
  CONECT_IP   VARCHAR2(23),
  CONECT_MTHD CHAR(4),
  ERROR_OCCRRNC_AT CHAR(1),
  ERROR_CODE  CHAR(3),
  CREAT_DT    DATE,
  CONSTRAINT LETTNLOGINLOG_PK PRIMARY KEY (LOG_ID)
);

CREATE INDEX IX_LOGINLOG_CREAT_DT ON LETTNLOGINLOG(CREAT_DT);

CREATE TABLE LETTSSYSLOGSUMMARY (
  OCCRRNC_DE CHAR(20)   NOT NULL,
  SRVC_NM    VARCHAR2(255) NOT NULL,
  METHOD_NM  VARCHAR2(60)  NOT NULL,
  CREAT_CO   NUMBER(10,0),
  UPDT_CO    NUMBER(10,0),
  RDCNT      NUMBER(10,0),
  DELETE_CO  NUMBER(10,0),
  OUTPT_CO   NUMBER(10,0),
  ERROR_CO   NUMBER(10,0),
  CONSTRAINT LETTSSYSLOGSUMMARY_PK PRIMARY KEY (OCCRRNC_DE, SRVC_NM, METHOD_NM)
);

CREATE INDEX IX_SYSLOG_DE_NM ON LETTSSYSLOGSUMMARY(OCCRRNC_DE, SRVC_NM);

--------------------------------------------------------------------------------
-- EMPLOYEE : 직원 정보 테이블
--------------------------------------------------------------------------------
CREATE TABLE employee (
    id                      VARCHAR2(36)      NOT NULL,
    employee_number         VARCHAR2(20),
    name                    VARCHAR2(80)      NOT NULL,
    email                   VARCHAR2(120)     NOT NULL,
    password                VARCHAR2(255)     NOT NULL,
    position                VARCHAR2(60),
    department              VARCHAR2(50),
    phone                   VARCHAR2(20),
    emergency_contact_phone VARCHAR2(20),
    address                 VARCHAR2(200),
    work_start_time         VARCHAR2(5),
    work_end_time           VARCHAR2(5),
    hire_date               TIMESTAMP(6)      NOT NULL,
    resign_date             TIMESTAMP(6),
    updated_at              TIMESTAMP(6),
    employment_type         VARCHAR2(20)      NOT NULL,
    status                  VARCHAR2(20)      DEFAULT '재직 중' NOT NULL,
    CONSTRAINT pk_employee PRIMARY KEY (id),
    CONSTRAINT uk_employee_email   UNIQUE (email),
    CONSTRAINT uk_employee_number UNIQUE (employee_number),
    CONSTRAINT chk_employee_number
        CHECK (employee_number IS NULL OR REGEXP_LIKE(employee_number, '^[A-Z0-9]+$')),
    CONSTRAINT chk_employee_email
        CHECK (REGEXP_LIKE(email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')),
    CONSTRAINT chk_employee_phone
        CHECK (phone IS NULL OR REGEXP_LIKE(phone, '^[0-9\-+() ]+$')),
    CONSTRAINT chk_employee_emergency_phone
        CHECK (emergency_contact_phone IS NULL OR REGEXP_LIKE(emergency_contact_phone, '^[0-9\-+() ]+$')),
    CONSTRAINT chk_employee_work_start
        CHECK (work_start_time IS NULL OR REGEXP_LIKE(work_start_time, '^(?:[01][0-9]|2[0-3]):[0-5][0-9]$')),
    CONSTRAINT chk_employee_work_end
        CHECK (work_end_time IS NULL OR REGEXP_LIKE(work_end_time, '^(?:[01][0-9]|2[0-3]):[0-5][0-9]$'))
);

COMMENT ON TABLE employee IS '직원 정보 테이블';
COMMENT ON COLUMN employee.id                      IS '직원 식별자(UUID 또는 프리픽스 ID)';
COMMENT ON COLUMN employee.employee_number         IS '직원 번호';
COMMENT ON COLUMN employee.name                    IS '직원 이름';
COMMENT ON COLUMN employee.email                   IS '직원 이메일(로그인 ID)';
COMMENT ON COLUMN employee.password                IS '암호화된 비밀번호';
COMMENT ON COLUMN employee.position                IS '직급 또는 직책';
COMMENT ON COLUMN employee.department              IS '부서 이름';
COMMENT ON COLUMN employee.phone                   IS '전화번호';
COMMENT ON COLUMN employee.emergency_contact_phone IS '비상 연락처 전화번호';
COMMENT ON COLUMN employee.address                 IS '주소';
COMMENT ON COLUMN employee.work_start_time         IS '근무 시작 시간 (HH:mm 형식)';
COMMENT ON COLUMN employee.work_end_time           IS '근무 종료 시간 (HH:mm 형식)';
COMMENT ON COLUMN employee.hire_date               IS '입사일';
COMMENT ON COLUMN employee.resign_date             IS '퇴사일';
COMMENT ON COLUMN employee.updated_at              IS '정보 수정일';
COMMENT ON COLUMN employee.employment_type         IS '고용 형태(정규직, 계약직 등)';
COMMENT ON COLUMN employee.status                  IS '고용 상태(재직중, 휴직중, 퇴사 등)';

--------------------------------------------------------------------------------
-- HOLIDAY_CALENDAR : 휴일 및 근무일 관리 테이블
--------------------------------------------------------------------------------
CREATE TABLE holiday_calendar (
    id             VARCHAR2(36)  NOT NULL,
    day            DATE          NOT NULL,
    name           VARCHAR2(100) NOT NULL,
    is_working_day NUMBER(1)     NOT NULL,
    CONSTRAINT pk_holiday_calendar PRIMARY KEY (id),
    CONSTRAINT uk_holiday_day UNIQUE (day),
    CONSTRAINT chk_holiday_is_working
        CHECK (is_working_day IN (0, 1))
);

COMMENT ON TABLE holiday_calendar IS '휴일 및 근무일 관리 테이블';
COMMENT ON COLUMN holiday_calendar.id             IS 'UUID 또는 프리픽스 ID';
COMMENT ON COLUMN holiday_calendar.day            IS '날짜';
COMMENT ON COLUMN holiday_calendar.name           IS '명칭';
COMMENT ON COLUMN holiday_calendar.is_working_day IS '근무일여부 (0: 휴일, 1: 근무일)';

--------------------------------------------------------------------------------
-- LEAVE_BALANCE : 연차 잔여일 테이블
--------------------------------------------------------------------------------
CREATE TABLE leave_balance (
    id      VARCHAR2(36) NOT NULL,
    emp_id  VARCHAR2(36) NOT NULL,
    year    NUMBER(4)    NOT NULL,
    total   NUMBER(10)   NOT NULL,
    used    NUMBER(10)   NOT NULL,
    CONSTRAINT pk_leave_balance PRIMARY KEY (id),
    CONSTRAINT uk_leave_balance_emp_year UNIQUE (emp_id, year),
    CONSTRAINT fk_leave_balance_emp
        FOREIGN KEY (emp_id) REFERENCES employee(id)
);

COMMENT ON TABLE leave_balance IS '연차 잔여일 테이블';
COMMENT ON COLUMN leave_balance.id     IS '연차 잔여일 식별자';
COMMENT ON COLUMN leave_balance.emp_id IS '직원 식별자(employee.id 참조)';
COMMENT ON COLUMN leave_balance.year   IS '연도';
COMMENT ON COLUMN leave_balance.total  IS '총 연차 일수';
COMMENT ON COLUMN leave_balance.used   IS '사용한 연차 일수';

--------------------------------------------------------------------------------
-- ATTENDANCE : 근태 기록 테이블
--------------------------------------------------------------------------------
CREATE TABLE attendance (
    id               VARCHAR2(36) NOT NULL,
    emp_id           VARCHAR2(36) NOT NULL,
    work_date        TIMESTAMP(6) NOT NULL,
    check_in         TIMESTAMP(6),
    check_out        TIMESTAMP(6),
    status           VARCHAR2(20) NOT NULL,
    overtime_minutes NUMBER(10)   DEFAULT 0 NOT NULL,
    CONSTRAINT pk_attendance PRIMARY KEY (id),
    CONSTRAINT uk_attendance_emp_workdate UNIQUE (emp_id, work_date),
    CONSTRAINT fk_attendance_emp
        FOREIGN KEY (emp_id) REFERENCES employee(id)
);

COMMENT ON TABLE attendance IS '근태 기록 테이블';
COMMENT ON COLUMN attendance.id               IS '근태 기록 식별자';
COMMENT ON COLUMN attendance.emp_id           IS '직원 식별자(EMPLOYEE.ID 참조)';
COMMENT ON COLUMN attendance.work_date        IS '근무 날짜';
COMMENT ON COLUMN attendance.check_in         IS '출근 시간';
COMMENT ON COLUMN attendance.check_out        IS '퇴근 시간';
COMMENT ON COLUMN attendance.status           IS '근무 상태(정상, 지각, 조퇴, 결근 등)';
COMMENT ON COLUMN attendance.overtime_minutes IS '초과 근무 시간(분 단위)';

--------------------------------------------------------------------------------
-- LEAVE_REQUEST : 휴가 신청 테이블
--------------------------------------------------------------------------------
CREATE TABLE leave_request (
    id          VARCHAR2(36)  NOT NULL,
    emp_id      VARCHAR2(36)  NOT NULL,
    type        VARCHAR2(20)  NOT NULL,
    start_date  TIMESTAMP(6)  NOT NULL,
    end_date    TIMESTAMP(6)  NOT NULL,
    days        NUMBER(10)    NOT NULL,
    reason      VARCHAR2(500),
    status      VARCHAR2(20)  NOT NULL,
    created_at  TIMESTAMP(6)  NOT NULL,
    approver    VARCHAR2(100),
    approved_at TIMESTAMP(6),
    CONSTRAINT pk_leave_request PRIMARY KEY (id),
    CONSTRAINT uk_leave_request_emp_start_end UNIQUE (emp_id, start_date, end_date),
    CONSTRAINT fk_leave_request_emp
        FOREIGN KEY (emp_id) REFERENCES employee(id)
);

COMMENT ON TABLE leave_request IS '휴가 신청 테이블';
COMMENT ON COLUMN leave_request.id          IS '휴가신청ID';
COMMENT ON COLUMN leave_request.emp_id      IS '사원ID';
COMMENT ON COLUMN leave_request.type        IS '휴가유형';
COMMENT ON COLUMN leave_request.start_date  IS '시작일';
COMMENT ON COLUMN leave_request.end_date    IS '종료일';
COMMENT ON COLUMN leave_request.days        IS '신청 일수';
COMMENT ON COLUMN leave_request.reason      IS '사유';
COMMENT ON COLUMN leave_request.status      IS '상태';
COMMENT ON COLUMN leave_request.created_at  IS '신청일시';
COMMENT ON COLUMN leave_request.approver    IS '승인자';
COMMENT ON COLUMN leave_request.approved_at IS '승인일시';

--------------------------------------------------------------------------------
-- LEAVE_GRANT_LOG : 연차 지급 이력 테이블
--------------------------------------------------------------------------------
CREATE TABLE leave_grant_log (
    id          VARCHAR2(36)  NOT NULL,
    emp_id      VARCHAR2(36)  NOT NULL,
    year        NUMBER(4)     NOT NULL,
    reason      VARCHAR2(30)  NOT NULL,
    change_days NUMBER(10)    NOT NULL,
    granted_at  TIMESTAMP(6)  NOT NULL,
    CONSTRAINT pk_leave_grant_log PRIMARY KEY (id),
    CONSTRAINT uk_grant_emp_year_reason_dt
        UNIQUE (emp_id, year, reason, granted_at),
    CONSTRAINT fk_leave_grant_emp
        FOREIGN KEY (emp_id) REFERENCES employee(id),
    CONSTRAINT fk_leave_grant_balance
        FOREIGN KEY (emp_id, year) REFERENCES leave_balance(emp_id, year)
);

COMMENT ON TABLE leave_grant_log IS '연차 지급 이력 테이블';
COMMENT ON COLUMN leave_grant_log.id          IS '연차 지급 로그 ID';
COMMENT ON COLUMN leave_grant_log.emp_id      IS '사원ID';
COMMENT ON COLUMN leave_grant_log.year        IS '연도';
COMMENT ON COLUMN leave_grant_log.reason      IS '지급사유';
COMMENT ON COLUMN leave_grant_log.change_days IS '변경일수(양수=지급, 음수=차감)';
COMMENT ON COLUMN leave_grant_log.granted_at  IS '지급일시';

--------------------------------------------------------------------------------
-- SESSION_LIMIT : 세션 제한 관리 테이블
--------------------------------------------------------------------------------
CREATE TABLE session_limit (
    session_id VARCHAR2(100) NOT NULL,
    emp_id     VARCHAR2(36),
    username   VARCHAR2(100) NOT NULL,
    expires_at TIMESTAMP(6)  NOT NULL,
    CONSTRAINT pk_session_limit PRIMARY KEY (session_id),
    CONSTRAINT fk_session_limit_emp
        FOREIGN KEY (emp_id) REFERENCES employee(id)
);

COMMENT ON TABLE session_limit IS '세션 제한 관리 테이블';
COMMENT ON COLUMN session_limit.session_id IS '세션ID';
COMMENT ON COLUMN session_limit.emp_id     IS '직원 식별자(employee.id)';
COMMENT ON COLUMN session_limit.username   IS '사용자명';
COMMENT ON COLUMN session_limit.expires_at IS '만료일시';

--------------------------------------------------------------------------------
-- 인덱스 생성
--------------------------------------------------------------------------------
CREATE INDEX ix_employee_hiredate
    ON employee (hire_date);

CREATE INDEX ix_attendance_workdate
    ON attendance (work_date);

CREATE INDEX ix_lrq_status_created
    ON leave_request (status, created_at);

CREATE INDEX ix_grant_emp_year
    ON leave_grant_log (emp_id, year);

CREATE INDEX ix_sessionlimit_username
    ON session_limit (username);

CREATE INDEX ix_holiday_workingday
    ON holiday_calendar (is_working_day);

-- 끝.