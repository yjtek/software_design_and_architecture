public interface CoffeeMachineInterface {
	public void chooseFirstSelection();
	public void chooseSecondSelection();
}

public class OldCoffeeMachine {

	public void selectA() {
		System.out.println(“A - Selected”);
	}
	Public void selectB() {
		System.out.println(“B - Selected”);
	}
}

public class CoffeeTouchscreenAdapter implements CoffeeMachineInterface {

	OldCoffeeMachine theMachine;

	public CoffeeTouchscreenAdapter(OldCoffeeMachine newMachine) {
		theMachine = newMachine;
	}
	
	public void chooseFirstSelection() {
		theMachine.selectA();
	}
	
	public void chooseSecondSelection() {
		theMachine.selectB();
}
}
