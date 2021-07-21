import java.util.Scanner;

/**
 *  A class that represents skip lists. Will have O(log n) number of layers.
 * @author nikhi
 *
 */
public class SkipList {

	//Represents the sentinal head for the topmost layer.
	private Node sentinalHead;
	private Node sentinalTail;
	private Node bottomLevelHead;
	private Node bottomLevelTail; 
	
	/**
	 * A class that represents the node for a skip list.
	 * @author nikhi
	 *
	 */
	class Node{
		int value;
		Node top;
		Node down;
		Node next;
		Node previous;
		
		public Node(int value) {
			this.value = value;
			this.top= null;
			this.down = null;
			this.next = null;
			this.previous = null;
		}
	}
	
	/**
	 * Intialize the lists. Make the connections for all the sentinal nodes (as all sentinal nodes are to be connected). 
	 */
	public SkipList() {
		this.sentinalHead = new Node(Integer.MIN_VALUE);
		this.sentinalTail = new Node(Integer.MAX_VALUE);
		sentinalHead.next = sentinalTail;
		
		//Store the reference to the bottom level.
		this.bottomLevelHead = sentinalHead;
		this.bottomLevelTail = sentinalTail;
	}
	
	/**
	 * Insert a value in the skip list.
	 * @param key The value to be entered.
	 */
	public void insert(int key) {
		
		Node temp = sentinalHead;
		Node newNode = new Node(key);
		//Insert it into the bottom layer
		while(temp!=null && temp.down!=null) {
			while(temp.next != null && temp.next.value <= key)
			{
				temp = temp.next;
			}
			temp = temp.down;
		}
		//Find the position in the bottom layer.
		while(temp!=null && key >= temp.next.value)
			temp = temp.next;
		
		//Inserting the nodes in the bottom-Most layer.
		Node t = temp.next;
		temp.next = newNode;
		newNode.next = t;
		newNode.previous = temp;
		t.previous = newNode;
		
		
		Node head = bottomLevelHead;
		Node tail = bottomLevelTail;
		//Now fix the upper layers.
		while(random() == 1) {
			t = new Node(key); //New node
			
			//If the sentinels are not present in this level --> make them.
			if(head.top == null) {
				Node newHeadLevel = new Node(Integer.MIN_VALUE);
				newHeadLevel.next  = t;
				t.previous = newHeadLevel;
				Node newTailLevel = new Node(Integer.MAX_VALUE);
				t.next = newTailLevel;
				newTailLevel.previous = t;
				
				//now make the top bottom connections here.
				head.top = newHeadLevel;
				newHeadLevel.down = head;
				tail.top = newTailLevel;
				newTailLevel.down = tail;
				
				newNode.top = t;
				t.down = newNode;
				
				head = head.top;
				tail = tail.top;
				newNode = newNode.top;
				
				//set the access points for future inserts.
				this.sentinalHead = newHeadLevel;
				this.sentinalTail = newTailLevel;
			}
			//call this if sentinels are already present in the upper levels.
			else {
				while(temp!=null && temp.top == null)
					temp = temp.previous;
				temp = temp.top; //Go to the upper layer.
				Node x = temp.next; //Store next of current node
				temp.next = t;
				t.previous = temp;
				t.next = x;
				x.previous = t;
				newNode.top = t;
				t.down = newNode;
				newNode = newNode.top;
				
				//Increment the head and tails as well
				head = head.top;
				tail = tail.top;
			}
		}
	}
	
	/**
	 * Method to look for a value in the skiplist.
	 * @param key The key to look for.
	 * @return True if found, false otherwise.
	 */
	public boolean found(int key) {
		Node x = sentinalHead, y = sentinalHead;
		while(x != null && x.value <= key) {
			if(x.value == key)
				return true;
			
			//Go down if the next value is greater.
			if(x.next.value>key) {
				x = x.down;
			}
			else
				x = x.next;
		}
		return false;
	}
	
	/**
	 * Delete the node from the skip list.
	 * @param key The list to be removed.
	 */
	public void delete(int key) {
		Node t = this.sentinalHead;
		boolean found = false;
		while(t!=null && t.value <= key ) {
			if(t.value == key) {
				found = true;
				break;
			}
			if(t.next.value > key)
				t = t.down;
			else
				t = t.next;
		}
		if(found) {
			System.out.println("Key " + key + "deleted");
			while(t!=null) {
				Node x = t;
				Node previous = t.previous;
				Node next = t.next;
				//remove connection to next.
				previous.next = next;
				next.previous = previous;
				//Now do it for all down pointers.
				t = t.down;
				//set down to null.
				x.down = null;
			}
		}
		else
			System.out.println("The value to be deleted isn't present!");
	}
	
	
	public void print() {
		Node down = sentinalHead, next = sentinalHead;
		
		while(down!=null) {
			System.out.print(String.format("%d->", next.value));
			next = next.next;
			//move down if we reach the end of the list.
			if(next!=null && next.next == null) {
				System.out.print(next.value);
				System.out.println();
				down = down.down;
				next = down;
			}
		}
	}
	
	/**
	 * Get either 0 or 1.
	 * @return either 0 or 1.
	 */
	private int random() {
		return (int) Math.round(Math.random());
	}
	
	public static void main(String [] args) {
		Scanner sc = new Scanner(System.in);
		//SkipList skipList = new SkipList((int)Math.ceil(Math.log(size)));
		SkipList skipList = new SkipList();
		while(true) {
			System.out.println("Enter 1 for insert, 2 for delete, 3 for lookup, 4 to print values.");
			int input = sc.nextInt();
			if(input > 4)
				break;
			switch(input) {
			case 1:
				System.out.println("Enter value to insert.");
				input = sc.nextInt();
				skipList.insert(input);
				break;
			case 2:
				System.out.println("Enter key to delete");
				input = sc.nextInt();
				skipList.delete(input);
				break;
			case 3:
				System.out.println("Enter value to look for.");
				input = sc.nextInt();
				boolean flag = skipList.found(input);
				if(flag)
					System.out.println("Element found.");
				else
					System.out.println("Not found!");
				break;
			case 4:
				skipList.print();
				break;
			default:
				System.out.println("Wrong choice!!");
				break;
			}
		}
		sc.close();
	}
}
