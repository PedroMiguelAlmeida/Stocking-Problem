package stockingproblem;

import algorithms.IntVectorIndividual;
import ga.GeneticAlgorithm;

import java.util.Arrays;

public class StockingProblemIndividual extends IntVectorIndividual<StockingProblem, StockingProblemIndividual> {

    int[][] material;
    int maxColumn = 0;
    int amountCuts = 0;

    public StockingProblemIndividual(StockingProblem problem, int size) {
        super(problem, size);
        int firstPosition,secondPosition,holder;
        for (int i = 0;i < genome.length;i++){
            genome[i] = i;
        }
        for (int j=0;j<genome.length;j++){
            firstPosition = GeneticAlgorithm.random.nextInt(size);
            secondPosition = GeneticAlgorithm.random.nextInt(size);

            holder = genome[firstPosition];
            genome[firstPosition] = genome[secondPosition];
            genome[secondPosition] = holder;
        }
        System.out.println(Arrays.toString(genome));
    }

    public StockingProblemIndividual(StockingProblemIndividual original) {
        //everytime cloning happens
        super(original);
        this.material = original.material;
        this.amountCuts = original.amountCuts;
        this.maxColumn = original.maxColumn;
    }

    @Override
    public double computeFitness() {
        this.fitness = 0;
        this.amountCuts = 0;
        this.maxColumn = 0;
        material = new int[problem.getMaterialHeight()][problem.getMaterialWidth()];
        for (int genomes : genome) {
            for (int j = 0; j < problem.getMaterialWidth(); j++) {
                for (int k = 0; k < problem.getMaterialHeight(); k++) {
                    if (checkValidPlacement(problem.getItems().get(genomes), k, j)) {
                        materialPlacement(problem.getItems().get(genomes), k, j);
                        j = problem.getMaterialWidth();
                        break;
                    }
                }
            }
        }
        //horizon cuts
        for (int i=0;i < problem.getMaterialHeight();i++){
            for (int j=0;j< problem.getMaterialWidth() -1;j++){
                if (material[i][j] != material[i][j+1]){
                    amountCuts++;
                }
            }
        }
        //vertical cuts
        for (int i=0;i < problem.getMaterialHeight() -1;i++){
            for (int j=0;j< problem.getMaterialWidth();j++){
                if (material[i][j] != material[i+1][j]){
                    amountCuts++;
                }
            }
        }
        maxColumn++;
        this.fitness= (amountCuts+ problem.getMaterialHeight()+1)*0.2 + maxColumn *0.8;
        return fitness;
    }

    private boolean checkValidPlacement(Item item, int lineIndex, int columnIndex) {
        int[][] itemMatrix = item.getMatrix();
        for (int i = 0; i < itemMatrix.length; i++) {
            for (int j = 0; j < itemMatrix[i].length; j++) {
                if (itemMatrix[i][j] != 0) {
                    if ((lineIndex + i  ) >= material.length
                            || (columnIndex + j) >= material[0].length
                            || material[lineIndex + i][columnIndex + j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fitness: ");
        sb.append(fitness);
        sb.append("\npath: ");
        for (int i=0;i<genome.length;i++){
            sb.append(genome[i]).append("");
        }
        sb.append("\n");
        sb.append("Cuts: " + amountCuts);
        sb.append("\n");
        for (int i=0;i<problem.getMaterialHeight();i++){
            for (int j=0;j< problem.getMaterialWidth();j++){
                if (material[i][j]==0){
                    sb.append(" ");
                }else{
                    sb.append((char) material[i][j]);
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * @param i
     * @return 1 if this object is BETTER than i, -1 if it is WORST than I and
     * 0, otherwise.
     */
    @Override
    public int compareTo(StockingProblemIndividual i) {
        return Double.compare(i.getFitness(), this.fitness);
    }

    @Override
    public StockingProblemIndividual clone() {
        return new StockingProblemIndividual(this);

    }

    private void materialPlacement(Item item,int lineIndex,int columnIndex){
        int[][] matrixItem = item.getMatrix();
        for (int i = 0;i <matrixItem.length;i++){
            for (int j = 0; j<matrixItem[i].length;j++){
                if (matrixItem[i][j] != 0){
                    material[lineIndex+i][columnIndex+j] = item.getRepresentation();
                    if (columnIndex + j > maxColumn){
                        maxColumn = columnIndex+j;
                    }
                }
            }
        }
    }

}