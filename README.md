>* 개발 언어 : Kotlin
>* 프레임워크 : Springboot 3.2
>* RDBMS : MariaDB 10.5.26
***

# 데이터 설계
> 데이터는 크게 '강연 정보'와 '강연 신청 정보'로 나눈다.
> '강연 정보'에는 '강연장 정보'가 포함된다.
>* 강연 ``` LectureEntity ```
>  * 강연자, 강연장, 신청 인원, 강연 제목, 강연 내용, 강연 시작 시간, 강연 종료 시간, 생성 시간, 수정 시간
>* 강연 신청 내역 ``` ApplyEntity ```
>  * 사번, 강연 정보 (ID), 생성 시간
>* 강연과 강연 신청은 **"강연ID"**로 연결
***

# 고민 / 설명
## 고민
* 주어진 시간 안에 심플하게 작성하자.
* 엔티티간 관계 설정 잘못하면 성능 이슈, 관계는 약하고 가볍게 하는게 좋겠다.
* 하지만, 그래도 강연에는 강연장 정보를 넣어야겠다.
* Front 강연 목록, 신청 내역 조회, 실시간 인기 강연 -> 조건에 따른 목록 조회 한 가지로 사용할 수 있을까? (x, 별도 API)
  * 조회 조건 차이가 큼
* 강연 등록 (강연자, **강연장, 신청인원**, 강연 기간, 강연내용 입력)
  * 강연 / 강연장 entity 합치는 걸로


## 설명
* DB 초기호를 위해 jpa.hibernate.ddl-auto: create 설정
* 

* API mapping은 '/api'로 시작
* BackOffice 기능과 Front 기능 구분은 위해, '/api' 뒤에 BackOffice 기능은 '/back', Front 기능은 '/front' 추가
* 기능 실게 사용 염두하여 pageable 적용
  * BackOffice - 강연 목록 API
  * BackOffice - 강연 신청자 목록
  * Front - 강연 목록
  * Front - 신청 내역 조회
  * Front - 실시간 인기 강연



## BACK OFFICE API
* ### 강연 목록 조회 - [GET] /api/back/list
>* REQUEST SAMPLE
> ```json
> [GET] /api/back/list

>* RESPONSE SAMPLE
> ```json
> {"timestamp":"2024-10-01T14:52:21.2727128","data":{"content":[{"id":2,"speaker":"김창옥","roomName":"강연장1","capacity":10,"title":"포프리쇼1","description":"행복한 순간을 기억해주세요1","startTime":"2024-10-02T10:00:00","endTime":"2024-10-02T13:00:00","createTime":"2024-10-01T14:31:20.161914","updateTime":null},{"id":1,"speaker":"법륜스님","roomName":"강연장1","capacity":10,"title":"즉문즉설1","description":"생각보다 간단한 행복해지는 방법1","startTime":"2024-10-02T15:00:00","endTime":"2024-10-02T17:00:00","createTime":"2024-10-01T14:15:27.023214","updateTime":null}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalElements":2,"totalPages":1,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":2,"empty":false},"error":null}


* ### 강연 등록 - [POST] /api/back/register
>* REQUEST SAMPLE
> ```json
> [POST] /api/back/register
> 
> {"speaker": "법륜스님","roomName": "강연장1","capacity": 10,"title": "즉문즉설1","description": "생각보다 간단한 행복해지는 방법1","startTime": "2024-10-02 15:00:00","endTime": "2024-10-02 17:00:00"}

> * RESPONSE SAMPLE
> ```json
> { "timestamp": "2024-10-01T14:15:27.0714892", "data": { "id": 1, "speaker": "법륜스님", "roomName": "강연장1", "capacity": 10, "title": "즉문즉설1", "description": "생각보다 간단한 행복해지는 방법1", "startTime": "2024-10-02T15:00:00", "endTime": "2024-10-02T17:00:00", "createTime": "2024-10-01T14:15:27.0232144", "updateTime": null }, "error": null }


* ### 강연 신청자 목록 조회 - [GET] /api/back/{lectureId}/members
>* REQUEST SAMPLE
> ```json
> [GET] /api/back/1/members

>* RESPONSE SAMPLE
> ```json
> { "timestamp": "2024-10-01T14:17:28.4086423", "data": { "content": [ { "id": 1, "employeeId": "ABCDE", "lecture": { "id": 1, "speaker": "법륜스님", "roomName": "강연장1", "capacity": 10, "title": "즉문즉설1", "description": "생각보다 간단한 행복해지는 방법1", "startTime": "2024-10-02T15:00:00", "endTime": "2024-10-02T17:00:00", "createTime": "2024-10-01T14:15:27.023214", "updateTime": null }, "createTime": "2024-10-01T14:17:16" } ], "pageable": { "pageNumber": 0, "pageSize": 10, "sort": { "empty": false, "sorted": true, "unsorted": false }, "offset": 0, "paged": true, "unpaged": false }, "last": true, "totalElements": 1, "totalPages": 1, "size": 10, "number": 0, "sort": { "empty": false, "sorted": true, "unsorted": false }, "first": true, "numberOfElements": 1, "empty": false }, "error": null }



## FRONT API
* ### 강연 목록 조회 - [GET] /api/front/list
>* REQUEST SAMPLE
> ```json
> [GET] /api/front/list

> * RESPONSE SAMPLE
> ```json
> {"timestamp":"2024-10-01T14:31:50.361237","data":{"content":[{"id":2,"speaker":"김창옥","roomName":"강연장1","capacity":10,"title":"포프리쇼1","description":"행복한 순간을 기억해주세요1","startTime":"2024-10-02T10:00:00","endTime":"2024-10-02T13:00:00","createTime":"2024-10-01T14:31:20.161914","updateTime":null},{"id":1,"speaker":"법륜스님","roomName":"강연장1","capacity":10,"title":"즉문즉설1","description":"생각보다 간단한 행복해지는 방법1","startTime":"2024-10-02T15:00:00","endTime":"2024-10-02T17:00:00","createTime":"2024-10-01T14:15:27.023214","updateTime":null}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":1,"totalElements":2,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":2,"empty":false},"error":null}


* ### 강연 신청 - [POST] /api/front/apply
>* REQUEST SAMPLE
> ```json
> [POST] /api/front/apply
> 
> {"employeeId" : "ABCDE","lectureId" : 2}

> * RESPONSE SAMPLE
> ```json
> { "timestamp": "2024-10-01T14:32:22.048894", "data": { "id": 2, "employeeId": "ABCDE", "lecture": { "id": 2, "speaker": "김창옥", "roomName": "강연장1", "capacity": 10, "title": "포프리쇼1", "description": "행복한 순간을 기억해주세요1", "startTime": "2024-10-02T10:00:00", "endTime": "2024-10-02T13:00:00", "createTime": "2024-10-01T14:31:20.161914", "updateTime": null }, "createTime": "2024-10-01T14:32:22.0439003" }, "error": null }


* ### 신청 내역 조회 - [GET] /api/front/list/{employeeId}
>* REQUEST SAMPLE
> ```json
> [GET] /api/front/list/ABCDE

> * RESPONSE SAMPLE
> ```json
> {"timestamp":"2024-10-01T14:32:55.5558052","data":{"content":[{"id":2,"employeeId":"ABCDE","lecture":{"id":2,"speaker":"김창옥","roomName":"강연장1","capacity":10,"title":"포프리쇼1","description":"행복한 순간을 기억해주세요1","startTime":"2024-10-02T10:00:00","endTime":"2024-10-02T13:00:00","createTime":"2024-10-01T14:31:20.161914","updateTime":null},"createTime":"2024-10-01T14:32:22.0439"},{"id":1,"employeeId":"ABCDE","lecture":{"id":1,"speaker":"법륜스님","roomName":"강연장1","capacity":10,"title":"즉문즉설1","description":"생각보다 간단한 행복해지는 방법1","startTime":"2024-10-02T15:00:00","endTime":"2024-10-02T17:00:00","createTime":"2024-10-01T14:15:27.023214","updateTime":null},"createTime":"2024-10-01T14:17:16"}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":1,"totalElements":2,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":2,"empty":false},"error":null}


* ### 강연 신청 취소 - [DELETE] /api/front/apply/{applyId}/cancel
>* REQUEST SAMPLE
> ```json
> [DELETE] /api/front/apply/1/cancel

> * RESPONSE SAMPLE
> ```json
> { "timestamp": "2024-10-01T14:33:40.3120468", "data": null, "error": null }


* ### 실시간 인기 간연 조회 - [GET] /api/front/popular
>* REQUEST SAMPLE
> ```json
> [GET] /api/front/popular

> * RESPONSE SAMPLE
> ```json
> {"timestamp":"2024-10-01T14:33:58.2538387","data":{"content":[{"id":2,"speaker":"김창옥","roomName":"강연장1","capacity":10,"title":"포프리쇼1","description":"행복한 순간을 기억해주세요1","startTime":"2024-10-02T10:00:00","endTime":"2024-10-02T13:00:00","createTime":"2024-10-01T14:31:20.161914","updateTime":null}],"pageable":{"pageNumber":0,"pageSize":10,"sort":{"empty":false,"sorted":true,"unsorted":false},"offset":0,"paged":true,"unpaged":false},"last":true,"totalPages":1,"totalElements":1,"size":10,"number":0,"sort":{"empty":false,"sorted":true,"unsorted":false},"first":true,"numberOfElements":1,"empty":false},"error":null}