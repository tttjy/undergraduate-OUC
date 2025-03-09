import tensorflow as tf
#from tensorflow import keras
import keras
import keras.optimizers
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
from keras.callbacks import CSVLogger, ReduceLROnPlateau
from utils import Create_Dataset, peak_signal_noise_ratio, name_sorting,psnr_tanh_norm_mu_tonemap
from model import model


def gpu_checker():
    gpus = tf.config.experimental.list_physical_devices('GPU')
    if gpus:
        try:
            for gpu in gpus:
                tf.config.experimental.set_memory_growth(gpu, True)
                print('GPU has been selected.\n\n')
        except RuntimeError as e:
            print(e)


if __name__ == '__main__':
    print('---------checking GPU---------\n')
    gpu_checker()
    
    print('---------Reading Model---------\n')
    model_ = model()
    model_.compile(optimizer=keras.optimizers.Adam(),
                    loss=['mae'],
                    metrics=[peak_signal_noise_ratio,psnr_tanh_norm_mu_tonemap])
    train_dir = 'E:/桌面/计算机视觉/HDR-VAM-main/Train/LDR/short/'
    val_dir = 'E:/桌面/计算机视觉/HDR-VAM-main/Train/Valid/short/'
    csv_path = 'E:/桌面/计算机视觉/HDR-VAM-main/history.csv'

    train_input_img_paths = name_sorting(train_dir)

    val_input_img_paths = name_sorting(val_dir)
    train_batch_size = 16
    valid_batch_size = 2

    call_back = [tf.keras.callbacks.ModelCheckpoint(filepath= 'weights.h5',save_best_only=True), 
                CSVLogger(csv_path),
                ReduceLROnPlateau(monitor='val_peak_signal_noise_ratio',factor=0.1,patience=25,verbose=0,mode='max')]

    print('---------Making Dataset Generators---------\n')
    train_gen = Create_Dataset(train_batch_size, train_input_img_paths)
    val_gen = Create_Dataset(valid_batch_size, val_input_img_paths,stage='valid',augment=False)

    if 'weights.h5' in os.listdir('E:/桌面/计算机视觉/HDR-VAM-main/HDR-VAM-main/weight/weight'):
        print('---------Loading Weights---------\n')
        model_.load_weights('E:/桌面/计算机视觉/HDR-VAM-main/HDR-VAM-main/weight/weights.h5')
    epochs = 100
    print('---------Training---------\n')
    history = model_.fit(train_gen, epochs=epochs,validation_data=val_gen,verbose="auto",callbacks=call_back)

