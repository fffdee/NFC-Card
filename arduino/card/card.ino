#include <ESP8266WiFi.h>        // 本程序使用 ESP8266WiFi库
#include <ESP8266WiFiMulti.h>   //  ESP8266WiFiMulti库
#include <ESP8266WebServer.h>   //  ESP8266WebServer库
#include <Ticker.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <Adafruit_NeoPixel.h>
#include <EEPROM.h>
#include "StringSplitter.h"

Adafruit_NeoPixel pixels(4, 5, NEO_GRB + NEO_KHZ800);

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP,"ntp1.aliyun.com",60*60*8,30*60*1000);

ESP8266WiFiMulti wifiMulti;     // 建立ESP8266WiFiMulti对象,对象名称是 'wifiMulti'
 
ESP8266WebServer esp8266_server(80);// 建立网络服务器对象，该对象用于响应HTTP请求。监听端口（80）
bool flag = false;
unsigned LEDRGB[4][3] = {0};
unsigned timeCount,timeCount2;
unsigned char play[4]={0xAA,0x02,0x00,0xAC};
unsigned char Pauses[4]={0xAA, 0x03, 0x00,0xAD};
unsigned char stops[4]={0xAA, 0x04, 0x00, 0xAE};
unsigned char last[4]={0xAA, 0x05, 0x00, 0xAF}; 
unsigned char next[4]={0xAA, 0x06, 0x00, 0xB0};

unsigned char Time_num[5][8] = {
{0,0,0,0,0,6,11,16},
 {0,0,0,0,1,7,12,17},
 {0,0,0,0,2,8,13,18},
{0,0,0,0,3,9,14,19},
{0,0,0,0,4,10,15,20},
};
unsigned char hour_SHI,hour_GE,min_SHI,min_GE;
unsigned char HourColor[3][3]={
  {255,0,0},
  {0,255,0},
  {0,0,255},
};

unsigned char color[10][3]{
  {0,0,0},
  {255,0,0},
  {255,153,0},
  {255,255,0},
  {0,255,0},
  {0,255,255},
  {0,0,255},
  {255,51,102},
  {102,51,152},
  {255,255,255},
};
 
unsigned char volue[5]={0xAA, 0x13, 0x01, 0x10, 0xCE};

unsigned char select_play[6]={0xAA, 0x07, 0x02, 0x00, 0x01, 0xB4}; 

int hour = 0,minute = 0, second = 0;

unsigned char volue_set[32] = {
  0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
  0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,
  0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,
  0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,
  
};
unsigned char SM[32] = {
  0xBE,0xBF,0xC0,0xC1,0xC2,0xC3,0xC4,0xC5,
  0xC6,0xC7,0xC8,0xC9,0xCA,0xCB,0xCC,0xCD,
  0xCE,0xCF,0xD0,0xD1,0xD2,0xD3,0xD4,0xD5,
  0xD6,0xD7,0xD8,0xD9,0xDA,0xDB,0xDC,0xDD

};
unsigned char play_set[48] = {
  0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
  0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0E,0x0F,
  0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,
  0x18,0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,
  0x20,0x21,0x22,0x23,0x24,0x25,0x26,0x27,
  0x28,0x29,0x2A,0x2B,0x2C,0x2D,0x2E,0x2F
};

unsigned char play_SM[48] = {
  0xB3,0xB4,0xB5,0xB6,0xB7,0xB8,0xB9,0xBA,
  0xBB,0xBC,0xBD,0xBE,0xBF,0xC0,0xC0,0xC1,
  0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,
  0xCA,0xCB,0xCC,0xCD,0xCE,0xCF,0xD0,0xD1,
  0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,
  0xDA,0xDB,0xDC,0xDD,0xDE,0xDF,0xe0,0xE1
};

Ticker time1,timeKey;

int plays = 0;
int Pausess = 0;
int stopss = 0; 
int lasts = 0;
int nexts = 0;
int volues = 25;
bool play_flag = false;
int nexttime;
int delaytime;

void setup(void){
  
  Serial.begin(9600);   // 启动串口通讯
  EEPROM.begin(25);
  volue[3] = volue_set[EEPROM.read(20)];
  volue[4] = SM[EEPROM.read(20)];
  Serial.println("音量为：");
  Serial.write(volue,5);
  
  Serial.println(EEPROM.read(20));

      select_play[4] = play_set[6]; 
    
      select_play[5] = play_SM[6];
      
      Serial.write(select_play,6);
  
  for(int i=0;i<5;i++){
    Time_num[0][i]=EEPROM.read(Time_num[0][i+4]);
    Time_num[1][i]=EEPROM.read(Time_num[1][i+4]);
    Time_num[2][i]=EEPROM.read(Time_num[2][i+4]);
    Time_num[3][i]=EEPROM.read(Time_num[3][i+4]);
    Time_num[4][i]=EEPROM.read(Time_num[4][i+4]);
    Serial.println("读取的数据："+String(Time_num[0][i])+" "+String(Time_num[1][i])+" "+String(Time_num[2][i])+" "+String(Time_num[3][i])+" "+String(Time_num[4][i])+" ");  
    delay(20);
  }
  time1.attach(1,clock_run);
  timeKey.attach(1,time_dis);

  wifiMulti.addAP("WIFI", "88888888"); // 将需要连接的一系列WiFi ID和密码输入这里
  wifiMulti.addAP("999", "beiyi128"); // ESP8266-NodeMCU再启动后会扫描当前网络
  wifiMulti.addAP("ssid_from_AP_3", "your_password_for_AP_3"); // 环境查找是否有这里列出的WiFi ID。如果有
  Serial.println("Connecting ...");                            // 则尝试使用此处存储的密码进行连接。
  
  int i = 0;                                 
  while(wifiMulti.run() != WL_CONNECTED) {  // 此处的wifiMulti.run()是重点。通过wifiMulti.run()，NodeMCU将会在当前
    delay(1000);                             // 环境中搜索addAP函数所存储的WiFi。如果搜到多个存储的WiFi那么NodeMCU
    Serial.print(i++); Serial.print('.');    // 将会连接信号最强的那一个WiFi信号。
  }                                          // 一旦连接WiFI成功，wifiMulti.run()将会返回“WL_CONNECTED”。这也是
                                             // 此处while循环判断是否跳出循环的条件。
  
  // WiFi连接成功后将通过串口监视器输出连接成功信息 
  Serial.println('\n');
  Serial.print("Connected to ");
  Serial.println(WiFi.SSID());              // 通过串口监视器输出连接的WiFi名称
  Serial.print("IP address:\t");
  Serial.println(WiFi.localIP());           // 通过串口监视器输出ESP8266-NodeMCU的IP
 
  esp8266_server.begin();                           // 启动网站服务
  esp8266_server.on("/", HTTP_GET, handleRoot);     // 设置服务器根目录即'/'的函数'handleRoot'
  esp8266_server.on("/control", HTTP_GET, handleControl);
  esp8266_server.on("/settime", HTTP_GET, SetTime);
  esp8266_server.on("/delaydis", HTTP_GET, disdelay);
  esp8266_server.on("/select", HTTP_GET,PlaySet);
  esp8266_server.on("/volue", HTTP_GET,voluechange);
  esp8266_server.onNotFound(handleNotFound);        // 设置处理404情况的函数'handleNotFound'
 
      select_play[4] = play_set[7]; 
    
      select_play[5] = play_SM[7];
      
      Serial.write(select_play,6);
  Serial.println("HTTP esp8266_server started");//  告知用户ESP8266网络服务功能已经启动 
  timeClient.begin();
  timeClient.update();
  
  Serial.println(timeClient.getFormattedTime());
  delay(1000);
  timeClient.update();
  Serial.println(timeClient.getFormattedTime());
  hour = timeClient.getHours();
  minute = timeClient.getMinutes();
  
   pixels.begin(); 
   pixels.clear();
   for(int i=0; i<3; i++) { // For each pixel...

    // pixels.Color() takes RGB values, from 0,0,0 up to 255,255,255
    // Here we're using a moderately bright green color:
    LEDRGB[0][i]=30*(i+1); LEDRGB[1][i]=30*(i+1); LEDRGB[2][i]=30*(i+1); LEDRGB[3][i]=30*(i+1);
    pixels.setPixelColor(0, pixels.Color(LEDRGB[0][0], LEDRGB[0][1], LEDRGB[0][2]));
    pixels.setPixelColor(1, pixels.Color(LEDRGB[1][0], LEDRGB[1][1], LEDRGB[1][2]));
    pixels.setPixelColor(2, pixels.Color(LEDRGB[2][0], LEDRGB[2][1], LEDRGB[2][2]));
    pixels.setPixelColor(3, pixels.Color(LEDRGB[3][0], LEDRGB[3][1], LEDRGB[3][2]));
    pixels.show();   // Send the updated pixel colors to the hardware.

    delay(500); // Pause before next pass through loop
    LEDRGB[0][i]=0; LEDRGB[1][i]=0; LEDRGB[2][i]=0; LEDRGB[3][i]=0;
    pixels.setPixelColor(0, pixels.Color(0, 0, 0));
    pixels.setPixelColor(1, pixels.Color(0, 0, 0));
    pixels.setPixelColor(2, pixels.Color(0, 0, 0));
    pixels.setPixelColor(3, pixels.Color(0, 0, 0));
    pixels.show();
  }
  pinMode(0,INPUT_PULLUP);
  pinMode(13,OUTPUT);  
  pinMode(2,OUTPUT);  
  digitalWrite(13,HIGH);
  delay(100);
  digitalWrite(13,LOW);
  pinMode(13,INPUT);

}
 
void loop(void){
  esp8266_server.handleClient();                     // 检查http服务器访问
   
 if(digitalRead(0)==0){
    delay(20);
    
    if(digitalRead(0)==0){
    play_flag = true;
    digitalWrite(2,HIGH);
    Serial.println("进入刷卡模式");
    Serial.println(play_flag);
   
 }
 }
   if(play_flag == true){
  if(digitalRead(13)==1){
     digitalWrite(2,LOW);
     play_flag = false;
      select_play[4] = play_set[1]; 
    
      select_play[5] = play_SM[1];
      
      Serial.write(select_play,6);
      Serial.println(play_flag);
    
     Serial.println("刷卡");

  }
   } 
    
}
 
/*设置服务器根目录即'/'的函数'handleRoot'
  该函数的作用是每当有客户端访问NodeMCU服务器根目录时，
  NodeMCU都会向访问设备发送 HTTP 状态 200 (Ok) 这是send函数的第一个参数。
  同时NodeMCU还会向浏览器发送HTML代码，以下示例中send函数中第三个参数，
  也就是双引号中的内容就是NodeMCU发送的HTML代码。该代码可在网页中产生LED控制按钮。 
  当用户按下按钮时，浏览器将会向NodeMCU的/LED页面发送HTTP请求，请求方式为POST。
  NodeMCU接收到此请求后将会执行handleLED函数内容*/
void handleRoot() {       
  esp8266_server.send(200, "text/html", "<form action=\"/control\" method=\"POST\"><input type=\"submit\" value=\"Toggle LED\"></form>");
}

//处理LED控制请求的函数'handleLED'
void handleControl() {
  plays = esp8266_server.arg("play").toInt();
  volues =esp8266_server.arg("volue").toInt(); 
  int nexts =esp8266_server.arg("next").toInt();
  int lasts =esp8266_server.arg("last").toInt();   
  int light =esp8266_server.arg("light").toInt();  
  int talktime  =esp8266_server.arg("talktime").toInt(); 
  int distime  =esp8266_server.arg("distime").toInt(); 
  int clean  =esp8266_server.arg("clean").toInt();                           
 // digitalWrite(LED_BUILTIN,!digitalRead(LED_BUILTIN));// 改变LED的点亮或者熄灭状态

//  esp8266_server.sendHeader("Location","/");          // 跳转回页面根目录
  //esp8266_server.send(303); 
  if(clean == 1){

   
    
    for(int i=0;i<20;i++){
    EEPROM.write(i,61);
    EEPROM.commit();
 
    delay(20);
    
    }
   }
  

  Serial.println("**********");
   if(talktime == 1){

   
    EEPROM.write(21,1);
    EEPROM.commit();
    
   }else if(talktime == 0){
    
    EEPROM.write(21,0);
    EEPROM.commit();
   }
   if(distime == 1){
      EEPROM.write(22,1);
      EEPROM.commit();
   }else if(distime == 0){
      EEPROM.write(22,0);
      EEPROM.commit();
   }  
   if(light == 1){
    pixels.setPixelColor(0, pixels.Color(255, 255, 255));
    pixels.setPixelColor(1, pixels.Color(255, 255, 255));
    pixels.setPixelColor(2, pixels.Color(255, 255, 255));
    pixels.setPixelColor(3, pixels.Color(255, 255, 255));
    pixels.show();
    if(EEPROM.read(22)){
      EEPROM.write(22,0);
      EEPROM.commit();
    }
   }else if(light == 0){
    pixels.setPixelColor(0, pixels.Color(0, 0, 0));
    pixels.setPixelColor(1, pixels.Color(0, 0, 0));
    pixels.setPixelColor(2, pixels.Color(0, 0, 0));
    pixels.setPixelColor(3, pixels.Color(0, 0, 0));
    pixels.show();
   }
   if(plays==1){
   
     Serial.println("停止");
      digitalWrite(LED_BUILTIN, LOW);
      
     Serial.write(volue,5);
    Serial.write(play,4);
   digitalWrite(LED_BUILTIN, HIGH);
   
   }else if(play == 0){
   
       Serial.println("播放");
    digitalWrite(LED_BUILTIN, LOW);
      Serial.write(Pauses,4);
   }
    if(nexts==1){
    Serial.println("下一首");
    digitalWrite(LED_BUILTIN, LOW);
    Serial.write(volue,5);
    Serial.write(next,4);
    delay(500);
    digitalWrite(LED_BUILTIN, HIGH);
  
   
   }
    if(lasts==1){
    Serial.println("上一首");
    digitalWrite(LED_BUILTIN, LOW);
    Serial.write(volue,5);
    Serial.write(last,4);
    delay(500);
    digitalWrite(LED_BUILTIN, HIGH);
  
  
   
   }

    if((esp8266_server.arg("timeup").toInt())==1)
    {
      timeClient.update();
      Serial.println(timeClient.getFormattedTime());
      hour = timeClient.getHours();
      Serial.println("更新时间");
      minute = timeClient.getMinutes();
    }
  
}
void voluechange() {
  volues =esp8266_server.arg("volue").toInt(); 
  volue[3] = volue_set[volues];
  volue[4] = SM[volues];
  Serial.write(volue,5);
  Serial.println("设置音量为");
  Serial.println(volues);
  EEPROM.write(20,volues);
  EEPROM.commit();

}


//定时器中断
void clock_run(){
    second++;
    if(second==60){
      second = 0;
      minute++;


      if(minute==60){
        minute=0;
        hour++;
        if(hour==24);
        {
          hour=0;
        }
      }
    }
    
    Serial.println("现在卡片的时间是："+String(hour)+":"+String(minute)+":"+String(second));
 if(EEPROM.read(21)==1){
      if((minute==0)&&((second > 0)&&(second < 2))){

        switch(hour){
          case 7:
          select_play[4] = play_set[8]; 
  
          select_play[5] = play_SM[8];
    
          Serial.write(select_play,6);
          break;
          case 8:
          select_play[4] = play_set[9]; 
  
          select_play[5] = play_SM[9];
    
          Serial.write(select_play,6);
          break;
          case 9:
          select_play[4] = play_set[10]; 
  
          select_play[5] = play_SM[10];
    
          Serial.write(select_play,6);
          break;
          case 10:
          select_play[4] = play_set[11]; 
  
          select_play[5] = play_SM[11];
    
          Serial.write(select_play,6);
          break;
          case 11:
          select_play[4] = play_set[12]; 
  
          select_play[5] = play_SM[12];
    
          Serial.write(select_play,6);
          break;
          case 12:
          select_play[4] = play_set[13]; 
  
          select_play[5] = play_SM[13];
    
          Serial.write(select_play,6);
          break;
          case 13:
          select_play[4] = play_set[2]; 
  
          select_play[5] = play_SM[2];
    
          Serial.write(select_play,6);
          break;
          case 14:
          select_play[4] = play_set[3]; 
  
          select_play[5] = play_SM[3];
    
          Serial.write(select_play,6);
          break;
          case 15:
          select_play[4] = play_set[4]; 
  
          select_play[5] = play_SM[4];
    
          Serial.write(select_play,6);
          break;
          case 16:
          select_play[4] = play_set[5]; 
  
          select_play[5] = play_SM[5];
    
          Serial.write(select_play,6);
          break;
          case 17:
          select_play[4] = play_set[6]; 
  
          select_play[5] = play_SM[6];
    
          Serial.write(select_play,6);
          break;       
          case 18:
          select_play[4] = play_set[7]; 
  
          select_play[5] = play_SM[7];
    
          Serial.write(select_play,6);
          break;
          case 19:
          select_play[4] = play_set[8]; 
  
          select_play[5] = play_SM[8];
    
          Serial.write(select_play,6);
          break;

          case 20:
          select_play[4] = play_set[9]; 
  
          select_play[5] = play_SM[9];
    
          Serial.write(select_play,6);
          break;

          case 21:
          select_play[4] = play_set[10]; 
  
          select_play[5] = play_SM[10];
    
          Serial.write(select_play,6);
          break;

          case 22:
          select_play[4] = play_set[11]; 
  
          select_play[5] = play_SM[11];
    
          Serial.write(select_play,6);
          break;

          case 23:
          select_play[4] = play_set[12]; 
  
          select_play[5] = play_SM[12];
    
          Serial.write(select_play,6);
          break;
          case 0:
          select_play[4] = play_set[13]; 
  
          select_play[5] = play_SM[13];
    
          Serial.write(select_play,6);
          break;
          
          
        }
        
      }
 }
      
      if((EEPROM.read(6)==hour)&&(EEPROM.read(11)==minute)&&((second > 0)&&(second < 2))){
         select_play[4] = play_set[EEPROM.read(0)]; 

        select_play[5] = play_SM[EEPROM.read(0)];
  
        Serial.write(select_play,6);
      }
 
    if((EEPROM.read(7)==hour)&&(EEPROM.read(12)==minute)&&((second > 0)&&(second < 2))){
        select_play[4] = play_set[EEPROM.read(1)]; 

        select_play[5] = play_SM[EEPROM.read(1)];
  
        Serial.write(select_play,6);
        
      }
  
         if((EEPROM.read(8)==hour)&&(EEPROM.read(13)==minute)&&((second > 0)&&(second < 2))){
        select_play[4] = play_set[EEPROM.read(2)]; 

        select_play[5] = play_SM[EEPROM.read(2)];
  
        Serial.write(select_play,6);        
        
      }

         if((EEPROM.read(9)==hour)&&(EEPROM.read(14)==minute)&&((second > 0)&&(second < 2))){
        select_play[4] = play_set[EEPROM.read(3)]; 

        select_play[5] = play_SM[EEPROM.read(3)];
  
        Serial.write(select_play,6);        
        
      }

         if((EEPROM.read(10)==hour)&&(EEPROM.read(15)==minute)&&((second > 0)&&(second < 2))){
        select_play[4] = play_set[EEPROM.read(4)]; 

        select_play[5] = play_SM[EEPROM.read(4)];
  
        Serial.write(select_play,6);        
        
      }
   
   
 
    
}

void time_dis(){
      hour_SHI = hour/10;
      hour_GE = hour%10;
      min_SHI = minute/10;
      min_GE = minute%10;  
   
if(flag){
  timeCount++;
}
    
    
  /*  if(((hour>14)&&(hour<20))||((hour>4)&&(hour<10))){
    hour_SHI = (hour/10)-1;
    }*/
    if(EEPROM.read(22)==1){
    if(timeCount>EEPROM.read(24)){
      pixels.setPixelColor(0, pixels.Color(0, 0, 0));
        pixels.setPixelColor(1, pixels.Color(0, 0, 0));
        pixels.setPixelColor(2, pixels.Color(0, 0, 0));
        pixels.setPixelColor(3, pixels.Color(0, 0, 0));
        pixels.show();
        flag  = false;
       timeCount = 0; 
    }
 
 


    timeCount2++;
 
    if(timeCount2>=EEPROM.read(23))
    {    
        flag = true;
        pixels.setPixelColor(1, pixels.Color(HourColor[hour_SHI][0], HourColor[hour_SHI][1], HourColor[hour_SHI][2]));
        pixels.setPixelColor(0, pixels.Color(color[hour_GE][0], color[hour_GE][1], color[hour_GE][2]));
        pixels.setPixelColor(2, pixels.Color(color[min_SHI][0], color[min_SHI][1], color[min_SHI][2]));
        pixels.setPixelColor(3, pixels.Color(color[min_GE][0], color[min_GE][1], color[min_GE][2]));
        pixels.show();   // Send the updated pixel colors to the hardware.
        delay(3000);

        timeCount2=0;
        
    } 
   }
}

void disdelay(){
  unsigned char Dis[3]={0};
  String dis = String(esp8266_server.arg("delaydis"));
    Serial.println("脑钟设置：");
    Serial.println(dis);
     StringSplitter *splitter = new StringSplitter(dis, 'A', 2);
     int itemCount = splitter->getItemCount();
    Serial.println("Item count: " + String(itemCount));

    for(int i = 0; i < itemCount; i++){
    String item = splitter->getItemAtIndex(i);
     int Get= splitter->getItemAtIndex(i).toInt();
    if(Get>0){
    Dis[i]= splitter->getItemAtIndex(i).toInt();
    Serial.println(Get);
    }  
     
    Serial.println("Item @ index " + String(i) + ": " + String(item)); 
    }
    EEPROM.write(23,Dis[0]);
    EEPROM.commit();
    EEPROM.write(24,Dis[1]);
    EEPROM.commit();
}
void SetTime() {
    unsigned char Time[4]={0};
   String Set = String(esp8266_server.arg("setting"));
    Serial.println("脑钟设置：");
    Serial.println(Set);
     StringSplitter *splitter = new StringSplitter(Set, 'A', 4);
     int itemCount = splitter->getItemCount();
    Serial.println("Item count: " + String(itemCount));

    for(int i = 0; i < itemCount; i++){
    String item = splitter->getItemAtIndex(i);
    int Get= splitter->getItemAtIndex(i).toInt();
    if(Get>0){
    Time[i]= splitter->getItemAtIndex(i).toInt();
    Serial.println(Get);
    }
    Serial.println("Item @ index " + String(i) + ": " + String(item)); 
    }
    
    EEPROM.write(Time_num[Time[1]-1][4],Time[0]);
    EEPROM.commit();
    EEPROM.write(Time_num[Time[1]-1][5],Time[2]);
    EEPROM.commit();
    EEPROM.write(Time_num[Time[1]-1][6],Time[3]);
    EEPROM.commit();
     for(int i=0;i<5;i++){
    Time_num[0][i]=EEPROM.read(Time_num[0][i+4]);
    Time_num[1][i]=EEPROM.read(Time_num[1][i+4]);
    Time_num[2][i]=EEPROM.read(Time_num[2][i+4]);
    Time_num[3][i]=EEPROM.read(Time_num[3][i+4]);
    Time_num[4][i]=EEPROM.read(Time_num[4][i+4]);
    Serial.println("读取的数据："+String(Time_num[0][i])+" "+String(Time_num[1][i])+" "+String(Time_num[2][i])+" "+String(Time_num[3][i])+" "+String(Time_num[4][i])+" ");  
    delay(20);
  }
  
} 


void PlaySet() {
  
  int demo = esp8266_server.arg("ok").toInt();
  
  Serial.write(volue,5);
  select_play[4] = play_set[demo]; 

  select_play[5] = play_SM[demo];
  
  Serial.write(select_play,6);
 // Serial.write(play,4);
  Serial.println("*********************");
  for(int i = 0; i<6;i++){ 
    
    Serial.print(select_play[i],HEX);
    
  }
  Serial.println("*********************");
  
}
// 设置处理404情况的函数'handleNotFound'
void handleNotFound(){
  esp8266_server.send(404, "text/plain", "404: Not found"); // 发送 HTTP 状态 404 (未找到页面) 并向浏览器发送文字 "404: Not found"
}
