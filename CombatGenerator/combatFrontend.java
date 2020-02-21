//Author: Mark McMurtury
//Start date: December 20 2019
//Updated: January 1 2020;


import java.util.*;
import java.io.*;

/* The following is the front of the program. It has the user interface which
 * creates a new combatGenerator object, that object contains all the coding
 * for the combat generation. The code loops and ask the user to input the
 * number of players, their levels, and how hard of a fight to generate.
 * Passing that information to the combatGenerator object, it waits for the 
 * returned encounte to print out before asking if they want to regenerate
 * the combat or return to the main menu.
 */

public class combatFrontend {

	//Just a main method to call the other object.
	public static void main(String args[]){
                Scanner input = new Scanner(System.in);
                combatGenerator gen = new combatGenerator();

                System.out.println("Welcome to the combat encounter generator.");
                
                while(true){
                        System.out.println("1: Generate new encounter\n0: EXIT");
                        int choice = input.nextInt();
                        if(choice == 0){
                                break;
                        } else{
                                int maxLevel = 0;
                                System.out.println("Please input the number of players you have.");
                                int numPlayers = input.nextInt();
                                int[] players = new int[numPlayers];
                                for(int i = 0; i < numPlayers; ++i){
                                        System.out.println("Input player " + (i+1) + " Level, hit enter afterwards.");
                                        players[i] = input.nextInt();
                                        if(players[i] > maxLevel) {
                                                maxLevel = players[i];
                                        }
                                }
                                System.out.println("\nChoose the dificulty \n0: Easy \n1: Medium \n2: Hard \n3: Deadly");
                                int difficulty = input.nextInt();
                                int budget = gen.totalEXP(players, difficulty);
				System.out.println("\nBudgeted EXP: " + budget + "\n");
                                //double[][] test = gen.oneVone(budget, maxLevel, numPlayers);
                                //System.out.println("Two possible Encounters: \nCR: " + test[0][0] + " Number: " + test[0][1] + " Total EXP: " + test[0][2]);
                                //System.out.println("CR: " + test[1][0] + " Number: " + test[1][1] + " Total EXP: " + test[1][2]);
				entry[] encounter = gen.randomEncounter(budget, numPlayers);
				double texp = 0.0;
				for(int i = 0; i < encounter.length; ++i){
					entry temp = encounter[i];
					if(temp != null){
						System.out.println("CR: " + temp.getCR()
						 + " \tNumber: " + temp.getNumber() 
						 + "\tEXP: " + temp.getExp());
						texp = texp + temp.getExp();
					}
				}
				int x = 0;
				System.out.println("Total exp from the monsters: " + texp);
				System.out.println("Regenerate encounter? (input 1 to regenerate)");
				x = input.nextInt();
				while(x == 1){
					System.out.println("\nBudgeted EXP: " + budget + "\n");
					encounter = gen.randomEncounter(budget, numPlayers);
	                                texp = 0.0;
        	                        for(int i = 0; i < encounter.length; ++i){
                	                        entry temp = encounter[i];
                        	                if(temp != null){
                                	                System.out.println("CR: " + temp.getCR()
                                        	         + " \tNumber: " + temp.getNumber()
                                                	 + "\tEXP: " + temp.getExp());
	                                                texp = texp + temp.getExp();
                                        	}
                                	}
					System.out.println("Total exp from the monsters: " + texp);
					System.out.println("Regenerate encounter? (input 1 to regenerate)");
	                                x = input.nextInt();
				}
                        }
                }
        }
}
