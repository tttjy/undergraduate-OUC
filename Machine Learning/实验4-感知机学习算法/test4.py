import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

#划分数据集
dataset = np.loadtxt(r"E:\桌面\机器学习\实验4-感知机学习算法\感知机数据集\perceptron_data.txt")
data = dataset[:,:2]
label = dataset[:,-1]
label = [-1.0 if i == 0 else i for i in label]

#绘图
def picture(data,label,w,b):
    x1 = []
    x2 = []
    y1 = []
    y2 = []
    for x, y in zip(data, label):
        if y == 1:
            x1.append(x[0])
            y1.append(x[1])
        else:
            x2.append(x[0])
            y2.append(x[1])
    plt.figure()
    x = [i for i in range(-5, 5)]
    y = [-(w[0] / w[1]) * i - b / w[1] for i in x]
    plt.plot(x, y)
    plt.scatter(x1, y1, color='blue')
    plt.scatter(x2, y2, color='red')
    plt.show()


#感知机参数
lr = 0.01 #学习率
epochs = 100 #最大迭代次数
weight = None #权重值，初始化
bias = None #偏置项

#训练过程
#随机数组元素与标准差相乘，得到最终初始权重矩阵的值
weight = np.random.randn(data.shape[1])*np.sqrt(1/data.shape[1])
bias = 0
for i in range(epochs):
    for x,y in zip(data,label):
        #应用激活函数sign
        y_hat = 1 if np.dot(x,weight)+bias >= 0 else -1
        weight += lr*(y - y_hat)*x
        bias += lr*(y - y_hat)
    print("weight: " + str(weight) + ", bias: " + str(bias))
    #picture(data,label,weight,bias)
picture(data,label,weight,bias)

