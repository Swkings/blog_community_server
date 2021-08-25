package com.swking.util;

import com.swking.entity.TSPEvent;
import com.swking.type.Node;
import com.swking.type.TSPData;
import com.swking.type.TSPSolutionData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

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
    private TSPEvent tspEvent;
    static double MaxNum = 1e31;
    public TSPData run(){
        int dimension = tspEvent.getTspData().getDIMENSION();
        List<Integer> solution = new ArrayList<>(dimension);
        double[][] disMatrix = getDisMatrix();
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

        tspEvent.getTspData().setSOLUTION(solution);
        return tspEvent.getTspData();
    }
    public double getDis(Node A, Node B){
        return Math.sqrt(Math.pow(A.getX()-B.getX(), 2)+Math.pow(A.getY()-B.getY(), 2));
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
                }
            }
        }
        return disMatrix;
    }
}
