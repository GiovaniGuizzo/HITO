#!/bin/bash  

#gera os componentes conectados
ccomps -v sccmap_OA_AJHotDraw.dot > sccmap_OA_AJHotDraw.txt
ccomps -v sccmap_OA_AJHsqldb.dot > sccmap_OA_AJHsqldb.txt
ccomps -v sccmap_OA_HealthWatcher.dot > sccmap_OA_HealthWatcher.txt
ccomps -v sccmap_OA_TollSystems.dot > sccmap_OA_TollSystems.txt
ccomps -v sccmap_OO_BCEL.dot > sccmap_OO_BCEL.txt
ccomps -v sccmap_OO_JBoss.dot > sccmap_OO_JBoss.txt
ccomps -v sccmap_OO_JHotDraw.dot > sccmap_OO_JHotDraw.txt
ccomps -v sccmap_OO_MyBatis.dot > sccmap_OO_MyBatis.txt

#alterar as palavras 'digraph' para 'graph' em todos os arquivos txt
find . -name "*.txt" -print | xargs sed -i 's/digraph/graph/g'

#gera os arquivos so com os clusters
dot -Tcanon sccmap_OA_AJHotDraw.txt > sccmap_OA_AJHotDraw_cluster.txt
dot -Tcanon sccmap_OA_AJHsqldb.txt > sccmap_OA_AJHsqldb_cluster.txt
dot -Tcanon sccmap_OA_HealthWatcher.txt > sccmap_OA_HealthWatcher_cluster.txt
dot -Tcanon sccmap_OA_TollSystems.txt > sccmap_OA_TollSystems_cluster.txt
dot -Tcanon sccmap_OO_BCEL.txt > sccmap_OO_BCEL_cluster.txt
dot -Tcanon sccmap_OO_JBoss.txt > sccmap_OO_JBoss_cluster.txt
dot -Tcanon sccmap_OO_JHotDraw.txt > sccmap_OO_JHotDraw_cluster.txt
dot -Tcanon sccmap_OO_MyBatis.txt > sccmap_OO_MyBatis_cluster.txt


#alterar as palavras 'digraph' para 'graph' em todos os arquivos txt
find . -name "*cluster.txt" -print | xargs sed -i 's/;//g'
find . -name "*cluster.txt" -print | xargs sed -i 's/\t\t//g'
find . -name "*cluster.txt" -print | xargs sed -i 's/}/\n/g'

#remove linus com a palavra 'subgraph'
#grep -v "subgraph" sccmap_OA_AJHotDraw_cluster.txt > sccmap_OA_AJHotDraw_cluster_ok.txt; 
#mv tmp.txt today.txt
grep -v "subgraph" sccmap_OA_AJHotDraw_cluster.txt > sccmap_OA_AJHotDraw_cluster_ok.txt; 
grep -v "subgraph" sccmap_OA_AJHsqldb_cluster.txt > sccmap_OA_AJHsqldb_cluster_ok.txt; 
grep -v "subgraph" sccmap_OA_HealthWatcher_cluster.txt > sccmap_OA_HealthWatcher_cluster_ok.txt; 
grep -v "subgraph" sccmap_OA_TollSystems_cluster.txt > sccmap_OA_TollSystems_cluster_ok.txt; 
grep -v "subgraph" sccmap_OO_BCEL_cluster.txt > sccmap_OO_BCEL_cluster_ok.txt; 
grep -v "subgraph" sccmap_OO_JBoss_cluster.txt > sccmap_OO_JBoss_cluster_ok.txt; 
grep -v "subgraph" sccmap_OO_JHotDraw_cluster.txt > sccmap_OO_JHotDraw_cluster_ok.txt; 
grep -v "subgraph" sccmap_OO_MyBatis_cluster.txt > sccmap_OO_MyBatis_cluster_ok.txt; 
