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
        x = [float(num) for num in line.split(' ') if num != '' and num != '[' and num != ']']
        data.append(x)
    return np.array(data)

def k_means(data, k):
    n = data.shape[0]
    #从样本中随机选择k哥样本作为初始的簇中心
    center = data[np.random.choice(n, k, replace=False)]
    #所有点标签都置为0
    lable = np.zeros(n)
    count=0
    while True:
        #计算每个点到簇的欧氏距离
        distance = np.array([np.linalg.norm(data - c, axis=1) for c in center])#np.linalg.norm计算二范数即欧氏距离
        #把该点的标签换为这个簇的序号
        lable = np.argmin(distance, axis=0)#np.argmin可以得到距离最近的簇中心的下标
        #求平均更新簇中心
        new_center = np.array([data[lable == i].mean(axis=0) for i in range(k)])
        count = count + 1
        #当值不再变化时，结束循环
        if np.allclose(center, new_center, atol=0.0001):
            print('迭代次数为：')
            print(count)
            return center, lable
        else:
            center = new_center

data = load_data(r'E:\桌面\机器学习\实验8-k-means\data.txt')

center,label = k_means(data,3)
print('聚类中心为：')
print(center)
#print(label)

#结果可视化
k = len(set(label))
x = [[] for i in range(k)]
y = [[] for i in range(k)]
for i in range(k):
    for j in range(data.shape[0]):
        if label[j] == i:
            x[i].append(data[j][0])
            y[i].append(data[j][1])
plt.figure()
for i in range(k):
    #绘制散点图
    plt.scatter(x[i],y[i],s=30)
    plt.scatter(center[i][0],center[i][1],marker='s',c='red')
plt.show()

