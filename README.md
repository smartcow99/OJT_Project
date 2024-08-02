# OJT_Project
## 문제 1: 카드 결제 시스템 엔티티 모델링
### 카드 결제 시스템의 결제 정보를 관리할 수 있는 엔티티 클래스 작성
### 요구 사항: Card와 Transaction JPA 엔티티 클래스 작성
#### Card 속성
- id (Long, PK , auto)
- cardNumber (String, 카드 번호)
- carOwnerName (String, 카드 소유자 이름)
- endDate (LocalDate, 카드 만료일)
- cvv (String, 카드 CVV)

#### Transaction 속성

- id (Long, PK, auto)
- transDate (LocalDateTime, 거래 일시)
- transCost (long, 거래 금액)
- card (Card, ManyToOne 관계)
- 두 엔티티 간의 관계를 설정하고 및 연관된 엔티티를 fetch 설정.
