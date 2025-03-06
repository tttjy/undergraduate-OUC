#统计文本中词组合的频次以及单个词的频次
dic = {}
dic_single = {}
pun = set(['、','——','：','。','！','“','”','？','《','》','（','）','‘','’','；','，'])

with open(r'E:\桌面\自然语言处理\实验\实验3\pku_training.txt','r',encoding='utf-8') as f:
    end = True
    f_last = 'EOS'
    for line in f:
        begin = True  #每行开始
        temp = line.split() #去掉空格
        if begin and end:
            temp.insert(0,'BOS')
            begin = False

        if temp[-1] in pun:
            end = True
        else:
            end = False
        
        #逆向遍历 temp 中的词，若遇到标点符号，则将其替换为 'EOS'，并在其后插入 'BOS'
        for index in range(len(temp)-1,-1,-1):
            if temp[index] in pun:
                temp[index] = 'EOS'
                if index != len(temp)-1:
                    temp.insert(index+1,'BOS')

        # 如果上一行最后不是EOS，则需要插入本行的开始，否则会少一对键
        if f_last != 'EOS':
            temp.insert(0,f_last)
        for index in range(len(temp)):
            word = temp[index]
            if word == 'EOS':
                not_end = False
            else:
                not_end = True
            #遍历 temp 中的每个词，根据当前词和下一个词的组合构建词组名称 name，将该词组名称作为键，统计在 dic 中的出现次数。
            if index < len(temp)-1 and not_end:
                word_next = temp[index+1]
                name = '{}-{}'.format(word,word_next)
                if name not in dic:
                    dic[name]=1
                else:
                    dic[name] += 1
                #同时，统计每个单独词的频次，将词作为键，出现次数作为值，记录在 dic_single 中
                if word not in dic_single:
                    dic_single[word] = 1
                else:
                    dic_single[word] += 1
            #更新 f_last 为当前行的最后一个词，用于下一行的词组合
            if index == len(temp) - 1:
                f_last = temp[-1]

#print('sum:',sum(dic.values()))
with open(r'E:\桌面\自然语言处理\实验\实验3\training_pair.txt', 'w', encoding='utf-8') as f:
    f.write(str(dic))
with open(r'E:\桌面\自然语言处理\实验\实验3\training_one.txt', 'w', encoding='utf-8') as f:
    f.write(str(dic_single))

#计算给定句子列表中每个句子的概率
with open(r'E:\桌面\自然语言处理\实验\实验3\training_pair.txt', 'r', encoding='utf-8') as f:
    tmp_dict = eval(f.read())
with open(r'E:\桌面\自然语言处理\实验\实验3\training_one.txt', 'r', encoding='utf-8') as f:
    tmp_dict_single = eval(f.read())

sentence = 'BOS 中国 和 世界 人民 共同 发展 EOS'
sentence = sentence.split()
sum = 1.0 #存储句子概率的累积乘积
for index in range(1,len(sentence)):
    #遍历句子中的每个单词，计算当前单词与前一个单词组成的词对（二元组）的概率
    name = '{}-{}'.format(sentence[index - 1], sentence[index])
    try:
        #如果存在，则直接计算概率并更新 sum
        print(name, tmp_dict[name] / tmp_dict_single[sentence[index - 1]])
        temp = tmp_dict[name] / tmp_dict_single[sentence[index - 1]]
    except KeyError:
        #如果不存在，则使用平滑处理，概率为 1 除以前一个单词的频率加 1
        print(name, 1 / (tmp_dict_single[sentence[index - 1]] + 1))
        temp = 1 / (tmp_dict_single[sentence[index - 1]] + 1)  # 平滑处理,避免概率为0
    sum *= temp
print('句子的概率:',sum)
print()
