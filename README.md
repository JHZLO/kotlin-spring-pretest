# 문서 작성
### a. 과제를 어떻게 분석하셨나요?
우선 주어진 조건을 여러 차례 꼼꼼히 읽었습니다. 구현해야 할 분량이 많고 각 엔티티 간의 관계가 얽혀 있어 처음에는 구조를 한눈에 파악하기 어려웠습니다.

그래서 약 30~40분 정도는 README를 기반으로 기능 명세서를 작성하고 이를 세부 구현 단위로 나누어 정리했습니다. 특히 엔티티 간의 관계가 복잡했기 때문에, 이 관계를 정확히 이해하고자 구조를 도식화하며 충분히 시간을 투자했습니다.

### b. 과제의 진행함에 있어 AI 를 어떻게 활용 하셨나요? 어떤 어려움이 있었나요?
외부 클라이언트를 활용해 직접 RestClient나 WebClient로 OpenAI API를 호출할 수도 있었지만, 이번에는 최근 관심을 갖고 있던 Spring AI를 적용해 보고자 했습니다.

또한 구현 명세서에 “OpenAI 등 유명한 provider는 알고 있지만, API spec에 대한 깊은 이해는 없습니다” 라는 문구를 보고, 이는 곧 직접 OpenAI API 스펙을 파고들기보다는 Spring AI의 추상화 레이어를 통해 서비스 구현에 집중하라는 의도가 담겨 있다고 해석했습니다.

따라서 chat model을 개발할 때, Spring AI의 OpenAiChatModel을 사용하여 OpenAI와의 연동을 구성했는데, Spring AI가 이러한 LLM 호출을 표준화된 컴포넌트로 추상화해주었기 때문에, 개발자는 실제 서비스 로직 구현에 더욱 집중할 수 있었습니다.

다만 Spring AI의 내부 구조나 컴포넌트 동작 방식을 충분히 학습해야 하는 러닝커브가 있었고 이를 이해하고 적용하는 과정에서 시간이 다소 소요되었다는 점이 어려움으로 느껴졌습니다.


### c. 지원자가 구현하기 가장 어려웠던 1개 이상의 기능을 설명 해주세요.
가장 어려웠던 부분은 chat, thread, user 간의 관계 설계였습니다.
처음에는 chat과 thread 사이의 관계를 이해하는 데 집중했지만, thread가 user를 소유하고 있는 구조와 동시에 chat이 thread를 참조하는 구조가 결합되면서 상당한 혼동이 있었습니다.
예를 들어 하나의 user가 여러 thread를 생성할 수 있고 그 thread마다 여러 chat이 연결될 수 있으며, 동시에 각 chat이 다시 user의 맥락 안에서만 유효하다는 규칙을 어떻게 모델링할지 고민이 많았습니다.

이러한 관계를 단순하게 코드 레벨에서만 바라보면 오류가 발생할 수 있다고 판단해 관계형 데이터베이스 관점에서 여러 차례 그려보았습니다.
특히 user → thread → chat 의 계층적인 관계를 명확히 표현하면서 스레드 단위의 대화를 어떻게 그룹핑하고 관리할지를 ERD로 반복적으로 다듬었습니다.
결국 user가 thread를 소유하고, thread가 여러 chat을 보유한다는 구조를 정리하면서 문제를 해결할 수 있었습니다.

또한 후반부에 분석 및 보고 기능을 구현하면서 사용자 활동 기록 중 로그인 수를 집계해야 하는 부분에서 어려움이 있었습니다.
이미 auth와 user 관련 도메인은 어느 정도 구현을 마친 상태였기 때문에 로그인 횟수를 별도로 어떻게 기록하고 집계할지 막막했습니다.

고민 끝에 LoginHistory라는 엔티티를 새로 설계하여 로그인 시점마다 이력을 기록하도록 구성했습니다.
이 방식으로 auth 도메인에 loginHistoryService를 간단히 주입해서 로그인 시마다 로그를 남기도록 했고,
덕분에 분석 도메인에서는 로그인 횟수를 독립적으로 집계할 수 있게 되었습니다.
결과적으로 분석 로직과 인증 로직의 의존성을 최소화하면서도 필요한 데이터 기록을 성공적으로 설계할 수 있었습니다.

---

# 기능 구현 명세서

```text
VIP onboarding 팀은 oooooo의 소규모 조직으로 잠재 고객사와의 초기 협력을 담당합니다.
이 팀은 1명의 영업 직원과 1명의 개발자로 이루어져 있으며, 지원자는 유일한 개발자 입니다.

어느 날 지원자는 영업 직원으로부터 고객사로의 긴급 시연 요청을 받았습니다.
고객사의 간단한 비 업무 요건은 아래와 같으며 3시간 이내에 메뉴얼과 같이 전달되기를 기대합니다.

- 챗봇 ai 를 사용할 수 있는 api 제공이 필요 합니다.
    - 향후 자사의 대외비 문서를 학습시키고 싶어 합니다.
- openai 등 유명 provider 는 알고 있지만, api spec 에 대한 깊은 이해는 없습니다.
- 시연의 목표는 api 를 통해 ai 를 활용할 수 있다. 입니다.
    - 단, 모종의 이유로 본 repo 는 지속적으로 확장 개발 가능해야 합니다.
- 업무 요건을 조율할 수 있는 영업/고객사 직원은 모두 긴급 회의에 들어가 연락이 되지 않습니다.

무리하고 어려운 일정이지만 지원자는 프로로서 납득할만한 결과를 만들어낼 수 있을 것 입니다..!
```

# 챗봇 서비스 요구사항

## 1. 사용자 관리 및 인증 기능
- [x] 사용자 엔티티 생성
  - 이메일
  - 패스워드
  - 이름
  - 생성일시
  - 권한 (멤버, 관리자)
- [x] 회원 가입 기능 구현
  - 이메일, 패스워드, 이름을 필수로 입력 받는다.
- [x] 로그인 기능 구현
  - 이메일, 패스워드
  - 로그인 완료시 JWT 형식의 토큰 발급
- [x] Spring Security 인증/인가 구현
  - JWT 토큰을 확인하여 인증하도록 구성한다.

## 2. 대화 관리 기능
- [x] 대화(chat) 엔티티 생성
  - 질문
  - 답변
  - 생성일시
- [x] 스레드를 활용한 대화 관리 기능 구현
  - 스레드는 각 유저마다 여러 개 생성할 수 있다.
  - 스레드 생성 시점
    - 해당 유저의 첫질문, 마지막 질문 후 30분이 지난 시점
    - 해당 유저가 마지막 질문으로 부터 30분 이내에 다시 질문할 경우 기존 스레드 사용

- [x] 대화 생성
  - 질문 -> 답변
  - 추가 옵션 파라미터 "선택"
    - isStreaming: true로 설정될 경우 stream 형태로 응답
    - model: 설정될 경우 해당 모델로 응답
- [x] 대화 목록 조회
  - 요청한 유저의 모든 대화 응답하는 기능
  - 스레드 단위로 그룹화된 대화의 목록 응답
  - 각 유저는 자신이 생성한 스레드와 대화만 조회 가능
    - 모든 스레드의 대화 조회 가능
  - 생성일시 기준으로 오름차순/내림차순 정렬이 가능하며 페이지네이션이 가능해야함
- [x] 스레드 삭제
  - 특정 스레드를 선택하여 삭제하는 기능
  - 각 유저는 자신이 생성한 스레드만 삭제 가능

## 3. 사용자 피드백 관리 기능
- [x] 피드백 엔티티 생성
  - 사용자 ID (string)
  - 대화 ID(string): 피드백 대인 대화의 ID
  - 긍정/부정 유무(boolean)
  - 생성일시(timestamp)
  - 상태(string) (`pending`, `resolved`)
- [x] 피드백 생성 기능
  - 특정 대화에 피드백 생성
  - 자신이 생성한 대화에만 생성 가능
    - 하나의 대화에 하나의 피드백만
    - 하나의 대화에 서로 다른 사용자들이 생성한 n개의 피드백 존재할 수 있음
  - 관리자는 모든 대화에 대해 피드백 생성 가능
- [x] 피드백 목록 조회
  - 각 사용자는 자신이 생성한 피드백 목록 조회 가능
  - 관리자는 모든 사용자 피드백 조회 가능
  - 생성일시 기준으로 오름차순/내림차순 정렬이 가능하며 페이지 네이션이 가능함
  - 긍정/부정 유무로 필터링할 수 있음
- [x] 피드백 상태 변경
  - 관리자는 피드백의 상태 업데이트 가능 부정, 긍정

## 4. 분석 및 보고 기능
- [x] 사용자 활동 기록 요청
    - 회원가입, 로그인, 대화 생성 수를 응답합니다.
    - 요청 시점으로부터 하루 동안의 기록을 응답합니다.
    - 해당 기능은 관리자만 요청 가능합니다.
- [x] 보고서 생성 요청
    - 호출 시 csv 형태의 보고서를 생성합니다.
    - 보고서에는 모든 사용자의 대화 목록과 어떤 사용자가 생성했는지 포함됩니다.
    - 요청 시점으로부터 하루 동안의 보고서가 생성되어야 합니다.
    - 해당 기능은 관리자만 요청 가능합니다.

---
