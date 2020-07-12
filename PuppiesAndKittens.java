class Pet {}

class CuteException extends Exception {
	public CuteException(String message) {
		super(message);
	}
}
class Kitten extends Pet {
	public void hug() throws CuteException {
		throw new CuteException("=-.-=");
	}
}
class Puppy extends Pet {
	public void hug() throws CuteException {
		throw new CuteException("(/-.-\\)");
	}
}
public class PuppiesAndKittens {
	public static void main(String[] args) {
		Pet[] basket = new Pet[10];
		for (int i = 0; i < 9; i++) {
			basket[i++] = new Puppy();
			basket[++i] = new Kitten();
		}
		func(basket);
	}	
	public static void func(Pet[] basket) {
		try {
			Kitten whiskers = (Kitten) basket[5];
			whiskers.hug();
		} catch (ClassCastException e) {
			System.out.println("Invalid Cast");
		} catch (NullPointerException e) {
			System.out.println("Null Pointer");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}