import os
import numpy as np
import matplotlib.pyplot as plt

#加载数据
def load_data(path):
    f = open(path)
    #数据中含有“[”，“]”python读入时不能自己处理
    lines = f.readlines()
    data = []
    lable = []
    for line in lines:
        line = line.strip('\n')
        x = [float(num) for num in line.split(' ') if num != '' and num != '[' and num != ']']
        data.append(x[:-1])
        lable.append(x[-1])
    return np.array(data), np.array(lable)

#基学习算法选择朴素贝叶斯分类
def naive_bayes(data, label):
    n, m = data.shape
    # 计算类概率
    class_probs = {}
    for c in np.unique(label):
        class_probs[c] = np.sum(label == c) / n

    # 计算每个类别中每个特征的平均值和标准偏差
    class_means = {}
    class_stds = {}
    for c in np.unique(label):
        class_data = data[label == c]
        class_means[c] = np.mean(class_data, axis=0)
        class_stds[c] = np.std(class_data, axis=0)

    return class_probs, class_means, class_stds

def classify_naive_bayes(data, class_probs, class_means, class_stds):
    preds = []
    for x in data:
        max_prob = -1
        best_class = None
        for c, prob in class_probs.items():
            p = prob
            for i, (mean, std) in enumerate(zip(class_means[c], class_stds[c])):
                p *= (1 / (np.sqrt(2 * np.pi) * std)) * np.exp(-(x[i] - mean) ** 2 / (2 * std ** 2))
            if p > max_prob:
                max_prob = p
                best_class = c
        preds.append(best_class)
    return preds

def single_classifier(data, label, D):
    class_probs, class_means, class_stds = naive_bayes(data, label)
    print("Class Probabilities:", class_probs)
    print("Class Means:", class_means)
    print("Class Standard Deviations:", class_stds)
    return class_probs, class_means, class_stds

def adaboost(data, label, num):
    n, m = data.shape
    D = np.ones(n) / n  
    classifiers = []
    for i in range(num):
        print(f"Classifier {i+1} Info:")
        class_probs, class_means, class_stds = single_classifier(data, label, D)
        preds = classify_naive_bayes(data, class_probs, class_means, class_stds)
        
        # 计算权重错误
        weighted_error = np.sum(D * (preds != label))
        
        if weighted_error >= 0.5:
            break
        
        # 计算分类权重
        alpha = 0.5 * np.log((1 - weighted_error) / weighted_error)
        
        # 更新权重 
        D = D * np.exp(-alpha * label * preds)
        D /= np.sum(D)
        
        classifiers.append((alpha, class_probs, class_means, class_stds))
    return classifiers

def test(data, label, classifiers):
    errors = 0
    n = len(data)
    for x, y in zip(data, label):
        prediction = 0
        for alpha, class_probs, class_means, class_stds in classifiers:
            preds = classify_naive_bayes(np.array([x]), class_probs, class_means, class_stds)
            prediction += alpha * preds[0]
        if np.sign(prediction) != y:
            errors += 1
    return errors / n

# 绘制分类结果
def plot_classification_result(test_data, test_labels, classifiers):
    predictions = []
    for x in test_data:
        prediction = 0
        for alpha, class_probs, class_means, class_stds in classifiers:
            preds = classify_naive_bayes(np.array([x]), class_probs, class_means, class_stds)
            prediction += alpha * preds[0]
        predictions.append(prediction)

    predictions = np.array(predictions)
    predictions = np.sign(predictions)

    # 绘制决策边界
    plt.scatter(test_data[:, 0], test_data[:, 1], c=predictions, cmap='seismic')
    plt.scatter(test_data[:, 0], test_data[:, 1], c=test_labels, cmap='seismic')
    plt.xlabel('Feature 1')
    plt.ylabel('Feature 2')
    plt.title('Classification Result using AdaBoost')
    plt.show()


if __name__ == '__main__':
    train_data, train_labels = load_data(r'E:\桌面\机器学习\实验7-Adaboost\train.txt')
    test_data, test_labels = load_data(r'E:\桌面\机器学习\实验7-Adaboost\test.txt')

    num = 10 #分类器个数
    classifiers = adaboost(train_data, train_labels,num)
    test_error = test(test_data, test_labels, classifiers)
    print('测试集准确率为:')
    print(1 - test_error)
    # 绘制测试数据的分类结果
    plot_classification_result(test_data, test_labels, classifiers)
    plot_classification_result(train_data, train_labels, classifiers)
