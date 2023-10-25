package com.chen.tool.redis;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author chenwh3
 */
@Slf4j
public class MySemaphore {

    private AtomicInteger permits = new AtomicInteger();

    MySemaphore(int num) {
        permits.set(num);
    }

    private static class Node {
        private Thread thread;
        private volatile Node next;
        private volatile Node prev;

        Node(Thread thread) {
            this.thread = thread;
        }


    }

    @Data
    private static class List {

        private AtomicReference<Node> head;
        private AtomicReference<Node> tail;

        public List init() {
            List list = new List();
            list.head = new AtomicReference<>();
            list.tail = new AtomicReference<>();
            Node node = new Node(null);
            list.head.set(node);
            list.tail.set(node);
            return list;
        }

        /**
         * use cas
         */
        public void addTail(Node node) {
            while (true) {
                Node tailNode = tail.get();
                if (tail.compareAndSet(tailNode, node)) {
                    tailNode.next = node;
                    node.prev = tailNode;
                    break;
                }
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node node = head.get();
            while (node != null) {
                sb.append(node.thread.getName()).append("->");
                node = node.next;
            }
            return sb.toString();
        }
    }

    /**
     * use compare and set , if failed , retry
     */
    public void acquire(int num) {

    }



    public void  release(int num) {

    }

    public static void test1() throws InterruptedException{
        Semaphore mySemaphore = new Semaphore(5);
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            new Thread(() -> {
                try {
                    if (finalI < 5) {
                        mySemaphore.acquire(1);
                    } else {
                        mySemaphore.release(1);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            TimeUnit.MILLISECONDS.sleep(200);
        }
        TimeUnit.SECONDS.sleep(2);
        System.out.println(mySemaphore.availablePermits());
    }

    public static void test2() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        CompletableFuture.runAsync(()->{
            try {
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });


        System.out.println(123);
    }

    public static void main(String[] args) throws InterruptedException {
        test2();
    }
}