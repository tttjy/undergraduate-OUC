import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from pylab import*
from matplotlib.font_manager import FontProperties
import os
from math import log2
from collections import Counter

#计算信息熵
def getEntropy(dataset):
    #求总样本数
    num = len(dataset)
    label_num = {}
    #统计共多少个类，以及每个类出现的次数
    for i in dataset:
        if i[-1] not in label_num.keys():
            label_num[i[-1]] = 0
        label_num[i[-1]] += 1
    e = 0
    for key in label_num:
        p = float(label_num[key]) / num
        e -= p * log2(p)
    return e

# 提取子集合
# 功能：从dataSet中先找到所有第axis个标签值 = value的样本
# 然后将这些样本删去第axis个标签值，再全部提取出来成为一个新的样本集
def splitData(data,index,value):
    subdata = []
    for row in data:
        if row[index] == value:
            subdata.append(np.hstack((row[:index],row[index+1:])))#np.hstack实现水平方向合并
    #print(subdata)
    return subdata

#选择当前最好的属性
def choose_best_feature(dataset,labels):
    num_of_feature = len(dataset[0]) - 1
    #计算当前数据集的信息熵
    current_ent = getEntropy(dataset)
    #初始化信息增益率
    max_gain_rate = 0.0
    #初始化最佳特征的下标
    best_feat = -1
    #枚举每种属性做划分的增益率
    for i in range(num_of_feature):
        # 用set()构造当前最佳特征取值的不重复集合
        feat_values = set([row[i] for row in dataset])
        new_ent = 0.0
        IV = 0.0
        for value in feat_values:
            sub_dataset = splitData(dataset,i,value)#把数据中属性i的值为value的行挑出来
            p = float(len(sub_dataset)) / len(dataset) #求占比
            IV -= p * log2(p)
            new_ent += p * getEntropy(sub_dataset)
        gain = current_ent - new_ent
        if len(feat_values) == 1: #只有一种属性值
            continue
        gain_rate = gain / IV #按公式计算信息增益率
        print(labels[i]," : ",gain_rate)
        if gain_rate > max_gain_rate:
            max_gain_rate = gain_rate
            best_feat = i
    return best_feat

# 返回具有最多样本数的那个标签的值
def maxClass(class_list):
    cnt = Counter(class_list)
    # 取cnt.most_common()中第一个元素中的第一个值，即为所求
    max_class = cnt.most_common()[0][0]
    return max_class

#按照伪代码编写决策树
def createTree(dataset,labels):
    class_list = [row[-1] for row in dataset]
    print(class_list)
    if class_list.count(class_list[0]) == len(class_list):#D中样本全属于同一类别，标记为叶节点
        return class_list[0]
    if len(dataset[0]) == 1: #D中样本在A上取值相同，标记为叶节点，其类别标记为D中样本数量最多的类
        return maxClass(class_list)
    #与这两种情况都不相符的话，就要从A中选取增益率最大的属性进行划分
    # 选取进行分支的最佳特征的下标
    best_feat = choose_best_feature(dataset,labels)
    print('\n')
    label = labels[best_feat]
    # 初始化决策树
    tree = {label: {}}
    labels.remove(labels[best_feat]) #已经使用过的属性不会再在分支中出现
    feat_value = set([row[best_feat] for row in dataset])# 取出各样本在当前最佳特征上的取值列表
    for i in feat_value:
        #为防止子进程修改父进程的labels，传入labels列表使用深拷贝
        tree[label][i] = createTree(splitData(dataset,best_feat,i),labels[:])# 子特征 = 当前特征（因为刚才已经删去了用过的特征）
    return tree

#可视化

mpl.rcParams['font.sans-serif'] = ['SimHei']
 
#定义文本框和箭头格式
decisionnode = dict(boxstyle="sawtooth", fc="0.8")
leafnode = dict(boxstyle="round4", fc="0.8")
arrow_args = dict(arrowstyle="<-")

#获取决策树叶子结点的数目
def getNumLeafs(inTree):
    numLeafs = 0
    firstStr = next(iter(inTree))
    secondDict = inTree[firstStr]
    for key in secondDict.keys():
        if type(secondDict[key]).__name__ == 'dict':
            numLeafs += getNumLeafs(secondDict[key])
        else:
            numLeafs += 1
    return numLeafs

#获取决策树的深度
def getTreeDepth(inTree):
    maxDepth = 0
    firstStr = next(iter(inTree))
    secondDict = inTree[firstStr]
    for key in secondDict.keys():
        if type(secondDict[key]).__name__ == 'dict':
            thisDepth = 1 + getTreeDepth(secondDict[key])
        else:
            thisDepth = 1
        if thisDepth > maxDepth:
            maxDepth = thisDepth
    return maxDepth

#绘制带箭头的注解
def plotNode(nodetxt, centerpt, parentpt, nodetype):
    createPlot.ax1.annotate(nodetxt,
                            xy=parentpt,
                            xycoords='axes fraction',
                            xytext=centerpt,
                            textcoords='axes fraction',
                            va='center',
                            ha='center',
                            bbox=nodetype,
                            arrowprops=arrow_args)


#标注有向边属性值
def plotMidText(cntrPt, parentPt, txtString):
    # 计算标注位置（箭头起始位置的中点处）
    xMid = (parentPt[0] - cntrPt[0]) / 2.0 + cntrPt[0]
    yMid = (parentPt[1] - cntrPt[1]) / 2.0 + cntrPt[1]
    createPlot.ax1.text(xMid, yMid, txtString, va="center", ha="center", rotation=30)

#绘制决策树
def plotTree(inTree,parentPt,nodeTxt):
    # 设置结点格式boxstyle为文本框的类型，sawtooth是锯齿形，fc是边框线粗细
    decisionNode = dict(boxstyle="sawtooth", fc="0.8")
    # 设置叶结点格式
    leafNode = dict(boxstyle="round4", fc="0.8")
    # 获取决策树叶结点数目，决定了树的宽度
    numLeafs = getNumLeafs(inTree)
    # 获取决策树层数
    depth = getTreeDepth(inTree)
    # 下个字典
    firstStr = next(iter(inTree))
    # 中心位置
    cntrPt = (plotTree.xoff + (1.0 + float(numLeafs)) / 2.0 / plotTree.totalW, plotTree.yoff)
    # 标注有向边属性值
    plotMidText(cntrPt, parentPt, nodeTxt)
    # 绘制结点
    plotNode(firstStr, cntrPt, parentPt, decisionNode)
    # 下一个字典，也就是继续绘制结点
    secondDict = inTree[firstStr]
    # y偏移
    plotTree.yoff = plotTree.yoff - 1.0 / plotTree.totalD
    for key in secondDict.keys():
        # 测试该结点是否为字典，如果不是字典，代表此结点为叶子结点
        if type(secondDict[key]).__name__ == 'dict':
            # 不是叶结点，递归调用继续绘制
            plotTree(secondDict[key], cntrPt, str(key))
        # 如果是叶结点，绘制叶结点，并标注有向边属性值
        else:
            plotTree.xoff = plotTree.xoff + 1.0 / plotTree.totalW
            plotNode(secondDict[key], (plotTree.xoff, plotTree.yoff), cntrPt, leafNode)
            plotMidText((plotTree.xoff, plotTree.yoff), cntrPt, str(key))
    plotTree.yoff = plotTree.yoff + 1.0 / plotTree.totalD

    
#创建绘图面板
def createPlot(inTree):
    fig = plt.figure(1,facecolor="white")
    fig.clf()
    axprops = dict(xticks=[],yticks=[])
    #去掉x、y轴
    createPlot.ax1 = plt.subplot(111,frameon=False,**axprops)
    #获取决策树叶节点数目
    plotTree.totalW = float(getNumLeafs(inTree))
    #获取决策树层数
    plotTree.totalD = float(getTreeDepth(inTree))
    #x偏移
    plotTree.xoff = -0.5 / plotTree.totalW
    plotTree.yoff = 1.0
    #绘制决策树
    plotTree(inTree,(0.5,1.0),'')
    #显示绘制结果
    plt.show()


if __name__=='__main__':
    #构造数据集
    file_path = 'E:/桌面/机器学习/实验3-基于信息增益率的决策树模型/隐形眼镜数据集/lenses_data.txt'
    dataset = np.loadtxt(file_path)
    labels = ['age','prescription','astigmatic','tear']
    dataset = dataset[:,1:] #去掉样本编号
    tree = createTree(dataset,labels)
    print(tree)
    createPlot(tree)
