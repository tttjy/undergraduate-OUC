#include <SFML/Graphics.hpp>
#include <fstream>
#include <iostream>
#include<math.h>
#include <sstream>
#include <string>
#include <windows.h>
#define SIZE 4  //8puzzle����3
using namespace sf;
using namespace std;

int main()
{
    RenderWindow app(sf::VideoMode(256, 256), "15-npuzzle!");//window�����ƿɸ��ģ����ﻻ����app��8puzzle����192
    app.setFramerateLimit(60); // call it once, after creating the window
    Texture t[20];
    t[0].loadFromFile("0.png");
    t[1].loadFromFile("1.png");
    t[2].loadFromFile("2.png");
    t[3].loadFromFile("3.png");
    t[4].loadFromFile("4.png");
    t[5].loadFromFile("5.png");
    t[6].loadFromFile("6.png");
    t[7].loadFromFile("7.png");
    t[8].loadFromFile("8.png");
    t[9].loadFromFile("9.png");
    t[10].loadFromFile("10.png");
    t[11].loadFromFile("11.png");
    t[12].loadFromFile("12.png");
    t[13].loadFromFile("13.png");
    t[14].loadFromFile("14.png");
    t[15].loadFromFile("15.png");
    int w = 64;//��ÿ����������ó��� 256/4=64
    int grid[SIZE][SIZE] = { 0 };
    Vector2f position[20];
    string line;
    Sprite sprite[10];
    
   
    //��ȡ�ļ�
    ifstream infile;
    infile.open("path3.txt", ios::in);//path1��path2��path3�ֱ��������׶ε�չʾ
    //�ж��Ƿ��ȡ�ɹ�
    if (!infile.is_open())
    {
        cout << "error" << endl;
        return 0;
    }
   
    
    while (app.isOpen())
    {
        Event e;
        while (app.pollEvent(e))
        {
            if (e.type == Event::Closed)
                app.close();
            int step = 0;
            while (getline(infile, line)) {
               
                istringstream is(line);
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        is >> grid[i][j];
                    }
                    
                }
                int cur = 0;
                for (int i = 0; i < SIZE; i++) {//�������飬�����ֵ�λ�ú�����ӳ�䵽������
                    for (int j = 0; j < SIZE; j++) {
                        int n = grid[i][j];
                        sprite[cur].setTexture(t[n]);
                        sprite[cur].setPosition(j*w, i*w);
                        app.draw(sprite[cur]);
                    }
                }
                app.display();
                cout << "��" << step << "��" << endl;
                step++;
                Sleep(700);//���ƻ����ƶ����ٶ�

                /*����move��������˼·
                if (pos.x == prex+1 && pos.y==prey) { dx = 1; dy = 0; };//����
                if (pos.x==prex && pos.y == prey + 1) { dx = 0; dy = 1; };//����
                if (pos.x==prex && pos.y == prey-1) { dx = 0; dy = -1; };//����
                if (pos.x == prex + 1 && pos.y == prey) { dx = -1; dy = 0; };//����
                animation
               
                if (grid[x + 1][y] == 0)
                {
                    dx = 1;
                    dy = 0;
                }
                if (grid[x][y + 1] == 0)
                {
                    dx = 0;
                    dy = 1;
                }
                if (grid[x][y - 1] == 0)
                {
                    dx = 0;
                    dy = -1;
                }
                if (grid[x - 1][y] == 0)
                {
                    dx = -1;
                    dy = 0;
                }

                int n = grid[x][y];
                grid[x][y] = 0;
                grid[x + dx][y + dy] = n;

                //animation
                sprite[blank].move(-dx * w, -dy * w);
                float speed = 3;

                for (int i = 0; i < w; i += speed)
                {
                    sprite[n].move(speed * dx, speed * dy);
                    sprite[n].setTexture(t[n]);
                    sprite[blank].setTexture(t[0]);
                    app.draw(sprite[blank]);
                    app.draw(sprite[n]);
                    app.display();
                }*/
                
      
            }


        }

    }
        infile.close();
        app.clear(Color::White);
        app.display();
    
    return 0;
}
