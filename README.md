# 프로젝트 명: Spring Boot 기반 커뮤니티 게시판

본 프로젝트는 Spring Boot를 이용한 웹 서비스의 기본 구조를 이해하고, 실제 AWS 환경에서의 배포 및 소셜 로그인(OAuth2) 연동 시 발생하는 보안 이슈를 해결하는 데 초점을 맞추어 제작되었습니다.

## 🔗 서비스 접속 주소
- **URL:** [http://3.36.132.53.nip.io/](http://3.36.132.53.nip.io/)
- **배포 환경:** AWS EC2 (Ubuntu/Amazon Linux), MariaDB
> (테스트 계정: 구글 로그인을 이용해 접속 가능합니다.)

## 1. 개발 환경
- **Language/Framework:** Java 17, Spring Boot 3.x
- **ORM:** Spring Data JPA
- **Security:** Spring Security (OAuth2, BCrypt)
- **Database:** MariaDB (Production), MySQL (Local/Development)
- **Infra:** AWS EC2 (Linux), iptables, Git

## 2. 주요 구현 내용

### 2.1. 인증 및 인가 (Spring Security)
- **Form Login:** `UserDetailsService` 구현을 통한 DB 연동 인증.
- **Social Login:** Google OAuth2 API 연동을 통한 간편 로그인 구현.
- **보안 설정:** `BCrypt` 패스워드 암호화 및 경로별 접근 권한(Role) 제어.

### 2.2. Persistence 레이어 (JPA)
- Entity 매핑을 통한 RDBMS 스키마 관리 및 인터페이스 기반 CRUD 구현.
- 제목/내용 키워드 기반 동적 검색 로직 처리.

---

## 3. Trouble Shooting (핵심 해결 기록)

### 3.1. Google OAuth2 'Access Blocked' 및 액세스 제한 이슈
- **현상:** AWS 배포 후 구글 로그인 시도 시 "Access Blocked: This app's request is invalid" 에러 발생하며 인증 불가.
- **원인 분석:** 1. 구글 클라우드 콘솔(GCP)의 OAuth 동의 화면이 '테스트' 상태였으나, 실제 접속 계정이 테스트 사용자로 등록되지 않음.
  2. 승인된 리다이렉트 URI에 실제 배포 서버의 IP 주소가 누락되어 보안 정책 위반으로 차단됨.
- **해결책:** 1. GCP 콘솔에서 본인 계정을 **테스트 사용자**로 등록하여 화이트리스트 처리.
  2. 승인된 리다이렉트 URI에 서버 공인 IP와 콜백 경로(`/login/oauth2/code/google`)를 명시적으로 추가하여 해결.

### 3.2. AWS EC2 외부 접속 및 80포트 바인딩 이슈
- **현상:** 서버 구동 후 외부 IP 접속 불가 및 8000번 포트 명시 없이는 접근 불가능.
- **원인 분석:** 애플리케이션 바인딩 주소가 `127.0.0.1`로 설정되어 외부 유입 차단 및 리눅스 보안 정책상 80포트 사용 제한.
- **해결책:** 1. `application.properties`의 서버 주소를 `0.0.0.0`으로 개방.
  2. `iptables` 설정을 통해 80번(HTTP) 요청을 내부 8000번 포트로 리다이렉트 처리.

### 3.3. 검색 결과 페이지 세션 정보(유저명) 소실
- **현상:** 검색 수행 후 결과 페이지 상단 바에서 로그인 사용자 정보가 사라지는 현상.
- **원인 분석:** 검색 컨트롤러에서 검색 결과 리스트만 전달하고, 세션 내 유저 객체를 Model에 재할당하지 않음.
- **해결책:** 검색 핸들러 내에 세션 체크 로직 추가 및 유저 정보를 Model에 담아 View로 재전달.

### 3.4. 페이지 이동 및 리다이렉트 경로 오류
- **현상:** 상세 페이지 진입 후 메인 이동 시 상대 경로 오설정으로 인한 404 에러.
- **해결책:** Header 및 Navigation 링크를 루트 경로(`/`) 기준으로 통일하여 절대 경로 탐색 이슈 해결.

---

## 4. 프로젝트 구조
- `config/`: Spring Security 및 OAuth2 설정
- `controller/`: 웹 요청 처리 및 세션/데이터 바인딩
- `entity/`: JPA 엔티티 정의
- `repository/`: 데이터 접근 인터페이스
- `templates/`: Thymeleaf 기반 뷰 렌더링

## 향후 계획
- 댓글/답글 CRUD 기능 추가
- 마이페이지 및 프로필 이미지 수정 기능
- CI/CD(GitHub Actions)를 활용한 자동 배포 환경 구축
