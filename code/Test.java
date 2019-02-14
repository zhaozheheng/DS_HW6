package zzh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class DataItem{	//data class with key and collision number
	private String iData;
	private int iColl = 0;
	
	public DataItem(String ii) {
		iData = ii;
	}
	
	public String getKey() {
		return iData;
	}
	
	public int getColl() {
		return iColl;
	}
	
	public void setColl(int col) {
		iColl = col;
	}
}

class HashTable{	//hashTable class
	private DataItem[] hashArray;
	private int arraySize;
	private DataItem nonItem;
	
	public HashTable(int size) {	//initialization
		arraySize = size;
		hashArray = new DataItem[arraySize];
		nonItem = new DataItem("null");
	}
	
	public void displayTable() {	//display the whole table
		System.out.print("Index: ");
		System.out.print(" \t");
		for (int i = 0; i < hashArray.length; i++) {	//print the index
			System.out.print(i + "\t");
		}
		System.out.println("");
		System.out.print("Table: ");
		System.out.print(" \t");
		for (int i = 0; i < hashArray.length; i++) {	//print the hash array key
			if (hashArray[i] != null) {
				System.out.print(hashArray[i].getKey() + "\t");
			}
			else {
				System.out.print("null\t");
			}
		}
		System.out.println("");
		System.out.print("Collision: ");
		System.out.print(" \t");
		for (int i = 0; i < hashArray.length; i++) {	//print the collision for each key
			if (hashArray[i] != null) {
				System.out.print(hashArray[i].getColl() + "\t");
			}
			else {
				System.out.print("0\t");
			}
		}
		System.out.println("");
	}
	
	public int hashFunc(String key) {	//hash function to transfer a string to int
		int hashVal = 0;
		for (int i = 0; i < key.length(); i++) {
			int letter = key.charAt(i) - 96;
			hashVal = (hashVal * 27 + letter) % hashArray.length;
		}
		return hashVal;
	}
	
	public void insert(String key) {	//insert function for the hash table
		int hashVal = hashFunc(key);
		DataItem item = new DataItem(key);
		int sum = item.getColl();
		
		while (hashArray[hashVal] != null) {
			sum++;
			++hashVal;
			hashVal %= hashArray.length;
			//System.out.println(sum);
		}
		item.setColl(sum);
		System.out.println("Collision: " + item.getColl());
		hashArray[hashVal] = item;
	}
	
	public DataItem delete(String key) {	//delete function for the hash table
		int hashVal = hashFunc(key);
		
		while (hashArray[hashVal] != null) {
			if (hashArray[hashVal].getKey().equals(key)) {
				DataItem temp = hashArray[hashVal];
				hashArray[hashVal] = nonItem;
				return temp;
			}
			++hashVal;
			hashVal %= hashArray.length;
		}
		return null;
	}
	
	public DataItem find(String key) {	//search function for the hash table
		int hashVal = hashFunc(key);
		
		while (hashArray[hashVal] != null) {
			//System.out.print(hashArray[hashVal].getKey() + key);
			if (hashArray[hashVal].getKey().equals(key)) {
				System.out.println("It's index is " + hashVal);
				return hashArray[hashVal];
			}
			++hashVal;
			hashVal %= hashArray.length;
			//System.out.print("###########");
		}
		return null;
	}
	
	public void reHash() {	//if the load factor is greater than 0.5, we need to rehash.(create a new hash array and reinsert all the number)
		int newSize = getPrime(2*hashArray.length);
		ArrayList<String> tmpKey = new ArrayList<>();
		for (int i = 0; i < hashArray.length; i++) {
			if  (hashArray[i] != null) {
				tmpKey.add(hashArray[i].getKey());
			}
		}
		hashArray = new DataItem[newSize];
		System.out.println("Rehashing~~~~~~~~~~");
		for (int i = 0; i < tmpKey.size(); i++) {
			insert(tmpKey.get(i));
		}
		System.out.println("Finished~~~~~~~~~~~");
	}

	public int getPrime(int min) {	//get the prime array size
		for (int i = min+1; true; i++) {
			if (isPrime(i)) {
				return i;
			}
		}
	}
	
	public boolean isPrime(int n) {	//help check a number is prime or not
		for (int i = 2; (i*i <= n); i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	public DataItem[] getHashArray() {
		return hashArray;
	}

}

public class Test {	//Test class

	public static void main(String[] args) throws IOException {	//main function
		// TODO Auto-generated method stub
		int initialSize = 7;	//initial size is 7
		int itemNum = 0;
		double ratio = 0.0;
		String item;
		DataItem dataItem;
		HashTable theHash = new HashTable(initialSize);
		//System.out.print(theHash.getHashArray()[0]);
		while (true) {	//operation UI for users to control the hash table
			System.out.print("Enter first letter of ");
			System.out.print("show, insert, delete, or find: ");
			char choice = getChar();
			switch (choice) {
			case 's':	//display operation
				theHash.displayTable();
				break;
			case 'i':	//insert operation
				System.out.print("Enter key value to insert: ");
				item = getString();
				theHash.insert(item);
				itemNum++;
				break;
			case 'd':	//delete operation
				System.out.print("Enter key value to delete: ");
				item = getString();
				theHash.delete(item);
				break;
			case 'f':	//search operation
				System.out.print("Enter key value to find: ");
				item = getString();
				dataItem = theHash.find(item);
				if (dataItem != null) {
					System.out.println("Found " + item);
				}
				else {
					System.out.println("Could not find " + item);
				}
				break;
			default:	//default output invalid if type in wrong words
				System.out.print("Invalid entry\n");
			}
			ratio = 1.0*itemNum/theHash.getHashArray().length;
			System.out.println("Load Factor: " + ratio);
			if (ratio > 0.5) {
				theHash.reHash();
			}
			//System.out.println(theHash.getHashArray().length);
		}
		
	}
	
	public static String getString() throws IOException {	//get type in string with IOException
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}
	
	public static char getChar() throws IOException {	//get the 1st character of type in string with IOException
		String s = getString();
		return s.charAt(0);
	}

}
