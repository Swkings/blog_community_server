package com.swking.util;

import com.swking.entity.TSPEvent;
import com.swking.service.TspTaskService;
import com.swking.type.Node;
import com.swking.type.TSPData;
import com.swking.type.TSPSolutionData;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/08/25
 * @File : TSPSolver
 * @Desc :
 **/

@Data
@Accessors(chain = true)
public class TSPSolver {

    @Autowired
    private TspTaskService tspTaskService;

    private TSPEvent tspEvent;
    static double MaxNum = 1e31;
    public TSPData run(){
        double[][] disMatrix = getDisMatrix();
        List<Integer> solution = SA(disMatrix);
        tspEvent.getTspData().setSOLUTION(solution);
        return tspEvent.getTspData();
    }
    public double getDis(Node A, Node B){
        return Math.sqrt(Math.pow(A.getX()-B.getX(), 2.0)+Math.pow(A.getY()-B.getY(), 2.0));
    }
    public double[][] getDisMatrix(){
        int dimension = tspEvent.getTspData().getDIMENSION();

        double[][] disMatrix = new double[dimension][dimension];
        List<Node> nodeList = tspEvent.getTspData().getNODE_COORD_SECTION();
        for (int i=0; i<dimension; i++){
            for (int j=i; j<dimension; j++){
                if(i==j){
                    disMatrix[i][i] = MaxNum;
                }else{
                    disMatrix[i][j] = this.getDis(nodeList.get(i), nodeList.get(j));
                    disMatrix[j][i] = this.getDis(nodeList.get(i), nodeList.get(j));
                }
            }
        }
        return disMatrix;
    }

    public double getTourDistance(double[][] disMatrix, List<Integer> tour){
        double distance = 0;
        for(int i=0; i<tour.size(); i++){
            int a = tour.get(i);
            int b = tour.get((i+1)%tour.size());
            distance += disMatrix[a][b];
        }
        return distance;
    }

    public List<Integer> greedy(double[][] disMatrix){
        int dimension = tspEvent.getTspData().getDIMENSION();
        List<Integer> solution = new ArrayList<>(dimension);
        int[] visit = new int[dimension];
        for(int i=0; i<dimension; i++){
            visit[i] = 0;
        }
        int cur = 0;
        int cnt = 1;
        visit[cur] = 1;
        solution.add(cur);
        while (cnt<dimension){
            int next = -1;
            double minDis = MaxNum;
            for(int i=0; i<dimension; i++){
                if(visit[i]==1) continue;
                if(disMatrix[cur][i] < minDis){
                    minDis = disMatrix[cur][i];
                    next = i;
                }
            }
            solution.add(next);
            cnt += 1;
            cur = next;
            visit[cur] = 1;
        }
        return solution;
    }

    public List<Integer> SA(double[][] disMatrix){
        int cityNum = tspEvent.getTspData().getDIMENSION();
        double sT = cityNum;
        double eT = 0.1;
        double alpha = 0.95;
        double mapkob = 10;
        List<Integer> tour = greedy(disMatrix);
        double fitness = getTourDistance(disMatrix, tour);
        List<Integer> globalTour = new ArrayList<>(tour);
        double globalFitness = fitness;
        int cnt = 0;
        // System.out.println("fitness:"+fitness);
        while (eT < sT){
            for (int i=0; i<mapkob; i++){
                List<Integer> newTour = mutation(tour);
                double newFitness = getTourDistance(disMatrix, newTour);
                // System.out.println("fitness:"+fitness+"  newFitness:"+newFitness);
                double delta = newFitness - fitness;
                if(delta < 0 || Math.random()<Math.exp(-delta/sT)){
                    tour = new ArrayList<>(newTour);
                    fitness = newFitness;
                    if(fitness < globalFitness){
                        globalFitness = fitness;
                        globalTour = new ArrayList<>(tour);
                    }
                }
            }
            sT *= alpha;
            cnt += 1;

            // if(cnt%10==0){
            //     int progress = 100-(int)((sT-eT)/(cityNum-eT));
            //     System.out.println("tspEvent.getId(): "+ tspEvent.getId()+ "  progress:"+progress);
            //     tspTaskService.updateTspTasksProgress(tspEvent.getId(), progress);
            //     cnt=1;
            // }
        }
        // System.out.println("globalFitness:"+getTourDistance(disMatrix, globalTour));
        // tspTaskService.updateTspTasksProgress(tspEvent.getId(), 100);
        return globalTour;
    }

    public List<Integer> mutation(List<Integer> tour) {
        Random random = new Random();
        int pA = random.nextInt(tour.size());
        int pB = random.nextInt(tour.size());
        while (pA == pB) {
            pB = random.nextInt(tour.size());
        }
        if (pA > pB) {
            int temp = pA;
            pA = pB;
            pB = temp;
        }
        List<Integer> newTour = new ArrayList<>(tour.size());
        if (Math.random() <= 0.5) {
            for (int i = 0; i < tour.size(); i++) {
                if (i == pA || i == pB) {
                    if (i == pA) {
                        newTour.add(tour.get(pB));
                    } else {
                        newTour.add(tour.get(pA));
                    }
                } else {
                    newTour.add(tour.get(i));
                }
            }
        } else {
            List<Integer> part = tour.subList(pA, pB+1);

            for (int i = 0; i < pA; i++) {
                newTour.add(tour.get(i));
            }
            for (int i = part.size() - 1; i >= 0; i--) {
                newTour.add(part.get(i));
            }
            for (int i = pB + 1; i < tour.size(); i++) {
                newTour.add(tour.get(i));
            }
        }
        return newTour;
    }
}
