# 미니프로젝트 humancloud2 - Restful 서버 만들기

### 개요

- Controller는 RestController로 변경
- Service의 모든 메서드에는 @Transactional 작성
- 각 도메인의 ReqDto와 RespDto 내에서 static으로 세부 Dto 만들어서 사용
- mybatis 경로 주의
- Exception은 throw new RuntimeException("필요한 메세지")로 작성
- Controller의 경로는 인증이 필요한 메서드들은 "/s"를 앞에 붙이고, 그 이외의 것들은 "/도메인/\*\*" 방식으로 작성
- Entity 생성자에는 @Builder 작성
- toEntity()는 Dto마다 작성, 관련 Dto 들도 각각 Dto 내에서 작성해서 사용
- insert는 SaveDto, update는 UpdateDto로 작성. 리스트를 보여주는 Dto는 도메인AllRespDto로 작성
- List 객체들의 변수명은 "도메인명List"로 작성

### DB - user생성 및 권한 부여

```sql
CREATE USER 'human'@'%' IDENTIFIED BY 'human1234';
CREATE database humandb;
GRANT ALL PRIVILEGES ON *.* TO 'human'@'%';
```

### 테이블 생성

```sql
-- 구직자(유저)
CREATE TABLE user (
	user_id INT auto_increment PRIMARY KEY,
	username VARCHAR(50) UNIQUE NOT null,
	password VARCHAR(500) NOT null,
	name VARCHAR(50) NOT null,
	email VARCHAR(120) UNIQUE NOT null,
	phone_number VARCHAR(100) UNIQUE,
	created_at TIMESTAMP
) engine=InnoDB default charset=UTF8;

-- 분야
CREATE TABLE category (
	category_id INT auto_increment PRIMARY KEY,
	category_resume_id INT,
	category_recruit_id INT,
	category_name VARCHAR(50)
) engine=InnoDB default charset=UTF8;

-- 회사
CREATE TABLE company (
	company_id INT auto_increment PRIMARY KEY,
	company_username VARCHAR(50) UNIQUE NOT null,
	company_password VARCHAR(500) NOT null,
	company_name VARCHAR(50) UNIQUE NOT null,
	company_email VARCHAR(120) UNIQUE NOT null,
	company_phone_number VARCHAR(100) UNIQUE,
	company_address VARCHAR(150) NOT null,
	company_logo VARCHAR(500),
	company_created_at TimeStamp
) engine=InnoDB default charset=UTF8;

-- 이력서
CREATE TABLE resume(
	resume_id INT AUTO_INCREMENT PRIMARY KEY,
	resume_title VARCHAR(50) NOT null,
	resume_photo VARCHAR(500),
	resume_education VARCHAR(50),
	resume_career VARCHAR(50),
	resume_link VARCHAR(500),
	resume_read_count INT,
	resume_user_id INT,
	resume_created_at TIMESTAMP
) engine=InnoDB default charset=UTF8;


-- 채용 공고
CREATE TABLE recruit(
	recruit_id int auto_increment PRIMARY KEY,
	recruit_title VARCHAR(50) NOT NULL,
	recruit_career VARCHAR(50),
	recruit_salary INT,
	recruit_location VARCHAR(120),
	recruit_content LONGTEXT,
	recruit_read_count INT,
	recruit_company_id INT,
	recruit_deadline TIMESTAMP,
	recruit_created_at TIMESTAMP
) engine=InnoDB default charset=UTF8;

-- 채용 지원
CREATE TABLE apply(
	apply_id INT AUTO_INCREMENT PRIMARY KEY,
	apply_recruit_id INT,
	apply_resume_id INT,
	apply_created_at TIMESTAMP
) engine=InnoDB default charset=UTF8;

-- 관심 기업 구독
CREATE TABLE subscribe(
	subscribe_id INT auto_increment PRIMARY KEY,
	subscribe_user_id INT,
	subscribe_company_id INT,
	subscribe_created_at TIMESTAMP
) engine=InnoDB default charset=UTF8;
```

### fk 제약조건

```sql
-- 이력서 테이블 fk
ALTER TABLE resume ADD FOREIGN KEY(resume_user_id) REFERENCES user(user_id);

-- 채용 공고 테이블 fk
ALTER TABLE recruit ADD FOREIGN KEY(recruit_company_id) REFERENCES company(company_id);

-- 채용 지원 테이블 fk
ALTER TABLE apply ADD FOREIGN KEY(apply_recruit_id) REFERENCES recruit(recruit_id);
ALTER TABLE apply ADD FOREIGN KEY(apply_resume_id) REFERENCES resume(resume_id);

-- 관심 기업 구독 테이블 fk
ALTER TABLE comment ADD FOREIGN KEY(subscribe_user_id) REFERENCES user(user_id);
ALTER TABLE comment ADD FOREIGN KEY(subscribe_company_id) REFERENCES company(company_id);

```

### utf-8 변경

```sql
alter table user convert to character set utf8;

alter table company convert to character set utf8;

alter table resume convert to character set utf8;

alter table recruit convert to character set utf8;

alter table category convert to character set utf8;

alter table apply convert to character set utf8;

alter table subscribe convert to character set utf8;
```


### recruit-detail 수정사항 메모

PathVarilable 이 해당 프로젝트에서는 동작하지않아, 쿼리스트릥으로
Get 요청을 받아 findById 메서드 실행을 한 후,
jstl 로 view 페이지에 로드 했음

layout/header.jsp 에서 충돌나는 헤드 코드 saveForm 에서만 쓰일 수 있도록 코드 옮김. 이 전의 코드를 참고.
