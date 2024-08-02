package step12_ojt;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Test;

import util.DBUtil;

public class CardTest {

	@Test
	public void cardTest() {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = DBUtil.getEntityManager();
			tx = em.getTransaction();
			tx.begin();

			Card card1 = new Card("1234567890123456", "John Doe", LocalDate.of(2025, 12, 31), "123");
			em.persist(card1);

			// Create a transaction and associate it with the card
			Transaction transaction1 = new Transaction(LocalDateTime.now(), 5000L, card1);
			em.persist(transaction1);

			tx.commit();
			printTransactions(em);
			updateCardOwnerName(em);
			deleteCardAndTransactions(em);

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

	private void printTransactions(EntityManager em) {
		List<Transaction> transactions = em.createQuery("SELECT t FROM Transaction t JOIN t.card c", Transaction.class)
				.getResultList();
		int index = 1;

		for (Transaction trans : transactions) {
			System.out.println("============== 거래 정보 [" + index + "] ==============");
			System.out.println("거래 ID: " + trans.getId());
			System.out.println("거래 일시: " + trans.getTransDate());
			System.out.println("거래 금액: " + trans.getTransCost());
			System.out.println("-------------- 카드 정보 --------------");
			if (trans.getCard() != null) {
				System.out.println("카드 ID: " + trans.getCard().getId());
				System.out.println("카드 번호: " + trans.getCard().getCardNumber());
				System.out.println("카드 소유자 이름: " + trans.getCard().getCardOwnerName());
				System.out.println("카드 만료일: " + trans.getCard().getEndDate());
				System.out.println("카드 CVV: " + trans.getCard().getCvv());
			} else {
				System.out.println("연관된 카드 정보가 없습니다.");
			}
			System.out.println("======================================\n");
			index++;
		}
	}

	private void updateCardOwnerName(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		List<Card> cards = em.createQuery("SELECT c FROM Card c WHERE c.cardNumber = '1234567890123456'", Card.class)
				.getResultList();

		System.out.println("====== 카드 소유자 이름 업데이트 시작 ======");
		for (Card card : cards) {
			if (card != null) {
				String oldName = card.getCardOwnerName();
				card.setCardOwnerName("Jane Doe");
				System.out.println("카드 번호: " + card.getCardNumber());
				System.out.println("소유자 이름이 " + oldName + "에서 " + card.getCardOwnerName() + "(으)로 업데이트되었습니다.");
				System.out.println("-----------------------------------");
			}
		}
		System.out.println("====== 카드 소유자 이름 업데이트 완료 ======\n");
		tx.commit();
	}

	private void deleteCardAndTransactions(EntityManager em) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		List<Card> cards = em.createQuery("SELECT c FROM Card c WHERE c.cardNumber = '1234567890123456'", Card.class)
				.getResultList();

		System.out.println("====== 카드 및 관련 거래 삭제 시작 ======");
		for (Card card : cards) {
			if (card != null) {
				// 카드에 속한 거래들의 관계를 먼저 제거
				List<Transaction> transactionsForCard = em
						.createQuery("SELECT t FROM Transaction t WHERE t.card = :card", Transaction.class)
						.setParameter("card", card).getResultList();

				for (Transaction trans : transactionsForCard) {
					em.remove(trans);
				}

				System.out.println("삭제할 카드 번호: " + card.getCardNumber());
				System.out.println("카드 소유자 이름: " + card.getCardOwnerName());
				System.out.println("카드 만료일: " + card.getEndDate());

				em.remove(card);
				System.out.println("카드 번호 " + card.getCardNumber() + "가 성공적으로 삭제되었습니다.");
				System.out.println("-----------------------------------");
			}
		}
		System.out.println("====== 카드 및 관련 거래 삭제 완료 ======\n");
		tx.commit();
	}
}