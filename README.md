# nice_nice_house

## 2021 대경권 공공데이터 활용 경진 대회

![image](https://user-images.githubusercontent.com/33050476/165393095-35ad02e6-5181-47fa-b16e-377b876436b5.png)

## 서비스 기획 배경 및 소개

---

Are there any areas in the student's identity where public data can be used to improve the quality of life?

Is there any way to revive the economy that has been stagnated in the long term due to the corona virus?

Our idea came from these two questions.
It's a **"nice and nice house".**

"**Nice and Nice House**" is an app that introduces cheap but good quality and hygienic restaurants. (To be expanded to all industries in the future)

Introduce :
Just because the price is good doesn't mean you can trust everyone. It analyzes good prices and Google ratings as well as information on safe businesses to introduce businesses that can provide both cost-effectiveness and quality to customers.
You can expect to revitalize the local economy by introducing good businesses to the economy that is stagnating due to the covid19-virus.

## ✍️ 서비스 세부내용

---

### 🔗 서비스 시연 영상

[착하디 착한 집 - 나착한디(2021 대경권 공공데이터 활용 경진대회)-데모영상](https://youtu.be/_sa8WWzdTs0)

### 서비스 세부 기능

- Provides information on good quality businesses based on good prices, safe businesses, and ratings
    - By applying three reference points, only businesses with good quality are selected and provided with information
- Provides categorization function
    - By categorizing, you can view only the information about the desired item.
- Provides location-based services and directions
    1. Marking points using markers
    2. When a marker is clicked, related details are displayed
    3. Provide wayfinding function

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4f1bb37a-bbb0-47bd-982f-77d161b6b93b/Untitled.png)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b877de34-b930-46d9-a1af-1cfe0d809eac/Untitled.png)

## 🛠 사용 기술 및 라이브러리

- Java, Android
- PHP
- MySQL

## 🖥 담당한 기능 (Android, BE)

- 사용자가 설정한 알람에 시간이 가까워졌 때 사용자가 얕은 수면 상태라면 알람 울리기
- 스마트폰 마이크를 이용하여 소리를 기반으로 수면 패턴을 분석, 데이터 ETL
- 사용자 정보 통계 수치 계산 및 시각화
- AWS ec2 서버를 생성하여 PHP와 MySQL을 이용한 Back-End 개발
