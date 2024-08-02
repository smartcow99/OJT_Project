package step12_ojt;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(name = "CARD_SEQ", sequenceName = "CARD_SEQ", initialValue = 1, allocationSize = 50)
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARD_SEQ")
	private Long id;

	@NonNull
	@Column(name = "card_number", nullable = false)
	private String cardNumber;

	@NonNull
	@Column(name = "card_owner_name", nullable = false)
	private String cardOwnerName;

	@NonNull
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@NonNull
	@Column(name = "cvv", nullable = false)
	private String cvv;
}