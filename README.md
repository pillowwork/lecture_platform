>* 개발 언어 : Kotlin
>* 프레임워크 : Springboot 3.2
>* RDBMS : MariaDB 10.5.26
***

### 데이터 설계
> 데이터는 크게 '강연 정보'와 '강연 신청 정보'로 나눈다.
> '강연 정보'에는 '강연장 정보'가 포함된다.
>* 강연 ``` LectureEntity ```
>* 강연장 ``` RoomEntity ```
>* 강연 신청 내역 ``` ApplyEntity ```
***

### 고민 / 설명
#### 고민
* 주어진 시간 안에 심플하게 작성하자.
* 엔티티간 관계 설정 잘못하면 성능 이슈, 관계는 약하고 가볍게 하는게 좋겠다.
* 하지만, 그래도 강연에는 강연장 정보를 넣어야겠다.
* Front 강연 목록, 신청 내역 조회, 실시간 인기 강연 -> 조건에 따른 목록 조회 한 가지로 사용할 수 있을까? (x, 별도 API)
  * 조회 조건 차이가 큼

#### 설명
* 강연장 정보는 정해져 있다고 가정
* API mapping은 '/api'로 시작
* BackOffice 기능과 Front 기능 구분은 위해, '/api' 뒤에 BackOffice 기능은 '/back', Front 기능은 '/front' 추가
* 기능 실게 사용 염두하여 pageable 적용
  * BackOffice - 강연 목록 API
  * BackOffice - 강연 신청자 목록
  * Front - 강연 목록
  * Front - 신청 내역 조회
  * Front - 실시간 인기 강연

#### BACK OFFICE API
* [GET] /api/back/list - 강연 목록 조회
>* RESPONSE SAMPLE
> ```json
> {"content":[{"id":3,"speaker":"법륜스님","title":"즉문즉설","description":"생각보다 간단한 행복해지는 방법","startTime":"2024-09-29T13:00:00","endTime":"2024-09-29T15:00:00","createTime":"2024-09-29T10:51:34.569524","updateTime":null,"roomId":3},{"id":2,"speaker":"김미경","title":"인생명언","description":"오늘 하루 괜찮았나요","startTime":"2024-09-29T13:00:00","endTime":"2024-09-29T15:00:00","createTime":"2024-09-29T10:51:16.150628","updateTime":null,"roomId":2}],"pageable":{"pageNumber":0,"pageSize":2,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":false,"totalElements":3,"totalPages":2,"size":2,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":2,"empty":false}

* [POST] /api/back/register - 강연 등록
>* REQUEST SAMPLE
>> ```json
>> {"speaker": "김창옥","title": "포프리쇼","description": "행복한 순간을 기억해주세요","startTime": "2024-09-29 13:00:00","endTime": "2024-09-29 15:00:00",}
>* RESPONSE SAMPLE
>> ```json
>> {"id":4,"speaker":"김창옥","title":"포프리쇼","description":"행복한 순간을 기억해주세요","startTime":"2024-09-29T13:00:00","endTime":"2024-09-29T15:00:00","createTime":"2024-09-29T11:59:17.7705606","updateTime":null,"roomId":1}

* [GET] /api/back/{lectureId}/members - 강연 신청자 목록 조회
>* RESPONSE SAMPLE

#### FRONT API
* 강연 목록 조회
* 강연 신청
* 신청 내역 조회
* 강연 신청 취소
* 실시간 인기 간연 조회
