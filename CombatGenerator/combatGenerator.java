//Author: Mark McMurtury
//Start date: December 20 2019
//Updated: January 1 2020;

import java.util.*;
import java.io.*;

/* The following code will preform the math from the Dungeon Master's
 *  Guide and generate a combat encouter for user inputed party
 *  levels. It will be based off templates that the user can either
 *  pick from or have the code select on at random. Currently it just
 *  randomly creates an encounter by randomly selectly a CR followed
 *  by randomly picking a number for the amount of monsters. Checking
 *  the amount of exp from that group against the budgetted amount of
 *  exp for the user inputted party, it either adds the monsters to 
 *  the encounter or not. If it does add the monsters, it reduces the
 *  amount of available exp by the amount from the previous group of 
 *  monsters. At which point it loops again using the remaining exp
 *  as the new limit. It keep looping till it finds a valid encounter.
 *  The encounter is valid when the remaining exp is between the following
 *  limits: 10 times the number of players under the budget and 50 times
 *  the number of players over the budget. 
 */

public class combatGenerator {

	//The code uses a map to store the CR to EXP values.
	private Map<Double, Integer> crToExp;
	private double[] CRs;
	private int[][] partyLevels;
	private int[] adventDay;
	private Map<Integer, Double> encountMult;
	private int[] monNum;

	public combatGenerator(){
		crToExp = new HashMap<Double, Integer>();
		CRs = new double[29];
		partyLevels = new int[20][4];
		adventDay = new int[20];
		monNum = new int[8];
		encountMult = new HashMap<Integer, Double>();
		readdata("combatTable.txt");
		readlevels("partylevels.txt");
		readday("adventureDay.txt");
		readmult("encounterMult.txt");
	}

	public void readmult(String filename){
                if (filename == null){
                        return;
                }
                try {
			int z = 0;
                        Scanner fileScanner = new Scanner(new File(filename));
                        while(fileScanner.hasNextLine()){
                                String fileLine = fileScanner.nextLine();
                                String[] splitStr = fileLine.trim().split("\\s+");
                                if(splitStr.length == 2){
					monNum[z] = Integer.parseInt(splitStr[0]);
					++z;
                                        encountMult.putIfAbsent(Integer.parseInt(splitStr[0]),
                                                        Double.parseDouble(splitStr[1]));
                                }
                        }
                        fileScanner.close();
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

	public void readdata(String filename){
                if (filename == null){
                        return;
                }
                try {
			int l = 0;
                        Scanner fileScanner = new Scanner(new File(filename));
                        while(fileScanner.hasNextLine()){
                                String fileLine = fileScanner.nextLine();
				String[] splitStr = fileLine.trim().split("\\s+");
				if(splitStr.length == 2){
                                	crToExp.putIfAbsent(Double.parseDouble(splitStr[0]), 
							Integer.parseInt(splitStr[1]));
					CRs[l] = Double.parseDouble(splitStr[0]);
					++l;
				}
                        }
                        fileScanner.close();
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

	public void readlevels(String filename){
                if (filename == null){
                        return;
                }
                try {
                        Scanner fileScanner = new Scanner(new File(filename));
                        while(fileScanner.hasNextLine()){
                                String fileLine = fileScanner.nextLine();
                                String[] splitStr = fileLine.trim().split("\\s+");
                                if(splitStr.length == 5){
                                        int index = Integer.parseInt(splitStr[0]) - 1;
					partyLevels[index][0] = Integer.parseInt(splitStr[1]);
					partyLevels[index][1] = Integer.parseInt(splitStr[2]);
					partyLevels[index][2] = Integer.parseInt(splitStr[3]);
					partyLevels[index][3] = Integer.parseInt(splitStr[4]);
                                }
                        }
                        fileScanner.close();
                } catch (Exception e) {
                        System.out.println(e);
                }
        }

	public void readday(String filename){
		if (filename == null){
                        return;
                }
                try {
                        Scanner fileScanner = new Scanner(new File(filename));
                        while(fileScanner.hasNextLine()){
                                String fileLine = fileScanner.nextLine();
                                String[] splitStr = fileLine.trim().split("\\s+");
                                if(splitStr.length == 2){
                                        int index = Integer.parseInt(splitStr[0]) - 1;
                                        adventDay[index] = Integer.parseInt(splitStr[1]);
                                }
                        }
                        fileScanner.close();
                } catch (Exception e) {
                        System.out.println(e);
                }

	}

	//Return the multiplier based on the number of players and monsters.
	public double getmult(int num, int pcount){
		double m = 1.0;
		for(int i = 0; i < monNum.length; ++i){
			if(num <= monNum[i]){
				if(pcount < 5 && pcount > 3){
					m = encountMult.get(monNum[i]);
				} else if (pcount > 5){
					m = encountMult.get(monNum[i-1]);
				} else if (pcount < 3){
					m = encountMult.get(monNum[i+1]);
				}
				break;
			}
		}
		return m;
	}

	//This finds the amount of exp the number of monsters are worth. 
	//m - multiplier  n - number of monsters  exp - exp value for the monster.
	public double checkExp(double m, int n, int exp){
		int total = 0;
		total = exp * n;
		double t  = Double.valueOf(total) * m;
		return t;
	}

	//This finds a valid one to one fight for the players.
	public double[][] oneVone(int budget, int level, int numberP){
		double[][] retVal = new double[2][3];
		int temp = level/2;
		double CR = Double.valueOf(temp);
		if(level == 1.0){ CR = 0.5; } 
		double minCR = 0.0;
		boolean valid = false;

		while(!valid && CR != 0){
			retVal[0][0] = CR;
			if(CR==0.125){
				minCR = 0;
			} else if(CR<=1){
				minCR = CR/2;
			} else {
				minCR = CR - 1;
			}
			retVal[1][0] = minCR; 
			int expVal = crToExp.get(CR);
			int expValmin = crToExp.get(minCR);
			int number = numberP;
			int minnumber = number; 
			double mult = getmult(number, numberP);
			double minmult = getmult(minnumber, numberP);
			
			if(checkExp(minmult, minnumber, expValmin) <= budget 
			  && checkExp(mult, number, expVal) >= budget
			  || checkExp(minmult, minnumber, expValmin) == budget
			  || checkExp(mult, number, expVal) == budget){
				retVal[0][1] = Double.valueOf(number);
				retVal[0][2] = checkExp(mult, number, expVal);
				retVal[1][1] = Double.valueOf(minnumber);
				retVal[1][2] = checkExp(minmult, minnumber, expValmin);
				valid = true;
			  } else if(checkExp(mult, number, expVal) < budget){
				  if(CR==0){
                                        CR = 0.125;
                                } else if(CR<=1){
                                        CR = CR*2;
                                } else {
                                        ++CR;
                                }
			  } else {
				if(CR==0.125){
        	                        CR = 0;
	                        } else if(CR<1||CR==1){
                        	        CR = CR/2;
                	        } else {
        	                        CR = CR - 1;
	                        }

			  }
		}
		return retVal;
	}
	
	//This finds the total exp for the party based on the difficulty.
	public int totalEXP(int[] p, int d){
		int total = 0;
		for(int i = 0; i < p.length; ++i){
			int level = p[i] - 1;
			total = total + partyLevels[level][d];
		}
		return total;
	}

	//A true randomly built encounter.
	public entry[] randomEncounter(int budget, int numberP){
		entry[] retVal = new entry[50];
		int total = 0;
		int allowance = budget;
		Random rand = new Random();
		int count = 0;
		int moncount = 0;
		boolean valid = false;
		Vector crUsed = new Vector();

		while(!valid && count < 400){
			int index = rand.nextInt(CRs.length);
			double temp = CRs[index];
			int tempexp = crToExp.get(temp);
			if(tempexp < budget){
				int ttemp = allowance/tempexp;
				if(!crUsed.contains(temp)){
					if (ttemp >= 1) {
						int number = rand.nextInt(ttemp);
						if(number == 0) {++number;}
						double mult = getmult(number, numberP);
						double currentexp = checkExp(mult, number, tempexp);
						int curexp = (int)currentexp;
						if(allowance - curexp <= 10*numberP 
						  && allowance - curexp > -50*numberP){
							retVal[moncount] = new entry(temp, number, currentexp);
	
							valid = true;
						}
						else if(allowance - curexp > 10*numberP){
							allowance = allowance - curexp;
							retVal[moncount] = new entry(temp, number, currentexp);
							++moncount;
							crUsed.add(temp);
						}
					}
				} else {
					if(rpick()){
						entry oldEntry = new entry();
						for(int i = 0; i<retVal.length; ++i){
							if(retVal[i].getCR() == temp){
								oldEntry = retVal[i];
								break;
							}
						}
						int number = oldEntry.getNumber();
						++number;
						double mult = getmult(number, numberP);
						double currentexp = checkExp(mult, number, tempexp);
						int difference = (int)currentexp - (int)oldEntry.getExp();
					       if(allowance - difference <= 10*numberP
                                                  && allowance - difference > -50*numberP){
                                                        oldEntry.setNumber(number);
							oldEntry.setExp(currentexp);
                                                        valid = true;
                                                }
                                                else if(allowance - difference > 10*numberP){
                                                        allowance = allowance - difference;
                                                        oldEntry.setNumber(number);
                                                        oldEntry.setExp(currentexp);
                                                }
	
					}
				}
				++count;
			}
		}
		return retVal;
	}	


	//This function returns a boolean, randomly picking true or false.
	public boolean rpick(){
		boolean retval = false;
		Random rand = new Random();
		int x = rand.nextInt();
		if (x%2 == 0){retval = true;}
		return retval;
	}
}
