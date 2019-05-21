# Node_Influence_Based_Label_Propagation
A Node Influence Based Label Propagation Algorithm for Community Detection in Networks

<<Description of NIBLPA algorithm>>

Although this algorithm works like general LPA, there are some differences:
LPA:
    -Puts nodes in a list(called X) by a random function.
    -For each node, selects a label which has the most frequency in the node's neighborhood and assigns it as the node's new label.
    -If there are more than one label with the most frequency for a node, LPA chooses one of them randomly.

NIBLPA:
    -Sorts the nodes in a vector by their NI value(NI is obtained by KS value)
    -KS definition: It is assigned a k value to each node of the graph. Each node is located in a connected subgraph called k-shell in which each node has a minimum degree of k and can not be in a (k+1)-shell.
    -To calculate k value for each node, NIBLPA begins from KS = 0 and the value is increased one unit level by level. In each level, edges of nodes with maximum degree of KS are removed. This process will continue untill all edges are removed.
    -NIBLPA works just like LPA in assigning labels to nodes but for the condition when there are more than one label with the most frequency for a node, NIBLPA calculates LI value and chooses the label with the most LI value.
    -At last,(in both algorithms) nodes with same labels are located in a unique community.
    
    
<<A brief explaination for methods used in the NIBLPA code>>
    -Begin: Addigns a unique label to each node.
    -findKS: Calculates the k value for each node and puts them in an array called KS.
    -findNI: Calculates the NI value for each node by the formula written in the article.
    (Notice:
    N(i) in the formula means the set of nodes which are neighbours of node i. 
    d(j) means degree of node j. 
    ∝ is a constant value between 0 and 1 that can be assigned in the begining of code run by the user. Its value can influence the result)
    -Merge & sort: Implementation of merge-sort algorithm in order to sort vector X
    -sortX: Sorts the nodes in decreasing order of their NI value
    -calculateMaxLI: ّFor each node of the graph, calculates the LI value by the formula written in the article.
    -updateLabels: for the condition when there are more than one label with the most frequency for a node, this method calculates LI value and chooses the label with the most LI value.
    -divideCommunity:At the end of the algorithm when the labels will not change any more, nodes with same label will be located in one community implemented by hashSet.Also, "Result" 
    -detectCommunity: By calling this method, the whole NIBLPA algorithm will run in order and step by step.v
    
