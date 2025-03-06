import numpy as np
import pandas as pd
import copy
import os
from collections import Counter
import math

#读取划分数据
def load(path):
    dataset = np.loadtxt(path)
    traindata = dataset[:20,1:] #去掉编号
    testdata = dataset[20:,1:]
    return traindata,testdata


#特征选择，采用向前搜索算法,通过信息增益评估
#计算信息熵
def getEntropy(datacol):
    p = pd.value_counts(datacol) / len(datacol)
    e = sum((-1) * p * np.log2(p))
    return e

#计算信息增益
def getGain(subset,dataset):
    dataA = dataset.groupby(subset)  #对属性子集A，假定根据其取值将D分成了V个子集
    EntDv = dataA.apply(lambda x:getEntropy(x[4])) #去除编号后第5列为目标变量
    p = dataA.size() / len(dataset[subset])
    GainA = getEntropy(dataset[4]) - sum(p*EntDv)
    return GainA

#前向搜索
def forward(dataset):
    subset = [] #特征子集
    subsetGain = 0 #特征自己的增益
    for i in range(1,5): #选择特征个数，数据集共4个特征
        maxGain = 0
        maxSubset = []  #最优候选特征子集
        for j in range(0,4): #逐步加入特征
            if j in subset:
                continue
            else:
                newSubset = copy.deepcopy(subset)
                newSubset.append(j)
                Gain = getGain(newSubset,dataset)
                if Gain > maxGain:
                    maxGain = Gain
                    maxSubset = newSubset
        if maxGain > subsetGain: #最优的候选(k+1)特征子集不如上一轮的选定集，则停止生成候选子集
            subsetGain = maxGain
            subset = maxSubset
        else:
            break
    return subset,subsetGain

#使用朴素贝叶斯训练模型
# 计算高斯分布概率密度函数
def gaussian_pdf(x, mean, var):
    return 1 / np.sqrt(2 * np.pi * var) * np.exp(- (x - mean)**2 / (2 * var))

#训练模型
def bayesModelTrain(train_x,train_y):
    #计算先验概率
    prior_p = {}  
    for i in np.unique(train_y):
        prior_p[i] = (np.sum(train_y == i) + 1) / (train_y.size + np.unique(train_y).size)
 
    likelihood = {}  # 存储该类别在每个特征上的似然概率
    for i in np.unique(train_y):
        likelihood[i] = {}
        data = train_x[train_y == i]
        for col in range(train_x.shape[1] - 1):
            for j in np.unique(train_x[:, col]):#添加拉普拉斯平滑
                likelihood[i][j] = ((data[data[:, col] == j]).shape[0] + 1) / (
                        data.shape[0] + np.unique(train_x[:, 0]).size)
    class_type = np.unique(train_y)
    return prior_p, likelihood, class_type

#模型分类测试
def bayestest(test_x,test_y,prior_p, likelihood, class_type):
    acc = 0
    predict = []
    for x, lable in zip(test_x, test_y):  # 测试样本
        max_p = 0
        y = 0
        for category in class_type:
            p = prior_p[category]
            for col,feature in enumerate(x):
                #p *= likelihood[category][feature]
                if col in likelihood[category].keys():  # 判断是否存在该特征
                        info = likelihood[category][col]
                        if isinstance(info, tuple) and len(info) == 2:  # 判断是否为合法的元组
                            mean, var = info
                            p *= gaussian_pdf(feature, mean, var)
            if max_p < p:
                max_p = p
                y = category
        predict.append(y)
        if y == lable:
            acc += 1
    return acc / test_x.shape[0], predict

if '__main__' == __name__:
    traindata,testdata = load(r'E:\桌面\机器学习\实验10-特征选择与分类\隐形眼镜数据集\lenses_data.txt')
    train_pd = pd.DataFrame(traindata)
    subset,gain = forward(train_pd)
    print('info_gain：',gain)
    #判断有多少特征被选择：
    features = ['age', 'spectacle', 'astigmatic', 'tear']
    print([features[i] for i in subset])
    print(subset)

    print('训练开始')
    prioriP, likelihood,class_type = bayesModelTrain(traindata[:,subset],traindata[:,-1])
    print('贝叶斯分类器的先验概率为:')
    print(prioriP)
    print('训练完成')

    print('模型评估开始')
    val,res = bayestest(testdata[:,subset],testdata[:,-1],prioriP,likelihood, class_type)
    print('朴素贝叶斯分类器的准确度为%.2f %%' % (val*100))
    print('结果是：',res)
    print('模型评估结束')
