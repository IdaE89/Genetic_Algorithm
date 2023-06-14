import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * this program is a genetic algorithm using Map class
 * its made to solve colorgraph problem using 2 point crossover
 * the problem has 3 colors, but can easily be changed to other colors or more colors
 * This algoritm keeps the best population from each generation
 */

class GeneticAlgorithm {
    public static void main(String[]args) {
        //Changable variables
        int antallChromo = 100; //number of chromosoms/number of nodes, size of the graph
        String variabel = "rsg"; //different color value they can have
        int numbParent = 60; //Population, has to be an even number
        int numberOfGen = 1; //number of generations 
        
        //Maps of all the parents and child, with chromosomes and fitness 
        Map<Integer, ArrayList<String>> parentChromo =new HashMap<Integer,ArrayList<String>>();
        Map<Integer, ArrayList<String>> childChromo =new HashMap<Integer,ArrayList<String>>();
        Map<Integer, Integer> parentF =new HashMap<Integer,Integer>();
        Map<Integer, Integer> childF =new HashMap<Integer,Integer>();
        //a sorting list
        ArrayList<Integer> sortert = new ArrayList<Integer>();

        Random random1 = new Random();
        Random random2 = new Random();

        int crossTemp, cross1, cross2;
        String childTemp;
        int parentNumb = 1, childNumb = numbParent, teller = 0;
        String count1, count2, count3, count4;

        for(int j = 0; j < (numbParent/2); j++) {
            ArrayList<String> parent1 = new ArrayList<String>();
            ArrayList<String> parent2 = new ArrayList<String>();
            int parentFitness1 = 0, parentFitness2 = 0;
            for(int i = 0; i < antallChromo; i++) {
                parent1.add(i, String.valueOf(variabel.charAt(random1.nextInt(variabel.length()))));
                parent2.add(i, String.valueOf(variabel.charAt(random2.nextInt(variabel.length()))));
            }
            //count satisfied and unsatisfied 
            for(int i = 0; i < antallChromo; i++) {
                for(int k = i; k < antallChromo-1; k++) {
                    count1 = parent1.get(i);
                    count2 = parent2.get(i);

                    if(count1.equals(parent1.get(k+1))==true) {
                        parentFitness1++;
                    }
                    if(count2.equals(parent2.get(k+1))==true) {
                        parentFitness2++;
                    }
                }
            }
            parentChromo.put(parentNumb, parent1);
            parentF.put(parentNumb, parentFitness1);
            parentNumb++;
            parentChromo.put(parentNumb, parent2);
            parentF.put(parentNumb, parentFitness2);
            parentNumb++;
        }
//System.out.print("\nFirstParentsGen1: "+parentF.entrySet());

        //next generation
        int genNumber = 1;
        while(numberOfGen >= genNumber) {
            parentNumb = 0; 
            childNumb = numbParent;
            ArrayList<String> parent1 = new ArrayList<String>();
            ArrayList<String> parent2 = new ArrayList<String>();

            for(int i = 1; i <= numbParent; i+=2) {
                ArrayList<String> child1 = new ArrayList<String>();
                ArrayList<String> child2 = new ArrayList<String>();
                int childFitness1 = 0, childFitness2 = 0, parentFitness1 = 0, parentFitness2 = 0;
                parent1 = parentChromo.get(i);
                parent2 = parentChromo.get(i+1);

                for(int j = 0; j < parent1.size(); j++) {
                    child1.add(parent1.get(j));
                    child2.add(parent2.get(j));
                }
                parentChromo.remove(i);
                parentChromo.remove(i+1);

                //choose where to do the 2 point cross
                cross1 = random1.nextInt(antallChromo);
                cross2 = random2.nextInt(antallChromo);
                //if the two numbers are the same
                while(cross1 == cross2) { cross2 = random2.nextInt(antallChromo); }
                //make cross1 be the smalles number
                if(cross2 < cross1) {
                    crossTemp = cross1;
                    cross1 = cross2;
                    cross2 = crossTemp;
                }
                //make the changes from cross1 to cross2
                for(int j = cross1; j <= cross2; j++) {
                    childTemp = child1.get(j);
                    child1.set(j,child2.get(j));
                    child2.set(j,childTemp);
                }
                //count satisfied and unsatisfied 
                for(int j = 0; j < antallChromo; j++) {
                    for(int k = j; k < antallChromo-1; k++) {
                        count1 = child1.get(j);
                        count2 = child2.get(j);
                        count3 = parent1.get(j);
                        count4 = parent2.get(j);

                        if(count1.equals(child1.get(k+1))==true) {
                            childFitness1++;
                        }
                        if(count2.equals(child2.get(k+1))==true) {
                            childFitness2++;
                        }
                        if(count3.equals(parent1.get(k+1))==true) {
                            parentFitness1++;
                        }
                        if(count4.equals(parent2.get(k+1))==true) {
                            parentFitness2++;
                        }
                    }
                }
                //put the two parents and childerens chromosoms and fitness in HashMaps
                parentNumb++;
                childNumb++;
                parentChromo.put(parentNumb, parent1);
                parentF.put(parentNumb, parentFitness1);
                childChromo.put(childNumb, child1);
                childF.put(childNumb, childFitness1);
                parentNumb++;
                childNumb++;
                parentChromo.put(parentNumb, parent2);
                parentF.put(parentNumb, parentFitness2);
                childChromo.put(childNumb, child2);
                childF.put(childNumb, childFitness2);
            }
            //merge parents and children to one list, one with chromosomes and one with fitness 
            parentChromo.putAll(childChromo);
            parentF.putAll(childF);

            //clear det arrayList to be ready for det next children
            childChromo.clear();
            sortert.clear();

            //sorted list with the best fitness
            for(int i = 1; i <= parentChromo.size(); i++) { sortert.add((i-1),parentF.get(i)); }
            Collections.sort(sortert);

            //remove the parents/child with the highest fitness. Highest unsatisfied
            for(int i = (parentF.size()/2); i < sortert.size(); i++) { parentF.values().remove(sortert.get(i)); }
            teller = 0;
            for(int i = 1; i <= (numbParent*2); i++) {
                if(parentF.containsKey(i) == false) {
                    parentChromo.remove(i);
                }
                if(parentF.containsKey(i)) {
                    teller++;
                    parentF.put(teller,parentF.remove(i));
                    parentChromo.put(teller, parentChromo.remove(i));
                }
            }
            //next generation
            genNumber++;
        }
        ArrayList<Integer> fitness = new ArrayList<Integer>();
        int best = Integer.MAX_VALUE, saved = 0;

        for(int i = 0; i < parentF.size(); i++) {
            fitness.add(i, parentF.get(i+1));
        }
        for(int i = 0; i < fitness.size(); i++) {
            if(fitness.get(i) < best) {
                best = fitness.get(i);
                saved = (i+1);
            }
        }
        //System.out.print("\nList of all the parent fitness after "+numberOfGen+" number of generations: \n"+parentF.entrySet());
        System.out.print("Number of Chromosome: "+antallChromo+" Number of parents: "+numbParent);
        System.out.print("\nBest fitness: "+best+" paretnumber: "+saved);
    }
}