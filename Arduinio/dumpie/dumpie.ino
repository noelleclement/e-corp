#include <Keypad.h>
#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 10
#define RST_PIN 5
MFRC522 mfrc522(SS_PIN, RST_PIN);


const byte ROWS = 4; //four rows
const byte COLS = 4; //four columns
char keys[ROWS][COLS] = {
    {'1','2','3','A'},
    {'4','5','6','B'},
    {'7','8','9','C'},
    {'*','0','#','D'}
};
byte rowPins[ROWS] = {1,8,7,6};//row pinouts of keypad
byte colPins[COLS] = {5,4,3,2}; //column pinouts of keypad

Keypad keypad = Keypad (makeKeymap(keys), rowPins, colPins, ROWS, COLS);

void setup() {
 Serial.begin(9600);
 SPI.begin();
 mfrc522.PCD_Init();
 Serial.println(" Scannen maar");
}

void loop() {

  char key = keypad.getKey();

  if(key){
    Serial.println(key);
  }

  if ( ! mfrc522.PICC_IsNewCardPresent()){
        return;
  }

  if ( ! mfrc522.PICC_ReadCardSerial()){
        return;
  }

  mfrc522.PICC_DumpToSerial(&(mfrc522.uid));
}
