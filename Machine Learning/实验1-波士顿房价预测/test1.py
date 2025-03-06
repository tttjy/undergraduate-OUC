import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import csv

#观察数据发现有很多NaN确实，为提高梯度下降的速度，对数据进行归一化处理
df = pd.read_csv('E:/桌面/机器学习/实验一/波士顿房价数据集/HousingData.csv')
df = df.fillna(0)
features = df[['CRIM', 'ZN', 'INDUS','CHAS','NOX','RM','AGE','DIS','RAD','TAX','PTRATIO','B','LSTAT','MEDV']]

min_values = features.min()
max_values = features.max()

normalized_features = (features - min_values) / (max_values - min_values)

df[['CRIM', 'ZN', 'INDUS','CHAS','NOX','RM','AGE','DIS','RAD','TAX','PTRATIO','B','LSTAT','MEDV']] = normalized_features

df.to_csv('E:/桌面/机器学习/实验一/波士顿房价数据集/normalized_data.csv', index=False)


# 设置CSV文件路径
csv_file_path = 'E:/桌面/机器学习/实验一/波士顿房价数据集/normalized_data.csv'

# 读取CSV文件
with open(csv_file_path, 'r') as file:
    reader = csv.reader(file)
    dataset = list(reader)

# 划分训练集和测试集
train_data = dataset[:450]
test_data = dataset[450:506]

# 保存训练集到文件
train_file_path = 'E:/桌面/机器学习/实验一/波士顿房价数据集/train.csv'
os.makedirs(os.path.dirname(train_file_path), exist_ok=True)
with open(train_file_path, 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(train_data)

# 保存测试集到文件
test_file_path = 'E:/桌面/机器学习/实验一/波士顿房价数据集/test.csv'
os.makedirs(os.path.dirname(test_file_path), exist_ok=True)
with open(test_file_path, 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerows(test_data)

train_data = np.array(train_data)
test_data = np.array(test_data)

#提取特征和目标变量
#拆分数据集
x_train = train_data[1:,:-1].astype(float)
y_train = train_data[1:,-1].astype(float)
x_test = test_data[1:,:-1].astype(float)
y_test = test_data[1:,-1].astype(float)

#添加常数项列
x_train = np.c_[np.ones(x_train.shape[0]),x_train]
x_test = np.c_[np.ones(x_test.shape[0]),x_test]

#定义损失函数和梯度函数
def loss_function(x,y,beta):
    y_pred = x.dot(beta)
    mse = np.mean((y - y_pred) ** 2)
    return mse

def gradient(x, y, beta):
    y_pred = x.dot(beta)
    error = y_pred - y
    gradient = np.zeros(beta.shape)
    for i in range(len(beta)):
        gradient[i] = np.mean(error * x[:, i])
    return gradient

#初始化参数和学习率
learning_rate = 0.01
num_iterations = 10000
beta = np.zeros(x_train.shape[1])

mse_list = []
# 生成迭代次数的列表
iterations = np.arange(1, 10001)

# 梯度下降训练模型
for i in range(num_iterations):
    beta = beta - learning_rate * gradient(x_train, y_train, beta)
    mse = loss_function(x_train, y_train, beta)
    mse_list.append(mse)
    #if i % 100 == 0:
        #mse = loss_function(x_train, y_train, beta)
        #mse_list.append(mse)
        #print("第{}次迭代，均方误差：{}".format(i, mse))

#在测试集上进行预测并计算均方误差
y_pred = x_test.dot(beta)
mse = loss_function(x_test,y_test,beta)

#输出均方误差
print("均方误差（MSE）:",mse)
# 绘制MSE曲线
plt.plot(iterations, mse_list)
plt.xlabel('Iterations')
plt.ylabel('MSE')
plt.title('MSE vs. Iterations')
plt.show()
