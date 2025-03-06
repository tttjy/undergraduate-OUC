import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

#加载数据
def load_data(path):
    f = open(path)
    #数据中含有“[”，“]”python读入时不能自己处理
    lines = f.readlines()
    data = []
    for line in lines:
        line = line.strip('\n')
        x = [float(num) for num in line.split(' ') if num !='' and num != '[' and num != ']']
        data.append(x)
    return np.array(data)

def PCA(data,d):
    mean = np.mean(data, axis=0)
    #中心化
    X = data - mean
    #print(X.shape)
    #计算样本的协方差矩阵
    cov = np.cov(X.T)
    #print(cov.shape)
    #特征值分解，求特征值和特征向量
    eigvalues,eigvectors = np.linalg.eig(cov)
    #对特征值进行从大到小排序
    maxeig = np.argsort(eigvalues)[::-1]
    #print(maxeig.shape)
    w = eigvectors[:,maxeig[:d]]
    #print(w.shape)
    return w,data.dot(w)

data0 = load_data(r'E:\桌面\机器学习\实验9-PCA\data.txt')
w,data1 = PCA(data0,1)
print('投影矩阵为:')
print(w)
print('降维后的数据为：')
print(data1)
# 将降维后的数据写入txt文档
np.savetxt('reduced_data.txt', data1, fmt='%f', delimiter='\t')
    
