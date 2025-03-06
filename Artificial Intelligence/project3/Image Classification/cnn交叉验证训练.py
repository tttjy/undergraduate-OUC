import torch                      #具有自动求导机制autograd
import torch.nn as nn             #用来自动构建模型
import torch.optim as optim       #用来自动更新参数
from torchvision import datasets, transforms
from sklearn.model_selection import KFold   #k折交叉验证

 
# 定义超参数
k = 5                 #交叉验证参数
batch_size = 64       #批大小，常用的有32，64 128
epochs = 10           #周期
learning_rate = 0.01  #刚开始训练时：学习率以 0.01 ~ 0.001 为宜。
 
# 判断是否有可用的GPU
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
 
# 加载数据集归一化
transform_train = transforms.Compose([transforms.RandomCrop(32, padding=4),
                                      transforms.RandomHorizontalFlip(),
                                      transforms.ToTensor(),
                                      transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])
 
transform_test = transforms.Compose([transforms.ToTensor(),
                                     transforms.Normalize((0.5, 0.5, 0.5), (0.5, 0.5, 0.5))])
 
# 加载CIFAR10数据集
train_dataset = datasets.CIFAR10(root='./data', train=True, download=True, transform=transform_train)
test_dataset = datasets.CIFAR10(root='./data', train=False, download=True, transform=transform_test)
 
# 创建数据加载器
#train_loader = torch.utils.data.DataLoader(train_dataset, batch_size=batch_size, shuffle=True, num_workers=2)
#test_loader = torch.utils.data.DataLoader(test_dataset, batch_size=batch_size, shuffle=False, num_workers=2)
 
 
# 定义CNN模型
class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(3, 32, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(32, 64, kernel_size=3, padding=1)
        self.fc1 = nn.Linear(64 * 8 * 8, 512)
        self.fc2 = nn.Linear(512, 10)

    #向前传播
    def forward(self, x):
        x = nn.functional.relu(self.conv1(x))
        x = nn.functional.max_pool2d(x, 2)
        x = nn.functional.relu(self.conv2(x))
        x = nn.functional.max_pool2d(x, 2)
        x = x.view(-1, 64 * 8 * 8)
        x = nn.functional.relu(self.fc1(x))
        x = self.fc2(x)
        return x
 

#初始化 k-fold
kf = KFold(n_splits = k, shuffle = True)

# 初始化准确率列表，用于存储每次交叉验证的准确率
accuracy_list = []

# 交叉验证训练
for fold, (train_indices, val_indices) in enumerate(kf.split(train_dataset)):
  # 数据分为训练集和验证集
  train_sampler = torch.utils.data.sampler.SubsetRandomSampler(train_indices)
  val_sampler = torch.utils.data.sampler.SubsetRandomSampler(val_indices)
  train_loader = torch.utils.data.DataLoader(train_dataset, batch_size=batch_size, sampler=train_sampler)
  val_loader = torch.utils.data.DataLoader(train_dataset, batch_size=batch_size, sampler=val_sampler)

  # 初始化模型
  net = Net().to(device)

  # 定义损失函数和优化器
  criterion = nn.CrossEntropyLoss()
  optimizer = optim.SGD(net.parameters(), lr=learning_rate, momentum=0.9, weight_decay=5e-4)

  # 训练模型
  for epoch in range(epochs):
    running_loss = 0.0
    for i, data in enumerate(train_loader, 0):
        inputs, labels = data[0].to(device), data[1].to(device)
 
        # 梯度清零
        optimizer.zero_grad()
 
        outputs = net(inputs)
        loss = criterion(outputs, labels)
        #反向传播，自动求导机制
        loss.backward()
        optimizer.step()
 
        running_loss += loss.item()
 
    # 输出训练结果
    print("Fold [%d]/[%d] Epoch [%d]/[%d] Loss: %.3f" % (fold+1, k, epoch+1, epochs, running_loss/(i+1)))   
  print('训练完成')
 

  # 验证模型
  correct = 0
  total = 0
  with torch.no_grad():
    for data in val_loader:
        images, labels = data
        outputs = net(images)
        _, predicted = torch.max(outputs.data, 1)
        total += labels.size(0)
        correct += (predicted == labels).sum().item()

  accuracy = 100 * correct / total
  accuracy_list.append(accuracy)
  print('Validation Accuracy: %d %%' % (accuracy))
    
  classes = ('plane', 'car', 'bird', 'cat', 'deer', 'dog', 'frog', 'horse', 'ship', 'truck')
 
  class_correct = list(0. for i in range(10))
  class_total = list(0. for i in range(10))
  with torch.no_grad():
    for data in val_loader:
        images, labels = data[0].to(device), data[1].to(device)
        outputs = net(images)
        _, predicted = torch.max(outputs, 1)
        c = (predicted == labels).squeeze()
        for i in range(len(labels)):
            label = labels[i]
            class_correct[label] += c[i].item()
            class_total[label] += 1
 
  for i in range(10):
      print('Accuracy of %5s : %2d %%' % (
        classes[i], 100 * class_correct[i] / class_total[i]))

# 打印平均准确率
print('Average accuracy: %.2f %%' % (sum(accuracy_list) / k))
