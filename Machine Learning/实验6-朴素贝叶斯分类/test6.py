import numpy as np
import pandas as pd
import os
from collections import Counter
import math

#读取数据集
def load_mnist_from_txt(directory_path):
    images = []
    labels = []

    for filename in os.listdir(directory_path):
        if filename.endswith('.txt'):
            file_path = os.path.join(directory_path, filename)

            with open(file_path, 'r') as file:
                # 从文件名中解析标签信息
                label = int(filename.split('_')[0])
                labels.append(label)

                # 读取图像数据
                image_data = []
                for _ in range(32):
                    row = [int(value) for value in file.readline().strip()]
                    image_data.append(row)

                # 将图像数据转换为numpy数组，并添加到列表中
                images.append(np.array(image_data, dtype=np.float32))

    # 转换为numpy数组
    images = np.array(images)
    labels = np.array(labels)

    return images, labels

# 输出数据的形状
#print("训练图像的形状:", train_images.shape)
#print("训练标签的形状:", train_labels.shape)

#训练模型
def bayesModelTrain(train_x,train_y):
    #计算先验概率
    totalNum = train_x.shape[0]
    classNumDic = Counter(train_y)
    prioriP = np.array([classNumDic[i]/totalNum for i in range(10)])

    #计算类条件概率
    oldShape = train_x.shape
    train_x.resize((oldShape[0],oldShape[1]*oldShape[2]))
    posteriorNum = np.empty((10,train_x.shape[1]))
    posteriorP = np.empty((10,train_x.shape[1]))
    for i in range(10):
        posteriorNum[i] = train_x[np.where(train_y == i)].sum(axis=0)
        #拉普拉斯平滑
        posteriorP[i] = (posteriorNum[i] + 1) /(classNumDic[i]+2)
    train_x.resize(oldShape)
    return prioriP,posteriorP

#模型分类测试
def bayesClassifier(test_x,prioriP,posteriorP):
    oldShape = test_x.shape
    test_x.resize(oldShape[0]*oldShape[1])
    classP = np.empty(10)
    for j in range(10):
        temp = sum([math.log(1-posteriorP[j][x]) if test_x[x] ==
                    0 else math.log(posteriorP[j][x]) for x in range(test_x.shape[0])])
        classP[j] = np.array(math.log(prioriP[j]) + temp)
        classP[j] = np.array(temp)
    test_x.resize(oldShape)
    return np.argmax(classP)

#模型评估
def modelEvaluation(test_x, test_y, prioriP, posteriorP):
    bayesClassifierRes = np.empty(test_x.shape[0])
    for i in range(test_x.shape[0]):
        bayesClassifierRes[i] = bayesClassifier(test_x[i], prioriP, posteriorP)
    return bayesClassifierRes, (bayesClassifierRes == test_y).sum() / test_y.shape[0]

if __name__ == '__main__':
    directory_path1 = 'E:/桌面/机器学习/实验6-朴素贝叶斯分类/traindata'
    directory_path2 = 'E:/桌面/机器学习/实验6-朴素贝叶斯分类/testdata'
    print('加载数据集')
    train_images, train_labels = load_mnist_from_txt(directory_path1)
    test_images, test_labels = load_mnist_from_txt(directory_path2)
    print('加载完成')

    print('训练开始')
    prioriP, posteriorP = bayesModelTrain(train_images,train_labels)
    print('贝叶斯分类器的先验概率为:')
    print(prioriP)
    print('每个属性的估计条件概率为:')
    print(posteriorP)
    print('训练完成')

    print('模型评估开始')
    res,val = modelEvaluation(test_images,test_labels,prioriP,posteriorP)
    print('朴素贝叶斯分类器的准确度为%.2f %%' % (val*100))
    print('模型评估结束')
