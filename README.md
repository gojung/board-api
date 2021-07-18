# spg
충남대학교 컴퓨터융합학부 학술동아리 SPG 홈페이지 
(www.cnu-spg.com)

서비스 운영중입니다. 19.09.04 ~ 21.02.08

서버 비용 문제로 서비스 중단중입니다.

## Contribution
건의사항, 버그 제보 등의 피드백은 언제나 환영합니다.

컨트리뷰션을 원하시는 후배님께서는 메일 주시거나 이슈 남겨주세요!

## 회고록
개발진이 겪은 시행 착오 및 회고를 기재하고 있습니다.

kihwankim - https://kihwankim.github.io/2020/10/18/spg-01-log/

LeeJiHyeong - https://easybrother0103.tistory.com/30

## 개발진

현재 동아리 웹페이지에 공지사항과 같은 동아리의 개인적인 내용이 있기 때문에 승인된 계정으로 로그인하지 않으면 열람할 수 없습니다.
회원가입을 하였을 경우 관리자(회장/부회장/총무/웹페이지 개발진)에게 학번/이름/학교/학년을 적어서 문자나 메일을 남겨주시면 승인하도록 하겠습니다.

## Convention
### Commit Log Convention
- <a href="https://gist.github.com/stephenparish/9941e89d80e2bc58a153">AngularJS Commit Message Conventions</a> 참고해 commit log 작성

### Git flow
- master merge 하기전에 무조건 dev branch로 merge
- 기능 구현 branch 명 : `Feature/${function.name}`
- git Flow 순서
  - `Feature/${function.name}` : 개인 repo/메인 repo
  - `dev
      - 메인 repo
      - code 리뷰 : 희망자만
  - `master` : 메인 repo

## 추가 개발
### 기능 개선
- [x] redis session 추가
- [ ] multi module로 변경
    - [x] 공통 모듈 추출
    - [ ] MVC와 rest-api로 분리
- [ ] JPA 성능 개선
- [ ] 모니터링 도구 추가
- [ ] 로그 적제 코드 추가
- [ ] MSA 구조로 변경
- [ ] Kafka 추가