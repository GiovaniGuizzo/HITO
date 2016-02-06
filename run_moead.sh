#!/bin/bash

problems="OO_MyBatis OA_AJHsqldb OA_AJHotDraw OO_BCEL OO_JHotDraw OA_HealthWatcher OO_JBoss"
functions="MultiArmedBandit"
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
Deltas="0.9"
nrs="3 6 30"
Cs="0.5 1.0 2.0 5.0"
Ws="150 300 600"

#Selected
paramNr=""
Ts="30"
Ds="0.1"
Deltas="0.9"
nrs="6"
Cs="5.0"
Ws="150"

rm -f runMAB$paramNr.txt

for objectives in $objectivesArray
do
        for function in $functions
        do
            for w in $Ws
            do
                for D in $Ds
                do
                    for T in $Ts
                    do
                        for delta in $Deltas
                        do
                            for NR in $nrs
                            do
                                if [ $NR -le $T ]; then
                                    for c in $Cs
                                    do
                                        path="experiment/MOEAD_"$NR"_"$D"_"$T"_"$delta"_"$c"_"$w"/"
                                        mkdir $path
                                        for problem in $problems
                                        do
                                                echo "java -Xms1024m -Xmx2048m -cp dist/HITO.jar hyperheuristics.main.MOEAD_MABHyperheuristicMain $population $evaluations $crossover $mutation $alpha $beta TwoPointsCrossover,MultiMaskCrossover,PMXCrossover SwapMutation,SimpleInsertionMutation $problem $function $w $c $objectives false $executions $path $T $NR $D $delta ucb" >> runMAB$paramNr.txt
                                                #ucb-v ucb-tunned ucb
                                        done
                                    done
                                fi
                            done
                        done
                    done
                done
            done
        done
done

cat runMAB$paramNr.txt | xargs -I CMD -P 24 bash -c CMD &
wait
rm -f runMAB$paramNr.txt
