from PIL import Image
from torchvision import transforms
import matplotlib.pyplot as plt
import torch                      #具有自动求导机制autograd
import torch.nn as nn             #用来自动构建模型
import torch.optim as optim       #用来自动更新参数
from torchvision import datasets, transforms
from sklearn.model_selection import KFold   #k折交叉验证

# 定义超参数
batch_size = 64       #批大小，常用的有32，64 128
epochs = 10           #周期
learning_rate = 0.01  #刚开始训练时：学习率以 0.01 ~ 0.001 为宜。
 
# 判断是否有可用的GPU
device = torch.device("cuda" if torch.cuda.is_available() else "cpu") 
 
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


#对卷积神经网络低层，中层和高层的特征图进行可视化展示(以一副图像为例）
model = Net().to(device)
# 从硬盘里加载图片
image = Image.open('baekhyun.jpg')

transforms = transforms.Compose([transforms.Resize(256),
                                 transforms.CenterCrop(224),
                                 transforms.ToTensor(),
                                 transforms.Normalize([0.485, 0.456, 0.406],
                                                      [0.229, 0.224, 0.225])])

# 图片需要经过一系列数据增强手段以及统计信息(符合ImageNet数据集统计信息)的调整，才能输入模型
image = transforms(image)
print(f"Image shape before: {image.shape}")
image = image.unsqueeze(0)
print(f"Image shape after: {image.shape}")

image = image.to(device)

#遍历得到网络所有卷积层及权重
model_weights = []   # 模型的权重
conv_layers = []   #模型的卷积层本身

# get all the model children as list
model_children = list(model.children())
for i in model.children():
     print(i)

# counter to keep count of the conv layers
counter = 0  # 统计模型里共有多少个卷积层

# append all the conv layers and their respective wights to the list
for i in range(len(model_children)):  # 遍历最表层(Sequential就是最表层)
    if type(model_children[i]) == nn.Conv2d:   # 最表层只有一个卷积层
        counter+=1
        model_weights.append(model_children[i].weight)
        conv_layers.append(model_children[i])

    elif type(model_children[i]) == nn.Sequential:
        for j in range(len(model_children[i])):
            for child in model_children[i][j].children():
                if type(child) == nn.Conv2d:
                    counter+=1
                    model_weights.append(child.weight)
                    conv_layers.append(child)
print(f"Total convolution layers: {counter}")

outputs = []
names = []

for layer in conv_layers[0:]:    # conv_layers即是存储了所有卷积层的列表
    image = layer(image)  # 每个卷积层对image做计算，得到以矩阵形式存储的图片，需要通过matplotlib画出
    outputs.append(image)
    names.append(str(layer))
print(len(outputs))

for feature_map in outputs:
    print(feature_map.shape)

print(outputs[1].shape)   # torch.Size([1, 64, 224, 224])  1代表图片数量，即1张图片；64代表颜色通道；后面两个是尺寸信息，为224*224的方块
print(outputs[1].squeeze(0).shape)   # torch.Size([64, 224, 224])  因为matplotlib绘画，这个第0维没用
print(torch.sum(outputs[1].squeeze(0),0).shape)   # torch.Size([224, 224])

processed = []

for feature_map in outputs:
    feature_map = feature_map.squeeze(0)  # torch.Size([1, 64, 224, 224]) —> torch.Size([64, 224, 224])  去掉第0维 即batch_size维
    gray_scale = torch.sum(feature_map,0)
    gray_scale = gray_scale / feature_map.shape[0]  # torch.Size([64, 224, 224]) —> torch.Size([224, 224])   从彩色图片变为黑白图片  压缩64个颜色通道维度，否则feature map太多张
    processed.append(gray_scale.data.cpu().numpy())  # .data是读取Variable中的tensor  .cpu是把数据转移到cpu上  .numpy是把tensor转为numpy

for fm in processed:
    print(fm.shape)

fig = plt.figure(figsize=(30, 50))

for i in range(len(processed)):  
    a = fig.add_subplot(5, 4, i+1)
    img_plot = plt.imshow(processed[i])
    a.axis("off")
    a.set_title(names[i].split('(')[0], fontsize=30)   # names[i].split('(')[0] 结果为Conv2d

plt.savefig('feature_maps.jpg', bbox_inches='tight')  # 若不加bbox_inches='tight'，保存的图片可能不完整
