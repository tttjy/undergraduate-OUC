import numpy as np
import pandas as pd
from numpy import *
import matplotlib.pyplot as plt

#划分数据集
dataset = np.loadtxt(r"E:\桌面\机器学习\实验5-支持向量机\支持向量机数据集\dataset.txt")
data = dataset[:,:2]
label = dataset[:,-1]

#选择第二个变量
#不采用启发式的方法,随机选择一个和第一个变量不同的值(这一步可进行优化）
def alpha2(i,m):
    j = int(random.uniform(0,m))
    while(i == j):
        j = int(random.uniform(0,m))
    return j

#根据推导出的条件修改alpha2
def modifyalpha2(a,H,L):
    if a > H:
        a = H
    if a < L:
        a = L
    return a

#SMO算法对对偶问题进行最优化求解
def SMO(data,label,C,tr,epochs):#C是惩罚常数，tr是容错率
    m,n = data.shape
    #后续矩阵运算
    data = np.mat(data)
    label = np.mat(label).T
    alphas = mat(np.zeros((m,1)))
    b = 0
    epoch = 0

    while epoch < epochs:
        changed = 0
        for i in range(m):
            #计算预测值
            fxi = float(multiply(alphas, label).T * (data * data[i, :].T)) + b
            #计算误差
            Ei = fxi - float(label[i])
            #根据KKT条件判断是否需要更新alpha
            if((label[i] * Ei < -tr) and (alphas[i] < C)) or ((label[i] * Ei > tr) and(alphas[i] > 0 )):
                #选择第二个变量
                j = alpha2(i,m)
                fxj = float(multiply(alphas,label).T * (data * data[j,:].T)) + b
                Ej = fxj - float(label[j])
                pre_alpha1 = alphas[i].copy()
                pre_alpha2 = alphas[j].copy()

                #K = X * X^t
                K_ii = data[i,:] * data[i,:].T
                K_jj = data[j,:] * data[j,:].T
                K_ij = data[i,:] * data[j,:].T
                #计算二阶导数
                eta = 2 * K_ij - K_ii - K_jj
                if eta >= 0:
                    #print("此时的解不是极值点，跳过\n")
                    continue
                #根据推导出的公式计算alpha的取值范围
                if label[i] != label[j]:#情况23，两个变量异号
                    L = max(0,alphas[j] - alphas[i])
                    H = min(C,C + alphas[j] - alphas[i])
                else:#情况14，两个变量同号
                    L = max(0,alphas[i] + alphas[j] - C)
                    H = min(C,alphas[i] + alphas[j])
                if L == H:
                    #print("上界等于下界，跳过\n")
                    continue

                #根据推导出的公式更新alpha2
                alphas[j] -= label[j] * (Ei - Ej) / eta  #eta为负所以这里的计算要记得改为负号
                alphas[j] = modifyalpha2(alphas[j],H,L)
                #改进太小则忽略
                if (abs(alphas[j] - pre_alpha2) < 0.00001):
                    #print("The change in j is not significant")
                    continue
                #根据推导出的公式更新alpha1
                alphas[i] -= label[i] * label[j] * (alphas[j] - pre_alpha2)

                #根据推导出的公式，更新偏置项
                b1 = b - Ei - label[i] * (alphas[i] - pre_alpha1) * data[i,:] * data[i,:].T - label[j] * (alphas[j] - pre_alpha2) * data[i,:] * data[j,:].T
                b2 = b - Ej - label[i] * (alphas[i] - pre_alpha1) * data[i,:] * data[j,:].T - label[j] * (alphas[j] - pre_alpha2) * data[j,:] * data[j,:].T
                if 0 < alphas[i] < C:
                    b = b1
                elif 0 < alphas[j] < C:
                    b = b2
                else:
                    b = (b1 + b2) / 2
                changed += 1
                #print('迭代:{} i:{} num:{}'.format(epoch, i, changed))
        if changed == 0:
            epoch += 1
        else:
            epoch = 0
        print('迭代次数:{}'.format(epoch))
    return alphas,b

#获取权重
def get_w(alphas, dataset, labels):
    alphas, dataset, labels = np.array(alphas), np.array(dataset), np.array(labels)
    yx = labels.reshape(1, -1).T * np.array([1, 1]) * dataset
    w = np.dot(yx.T, alphas)
    return w.tolist()

#结果可视化
alphas,bias = SMO(data,label,1.0,0.01,50)
# 绘制数据点
x1, y1, x2, y2 = [], [], [], []
for i in range(len(data)):
    if label[i] == 1:
        x1.append(data[i][0])
        y1.append(data[i][1])
    else:
        x2.append(data[i][0])
        y2.append(data[i][1])

plt.figure()
plt.scatter(x1, y1, s=30, c='red', label='Class 1')
plt.scatter(x2, y2, s=30, c='blue', label='Class -1')

#绘制决策边界
weight = get_w(alphas,data,label)
a1,a2 = weight
bias = float(bias)
a1 = float(a1[0])
a2 = float(a2[0])

# 计算支持向量
support_vectors = []
for i, alpha in enumerate(alphas):
    if abs(alpha) > 0:
        x, y = data[i]
        # 检查该点是否为支持向量
        if y * (a1 * x + a2 * y + bias) <= 1:
            support_vectors.append(data[i])

# 绘制决策边界
y_decision = [(-bias - a1 * x) / a2 for x in data[:, 0]]
plt.plot(data[:, 0], y_decision, 'k-', label='Decision Boundary')

x = []
y = []
# 为每个支持向量画出直线
for sv in support_vectors:
    sv_x, sv_y = sv
    x.append(sv_x)
    y.append(sv_y)

    # 选择一个附近的点来画直线
    point_x = np.array([sv_x - 1, sv_x, sv_x + 1])
    point_y1 = (-bias - a1 * point_x - 1) / a2
    point_y2 = (-bias - a1 * point_x + 1) / a2
    plt.plot(point_x, point_y1, 'r--', alpha=0.5)
    plt.plot(point_x, point_y2, 'r--', alpha=0.5)
plt.scatter(x, y, s=150, c='none', alpha=0.7, linewidth=1.5, edgecolor='green', label='Support Vectors')

plt.xlabel('x1')
plt.ylabel('x2')
plt.title('Support Vector Machine')
plt.legend()
plt.show()
