package ex91;

public class Queue {
	private Stack in; // Stack for adding elements
	private Stack out; // Stack for removing elements

	// Initializes the queue with two empty stacks
	public Queue(int maxSize) {
		in = new Stack(maxSize); // Stack for enqueueing
		out = new Stack(maxSize); // Stack for dequeueing
	}

	// Adds a new element x to the queue
	public void enqueue(int x) {
		in.push(x); // Push the element onto the in stack
	}

	// Removes the oldest element from the queue and returns it
	public double dequeue() {
		if (out.isEmpty()) { // Check if the out stack is empty
			while (!in.isEmpty()) { // Move all elements from in to out
				out.push(in.pop());
			}
		}
		return out.pop(); // Pop the top element from the out stack
	}

	// Returns the number of elements in the queue
	public int size() {
		return in.size() + out.size(); // Total size is the sum of both stacks
	}

	// Checks if the queue is empty
	public boolean isEmpty() {
		return in.isEmpty() && out.isEmpty(); // Queue is empty if both stacks are empty
	}
}
