#!/bin/bash

problems="OO_MyBatis OA_AJHsqldb OA_AJHotDraw OO_BCEL OO_JHotDraw OA_HealthWatcher OO_JBoss"
functions="ChoiceFunction"
objectivesArray="2"
alpha=1.0
beta=0.00005
evaluations=60000
population=300
crossover=1
mutation=1
executions=30

paramNr=$1
Ts="30"
Ds="0.1 0.3 0.5 0.7 0.9 1.0"
delta=0.9
nrs="3 6 30"
alpha=1.0
Betas="0.10 0.05 0.01 0.005 0.001 0.00005"
tipo=4
w=150
c=5

#Selected
paramNr=""
Ts="30"
Ds="0.3"
delta=0.9
nrs="6"
alpha=1.0
Betas="0.10"
#fix
Ds="1.0"

rm -f runCF$paramNr.txt

for objectives in $objectivesArray
do
        for function in $functions
        do
            for D in $Ds
            do
                for beta in $Betas
                do
                    for T in $Ts
                    do
                    	#NR=$T
                        for NR in $nrs
                        do
                            if [ $NR -le $T ]; then
                                path="experiment/MOEADCF_"$alpha"_"$beta"_"$T"_"$delta"_"$NR"_"$D"/"
                                mkdir $path
                                for problem in $problems
                                do
                                        echo "java -Xms1024m -Xmx2048m -cp dist/HITO.jar hyperheuristics.main.CFMOEAD_FRRHyperheuristicMain $population $evaluations $crossover $mutation $alpha $beta TwoPointsCrossover,MultiMaskCrossover,PMXCrossover SwapMutation,SimpleInsertionMutation $problem $function $w $c $objectives false $executions $path $T $NR $D $tipo" >> runCF$paramNr.txt
                                done
                            fi
                        done
                    done
                done
            done
        done
done

cat runCF$paramNr.txt | xargs -I CMD -P 16 bash -c CMD &
wait
rm -f runCF$paramNr.txt
