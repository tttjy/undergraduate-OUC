import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import mpl_toolkits.axisartist as axisartist
import os
import csv

#数据归一化处理
file_path = 'E:/桌面/机器学习/实验2-线性判别分析/输血服务中心数据集/blood_data.txt'
df = pd.read_table(file_path,sep=',',header=None)
subset = df.iloc[:,:4]
normalized_data = (subset - subset.min()) / (subset.max() - subset.min())
df.update(normalized_data)
df.to_csv('E:/桌面/机器学习/实验2-线性判别分析/输血服务中心数据集/normalized_data.txt',sep=' ',index=0,header=0)

#读取数据集划分训练集和测试集
dataset = pd.read_table('E:/桌面/机器学习/实验2-线性判别分析/输血服务中心数据集/blood_data.txt',sep=',',header=None)

train_data = dataset.iloc[:600,:]
test_data = dataset.iloc[600:,:]
train_data.to_csv('E:/桌面/机器学习/实验2-线性判别分析/输血服务中心数据集/train_data.txt',sep=' ',index=0,header=0)
test_data.to_csv('E:/桌面/机器学习/实验2-线性判别分析/输血服务中心数据集/test_data.txt',sep=' ',index=0,header=0)

# 将DataFrame转换为NumPy数组
train_data = train_data.values  
test_data = test_data.values

#分离数据集和标签
train_target = train_data[:,-1].astype(int)
train_data = train_data[:,:-1]
test_target = test_data[:,-1].astype(int)
test_data = test_data[:,:-1]

#计算每个类别的均值向量
u0 = np.asmatrix(np.average(train_data[train_target[:] == 0],axis=0))
u1 = np.asmatrix(np.average(train_data[train_target[:] == 1],axis=0))
x0 = np.asmatrix(train_data[train_target[:] == 0])
x1 = np.asmatrix(train_data[train_target[:] == 1])

#计算类内散度矩阵Sw
S_w = (x0 - u0).T * (x0 - u0) + (x1 - u1).T * (x1 - u1)

#计算投影方向w
w = S_w.I * np.asmatrix(u0 - u1).T
print("w：",w)

#进行预测和可视化
centre0 = np.array(w.T * u0.T)
centre1 = np.array(w.T * u1.T)
predict_value = np.array(np.asmatrix(w).T * test_data.T)
y = [0 if abs(centre0 - i) < abs(centre1 - i) else 1 for i in predict_value[0]]
accracy = np.mean([y == test_target])
print("accracy：",accracy)

fig = plt.figure()
ax = axisartist.Subplot(fig,1,1,1)
fig.add_axes(ax)
plt.xlim([-0.001,0.01])

class0 = [i for i in predict_value[0] if abs(centre0 - i) < abs(centre1 - i)]
class1 = [i for i in predict_value[0] if abs(centre0 - i) >= abs(centre1 - i)]
ax.scatter(class0,np.zeros(len(class0)),color = 'blue',alpha=0.5)
ax.scatter(class1,np.zeros(len(class1)),color = 'green',alpha=0.5)
ax.scatter((centre0,centre1),(0,0),color='red',alpha=1)
plt.xlabel('LDA Component')
plt.ylabel(' ')
plt.title('LDA Blood Projection')
plt.show()
