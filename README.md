```
MSA 구축과 kafka study를 위한 repo 입니다~_~
```

# 컨테이너 빌드
`docker` 디렉토리 내부에서 명령어 실행
## airflow container

```bash
docker compose -p airflow -f docker-compose.airflow.yaml up --build -d
```

## backend server container

```bash
docker compose -p backend -f docker-compose.backend.yaml up --build -d
```

## kafka server container

```bash
docker compose -p kafka -f docker-compose.kafka.yaml up --build -d
```

## 이슈?

🎈 아마 backend 서버 DB 컨테이너를 아직 정해주지 않아서 빌드후 스프링컨테이너 오류메세지가 계속해서 뜨는것 같습니다.

🎈 backend 서버 외의 다른 컨테이너 kafka, airflow 서버는 빌드해도 크게 이슈가 있진 않습니다 !(직접 명령어 입력해보시면 알 수 있습니다.)

<img src=https://github.com/user-attachments/assets/d2470a0c-4d07-4084-965a-01522bcec48e>

## `todo`
(1단계)

- 현재 상황에서 원래 사용하던 airflow의 DB를 같이 사용하는 것이 아닌 따로 backend 컨테이너에 스프링 서버만을 위한 DB를 빌드해야 합니다!

(2단계)

- airflow : kafka 프로듀서 구현
- backend : kafka 컨슈머 구현

한대의 서버의 서로 다른 컨테이너에서 토픽 발행, 구독 구현
(이부분이 구현되면 서로 코드를 보면서 공부하면 좋을것 같습니다.)

(3단계)

서로다른 3대의 서버로 빌드 및 서버통신 구현\
아마 kafka를 구독하는 서로 다른 서버는 HTTP 프로토콜로 통신하지 않는다 했던것 같습니다 (RPC?) 이부분에 대해서 공부하면 좋을것 같습니다.
- 세부사항 미정

(4단계)

kafka 를 다른 서비스에 적용해보기




