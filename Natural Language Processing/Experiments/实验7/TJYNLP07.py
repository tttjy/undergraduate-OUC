import heapq

# 定义常量和数据结构
n = 5
strs = [""] * n  # 存储输入句子的列表
dictionary = set()  # 词典
m = 0  # 词典中最长单词的字数*2

# 读取词典文件并建立词典
def create_dic():
    global m
    with open("E:/桌面/自然语言处理/实验/实验7/pku_training.txt", 'r', encoding='utf-8') as ifs:
        for line in ifs:
            words = line.split()
            for word in words:
                if word not in dictionary:
                    dictionary.add(word)
                    m = max(m, len(word))

# 保存分词结果
def save_segmentation_results(results, filename):
    with open(filename, 'w', encoding='utf-8') as f:
        for result in results:
            f.write(' '.join(result) + '\n')

# 读取参考分词结果
def read_reference_segmentation(filename):
    reference = []
    with open(filename, 'r', encoding='utf-8') as f:
        for line in f:
            words = line.strip().split()
            reference.append(words)
    return reference

# 计算准确度、召回率和F1值
def evaluate_segmentation(predicted, reference):
    correct = 0
    predicted_total = 0
    reference_total = 0

    for p, r in zip(predicted, reference):
        predicted_total += len(p)
        reference_total += len(r)
        for wp, wr in zip(p, r):
            if wp == wr:
                correct += 1

    precision = correct / predicted_total
    recall = correct / reference_total
    f1 = 2 * (precision * recall) / (precision + recall)

    return precision, recall, f1

# 正向最大匹配算法函数
def forward_maximum_matching(s, dictionary, m):
    words = []
    i = 0
    while i < len(s):
        max_length = min(m, len(s) - i)
        for length in range(max_length, 0, -1):
            word = s[i:i+length]
            if word in dictionary:
                words.append(word)
                i += length
                break
        else:  # 如果没有匹配的单词，则按字符分词
            words.append(s[i])
            i += 1
    return words

# 最小分词法函数
def least_word_segmentation(s, dictionary):
    # 初始化图
    graph = {i: [] for i in range(len(s) + 1)}
    for i in range(len(s)):
        graph[i].append(i + 1)
        for j in range(i + 1, min(len(s), i + m) + 1):
            word = s[i:j]
            if word in dictionary:
                graph[i].append(j)

    # Dijkstra算法
    dis = [float('inf')] * (len(s) + 1)
    dis[0] = 0
    pre = [None] * (len(s) + 1)
    visited = [False] * (len(s) + 1)
    pq = [(0, 0)]

    while pq:
        d, v = heapq.heappop(pq)
        if visited[v]:
            continue
        visited[v] = True
        for nv in graph[v]:
            if dis[nv] > d + 1:
                dis[nv] = d + 1
                pre[nv] = v
                heapq.heappush(pq, (dis[nv], nv))

    # 输出结果
    res = []
    i = len(s)
    while i:
        res.append(s[pre[i]:i])
        i = pre[i]
    res.reverse()
    return res


# 主函数
def main():
    create_dic()  # 建立词典
    #for i in range(n):
        #strs[i] = input(f"Please input sentence {i+1}: ")
    strs=[
           "自然语言处理课程有意思",
           "毕业答辩如何准备",
           "他是研究计算机科学的一位科学家",
           "中国人为了实现自己的梦想",
           "顺应世界的未来我们十分高兴"]


    # 最大匹配算法
    print("The result of FMM:")
    fmm_results = []
    for s in strs:
        result = forward_maximum_matching(s, dictionary, m)
        fmm_results.append(result)
        print('/'.join(result))


    # 最小分词法
    print("\nThe result of Least word segmentation:")
    least_word_results = []
    for s in strs:
        result = least_word_segmentation(s, dictionary)
        least_word_results.append(result)
        print('/'.join(result))


    # 保存分词结果
    save_segmentation_results(fmm_results, 'E:/桌面/自然语言处理/实验/实验7/fmm_results.txt')
    save_segmentation_results(least_word_results, 'E:/桌面/自然语言处理/实验/实验7/least_word_results.txt')

    # 读取参考分词结果
    reference = read_reference_segmentation('E:/桌面/自然语言处理/实验/实验7/reference_segmentation.txt')

    # 计算准确度、召回率和F1值
    print("\nEvaluation results for FMM:")
    precision, recall, f1 = evaluate_segmentation(fmm_results, reference)
    print(f"Precision: {precision:.4f}")
    print(f"Recall: {recall:.4f}")
    print(f"F1: {f1:.4f}")

    print("\nEvaluation results for Least word segmentation:")
    precision, recall, f1 = evaluate_segmentation(least_word_results, reference)
    print(f"Precision: {precision:.4f}")
    print(f"Recall: {recall:.4f}")
    print(f"F1: {f1:.4f}")

if __name__ == "__main__":
    main()
