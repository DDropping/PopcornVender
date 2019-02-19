
package store;

import java.util.*;
import java.io.*;

public class Store {
    customer[] queueArray;
    int queueSize=0;
    int front=0;
    int numberOfItems=0;
    int rear=0;
    int customersLost = 0;
    customer customerBeingServed;
    static boolean showTime = false;
 
    Store(int size){
        queueSize = size;
        queueArray = new customer[size];
    }
    
    public static void main(String[] args) {
        
        customer[] customerArr = null;
        customer[] qcustomerArr = null;
        int hours =0;
        int customers =0;
        boolean readFile =false;
        
        //custom info or randomly generated info
        Scanner myscan = new Scanner(System.in);
        System.out.println("Use Customer File (y/n) ");
        System.out.print("Ensure correct filepath is entered(line 51 Store.java): ");
        String ans1 = myscan.next().toLowerCase();
        if(ans1.equals("y") || ans1.equals("yes")){
            readFile = true;
        }
        
        if(readFile){
            System.out.print("Show Event Log? (y/n): ");
            String ans2 = myscan.next().toLowerCase();
            if(ans2.equals("y") || ans2.equals("yes")){
            showTime = true;
        }
        }

        //read custom info file Test.txt
        if(readFile){
            Scanner sc2 = null;
            try{
                sc2 = new Scanner(new File("C:\\Users\\DavidXPS\\Documents\\NetBeansProjects\\Store\\src\\store\\Test.txt"));
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

            String customerNumber = "customerX";
            int arrivalTime1 =0, cookTime1=0, maxWaitTime1=0, priority1=0, beenWaiting1=0, counter=0;
            boolean beenServed=false, hasLeft=false, ready = false;

            while (sc2.hasNextLine()) {
                Scanner s2 = new Scanner(sc2.nextLine());
                while(s2.hasNext()){
                    String s = s2.nextLine();
                    String splitted[] = s.split("=");
                    if(splitted[0].equals("hours")){
                        hours = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("customers")){
                        customers = Integer.parseInt(splitted[1]);
                        customerArr = new customer[customers];
                        qcustomerArr = new customer[customers];
                    }
                    if(splitted[0].equals("customerNumber")){
                        customerNumber = splitted[1];
                    }
                    if(splitted[0].equals("arrivalTime")){
                        arrivalTime1 = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("cookTime")){
                        cookTime1 = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("maxWaitTime")){
                        maxWaitTime1 = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("priority")){
                        priority1 = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("beenWaiting")){
                        beenWaiting1 = Integer.parseInt(splitted[1]);
                    }
                    if(splitted[0].equals("beenServed")){
                        String temp = splitted[1];
                        if (temp.equals("true")){
                            beenServed = true;
                        }
                        else{
                            beenServed = false;
                        }
                    }
                    if(splitted[0].equals("hasLeft")){
                        ready = true;
                        String temp = splitted[1];
                        if (temp.equals("true")){
                            hasLeft = true;
                        }
                        else{
                            hasLeft = false;
                        }
                    }


                    if(ready){
                    customer tempCust = new customer(customerNumber, arrivalTime1, cookTime1, maxWaitTime1, priority1, beenWaiting1, beenServed, hasLeft);
                    customer qtempCust = new customer(customerNumber, arrivalTime1, cookTime1, maxWaitTime1, priority1, beenWaiting1, beenServed, hasLeft);
                        //System.out.println("failure?");
                    customerArr[counter] = tempCust;
                    qcustomerArr[counter] = qtempCust;
                    
                    //System.out.println("done: "+counter);
                    counter++;
                    ready = false;
                    }
                }
            }
            quickSort(customerArr, 0, (customerArr.length-1));
            quickSort(qcustomerArr, 0, (qcustomerArr.length-1));
            
        }
        
        
        if(!readFile){
        
            Scanner sc = new Scanner(System.in);

            //the next ~125 lines are creating the customer objects and placeing into an array

            //validate hours is positive integer
            System.out.println("\nPlease enter store hours and number of customers. ");
            int number;
            do {
                System.out.print("Please enter a positive integer.\n>>>hours: ");
            while (!sc.hasNextInt()) {
                System.out.print("Please enter a postitive integer.\n>>>hours: ");
                sc.nextInt(); 
            }
            number = sc.nextInt();
            hours = number;
            } while (number < 0);

            //validate customers is positive integer
            int number2;
            do {
                System.out.print("Please enter a positive integer.\n>>>customers: ");
            while (!sc.hasNextInt()) {
                System.out.print("Please enter a postitive integer.\n>>>customers: ");
                sc.next(); 
            }
            number2 = sc.nextInt();
            customers = number2;
            } while (number2 < 0);


            System.out.print("View event log? (y/n): ");
            String ans = sc.next().toLowerCase();
            if(ans.equals("y") || ans.equals("yes")){
                showTime = true;
            }



            //check to see if any customers arrived or if the store was opened
            if(customers<=0){
                System.out.println("Sorry, looks like no one wanted popcorn today");
                System.out.println("Total Customers: 0");
                System.out.println("Average Wait:    0 min");
                return;
            }
            if(hours<=0){
                System.out.println("The store was cloased all day.");
                System.out.println("Total Customers: 0");
                System.out.println("Average Wait:    0 min");
                return;
            }

            //array for priority queue
            customerArr = new customer[customers];
            //array for standard queue
            qcustomerArr = new customer[customers];

            //loop to create customers and place in array
            for (int i = 0; i < customers; i++) {

            //create arrivalTime
                int arrivalTime = (int) (Math.random() * (hours*60) + 0);

            //create cookTime
                int order = (int) (Math.random() * 5) + 1;
                int cookTime;
                if (order == 1) {cookTime = 1;
                } else if (order == 2) {cookTime = 2;
                } else if (order == 3) {cookTime = 3;
                } else if (order == 4) {cookTime = 4;
                } else if (order == 5) {cookTime = 5;
                } else {cookTime = 0;}

            //create maxWaitTime
                int maxWaitTime = (int) (Math.random() * (.5*hours*60)) + 10;

            //create priority
                int priority;
                switch (order) {
                    case 1:
                        priority = 5;
                        break;
                    case 2:
                        priority = 4;
                        break;
                    case 3:
                        priority = 3;
                        break;
                    case 4:
                        priority = 2;
                        break;
                    case 5:
                        priority = 1;
                        break;
                    default:
                        priority = 0;
                        break;
                }


            //create customer objects and place in array
                customer customer = new customer("Customer" ,arrivalTime, cookTime, maxWaitTime, priority, 0, false, false);
                customer qcustomer = new customer("Customer" ,arrivalTime, cookTime, maxWaitTime, priority, 0, false, false);
                customerArr[i] = customer;
                qcustomerArr[i] = qcustomer;
            }


            //sort the arrays based on arrival time
            quickSort(customerArr, 0, (customerArr.length-1));
            quickSort(qcustomerArr, 0, (qcustomerArr.length-1));

            //assign customerNumbers
            int i=1;
            for(customer x: customerArr){
                x.customerNumber = "customer" +i;
                i++;
            }

            //qcust assign customerNumbers
            i=1;
            for(
                customer x: qcustomerArr){
                x.customerNumber = "customer" +i;
                i++;
            }

        }

        if(showTime){
        System.out.println("\n\n\n -----Priority Queue-----\n");
        }
        
        //create the Priority queue
        Store theQueue = new Store(customers+1);
        boolean isBusy = false;
        int cookTimer = 0;
        
        //store simulator
        for(int time = 0; time<= hours*60; time++){
            if(showTime){
                System.out.println("time: " + time + "  cookTimer: " + cookTimer);
            }
            
            //add customer to queue
            for(customer x: customerArr){
                if(x.arrivalTime == time){
                    theQueue.insert(x);
                }
                if(x.arrivalTime > time){
                    break;
                }
            }
            
            theQueue.increaseWaitTime();
            
            theQueue.increasePriority();
            
            theQueue.prioritizeQueue();
            
            //pop customer from queue
            if(isBusy == false && theQueue.peek() != null ){
                customer currentCustomer = theQueue.peek();
                cookTimer += currentCustomer.cookTime;
                isBusy = true;
                theQueue.remove();
            }
            
            //decrement cook timer
            if(cookTimer > 0){cookTimer--;}
            if(cookTimer == 0 && isBusy == true){isBusy = false;}
        }
              

        if(showTime){
            System.out.println("\n\n\n -----Standard Queue-----\n");
        }
        
        //prints stand queue data
        standardQueue(qcustomerArr, hours);
        
        //prints priority queue data
        theQueue.getData(); 
    }
    
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////

    static void quickSort(customer[] arr, int low, int high) {
		if (arr == null || arr.length == 0)
			return;
		if (low >= high)
			return;
		int middle = low + (high - low) / 2;
		int pivot = arr[middle].arrivalTime;
		int i = low, j = high;
		while (i <= j) {
			while (arr[i].arrivalTime < pivot) {
				i++;
			}
			while (arr[j].arrivalTime > pivot) {
				j--;
			}
			if (i <= j) {
				customer temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				i++;
				j--;
			}
		}
		if (low < j)
			quickSort(arr, low, j);
		if (high > i)
			quickSort(arr, i, high);
	}
    
    public void getData(){
        int customersServed = 0, totalWaitTime = 0, customersWhoLeft =0, leftOverCustomers = 0;
        
        for(int x=0; x<queueArray.length-1;x++){
            //customers who left
            if(queueArray[x].cookTime == 0){
                queueArray[x].hasLeft = true;
                customersWhoLeft++;
            }
            
            //customers who have been served
            if(queueArray[x].beenServed == true && queueArray[x].hasLeft == false){
                customersServed++;
                totalWaitTime += queueArray[x].beenWaiting;
            }
            
            //left over customers
            if(queueArray[x].beenServed == false && queueArray[x].hasLeft == false){
                leftOverCustomers++;
            }
        }

        
        float averageWaitTime = (float) totalWaitTime / (float) customersServed;
        System.out.println("Priority Queue: ");
        System.out.println("    Customers Served: " + customersServed);
        System.out.println("    Average Wait Time: " + averageWaitTime + " min");
        System.out.println("    Customers Who Left: " + customersWhoLeft);
        System.out.println("    Left Over Customers: "+ leftOverCustomers);
        System.out.println("");
    }
    
    
    
    customer peek(){
        //view the front queue item
        return queueArray[front];
    }
    
    public void increaseWaitTime(){
        //increase the time customers have been waiting
        for(int x=front; x<rear; x++){
            queueArray[x].beenWaiting++;
            if(queueArray[x].beenWaiting == queueArray[x].maxWaitTime){
                queueArray[x].hasLeft = true;
                if(showTime){
                            System.out.println("    "+ queueArray[x].customerNumber + " has left.");
                        }
                queueArray[x].cookTime = 0;
            }
        }
        
    }
    
    public void increasePriority(){
        // increase priority for every 10 minutes a customer waits
        int i = rear-1;
        while(i > front){
            if(queueArray[i].beenWaiting % 10 == 0){
                queueArray[i].priority++;  
                if(showTime && queueArray[i].cookTime != 0){
                    System.out.println("    "+queueArray[i].customerNumber + " has increased in priority: "+queueArray[i].priority);
                }
            }
            i--;
        }
    }
    
    public void prioritizeQueue(){
        //order the array based on greatest priority
        if(numberOfItems > 1){
            int in = rear-1;
            while(in > front){
                if(queueArray[in].priority > queueArray[in-1].priority){
                    customer tmp = queueArray[in];
                    queueArray[in] = queueArray[in-1];
                    queueArray[in-1] = tmp;
                }
                in--;
            }            
        }
    }
    
    void insert(customer input){
        //insert item into back of queue
        if(numberOfItems+1 <=queueSize){
            if(showTime){
                System.out.println("    " + input.customerNumber + " has arrived with priority: "+input.priority);
            }
            queueArray[rear] = input;
            rear++;
            numberOfItems++;
        } else {
            System.out.println("***Queue is Full");
        }
    }
    
    public void remove(){
        //remove item from front of queue
        if(numberOfItems > 0){
            if(queueArray[front].cookTime == 0){
                front++;
                numberOfItems--;
            }
        }
        if(numberOfItems > 0){
            if(queueArray[front].beenWaiting>0){
                queueArray[front].beenWaiting--;
            }
            if(queueArray[front].cookTime >0){
                queueArray[front].beenServed = true;
            }
            if(showTime){
                System.out.println("    "+queueArray[front].customerNumber + " is being served. " + "priority: "+ queueArray[front].priority + " | waited: " + queueArray[front].beenWaiting + " min" + " | cook time: "+ queueArray[front].cookTime);
            }
            customerBeingServed = queueArray[front];
            front++;
            numberOfItems--;
        } else {
            System.out.println("***Queue is empty");
        }
    }
    
    static void standardQueue(customer[] qcustomerArr, int hours){
        
        
        int front=0;
        int back=-1;
        int customersLost =0;
        int leftOverCustomers =0;
        int customersServed=0;
        int totalWaitTime=0;
        int cookTimer = 0;
        customer currentCustomer = null;
        
        for(int time=0; time<=hours*60; time++){
            //add to queue
            if(showTime){
                System.out.println("time: "+time + " | cookTimer: "+ cookTimer);
            }
            for(customer x: qcustomerArr){
                if(x.arrivalTime == time){ 
                    back++;
                    if(showTime){
                        System.out.println("    "+x.customerNumber + " added to queue.");
                    }
                } 
            }
            
            //customers who left
            if(front<=back){
                for(int x = front; x <= back; x++){
                    if(qcustomerArr[x].beenWaiting == qcustomerArr[x].maxWaitTime){
                        qcustomerArr[x].hasLeft = true;
                        if(showTime){
                            System.out.println("    "+ qcustomerArr[x].customerNumber + " has left.");
                        }
                        qcustomerArr[x].cookTime = 0;
                        front++;
                    }
                }
            }
            
            //pop from queue (serve customer)
            if(cookTimer == 0 && front<=back){
                cookTimer += qcustomerArr[front].cookTime;
                if(qcustomerArr[front].cookTime != 0){
                    qcustomerArr[front].beenServed = true;
                    currentCustomer = qcustomerArr[front];
                }
                if(showTime){
                    System.out.println("    "+qcustomerArr[front].customerNumber + " is being served | waited: "+qcustomerArr[front].beenWaiting+" | cook time: "+qcustomerArr[front].cookTime );
                }
                front++; 
            }
            
            //increase wait time
            if(front<=back){
                for(int x = front; x <= back; x++){
                    qcustomerArr[x].beenWaiting++;
                }
            }
            //decrease cook timer
            if(cookTimer>0){cookTimer--;}
        }
        
        for(int x=0; x< qcustomerArr.length; x++){
            //count how many customers were served
            if(qcustomerArr[x].beenServed == true && qcustomerArr[x].hasLeft == false){
                customersServed++;
                totalWaitTime += qcustomerArr[x].beenWaiting;
            }
            //count how many customers left
            if(qcustomerArr[x].hasLeft == true){
                customersLost++;
            }
            
            //count how many customers were left over 
            if(qcustomerArr[x].beenServed == false && qcustomerArr[x].hasLeft == false){
                leftOverCustomers++;
            }
        }
        
        float averageWait = (float) totalWaitTime/ (float) customersServed;
        System.out.println("\n\n\nNormal Queue: ");
        System.out.println("    customers served: "+customersServed);
        System.out.println("    average wait time: "+averageWait+" min");
        System.out.println("    Customer Who Left: "+customersLost);
        System.out.println("    Leftover Customers: "+leftOverCustomers);
        System.out.println("");
    }
    
}

class customer {

    int arrivalTime, cookTime, maxWaitTime, priority, beenWaiting;
    boolean beenServed, hasLeft;
    String customerNumber;

    public customer(String customerNumber, int arrivalTime, int cookTime, int maxWaitTime, int priority, int beenWaiting, boolean beenServed, boolean hasLeft) {
        this.customerNumber = customerNumber;
        this.arrivalTime = arrivalTime;
        this.cookTime = cookTime;
        this.maxWaitTime = maxWaitTime;
        this.priority = priority;
        this.beenWaiting = beenWaiting;
        this.beenServed = beenServed;
        this.hasLeft = hasLeft;
    }
}