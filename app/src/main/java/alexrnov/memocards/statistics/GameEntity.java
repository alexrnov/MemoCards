package alexrnov.memocards.statistics;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GameEntity {
	@PrimaryKey
	public int id;

	@ColumnInfo(name = "date")
	public String date;

	@ColumnInfo(name = "cards_quantity")
	public int cardsQuantity;

	@ColumnInfo(name = "errors")
	public int errors;

	public GameEntity(int id, String date, int cardsQuantity, int errors) {
		this.id = id;
		this.date = date;
		this.cardsQuantity = cardsQuantity;
		this.errors = errors;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) return true;
		if (otherObject == null) return false;
		if (getClass() != otherObject.getClass()) return false;

		GameEntity otherGame = (GameEntity) otherObject;

		return (id == otherGame.id)
			&& (date.equals(otherGame.date)
			&& (cardsQuantity == otherGame.cardsQuantity)
			&& (errors == otherGame.cardsQuantity));
	}
}
