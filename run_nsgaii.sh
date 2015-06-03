#!/bin/bash

problems="OO_MyBatis OA_AJHsqldb OA_AJHotDraw OO_BCEL OO_JHotDraw OA_HealthWatcher OO_JBoss"

functions="ChoiceFunction MultiArmedBandit Random"

objectivesArray="2 4"

alpha=1.0
beta=0.00005

w=150
c=5.0

evaluations=60000
population=300
crossover=1
mutation=1

executions=30

path="experiment/NSGA-II/"

rm -f run.txt

for objectives in $objectivesArray
do
        #echo "java -cp dist/MECBA-Hyp.jar jmetal.experiments.Combined_NSGAII_"$objectives"obj" >> run.txt
        for function in $functions
        do
                for problem in $problems
                do
                        echo "java -cp dist/MECBA-Hyp.jar hyperheuristics.main.NSGAIIHyperheuristicMain $population $evaluations $crossover $mutation $alpha $beta TwoPointsCrossover,MultiMaskCrossover,PMXCrossover SwapMutation,SimpleInsertionMutation $problem $function $w $c $objectives false $executions $path" >> run.txt
                done
        done
done

cat run.txt | xargs -I CMD -P 3 bash -c CMD &
wait

rm -f run.txt

#for objectives in $objectivesArray
#do
#        echo "java -cp dist/MECBA-Hyp.jar hyperheuristics.main.CompareHypervolumes $executions $path $path $path $objectives $problems" >> run.txt
#done
#
#cat run.txt | xargs -I CMD -P 3 bash -c CMD &
#wait
#
#rm -f run.txt

#zenity --info --text="Execuções finalizadas!"
