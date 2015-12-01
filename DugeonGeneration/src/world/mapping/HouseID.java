package world.mapping;

public enum HouseID implements ID {
	UpperLeftInnerCorner11, UpperLeftInnerCorner12, UpperRightInnerCorner11, UpperRightInnerCorner12, WallFront11, WallCenter, UpperLefttOuterCornerSpecial, UpperRightOuterCornerSpecial, empty1, UpperLeftInnerCorner21, UpperLeftInnerCorner22, UpperRightInnerCorner21, UpperRightInnerCorner22, WallFront21, UpperLeftOuterCorner11, UpperLeftOuterCorner12, UpperRightOuterCorner11, UpperRightOuterCorner12, UpperLeftInnerCorner31, UpperLeftInnerCorner32, UpperRightInnerCorner31, UpperRightInnerCorner32, WallFront31, UpperLeftOuterCorner21, UpperLeftOuterCorner22, UpperRightOuterCorner21, UpperRightOuterCorner22, LowerLeftInnerCorner11, LowerLeftInnerCorner12, LowerRightInnerCorner11, LowerRightInnerCorner12, WallBack11, LowerLeftOuterCorner11, LowerLeftOuterCorner12, LowerRightOuterCorner11, LowerRightOuterCorner12, LowerLeftInnerCornerSpecial11, LowerLeftInnerCornerSpecial12, LowerRightInnerCornerSpecial11, LowerRightInnerCornerSpecial12, WallBackSpecial, LowerLeftOuterCorner21, LowerLeftOuterCorner22, LowerRightOuterCorner21, LowerRightOuterCorner22, Floor11, Floor12, Floor13, Floor14, empty, LowerLeftOuterCorner31, LowerLeftOuterCorner32, LowerRightOuterCorner31, LowerRightOuterCorner32, empty2, Floor22, Floor23, Floor24, Floor25, WallLeft11, WallLeft12, WallRight11, WallRight12, empty3, Floor32, DoubleCornerLeft11, DoubleCornerLeft12, DoubleCornerRight11, DoubleCornerRight12, FloorExtra11, empty4, empty5, empty6, DoubleCornerLeft20, DoubleCornerLeft21, DoubleCornerLeft22, DoubleCornerRight21, DoubleCornerRight22, FloorExtra21, FloorShadow11, FloorShadow12;

	private HouseID correspondingID;

	static {
		for (HouseID id : values()) {
			switch (id) {
			case UpperLefttOuterCornerSpecial:
				id.correspondingID = UpperLeftOuterCorner12;
				break;
			case UpperRightOuterCornerSpecial:
				id.correspondingID = UpperRightOuterCorner11;
				break;
			case LowerLeftInnerCornerSpecial11:
				id.correspondingID = LowerLeftInnerCorner11;
				break;
			case LowerLeftInnerCornerSpecial12:
				id.correspondingID = LowerLeftInnerCorner12;
				break;
			case LowerRightInnerCornerSpecial11:
				id.correspondingID = LowerRightInnerCorner11;
				break;
			case LowerRightInnerCornerSpecial12:
				id.correspondingID = LowerRightInnerCorner12;
				break;
			case WallBackSpecial:
				id.correspondingID = WallBack11;
				break;
			default:
				id.correspondingID = null;
				break;
			}
		}
	}

	public HouseID getCorrespondingID() {
		return correspondingID;
	}
}
