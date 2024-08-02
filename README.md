# 🙊 OJT_Project	


## Team
|<img src="https://avatars.githubusercontent.com/u/65991884?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/107031994?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/86452494?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/78792358?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/74589010?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/175282913?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/127727927?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
|[@RyuChaeHyun](https://github.com/RyuChaeHyun)|Jeongju Park<br/>[@gorapang](https://github.com/gorapang)|[@seungji2001](https://github.com/seungji2001)|HyeonWoo Park<br/>[@smartcow99](https://github.com/smartcow99)|강유완<br/>[@yuwankang](https://github.com/yuwankang)|이정욱<br/>[@jeonguk0201](https://github.com/jeonguk0201)|부준혁<br/>[@BooJunhyuk](https://github.com/BooJunhyuk)|
 	
## 🖋 문제 1: 카드 결제 시스템 엔티티 모델링
	카드 결제 시스템의 결제 정보를 관리할 수 있는 엔티티 클래스 작성
#### > 요구 사항: Card와 Transaction JPA 엔티티 클래스 작성
#### 1. Card 속성
- id (Long, PK , auto)
- cardNumber (String, 카드 번호)
- carOwnerName (String, 카드 소유자 이름)
- endDate (LocalDate, 카드 만료일)
- cvv (String, 카드 CVV)

#### 2. Transaction 속성

- id (Long, PK, auto)
- transDate (LocalDateTime, 거래 일시)
- transCost (long, 거래 금액)
- card (Card, ManyToOne 관계)
- 두 엔티티 간의 관계를 설정하고 및 연관된 엔티티를 fetch 설정.

## 🖋 문제 2: SQL -> JPQL 쿼리 변경하기
### 데이터 베이스 환경
![image](https://github.com/user-attachments/assets/3991de01-04c2-498d-ad19-ba573593b20e)
![image](https://github.com/user-attachments/assets/1fb5455d-20a6-4dfc-a12e-e35fe8fe1b41)

### SQL 쿼리 
```
SELECT * from emp e , dept d where ename = 'SMITH' AND e.deptno = d.deptno;
```

## 🖋 문제 3 : select문이 호출되는 문장의 번호 선택
### step02Test() 호출 시 실제로 데이터베이스에 select문이 호출되는 문장의 번호를 모두 선택하세요.
단, Emp 클래스의 멤버 변수 DEPTNO는 아래와 같이 설정되어 있습니다.

### Emp.java

```java
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DEPTNO")
    private Dept DEPTNO;
```

### step02RunTest.java
```java
	@Test
	public void step02Test() {
	    EntityManager em = DBUtil.getEntityManager();
	    try {
	    	
	        Emp employee = em.find(Emp.class, 7839L); //1 select
	        
	        System.out.println(1);
	        
	        System.out.println(employee.getENAME()); //KING
	        
	        employee = em.find(Emp.class, 7839L); //2 no select 
	        
	        System.out.println(2);
	        
            TypedQuery<Emp> query = em.createQuery("SELECT e FROM Emp e WHERE e.ENAME = :name", Emp.class);
            query.setParameter("name", "KING");
            List<Emp> employees = query.getResultList(); //3 select
            
            System.out.println(employees.get(0).getENAME()); // 4
            
            
            employee = em.find(Emp.class, 7839L); //5 no select 
            System.out.println(employee.getENAME()); // 6
	        

	        System.out.println(employee.getDEPTNO().getLOC()); //7 select
	        
	    } finally {
	        if (em != null) {
	            em.close();
	            em = null;
	        }
	    }
	}

```



### 정답: 1, 3, 7
## ☘ 해설
해설은 주석에 설명되어 있습니다.
```java
@Test
public void step02Test() {
    EntityManager em = DBUtil.getEntityManager();
    try {
        
        Emp employee = em.find(Emp.class, 7839L); //1 select
        // 이 줄은 데이터베이스에서 `EMPNO`가 7839인 `Emp` 엔티티를 검색합니다.
        
        System.out.println(1);
        
        System.out.println(employee.getENAME()); //KING
        // 캐시된 `employee` 객체에서 `ENAME` 값을 출력합니다. 추가 쿼리 없음.
        
        employee = em.find(Emp.class, 7839L); //2 no select
        // 이미 영속성 컨텍스트에 존재하는 엔티티이므로 추가 쿼리 없음.
        
        System.out.println(2);
        
        TypedQuery<Emp> query = em.createQuery("SELECT e FROM Emp e WHERE e.ENAME = :name", Emp.class);
        query.setParameter("name", "KING");
        List<Emp> employees = query.getResultList(); //3 select
        // 이름이 "KING"인 `Emp` 엔티티를 검색하는 쿼리가 실행됩니다.
        
        System.out.println(employees.get(0).getENAME()); // 4
        // 캐시된 `employees` 리스트에서 첫 번째 엔티티의 `ENAME` 값을 출력합니다. 추가 쿼리 없음.
        
        employee = em.find(Emp.class, 7839L); //5 no select
        // 이미 영속성 컨텍스트에 존재하는 엔티티이므로 추가 쿼리 없음.
        
        System.out.println(employee.getENAME()); // 6
        // 캐시된 `employee` 객체에서 `ENAME` 값을 출력합니다. 추가 쿼리 없음.
        
        System.out.println(employee.getDEPTNO().getLOC()); //7 select
        // `FetchType.LAZY`로 설정된 `DEPTNO`의 `LOC` 값을 가져오기 위해 추가 쿼리가 실행됩니다.
        
    } finally {
        if (em != null) {
            em.close();
            em = null;
        }
    }
}

```



## 🖋 문제 3 : JPQL 쿼리 조회 횟수 
### step02Test() 호출 시 Dept 엔티티를 조회하는 JPQL 쿼리가 몇 번 호출되는지 횟수를 작성하시오.
Dept와 EMP 는 이전 코드와 동일합니다.
```java
@Test
	public void step02Test() {
	    EntityManager em = null;
	    EntityTransaction tx = null;
	
	    try {
	        em = DBUtil.getEntityManager();
	        tx = em.getTransaction();
	        tx.begin();
	
	        // dept 엔티티에서 dname이 "test1"인 레코드 하나만 조회
	        String dname = "test1"; //1
	        Dept result = em.createQuery("SELECT d FROM Dept d WHERE d.DNAME = :dname", Dept.class)
	                        .setParameter("dname", dname)
	                        .getSingleResult(); // (1)
	
	        // 조회 결과 출력
	        System.out.println("Dept ID: " + result.getDEPTNO() + ", Dname: " + result.getDNAME() + ", Location: " + result.getLOC());

	        String dname2 = "test1"; //1
	        Dept result2 = em.createQuery("SELECT d FROM Dept d WHERE d.DNAME = :dname2", Dept.class)
	                        .setParameter("dname2", dname)
	                        .getSingleResult(); //(2)
 
	        // 조회 결과 출력
	        System.out.println("Dept ID: " + result2.getDEPTNO() + ", Dname: " + result2.getDNAME() + ", Location: " + result2.getLOC());

	        String dname3 = "test1"; //1
	        Dept result3 = em.createQuery("SELECT d FROM Dept d WHERE d.DNAME = :dname3", Dept.class)
	                        .setParameter("dname3", dname)
	                        .getSingleResult(); //(3)
	        
	        // 조회 결과 출력
	        System.out.println("Dept ID: " + result3.getDEPTNO() + ", Dname: " + result3.getDNAME() + ", Location: " + result3.getLOC());
	        tx.commit();
	    } catch (Exception e) {
	        if (tx != null && tx.isActive()) {
	            tx.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        if (em != null) {
	            em.close();
	        }
	    }
	}

```


#### 정답: 3개
## ☘ 해설

1. 쿼리 호출 (1): 데이터베이스에서 처음으로 dname이 "test1"인 Dept를 조회하고, 이 결과는 **1차 캐시**에 저장된다.

2. 쿼리 호출 (2): 동일한 dname 값을 가진 Dept를 다시 조회한다.

3. 쿼리 호출 (3): 동일한 dname 값을 가진 Dept를 다시 조회한다.

이 코드는 JPQL 쿼리 방식으로 Dept를 조회하고 있다. **1차 캐시는 동일한 식별자(식별자 기반 조회)로 조회할 때만 유효**하므로, 식별자가 아닌 다른 속성으로 조회할 경우 JPQL 쿼리가 데이터베이스에 직접 실행되기 때문에 **3번의 쿼리 호출** 이 일어난다.

**(1) 에서 Dept가 호출되었으니 1차캐시에 저장되어 (2) , (3) 에선 1차 캐시를 조회하기 때문에 한번만 호출될거라고 생각할 수 있지만 1차 캐시를 사용하는데에는 식별자(id) 로 조회하는 경우에만 해당된다는 특징**이 있다. 하지만 영속성 컨텍스트는 내부적으로 식별자로 Entity를 관리하기 때문에 영속상태인 객체는 식별자가 반드시 있어야 한다.

위의 (2) , (3) 처럼 **query methods 방식**을 사용하게되면 영속성 컨텍스트가 관리하는 식별자로 값을 찾는게 아닌 JPQL로 쿼리가 나가기 때문에 1차캐시에서 데이터를 불러오지 못한다.

#### 실행되는 SQL 문장

![image](https://github.com/user-attachments/assets/bd2e972c-ce22-4f31-9e02-cae3ca111780)

---

