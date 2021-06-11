package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class Recombination3<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {

    public Recombination3(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2) {
        int amountGenes = ind1.getNumGenes();
        int [] firstChild =new int[amountGenes];
        int[] secondChild = new int[amountGenes];
        int firstCut,secondCut;

        do {
            firstCut = GeneticAlgorithm.random.nextInt(amountGenes);
            secondCut = GeneticAlgorithm.random.nextInt(amountGenes);
        }while(firstCut>=secondCut);

        for (int i=0;i<amountGenes;i++){
            firstChild[i] = -1;
            secondChild[i] = -1;
        }

        for (int i = firstCut;i<=secondCut;i++){
            firstChild[i] = ind1.getGene(i);
            secondChild[i] = ind2.getGene(i);
        }

        int unusedMaterial[] = new int [amountGenes - (secondCut - firstCut) -1];
        int unusedMaterial2[] = new int [amountGenes - (secondCut - firstCut) -1];


        int i = 0;
        while( i<amountGenes){
            int firstChildAux = 0,secondChildAux=0;

            if (!checkGene(firstChild,ind2.getGene(i))){
                unusedMaterial[firstChildAux] = ind2.getGene(i);
                firstChildAux++;
            }

            if (!checkGene(secondChild,ind2.getGene(i))){
                unusedMaterial2[secondChildAux] = ind2.getGene(i);
                secondChildAux++;
            }
            i++;
        }

        i = 0;
        while( i<amountGenes){
            int firstChildAux = 0,secondChildAux=0;

            if (firstChild[i] == -1){
                firstChild[i] = unusedMaterial[firstChildAux];
                firstChildAux++;
            }

            if (secondChild[i] == -1){
                secondChild[i] = unusedMaterial[secondChildAux];
                secondChildAux++;
            }
            i++;
        }

        for (int j = 0;j<amountGenes;j++){
            ind1.setGene(j,firstChild[j]);
            ind2.setGene(j,secondChild[j]);
        }

    }

    private boolean checkGene(int[] child, int gene){
        for (int i=0;i<child.length;i++){
            if (child[i] == gene)
                return  true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "Recombination 3";
    }    
}