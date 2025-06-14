package ex91;

public class Main {
	public static void main(String[] args) {
		Queue queue = new Queue(5); // Create a queue with a maximum size of 5

		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(3);

		System.out.println("Dequeued: " + queue.dequeue()); // Outputs: Dequeued: 1
		System.out.println("Size: " + queue.size()); // Outputs: Size: 2
		System.out.println("Is empty: " + queue.isEmpty()); // Outputs: Is empty: false
	}
}
