-- 파일명: data_all.sql
-- 목적: 전체 스키마에 대한 초기 샘플 데이터 삽입
-- 비밀번호는 모두 '1234'를 EgovPasswordEncoder(SHA-256 + Base64)로 인코딩한 값 사용:
-- A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=

--------------------------------------------------------------------------------
-- 0) COMTECOPSEQ 초기값 (IDGEN용)
--------------------------------------------------------------------------------
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('EMP_ID', 4);
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('ATT_ID', 4);
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('LEAVEBALANCE_ID', 4);
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('LEAVEREQ_ID', 4);
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('LEAVELOG_ID', 4);
INSERT INTO COMTECOPSEQ (TABLE_NAME, NEXT_ID) VALUES ('HOLIDAY_ID', 4);

--------------------------------------------------------------------------------
-- 1) ROLE 관련 초기 데이터
--------------------------------------------------------------------------------
-- URL 권한 매핑
INSERT INTO ROLE_URL (AUTHORITY, ROLE_PATTERN, ROLE_SORT)
VALUES ('ROLE_ADMIN', '/admin/**', 10);

INSERT INTO ROLE_URL (AUTHORITY, ROLE_PATTERN, ROLE_SORT)
VALUES ('ROLE_USER',  '/mypage/**', 20);

-- 권한 계층
INSERT INTO ROLE_HIERARCHY (PARENT_ROLE, CHILD_ROLE)
VALUES ('ROLE_ADMIN', 'ROLE_USER');

-- 권한 마스터
INSERT INTO ROLE_MASTER (AUTHORITY) VALUES ('ROLE_USER');
INSERT INTO ROLE_MASTER (AUTHORITY) VALUES ('ROLE_MANAGER');
INSERT INTO ROLE_MASTER (AUTHORITY) VALUES ('ROLE_HR');
INSERT INTO ROLE_MASTER (AUTHORITY) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE_MASTER (AUTHORITY) VALUES ('ROLE_AUDITOR');

--------------------------------------------------------------------------------
-- 2) EMPLOYEE / USERS / AUTHORITIES 샘플
--------------------------------------------------------------------------------
-- 비밀번호 해시 공통
-- '1234' → A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=

-- 직원 1: 관리자
INSERT INTO employee (
    id, employee_number, name, email, password,
    position, department, phone, emergency_contact_phone, address,
    work_start_time, work_end_time, hire_date,
    resign_date, updated_at, employment_type, status
) VALUES (
    'EMP_00000001', '1001', '관리자', 'admin@example.com',
    'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
    '관리자', '경영지원', '010-0000-0001', '010-9999-0001', '서울시 어딘가 1',
    '09:00', '18:00',
    TIMESTAMP '2024-01-01 09:00:00',
    NULL,
    SYSTIMESTAMP,
    '정규직', '재직 중'
);

-- 직원 2: 인사담당자
INSERT INTO employee (
    id, employee_number, name, email, password,
    position, department, phone, emergency_contact_phone, address,
    work_start_time, work_end_time, hire_date,
    resign_date, updated_at, employment_type, status
) VALUES (
    'EMP_00000002', '1002', '인사담당', 'hr@example.com',
    'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
    '대리', '인사팀', '010-0000-0002', '010-9999-0002', '서울시 어딘가 2',
    '09:00', '18:00',
    TIMESTAMP '2024-02-01 09:00:00',
    NULL,
    SYSTIMESTAMP,
    '정규직', '재직 중'
);

-- 직원 3: 일반 직원
INSERT INTO employee (
    id, employee_number, name, email, password,
    position, department, phone, emergency_contact_phone, address,
    work_start_time, work_end_time, hire_date,
    resign_date, updated_at, employment_type, status
) VALUES (
    'EMP_00000003', '1003', '일반직원', 'user@example.com',
    'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
    '사원', '개발팀', '010-0000-0003', '010-9999-0003', '서울시 어딘가 3',
    '09:00', '18:00',
    TIMESTAMP '2024-03-01 09:00:00',
    NULL,
    SYSTIMESTAMP,
    '정규직', '재직 중'
);

-- USERS (인증용) - EMPLOYEE와 이메일을 맞춰줌
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED, EMAIL, CREATED_AT)
VALUES ('admin@example.com',
        'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
        1, 'admin@example.com', SYSDATE);

INSERT INTO USERS (USERNAME, PASSWORD, ENABLED, EMAIL, CREATED_AT)
VALUES ('hr@example.com',
        'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
        1, 'hr@example.com', SYSDATE);

INSERT INTO USERS (USERNAME, PASSWORD, ENABLED, EMAIL, CREATED_AT)
VALUES ('user@example.com',
        'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=',
        1, 'user@example.com', SYSDATE);

-- AUTHORITIES (기본 권한)
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('admin@example.com', 'ROLE_ADMIN');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('admin@example.com', 'ROLE_HR');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('hr@example.com',    'ROLE_HR');
INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ('user@example.com',  'ROLE_USER');

--------------------------------------------------------------------------------
-- 3) HOLIDAY_CALENDAR 샘플
--------------------------------------------------------------------------------
INSERT INTO holiday_calendar (id, day, name, is_working_day)
VALUES ('HOL_00000001', DATE '2025-01-01', '신정', 0);

INSERT INTO holiday_calendar (id, day, name, is_working_day)
VALUES ('HOL_00000002', DATE '2025-02-10', '설날 연휴', 0);

INSERT INTO holiday_calendar (id, day, name, is_working_day)
VALUES ('HOL_00000003', DATE '2025-03-01', '삼일절', 0);

--------------------------------------------------------------------------------
-- 4) LEAVE_BALANCE 샘플 (2025년 기준)
--------------------------------------------------------------------------------
-- 관리자
INSERT INTO leave_balance (id, emp_id, year, total, used)
VALUES ('LBAL_00000001', 'EMP_00000001', 2025, 25, 2);

-- 인사담당
INSERT INTO leave_balance (id, emp_id, year, total, used)
VALUES ('LBAL_00000002', 'EMP_00000002', 2025, 20, 1);

-- 일반직원
INSERT INTO leave_balance (id, emp_id, year, total, used)
VALUES ('LBAL_00000003', 'EMP_00000003', 2025, 15, 0);

--------------------------------------------------------------------------------
-- 5) LEAVE_GRANT_LOG 샘플
--    reason: Enums.ANNUAL_GRANT_CALENDAR = '캘린더 기준 연차 지급'
--------------------------------------------------------------------------------
INSERT INTO leave_grant_log (
    id, emp_id, year, reason, change_days, granted_at
) VALUES (
    'LLOG_00000001', 'EMP_00000001', 2025, '캘린더 기준 연차 지급', 25,
    TIMESTAMP '2025-01-01 09:00:00'
);

INSERT INTO leave_grant_log (
    id, emp_id, year, reason, change_days, granted_at
) VALUES (
    'LLOG_00000002', 'EMP_00000002', 2025, '캘린더 기준 연차 지급', 20,
    TIMESTAMP '2025-01-01 09:00:00'
);

INSERT INTO leave_grant_log (
    id, emp_id, year, reason, change_days, granted_at
) VALUES (
    'LLOG_00000003', 'EMP_00000003', 2025, '캘린더 기준 연차 지급', 15,
    TIMESTAMP '2025-01-01 09:00:00'
);

--------------------------------------------------------------------------------
-- 6) ATTENDANCE 샘플 (2025-01-02 기준)
--    status: PRESENT='정상 출근', LATE='지각', EARLY_LEAVE='조퇴'
--------------------------------------------------------------------------------
-- 관리자: 정상 출근
INSERT INTO attendance (
    id, emp_id, work_date, check_in, check_out, status, overtime_minutes
) VALUES (
    'ATT_00000001',
    'EMP_00000001',
    TIMESTAMP '2025-01-02 00:00:00',
    TIMESTAMP '2025-01-02 09:00:00',
    TIMESTAMP '2025-01-02 18:00:00',
    '정상 출근',
    60
);

-- 인사담당: 지각
INSERT INTO attendance (
    id, emp_id, work_date, check_in, check_out, status, overtime_minutes
) VALUES (
    'ATT_00000002',
    'EMP_00000002',
    TIMESTAMP '2025-01-02 00:00:00',
    TIMESTAMP '2025-01-02 09:30:00',
    TIMESTAMP '2025-01-02 18:00:00',
    '지각',
    0
);

-- 일반직원: 조퇴
INSERT INTO attendance (
    id, emp_id, work_date, check_in, check_out, status, overtime_minutes
) VALUES (
    'ATT_00000003',
    'EMP_00000003',
    TIMESTAMP '2025-01-02 00:00:00',
    TIMESTAMP '2025-01-02 09:00:00',
    TIMESTAMP '2025-01-02 17:00:00',
    '조퇴',
    0
);

--------------------------------------------------------------------------------
-- 7) LEAVE_REQUEST 샘플
--    type: ANNUAL='연차'
--    status: APPROVED='승인 완료', PENDING='승인 대기'
--------------------------------------------------------------------------------
-- 관리자: 연차 1일 사용
INSERT INTO leave_request (
    id, emp_id, type, start_date, end_date, days,
    reason, status, created_at, approver, approved_at
) VALUES (
    'LREQ_00000001',
    'EMP_00000001',
    '연차',
    TIMESTAMP '2025-01-10 00:00:00',
    TIMESTAMP '2025-01-10 23:59:59',
    1,
    '가족 행사',
    '승인 완료',
    TIMESTAMP '2024-12-20 10:00:00',
    'hr@example.com',
    TIMESTAMP '2024-12-21 09:00:00'
);

-- 인사담당: 반차 (type은 연차, days=0.5로 표현)
INSERT INTO leave_request (
    id, emp_id, type, start_date, end_date, days,
    reason, status, created_at, approver, approved_at
) VALUES (
    'LREQ_00000002',
    'EMP_00000002',
    '연차',
    TIMESTAMP '2025-01-15 13:00:00',
    TIMESTAMP '2025-01-15 18:00:00',
    0.5,
    '병원 진료',
    '승인 완료',
    TIMESTAMP '2025-01-05 09:30:00',
    'admin@example.com',
    TIMESTAMP '2025-01-05 11:00:00'
);

-- 일반직원: 승인 대기
INSERT INTO leave_request (
    id, emp_id, type, start_date, end_date, days,
    reason, status, created_at, approver, approved_at
) VALUES (
    'LREQ_00000003',
    'EMP_00000003',
    '연차',
    TIMESTAMP '2025-02-01 00:00:00',
    TIMESTAMP '2025-02-02 23:59:59',
    2,
    '여행',
    '승인 대기',
    TIMESTAMP '2025-01-10 15:00:00',
    NULL,
    NULL
);

--------------------------------------------------------------------------------
-- 8) SESSION_LIMIT 샘플
--------------------------------------------------------------------------------
INSERT INTO session_limit (session_id, emp_id, username, expires_at)
VALUES ('SID_SAMPLE_1', 'EMP_00000001', 'admin@example.com',
        SYSTIMESTAMP + INTERVAL '1' HOUR);

INSERT INTO session_limit (session_id, emp_id, username, expires_at)
VALUES ('SID_SAMPLE_2', 'EMP_00000002', 'hr@example.com',
        SYSTIMESTAMP + INTERVAL '1' HOUR);

-- 끝.
