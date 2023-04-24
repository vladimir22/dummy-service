package interview;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

public class LinkedList_DetectCycle {

@Builder
@Data
public static class Node {
    int data;
    Node next;
    Node(int data, Node next) {
        this.data = data;
        this.next = next;
    }
}

    // Detect cycle in a linked list: https://www.techiedelight.com/detect-cycle-linked-list-floyds-cycle-detection-algorithm/
    public static void main(String[] args)
    {
        // Create list
        int[] keys = {1, 2, 3, 4, 5};
        Node head = null;
        for (int i = keys.length - 1; i >= 0; i--) {
            head = new Node(keys[i], head);
        }
        // insert cycle
        head.next.next.next.next.next = head.next.next;

        if (detectCycle_byHashSet(head)) { // 1st Option: collect HashSet and compare - O(N) time, O(N) space
//        if (detectCycle_bySlowFastPointer(head)) {    // 2nd Option: use Slow and Faster Pointer - O(N) time, O(1) space (Floyd’s Cycle Detection Algorithm)
            System.out.println("Cycle Detected");
        }
        else {
            System.out.println("Cycle NOT Found");
        }
    }


    // Function to detect a cycle in a linked list using hashing
    public static boolean detectCycle_bySlowFastPointer(Node head)
    {
        Node slow = head, fast = head; // take two references – `slow` and `fast`
        while (fast != null && fast.next != null)
        {
            slow = slow.next; // move slow by one
            fast = fast.next.next; // move fast by two
            if (slow == fast) {
                return true; // if they meet any node, the linked list contains a cycle
            }
        }
        return false; // cycle NOT detected
    }

    // Function to detect a cycle in a linked list using hashing
    public static boolean detectCycle_byHashSet(Node head)
    {
        Node node = head;
        Set<Node> set = new HashSet<>();

        while (node != null) {
            if (set.contains(node)) {
                return true; // cycle detected
            }
            set.add(node); // insert the current node into the set
            node = node.next; // move to the next node
        }
        return false; // cycle NOT detected
    }


}
