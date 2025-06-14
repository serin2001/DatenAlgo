package ex91;

public class Stack {
	private double[] array; // Array to store stack elements
	private int top; // Pointer to the top element
	private int maxSize; // Maximum size of the stack

	// Initializes the stack with a fixed size
	public Stack(int size) {
		maxSize = size;
		array = new double[maxSize];
		top = -1; // Stack is initially empty
	}

	// Adds an element x to the top of the stack
	public void push(double x) {
		if (top + 1 < maxSize) { // Check if the stack is not full
			top++;
			array[top] = x; // Add the element
		} else {
			throw new RuntimeException("Stack Overflow"); // Error if the stack is full
		}
	}

	// Removes the top element of the stack and returns it
	public double pop() {
		if (top >= 0) { // Check if the stack is not empty
			double value = array[top];
			top--; // Decrease the pointer
			return value; // Return the element
		} else {
			throw new RuntimeException("Stack Underflow"); // Error if the stack is empty
		}
	}

	// Returns the top element of the stack without removing it
	public double peek() {
		if (top >= 0) {
			return array[top]; // Return the top element
		} else {
			throw new RuntimeException("Stack is empty"); // Error if the stack is empty
		}
	}

	// Checks if the stack is empty
	public boolean isEmpty() {
		return top == -1; // Returns true if the stack is empty
	}

	// Returns the number of elements in the stack
	public int size() {
		return top + 1; // The number of elements is the pointer + 1
	}
}
