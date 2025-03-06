import numpy as np
import pandas as pd


class DFA:
    def __init__(self,states,transitions,initial_state):
        self.states = states
        self.transitions = transitions
        self.current_state = initial_state

    def handle_input(self,input_str):
        process = []
        process.append(self.current_state)
        for char in input_str:
            if (self.current_state,char) in self.transitions:
                self.current_state = self.transitions[(self.current_state,char)]
                process.append(self.current_state)
            else:
                print('字符串中的字符不在输入符号集中')
                return 
        else:
            init = process[0]
            end = process[-1]
            if(init == end):
                result = "->".join(process)
                print('字符串可以被有限自动机接收,状态序列为：',result)
            else:
                print('字符串不可以被有限自动机接收')
            
#定义状态
states = ['q0','q1','q2','q3']

#定义转换规则
transitions = {
    ('q0','1'):'q1',
    ('q0','0'):'q2',
    ('q1','1'):'q0',
    ('q1','0'):'q3',
    ('q2','1'):'q3',
    ('q2','0'):'q0',
    ('q3','1'):'q2',
    ('q3','0'):'q1'
}

print('请输入字符串：')
while True:
    dfa = DFA(states,transitions,'q0')
    str = input()
    if str == "0":
        print('退出')
        break
    dfa.handle_input(str)
    
