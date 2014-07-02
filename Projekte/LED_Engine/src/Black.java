import ledControl.BoardController;

public class Black {
	public static void main(String[] args) {
		BoardController controller = BoardController.getBoardController();
		controller.addNetWorkHost("192.168.69.11");
		for (int x = 0; x < 12; x++)
			for (int y = 0; y < 12; y++)
				controller.setColor(x, y, 0, 0, 0);

		controller.updateLedStripe();
	}
}