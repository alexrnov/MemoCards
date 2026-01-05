package alexrnov.memocards.cards

fun setComposition(cards: Map<Int, Card>, portrait: Boolean) {
	when (cards.size) {
		12 -> setCompositionFor12Cards(cards, portrait)
		16 -> setCompositionFor16Cards(cards, portrait)
		20 -> setCompositionFor20Cards(cards, portrait)
		30 -> setCompositionFor30Cards(cards, portrait)
	}
}

private fun setCompositionFor12Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.05f, 2.76f, 0.0f, 45.0f)
		cards.getValue(1).position(0f, 2.76f, 0f, 45.0f)
		cards.getValue(2).position(1.05f, 2.76f, 0.0f, 45.0f)

		// второй ряд
		cards.getValue(3).position(-1.05f, 0.92f, 0.0f, 45.0f)
		cards.getValue(4).position(0f, 0.92f, 0f, 45.0f)
		cards.getValue(5).position(1.05f, 0.92f, 0.0f, 45.0f)

		// третий ряд
		cards.getValue(6).position(-1.05f, -0.92f, 0.0f, 45.0f)
		cards.getValue(7).position(0f, -0.92f, 0.0f, 45.0f)
		cards.getValue(8).position(1.05f, -0.92f, 0.0f, 45.0f)

		// четвертый ряд
		cards.getValue(9).position(-1.05f, -2.76f, 0.0f, 45.0f)
		cards.getValue(10).position(0f, -2.76f, 0f, 45.0f)
		cards.getValue(11).position(1.05f, -2.76f, 0.0f, 45.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-2.65f, 0.80f, 0.0f, 45.0f)
		cards.getValue(1).position(-1.59f, 0.80f, 0f, 45.0f)
		cards.getValue(2).position(-0.53f, 0.80f, 0.0f, 45.0f)
		cards.getValue(3).position(0.53f, 0.80f, 0.0f, 45.0f)
		cards.getValue(4).position(1.59f, 0.80f, 0f, 45.0f)
		cards.getValue(5).position(2.65f, 0.80f, 0.0f, 45.0f)

		// второй ряд
		cards.getValue(6).position(-2.65f, -1.04f, 0.0f, 45.0f)
		cards.getValue(7).position(-1.59f, -1.04f, 0f, 45.0f)
		cards.getValue(8).position(-0.53f, -1.04f, 0.0f, 45.0f)
		cards.getValue(9).position(0.53f, -1.04f, 0.0f, 45.0f)
		cards.getValue(10).position(1.59f, -1.04f, 0f, 45.0f)
		cards.getValue(11).position(2.65f, -1.04f, 0.0f, 45.0f)
	}
}

private fun setCompositionFor16Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.56f, 2.76f, 0.0f, 45.0f)
		cards.getValue(1).position(-0.52f, 2.76f, 0f, 45.0f)
		cards.getValue(2).position(0.52f, 2.76f, 0.0f, 45.0f)
		cards.getValue(3).position(1.56f, 2.76f, 0.0f, 45.0f)

		// второй ряд
		cards.getValue(4).position(-1.56f, 0.92f, 0f, 45.0f)
		cards.getValue(5).position(-0.52f, 0.92f, 0.0f, 45.0f)
		cards.getValue(6).position(0.52f, 0.92f, 0.0f, 45.0f)
		cards.getValue(7).position(1.56f, 0.92f, 0.0f, 45.0f)

		// третий ряд
		cards.getValue(8).position(-1.56f, -0.92f, 0.0f, 45.0f)
		cards.getValue(9).position(-0.52f, -0.92f, 0.0f, 45.0f)
		cards.getValue(10).position(0.52f, -0.92f, 0f, 45.0f)
		cards.getValue(11).position(1.56f, -0.92f, 0.0f, 45.0f)

		// четвертый ряд
		cards.getValue(12).position(-1.56f, -2.76f, 0.0f, 45.0f)
		cards.getValue(13).position(-0.52f, -2.76f, 0f, 45.0f)
		cards.getValue(14).position(0.52f, -2.76f, 0.0f, 45.0f)
		cards.getValue(15).position(1.56f, -2.76f, 0.0f, 45.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-3.84f, 0.80f, 0.0f, 45.0f)
		cards.getValue(1).position(-2.78f, 0.80f, 0.0f, 45.0f)
		cards.getValue(2).position(-1.72f, 0.80f, 0f, 45.0f)
		cards.getValue(3).position(-0.66f, 0.80f, 0.0f, 45.0f)
		cards.getValue(4).position(0.40f, 0.80f, 0.0f, 45.0f)
		cards.getValue(5).position(1.46f, 0.80f, 0f, 45.0f)
		cards.getValue(6).position(2.50f, 0.80f, 0.0f, 45.0f)
		cards.getValue(7).position(3.56f, 0.80f, 0f, 45.0f)

		// второй ряд
		cards.getValue(8).position(-3.84f, -1.04f, 0.0f, 45.0f)
		cards.getValue(9).position(-2.78f, -1.04f, 0.0f, 45.0f)
		cards.getValue(10).position(-1.72f, -1.04f, 0f, 45.0f)
		cards.getValue(11).position(-0.66f, -1.04f, 0.0f, 45.0f)
		cards.getValue(12).position(0.40f, -1.04f, 0.0f, 45.0f)
		cards.getValue(13).position(1.46f, -1.04f, 0.0f, 45.0f)
		cards.getValue(14).position(2.50f, -1.04f, 0f, 45.0f)
		cards.getValue(15).position(3.56f, -1.04f, 0.0f, 45.0f)
	}
}

private fun setCompositionFor20Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.56f, 3.66f, 0.0f, 45.0f)
		cards.getValue(1).position(-0.52f, 3.66f, 0f, 45.0f)
		cards.getValue(2).position(0.52f, 3.66f, 0.0f, 45.0f)
		cards.getValue(3).position(1.56f, 3.66f, 0.0f, 45.0f)

		// второй ряд
		cards.getValue(4).position(-1.56f, 1.82f, 0f, 45.0f)
		cards.getValue(5).position(-0.52f, 1.82f, 0.0f, 45.0f)
		cards.getValue(6).position(0.52f, 1.82f, 0.0f, 45.0f)
		cards.getValue(7).position(1.56f, 1.82f, 0.0f, 45.0f)

		// третий ряд
		cards.getValue(8).position(-1.56f, -0.02f, 0.0f, 45.0f)
		cards.getValue(9).position(-0.52f, -0.02f, 0.0f, 45.0f)
		cards.getValue(10).position(0.52f, -0.02f, 0f, 45.0f)
		cards.getValue(11).position(1.56f, -0.02f, 0.0f, 45.0f)

		// четвертый ряд
		cards.getValue(12).position(-1.56f, -1.86f, 0.0f, 45.0f)
		cards.getValue(13).position(-0.52f, -1.86f, 0f, 45.0f)
		cards.getValue(14).position(0.52f, -1.86f, 0.0f, 45.0f)
		cards.getValue(15).position(1.56f, -1.86f, 0.0f, 45.0f)

		cards.getValue(16).position(-1.56f, -3.7f, 0.0f, 45.0f)
		cards.getValue(17).position(-0.52f, -3.7f, 0f, 45.0f)
		cards.getValue(18).position(0.52f, -3.7f, 0.0f, 45.0f)
		cards.getValue(19).position(1.56f, -3.7f, 0.0f, 45.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-4.96f, 0.80f, 0.0f, 45.0f)
		cards.getValue(1).position(-3.90f, 0.80f, 0.0f, 45.0f)
		cards.getValue(2).position(-2.84f, 0.80f, 0.0f, 45.0f)
		cards.getValue(3).position(-1.78f, 0.80f, 0f, 45.0f)
		cards.getValue(4).position(-0.72f, 0.80f, 0.0f, 45.0f)
		cards.getValue(5).position(0.34f, 0.80f, 0.0f, 45.0f)
		cards.getValue(6).position(1.40f, 0.80f, 0f, 45.0f)
		cards.getValue(7).position(2.44f, 0.80f, 0.0f, 45.0f)
		cards.getValue(8).position(3.50f, 0.80f, 0f, 45.0f)
		cards.getValue(9).position(4.56f, 0.80f, 0f, 45.0f)

		// второй ряд
		cards.getValue(10).position(-4.96f, -1.04f, 0.0f, 45.0f)
		cards.getValue(11).position(-3.90f, -1.04f, 0.0f, 45.0f)
		cards.getValue(12).position(-2.84f, -1.04f, 0.0f, 45.0f)
		cards.getValue(13).position(-1.78f, -1.04f, 0f, 45.0f)
		cards.getValue(14).position(-0.72f, -1.04f, 0.0f, 45.0f)
		cards.getValue(15).position(0.34f, -1.04f, 0.0f, 45.0f)
		cards.getValue(16).position(1.40f, -1.04f, 0.0f, 45.0f)
		cards.getValue(17).position(2.44f, -1.04f, 0f, 45.0f)
		cards.getValue(18).position(3.50f, -1.04f, 0.0f, 45.0f)
		cards.getValue(19).position(4.56f, -1.04f, 0f, 45.0f)
	}
}

private fun setCompositionFor30Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-2.08f, 4.66f, 0.0f, 45.0f)
		cards.getValue(1).position(-1.04f, 4.66f, 0f, 45.0f)
		cards.getValue(2).position(0f, 4.66f, 0.0f, 45.0f)
		cards.getValue(3).position(1.04f, 4.66f, 0.0f, 45.0f)
		cards.getValue(4).position(2.08f, 4.66f, 0f, 45.0f)

		// второй ряд
		cards.getValue(5).position(-2.08f, 2.82f, 0f, 45.0f)
		cards.getValue(6).position(-1.04f, 2.82f, 0.0f, 45.0f)
		cards.getValue(7).position(0f, 2.82f, 0.0f, 45.0f)
		cards.getValue(8).position(1.04f, 2.82f, 0.0f, 45.0f)
		cards.getValue(9).position(2.08f, 2.82f, 0.0f, 45.0f)

		// третий ряд
		cards.getValue(10).position(-2.08f, 0.98f, 0.0f, 45.0f)
		cards.getValue(11).position(-1.04f, 0.98f, 0.0f, 45.0f)
		cards.getValue(12).position(0f, 0.98f, 0f, 45.0f)
		cards.getValue(13).position(1.04f, 0.98f, 0.0f, 45.0f)
		cards.getValue(14).position(2.08f, 0.98f, 0.0f, 45.0f)

		// четвертый ряд
		cards.getValue(15).position(-2.08f, -0.86f, 0.0f, 45.0f)
		cards.getValue(16).position(-1.04f, -0.86f, 0f, 45.0f)
		cards.getValue(17).position(0f, -0.86f, 0.0f, 45.0f)
		cards.getValue(18).position(1.04f, -0.86f, 0.0f, 45.0f)
		cards.getValue(19).position(2.08f, -0.86f, 0.0f, 45.0f)

		cards.getValue(20).position(-2.08f, -2.7f, 0.0f, 45.0f)
		cards.getValue(21).position(-1.04f, -2.7f, 0f, 45.0f)
		cards.getValue(22).position(0f, -2.7f, 0.0f, 45.0f)
		cards.getValue(23).position(1.04f, -2.7f, 0.0f, 45.0f)
		cards.getValue(24).position(2.08f, -2.7f, 0.0f, 45.0f)

		cards.getValue(25).position(-2.08f, -4.54f, 0.0f, 45.0f)
		cards.getValue(26).position(-1.04f, -4.54f, 0f, 45.0f)
		cards.getValue(27).position(0f, -4.54f, 0.0f, 45.0f)
		cards.getValue(28).position(1.04f, -4.54f, 0.0f, 45.0f)
		cards.getValue(29).position(2.08f, -4.54f, 0.0f, 45.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-4.96f, 1.60f, 0.0f, 45.0f)
		cards.getValue(1).position(-3.90f, 1.60f, 0.0f, 45.0f)
		cards.getValue(2).position(-2.84f, 1.60f, 0.0f, 45.0f)
		cards.getValue(3).position(-1.78f, 1.60f, 0f, 45.0f)
		cards.getValue(4).position(-0.72f, 1.60f, 0.0f, 45.0f)
		cards.getValue(5).position(0.34f, 1.60f, 0.0f, 45.0f)
		cards.getValue(6).position(1.40f, 1.60f, 0f, 45.0f)
		cards.getValue(7).position(2.44f, 1.60f, 0.0f, 45.0f)
		cards.getValue(8).position(3.50f, 1.60f, 0f, 45.0f)
		cards.getValue(9).position(4.56f, 1.60f, 0f, 45.0f)

		// второй ряд
		cards.getValue(10).position(-4.96f, -0.24f, 0.0f, 45.0f)
		cards.getValue(11).position(-3.90f, -0.24f, 0.0f, 45.0f)
		cards.getValue(12).position(-2.84f, -0.24f, 0.0f, 45.0f)
		cards.getValue(13).position(-1.78f, -0.24f, 0f, 45.0f)
		cards.getValue(14).position(-0.72f, -0.24f, 0.0f, 45.0f)
		cards.getValue(15).position(0.34f, -0.24f, 0.0f, 45.0f)
		cards.getValue(16).position(1.40f, -0.24f, 0.0f, 45.0f)
		cards.getValue(17).position(2.44f, -0.24f, 0f, 45.0f)
		cards.getValue(18).position(3.50f, -0.24f, 0.0f, 45.0f)
		cards.getValue(19).position(4.56f, -0.24f, 0f, 45.0f)

		// третий ряд
		cards.getValue(20).position(-4.96f, -2.08f, 0.0f, 45.0f)
		cards.getValue(21).position(-3.90f, -2.08f, 0.0f, 45.0f)
		cards.getValue(22).position(-2.84f, -2.08f, 0.0f, 45.0f)
		cards.getValue(23).position(-1.78f, -2.08f, 0f, 45.0f)
		cards.getValue(24).position(-0.72f, -2.08f, 0.0f, 45.0f)
		cards.getValue(25).position(0.34f, -2.08f, 0.0f, 45.0f)
		cards.getValue(26).position(1.40f, -2.08f, 0.0f, 45.0f)
		cards.getValue(27).position(2.44f, -2.08f, 0f, 45.0f)
		cards.getValue(28).position(3.50f, -2.08f, 0.0f, 45.0f)
		cards.getValue(29).position(4.56f, -2.08f, 0f, 45.0f)
	}
}
