package com.nc.edu.ta.scherbak;

import java.io.Serializable;
import java.util.Objects;

public class LinkedTaskList extends TaskList {
    private Node first;
    public LinkedTaskList() {}
    @Override
    public void add(Task task) {
        if(task != null) {
            Node newNode = new Node(task, null);
            if (first == null) {
                first = newNode;
            } else {
                Node tmp = first;
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = newNode;
            }
            size++;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public void remove(Task task) {
        if(task != null) {
            while(task.equals(first.task)){
                first = first.next;
                size--;
            }
            Node current = first;
            Node next = current.next;
            while(next != null){
                if(task.equals(next.task)) {
                    current.next = next.next;
                    size--;
                }
                current = current == null ? null : current.next;
                next = current == null ? null : current.next;
            }
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public Task getTask(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException(index);
        }
        Node tmp = first;
        for (int i = 0; i < index; i++) {
            tmp = tmp.next;
        }
        return tmp.task;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LinkedTaskList").append("(").append(size).append(")");
        builder.append("[");
        Node tmp = first;
        while(tmp != null){
            Task tmpTask = tmp.task;
            builder.append("\n");
            builder.append(tmpTask.getStringByTitle(" ", tmpTask.getTitle()));
            tmp = tmp.next;
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList list = (LinkedTaskList) o;
        if(this.size != list.size) return false;
        Node tmp1 = this.first;
        Node tmp2 = list.first;
        for (int i = 0; i < size; i++) {
            if(!Objects.equals(tmp1.task, tmp2.task)) {
                return false;
            }
            tmp1 = tmp1.next;
            tmp2 = tmp2.next;
        }
        return true;
    }

    private static class Node implements Serializable {
        Task task;
        Node next;
        public Node(Task task, Node next) {
            this.task = task;
            this.next = next;
        }
    }
}
