package ex102;

import java.util.ArrayList;

class MaxHeap {
	private ArrayList<Integer> heap;

	// Constructor to initialize an empty MaxHeap
	public MaxHeap() {
		this.heap = new ArrayList<>(); // O(1)
	}

	// Method to insert a new element x into the MaxHeap
	public void insert(int x) {
		heap.add(x); // O(1)
		heapifyUp(heap.size() - 1); // O(log n)
	}

	// Method to find the maximum element without removing it
	public Integer findMaximum() {
		if (heap.isEmpty()) {
			return null; // Heap is empty
		}
		return heap.get(0); // O(1)
	}

	// Method to extract and remove the maximum element from the MaxHeap
	public Integer extractMaximum() {
		if (heap.isEmpty()) {
			return null; // Heap is empty
		}
		int maxValue = heap.get(0);
		int lastValue = heap.remove(heap.size() - 1); // Remove the last element

		if (!heap.isEmpty()) {
			heap.set(0, lastValue); // Set the last element as the new root
			heapifyDown(0); // O(log n)
		}
		return maxValue;
	}

	// Helper method to maintain the heap property after insertion
	private void heapifyUp(int index) {
		int parentIndex = (index - 1) / 2;
		if (index > 0 && heap.get(index) > heap.get(parentIndex)) {
			// Swap the current node with its parent
			swap(index, parentIndex);
			heapifyUp(parentIndex);
		}
	}

	// Helper method to maintain the heap property after extraction
	private void heapifyDown(int index) {
		int largest = index;
		int leftIndex = 2 * index + 1;
		int rightIndex = 2 * index + 2;

		if (leftIndex < heap.size() && heap.get(leftIndex) > heap.get(largest)) {
			largest = leftIndex;
		}
		if (rightIndex < heap.size() && heap.get(rightIndex) > heap.get(largest)) {
			largest = rightIndex;
		}

		if (largest != index) {
			// Swap the current node with the largest child
			swap(index, largest);
			heapifyDown(largest);
		}
	}

	// Helper method to swap two elements in the heap
	private void swap(int i, int j) {
		int temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}
	
    private int leftChild(int pos) { return (2 * pos) + 1; }

    private int rightChild(int pos)
    {
        return (2 * pos) + 2;
    }
    
    public void print()
    {
        for (int i = 0; i < heap.size() / 2; i++) {

            System.out.print("Node: " + heap.get(i));

            if (leftChild(i) < heap.size()) // if the child is out of the bound
                        // of the array
                System.out.print(", Left Child: "
                                 + heap.get(leftChild(i)));

            if (rightChild(i) < heap.size()) // the right child index must not
                        // be out of the index of the array
                System.out.print(", Right Child: "
                                 + heap.get(rightChild(i)));

            System.out.println(); // for new line
        }
    }
    
	public static void main(String[] args) {
		MaxHeap tree = new MaxHeap();
		// a
		tree.insert(38);
		tree.insert(17);
		tree.insert(3);
		tree.insert(2);
		tree.insert(9);
		tree.insert(15);
		tree.insert(11);
		
		tree.print();
		
		System.out.println("Max: " + tree.findMaximum().toString());
		
		System.out.println("ArrayList contents: " + tree.heap);
		
		System.out.println("Max: " + tree.extractMaximum().toString());
		
		System.out.println("ArrayList contents: " + tree.heap);
		tree.print();
	}
}
