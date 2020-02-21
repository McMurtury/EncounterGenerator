// Author: Mark McMurtury
// Date: December 26, 2019

/* This is a helper class, it creates an object that will store
 * the CR, the number of monsters, and their exp values. All the
 * values can be passed in when the object is created, they can 
 * also be altered later with set functions. Each value has a 
 * matching get function to return the values stored.
 */

public class entry {

	private double CR;
	private int number;
	private double exp;

	public entry(){
		this.CR = 0.0;
		this.number = 0;
		this.exp = 0.0;
	}
	public entry(double c, int n, double x){
		this.CR = c;
		this.number = n;
		this.exp = x;
	}
	public void setCR(double c){
		this.CR = c;
	}
	public void setNumber(int n){
		this.number = n;
	}
	public void setExp(double x){
		this.exp = x;
	}
	public double getCR(){
		return this.CR;
	}
	public int getNumber(){
		return this.number;
	}
	public double getExp(){
		return this.exp;
	}
}
